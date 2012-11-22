package com.siberhus.ngai.converter;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import com.siberhus.ngai.config.CoreConfig;

public class SqlTimeTypeConverter implements TypeConverter<Time> {
	
	private DateFormat dateFormat;
	
	public SqlTimeTypeConverter(){
		CoreConfig config = CoreConfig.get();
		Locale locale = config.getLocale();
		dateFormat = new SimpleDateFormat(config.getFormatPattern()
				.get(Time.class.getName()),locale);
	}
	
	@Override
	public Time convert(String input, Class<? extends Time> targetType,
			Collection<ValidationError> errors) {
		try{
			java.util.Date utilDate = (java.util.Date)dateFormat.parse(input);
			synchronized (dateFormat) {
				return new Time(utilDate.getTime());
			}
		}catch(ParseException e){
			 errors.add( new ScopedLocalizableError("converter.time", "invalidTime") );
		}
		return null;
	}	
	@Override
	public void setLocale(Locale locale) {}
	
}
