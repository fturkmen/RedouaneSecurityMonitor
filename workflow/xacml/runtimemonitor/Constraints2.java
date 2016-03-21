package org.workflow.xacml.runtimemonitor;



import java.util.HashMap;

import org.workflow.xacml.Attribute;

public class Constraints2 {
	HashMap<String, Constraint> hashMap = new HashMap<>();
	public Constraints2(String fileConstraints){
		Attribute A = new Attribute();
		//String path=null;
		String C = A.readConstraints(fileConstraints);
		String [] output = C.split("\\;");
		for(int i = 0; i < output.length;i++){
		//System.out.println(output[i]);
		String[] d = output[i].split("\\,");
		setConstraint(d[0],d[1],d[2]);
		}
	
	    /*
		hashMap.put("t1", new Constraint(){
			public void evaluate(String task, String user, Pep request){			
				((Pep) request).allow();
				System.out.println("task 1 is being evaluated");
			}
		});
		
	hashMap.put("t2", new Constraint(){
		public boolean evaluate(String task, String user){
			Attribute A = new Attribute();
			boolean eval = true;
			try{
				
				if (Integer.parseInt(A.readFromAttribute(String.valueOf(user) , "level")) > Integer.parseInt(A.readFromAttribute(A.readFromAttribute("t1","executed"),"level"))){
					eval = true;			
				}	else eval = false;
			}catch(NumberFormatException nfe){
				System.out.println(nfe);
				nfe.printStackTrace();
			}
			return eval;
		}
	});
	
	hashMap.put("t3", new Constraint(){
		public void evaluate(String task, String user, Pep request){			
			((Pep) request).allow();
		}
	});
	
	
	hashMap.put("t4", new Constraint(){
		public boolean evaluate(String task, String user){
			Attribute A = new Attribute();		
			boolean eval = true;
			if (Integer.parseInt(A.readFromAttribute(String.valueOf(user) , "workexperience")) > 2){
				eval = true;			
			}else eval = false;
			return eval;
	}
	});
	
	
	hashMap.put("t5", new Constraint(){
		public void evaluate(String task, String user, Pep request){			
			((Pep) request).allow();
		}
	});
	
		
	hashMap.put("t6", new Constraint(){
		public boolean evaluate(String task, String user){
			Attribute A = new Attribute();
			boolean eval = true;
			if(!user.equals(A.readFromAttribute("t5","executed"))){
				eval = true;;			
			}else eval = false;
			return eval;
	}
		
	});
	
	hashMap.put("t7", new Constraint(){
		public boolean evaluate(String task, String user){
			Attribute A = new Attribute();
			boolean eval = true;
			if(user.equals(A.readFromAttribute("t2","executed")) || (A.readFromAttribute("t2","executed")).equals("not")){
				eval = true;			
			}else eval = false;
			return eval;
	}
	});	
		*/
	}
	
	
	public boolean evaluate(String task, String user){
		return hashMap.get(task).evaluate(task, user);
		
	}
	public boolean equal(String check){
		return hashMap.containsKey(check);
	}
	
	public void setConstraint(String c, String task, final String cond){
		final String con = cond;
		//System.out.println("cond = " +cond);
		switch(c){
		case "l" : hashMap.put(task, new Constraint(){ //worklevel
			public boolean evaluate(String task, String user){
				Attribute A = new Attribute();
				boolean eval = true;
				try{					
					if (Integer.parseInt(A.readFromAttribute(String.valueOf(user) , "level")) > Integer.parseInt(A.readFromAttribute(A.readFromAttribute(con,"executed"),"level"))){
						eval = true;			
					}	else eval = false;
				}catch(NumberFormatException nfe){
					System.out.println(nfe);
					nfe.printStackTrace();
				}
				return eval;
			}
		});
		break;
		case "w": hashMap.put(task, new Constraint(){
			public boolean evaluate(String task, String user){ //workexperience
				Attribute A = new Attribute();		
				boolean eval = true;
				if (Integer.parseInt(A.readFromAttribute(String.valueOf(user) , "workexperience")) > Integer.parseInt(cond)){
					eval = true;			
				}else eval = false;
				return eval;
		}
		});
		break;
		case "d": hashMap.put(task, new Constraint(){ //dsod
			public boolean evaluate(String task, String user){
				Attribute A = new Attribute();
				boolean eval = true;
				if(!user.equals(A.readFromAttribute(cond,"executed"))){
					eval = true;;			
				}else eval = false;
				return eval;
		}
			
		});
		break;
		case "b": hashMap.put(task, new Constraint(){ //dbod
			public boolean evaluate(String task, String user){
				Attribute A = new Attribute();
				boolean eval = true;
				if(user.equals(A.readFromAttribute(cond,"executed")) || (A.readFromAttribute(cond,"executed")).equals("not")){
					eval = true;			
				}else eval = false;
				return eval;
		}
		});	
		break; 
		default:			
		break;
		}
		
	}

}

