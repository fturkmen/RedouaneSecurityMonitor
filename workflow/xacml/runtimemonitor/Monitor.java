package org.workflow.xacml.runtimemonitor;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.workflow.xacml.Attribute;
import org.workflow.xacml.Pep;
import org.workflow.xacml.runtimemonitor.Constraints2;


@Aspect
public class Monitor {	
	
	@Around("@annotation(org.workflow.xacml.runtimemonitor.Interceptor) &&" + "args(task, user)" )
	public Object Control(ProceedingJoinPoint pjp, String task, String user){
			
		Object returnValue = null;
		//String[] constraint = null;
		//String requester;
		System.out.println("Request intercepted");
		try {
			returnValue = (Pep) pjp.getTarget();
			Pep pep = (Pep) returnValue;
			pep.setConstraint();
			Constraints2 cs = new Constraints2(pep.setConstraint());
			
			if(!cs.equal(String.valueOf(task))){
				pep.allow();
				pjp.proceed();
			} else{
			//cs.evaluate(task, user, (Pep) returnValue);
				if(cs.evaluate(task,user)){
					pep.allow();
				}else pep.disAllow();					
				pjp.proceed();
			
			}
			
			//System.out.println("Task "+ task+" onderschept with user : " +user); 
			/*
			switch(task){
			case "t2" : t2(task, user, (Pep) returnValue);			
						pjp.proceed();
						break;
			case "t4" : t4(task, user, (Pep) returnValue);			
						pjp.proceed();
						break;
			case "t6" : t6(task, user, (Pep) returnValue);
						pjp.proceed(); 
						break;
			case "t7" : t7(task, user, (Pep) returnValue);
						pjp.proceed();
						break;
			default: 
				((Pep) returnValue).allow();
				pjp.proceed();
				break;
			}
			*/
			//System.out.println("task "+pjp.getSignature().getName()+" is intercepted, doing dynamic policy check");
			
			//System.out.println(pjp.getSignature().getName());
			//returnValue = pjp.proceed();
			//returnValue = (Pep) pjp.getTarget();			
			//System.out.println(((Pep) returnValue).getExecutingTask().getTaskname());
			//requester = ((Pep) returnValue).getExecutingTask().getExecutingUser();
			//String taskname = ((Pep) returnValue).getExecutingTask().getTaskname();
			
			//System.out.println(returnValue);
			//Constraints filter
			/*if(cs.checkConstraint(taskname)){
			  constraint = cs.getConstraint();
				switch(constraint[constraint.length-1]){
				case "DSoD":				
					if(DSoD(constraint[1], requester))					  
						pjp.proceed();
					else
						System.out.println("user cannot execute task "+taskname);
				return true;
				case "DBoD":
				System.out.println("DBoD");
				return true;
						
			
				}
			}// end if statement
			
			else
				pjp.proceed();	*/		
		
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			System.out.println("After throwing " +e);
			System.out.print("__");
		}
		//System.out.println("monitor finished");
		return returnValue;
	}	
	
	public void t2 (String task, String user, Pep request){		
		Attribute A = new Attribute();		
		try{
			
			if (Integer.parseInt(A.readFromAttribute(String.valueOf(user) , "level")) > Integer.parseInt(A.readFromAttribute(A.readFromAttribute("t1","executed"),"level"))){
				((Pep) request).allow();			
			}	else ((Pep) request).disAllow();
		}catch(NumberFormatException nfe){
			System.out.println(nfe);
			nfe.printStackTrace();
		}
	}
	
	public void t4 (String task, String user, Pep request){		
		Attribute A = new Attribute();		
		
		if (Integer.parseInt(A.readFromAttribute(String.valueOf(user) , "workexperience")) > 2){
			((Pep) request).allow();			
		}else ((Pep) request).disAllow();		
	}
	
	public void t6 (String task, String user, Pep request){
		Attribute A = new Attribute();
		if(!user.equals(A.readFromAttribute("t5","executed"))){
			((Pep) request).allow();			
		}else ((Pep) request).disAllow();
	}
	
	public void t7 (String task, String user, Pep request){
		Attribute A = new Attribute();
		if(user.equals(A.readFromAttribute("t2","executed")) || (A.readFromAttribute("t2","executed")).equals("not")){
			((Pep) request).allow();			
		}else ((Pep) request).disAllow();
	}	
	}
