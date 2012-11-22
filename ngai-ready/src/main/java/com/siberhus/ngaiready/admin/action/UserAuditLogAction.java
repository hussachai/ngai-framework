package com.siberhus.ngaiready.admin.action;

import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.guardian.model.UserAuditLog;
import com.siberhus.ngai.guardian.service.IUserAuditLogService;
import com.siberhus.ngai.guardian.service.impl.UserAuditLogService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.action.BaseCrudAction;

@UrlBinding("/action/admin/userAuditLog")
public class UserAuditLogAction extends BaseCrudAction<UserAuditLog, Long> {
	
	@InjectService(implementation=UserAuditLogService.class)
	private IUserAuditLogService userAuditLogService;
	
	private UserAuditLog model;
	
	@Override
	public UserAuditLog getModel(){
		return model;
	}
	
	@Override
	public void setModel(UserAuditLog model){
		this.model = model;
	}
	
	@Override
	public Class<UserAuditLog> getModelClass(){
		return UserAuditLog.class;
	}
	
	@Override
	public ICrudService<UserAuditLog, Long> getCrudService() {
		
		return userAuditLogService;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/user-audit-log";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from UserAuditLog ual where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getUsername()!=null){
			qto.appendQuery("and ual.username like ? ");
			qto.addParameter(getModel().getUsername());
		}
		if(getModel().getActionUri()!=null){
			qto.appendQuery("and ual.actionUri like ? ");
			qto.addParameter(getModel().getActionUri());
		}
		if(getModel().getEventName()!=null){
			qto.appendQuery("and ual.eventName like ? ");
			qto.addParameter(getModel().getEventName());
		}
		if(getModel().getExecutedAt()!=null){
			qto.appendQuery("and ual.executedAt = ? ");
			qto.addParameter(getModel().getExecutedAt());
		}
		if(getModel().getForbiddenAction()!=null){
			qto.appendQuery("and ual.forbiddenAction = ? ");
			qto.addParameter(getModel().getForbiddenAction());
		}
		return qto;
	}
}
