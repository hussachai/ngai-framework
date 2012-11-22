package com.siberhus.ngaiready.admin.action;

import javax.persistence.TemporalType;

import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.TemporalObject;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.guardian.model.LoginLog;
import com.siberhus.ngai.guardian.service.ILoginLogService;
import com.siberhus.ngai.guardian.service.impl.LoginLogService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.action.BaseCrudAction;

@UrlBinding("/action/admin/loginLog")
public class LoginLogAction extends BaseCrudAction<LoginLog, Long>{
	
	@InjectService(implementation=LoginLogService.class)
	private ILoginLogService loginLogService;
	
	private LoginLog model;
	
	@Override
	public LoginLog getModel(){
		return model;
	}
	
	@Override
	public void setModel(LoginLog model){
		this.model = model;
	}
	
	@Override
	public Class<LoginLog> getModelClass(){
		return LoginLog.class;
	}
	
	@Override
	public ICrudService<LoginLog, Long> getCrudService() {
		
		return loginLogService;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/login-log";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from LoginLog ll where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getUsername()!=null){
			qto.appendQuery("and ll.username like ?");
			qto.addParameter(getModel().getUsername());
		}
		if(getModel().getLoginAt()!=null){
			qto.appendQuery("and ll.loginAt = ?");
			qto.addParameter(new TemporalObject(getModel().getLoginAt()
					, TemporalType.DATE));
		}
		if(getModel().getLogoutAt()!=null){
			qto.appendQuery("and ll.logoutAt = ?");
			qto.addParameter(new TemporalObject(getModel().getLogoutAt()
					, TemporalType.DATE));
		}
		if(getModel().getIpAddress()!=null){
			qto.appendQuery("and ll.ipAddress like ?");
			qto.addParameter(getModel().getIpAddress());
		}
		
		return qto;
	}
	
}
