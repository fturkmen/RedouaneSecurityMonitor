package queryparser;


import java.util.ArrayList;

import queryparser.QueryParser.OPERATION;

public class QueryNode{
	
	/*1: double, 2: integer, 3: string, 4 :boolean*/
	private Pair<Integer, Object> typeAndValue;
			
	private OPERATION Operation;
	private QueryNode Left = null;
	private QueryNode Right= null;
	
	
	/*There are necessary for Tree manipulation ...*/
	private Pair<String,String> identifier; /*Unique identifier and original identifiers ...*/
	private static int uniqueCounter;
	
	

	public QueryNode(int type, Object value/*double value*/){
		this.typeAndValue = new Pair<Integer, Object>(type,value);
		
		identifier = new Pair(value.toString() + "_" + uniqueCounter, value.toString());
		uniqueCounter++;

		
		//this.Value = value;
		this.Operation = null;
	}
	
	
	public QueryNode(OPERATION Operation){
		this.typeAndValue = null;
		identifier = new Pair(Operation.toString()+ "_" + uniqueCounter, Operation.toString());
		uniqueCounter++;
		
		
		this.Operation = Operation;
	}
	
	
    public Pair<String,String> getIdentifier() {
        return identifier;
    }
    
	public OPERATION getOperation(){
		return Operation;
	}
	
	public Pair getTypeAndValue(){
		return typeAndValue;
	}
	
	public boolean isUnary(){
		if (Operation == null){
			System.err.println("Not an Operation so not UNARY");
			return false;
		}
		/*TODO Add other operations here ...*/
		if (this.Operation == Operation.Neg) return true;
		return false;
	}
	 
	public String toString(){
		
		if (this.Operation != null) {
			return this.getOperationString(); 
		}else if (this.typeAndValue != null) {
			return "(" + typeAndValue.second.toString() + typeAndValue.first + ")";
		}
		return null;
	}

	
	public QueryNode getLeft(){return Left;}
	public QueryNode getRight(){return Right;}
	
	private String getOperationString(){
		/*Works based on the order of the operator symbol in the string text file ...*/
		//OPERATIONS = "\\MINUS \\PLUS \\MOD \\MULT \\DIV \\POW \\LEFTP \\RIGHTP"
		switch(this.Operation){
			case LeftParenthesis: return "(";
			case RightParenthesis: return ")";
			case Subtraction: return "-";
			case Addition: return "+";
			case Modulus: return "%";
			case Multiplication: return "*";
			case Division: return "/";
			case Power: return "^";
			case Equals: return "=";
			case And: return "\\WEDGE";
			case Or: return "\\VEE";
			case Implies: return "\\IMPLIES";
			case ForAll: return "\\FORALL";
			case Exists: return "\\EXISTS";
			case Rest: return "\\REST";
			case Ins: return "\\INS";
			case Neg: return "\\NEG";
		}
		System.out.println("Unknown operation");
		return null;
	}
	

	   
	public void SetChildren(QueryNode Left, QueryNode Right){
		this.Left = Left;
		this.Right = Right;
	}
	
	
	/*This is not used currently ...*/
	public Pair<Integer, Object> convert2JavaObjects(){
		//determine and perform operation on children
		switch (this.Operation){
			case Equals: 
				
				break;
			case Power: 
				this.typeAndValue = new Pair<Integer,Object>(1, Math.pow(Double.parseDouble(this.Left.convert2JavaObjects().second.toString()), 
														 				 Double.parseDouble(this.Right.convert2JavaObjects().second.toString())));
				break;
			case Division:
				this.typeAndValue = new Pair<Integer,Object>(1, Double.parseDouble(this.Left.convert2JavaObjects().second.toString()) / 
																Double.parseDouble(this.Right.convert2JavaObjects().second.toString()));
				break;
			case Multiplication:
				this.typeAndValue = new Pair<Integer,Object>(1, Double.parseDouble(this.Left.convert2JavaObjects().second.toString()) * 
																Double.parseDouble(this.Right.convert2JavaObjects().second.toString()));
				break;
			case Modulus:
				this.typeAndValue = new Pair<Integer,Object>(1,(int) Double.parseDouble(this.Left.convert2JavaObjects().second.toString()) % 
															   (int) Double.parseDouble(this.Right.convert2JavaObjects().second.toString()));
				break;
			case Addition:
				this.typeAndValue = new Pair<Integer,Object>(1,Double.parseDouble(this.Left.convert2JavaObjects().second.toString()) + 
															   Double.parseDouble(this.Right.convert2JavaObjects().second.toString()));
				break;
			case Subtraction:
				this.typeAndValue = new Pair<Integer,Object>(1,Double.parseDouble(this.Left.convert2JavaObjects().second.toString()) - 
															   Double.parseDouble(this.Right.convert2JavaObjects().second.toString()));
				break;
				
				
			/*I added the followings ...*/
			case And:
				this.typeAndValue = new Pair<Integer,Object>(4, 
																Boolean.parseBoolean(this.Left.convert2JavaObjects().second.toString()) &&
																Boolean.parseBoolean(this.Right.convert2JavaObjects().second.toString()));
				break;
			case Or:  
				this.typeAndValue = new Pair<Integer,Object>(4, 
																Boolean.parseBoolean(this.Left.convert2JavaObjects().second.toString()) ||
																Boolean.parseBoolean(this.Right.convert2JavaObjects().second.toString()));
				break;
			case StringEqual: 
				
				break;
			case NotIn: break;
			case IntegerAdd: break;
			case GreaterThan: break;
			case LessThan:  break;
			default:
				this.typeAndValue = null;
				//error
		}
		return this.typeAndValue;
	}
	

	
	public String GetPrefixExpression(){
		if (this.Operation == null) return this.typeAndValue.second.toString();
		/*TODO  (EQUALITY CHECK??) Check this control whether it is useful or not!!!*/
		/*if (this.Operation == OPERATION.Equals){
			return this.typeAndValue.second.toString();
		}
		else{*/
			return this.getOperationString() + " " + this.Left.GetPrefixExpression() + " " + this.Right.GetPrefixExpression();
		/*}*/
	}
	
	public String GetInfixExpression(){
		if (this.Operation == null) return this.typeAndValue.second.toString();
		
		/*TODO  (EQUALITY CHECK??) Check this control whether it is useful or not!!!*/
		/*if (this.Operation == OPERATION.Equals){
			return this.typeAndValue.second.toString();
		}
		else{*/
		
			return "(" + this.Left.GetInfixExpression()  + " " + this.getOperationString() + " " + this.Right.GetInfixExpression() + ")";
		/*}*/
	}
	
	public String GetPostfixExpression(){
		
		/*Here we have to check if the value is an operand, operator ... For QueryNode type printing will be different ...*/
		//System.out.println("OPERATION " + this.Operation);
		if (this.Operation == null) return this.typeAndValue.second.toString();
		
		/*TODO  (EQUALITY CHECK??) Check this control whether it is useful or not!!!*/
		/*if (this.Operation == OPERATION.Equals){
			return this.typeAndValue.second.toString();
		}else{*/
			if (this.isUnary()) return this.Left.GetPostfixExpression() + " " + this.getOperationString();
			return this.Left.GetPostfixExpression() + " " + this.Right.GetPostfixExpression() + " " + this.getOperationString();
		/*}*/
	}
	
	
	public boolean IsOperation(){
		//if the node is not a value and the children are not assigned then we are a stand-alone operation
		return (this.Operation != OPERATION.Equals) && (this.Left == null);
	}

}
