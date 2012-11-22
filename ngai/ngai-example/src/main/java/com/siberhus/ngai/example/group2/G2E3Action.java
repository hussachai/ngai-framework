package com.siberhus.ngai.example.group2;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.example.BaseExampleAction;


/**
 * Action + Stateful Service or Session Scope Service
 * 
 * @author hussachai
 *
 */
@UrlBinding("/action/g2e3")
public class G2E3Action extends BaseExampleAction {
	
	@InjectService
	private G2E3Service g2e3Service;
	
	public G2E3Action() {
		super("Group2 - Example3");
		setDetail("Action + Stateful Service or Session Scope Service");
	}
	
	@Override
	public String getIndexPage() {
		return "/pages/number-counter.jsp";
	}
	
	public Resolution incrementCounter(){
		g2e3Service.incrementCounter();
		return index();
	}
	
	public int getCounter(){
		return g2e3Service.getCounter();
	}
	
}
