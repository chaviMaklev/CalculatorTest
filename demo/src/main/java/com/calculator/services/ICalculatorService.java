package com.calculator.services;

import java.util.Map;

public interface ICalculatorService {
    double calculate(String expression, Map<String, Double> variableMap);
}
