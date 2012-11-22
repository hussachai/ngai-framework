package com.siberhus.ngai.format;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.FastDateFormat;

import net.sourceforge.stripes.format.Formatter;

import com.siberhus.ngai.config.CoreConfig;

public class DateFormatter implements Formatter<Date> {

	private String formatPattern;
	
	private FastDateFormat dateFormat;
	
	@Override
	public void init() {
		CoreConfig config = CoreConfig.get();
		if(formatPattern==null){
			formatPattern = config.getFormatPattern().get(Date.class.getName());
		}
		dateFormat = FastDateFormat.getInstance(formatPattern, config.getLocale());
	}
	
	@Override
	public String format(Date input) {
		
		return dateFormat.format(input);
	}

	@Override
	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}
	
	@Override
	public void setFormatType(String formatType) {}

	@Override
	public void setLocale(Locale locale) {}
	
}
