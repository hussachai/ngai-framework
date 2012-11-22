package com.siberhus.ngaiready.admin.action;

import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import com.siberhus.ngai.guardian.model.AbstractPermission;
import com.siberhus.ngaiready.action.BaseCrudAction;

public abstract class BasePermissionAction<T extends AbstractPermission> extends BaseCrudAction<T, Long> {
	
	private String searchEventName;
	
	@ValidateNestedProperties({
		@Validate(on="save", field="actionUri", required=true)
	})
	private T model;
	
	@Override
	public T getModel() {
		return model;
	}
	
	@Override
	public void setModel(T model) {
		this.model = model;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/permission";
	}
	
	public String getSearchEventName() {
		return searchEventName;
	}
	
	public void setSearchEventName(String searchEventName) {
		this.searchEventName = searchEventName;
	}
	
}
