package com.siberhus.ngaiready.admin.action;

import java.util.Set;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.guardian.Guardian;
import com.siberhus.ngai.guardian.model.AbstractPermission;
import com.siberhus.ngai.guardian.model.RolePermission;
import com.siberhus.ngai.guardian.service.IRolePermissionService;
import com.siberhus.ngai.guardian.service.impl.RolePermissionService;
import com.siberhus.ngai.service.ICrudService;

@UrlBinding("/action/admin/rolePermission")
public class RolePermissionAction extends BasePermissionAction<RolePermission>{
	
	@InjectService(implementation=RolePermissionService.class)
	private IRolePermissionService rolePermService;
	
	private String roleName;
	
	@Override
	public Class<RolePermission> getModelClass() {
		return RolePermission.class;
	}
	
	@Override
	public String getEditPage() {
		return "/role-perm-edit.jsp";
	}

	@Override
	public String getIndexPage() {
		return "/role-perm-list.jsp";
	}

	@Override
	public String getViewPage() {
		return "/role-perm-view.jsp";
	}

	@Override
	public QTO createQueryTransferObjectForSearch() {
		
		QTO qto = new QTO("from RolePermission rp where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getRoleId()!=null){
			qto.appendQuery("and rp.roleId=?");
			qto.addParameter(getModel().getRoleId());
		}
		if(getModel().getActionUri()!=null){
			qto.appendQuery("and rp.actionUri like ?");
			qto.addParameter(getModel().getActionUri());
		}
		if(getSearchEventName()!=null){
			qto.appendQuery("and rp.eventNames like ?");
			qto.addParameter(getSearchEventName());
		}
		return qto;
	}
	
	@Override
	public ICrudService<RolePermission, Long> getCrudService() {
		return rolePermService;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Override
	public Resolution save(){
		boolean isAllEvents = AbstractPermission.ALL_EVENTS.equals(
				getContext().getRequest().getParameter("eventNames"));
		if(isAllEvents){
			getModel().setEventNames(AbstractPermission.ALL_EVENTS);
		}
		
		Resolution resolution = (Resolution)super.save();
		
		if(resolution instanceof RedirectResolution ){
			RedirectResolution redir = (RedirectResolution)resolution;
			redir.addParameter("roleName", getRoleName());
			redir.addParameter("model.roleId", getModel().getRoleId());
		}
		return resolution;
	}

	@Before(on="!view")
	protected Resolution checkRequiredFields(){
		if(getRoleName()==null 
				|| getModel()!=null && getModel().getRoleId()==null){
			return redirect(RoleAction.class);
		}
		Set<String> actionUriSet = Guardian.getInstance()
			.getActionBeanDetailMap().keySet();
		getContext().getRequest().setAttribute("actionUriSet", actionUriSet);
		return null;
	}
	
}
