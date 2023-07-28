package com.calculator.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RPNCalculatorServiceTest {

    @Autowired
    private RPNCalculatorService RPNCalculatorService;

    private Map<String, Double> variableMap;

    @Before
    public void setUp() {
        variableMap = new HashMap<>();
        variableMap.put("x", 3.0);
        variableMap.put("y", 5.0);
        variableMap.put("i", 2.0);
    }

    @Test
    public void testEvaluateSimpleExpression() {
        String expression = "5.0 + 3";
        double result = RPNCalculatorService.calculate(expression);
        Assertions.assertEquals(8, result);
    }

    @Test
    public void test_more_than_one_digits_number(){
        String expression = "17+2*10--2";
        double result = RPNCalculatorService.calculate(expression);
        Assertions.assertEquals(39,result);
    }

    @Test
    public void test_multi_and_div(){
        String expression = "6+2*40/4";
        double result = RPNCalculatorService.calculate(expression);
        Assertions.assertEquals(26,result);
    }

    @Test
    public void test_long_parenthesis(){
        String expression = "6+(5-3+10)*2";
        double result = RPNCalculatorService.calculate(expression);
        Assertions.assertEquals(30,result);
    }

    @Test
    public void test_multi_parenthesis(){
        String expression = "5 + (3 * (-10))";
        double result = RPNCalculatorService.calculate(expression);
        Assertions.assertEquals(-25,result);
    }

    @Test
    public void test_include_minus_val(){
        String expression ="-6+(5-3+10)+-2";
        double result = RPNCalculatorService.calculate(expression);
        Assertions.assertEquals(4,result);
    }

    @Test
    public void test_invalid_operator(){
        assertThrows(IllegalArgumentException.class,() ->RPNCalculatorService.calculate("3 & 2"));
        assertThrows(IllegalArgumentException.class, () -> RPNCalculatorService.calculate("-6+(5-3+10+-2"));
        assertThrows(IllegalArgumentException.class, () -> RPNCalculatorService.calculate("-6+(5-3+(10+-2"));
    }



}

