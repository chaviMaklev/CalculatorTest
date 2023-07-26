package com.calculator.services.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

        // Additional validation checks specific to your application can be added here
    }


    public void isValidIdentifier(String identifier) {

        if(!identifier.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")){
            throw new IllegalArgumentException("Invalid variable name: " + identifier);
        }
    }

    public void isValidAssignmentOperator(String operator) {
        boolean validAssignmentCase1 = operator.equals("=");
        boolean validAssignmentCase2 = operator.length()==2 && operator.contains("=") && operatorUtils.getValidOperatorsContains(operator.charAt(0));

        if(!validAssignmentCase1 && !validAssignmentCase2){
            throw new IllegalArgumentException("Invalid assignment operator: " + operator);
        }
    }

//    public void isValidMathExpression(String expression) {
//        expression = expression.replaceAll("\\s+","");
//        //read the expression and check if it contains only allowed token
//        Pattern pattern = Pattern.compile("((([0-9]*[.])?[0-9]+)|([+\\-*/%()^]))");
//        Matcher matcher = pattern.matcher(expression);
//
//        int counter = 0; //must be equal to the index of the end of the last matching group
//        List<String> tokens = new ArrayList<>();
//        while (matcher.find()) {
//            if (matcher.start() != counter) {
//                //at this point counter indicates the end of the last matching group. If the next matching group
//                //doesn't start at this index, it means that some characters were skipped
//                throw new IllegalArgumentException("Invalid Expression:" + expression + ". Error between " + counter + " end " + matcher.start());
//            }
//            tokens.add(matcher.group().trim());
//            counter += tokens.get(tokens.size() - 1).length();
//        }
//        if (counter != expression.length()) {
//            //if the matcher reaches the end of the string, we want to check if the last matching group ends at the end of the expression
//            throw new IllegalArgumentException("Invalid end of expression");
//        }
//    }
    public void isValidMathExpression(String expression) {
        //spaces
        expression = expression.replaceAll("\\s+","");
        //remove increment or decrement operators
        expression = expression.replaceAll("[+]{2}|--", "");
        Stack<Character> parenthesesStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == '(') {
                parenthesesStack.push(ch);
            } else if (ch == ')') {
                if (parenthesesStack.isEmpty() || parenthesesStack.pop() != '(') {
                    throw new IllegalArgumentException("Unbalanced closing parenthesis");
                }
            } else if (operatorUtils.getValidOperatorsContains(ch)) {
                if (i == 0 && ch != '-'|| i == expression.length() - 1) {
                    throw new IllegalArgumentException("Operator at the start or end of the expression");
                }

                if(i > 0 ) {
                    char prevChar = expression.charAt(i - 1);
                    char nextChar = expression.charAt(i + 1);

                    if (!(prevChar == '(' && ch == '-')) {// case of (-x)
                        if (prevChar == '(' || nextChar == ')' || operatorUtils.getValidOperatorsContains(prevChar) || operatorUtils.getValidOperatorsContains(nextChar)) {
                            throw new IllegalArgumentException("Operator not between two numbers/variables");
                        }
                    }
                }
            }
        }

        //return parenthesesStack.isEmpty();
    }


//    @PostConstruct
//    private void main(String[] args) {
//        Map<String, Double> variables = new HashMap<>();
//        variables.put("x", 3);
//        variables.put("y", 5);
//        variables.put("z", 10);
//
//        ExpressionValidatorUtils exerciseEvaluator = new ExpressionValidatorUtils();
//
//        String[] expressions = {
//                "3 + 2",       // Valid expression
//                "(x + y)",     // Valid expression
//                "x + / 2",     // Invalid operator
//                "x + 2 *",     // Operator at the end of expression
//                "(x + y",      // Missing closing parenthesis
//                "x + * y",     // Operator not between two numbers/variables
//                "(x +) * y",   // Operator not between two numbers/variables
//                "x + (y * z)", // Valid expression
//                "x + y * z)",  // Missing opening parenthesis
//                "x++ * ++y + z--", // Valid expression with increment and decrement operators
//                "++x * --y + z++"  // Valid expression with increment and decrement operators
//        };
//
//        for (String expression : expressions) {
//            System.out.println("Checking expression: " + expression);
//            try {
//                exerciseEvaluator.isValidMathExpression(expression);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Invalid expression: " + expression + " - " + e.getMessage());
//            }
//        }
//    }
}



