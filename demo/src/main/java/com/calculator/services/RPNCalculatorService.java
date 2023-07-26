package com.calculator.services;

import com.calculator.services.utils.OperatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;

@Service
public class RPNCalculatorService implements ICalculatorService {

    @Autowired
    protected OperatorUtils operatorUtils;

    public double calculate(String expression, Map<String, Double> variableMap) {
        expression = prepareExpression(expression);

        //Implementation of the cloth yard method, the phrase "calculated according to RPN"
        String postfix = infixToPostfix(expression);//, variableMap);
        return evaluatePostfix(postfix);
    }

    private String prepareExpression(String expression) {
        expression = expression.replaceAll("\\s+",""); //remove spaces
        //add a 0 before the "-" in order to consider the "-" as a standalone operator
        expression = expression.replace("(-", "(0-");
        if (expression.startsWith("-")){
            expression = "0" + expression;
        }
        return expression;
    }

    private String infixToPostfix(String infix){//, Map<String, Double> variableMap) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char currentChar = infix.charAt(i);

            if (Character.isDigit(currentChar) || currentChar == '.') {
                postfix.append(currentChar);
                while (i + 1 < infix.length() && (Character.isDigit(infix.charAt(i + 1)) || infix.charAt(i + 1) == '.')) {
                    postfix.append(infix.charAt(++i));
                }
                postfix.append(' ');
            //} else if (Character.isLetter(currentChar)) {
//                i = processVariable(infix, postfix, i, variableMap);
            } else if (currentChar == '(') {
                operatorStack.push(currentChar);
            } else if (currentChar == ')') {
                processRightParenthesis(postfix, operatorStack);
            } else {
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


//    private int processVariable(String infix, StringBuilder postfix, int index, Map<String, Double> variableMap) {
//        int j = index;
//        while (j < infix.length() && (Character.isLetter(infix.charAt(j)))) {
//            j++;
//        }
//        String variableName = infix.substring(index, j);
//        Double variableValue = variableMap.get(variableName);
//        if (variableValue == null) {
//            throw new IllegalArgumentException("Undefined variable: " + variableName);
//        }
//        postfix.append(variableValue);
//        postfix.append(" ");
//        return j - 1;
//    }

    private void processRightParenthesis(StringBuilder postfix, Stack<Character> operatorStack) {
        while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
            postfix.append(operatorStack.pop());
            postfix.append(" ");
        }
        operatorStack.pop();
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
                    char firstChar = token.charAt(0);
                    if (Character.isDigit(firstChar)) {
                        operandStack.push(Double.parseDouble(token));
                    } else {
                        double operand2 = operandStack.pop();
                        double operand1 = operandStack.pop();
                        double result = performOperation(firstChar, operand1, operand2);
                        operandStack.push(result);
                    }
                });

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression");
        }

        return operandStack.pop();
    }

    private int getPrecedence(char operator) {
        return operatorUtils.getPrecedence(operator);
    }

    private double performOperation(char operator, double operand1, double operand2) {
        return operatorUtils.getBinaryOperator(String.valueOf(operator)).apply(operand1,operand2);
    }

}
