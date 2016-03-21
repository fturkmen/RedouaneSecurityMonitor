package org.workflow.xacml.components;
import java.io.Console;
import java.util.Scanner;

import org.workflow.xacml.Pep;
import org.workflow.xacml.runtimemonitor.Interceptor;

public class Workflow {
	Pep pep = null;
	
	
	
	Scanner Keyboard = new Scanner(System.in);
	Console c = System.console();
	
	public void initializeWF(Pep pep){
		this.pep = pep;
		//path 1
		startP1Workflow();		
		
		//path 2
		//startP2Workflow();	
	}
	
	public void startP1Workflow(){
		System.out.println("Enter key to execute the first task");
		//path 1
		
		//use case all goes oke
		
		Keyboard.nextLine();
		pep.Request("t1", "u1");
		//System.out.println("Allowed : " +pep.Request("t1", "u1"));
		
		Keyboard.nextLine();
		pep.Request("t3", "u3");
		//System.out.println("Allowed : " +pep.Request("t3", "u3"));
		
		Keyboard.nextLine();
		pep.Request("t4", "u4");
		//System.out.println("Allowed : " +pep.Request("t4", "u4"));
		
		
		Keyboard.nextLine();
		pep.Request("t5", "u5");
		//System.out.println("Allowed : " +pep.Request("t5", "u5"));
		
		Keyboard.nextLine();
		pep.Request("t6", "u6");
		//System.out.println("Allowed : " +pep.Request("t6", "u6"));
		
		//use case violation of DSOD on task 6
		
		/*
		Keyboard.nextLine();
		pep.Request("t1", "u1");
		//System.out.println(pep.xRequest());
		
		Keyboard.nextLine();
		pep.Request("t3", "u3");
		
		Keyboard.nextLine();
		pep.Request("t4", "u4");
		//System.out.println(pep.xRequest());
		
		Keyboard.nextLine();
		pep.Request("t5", "u6");
		//System.out.println(pep.xRequest());
		
		Keyboard.nextLine();
		pep.Request("t6", "u6");
		//System.out.println(pep.xRequest());
		
		*/		
		
	}
	
	public void startP2Workflow(){
		System.out.println("Enter key to execute the first task");
		// path 2
		
		/*
		Keyboard.nextLine();
		pep.Request("t1", "u1");
		
		Keyboard.nextLine();
		pep.Request("t2", "u2");		
		
		Keyboard.nextLine();
		pep.Request("t7", "u2");
		*/
		
		// use case violation of SC constraint on task 7
		/*Keyboard.nextLine();
		pep.Request("t1", "u1");
		
		Keyboard.nextLine();
		pep.Request("t2", "u2");		
		
		Keyboard.nextLine();
		pep.Request("t7", "u7");
		*/
		
		// use case violation of SC constraint on task 7 + 2 && does not influnce the flow of thw workflow
		/*
		Keyboard.nextLine();
		pep.Request("t1", "u1");
		
		Keyboard.nextLine();
		pep.Request("t2", "u1");		
		
		Keyboard.nextLine();
		pep.Request("t7", "u7");
		*/
	}

}
