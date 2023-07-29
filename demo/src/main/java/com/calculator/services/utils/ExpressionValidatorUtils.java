package com.calculator.services.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExpressionValidatorUtils {

    @Autowired
    protected OperatorUtils operatorUtils;

    public void validateExpression(String variableName, String operator, String valueExpression) {
        // Check if the variable name is a valid identifier
        isValidIdentifier(variableName);

        // Check if the operator is a valid assignment operator
        isValidAssignmentOperator(operator);

        // Check if the value expression is a valid arithmetic expression
        isValidMathExpression(valueExpression);

    }


    public void isValidIdentifier(String identifier) {

        if(!identifier.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")){
            throw new IllegalArgumentException("Invalid variable name: " + identifier);
        }
    }

    public void isValidAssignmentOperator(String operator) {
        boolean validAssignmentCase1 = operator.equals("=");
        boolean validAssignmentCase2 = operator.length()==2 && operator.contains("=") && operatorUtils.isValidOperator(operator.charAt(0));

        if(!validAssignmentCase1 && !validAssignmentCase2){
            throw new IllegalArgumentException("Invalid assignment operator: " + operator);
        }
    }

    public void isValidMathExpression(String expression) {
        expression = expression.replaceAll("\\s+","");

        //remove increment or decrement operators
        expression= replaceIncrementDecrementVariables(expression);

        Stack<Character> parenthesesStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == '(') {
                parenthesesStack.push(ch);
            } else if (ch == ')') {
                if (parenthesesStack.isEmpty() || parenthesesStack.pop() != '(') {
                    throw new IllegalArgumentException("Unbalanced parenthesis");
                }
            } else if (Character.isDigit(ch) || ch == '.') {
                while (i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                    i++;
                }
                if (i+1 < expression.length() && Character.isLetter(expression.charAt(i + 1))) {
                    throw new IllegalArgumentException("An operator is missing between the numbers and the conditionals");
                }
            } else if (Character.isLetter(ch)) {
                while (i + 1 < expression.length() && (Character.isLetter(expression.charAt(i + 1)))) {
                    i++;
                }
                if (i+1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    throw new IllegalArgumentException("An operator is missing between the numbers and the conditionals");
                }

            }else if (operatorUtils.getBinaryOperator(ch)!= null) {
                if (i == 0 && ch != '-'|| i == expression.length() - 1) {
                    throw new IllegalArgumentException("Operator at the start or end of the expression");
                }

                if(i > 0 ) {
                    char prevChar = expression.charAt(i - 1);
                    char nextChar = expression.charAt(i + 1);

                    if (!(prevChar == '(' && ch == '-')) {// case of (-x)
                        if (prevChar == '(' || nextChar == ')' || operatorUtils.isValidOperator(prevChar)&& ch != '-' || (operatorUtils.isValidOperator(nextChar) && nextChar != '-')) {
                            throw new IllegalArgumentException("Operator not between two numbers/variables");
                        }
                    }
                }
            }
        }

        if(!parenthesesStack.isEmpty()){
            throw new IllegalArgumentException("Unbalanced parenthesis");
        }
    }

    private  String replaceIncrementDecrementVariables(String expression) {
        // Regular expression pattern to match variables with addition or decrement operators
        Pattern variablePattern = Pattern.compile("(\\+\\+|--)?\\b([a-zA-Z]+)\\b(\\+\\+|--)?");

        // Find and replace variables in the expression
        Matcher matcher = variablePattern.matcher(expression);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String variable = matcher.group().replaceAll("(\\+\\+|--)", "");
            matcher.appendReplacement(result, variable);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}



