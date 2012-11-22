package com.siberhus.ngaiready.admin.action;

import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.guardian.model.LoginFailureLog;
import com.siberhus.ngai.guardian.service.ILoginFailureLogService;
import com.siberhus.ngai.guardian.service.impl.LoginFailureLogService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.action.BaseCrudAction;

@UrlBinding("/action/admin/loginFailureLog")
public class LoginFailureLogAction extends BaseCrudAction<LoginFailureLog, Long> {
	
	@InjectService(implementation=LoginFailureLogService.class)
	private ILoginFailureLogService loginFailureLogService;
	
	private LoginFailureLog model;
	
	@Override
	public LoginFailureLog getModel(){
		return model;
	}
	
	@Override
	public void setModel(LoginFailureLog model){
		this.model = model;
	}
	
	@Override
	public Class<LoginFailureLog> getModelClass(){
		return LoginFailureLog.class;
	}
	
	@Override
	public ICrudService<LoginFailureLog, Long> getCrudService() {
		
		return loginFailureLogService;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/login-failure-log";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from LoginFailureLog lfl where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getIpAddress()!=null){
			qto.appendQuery("and lfl.ipAddress like ? ");
			qto.addParameter(getModel().getIpAddress());
		}
		if(getModel().getAttemptCount()!=null){
			qto.appendQuery("and lfl.attemptCount = ? ");
			qto.addParameter(getModel().getAttemptCount());
		}
		return qto;
	}

	
	
}
