package com.siberhus.ngai.format;

import java.sql.Timestamp;
import java.util.Locale;

import net.sourceforge.stripes.format.Formatter;

import org.apache.commons.lang.time.FastDateFormat;

import com.siberhus.ngai.config.CoreConfig;

public class SqlTimestampFormatter implements Formatter<Timestamp> {

	private String formatPattern;
	
	private FastDateFormat dateFormat;
	
	@Override
	public void init() {
		CoreConfig config = CoreConfig.get();
		if(formatPattern==null){
			formatPattern = config.getFormatPattern().get(Timestamp.class.getName());
		}
		dateFormat = FastDateFormat.getInstance(formatPattern, config.getLocale());
	}
	
	@Override
	public String format(Timestamp input) {
		
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
