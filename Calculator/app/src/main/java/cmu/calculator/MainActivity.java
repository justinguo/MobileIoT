package cmu.calculator;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnLongClickListener {

    TextView displayArea;
    private double leftArg, rightArg, result = 0;
    private int functionCode = -1;
    private String operands = "";
    private boolean isEmpty = true;
    Button clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayArea = (TextView) findViewById(R.id.output);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);
        clearBtn.setOnLongClickListener(this);
    }

    //append numeric to the display area when clicked
    public void appendNumber(View sender) {
        //handle the case when the operator has been selected
        if (functionCode != -1 && isEmpty == false) {
            displayArea.setText("");
            isEmpty = true;
        } else if (isEmpty == false) {
            displayArea.setText("");
            isEmpty = true;
        }
        Button btn = (Button) sender;
        displayArea.append(btn.getText());
    }

    //set the operand for calculation
    public void setOperand(View sender) {
        //handle the case when the function code already exists
        if (functionCode != -1) {
            return;
        } else if (displayArea.getText().toString().equals("") ||
                displayArea.getText().toString().indexOf("=") != -1) {
            toastMessage(sender);
            onLongClick(sender);
            return;
        }

        Button btn = (Button) sender;

        //set the function code for an operand
        if (btn.getText().toString().equals("+")) {
            functionCode = 0;
        } else if (btn.getText().toString().equals("-")) {
            functionCode = 1;
        } else if (btn.getText().toString().equals("*")) {
            functionCode = 2;
        } else if (btn.getText().toString().equals("/")) {
            functionCode = 3;
        }

        //get the first numeric
        leftArg = Double.parseDouble(displayArea.getText().toString());

        //add an operand
        operands = btn.getText().toString();
        displayArea.append(" " + btn.getText());
        isEmpty = false;
    }

    //check if the input expression is in the correct format
    public boolean checkExpression(View sender) {
        String input = displayArea.getText().toString();
        if (!input.equals("") && !operands.equals("") && input.indexOf(" ") == -1) {
            return true;
        } else {
            return false;
        }
    }

    public void getResult(View sender) {
        //validate the input expression
        if (!checkExpression(sender)) {
            toastMessage(sender);
            return;
        }

        //get the second numeric
        rightArg = Double.parseDouble(displayArea.getText().toString());

        //perform a basic operation to get the result
        if (functionCode == 0) {
            result = leftArg + rightArg;
        } else if (functionCode == 1) {
            result = leftArg - rightArg;
        } else if (functionCode == 2) {
            result = leftArg * rightArg;
        } else if (functionCode == 3) {
            if (rightArg == 0) {
                result = 0;
            } else {
                result = leftArg / rightArg;
            }
        }

        //show the entered expression in display area
        displayArea.setText(leftArg + " " + operands + " " + rightArg + " = " + result);

        //reset input variables
        leftArg = 0;
        rightArg = 0;
        result = 0;
        functionCode = -1;
        operands = "";
        isEmpty = false;
    }

    //add decimal to the input numbers
    public void addDecimal(View sender) {
        String display = displayArea.getText().toString();

        //if display contains non-integer values, return
        if (isOperand(sender, display)) {
            return;
        }

        //if the decimal is the first input, append 0. to the head
        else if (displayArea.getText().toString().equals("")) {
            displayArea.setText("0.");
            return;
        }

        //if the decimal is already in-place, return
        if (displayArea.getText().toString().indexOf(".") != -1) {
            return;
        } else {
            displayArea.append(".");
        }
    }

    //check whether the operand is valid
    public boolean isOperand(View sender, String str) {
        String[] operators = {"+", "-", "*", "/"};
        for (int i = 0; i < operators.length; i++) {
            if (str.contains(operators[i])) {
                return true;
            }
        }
        return false;
    }

    //display warning message if the input format is incorrect
    public void toastMessage(View v) {
        Toast message = Toast.makeText(getApplicationContext(), "Invalid Input Format!", Toast.LENGTH_SHORT);
        message.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, -250);
        message.show();
    }

    @Override
    //when the 'C' button is clicked and held, reset the calculator
    public boolean onLongClick(View v) {
        //reset variables
        leftArg = 0;
        rightArg = 0;
        result = 0;
        functionCode = -1;
        operands = "";
        isEmpty = true;
        displayArea.setText("");
        return true;
    }

    @Override
    //when the 'C' button is clicked, delete the last input value on the screen
    public void onClick(View v) {
        //original output string
        String curDisp = displayArea.getText().toString();
        //if empty, do nothing
        if (curDisp.equals("")) {
            return;
        } else { //remove last character
            String newDisp = curDisp.substring(0, curDisp.length() - 1);
            displayArea.setText(newDisp);
        }
    }
}