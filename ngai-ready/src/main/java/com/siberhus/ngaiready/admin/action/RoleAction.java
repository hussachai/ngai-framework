package com.siberhus.ngaiready.admin.action;


import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.guardian.service.IRoleService;
import com.siberhus.ngai.guardian.service.impl.RoleService;
import com.siberhus.ngaiready.action.BaseCrudAction;

@UrlBinding("/action/admin/role")
public class RoleAction extends BaseCrudAction<Role, Long>{

	
	@InjectService(implementation=RoleService.class)
	private IRoleService roleService;
	
	@ValidateNestedProperties({
		@Validate(on="save", field="roleName",required=true)
	})
	private Role model;
	
	@Override
	public Role getModel(){
		return model;
	}
	
	@Override
	public void setModel(Role model){
		this.model = model;
	}
	
	@Override
	public Class<Role> getModelClass(){
		return Role.class;
	}
	
	@Override
	public ICrudService<Role, Long> getCrudService() {
		
		return roleService;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/role";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from Role r where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		
		if(getModel().getRoleName()!=null){
			qto.appendQuery("and r.roleName like ?");
			qto.addParameter(getModel().getRoleName());
		}
		if(getModel().getDescription()!=null){
			qto.appendQuery("and r.description like ?");
			qto.addParameter(getModel().getDescription());
		}
		return qto;
	}
	
	@Override
	public Resolution delete() {
		
		int userCount = getModel().getUserSet().size();
		if(userCount!=0){
			getContext().getValidationErrors()
				.addGlobalError(new SimpleError("There are total "
						+userCount+" users are using this role"));
			return search();
		}
		return super.delete();
	}
	
	
}
