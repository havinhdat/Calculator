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


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private enum Operator {ADD, SUB, MUL, DIV}

    private TextView tvResult;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0,
            btnPlus, btnMinus, btnMul, btnDiv, btnDot, btnDel, btnRes;
    private HorizontalScrollView horizontalScrollView;
    private boolean delSingle = true;
    private String calculation = "";

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
                    if (isOperator(calculation.charAt(calculation.length() - 1)))
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

    private double calResult() {
        double result = 0.0;

        /*List<Operator> operators = new ArrayList<Operator>();
        List<Double> nums = new ArrayList<Double>();
        int start, end;
        start = 0;
        for (int i = 0; i < calculation.length(); i++) {
            if (isOperator(calculation.charAt(i)) || i == calculation.length() - 1) {
                end = i;
                if (end == calculation.length() - 1 && !isOperator(calculation.charAt(i))) end++;
                switch (calculation.charAt(i)) {
                    case '+':
                        operators.add(Operator.ADD);
                        break;
                    case '-':
                        operators.add(Operator.SUB);
                        break;
                    case 'x':
                        operators.add(Operator.MUL);
                        break;
                    case '÷':
                        operators.add(Operator.DIV);
                        break;
                }

                try {
                    nums.add(Double.parseDouble(calculation.substring(start, end)));
                    start = end + 1;

                } catch (Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (isOperator(calculation.charAt(calculation.length() - 1))) {
            operators.remove(operators.size() - 1);
        }
*/
        //handle the calculation


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
