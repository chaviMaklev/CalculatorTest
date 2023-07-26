package com.calculator.services;

import java.util.Map;

public interface IEvaluateService {
    Map<String, Double> evaluateAndCalculateExpression(String inputSeries);
}
