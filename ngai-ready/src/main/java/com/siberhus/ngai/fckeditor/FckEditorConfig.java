package com.siberhus.ngai.fckeditor;

import com.siberhus.ngai.config.NgaiConfiguration;
import com.siberhus.ngai.core.INgaiBootstrap;
import com.siberhus.ngai.core.Ngai;

public class FckEditorConfig implements INgaiBootstrap {

	@Override
	public void execute(NgaiConfiguration config) {
		
		Ngai.getInstance().getNgaiProperties();
	}
	
}
