package com.siberhus.ngai.guardian.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.ObjectUtils;

import com.siberhus.ngai.guardian.config.GuardianConfig;
import com.siberhus.ngai.guardian.exception.GuardianException;
import com.siberhus.ngai.model.AbstractAuditableModel;
import com.siberhus.ngai.model.IUnremovableModel;
import com.siberhus.ngai.util.DefaultDataFormat;

@NamedQueries( { @NamedQuery(name = "User.findByUsername", query = "from User u where u.username=?") })
@Entity
@Table(name = "USERS")
public class User extends AbstractAuditableModel<Long,User> implements IUnremovableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "USERNAME", nullable = false, unique = true)
	private String username;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "ALIAS")
	private String alias;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "CONTACT_NUMBER")
	private String contactNumber;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
	private Set<Role> roleSet = new HashSet<Role>();

	@Column(name = "AUDIT_TRAILS_ENABLED")
	private Boolean auditTrailEnabled = Boolean.FALSE;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name="IS_ADMIN")
	private Boolean admin = Boolean.FALSE;
	
	@Column(name = "IS_ACTIVE")
	private Boolean active = Boolean.TRUE;

	@Column(name = "PASSWORD_AGE")
	private Integer passwordAge;

	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_DATE_START")
	private Date validDateStart;

	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_DATE_END")
	private Date validDateEnd;

	@Override
	public String[] toNameKeys() {
		return new String[] { "username", "password", "firstName", "lastName",
				"alias", "email", "contactNumber", "roles", "description",
				"active", "audit","passwordAge","validDateStart","validDateEnd" };
	}

	@Override
	public String[] toNames() {
		return new String[] { "Username", "Password", "First Name",
				"Last Name", "Alias", "Email", "Contact Number", "Roles",
				"Description", "Active", "Audit",
				"Password Age","Valid Date Start","Valid Date End" };
	}
	
	@Override
	public String[] toValues() {
		return new String[] { username, password, firstName, lastName, alias,
				email, contactNumber, roleSet.toString(), description,
				ObjectUtils.toString(active),
				ObjectUtils.toString(auditTrailEnabled),
				ObjectUtils.toString(passwordAge),
				DefaultDataFormat.formatDate(validDateStart),
				DefaultDataFormat.formatDate(validDateEnd),
			};
	}
	
	// Bug
	// @PrePersist
	// protected void checkRootUsernameChange() {
	// if (GuardianConfig.get().isRootAccessEnabled()) {
	// if (!GuardianConfig.get().getRootUsername().equals(getUsername())) {
	// throw new GuardianException("Root user cannot change username."
	// + " To change root's username you must change "
	// + GuardianConfig.ROOT_USERNAME + " property"
	// + " and alter root record in database directly.");
	// }
	// }
	// }

	@PreRemove
	protected void checkRootUserDeletion() {
		if (GuardianConfig.get().isRootAccessEnabled()) {
			if (GuardianConfig.get().getRootUsername().equals(getUsername())) {
				throw new GuardianException("Cannot remove root user because "
						+ GuardianConfig.ROOT_ACCESS_ENABLED + " is 'true'");
			}
		}
	}

	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	@Override
	public String toString() {
		return username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	@Override
	public Boolean getActive() {
		return active;
	}

	@Override
	public void setActive(Boolean isActive) {
		this.active = isActive;
	}

	public Boolean getAuditTrailEnabled() {
		return auditTrailEnabled;
	}

	public void setAuditTrailEnabled(Boolean auditTrailEnabled) {
		this.auditTrailEnabled = auditTrailEnabled;
	}

	public Integer getPasswordAge() {
		return passwordAge;
	}

	public void setPasswordAge(Integer passwordAge) {
		this.passwordAge = passwordAge;
	}

	public Date getValidDateStart() {
		return validDateStart;
	}

	public void setValidDateStart(Date validDateStart) {
		this.validDateStart = validDateStart;
	}

	public Date getValidDateEnd() {
		return validDateEnd;
	}

	public void setValidDateEnd(Date validDateEnd) {
		this.validDateEnd = validDateEnd;
	}

}
