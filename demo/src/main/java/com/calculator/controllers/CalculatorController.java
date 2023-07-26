package com.calculator.controllers;

import com.calculator.services.IEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    @Autowired
    private IEvaluateService evaluateService;

    @PostMapping("/calculate")
    public Map<String, Double> evaluateAndCalculateExpression(@RequestBody String inputSeries) {

        return evaluateService.evaluateAndCalculateExpression(inputSeries);
    }

}
