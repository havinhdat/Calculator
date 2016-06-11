package com.a1353008_1353049.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private enum Operator {ADD, SUB, MUL, DIV}

    private TextView tvResult;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0,
            btnPlus, btnMinus, btnMul, btnDiv, btnDot, btnDel, btnRes;
    private HorizontalScrollView horizontalScrollView;
    private boolean delSingle = true;
    private String calculation = "";
    private boolean isNaNorInf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();

    }

    protected void initializeComponents() {
        tvResult = (TextView) findViewById(R.id.tvResult);
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMul = (Button) findViewById(R.id.btnMultiply);
        btnDiv = (Button) findViewById(R.id.btnDivide);
        btnDot = (Button) findViewById(R.id.btnDot);
        btnDel = (Button) findViewById(R.id.btnDelete);
        btnRes = (Button) findViewById(R.id.btnRes);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
        btnDot.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnRes.setOnClickListener(this);

        btnDel.setOnLongClickListener(this);

        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.hScroll);

        btnDel.setText("<=");
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        if (isNaNorInf){
            calculation = "";
            isNaNorInf = false;
        }
        switch (v.getId()) {
            case R.id.btn0:
                if (!(calculation.length() == 1 && calculation.charAt(calculation.length() - 1) == '0') ||
                        (calculation.length() > 0 && !(calculation.charAt(calculation.length() - 1) == '0'))) {
                    calculation += button.getText().toString();
                }
                break;
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
                calculation += button.getText().toString();
                break;
            case R.id.btnMinus:
                if (calculation.length() > 0 && calculation.charAt(calculation.length() - 1) == '-') {
                    break;
                }
                if (calculation.length() > 0 && calculation.charAt(calculation.length() - 1) == '+') {
                    calculation = calculation.substring(0, calculation.length() - 1);
                }
                calculation += button.getText().toString();
                break;
            case R.id.btnPlus:
            case R.id.btnMultiply:
            case R.id.btnDivide:
                if (calculation.length() == 0) {
                    break;
                }

                if (calculation.length() > 0 && isOperator(calculation.charAt(calculation.length() - 1))) {
                    calculation = calculation.substring(0, calculation.length() - 1);
                    if (calculation.length() > 0 && isOperator(calculation.charAt(calculation.length() - 1)))
                        calculation = calculation.substring(0, calculation.length() - 1);
                }
                calculation += button.getText().toString();
                break;
            case R.id.btnDelete:
                try {
                    if (delSingle && calculation.length() > 0)
                        calculation = calculation.substring(0, calculation.length() - 1);
                    else {
                        calculation = "";
                        delSingle = true;
                        btnDel.setText("<=");
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDot:
                if ((calculation.length() > 0 && isOperator(calculation.charAt(calculation.length() - 1))) ||
                        calculation.length() == 0) {
                    calculation += "0.";
                } else if (!((calculation.length() > 0 && calculation.charAt(calculation.length() - 1) == '.'))) {
                    if (!hasDot())
                        calculation += button.getText().toString();
                }
                break;
            case R.id.btnRes:
                delSingle = false;
                btnDel.setText("C");

                calculation = prettyPrint(calResult());
                break;
        }

        if (!delSingle){
            if (v.getId() != R.id.btnDelete && v.getId() != R.id.btnRes){
                delSingle = true;
                btnDel.setText("<=");
            }
        }
        tvResult.setText(calculation);
        horizontalScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        }, 10);
    }

    public static String prettyPrint(double d) {
        int i = (int) d;
        return d == i ? String.valueOf(i) : String.valueOf(d);
    }

    private boolean isOperator(Character c) {
        return (c.equals('+') || c.equals('-') || c.equals('x') || c.equals('÷'));
    }

    private boolean isMinusSign(Character c){
        return c.equals('-');
    }

    private int getPriority(Character c){
        int priority = 0;
        switch (c){
            case '-':
            case '+':
                priority = 0;
                break;
            case '÷':
            case 'x':
                priority = 1;
                break;
        }
        return priority;
    }

    protected class OP{
        public String data = "";
        public boolean isOperator = false;
    }

    /**
     * delete redundant operator
     *
     * @param expression
     * @return
     */
    public String makeup(String expression){
        String prettyExpression;

        int index = expression.length() - 1;

        while (index >= 0){
            if (!isOperator(expression.charAt(index))){
                break;
            }
            index--;
        }

        prettyExpression = expression.substring(0, index + 1);

        return prettyExpression;
    }

    /**
     * transform infix expression to postfix using Polish notation
     *
     * @param expression
     * @return
     */
    public List<OP> infix2postfix(String expression)
    {
        List<OP> output = new ArrayList<OP>();

        int expression_length = expression.length();
        Stack<OP> opStack = new Stack<OP>();

        int index = 0;

        Character cur;

        while (index<expression_length)
        {
            cur = expression.charAt(index);

            // current character is operator or not
            if (isOperator(cur)){
                //current character is minus sign of negative number or not
                if(isMinusSign(cur) && (index == 0 || isOperator(expression.charAt(index-1)))){
                    // current character IS minus sign of negative number
                    OP number = new OP();

                    // add minus sign to number
                    number.data += '-';

                    // set OP flag that OP is not a operator
                    number.isOperator = false;

                    index++;

                    // get rest numeric
                    for(;index < expression_length; index++){
                        // character is operator -> end of number
                        if (isOperator(expression.charAt(index))){
                            break;
                        }
                        number.data += expression.charAt(index);
                    }

                    // add number to output
                    output.add(number);

                }
                //current character is NOT minus sign of negative number
                else{
                    OP operator = new OP();

                    // add character sign to operator
                    operator.data += expression.charAt(index);

                    // set OP flag that OP is a operator
                    operator.isOperator = true;

                    index++;

                    //put operator to empty stack
                    if (opStack.isEmpty()){
                        opStack.push(operator);
                    }
                    else{
                        // stack not empty
                        // pop an operator and compare priority
                        OP in_stack = opStack.pop();

                        if (getPriority(in_stack.data.charAt(0)) >= getPriority(operator.data.charAt(0))){
                            output.add(in_stack);
                            opStack.push(operator);
                        }
                        else {
                            opStack.push(in_stack);
                            opStack.push(operator);
                        }
                    }
                }
            }
            else{
                // current character is numeric
                OP number = new OP();

                // set OP flag that OP is not a operator
                number.isOperator = false;

                // get whole numeric
                for(;index < expression_length; index++){
                    // character is operator -> end of number
                    if (isOperator(expression.charAt(index))){
                        break;
                    }
                    number.data += expression.charAt(index);
                }

                // add number to output
                output.add(number);
            }
        }

        // put rest of stack to output
        while (!opStack.isEmpty()){
            output.add(opStack.pop());
        }

        return output;
    }

    /**
     *  evaluation of postfix expression
     *
     * @param postfix
     * @return
     */
    public double evaluate(List<OP> postfix){
        double result;

        Stack<OP> opStack = new Stack<OP>();

        int postfix_length = postfix.size();

        OP current;

        for(int i = 0; i < postfix_length; i++){

            current = postfix.get(i);

            if (!current.isOperator)
            {
                opStack.push(current);
            }
            else {

                // get 2 operand

                OP operand1 = opStack.pop();
                OP operand2 = opStack.pop();

                // calculation
                OP res = new OP();

                res.isOperator = false;

                switch (current.data.charAt(0)){
                    case '-':
                        res.data = String.valueOf(Double.parseDouble(operand2.data) - Double.parseDouble(operand1.data));
                        break;
                    case '+':
                        res.data = String.valueOf(Double.parseDouble(operand2.data) + Double.parseDouble(operand1.data));
                        break;
                    case '÷':
                        res.data = String.valueOf(Double.parseDouble(operand2.data) / Double.parseDouble(operand1.data));
                        break;
                    case 'x':
                        res.data = String.valueOf(Double.parseDouble(operand2.data) * Double.parseDouble(operand1.data));
                        break;
                }

                opStack.push(res);
            }
        }

        result = Double.parseDouble(opStack.peek().data);

        return result ;
    }

    /**
     * calculate string expression
     *
     * @return
     */
    private double calResult() {
        double result = 0.0;
        //handle the calculation

        // if expression is empty
        if (calculation.equals(""))
            return result;

        // convert infix expression to postfix
        List<OP> postfix = infix2postfix(makeup(calculation));

        // evaluate postfix expression
        result = evaluate(postfix);

        isNaNorInf = false;
        if (Double.isInfinite(result) || Double.isNaN(result))
            isNaNorInf = true;

        return result;
    }

    private boolean hasDot() {
        for (int i = calculation.length() - 1; i >= 0; i--) {
            if (calculation.charAt(i) == '.') {
                return true;
            }

            if (isOperator(calculation.charAt(i))) {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.btnDelete) {
            calculation = "";
            tvResult.setText("");
        }
        return false;
    }
}
