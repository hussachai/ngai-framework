package com.siberhus.ngai.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.exception.ExceptionHandler;
import net.sourceforge.stripes.exception.StripesServletException;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.siberhus.ngai.config.CoreConfig;

public class TraceableExceptionHandler implements ExceptionHandler {

	@Override
	public void init(Configuration configuration) throws Exception {
	}

	@Override
	public void handle(Throwable throwable, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		ActionBean bean = (ActionBean) request
				.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);
		if (bean != null) {
			throwable.printStackTrace();
			Throwable rootThrowable = ExceptionUtils.getRootCause(throwable);
			if(rootThrowable==null){
				rootThrowable = throwable;
			}
			bean.getContext().getValidationErrors()
				.addGlobalError(new SimpleError(rootThrowable.getLocalizedMessage()));
			try {
				bean.getContext().getSourcePageResolution().execute(request, response);
			} catch (Exception e) {
				throw new StripesServletException("Unhandled exception in exception handler.",e);
			}
		} else {
			String errorPage = CoreConfig.get().getErrorPage();
			request.getRequestDispatcher(errorPage).forward(request,response);
		}
	}
	
	
}
