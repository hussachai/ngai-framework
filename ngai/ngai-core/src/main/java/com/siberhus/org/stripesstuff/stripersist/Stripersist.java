/* Copyright 2008 Aaron Porter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siberhus.org.stripesstuff.stripersist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.MappedSuperclass;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Stripersist provides a simple way to use <a
 * href="http://java.sun.com/developer/technicalArticles/J2EE/jpa/">JPA</a> in
 * <a href="http://www.stripesframework.org">Stripes</a> applications. It does
 * this by providing Stripes with an EntityFormatter and EntityTypeConverter.
 * The EntityFormatter finds the primary key for the entity and returns its
 * value. The EntityTypeConverter takes a primary key value and retrieves the
 * corresponding entity from the database.
 * </p>
 * <p>
 * To start using Stripersist with Stripes you need to do the following:
 * <ol>
 * <li>Set up a JPA EntityManager. Your <code>persistence.xml</code> file should
 * go in <code>WEB-INF/classes/META-INF/</code>. Instructions on how to do that
 * are beyond the scope of this documentation but you can find more info in
 * Sample Chapter 2 of <a href="http://www.manning.com/bauer2/">Java Persistence
 * with Hibernate</a>. The relevant information starts on page 68 of the PDF.</li>
 * <li>Add the Stripersist jar file to you WEB-INF/lib directory.</li>
 * <li>Add <code>com.stripersist</code> to the <code>Extension.Packages</code>
 * parameter of your StripesFilter in web.xml</li>
 * </ol>
 * </p>
 * <p>
 * That's it! Now you should be able to use your entities as properties of your
 * ActionBean and Stripes will handle the binding for you.
 * </p>
 * 
 * @author Aaron Porter
 * 
 */
@Intercepts( { LifecycleStage.RequestInit, LifecycleStage.RequestComplete })
public class Stripersist implements Interceptor, ConfigurableComponent {
	/**
	 * Parameter name for specifying StripersistInit classes in web.xml. This is
	 * optional; StripersistInit classes are also loaded via the
	 * Extension.Packages.
	 */
	// public static final String INIT_CLASSES_PARAM_NAME =
	// "StripersistInit.Classes";
	private static final Log log = Log.getInstance(Stripersist.class);

	static private ThreadLocal<Map<EntityManagerFactory, EntityManager>> threadEntityManagers = new ThreadLocal<Map<EntityManagerFactory, EntityManager>>();
	static private Map<String, EntityManagerFactory> entityManagerFactories;
	static private Map<Class<?>, EntityManagerFactory> entityManagerFactoryLookup = new ConcurrentHashMap<Class<?>, EntityManagerFactory>();

	/**
	 * Called by Stripes during initialization.
	 */
	public void init(Configuration configuration) throws Exception {
		//I moved the code in this block to NgaiConfiguration.init(Configuration)
	}

