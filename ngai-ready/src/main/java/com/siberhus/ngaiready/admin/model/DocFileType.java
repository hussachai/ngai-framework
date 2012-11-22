package com.siberhus.ngaiready.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.siberhus.ngai.model.AbstractModel;

@Entity
@Table(name="FILE_TYPES")
public class DocFileType extends AbstractModel<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="FILE_EXTENSION", nullable=false)
	private String extension;
	
	@Column(name="DISPLAY_NAME", nullable=false)
	private String displayName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Override
	public String toString(){
		return displayName;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
