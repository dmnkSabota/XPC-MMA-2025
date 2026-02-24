package com.example.calculator;

// ─────────────────────────────────────────────────────────────────────────────
// IMPORTS
// Standard Android imports for UI, lifecycle, and configuration handling.
// mXparser is the external math library added via local JAR in app/libs/.
// ─────────────────────────────────────────────────────────────────────────────
import android.content.res.Configuration;          // Lets us detect orientation (portrait/landscape)
import android.os.Bundle;                          // Used to pass state into onCreate()
import android.view.View;                          // Base class for all UI elements
import android.widget.Button;                      // Button widget
import android.widget.TextView;                    // Text display widget
import androidx.appcompat.app.AppCompatActivity;   // Base Activity class with modern Android support

import org.mariuszgromada.math.mxparser.Expression; // External library: evaluates math expression strings


public class MainActivity extends AppCompatActivity {

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 1: UI REFERENCES
    // These two TextViews are the visual display of the calculator.
    // tvExpression = top row, shows the full equation being built (e.g. "sin(30)+2²")
    // tvDisplay    = bottom row, shows what the user is currently typing or the result
    // ─────────────────────────────────────────────────────────────────────────
    private TextView tvDisplay;
    private TextView tvExpression;


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 2: CALCULATOR STATE VARIABLES
    // These strings track the calculator's memory across button presses.
    //
    // We maintain TWO parallel expression strings because:
    //   - mXparser needs its own syntax  (e.g. "asin(" for inverse sine, "#" for modulo)
    //   - The user should see nice symbols (e.g. "sin⁻¹(",              "mod")
    // ─────────────────────────────────────────────────────────────────────────
    private String expressionForEval    = "";  // String passed to mXparser for evaluation
    private String expressionForDisplay = "";  // String shown to the user in tvExpression
    private String currentInput         = "";  // Number currently being typed (not yet committed)
    private boolean resultShown         = false; // True after "=" is pressed — next input starts fresh
    private boolean insideFn            = false; // True when user is typing an argument inside a function
    private String fnName               = "";    // Name of the currently open function (e.g. "sin(")


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 3: LIFECYCLE — onCreate()
    // Called once when the Activity is first created.
    // Chooses which XML layout to inflate based on current orientation,
    // then wires up all views and buttons.
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load landscape layout (with scientific buttons) or portrait (basic) layout
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land); // res/layout-land/activity_main_land.xml
        } else {
            setContentView(R.layout.activity_main);      // res/layout/activity_main.xml
        }

        bindViews();       // Connect Java variables to XML views
        setupAllButtons(); // Attach click listeners to all buttons
    }


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 4: LIFECYCLE — onConfigurationChanged()
    // Called instead of destroying/recreating the Activity on rotation.
    // This works because AndroidManifest.xml has:
    //   android:configChanges="orientation|screenSize|keyboardHidden"
    //
    // Without this, all state variables would be wiped on every rotation.
    // With this, we manually swap the layout while keeping all state intact.
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Swap to the correct layout for the new orientation
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land);
        } else {
            setContentView(R.layout.activity_main);
        }

        bindViews();        // Re-bind views (new layout = new view objects)
        setupAllButtons();  // Re-attach listeners (new layout = new button objects)
        restoreDisplays();  // Put the previous expression/input back on screen
    }


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 5: VIEW BINDING
    // Connects the Java TextView variables to the actual views in the XML layout.
    // Must be called again after every setContentView() call.
    // ─────────────────────────────────────────────────────────────────────────
    private void bindViews() {
        tvDisplay    = findViewById(R.id.tvDisplay);
        tvExpression = findViewById(R.id.tvExpression);
    }

    // Restores the displayed text after a layout swap (rotation).
    // Called only from onConfigurationChanged(), not onCreate().
    private void restoreDisplays() {
        tvDisplay.setText(currentInput.isEmpty() ? "0" : currentInput);
        tvExpression.setText(expressionForDisplay);
    }


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 6: BUTTON SETUP — Entry Point
    // Calls three sub-methods to keep setup organized by button type.
    // safeClick() is used throughout — it checks if a button exists before
    // attaching a listener, so portrait layout doesn't crash on scientific IDs.
    // ─────────────────────────────────────────────────────────────────────────
    private void setupAllButtons() {
        setupNumberButtons();
        setupBasicOperatorButtons();
        setupScientificButtons();  // Scientific buttons only exist in landscape layout
    }

    // Null-safe click helper — finds the view by ID, only sets listener if it exists.
    // This prevents NullPointerException when portrait layout doesn't have scientific buttons.
    private void safeClick(int id, View.OnClickListener listener) {
        View v = findViewById(id);
        if (v != null) v.setOnClickListener(listener);
    }


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 7: NUMBER BUTTONS (0–9, 00, decimal point)
    // All number buttons share the same listener — they read their own label
    // via getText() and pass it to onNumberClick().
    // ─────────────────────────────────────────────────────────────────────────
    private void setupNumberButtons() {
        int[] ids = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btn00, R.id.btnDot
        };
        // Lambda: cast the clicked view to Button, read its text, forward to handler
        View.OnClickListener listener = v -> onNumberClick(((Button) v).getText().toString());
        for (int id : ids) {
            View btn = findViewById(id);
            if (btn != null) btn.setOnClickListener(listener);
        }
    }

    // Handles all digit/decimal input.
    private void onNumberClick(String digit) {
        // If a result is already shown, pressing a number starts a brand new calculation
        if (resultShown) {
            expressionForEval    = "";
            expressionForDisplay = "";
            currentInput         = "";
            resultShown          = false;
        }

        if (digit.equals(".")) {
            // Prevent double decimal — only add dot if currentInput doesn't already have one
            if (!currentInput.contains(".")) {
                currentInput = currentInput.isEmpty() ? "0." : currentInput + ".";
            }
        } else if (digit.equals("00")) {
            // "00" button only works if there's already a non-zero number typed
            if (!currentInput.isEmpty() && !currentInput.equals("0")) {
                currentInput += "00";
            }
        } else {
            // Normal digit: replace leading "0" or append
            if (currentInput.equals("0")) {
                currentInput = digit;
            } else {
                currentInput += digit;
            }
        }

        tvDisplay.setText(currentInput.isEmpty() ? "0" : currentInput);
    }


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 8: BASIC OPERATOR BUTTONS (+, −, ×, ÷, C, DEL, =)
    // ─────────────────────────────────────────────────────────────────────────
    private void setupBasicOperatorButtons() {
        int[] ids = { R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide };
        View.OnClickListener listener = v -> onOperatorClick(((Button) v).getText().toString());
        for (int id : ids) {
            View btn = findViewById(id);
            if (btn != null) btn.setOnClickListener(listener);
        }

        safeClick(R.id.btnClear,  v -> clearAll());
        safeClick(R.id.btnDelete, v -> onDeleteClick());
        safeClick(R.id.btnEquals, v -> onEqualsClick());
    }

    // Handles +, −, ×, ÷ (and ! from scientific).
    // Converts display symbols to mXparser syntax before storing in expressionForEval.
    private void onOperatorClick(String displayOp) {
        // Map display symbols → mXparser syntax
        String evalOp = displayOp
                .replace("×", "*")
                .replace("÷", "/")
                .replace("mod", "#"); // mXparser uses '#' for modulo, not '%'

        if (!currentInput.isEmpty()) {
            // Commit the typed number + operator to both expression strings
            expressionForEval    += currentInput + evalOp;
            expressionForDisplay += currentInput + displayOp;
            currentInput = ""; // Number is now part of the expression, clear the input buffer
        } else if (!expressionForEval.isEmpty()) {
            // No number typed yet — just append the operator (e.g. user pressed × twice)
            expressionForEval    += evalOp;
            expressionForDisplay += displayOp;
        }

        resultShown = false;
        tvExpression.setText(expressionForDisplay);
        tvDisplay.setText("0");
    }

    // Deletes one character at a time — from currentInput first, then from expression.
    private void onDeleteClick() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            tvDisplay.setText(currentInput.isEmpty() ? "0" : currentInput);
        } else if (!expressionForEval.isEmpty()) {
            // Remove last character from both parallel strings simultaneously
            expressionForEval    = expressionForEval.substring(0, expressionForEval.length() - 1);
            expressionForDisplay = expressionForDisplay.substring(0, expressionForDisplay.length() - 1);
            tvExpression.setText(expressionForDisplay);
        }
    }

    // Resets all state back to zero — called by "C" button or on error.
    private void clearAll() {
        expressionForEval    = "";
        expressionForDisplay = "";
        currentInput         = "";
        resultShown          = false;
        insideFn             = false;
        fnName               = "";
        tvDisplay.setText("0");
        tvExpression.setText("");
    }


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 9: SCIENTIFIC BUTTONS (landscape only)
    // All of these use safeClick() — safe to call even in portrait where they
    // don't exist. The null check inside safeClick() handles that silently.
    // ─────────────────────────────────────────────────────────────────────────
    private void setupScientificButtons() {

        // ── Trigonometric functions ──────────────────────────────────────────
        // Each calls startFunction() with two strings:
        //   1st = mXparser function name  (what gets evaluated)
        //   2nd = display label           (what the user sees)
        safeClick(R.id.btnSin,  v -> startFunction("sin(",  "sin("));
        safeClick(R.id.btnCos,  v -> startFunction("cos(",  "cos("));
        safeClick(R.id.btnTan,  v -> startFunction("tan(",  "tan("));
        safeClick(R.id.btnAsin, v -> startFunction("asin(", "sin⁻¹(")); // Inverse sine
        safeClick(R.id.btnAcos, v -> startFunction("acos(", "cos⁻¹(")); // Inverse cosine
        safeClick(R.id.btnAtan, v -> startFunction("atan(", "tan⁻¹(")); // Inverse tangent

        // ── Logarithmic / exponential functions ──────────────────────────────
        safeClick(R.id.btnLn,  v -> startFunction("ln(",    "ln("));     // Natural log
        safeClick(R.id.btnLog, v -> startFunction("log10(", "log("));    // Base-10 log
        safeClick(R.id.btnExp, v -> startFunction("exp(",   "exp("));    // e^x

        // ── Factorial — treated as a postfix operator (e.g. "5!") ────────────
        safeClick(R.id.btnFact, v -> onOperatorClick("!"));

        // ── Square (x²) ──────────────────────────────────────────────────────
        // Appends "^2" to the eval string and "²" to the display string
        safeClick(R.id.btnSq, v -> {
            if (!currentInput.isEmpty()) {
                appendToExpression(currentInput + "^2", currentInput + "²");
                currentInput = ""; // Number is now committed into the expression
            }
        });

        // ── Power of Y (x^y) ─────────────────────────────────────────────────
        safeClick(R.id.btnPow, v -> onOperatorClick("^"));

        // ── Y-th Root ────────────────────────────────────────────────────────
        // mXparser has no native yroot() function, so we use the math identity:
        //   ʸ√x  =  x^(1/y)
        // Example: cube root of 8 → user types "8", presses ʸ√, types "3", presses "="
        //   builds: "8^(1/3)" → mXparser evaluates → 2
        // The closing ")" is added automatically by onEqualsClick().
        safeClick(R.id.btnYRoot, v -> {
            if (!currentInput.isEmpty()) {
                appendToExpression(currentInput + "^(1/", currentInput + "ʸ√");
                currentInput = "";
            }
        });

        // ── Modulo ───────────────────────────────────────────────────────────
        // mXparser's modulo operator is "#" (not "%" like most languages)
        // Display shows "mod" but eval string stores "#"
        safeClick(R.id.btnMod, v -> {
            if (!currentInput.isEmpty()) {
                appendToExpression(currentInput + "#", currentInput + " mod ");
                currentInput = "";
            }
        });

        // ── Pi (π) ───────────────────────────────────────────────────────────
        // mXparser recognises "pi" as a built-in constant (3.14159...)
        // We store "pi" in currentInput for evaluation, but show "π" on screen
        safeClick(R.id.btnPi, v -> {
            if (resultShown) {
                expressionForEval    = "";
                expressionForDisplay = "";
                resultShown          = false;
            }
            currentInput = "pi";       // mXparser constant keyword
            tvDisplay.setText("π");    // Pretty symbol for the user
        });
    }

    // Handles pressing a function button (sin, cos, ln, etc.).
    // If there's already a number typed, it gets committed to the expression first,
    // then the function name is appended — waiting for the user to type the argument.
    private void startFunction(String evalFn, String displayFn) {
        if (!currentInput.isEmpty()) {
            // Commit any pending number before the function (e.g. "2 * sin(")
            expressionForEval    += currentInput;
            expressionForDisplay += currentInput;
            currentInput = "";
        }
        // Append the function opening to both strings (e.g. "sin(" / "sin(")
        expressionForEval    += evalFn;
        expressionForDisplay += displayFn;
        insideFn    = true;   // Flag that we're now inside a function argument
        fnName      = evalFn; // Remember which function we're in
        resultShown = false;
        tvExpression.setText(expressionForDisplay);
        tvDisplay.setText("0");
    }

    // Simple helper: appends parts to both expression strings and refreshes tvExpression.
    // Used by btnSq, btnYRoot, btnMod.
    private void appendToExpression(String evalPart, String displayPart) {
        expressionForEval    += evalPart;
        expressionForDisplay += displayPart;
        tvExpression.setText(expressionForDisplay);
    }


    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 10: EVALUATION — onEqualsClick()
    // Triggered by the "=" button.
    // Steps:
    //   1. Commit any remaining currentInput into the expression
    //   2. Auto-close unclosed parentheses (needed for functions like sin(), y-root)
    //   3. Pass the complete expression string to mXparser
    //   4. Show result
    // ─────────────────────────────────────────────────────────────────────────
    private void onEqualsClick() {
        // Step 1: Commit whatever number is still being typed
        if (!currentInput.isEmpty()) {
            expressionForEval    += currentInput;
            expressionForDisplay += currentInput;
            currentInput = "";
        }

        // Step 2: Count open vs closed parentheses and append missing ")"
        // This handles: sin(45  →  sin(45)
        //               8^(1/3  →  8^(1/3)
        long opens   = expressionForEval.chars().filter(c -> c == '(').count();
        long closes  = expressionForEval.chars().filter(c -> c == ')').count();
        long missing = opens - closes;
        for (long i = 0; i < missing; i++) {
            expressionForEval    += ")";
            expressionForDisplay += ")";
        }

        if (expressionForEval.isEmpty()) return; // Nothing to evaluate

        // Show the complete equation with "=" appended in tvExpression
        tvExpression.setText(expressionForDisplay + "=");

        // Step 3 & 4: Evaluate and display result
        double result = evaluate(expressionForEval);
        showResult(result);
    }

    // Passes the expression string to mXparser and returns the numeric result.
    // Returns Double.NaN on any parse or math error.
    private double evaluate(String expr) {
        try {
            Expression e = new Expression(expr); // mXparser parses the string
            return e.calculate();                 // mXparser computes the result
        } catch (Exception ex) {
            return Double.NaN; // Catch any unexpected errors safely
        }
    }

    // Formats the result and updates the display.
    // If result is a whole number, show without decimal point (e.g. "42" not "42.0").
    private void showResult(double result) {
        String resultStr;
        if (Double.isNaN(result)) {
            resultStr = "Error"; // mXparser returned NaN (e.g. sqrt of negative number)
            clearAll();
        } else if (result == (long) result) {
            resultStr = String.valueOf((long) result); // Integer result — strip ".0"
        } else {
            resultStr = String.valueOf(result);        // Decimal result
        }

        tvDisplay.setText(resultStr);
        currentInput = resultStr; // Store result as input so user can chain calculations
        resultShown  = true;
        insideFn     = false;
    }
}