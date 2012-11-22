package com.siberhus.ngai.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import com.siberhus.ngai.config.CoreConfig;

public class DateTypeConverter implements TypeConverter<Date> {
	
	private DateFormat dateFormat;
	
	public DateTypeConverter(){
		CoreConfig config = CoreConfig.get();
		Locale locale = config.getLocale();
		dateFormat = new SimpleDateFormat(config.getFormatPattern()
				.get(Date.class.getName()),locale);
	}
	
	@Override
	public Date convert(String input, Class<? extends Date> targetType,
			Collection<ValidationError> errors) {
		try{
			synchronized (dateFormat) {
				return (Date)dateFormat.parse(input);
			}
		}catch(ParseException e){
			 errors.add( new ScopedLocalizableError("converter.date", "invalidDate") );
		}
		return null;
	}	
	@Override
	public void setLocale(Locale locale) {}
	
}
