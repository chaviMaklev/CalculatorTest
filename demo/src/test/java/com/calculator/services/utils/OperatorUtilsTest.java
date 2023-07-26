package com.calculator.services.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.BinaryOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperatorUtilsTest {

    @Autowired
    private OperatorUtils operatorUtils;

    @Test
    public void test_add_operator(){
        BinaryOperator binaryOperator = operatorUtils.getBinaryOperator("+");
        assertEquals(10.0, binaryOperator.apply(5.0,5.0));
    }

    @Test
    public void test_subtract_operator(){
        BinaryOperator binaryOperator = operatorUtils.getBinaryOperator("-");
        assertEquals(3.0, binaryOperator.apply(5.0,2.0));
    }

    @Test
    public void test_multy_operator(){
        BinaryOperator binaryOperator = operatorUtils.getBinaryOperator("*");
        assertEquals(12.0, binaryOperator.apply(6.0,2.0));
    }

    @Test
    public void test_div_operator(){
        BinaryOperator binaryOperator = operatorUtils.getBinaryOperator("/");
        assertEquals(2.0, binaryOperator.apply(8.0,4.0));
        assertThrows(ArithmeticException.class, () -> binaryOperator.apply(4.0, 0.0));

    }



}