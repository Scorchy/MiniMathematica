package minimathematica;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws WrongExpression
	{
		//Information
		System.out.println("Welcome to Mathematica Mini \nYou can use +,-,*,/,log,pow,sqrt,sin,cos,tan,cotan to evaluate.");
		System.out.println("Input your expression:");
		
		Scanner scanner = new Scanner(System.in);
		Mathematica math = new Mathematica();
		math.calc(scanner.nextLine());
		//5 + sin(pi) / pow(2, 10) - log(e, pow(e, sqrt(4)))
		scanner.close();
	}
		
}