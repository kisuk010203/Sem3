import java.io.*;
import java.util.Stack;
import java.lang.String;
import java.lang.Math;
public class CalculatorTest
{
	public static int order(char operator){
		switch(operator){
			case '+':
			case '-':
				return 2;
			case '*':
			case '/':
			case '%':
				return 3;
			case '~':
				return -4;//return negative value to show right
			case '^':
				return -5;
		}
		return -1;
	}
	public static String toPostfix(String exp) throws Exception{
		StringBuilder result = new StringBuilder();
		Stack<Character> stack = new Stack<>(); // stack of operators
		Stack<Character> avgStack = new Stack<>(); // stack saving only parthenses and commas - to find size of average

		for (int i = 0; i < exp.length(); i++) {
			char c = exp.charAt(i);
			if(c == ' ') continue;
			if(c == '-' && (i==0 || order(exp.charAt(i-1)) != -1 || exp.charAt(i-1) == '(' || exp.charAt(i-1) == ',')) c = '~'; //- and ~ determine
			if(Character.isDigit(c)){ 
				while(Character.isDigit(c)){
					result.append(c);
					i++;
					if(i<exp.length()) c = exp.charAt(i);
					else break;
				}
				result.append(' ');
				i--;
			}
			else if(c == '('){ 
				stack.push(c);
				avgStack.push(c);
			}
			else if(c == ')'){
				int avg_num = 0; // number of commas
				while(!stack.isEmpty() && stack.peek() != '('){ // pop until meeting first (
					result.append(stack.pop());
					result.append(' ');
				}
				while(!avgStack.isEmpty() && avgStack.peek() != '('){ // do the same thing for avgstack
					avg_num++;
					avgStack.pop();
				}
				if(!stack.isEmpty() && stack.peek() != '('){ // if no parathenses
					throw new Exception();
				}
				else{ // pop (
					stack.pop();
					avgStack.pop();
				}
				if(avg_num != 0){ // when there are more than 1 commas
					result.append(avg_num+1);
					result.append(" avg ");
				}
			}
			else if(c == ','){
				avgStack.push(',');
				if(i == 0 || (exp.charAt(i-1) == ',') || exp.charAt(i-1) == '(') { // raise error when intolerated average input 
					throw new Exception();
				}
				while(!stack.isEmpty() && stack.peek() != '('){
					result.append(stack.pop());
					result.append(' ');
				}
				if(!stack.isEmpty() && stack.peek() != '('){
					throw new Exception();
				}
			}
			else{
				if(order(c) > 0){
					while(!stack.isEmpty() && order(c) <= Math.abs(order(stack.peek()))){ // if left-associative
						result.append(stack.pop());
						result.append(' ');
					}
					stack.push(c);
				}
				else{
					while(!stack.isEmpty() && Math.abs(order(c)) < Math.abs(order(stack.peek()))){ // if right-associative
						result.append(stack.pop());
						result.append(' ');
					}
					stack.push(c);
				}
			}
		}
		while(!stack.isEmpty()){
			if(stack.peek() == '(') {
				throw new Exception();
			}
			result.append(stack.pop());
			result.append(' ');
		}
		return result.toString().trim(); // trim last space
	}	
	public static long evaluate(String postString) throws Exception{
		Stack<Long> stack = new Stack<>();
		for (int i = 0; i < postString.length(); i++) {
			char c = postString.charAt(i);
			if(c == ' ') continue;
			if(Character.isDigit(c)){
				long num = 0;
				while(Character.isDigit(c)){
					num = num*10 + (c-'0');
					i++;
					if(i<postString.length()) c = postString.charAt(i);
					else break;
				}i--;
				stack.push(num);
			}
			else if(c == '~'){
				try {
					long num1 = stack.pop();
					stack.push(-1l*num1);
				} catch (Exception e) {
					throw new Exception();
				}
			}
			else if(c == 'a'){
				i+=2;
				try {
					long avg_num = stack.pop();
					long sum = 0;
					for (int j = 0; j < avg_num; j++) {
						sum += stack.pop();
					}
					stack.push(sum/avg_num);
				} catch (Exception e) {
					throw new Exception();
				}
			}
			else{ // binary opreators
				try {
					long num2 = stack.pop();
					long num1 = stack.pop();
					switch(c){
						case '+':
							stack.push(num1+num2);
							break;
						case '-':
							stack.push(num1-num2);
							break;
						case '*':
							stack.push(num1*num2);
							break;
						case '/':
							stack.push(num1/num2);
							break;
						case '^':
							if(num1 == 0 && num2 <0) throw new Exception();
							stack.push((long)(Math.pow(num1, num2)));
							break;
						case '%':
							stack.push(num1%num2);
							break;

					}
				} catch (Exception e) {
					throw new Exception();
				}
			}
			
		}
		try {
			if(stack.size() != 1) throw new Exception(); // if stack empty or many left
			return stack.pop();
		} catch (Exception e) {
			throw new Exception();
		}
	}
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				input = input.replaceAll("\\s+", " "); // replace all consecutive blanks into one
				input = input.replaceAll("(?<!\\d)\\s+|\\s+(?!\\d)", ""); // replace all blanks except blanks between digits
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input)
	{
		try {
			String postString = toPostfix(input);
			long ans = evaluate(postString);
			System.out.println(postString);
			System.out.println(ans);
		} catch (Exception e) {
			System.out.println("ERROR");	
		};
	}
}
