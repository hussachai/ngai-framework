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
import com.siberhus.ngai.guardian.model.UserPermission;
import com.siberhus.ngai.guardian.service.IUserPermissionService;
import com.siberhus.ngai.guardian.service.impl.UserPermissionService;
import com.siberhus.ngai.service.ICrudService;

@UrlBinding("/action/admin/userPermission")
public class UserPermissionAction extends BasePermissionAction<UserPermission>{

	@InjectService(implementation=UserPermissionService.class)
	private IUserPermissionService userPermService;
	
	private String username;
	
	@Override
	public Class<UserPermission> getModelClass() {
		return UserPermission.class;
	}
	
	@Override
	public String getEditPage() {
		return "/user-perm-edit.jsp";
	}

	@Override
	public String getIndexPage() {
		return "/user-perm-list.jsp";
	}

	@Override
	public String getViewPage() {
		return "/user-perm-view.jsp";
	}

	@Override
	public QTO createQueryTransferObjectForSearch() {
		
		QTO qto = new QTO("from UserPermission up where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getUserId()!=null){
			qto.appendQuery("and up.userId=?");
			qto.addParameter(getModel().getUserId());
		}
		if(getModel().getActionUri()!=null){
			qto.appendQuery("and up.actionUri like ?");
			qto.addParameter(getModel().getActionUri());
		}
		if(getSearchEventName()!=null){
			qto.appendQuery("and up.eventNames like ?");
			qto.addParameter(getSearchEventName());
		}
		return qto;
	}
	
	@Override
	public ICrudService<UserPermission, Long> getCrudService() {
		return userPermService;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
			redir.addParameter("username", getUsername());
			redir.addParameter("model.userId", getModel().getUserId());
		}
		return resolution;
	}
	
	@Before(on="!view")
	protected Resolution checkRequiredFields(){
		if(getUsername()==null 
				|| getModel()!=null && getModel().getUserId()==null){
			return redirect(UserAction.class);
		}
		Set<String> actionUriSet = Guardian.getInstance()
			.getActionBeanDetailMap().keySet();
		getContext().getRequest().setAttribute("actionUriSet", actionUriSet);
		return null;
	}
}
