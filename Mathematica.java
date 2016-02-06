package minimathematica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Mathematica 
{
	private HashMap<String, Integer> ARITHMETIC;
    {
		ARITHMETIC = new HashMap<String, Integer>();
		ARITHMETIC.put("+", 1);
		ARITHMETIC.put("-", 1);
		ARITHMETIC.put("*", 2);
		ARITHMETIC.put("/", 2);
    }
	private HashMap<String, Integer> ADVANCED;
	{
		ADVANCED = new HashMap<String, Integer>();
		ADVANCED.put("log", 3);
		ADVANCED.put("pow", 3);
		ADVANCED.put("sqrt", 3);
		ADVANCED.put("sin", 3);
		ADVANCED.put("cos", 3);
		ADVANCED.put("tan", 3);
		ADVANCED.put("cotg", 3);
    }
	private Stack<String> tokens;
	private Stack<Double> result;
	
	private String[] BRACKETS = {"(", ")"};
	private String[] CONSTANTS = {"pi", "e"};
	private String[] IGNORESYMBOLS = {","};
	
	public Mathematica() {
		tokens = new Stack<String>();
		result = new Stack<Double>();
	}
	
	public double calc(String expression) throws WrongExpression {
		expression = expression.replaceAll("\\s+","");
		ArrayList<String> parts = splitter(expression);
		double result = RPN(parts);
		return result;
	}
	
	private boolean doesContain(String str, String[] arr) {
		return Arrays.asList(arr).contains(str);
	}
	
	private boolean doesContain(String str, HashMap<String, Integer> map) {
		return map.containsKey(str);
	}
	
	private static boolean isNumeric(String str)  
	{  
		return str.matches("-?\\d+(\\.\\d+)?"); 
	}
	
	private ArrayList<String> splitter(String expression) {
		boolean firstSymbol = true;
		ArrayList<String> parts = new ArrayList<String>();
		String merge = "";
		for(int i = 0; i < expression.length(); i++) {
			if(merge.isEmpty() && expression.charAt(i) == '-' && firstSymbol) {
				merge += expression.charAt(i);
				firstSymbol = false;
			}
			else if((expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z') || 
					(expression.charAt(i) >= 'A' && expression.charAt(i) <= 'Z') ||
					(expression.charAt(i) >= '0' && expression.charAt(i) <= '9') ||
					((expression.charAt(i) == '.'))) {
				merge += expression.charAt(i);
				firstSymbol = false;
			}
			else {
				if(merge.isEmpty())
					parts.add(String.valueOf(expression.charAt(i)));
				else {
					parts.add(merge);
					parts.add(String.valueOf(expression.charAt(i)));
					merge  = "";
				}
			}
		}
		
		if(!merge.isEmpty())
			parts.add(merge);
		
		return parts;
	}

	private double RPN(ArrayList<String> parts) throws WrongExpression 
	{
		for (String part : parts) 
		{
			if(isNumeric(part))
				result.push(Double.valueOf(part));
			else if(doesContain(part, ARITHMETIC)) 
			{
				if(tokens.isEmpty())
					tokens.push(part);
				else if(ARITHMETIC.containsKey(tokens.lastElement())) 
				{
					while(!tokens.isEmpty()) 
					{
						if(ARITHMETIC.get(tokens.lastElement()).intValue() >= ARITHMETIC.get(part).intValue()) 
						{
							Double r = AdvCalc(tokens.pop(), result);
							result.push(r);
						}
						else
							break;
					}
					tokens.push(part);
				}
				else
					tokens.push(part);
			}
			else if(doesContain(part, ADVANCED)) 
			{
				tokens.push(part);
			}
			else if(doesContain(part, IGNORESYMBOLS)) 
			{
				// Ignore
			}
			else if(doesContain(part, CONSTANTS)) 
			{
				if(part.equals("pi"))
					part = String.valueOf((Math.PI));
				else
					part = String.valueOf((Math.E));
				result.push(Double.valueOf(part));
			}
			else if(part.equals(BRACKETS[0]))
			{
				tokens.add(part);
			}
			else if(part.equals(BRACKETS[1])) 
			{
				String pop = tokens.pop();
				while(!pop.equals(BRACKETS[0])) 
				{
					Double r = AdvCalc(pop, result);
					result.push(r);
					pop = tokens.pop();
				}
				while(!tokens.isEmpty() && doesContain(tokens.lastElement(), ADVANCED)) 
				{
					pop = tokens.pop();
					Double r = AdvCalc(pop, result);
					result.push(r);
				}
			}
			else 
			{
				System.out.println("Error");
				return 0;
			}
		}
		while(!tokens.isEmpty()) {
			Double r = AdvCalc(tokens.pop(), result);
			result.push(r);
		}
		System.out.println("The answer is:\n" + result.pop());
		return 0;
	}
	
	private double AdvCalc(String func, Stack<Double> stack) throws WrongExpression {
		Double firstoperand, secondoperand = null;
		Double result, log1, log2, logResult = null;
		switch (func) {
			case "+":
				firstoperand = stack.pop();
				secondoperand = stack.pop();
				result = firstoperand + secondoperand;
				return result;
			
			case "-":
				firstoperand = stack.pop();
				secondoperand = stack.pop();
				result = secondoperand - firstoperand;
				return result;
				
			case "/":
				firstoperand = stack.pop();
				secondoperand = stack.pop();
				if(firstoperand == 0.0) throw new IllegalArgumentException("Cannot divide by 0.");
				result = secondoperand / firstoperand;
				return result;
				
			case "*":
				firstoperand = stack.pop();
				secondoperand = stack.pop();
				result = firstoperand * secondoperand;
				return result;
				
			case "log":
				firstoperand = stack.pop();
				secondoperand = stack.pop();
				log1 = Math.log(secondoperand);
				log2 = Math.log(firstoperand);
				logResult = log2 / log1;
				return logResult;
				
			case "pow":
				firstoperand = stack.pop();
				secondoperand = stack.pop();
				result = Math.pow(secondoperand, firstoperand);
				return result;
				
			case "sqrt":
				firstoperand = stack.pop();
				result = Math.sqrt(firstoperand);
				return result;
				
			case "sin":
				firstoperand = stack.pop();
				result = Math.sin(firstoperand);
				return result;
				
			case "cos":
				firstoperand = stack.pop();
				result = Math.cos(firstoperand);
				return result;
				
			case "tan":
				firstoperand = stack.pop();
				result = Math.tan(firstoperand);
				return result;
				
			case "cotg":
				firstoperand = stack.pop();
				result = 1.0 / Math.tan(firstoperand);
				return result;
	
			default:
				throw new WrongExpression();
			}
	}
}
