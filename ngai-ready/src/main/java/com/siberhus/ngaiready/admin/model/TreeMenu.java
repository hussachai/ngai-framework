package com.siberhus.ngaiready.admin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.model.AbstractModel;

@NamedQueries({
	@NamedQuery(name="TreeMenu.getAllNoParentMenus", 
			query="from TreeMenu tm where tm.parent is null order by tm.position asc"),
	@NamedQuery(name="TreeMenu.findByLinkUrl", query="from TreeMenu tm where tm.linkUrl=?"),
	@NamedQuery(name="TreeMenu.findByParentIdAndPosition", query="from TreeMenu tm where tm.parent.id=? and tm.position=?")
})

@Entity
@Table(name="TREE_MENUES")
public class TreeMenu extends AbstractModel<Long>{
	
	public static enum Status {
		ENABLED, DISABLED, HIDDEN
	}
	
	public static enum LinkMethod {
		POST, GET
	}
	
	public static enum LinkType {
		JAVASCRIPT, HREF
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS", nullable=false)
	private Status status = Status.ENABLED;
	
	@ManyToOne
	@JoinColumn(name="PARENT_MENU_ID", referencedColumnName="ID")
	private TreeMenu parent;
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.REMOVE)
	@OrderBy("position asc")
	private List<TreeMenu> children = new ArrayList<TreeMenu>();
	
	@Column(name="MENU_POSITION")
	private Integer position = new Integer(0);
	
	@ManyToMany
	@JoinTable(name="TREE_MENU_ROLES", 
			joinColumns=@JoinColumn(name="TREE_MENU_ID", referencedColumnName="ID"),
			inverseJoinColumns=@JoinColumn(name="ROLE_ID", referencedColumnName="ID"))
	private Set<Role> roleSet = new TreeSet<Role>();
	
	@Column(name="MENU_LABEL" ,nullable=false)
	private String label;
	
	@Column(name="MENU_LABEL_KEY")
	private String labelKey;
	
	@Column(name="LINK_URL")
	private String linkUrl;
	
	@Column(name="LINK_TYPE", nullable=false)
	private LinkType linkType;
	
	@Column(name="LINK_METHOD")
	private LinkMethod linkMethod;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="DESCRIPTION_KEY")
	private String descriptionKey;
	
	public TreeMenu(){}
	
	public TreeMenu(Long id, String label, String linkUrl){
		setId(id);
		this.label = label;
		this.linkUrl = linkUrl;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public TreeMenu getParent() {
		return parent;
	}

	public void setParent(TreeMenu parent) {
		this.parent = parent;
	}
	
	public List<TreeMenu> getChildren() {
		return children;
	}

	public void setChildren(List<TreeMenu> children) {
		this.children = children;
	}
	
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public LinkMethod getLinkMethod() {
		return linkMethod;
	}

	public void setLinkMethod(LinkMethod linkMethod) {
		this.linkMethod = linkMethod;
	}

	public LinkType getLinkType() {
		return linkType;
	}
	
	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}

	public LinkMethod getRequestMethod() {
		return linkMethod;
	}

	public void setRequestMethod(LinkMethod requestMethod) {
		this.linkMethod = requestMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescriptionKey() {
		return descriptionKey;
	}

	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}
	
}
