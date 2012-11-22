package com.siberhus.ngaiready.admin.action;

import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.guardian.model.AccessDeniedLog;
import com.siberhus.ngai.guardian.service.IAccessDeniedLogService;
import com.siberhus.ngai.guardian.service.impl.AccessDeniedLogService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.action.BaseCrudAction;

@UrlBinding("/action/admin/accessDeniedLog")
public class AccessDeniedLogAction extends BaseCrudAction<AccessDeniedLog, Long> {
	
	@InjectService(implementation=AccessDeniedLogService.class)
	private IAccessDeniedLogService accessDeniedLogService;
	
	private AccessDeniedLog model;
	
	@Override
	public AccessDeniedLog getModel(){
		return model;
	}
	
	@Override
	public void setModel(AccessDeniedLog model){
		this.model = model;
	}
	
	@Override
	public Class<AccessDeniedLog> getModelClass(){
		return AccessDeniedLog.class;
	}
	
	@Override
	public ICrudService<AccessDeniedLog, Long> getCrudService() {
		
		return accessDeniedLogService;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/access-denied-log";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from AccessDeniedLog adl where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getIpAddress()!=null){
			qto.appendQuery("and adl.ipAddress like ? ");
			qto.addParameter(getModel().getIpAddress());
		}
		if(getModel().getRequestURI()!=null){
			qto.appendQuery("and adl.requestURI like ? ");
			qto.addParameter(getModel().getRequestURI());
		}
		if(getModel().getUserAgent()!=null){
			qto.appendQuery("and adl.userAgent like ? ");
			qto.addParameter(getModel().getUserAgent());
		}
		if(getModel().getRetryCount()!=null){
			qto.appendQuery("and adl.retryCount >= ? ");
			qto.addParameter(getModel().getRetryCount());
		}
		return qto;
	}
	
}