	/**
	 * Initialize Stripersist, pulling persistent unit names from the specified
	 * URL.
	 * 
	 * @param xml
	 *            a URL pointing to persistence.xml
	 */
	public void init(URL xml) throws Exception {
		log.debug("Initializing Stripersist using JPA persistence file.");
		
		try {
			entityManagerFactories = new ConcurrentHashMap<String, EntityManagerFactory>();

			Document document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(xml.openStream());
			NodeList nodeList = document
					.getElementsByTagName("persistence-unit");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node persistenceUnit = nodeList.item(i);

				String name = persistenceUnit.getAttributes().getNamedItem(
						"name").getNodeValue();
				log.debug("Creating EntityManagerFactory for persistence unit \"",name, "\"");

				EntityManagerFactory factory = Persistence
						.createEntityManagerFactory(name);

				entityManagerFactories.put(name, factory);
				
				NodeList children = persistenceUnit.getChildNodes();

				for (int j = 0; j < children.getLength(); j++) {
					Node child = children.item(j);

					if ("class".equalsIgnoreCase(child.getNodeName())) {
						String className = child.getFirstChild().getNodeValue();

						try {
							Class<?> clazz = Class.forName(className);

							log.debug("Associating ", className," with persistence unit \"", name, "\"");

							entityManagerFactoryLookup.put(clazz, factory);
						} catch (Exception e) {
							log.error(e, "Exception occurred while loading ",className);
						}
					} else if ("jar-file".equalsIgnoreCase(child.getNodeName())) {
						String jarFile = child.getFirstChild().getNodeValue();

						if (jarFile.startsWith("../../lib/"))
							jarFile = jarFile.substring(10);
						
						for (Class<?> clazz : findEntities(jarFile)) {
							log.debug("Associating ", clazz.getName(),
									" with persistence unit \"", name, "\"");

							entityManagerFactoryLookup.put(clazz, factory);
						}
					}
				}
			}

			if ((entityManagerFactories.size() == 1)
					&& (entityManagerFactoryLookup.size() == 0)) {
				EntityManagerFactory factory = entityManagerFactories.values()
						.iterator().next();
				String name = entityManagerFactories.keySet().iterator().next();

				for (Class<?> clazz : findEntities(null)) {
					log.debug("Associating ", clazz.getName(),
							" with persistence unit \"", name, "\"");
					entityManagerFactoryLookup.put(clazz, factory);
				}
			}
		} catch (Exception e) {
//			log.error(e);
			throw e;
		}
	}

	/**
	 * Finds and returns all classes that are annotated with {@link Entity} or
	 * {@link MappedSuperclass}. This code was taken from Stripes
	 * {@link net.sourceforge.stripes.util.ResolverUtil} and modified to suit
	 * our needs.
	 * 
	 * @param jarName
	 * @return a set of entity classes
	 */
	protected static Set<Class<?>> findEntities(String jarName) {
		URLClassLoader loader = (URLClassLoader) Thread.currentThread()
				.getContextClassLoader();

		URL[] urls = loader.getURLs();

		Set<Class<?>> classes = new HashSet<Class<?>>();

		for (URL url : urls) {
			try {
				String urlPath = url.getFile();
				urlPath = URLDecoder.decode(urlPath, "UTF-8");
				if (jarName != null && !urlPath.endsWith(jarName))
					continue;
				// If it's a file in a directory, trim the stupid file: spec
				if (urlPath.startsWith("file:")) {
					urlPath = urlPath.substring(5);
				}

				File file = new File(urlPath);

				if (jarName == null && file.isFile())
					continue;

				log.debug("Scanning for entities in [", urlPath, "]");
				if (file.isDirectory()) {
					classes.addAll(findEntitiesInDirectory("", file));
				} else {
					classes.addAll(findEntitiesInJar(file));
				}

			} catch (Exception e) {
				log.error(e);
			}
		}

		return classes;
	}

	/**
	 * Returns a set of classes that are annotated with {@link Entity} or
	 * {@link MappedSuperclass} in the specified jar file.
	 * 
	 * @param file
	 * @return a set of entity classes
	 */
	private static Set<? extends Class<?>> findEntitiesInJar(File file) {
		try {
			JarEntry entry;
			JarInputStream jarStream = new JarInputStream(new FileInputStream(
					file));

			Set<Class<?>> classes = new HashSet<Class<?>>();

			while ((entry = jarStream.getNextJarEntry()) != null) {
				String name = entry.getName();
				if (!entry.isDirectory() && name.endsWith(".class")) {
					addIfEntity(classes, name);
				}
			}

			return classes;
		} catch (IOException ioe) {
			log.error("Could not search jar file '", file,
					"' for entities due to an IOException: ", ioe.getMessage());
		}

		return null;
	}

	/**
	 * Returns a set of classes that are annotated with {@link Entity} or
	 * {@link MappedSuperclass} in the specified directory.
	 * 
	 * @param parent
	 * @param location
	 * @return a set of entities
	 */
	private static Set<? extends Class<?>> findEntitiesInDirectory(
			String parent, File location) {
		File[] files = location.listFiles();
		StringBuilder builder = null;

		// File.listFiles() can return null when an IO error occurs!
		if (files == null) {
			log.warn("Could not list directory " + location.getAbsolutePath()
					+ " when looking for entities");
			return null;
		}

		Set<Class<?>> classes = new HashSet<Class<?>>();

		for (File file : files) {
			builder = new StringBuilder(100);
			if (parent != null && parent.length() > 0)
				builder.append(parent).append("/");
			builder.append(file.getName());
			String packageOrClass = (parent == null ? file.getName() : builder.toString());

			if (file.isDirectory()) {
				classes.addAll(findEntitiesInDirectory(packageOrClass, file));
			} else if (file.getName().endsWith(".class")) {
				addIfEntity(classes, packageOrClass);
			}
		}

		return classes;
	}

	/**
	 * If fqn describes a class annotated with @Entity it will be added to the
	 * classes @Set otherwise it is ignored.
	 * 
	 * @param classes
	 *            the set that entity classes will be added to
	 * @param fqn
	 *            the fully qualified class name to check
	 */
	private static void addIfEntity(Set<Class<?>> classes, String fqn) {
		try {
			String externalName = fqn.substring(0, fqn.indexOf('.')).replace(
					'/', '.');

			Class<?> type = Thread.currentThread().getContextClassLoader()
					.loadClass(externalName);
			if (type.getAnnotation(Entity.class) != null
					|| type.getAnnotation(MappedSuperclass.class) != null) {
				classes.add(type);
			}
		} catch (NoClassDefFoundError e) {
			// Ignored
		} catch (Throwable t) {
			log.debug("Could not examine class '", fqn, "'", " due to a ", t
					.getClass().getName(), " with message: ", t.getMessage());
		}
	}

	/**
	 * Shutdown the EntityManagerFactories so they can release resources
	 */
	@Override
	protected void finalize() throws Throwable {
		for (EntityManagerFactory factory : entityManagerFactoryLookup.values()) {
			if (factory.isOpen())
				factory.close();
		}
	}

	/**
	 * Finds the EntityManagerFactory which is associated with the specified
	 * persistence unit. Normally you shouldn't use this class because
	 * Stripersist won't clean up any EntityManagers that you create.
	 * 
	 * @param persistenceUnit
	 *            the name of the persistence unit
	 * @return an EntityManagerFactory or null
	 */
	public static EntityManagerFactory getEntityManagerFactory(
			String persistenceUnit) {
		return entityManagerFactories.get(persistenceUnit);
	}

	/**
	 * Finds the EntityManagerFactory which is associated with the specified
	 * class. Normally you shouldn't use this class because Stripersist won't
	 * clean up any EntityManagers that you create.
	 * 
	 * @param forType
	 *            a class that the EntityManagerFactory knows how to handle
	 * @return an EntityManagerFactory
	 */
	public static EntityManagerFactory getEntityManagerFactory(Class<?> forType) {
		return entityManagerFactoryLookup.get(forType);
	}

	/**
	 * Gets an EntityManager from the specified factory.
	 * 
	 * @param factory
	 * @return an EntityManager or null
	 */
	private static EntityManager getEntityManager(EntityManagerFactory factory) {
		Map<EntityManagerFactory, EntityManager> map = threadEntityManagers
				.get();

		EntityManager entityManager = null;

		if (map == null) {
			StripesRuntimeException sre = new StripesRuntimeException(
					"It looks like Stripersist isn't configured as an Interceptor\n"
							+ "or you're calling Stripersist from a thread outside of the\n"
							+ "StripesFilter. If you want use Stripersist from outside\n"
							+ "of Stripes you should call Stripersist.initRequest() inside\n"
							+ "of a try block before requesting an EntityManager and\n"
							+ "call Stripersist.requestComplete() in a finally block so\n"
							+ "Stripersist can clean everything up for you.");

			log.error(sre);
			
			return null;
		}

		entityManager = map.get(factory);

		if (entityManager == null) {
			entityManager = factory.createEntityManager();
			map.put(factory, entityManager);
		}

		// EntityTransaction transaction = entityManager.getTransaction();
		//		
		// if (!transaction.isActive())
		// transaction.begin();

		return entityManager;
	}

	/**
	 * If Stripersist only knows about one EntityManager this is a convenient
	 * way to retrieve it. Keep in mind that if you use this and later decide to
	 * add another persistence unit you will have to go back and fix every call
	 * to getEntityManager() in your application.
	 * 
	 * @return an @EntityManager
	 */
	public static EntityManager getEntityManager() {
		if (entityManagerFactories.size() != 1) {
			StripesRuntimeException sre = new StripesRuntimeException(
					"In order to call Stripersist.getEntityManager() without "
							+ "any parameters there must be exactly one persistence "
							+ "unit defined.");

			log.error(sre);

			return null;
		}

		return getEntityManager(entityManagerFactories.values().iterator()
				.next());
	}

	/**
	 * Retrieves the EntityManager associated with the named persistence unit.
	 * 
	 * @param persistenceUnit
	 *            the name of the persistence unit
	 * @return an EntityManager or null
	 */
	public static EntityManager getEntityManager(String persistenceUnit) {
		EntityManagerFactory factory = getEntityManagerFactory(persistenceUnit);

		if (factory == null) {
			log.warn(
					"Couldn't find EntityManagerFactory for persistence unit ",
					persistenceUnit);
			return null;
		}

		return getEntityManager(factory);
	}

	/**
	 * Retrieves an EntityManager that may be used with the specified type.
	 * 
	 * @param forType
	 *            a class that is handled by the EntityManager
	 * @return an EntityManager or null
	 */
	public static EntityManager getEntityManager(Class<?> forType) {
		log.debug("Looking up EntityManager for type ", forType.getName());

		EntityManagerFactory entityManagerFactory = getEntityManagerFactory(forType);

		if (entityManagerFactory == null) {
			log.warn("Couldn't find EntityManagerFactory for class ", forType.getName());
			return null;
		}

		return getEntityManager(entityManagerFactory);
	}

	/**
	 * Initializes request specific variables. Under normal circumstances this
	 * is called automatically but if you want to use Stripersist from your own
	 * threads you may call this as long as you remember to call
	 * {@link #requestComplete()} when you are done (preferably from inside a
	 * finally block).
	 */
	public static void requestInit() {
		Map<EntityManagerFactory, EntityManager> map = threadEntityManagers.get();
		
		if (map == null) {
			map = new ConcurrentHashMap<EntityManagerFactory, EntityManager>();
			threadEntityManagers.set(map);
		}
	}

	/**
	 * Rolls back current {@link EntityTransaction}s and closes
	 * {@link EntityManager}s. Under normal circumstances this is called
	 * automatically but if you've called {@link #requestInit()} from within
	 * your own thread you should make sure this is in a finally block so
	 * everything gets cleaned up.
	 */
	public static void requestComplete() {
		Map<EntityManagerFactory, EntityManager> map = Stripersist.threadEntityManagers.get();
		
		if (map == null) {
			// looks like nobody needed us this time
			return;
		}

		log.trace("Cleaning up EntityManagers");

		Stripersist.threadEntityManagers.remove();

		for (EntityManager entityManager : map.values()) {
			EntityTransaction transaction = entityManager.getTransaction();

			if (transaction != null) {
				if (transaction.isActive())
					transaction.rollback();
				if (entityManager.isOpen())
					entityManager.close();
			}
		}
	}

	/**
	 * Called by Stripe's {@link Interceptor} system. We use it to perform
	 * initialization and cleanup at the start and end of each request
	 * respectively.
	 */
	public Resolution intercept(ExecutionContext context) throws Exception {
		switch (context.getLifecycleStage()) {
		case RequestInit:
			log.trace("RequestInit");
			requestInit();
			break;
		case RequestComplete:
			log.trace("RequestComplete");
			requestComplete();
			break;
		}

		return context.proceed();
	}

}
