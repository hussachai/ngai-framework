package com.siberhus.ngai.localization.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.siberhus.ngai.model.AbstractModel;

@NamedQueries({
	@NamedQuery(name="ResourceBundle.getDefaultMessages"
		,query="from ResourceBundle rb where rb.type='MESSAGE' and rb.language is null"),
	@NamedQuery(name="ResourceBundle.findByTypeAndLanguage"
		,query="from ResourceBundle rb where rb.type=? and rb.language=?"),
	@NamedQuery(name="ResourceBundle.getResourceBundle"
		,query="from ResourceBundle rb where rb.type=? and rb.language=? and rb.actionBeanName=? and rb.key=?"),
	@NamedQuery(name="ResourceBundle.getGlobalResourceBundle"
		,query="from ResourceBundle rb where rb.type=? and rb.language=? and rb.actionBeanName is null and rb.key=?")
})
@Entity(name="ResourceBundle")
@Table(name="RESOURCE_BUNDLES",
		uniqueConstraints=@UniqueConstraint(columnNames={"LANGUAGE","ACTION_BEAN_NAME","RESOURCE_KEY","RESOURCE_TYPE"}))
public class NgaiResourceBundle extends AbstractModel<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@Column(name="RESOURCE_TYPE", nullable=false)
	private ResourceType type;
	
	@Column(name="LANGUAGE")
	private String language;
	
	@Column(name="ACTION_BEAN_NAME")
	private String actionBeanName;
	
	@Column(name="RESOURCE_KEY", nullable=false)
	private String key;
	
	@Column(name="RESOURCE_VALUE", nullable=false)
	private String value;

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getActionBeanName() {
		return actionBeanName;
	}

	public void setActionBeanName(String actionBeanName) {
		this.actionBeanName = actionBeanName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
