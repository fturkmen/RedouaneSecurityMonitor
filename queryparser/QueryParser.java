package queryparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class QueryParser{
	public static enum EXPRESSIONTYPE{Prefix, Infix, Postfix}
	
	//Parser original source : https://gist.github.com/JakeWharton/237462
	
	//private static enum OPERATION{And, Or, StringEqual, NotIn, IntegerAdd, GreaterThan, LessThan, LeftParenthesis, RightParenthesis}
	public static enum OPERATION{Subtraction, Addition, Modulus, Multiplication, 
								 Division, Power, LeftParenthesis, RightParenthesis, Equals, 
								 /*Query Language constructs*/
								 And, Or, Implies, Neg, ForAll,Exists, Rest, Ins,
								 /*I added these TO BE IMPLEMENTED...*/
								 StringEqual, NotIn, IntegerAdd, GreaterThan, LessThan}
	
	
	public static Map<String, Integer> specialNodes = new HashMap<String, Integer>();
	
	
	private static int Precedence(OPERATION Operation)
	{

		switch (Operation/*GetOperation(findIndexOf(Operation.toString()))*/)
		{
			case LeftParenthesis:
			case RightParenthesis:
				return 10;
			case Equals: 
				return 9;
			case Power:
				return 8;
			case Division:
			case Multiplication:
			case Modulus:
				return 7;
			case Addition:
			case Subtraction:
				return 6;
			case Neg:
			case Rest:
			case Ins:
				return 5;
			case ForAll:
			case Exists:
				return 4;
			case And:
				return 3;	
			case Or:
				return 2;
			case Implies:
				return 1;
			default:
				//throw new InvalidArgumentException();
				System.err.println("Invalid operation.");
				return -1;
		}
	}
	
	
	
	private static OPERATION GetOperation(int i)
	{
		switch (i){
			case 0:
				return OPERATION.Subtraction;
			case 1:
				return OPERATION.Addition;
			case 2:
				return OPERATION.Modulus;
			case 3:
				return OPERATION.Multiplication;
			case 4:
				return OPERATION.Division;
			case 5:
				return OPERATION.Power;
			case 6:
				return OPERATION.LeftParenthesis;
			case 7:
				return OPERATION.RightParenthesis;
			case 8:
				return OPERATION.Equals;
			case 9:
				return OPERATION.And;
			case 10:
				return OPERATION.Or;
			case 11:
				return OPERATION.Implies;
			case 12:
				return OPERATION.Neg;
			case 13:
				return OPERATION.ForAll;
			case 14:
				return OPERATION.Exists;
			case 15:
				return OPERATION.Rest;
			case 16:
				return OPERATION.Ins;
		}
		
		return null;
	}
	

/*****************************************************************************************************/	
	//public static String OPERATIONS = "-+%*/^()";
	public static String[] OPERATIONS = new String[]{"-","+","%", "*", "/", "^", "(", ")","=",
									  "\\WEDGE", "\\VEE", "\\IMPLIES", "\\NEG", "\\FORALL", 
									  "\\EXISTS", "\\REST", "\\INS"};
	
	public QueryNode Root;
	private HashMap<String, QueryNode> nodes;
	
	
	public QueryParser(String Expression, EXPRESSIONTYPE Type)
	{
		nodes = new HashMap<String, QueryNode>();
		specialNodes.put("\\emptyset", 1);
		
		
		switch (Type)
		{
			case Prefix:
				this.Root = this.ConstructPrefixExpressionTree(Expression);
				break;
			case Infix:
				this.Root = this.ConstructInfixExpressionTree(Expression);
				break;
			case Postfix:
				this.Root = this.ConstructPostfixExpressionTree(Expression);
				break;
			default:
				System.err.println("Invalid expression type. Reverting to zero.");
				QueryNode qNode = new QueryNode(1, 0);
				nodes.put(qNode.getIdentifier().first, qNode);
				this.Root = qNode;
		}
	}
	
	
	public HashMap<String, QueryNode> getNodes(){ return nodes; }
	
	
	private static int findIndexOf(String str, String before, String after){
		
		for (int i = 0; i < OPERATIONS.length; i++)
			if (OPERATIONS[i].equals(str)) {
				if (str.equals("-") || str.equals("+") || str.equals("*") || str.equals("%") || str.equals("/") || str.equals("^")){
					if (!before.equals(" ") || !after.equals(" ")) return -1; 
				}
				return i;
			}
		return -1;
	}
	
	
	
	private QueryNode ConstructPrefixExpressionTree(String PrefixExpression)
	{
		String[] Terms = PrefixExpression.split(" ");
		ArrayList<QueryNode> Nodes = new ArrayList<QueryNode>();
		
		//create leaf node for each expression term
		for (int i = 0; i < Terms.length; i++)
		{
			String Term = Terms[i];
			String before = (i == 0) ? Term : Terms[i-1];
			String after = ((i+1) >= Terms.length) ? Term : Terms[i+1];
			//OPERATIONS = "\\MINUS \\PLUS \\MOD \\MULT \\DIV \\POW \\LEFTP \\RIGHTP"
			//String[] operations = QueryParser.OPERATIONS.split(" ");
			if (/*ExpressionEvaluator.OPERATIONS.indexOf(Term) == -1*/findIndexOf(/*operations, */Term,before,after) == -1)
			{
				try{
					QueryNode qNode = new QueryNode(1, Double.valueOf(Term));
					nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
				catch (NumberFormatException e){
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					QueryNode qNode = new QueryNode(1, 0);
					nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
			}else{
				QueryNode qNode = new QueryNode(QueryParser.GetOperation(/*ExpressionEvaluator.OPERATIONS.indexOf(Term)*/
						findIndexOf(Term, before, after)));
				nodes.put(qNode.getIdentifier().first, qNode);
				Nodes.add(qNode);
			}
		}
		
		try
		{
			int Current = Nodes.size() - 3; //first possible operation position
			while (Nodes.size() > 1)
			{
				if (Nodes.get(Current).IsOperation())
				{
					//assign children and remove them from working nodes
					Nodes.get(Current).SetChildren(Nodes.remove(Current + 1), Nodes.remove(Current + 1));
					Current = Nodes.size() - 3; //reset to first possible position
				}
				else
				{
					--Current;
				}
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			System.err.println("Too many/few operations. Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(1, 0);
			nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.err.println("Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(1, 0);
			nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
		}
		
		//last node, root node
		return Nodes.get(0);
	}
	
	
	
	
	
	
	
	
	private QueryNode ConstructInfixExpressionTree(String InfixExpression){
		//TODO: unary operators
		
		//un-suck infix with spacing and check parenthesis
		int j = 0;
		int i = 0;
		while (i < InfixExpression.length())
		{
			//String[] operations = ExpressionEvaluator.OPERATIONS.split(" ");
			String str = String.valueOf(InfixExpression.charAt(i));	
			String before = (i == 0) ? str : String.valueOf(InfixExpression.charAt(i - 1));
			String after = ((i+1) >= InfixExpression.length()) ? str : String.valueOf(InfixExpression.charAt(i + 1));
			boolean specialOp = false;
			if (str.equals("\\")){
				i++;
				while(InfixExpression.charAt(i) != ' '){
					str += InfixExpression.charAt(i);
					i++;
				}
				specialOp = true;
			} 
			// We found the operator, stored in str. Now we will insert space between operands and operators...
			if (/*QueryParser.OPERATIONS.indexOf(InfixExpression.charAt(i))*/ findIndexOf(str ,before, after) != -1)
			{
				if (/*InfixExpression.charAt(i)*/ str.equals("(")) ++j;
				if (/*InfixExpression.charAt(i)*/ str.equals(")")) --j;
				if (j < 0)
				{
					System.err.println("Opening parenthesis expected. Reverting expression to zero.");
					QueryNode qNode = new QueryNode(1, 0);
					nodes.put(qNode.getIdentifier().first, qNode);
					return qNode;
					//throw new Exception... Mismatched parenthesis at character i
				}
				/*Insert space before*/
				if (!specialOp && (i > 0) && (InfixExpression.charAt(i - 1) != ' '))
				{
					InfixExpression = InfixExpression.substring(0, (i)) 
									  + " " + InfixExpression.substring(i);
					++i; //adjust for insertion
				}
				/*Insert space after --> PROBLEM HERE WITH TWO DIGIT NUMBERS!!!!*/
				if (!specialOp && (i < InfixExpression.length() - 1) && (InfixExpression.charAt(i + 1) != ' '))
				{
					InfixExpression = InfixExpression.substring(0, i + 1) + " " + InfixExpression.substring(i + 1);
					++i; //adjust for insertion
				}
			}
			i++;
		}
		if (j > 0)
		{
			System.err.println("Closing parenthesis expected. Reverting expression to zero.");
			QueryNode qNode = new QueryNode(1, 0);
			nodes.put(qNode.getIdentifier().first, qNode);
			return qNode;
			//throw new Exception... Mismatched parenthesis
		}
		
		String[] Terms = InfixExpression.split(" +");
		ArrayList<QueryNode> Nodes = new ArrayList<QueryNode>();
		
		//create leaf node for each expression term --> this includes also the parenthesis at the beginning...
		for (int k = 0; k < Terms.length; k++)
		{
			String Term = Terms[k];
			String before = (k == 0) ? Term : Terms[k-1];
			String after = ((k+1) >= Terms.length) ? Term : Terms[k+1];
			if (findIndexOf(Term,before, after) == -1)
			{
				try
				{
					QueryNode qNode = new QueryNode(3, Term.trim() /*Double.valueOf(Term)*/);
					nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					QueryNode qNode = new QueryNode(1, 0);
					nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
			}
			else
			{
				QueryNode qNode = new QueryNode(QueryParser.GetOperation(findIndexOf(Term.trim(), before,after)));
				nodes.put(qNode.getIdentifier().first, qNode);
				Nodes.add(qNode);
			}
		}
		

		
		
		
		
		//temporary stack space  --> You have operations and operands stored separately and then added everything to ValueStack...
		ArrayList<QueryNode> OperationStack = new ArrayList<QueryNode>();
		ArrayList<QueryNode> ValueStack = new ArrayList<QueryNode>();
		
		/*						
	 	StringEqual, NotIn, IntegerAdd, GreaterThan, LessThan
		 */
		try
		{
			//iterate terms and construct tree
			for ( i = 0; i < Nodes.size(); ++i)
			{
				if (Nodes.get(i).getOperation() != null){ /*OPERATION*/
					switch (Nodes.get(i).getOperation())
					{
						case LeftParenthesis:
							OperationStack.add(0, Nodes.get(i));
							break;
						case RightParenthesis:
							//assign operations children until left parenthesis is encountered
							while (OperationStack.get(0).getOperation().toString().equals("LeftParenthesis") == false)
							{
								QueryNode op = OperationStack.remove(0);
								ValueStack.add(0, op);
								if (op.isUnary())
									ValueStack.get(0).SetChildren(ValueStack.remove(1), null);
								else 
									ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));
							}
							OperationStack.remove(0); //remove left parenthesis
							break;
						case Subtraction:
						case Addition:
						case Modulus:
						case Multiplication:
						case Division:
						case Power:
						case Equals:
						case And: 
						case Or:
						case Implies:
						case ForAll:
						case Exists:
						case Rest:
						case Ins:
							//assign operations children until operation stack is empty, a left parenthesis is encountered, or a lower precedence operation is encountered
							while ((OperationStack.size() > 0) && (OperationStack.get(0).getOperation().toString().equals("LeftParenthesis") == false) && 
								(QueryParser.Precedence(Nodes.get(i).getOperation()) <= QueryParser.Precedence(OperationStack.get(0).getOperation())))
							{
								QueryNode op = OperationStack.remove(0);
								ValueStack.add(0, op);
								if (op.isUnary())
									ValueStack.get(0).SetChildren(ValueStack.remove(1), null);
								else ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));			
							}
							OperationStack.add(0, Nodes.get(i));
							break;
						case Neg:  /*FOR NEGATION THE RIGHT CHILD IS NULL!!!!!!!!!*/
							//assign operations children until operation stack is empty, a left parenthesis is encountered, or a lower precedence operation is encountered
							while ((OperationStack.size() > 0) && (OperationStack.get(0).getOperation().toString().equals("LeftParenthesis") == false) && 
									(QueryParser.Precedence(Nodes.get(i).getOperation()) <= QueryParser.Precedence(OperationStack.get(0).getOperation())))
							{
								ValueStack.add(0, OperationStack.remove(0));
								ValueStack.get(0).SetChildren(ValueStack.remove(1), null);
							}
							OperationStack.add(0, Nodes.get(i));
							break;
						default: 
							
					}
				}else/*VALUE*/{
					ValueStack.add(0, Nodes.get(i));
				}

			}
			//assign the rest of the operations children
			while (OperationStack.size() > 0)
			{
				QueryNode op = OperationStack.remove(0);
				ValueStack.add(0, op);
				if (op.isUnary()) ValueStack.get(0).SetChildren(ValueStack.remove(1), null); //NEG...
				else  ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));
			}
		
			/*for (Iterator<QueryNode> itOp = OperationStack.iterator(); itOp.hasNext();){
				QueryNode qn = itOp.next();
				System.out.println("QueryNode : " + qn.toString());
			}
			for (Iterator<QueryNode> itOp = ValueStack.iterator(); itOp.hasNext();){
				QueryNode qn = itOp.next();
				System.out.println("QueryNode : " + qn.toString());
			}*/
		}
		catch (IndexOutOfBoundsException e)
		{
			System.err.println("Too many/few operations. Reverting expression to zero." + e.getLocalizedMessage());
			ValueStack.clear();
			QueryNode qNode = new QueryNode(1, 0);
			nodes.put(qNode.getIdentifier().first, qNode);
			ValueStack.add(qNode);
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.err.println("Reverting expression to zero." + e.getLocalizedMessage());
			ValueStack.clear();
			QueryNode qNode = new QueryNode(1, 0);
			nodes.put(qNode.getIdentifier().first, qNode);
			ValueStack.add(qNode);
		}
		
		//last node, root node
		//System.out.println("HERE WEARE " + ValueStack.get(0).toString() );
	//	System.out.println("INFIX EXPRESSION + " + ValueStack.get(0).GetInfixExpression());
		return ValueStack.get(0);
	}
	
	
	
	private QueryNode ConstructPostfixExpressionTree(String PostfixExpression){
		String[] Terms = PostfixExpression.split(" ");
		ArrayList<QueryNode> Nodes = new ArrayList<QueryNode>();
		
		//create a leaf node for each expression term
		for (int i = 0; i < Terms.length; i++)
		{
			String Term = Terms[i];
			String before = (i == 0) ? Term : Terms[i-1];
			String after = ((i+1) >= Terms.length) ? Term : Terms[i+1];
			//String[] operations = QueryParser.OPERATIONS.split(" ");
			if (/*ExpressionEvaluator.OPERATIONS.indexOf(Term) == -1*/findIndexOf( Term, before, after) == -1){
				try{
					QueryNode qNode = new QueryNode(1, Double.valueOf(Term));
					nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
				catch (NumberFormatException e){
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					QueryNode qNode = new QueryNode(1, 0);
					nodes.put(qNode.getIdentifier().first, qNode);
					Nodes.add(qNode);
				}
			}
			else{
				QueryNode qNode = new QueryNode(QueryParser.GetOperation(findIndexOf(Term,before, after)));
				nodes.put(qNode.getIdentifier().first, qNode);
				Nodes.add(qNode);
			}
		}
		
		try
		{
			int Current = 2; //first possible operation postition
			while (Nodes.size() > 1)
			{
				if (Nodes.get(Current).IsOperation())
				{
					//assign children and remove them from working nodes
					Nodes.get(Current).SetChildren(Nodes.remove(Current - 2), Nodes.remove(Current - 2));
					Current = 2; //reset to first possible position
				}
				else
				{
					++Current;
				}
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			System.err.println("Too many/few operations. Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(1, 0);
			nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.err.println("Reverting expression to zero.");
			Nodes.clear();
			QueryNode qNode = new QueryNode(1, 0);
			nodes.put(qNode.getIdentifier().first, qNode);
			Nodes.add(qNode);
		}
		
		//last node, root node
		return Nodes.get(0);
	}
	
	public String GetPrefixExpression() { return this.Root.GetPrefixExpression();}
	public String GetInfixExpression(){ return this.Root.GetInfixExpression(); }
	public String GetPostfixExpression() { return this.Root.GetPostfixExpression(); }
	public Pair<Integer, Object> GetValue(){ return this.Root.convert2JavaObjects(); }
	
}