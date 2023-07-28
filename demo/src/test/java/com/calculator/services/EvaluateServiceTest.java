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

@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluateServiceTest {

    @Autowired
    private EvaluateService evaluateService;

    private Map<String, Double> variables;

    @Before
    public void setUp() {
        variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 2.0);
        variables.put("z", 3.0);
        variables.put("a", 2.0);
    }

    /// Tests for : handleIncrementsDecrementsAndPrepareExpression
    @Test
    public void testSimpaleValidExpressionsWithoutIncrementsDecrements() {
        assertEquals("1.0 + 2.0", evaluateService.handleIncrementsDecrementsAndPrepareExpression("x + y", variables));
    }

    @Test
    public void testValidExpressionsWithoutIncrementsDecrements() {
        assertEquals("(1.0 + 2.0) * 3.0", evaluateService.handleIncrementsDecrementsAndPrepareExpression("(x + y) * z", variables));
    }

    @Test
    public void test_same_variable_twice(){
        String s = "a++ * ++a";
        Assertions.assertEquals("2.0 * 4.0", evaluateService.handleIncrementsDecrementsAndPrepareExpression(s,variables));
        Assertions.assertEquals(4.0, variables.get("a"));
    }

    @Test
    public void build_math_expression_test_pre_subtract(){
        String s =  "--x + 5";
        Assertions.assertEquals("0.0 + 5", evaluateService.handleIncrementsDecrementsAndPrepareExpression(s,variables));
        Assertions.assertEquals(0.0, variables.get("x"));
    }

    @Test
    public void build_math_expression_test_post_subtract(){
        String s =  "y-- + 5";
        Assertions.assertEquals("2.0 + 5", evaluateService.handleIncrementsDecrementsAndPrepareExpression(s,variables));
        Assertions.assertEquals(1.0, variables.get("y"));
    }

    @Test
    public void build_math_expression_test_multi_vars(){
        String s =  "y-- + 5 * ++z + 6";
        Assertions.assertEquals("2.0 + 5 * 4.0 + 6", evaluateService.handleIncrementsDecrementsAndPrepareExpression(s,variables));
        Assertions.assertEquals(1.0, variables.get("y"));
        Assertions.assertEquals(4.0, variables.get("z"));
    }

    @Test
    public void build_math_expression_test_multi_vars_parenthesis(){
        String s =  "(--y + 5) * (++z + 6)";
        Assertions.assertEquals("(1.0 + 5) * (4.0 + 6)", evaluateService.handleIncrementsDecrementsAndPrepareExpression(s,variables));
        Assertions.assertEquals(1.0, variables.get("y"));
        Assertions.assertEquals(4.0, variables.get("z"));
    }

    @Test
    public void build_math_expression_test_multi_vars_mius_begin(){
        String s =  "-6 + y-- + 5 * ++z";
        Assertions.assertEquals("-6 + 2.0 + 5 * 4.0", evaluateService.handleIncrementsDecrementsAndPrepareExpression(s,variables));
        Assertions.assertEquals(1.0, variables.get("y"));
        Assertions.assertEquals(4.0, variables.get("z"));
    }


    @Test
    public void test_end_to_end(){
        Map<String,Double> results = evaluateService.evaluateAndCalculateExpression(
                "i = 0\n" +
                        "j = ++i\n" +
                        "x = i++ + 5\n" +
                        "y = 5 + 3 * 10\n" +
                        "i += y");
        Assertions.assertEquals(results.get("x"), 6.0);
        Assertions.assertEquals(results.get("i"),37.0);
        Assertions.assertEquals(results.get("y"),35.0);
        Assertions.assertEquals(results.get("j"), 1.0);
    }

    @Test
    public void test_end_to_end_with_minus(){
        Map<String,Double> results = evaluateService.evaluateAndCalculateExpression(
                "i = 0\n" +
                        "j = --i\n" +
                        "x = --i + 5 * --i\n" +
                        "y = 5 + 3 * 10\n" +
                        "i += y");
        Assertions.assertEquals(results.get("x"), -17.0);
        Assertions.assertEquals(results.get("i"),32.0);
        Assertions.assertEquals(results.get("y"),35.0);
        Assertions.assertEquals(results.get("j"), -1.0);

    }


}



