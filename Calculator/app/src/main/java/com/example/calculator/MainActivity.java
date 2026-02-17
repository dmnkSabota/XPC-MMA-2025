package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private String currentInput = "";
    private String operator = "";
    private double firstValue = 0;
    private boolean isNewOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // loads XML

        tvDisplay = findViewById(R.id.tvDisplay); // Connects tvDisplay to the TextView

        setupNumberButtons();
        setupOperatorButtons();
        setupFunctionButtons();
    }

    /**
     * Click listeners for all number buttons (0-9, 00, .)
     */
    private void setupNumberButtons() {
        // Array of number button IDs
        int[] numberButtonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btn00, R.id.btnDot
        };

        View.OnClickListener numberClickListener = v -> {
            Button button = (Button) v;
            String buttonText = button.getText().toString();

            // Handle decimal point
            if (buttonText.equals(".")) {
                if (currentInput.isEmpty()) {
                    currentInput = "0.";
                } else if (!currentInput.contains(".")) {
                    currentInput += ".";
                }
            } else {
                // Handle numbers
                if (isNewOperation) {
                    currentInput = buttonText;
                    isNewOperation = false;
                } else {
                    if (currentInput.equals("0") && !buttonText.equals("00")) {
                        currentInput = buttonText;
                    } else {
                        currentInput += buttonText;
                    }
                }
            }

            updateDisplay(currentInput);
        };

        // Attach listener to all number buttons
        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(numberClickListener);
        }
    }

    /**
     * Setup click listeners for operator buttons (+, -, ×, ÷)
     */
    private void setupOperatorButtons() {
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnSubtract = findViewById(R.id.btnSubtract);
        Button btnMultiply = findViewById(R.id.btnMultiply);
        Button btnDivide = findViewById(R.id.btnDivide);

        View.OnClickListener operatorClickListener = v -> {
            Button button = (Button) v;

            // If there's already an operator, calculate first
            if (!operator.isEmpty() && !currentInput.isEmpty() && !isNewOperation) {
                calculateResult();
            } else if (!currentInput.isEmpty()) {
                firstValue = Double.parseDouble(currentInput);
            }

            operator = button.getText().toString();
            isNewOperation = true;
        };

        btnAdd.setOnClickListener(operatorClickListener);
        btnSubtract.setOnClickListener(operatorClickListener);
        btnMultiply.setOnClickListener(operatorClickListener);
        btnDivide.setOnClickListener(operatorClickListener);
    }

    /**
     * Setup click listeners for function buttons (C, DEL, =)
     */
    private void setupFunctionButtons() {
        // Clear button
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> clearAll());

        // Delete button
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            if (!currentInput.isEmpty() && !isNewOperation) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                if (currentInput.isEmpty()) {
                    currentInput = "0";
                }
                updateDisplay(currentInput);
            }
        });

        // Equals button
        Button btnEquals = findViewById(R.id.btnEquals);
        btnEquals.setOnClickListener(v -> {
            if (!operator.isEmpty() && !currentInput.isEmpty() && !isNewOperation) {
                calculateResult();
            }
        });
    }

    /**
     * Perform calculation based on the selected operator
     */
    private void calculateResult() {
        if (currentInput.isEmpty()) return;

        double secondValue = Double.parseDouble(currentInput);
        double result = 0;

        switch (operator) {
            case "+":
                result = firstValue + secondValue;
                break;
            case "-":
                result = firstValue - secondValue;
                break;
            case "×":
                result = firstValue * secondValue;
                break;
            case "÷":
                if (secondValue != 0) {
                    result = firstValue / secondValue;
                } else {
                    updateDisplay("Error");
                    clearAll();
                    return;
                }
                break;
        }

        // Format result (remove unnecessary decimals)
        String resultString;
        if (result == (long) result) {
            resultString = String.valueOf((long) result);
        } else {
            resultString = String.valueOf(result);
        }

        updateDisplay(resultString);
        currentInput = resultString;
        firstValue = result;
        operator = "";
        isNewOperation = true;
    }

    /**
     * Clear all calculator state
     */
    private void clearAll() {
        currentInput = "";
        operator = "";
        firstValue = 0;
        isNewOperation = true;
        updateDisplay("0");
    }

    /**
     * Update the display TextView
     */
    private void updateDisplay(String value) {
        tvDisplay.setText(value);
    }
}