package com.siberhus.ngaiready.admin.action;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang.StringUtils;
import com.siberhus.commons.util.SqlLikePattern;
import com.siberhus.ngai.action.AbstractAction;
import com.siberhus.ngai.action.JSONObjectResolution;
import com.siberhus.ngai.guardian.ActionBeanDetail;
import com.siberhus.ngai.guardian.Guardian;
import com.siberhus.ngai.guardian.config.AuthorizationConfig;

@UrlBinding("/action/admin/eventList")
public class EventListAction extends AbstractAction{
	
	private String actionUri;
	
	private Boolean sqlRegex = Boolean.TRUE;
	
	@Override
	public String getPathPrefix(){
		return null;
	}
	
	@Override
	public String getIndexPage() {
		return null;
	}
	
	public Resolution ajaxGetEvents(){
		if(StringUtils.isBlank(actionUri)){
			return null;
		}
		Set<String> eventNameSet = null;
		Map<String, ActionBeanDetail> actionBeanDetailMap = Guardian
			.getInstance().getActionBeanDetailMap();
		ActionBeanDetail actionBeanDetail = actionBeanDetailMap.get(actionUri);
		if(actionBeanDetail==null){
			if(AuthorizationConfig.get().isRegexEnabled()){
				eventNameSet = new TreeSet<String>();
				for(String uri : actionBeanDetailMap.keySet()){
					boolean matches = false;
					if(sqlRegex){
						matches = SqlLikePattern.matches(actionUri, uri);
					}else{
						matches = Pattern.matches(actionUri, uri);
					}
					if( matches ){
						actionBeanDetail = actionBeanDetailMap.get(uri);
						eventNameSet.addAll(actionBeanDetail.getEventNameSet());
					}
				}
			}
		}else{
			eventNameSet = actionBeanDetail.getEventNameSet();
		}
		return new JSONObjectResolution(eventNameSet);
	}
	
	public String getActionUri() {
		return actionUri;
	}

	public void setActionUri(String actionUri) {
		this.actionUri = actionUri;
	}

	public Boolean getSqlRegex() {
		return sqlRegex;
	}

	public void setSqlRegex(Boolean sqlRegex) {
		this.sqlRegex = sqlRegex;
	}
	
}
