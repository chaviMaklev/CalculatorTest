package com.calculator.services.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpressionValidatorUtilsTests {

    @Autowired
    private ExpressionValidatorUtils expressionValidatorUtils;

    @Test
    public void testValidIdentifier() {
        // Valid identifiers
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidIdentifier("x"));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidIdentifier("variable"));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidIdentifier("_var"));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidIdentifier("Var_123"));

        // Invalid identifiers
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidIdentifier("123"));
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidIdentifier("var name"));
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidIdentifier("$variable"));
    }

    @Test
    public void testValidAssignmentOperator() {
        // Valid operators
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidAssignmentOperator("="));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidAssignmentOperator("+="));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidAssignmentOperator("-="));

        // Invalid operators
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidAssignmentOperator("=="));
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidAssignmentOperator("+"));
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidAssignmentOperator("-"));
    }
//
    @Test
    public void testValidMathExpression() {
        // Valid expressions
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidMathExpression("3 + 2"));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidMathExpression("(x + y)*8"));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidMathExpression("x + 2 * y"));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidMathExpression("-x + 2 * y"));
        assertDoesNotThrow(() -> expressionValidatorUtils.isValidMathExpression("x + (-2 * y)"));

        // Invalid expressions
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidMathExpression("x+/2"));
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidMathExpression("(x +"));
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidMathExpression("x + * y"));
        assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidMathExpression("x + y)"));
        //assertThrows(IllegalArgumentException.class, () -> expressionValidatorUtils.isValidMathExpression("34/a2(b)"));
    }

}

