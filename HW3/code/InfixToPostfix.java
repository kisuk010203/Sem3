import java.util.*;

public class InfixToPostfix {
   public static int precedence(char operator) {
      switch(operator) {
         case '+':
         case '-':
            return 1;

         case '*':
         case '/':
            return 2;

         case '^':
            return 3;
      }
      return -1;
   }

   public static String infixToPostfix(String expression) {
    StringBuilder result = new StringBuilder();
    Stack<Character> stack = new Stack<>();

    for(int i = 0; i < expression.length(); i++) {
        char c = expression.charAt(i);

        if(Character.isLetterOrDigit(c)) {
            result.append(c);
        } else if(c == '(') {
            stack.push(c);
        } else if(c == ')') {
            while(!stack.isEmpty() && stack.peek() != '(') {
                result.append(stack.pop());
            }
            if(!stack.isEmpty() && stack.peek() != '(') {
                return "Invalid expression";
            } else {
                stack.pop();
            }
        } else {
            while(!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                if(c == '^' && stack.peek() == '^') {
                    result.append(stack.pop());
                } else {
                    break;
                }
            }
            stack.push(c);
        }
    }

    while(!stack.isEmpty()) {
        char c = stack.pop();
        if(c == '(' || c == ')') {
            return "Invalid expression";
        }
        result.append(c);
    }

    return result.toString();
}


   public static void main(String[] args) {
      int arr[] = {0,1,2};
    System.out.println(arr[arr[0]--]--);
    for (int i = 0; i < arr.length; i++) {
        System.out.println(arr[i]);
    }
   }
}
