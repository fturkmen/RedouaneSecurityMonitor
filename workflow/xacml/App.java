package org.workflow.xacml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.workflow.xacml.components.Workflow;



public class App {
	/**
	 * @param args
	 */
	static Workflow Wf = null;
	static Pep pep;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new ClassPathXmlApplicationContext("pep.xml");
		pep = ctx.getBean("pep", Pep.class);
		
		//System.out.println(pep.getControlPDP().getPdp());
		//System.out.println(pep.getUser1().getUsername());
		
		pep.InitPep();
		runWorkflow();
		
		/*int swValue;
		while(true){
			
			System.out.println("=========================================");
		    System.out.println("|   MENU SELECTION ABAC    				|");
		    System.out.println("=========================================");
		    System.out.println("| Options:                 				|");
		    System.out.println("|        1. Run Workflow   				|");
		    System.out.println("|        2. Create new constraint       |");
		    System.out.println("|        3. Exit           				|");
		    System.out.println("=========================================");
		    swValue = Keyin.inInt(" Select option: ");
			
		}*/
		
				

	}
	
	public static void runWorkflow(){
		Wf = new Workflow();
		Wf.initializeWF(pep);
		//Wf.startWorkflow();
		//pep.Request(Wf.startTask());
		//pep.ExecutingRequest();
	}

}
