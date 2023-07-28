package com.calculator.services;

import com.calculator.services.utils.OperatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Stack;

@Service
public class RPNCalculatorService implements ICalculatorService {

    @Autowired
    protected OperatorUtils operatorUtils;

    public double calculate(String expression) {
        expression = prepareExpression(expression);

        //Implementation of the shunting yard method, the phrase "calculated according to RPN"
        String postfix = infixToPostfix(expression);//, variableMap);
        return evaluatePostfix(postfix);
    }

    private String prepareExpression(String expression) {
        expression = expression.replaceAll("\\s+",""); //remove spaces
        //add a 0 before the "-" in order to consider the "-" as a standalone operator
//        expression = expression.replace("(-", "(0-");
//        if (expression.startsWith("-")){
//            expression = "0" + expression;
//        }
          return expression;
    }

    private String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char currentChar = infix.charAt(i);

            if (Character.isDigit(currentChar) || currentChar == '.') {
                // Process operands (numbers)
                i = processOperand(postfix,infix,i);
            } else if (currentChar == '(') {
                // Process left parenthesis
                operatorStack.push(currentChar);
            } else if (currentChar == ')') {
                // Process right parenthesis
                processRightParenthesis(postfix, operatorStack);
            } else if (isNegativeNumber(infix, i)) {
                // Process negative number
                i = processNegativeNumber(postfix, infix, i);
            } else {
                // Process operators
                processOperator(postfix, operatorStack, currentChar);
            }
        }

        // Process remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
            postfix.append(" ");
        }

        return postfix.toString();
    }

    private int processOperand(StringBuilder postfix, String infix, int startIndex) {
        postfix.append(infix.charAt(startIndex));
        int i = startIndex;
        while (i+1 < infix.length() && (Character.isDigit(infix.charAt(i+1)) || infix.charAt(i+1) == '.')) {
            postfix.append(infix.charAt(++i));
        }
        postfix.append(' ');
        return i;

    }
    private boolean isNegativeNumber(String infix, int index) {
        return (infix.charAt(index) == '-') && (index == 0 || operatorUtils.isValidOperator(infix.charAt(index - 1)) || infix.charAt(index - 1) == '(');
    }

    private int processNegativeNumber(StringBuilder postfix, String infix, int startIndex) {
        postfix.append("-");
//        int i = startIndex + 1;
//        while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
//            postfix.append(infix.charAt(i));
//            i++;
//        }
//        postfix.append(" ");
//        return i ;
        return processOperand(postfix,infix,startIndex+1);
    }

    private void processRightParenthesis(StringBuilder postfix, Stack<Character> operatorStack) {
        while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
            postfix.append(operatorStack.pop());
            postfix.append(" ");
        }
        operatorStack.pop();// Pop the left parenthesis
    }

    private void processOperator(StringBuilder postfix, Stack<Character> operatorStack, char currentChar) {
        while (!operatorStack.isEmpty() && getPrecedence(currentChar) <= getPrecedence(operatorStack.peek())) {
            postfix.append(operatorStack.pop());
            postfix.append(" ");
        }
        operatorStack.push(currentChar);
    }

    private double evaluatePostfix(String postfix) {
        Stack<Double> operandStack = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        Arrays.stream(tokens)
                .filter(token -> !token.isEmpty())
                .forEach(token -> {
                    //char firstChar = token.charAt(0);
                    boolean isDigit = Character.isDigit(token.charAt(0));
                    boolean isMinusDigit = token.length()>1 && token.charAt(0) == '-' && Character.isDigit(token.charAt(1));
                    if (isDigit || isMinusDigit) {
                        operandStack.push(Double.parseDouble(token));
                    } else {
                        try {
                            double operand2 = operandStack.pop();
                            double operand1 = operandStack.pop();
                            double result = performOperation(token.charAt(0), operand1, operand2);
                            operandStack.push(result);

                        }catch (Exception exception){
                            throw new IllegalArgumentException("Invalid postfix expression: "+ postfix);
                        }
                    }
                });

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression: "+ postfix);
        }

        return operandStack.pop();
    }

    private int getPrecedence(char operator) {
        return operatorUtils.getPrecedence(operator);
    }

    private double performOperation(char operator, double operand1, double operand2) {
        return operatorUtils.getBinaryOperator(operator).apply(operand1,operand2);
    }

}
