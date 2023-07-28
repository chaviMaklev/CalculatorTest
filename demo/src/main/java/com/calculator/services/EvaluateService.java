package com.calculator.services;

import com.calculator.services.utils.ExpressionValidatorUtils;
import com.calculator.services.utils.OperatorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EvaluateService implements IEvaluateService{

    private static final Logger logger = LogManager.getLogger(EvaluateService.class);

    @Autowired
    protected ICalculatorService calculatorService;
    @Autowired
    protected ExpressionValidatorUtils expressionValidatorUtils;
    @Autowired
    protected OperatorUtils operatorUtils;

    Map<String, Double> variableMap;

    public Map<String, Double> evaluateAndCalculateExpression(String inputSeries) {
        logger.info("****** Start evaluateAndCalculateExpression with input: "+ inputSeries);
        variableMap = new HashMap<>();
        List<String> placementSentences = tokenizePlacementSentences(inputSeries);

        for (String sentence : placementSentences) {
            evaluateExpression(sentence);
        }

        logger.info("****** End evaluateAndCalculateExpression with Output: "+ variableMap);
        return variableMap;
    }

    public void evaluateExpression(String expression) {
        Pattern pattern = Pattern.compile("(\\w+)\\s*([+=\\-*/]*)\\s*(.+)");
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {
            String variableName = matcher.group(1);
            String operator = matcher.group(2);
            String valueExpression = matcher.group(3);

            expressionValidatorUtils.validateExpression(variableName,operator,valueExpression);

            String updateExpression = handleIncrementsDecrementsAndPrepareExpression(valueExpression, variableMap);
            double value = calculatorService.calculate(updateExpression);
            handleMapUpdate(variableName, operator, value);        }
    }


    private void handleMapUpdate(String variableName , String operator, double value){

        if(operator.length()>1 && operator.contains("=")){
            //Handling a placement statement with an operator
            operator = operator.replace("=","");
            Double variableValue = variableMap.get(variableName);
            if (!variableMap.containsValue(variableValue)) {
                throw new IllegalArgumentException("Undefined variable: " + variableName);
            }
        }
        double currentValue = variableMap.getOrDefault(variableName, 0.0);
        double newValue = operator.equals("=")? value
                : operatorUtils.getBinaryOperator(operator.charAt(0)).apply(currentValue, value);

        variableMap.put(variableName, newValue);
    }

    private List<String> tokenizePlacementSentences(String input) {
        return Arrays.stream(input.split("\n")).collect(Collectors.toList());
    }


    public String handleIncrementsDecrementsAndPrepareExpression(String exercise, Map<String, Double> variables) {
        //Find all variables in the expression with increment or decrement operators
        Pattern variablePattern = Pattern.compile("(\\+\\+|--)?\\b([a-zA-Z]+)\\b(\\+\\+|--)?");
        Matcher matcher = variablePattern.matcher(exercise);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String preIncrementDecrementOperator = matcher.group(1);
            String variableName = matcher.group(2);
            String postIncrementDecrementOperator = matcher.group(3);

            if(!variables.containsKey(variableName)){
                throw new IllegalArgumentException("Undefined variable: " + variableName);
            }

            double value = variables.get(variableName);

            value = applyIncrementDecrement(value,preIncrementDecrementOperator);

            matcher.appendReplacement(result, String.valueOf(value));

            value = applyIncrementDecrement(value,postIncrementDecrementOperator);


            if(!StringUtils.isEmpty(preIncrementDecrementOperator) || !StringUtils.isEmpty(postIncrementDecrementOperator)) {
                variables.put(variableName, value);
            }

        }
        matcher.appendTail(result);

        return String.valueOf(result);
    }

    private double applyIncrementDecrement(double value, String operator) {
        if ("++".equals(operator)) {
            return value + 1;
        }
        if ("--".equals(operator)) {
            return value - 1;
        }
        return value;
    }
}



