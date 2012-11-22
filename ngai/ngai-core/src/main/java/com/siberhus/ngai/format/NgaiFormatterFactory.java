package com.siberhus.ngai.format;

import java.util.Date;
import java.util.Map;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.format.DefaultFormatterFactory;
import net.sourceforge.stripes.format.Formatter;

public class NgaiFormatterFactory extends DefaultFormatterFactory{

	@Override
	public void init(Configuration configuration) throws Exception {
		super.init(configuration);
		Map<Class<?>, Class<? extends Formatter<?>>> formatters = getFormatters();
		formatters.put(Date.class, DateFormatter.class);
		formatters.put(java.sql.Date.class, SqlDateFormatter.class);
		formatters.put(java.sql.Time.class, SqlTimeFormatter.class);
		formatters.put(java.sql.Timestamp.class, SqlTimestampFormatter.class);
	}

	
}
