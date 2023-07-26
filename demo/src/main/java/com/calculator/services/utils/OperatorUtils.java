package com.calculator.services.utils;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;
@Service
public class OperatorUtils {
    private final Map<String, BinaryOperator<Double>> OPERATORS;
    private final Map<Character, Integer> PRECEDENCE_MAP;
    private final Set<Character> VALID_OPERATORS;



    // Constructor
    public OperatorUtils() {
        OPERATORS = new HashMap<>();
        OPERATORS.put("=", (a, b) -> b);
        OPERATORS.put("+", Double::sum);
        OPERATORS.put("-", (a, b) -> a - b);
        OPERATORS.put("*", (a, b) -> a * b);
        OPERATORS.put("/", (a, b) -> {
            if (b == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        });

        PRECEDENCE_MAP = new HashMap<>();
        PRECEDENCE_MAP.put('+', 1);
        PRECEDENCE_MAP.put('-', 1);
        PRECEDENCE_MAP.put('*', 2);
        PRECEDENCE_MAP.put('/', 2);


        VALID_OPERATORS = new HashSet<>(Arrays.asList('+', '-', '*', '/'));
    }

    public BinaryOperator<Double> getBinaryOperator(String operator) {
        return OPERATORS.getOrDefault(operator, (a, b) -> {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        });
    }

    public int getPrecedence(char operator) {
        return PRECEDENCE_MAP.getOrDefault(operator, 0);
    }

    public boolean getValidOperatorsContains(char operator){return VALID_OPERATORS.contains(operator);}
}


