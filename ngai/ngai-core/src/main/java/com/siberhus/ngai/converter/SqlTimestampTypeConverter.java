package com.siberhus.ngai.converter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import com.siberhus.ngai.config.CoreConfig;

public class SqlTimestampTypeConverter implements TypeConverter<Timestamp> {
	
	private DateFormat dateFormat;
	
	public SqlTimestampTypeConverter(){
		CoreConfig config = CoreConfig.get();
		Locale locale = config.getLocale();
		dateFormat = new SimpleDateFormat(config.getFormatPattern()
				.get(Timestamp.class.getName()),locale);
	}
	
	@Override
	public Timestamp convert(String input, Class<? extends Timestamp> targetType,
			Collection<ValidationError> errors) {
		try{
			java.util.Date utilDate = (java.util.Date)dateFormat.parse(input);
			synchronized (dateFormat) {
				return new Timestamp(utilDate.getTime());
			}
		}catch(ParseException e){
			 errors.add( new ScopedLocalizableError("converter.timestamp", "invalidTimestamp") );
		}
		return null;
	}	
	@Override
	public void setLocale(Locale locale) {}
	
}
