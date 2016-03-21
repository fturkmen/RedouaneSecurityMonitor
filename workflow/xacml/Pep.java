package org.workflow.xacml;
import org.workflow.xacml.MainFrame;

import javax.swing.SwingUtilities;


import org.workflow.xacml.runtimemonitor.Interceptor;


public class Pep {	
	
	ControlPDP controlPDP;
	boolean permit;
	String Enforcement_answer = null;
	MainFrame mf = null;
		
	public void InitPep(){		
		frame();
		controlPDP.pdpInit();
	}
	
	public void frame(){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				 mf = new MainFrame();
			}
		});
	}

	@Interceptor
	public void Request(String task, String user){
		Attribute A = new Attribute();
		controlPDP.setPolicy(mf.getPolicy());
		System.out.println(" Request "+task);
				
		if(permit){
			System.out.println("Security monitor : Allowed");
			permit = controlPDP.DecisionRequest(task, user);
			if(permit){ //controlPDP.DecisionRequest(task, user)
			  	
			 A.writeToAttribute(String.valueOf(task), "executed", String.valueOf(user));
			 System.out.println("XACML monitor : Allowed");
			} else{
				disAllow();
				A.writeToAttribute(String.valueOf(task), "executed", "not");
				System.out.println("XACML : not allowed");
			}
		} else{ 
			A.writeToAttribute(String.valueOf(task), "executed", "not");
			System.out.println("Security monitor: not allowed");			
		}
		Enforcement_answer = new String (Boolean.toString(permit));
		//System.out.println(Enforcement_answer);
		//return "true";
	}	
	
	public String xRequest(){		
		return Enforcement_answer;
	}
	
	public void allow(){
		permit = Boolean.valueOf(true);
	}
	public void disAllow(){
		permit = Boolean.valueOf(false);
	}	
	
	public ControlPDP getControlPDP() {		
		return controlPDP;
	}

	public void setControlPDP(ControlPDP controlPDP) {
		this.controlPDP = controlPDP;
	}	
	
	public String setConstraint(){
		//System.out.println("PEP "+mf.getConstraint());
		return mf.getConstraint();
	}
	
}
