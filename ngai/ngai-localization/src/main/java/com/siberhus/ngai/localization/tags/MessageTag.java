package com.siberhus.ngai.localization.tags;

import com.siberhus.ngai.localization.model.ResourceType;

public class MessageTag extends LocalizableTag {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public ResourceType getType() {
		return ResourceType.MESSAGE;
	}
	
}
