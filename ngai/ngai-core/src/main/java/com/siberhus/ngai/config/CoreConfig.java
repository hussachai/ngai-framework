package com.siberhus.ngai.config;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.properties.ConfigurationException;
import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.exception.NgaiException;

public class CoreConfig {
	
	public static final String PREFIX = "core.";
	public static final String DEFAULT_PERSISTENCE_UNIT = PREFIX+"defaultPersistenceUnit";
	public static final String APP_VARIABLE_NAME = PREFIX+"appVariableName";
	public static final String MODE = PREFIX+"mode";
	public static final String ERROR_PAGE = PREFIX+"errorPage";
	/**
	 * This property is available in unmanaged-transaction environment (LOCAL_RESOURCE)
	 */
	public static final String AUTO_BEGIN_TRANSACTION = PREFIX+"autoBeginTransaction";
	/**
	 * This property is available in unmanaged-transaction environment (LOCAL_RESOURCE)
	 */
	public static final String AUTO_COMMIT_TRANSACTION = PREFIX+"autoCommitTransaction";
	public static final String LOCALE_COUNTRY = PREFIX+"locale.country";
	public static final String LOCALE_LANGUAGE = PREFIX+"locale.language";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreConfig.class);
	
	private String defaultPersistenceUnit;
	private String appVariableName = "app";
	private String mode = "dev";
	private String errorPage;
	private boolean autoBeginTransaction;
	private boolean autoCommitTransaction;
	private Locale locale;
	
	private Map<String, String> formatPattern = new HashMap<String, String>();
	
	public CoreConfig(StringMap ngaiProps) throws NgaiException {
		
		LOGGER.debug("Reading Ngai configuration");
		defaultPersistenceUnit = ngaiProps.getString(DEFAULT_PERSISTENCE_UNIT);
		if(defaultPersistenceUnit==null){
			throw new NgaiException(DEFAULT_PERSISTENCE_UNIT
				+ " parameter is required\n"
				+ "To solve this problem: simply put something like this into StripesFilter section\n"
				+ "<init-param>\n"
				+ "\t<param-name>Ngai.Properties</param-name>\n"
				+ "\t<param-value>\n"
				+ "\t\t"+DEFAULT_PERSISTENCE_UNIT+"=[obtain this value from persistence.xml]"
				+ "\t</param-value>\n"
				+ "</init-param>\n");
		}
		
		appVariableName = ngaiProps.getString(APP_VARIABLE_NAME, appVariableName);
		String modes[] = new String[]{"dev","prod","test"};
		mode = ngaiProps.getString(MODE, mode);
		if(!ArrayUtils.contains(modes, mode)){
			throw new ConfigurationException("Unknown mode: "+mode
					+", mode must be one of "+Arrays.toString(modes));
		}
		errorPage = ngaiProps.getString(ERROR_PAGE,"/error.jsp");
		String language = ngaiProps.getString(LOCALE_LANGUAGE, "en");
		String country = ngaiProps.getString(LOCALE_COUNTRY, "US");
		locale = new Locale(language, country);
		
		formatPattern.put("java.util.Date", ngaiProps
				.getString("pattern.java.util.Date","dd/MM/yyyy"));
		formatPattern.put("java.sql.Date", ngaiProps
				.getString("pattern.java.sql.Date","dd/MM/yyyy"));
		formatPattern.put("java.sql.Timestamp", ngaiProps
				.getString("pattern.java.sql.Timestamp","dd/MM/yyyy HH:mm:ss"));
		formatPattern.put("java.sql.Time", ngaiProps
				.getString("pattern.java.sql.Time","HH:mm:ss"));
		formatPattern.put("java.lang.Integer", ngaiProps
				.getString("pattern.java.lang.Integer","###"));
		formatPattern.put("java.lang.Long", ngaiProps
				.getString("pattern.java.lang.Long","###"));
		formatPattern.put("java.lang.Short", ngaiProps
				.getString("pattern.java.lang.Short","###"));
		formatPattern.put("java.lang.Float", ngaiProps
				.getString("pattern.java.lang.Float","###.###"));
		formatPattern.put("java.lang.Double", ngaiProps
				.getString("pattern.java.lang.Double","###.###"));
		formatPattern.put("java.math.BigInteger", ngaiProps
				.getString("pattern.java.math.BigInteger","###"));
		formatPattern.put("java.math.BigDecimal", ngaiProps
				.getString("pattern.java.math.BigDecimal","###,###.###"));
		//
		formatPattern.put("date", formatPattern.get("java.util.Date"));
		formatPattern.put("time", formatPattern.get("java.sql.Time"));
		formatPattern.put("timestamp", formatPattern.get("java.sql.Timestamp"));
		formatPattern.put("int", formatPattern.get("java.lang.Integer"));
		formatPattern.put("long", formatPattern.get("java.lang.Long"));
		formatPattern.put("short", formatPattern.get("java.lang.Short"));
		formatPattern.put("float", formatPattern.get("java.lang.Float"));
		formatPattern.put("double", formatPattern.get("java.lang.Double"));
		
		autoBeginTransaction = ngaiProps.get(Boolean.class, AUTO_BEGIN_TRANSACTION, false);
		
		autoCommitTransaction = ngaiProps.get(Boolean.class, AUTO_COMMIT_TRANSACTION, false);
		
	}
	
	public static CoreConfig get(){
		return Ngai.getInstance().getConfiguration();
	}
	
	public String getDefaultPersistenceUnit() {
		return defaultPersistenceUnit;
	}
	
	public String getAppVariableName() {
		return appVariableName;
	}

	public String getMode() {
		return mode;
	}
	
	public String getErrorPage(){
		return errorPage;
	}
	
	public boolean isAutoBeginTransaction() {
		return autoBeginTransaction;
	}

	public boolean isAutoCommitTransaction() {
		return autoCommitTransaction;
	}

	public Locale getLocale() {
		return locale;
	}

	public Map<String, String> getFormatPattern() {
		return formatPattern;
	}

	public static void main(String[] args) {
		System.out.println(new DecimalFormat("###,###.##").format(3424323234.336));
	}
	
}
