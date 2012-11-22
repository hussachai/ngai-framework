package com.siberhus.ngai.example.group2;

import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.annot.ServiceBean;

@ServiceBean(scope=Scope.Session)
public class G2E3Service {
	
	private int counter = 0;
	
	public void incrementCounter(){
		counter++;
	}
	
	public int getCounter(){
		return counter;
	}
	
}
