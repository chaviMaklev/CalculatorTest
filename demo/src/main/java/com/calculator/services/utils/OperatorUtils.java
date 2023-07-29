package com.calculator.services.utils;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;

@Service
public class OperatorUtils {
    private final Map<Character, OperatorInfo> OPERATORS;

    // Constructor
    public OperatorUtils() {
        OPERATORS = new HashMap<>();
        OPERATORS.put('+', new OperatorInfo(Double::sum, 1));
        OPERATORS.put('-', new OperatorInfo((a, b) -> a - b, 1));
        OPERATORS.put('*', new OperatorInfo((a, b) -> a * b, 2));
        OPERATORS.put('/', new OperatorInfo((a, b) -> {
            if (b == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        }, 2));
    }

    public BinaryOperator<Double> getBinaryOperator(char operator) {
        OperatorInfo operatorInfo = OPERATORS.get(operator);
        if (operatorInfo == null) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
        return operatorInfo.getOperator();
    }

    public int getPrecedence(char operator) {
        OperatorInfo operatorInfo = OPERATORS.get(operator);
        if (operatorInfo == null) {
            return 0; // Default precedence for unknown operators
        }
        return operatorInfo.getPrecedence();    }

    public boolean isValidOperator(char operator) {
        return OPERATORS.containsKey(operator);
    }

    // Helper class to store both BinaryOperator and precedence for an operator
    private static class OperatorInfo {
        private final BinaryOperator<Double> operator;
        private final int precedence;

        public OperatorInfo(BinaryOperator<Double> operator, int precedence) {
            this.operator = operator;
            this.precedence = precedence;
        }

        public BinaryOperator<Double> getOperator() {
            return operator;
        }

        public int getPrecedence() {
            return precedence;
        }
    }
}

