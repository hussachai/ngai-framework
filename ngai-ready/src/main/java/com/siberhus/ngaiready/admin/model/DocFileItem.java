package com.siberhus.ngaiready.admin.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.model.AbstractModel;

@Entity
@Table(name="FILE_ITEMS")
public class DocFileItem extends AbstractModel<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="FILE_NAME", nullable=false)
	private String name;
	
	@Column(name="FILE_PATH", nullable=false)
	private String path;
	
	@Column(name="IS_DIRECTORY")
	private boolean directory;
	
	@ManyToOne
	@JoinColumn(name="FILE_ITEM_ID", referencedColumnName="ID")
	private DocFileItem parent;
	
	@Column(name="FILE_SIZE")
	private Long size;
	
	@OneToMany
	@JoinTable(name="FILE_ITEM_ROLES", 
			joinColumns=@JoinColumn(name="FILE_ITEM_ID",referencedColumnName="ID"),
			inverseJoinColumns=@JoinColumn(name="ROLE_ID",referencedColumnName="ID"))
	private Set<Role> roleSet = new HashSet<Role>();
	
	@ManyToOne
	@JoinColumn(name="USER_ID", referencedColumnName="ID", nullable=false)
	private User owner;
	
	@ManyToOne
	@JoinColumn(name="FILE_TYPE_ID", referencedColumnName="ID")
	private DocFileType type;
	
	@Column(name="IS_PUBLIC_FILE")
	private boolean publicFile;
	
	@Column(name="DESCRIPTION")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public DocFileItem getParent() {
		return parent;
	}

	public void setParent(DocFileItem parent) {
		this.parent = parent;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	
	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public DocFileType getType() {
		return type;
	}

	public void setType(DocFileType type) {
		this.type = type;
	}

	public boolean isPublicFile() {
		return publicFile;
	}

	public void setPublicFile(boolean publicFile) {
		this.publicFile = publicFile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
