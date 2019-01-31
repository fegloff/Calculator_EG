package com.egloffgroup.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private TextView mainScreenTV;
    private TextView formulaScreenTV;
    private double numberA;
    private double numberB;
    private double memory;
    //private int decimalPrecision;

    private final int NONE = 0;
    private final int ADD = 1;
    private final int SUB = 2;
    private final int DIV = 3;
    private final int MUL = 4;
    private final int EQUAL = 5;
    private final int ERROR = 6;
    private int operation = NONE;
    private final float textSize = 36;

    private String hundredSymbol = ",";
    private String decimalSymbol = ".";
    private String pattern = "#"+hundredSymbol+"###";

    private boolean numberInput = true;
    private boolean decimal;

    private String getOperationSymbol() {
        switch (operation) {
            case ADD:
                return " + ";
            case SUB:
                return " - ";
            case DIV:
                return " / ";
            default:
                return "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainScreenTV = (findViewById(R.id.textView_mainScreen));
        formulaScreenTV = (findViewById(R.id.textView_formulaScreen));
        decimal = false;
        numberInput = false;
        operation = NONE;
        numberA = Double.NaN;
        numberB = Double.NaN;
        memory = Double.NaN;
        //decimalPrecision = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.forex:
                Toast.makeText(this, "Forex option selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings option selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.personal_finance:
                Toast.makeText(this, "Personal Finance selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.stock:
                Toast.makeText(this, "Stock option selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearButton(View v) {
        decimal = false;
        mainScreenTV.setText("");
        formulaScreenTV.setText("");
        operation = NONE;
        numberA = Double.NaN;
        numberB = Double.NaN;
        numberInput = false;
        mainScreenTV.setTextSize(textSize);
        //decimalPrecision = 0;
    }

    public void delButton(View v) {
        String newNum;
        int length = mainScreenTV.getText().toString().length();
        if (length > 0) {
            newNum = mainScreenTV.getText().toString().substring(0, length -1);
            addNumberToScreen(newNum);
            if (!newNum.contains(".") && decimal) {
                decimal = false;
            }
        }
    }

    public void mPlusButton(View v) {
        if (Double.isNaN(memory)) {
            memory = 0.0;
        }
        memory += Double.valueOf(mainScreenTV.getText().
                        toString().replaceAll(hundredSymbol, ""));
        Log.d("CALCULATOR","m+ memory: " + memory);
    }

    public void mrButton (View v) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(memory));
        int intValue = bigDecimal.intValue();
        Log.d("CALCULATOR","Double Number: " + bigDecimal.toPlainString());
        Log.d("CALCULATOR","Integer Part: " + intValue);
        Log.d("CALCULATOR","Decimal Part: " + bigDecimal.subtract(
                new BigDecimal(intValue)).toPlainString());
        Log.d("CALCULATOR","SCALE: " + bigDecimal.scale());
    }


    public void mSubButton (View v) {
        if (Double.isNaN(memory)) {
            memory = Double.valueOf(mainScreenTV.getText().
                    toString().replaceAll(hundredSymbol, ""));
        } else {
            memory -= Double.valueOf(mainScreenTV.getText().
                            toString().replaceAll(hundredSymbol, ""));
        }
    }

    public void sumButton(View v) {
        if (operation != ERROR) {
            processOperationButton(mainScreenTV.getText().toString(), ADD);
        } else {
            clearButton(v);
        }
    }

    public void subButton(View v) {
        if (operation != ERROR) {
            processOperationButton(mainScreenTV.getText().toString(), SUB);
        } else {
            clearButton(v);
        }
    }

    public void mulButton(View v) {
        if (operation != ERROR) {
        processOperationButton(mainScreenTV.getText().toString(), MUL);
        } else {
            clearButton(v);
        }
    }

    public void divButton(View v) {
        if (operation != ERROR) {
            processOperationButton(mainScreenTV.getText().toString(), DIV);
        } else {
            clearButton(v);
        }
    }

    private void processOperationButton(String number, int op) {
        if (numberInput || operation == EQUAL) { // double or more add,sub,div,mul button pressed ignore
            if (operation == EQUAL) {
                mainScreenTV.setTextSize(textSize);
                printFormula(number);
                saveNumberA(number);
            } else {
                if (Double.isNaN(numberA)) {
                    printFormula(number);
                    saveNumberA(number);
                } else {
                    printFormula(number);
                    makeCalculation(number);
                }
            }
            if (operation != ERROR) {
                operation = op;
                numberInput = false;
            }
        }
    }

    /*
     * Add a digit to screen in order to create a new number.
     * If numberInput is false save the created number and start a new one.
     */
    public void addDigitToScreen(View v) {
        Button button = (Button) v;
        String newNumber;
        if (numberInput) {
            newNumber = mainScreenTV.getText().toString() + button.getText().toString();
            addNumberToScreen(newNumber);
        } else {
            mainScreenTV.setText(button.getText().toString());
            numberInput = true;
            if (operation == ERROR) {
                operation = NONE;
            }
        }
    }

    public void addDecimalToScreen(View v) {
        if (!decimal) {
            decimal = true;
            //Button button = (Button) v;
            String numberToPrint = mainScreenTV.getText().toString();
            mainScreenTV.setText(numberToPrint + decimalSymbol);
            numberInput = true;
        }
    }

    private void addNumberToScreen(String number) {
        DecimalFormat formatter = new DecimalFormat();
        double mainScreenNumber;
        if (operation == EQUAL) {
            mainScreenTV.setTextSize(textSize);
        }
        if (!decimal) {
            if (number.length() >= 3) {
                number = number.replaceAll(hundredSymbol, "");
                mainScreenNumber = Double.valueOf(number);
                formatter.applyPattern(pattern);
                number = String.valueOf(formatter.format(mainScreenNumber));
            }
        }
        mainScreenTV.setText(number);
    }

    /*private void addNumberToScreen(Double number) {
        DecimalFormat formatter = new DecimalFormat();
        double mainScreenNumber;
        formatter.applyPattern("#,###");
        number = String.valueOf(formatter.format(mainScreenNumber));
        mainScreenTV.setText(number);


        if (operation == EQUAL) {
            mainScreenTV.setTextSize(textSize);
        }
        if (!decimal) {
            if (number.length() >= 3) {
                number = number.replaceAll(hundredSymbol, "");
                mainScreenNumber = Double.valueOf(number);

            }
        }
        mainScreenTV.setText(number);
    }*/

    private void saveNumberA(String number) {
        if (number.isEmpty()) {
            number = "0.0";
        }
        /*if (decimal) {
            decimalPrecision = getDecimalPrecision(number);
        }*/
        numberA = Double.valueOf(number.replaceAll(hundredSymbol, ""));
        mainScreenTV.setText("");
        decimal = false;
    }

    private int getDecimalPrecision (String number) {
        String[] s = number.split("\\" + decimalSymbol);
        //Log.d("CALCULATOR", "SAVE NUMBER A decimals: " + decimalPrecision);
        return s[s.length - 1].length();
    }

    private void printFormula(String number) {
        if (formulaScreenTV.getText().toString().isEmpty()) {
            formulaScreenTV.append(number);
        } else {
            formulaScreenTV.append(getOperationSymbol() + number);
        }
    }

    private void makeCalculation(String numB) {
        DecimalFormat formatter = new DecimalFormat();
        Double result;
        String patt; //String pattern for the result

        int numberBDecimalPrecision = 0;
        if (numB.isEmpty()) {
            numB = "0.0";
        }
        numberB = Double.valueOf(numB.replaceAll(hundredSymbol, ""));
        if (Double.isNaN(this.numberB)) {
            this.numberB = 0.0;
        }
        if (Double.isNaN(this.numberA)) {
            this.numberA = 0.0;
        }

        switch (operation) {
            case ADD:
                result = numberA + numberB;
                Log.d("CALCULATOR","ADD : scale A: " + getPrecision(Double.toString(numberA)));
                Log.d("CALCULATOR","ADD : scale B: " + getPrecision(numberB));
                Log.d("CALCULATOR","ADD result: " + Double.toString(result));
                break;
            case SUB:
                result = numberA - numberB;
                break;
            case DIV:
                result = numberA / numberB;
                break;
            case MUL:
                result = numberA * numberB;
                break;
            default:
                result = 0.0;
                break;
        }
        if (Double.isInfinite(result)) {
            mainScreenTV.setText("ERROR");
            numberInput = false;
            numberA = Double.NaN;
            numberB = Double.NaN;
            operation = ERROR;
        } else {
            //int decimalPrecision;
            BigDecimal bd;

            if (operation == DIV) {
                bd = new BigDecimal(Double.toString(result)).
                        setScale(13,BigDecimal.ROUND_DOWN);
            } else {
                bd = new BigDecimal(Double.toString(result)); //.setScale(13,BigDecimal.ROUND_DOWN);
            }
            Log.d("CALCULATOR","Double Number: " + bd.toPlainString());
            Log.d("CALCULATOR","SCALE: " + bd.scale());
            mainScreenTV.setText(bd.toPlainString());
            numberInput = false;
            numberA = result;

            /*


            if (operation == DIV) {
                patt = pattern + decimalSymbol + "#############";
            } else {
                if (decimal) {
                    numberBDecimalPrecision = getDecimalPrecision(numB);
                    Log.d("CALCULATOR","NUMB: " + numB + " precision: " + numberBDecimalPrecision);
                }
                if (decimalPrecision > numberBDecimalPrecision) {
                    patt = getDecimalPattern(decimalPrecision);
                } else {
                    patt = getDecimalPattern(numberBDecimalPrecision);
                }
            }
            Log.d("CALCULATOR","PATTERN FOR RESULT: " + patt);
            formatter.applyPattern(patt);
            String resultStr = String.valueOf(formatter.format(result));
            mainScreenTV.setText(resultStr);
            numberInput = false;
            numberA = result; */
            //decimalPrecision = getDecimalPrecision(resultStr);
        }
    }

    private int getPrecision (Double number) {
        BigDecimal bd = new BigDecimal(number); //.setScale(13,BigDecimal.ROUND_DOWN);
        return bd.scale();
    }

    private int getPrecision (String number) {
        BigDecimal bd = new BigDecimal(number); //.setScale(13,BigDecimal.ROUND_DOWN);
        return bd.scale();
    }

    private String getDecimalPattern (int precision) {
        String patt;
        if (precision > 0) {
            patt = pattern + decimalSymbol;
            for (int i = 0; i < precision; i++) {
                patt = patt.concat("#");
            }
        } else {
            patt = pattern;
        }
        return patt;
    }

    public void calculateButton(View v) {
        if (numberInput) {

            if (!Double.isNaN(numberA)) {
              formulaScreenTV.setText("");
              makeCalculation(mainScreenTV.getText().toString());
            }
        }
        if (mainScreenTV.getText().toString().length() > 12) {
            mainScreenTV.setTextSize(42);

        } else {
            mainScreenTV.setTextSize(52);
        }
        if (operation != ERROR) {
            operation = EQUAL;
            numberInput = false;
        }
    }
}
   /* private void addNumberToScreen(String number) {
        DecimalFormat formatter = new DecimalFormat();
        String numberToPrint;
        double mainScreenNumber;
        if (operation == EQUAL) {
            mainScreenTV.setTextSize(textSize);
        }
        if (!decimal) {
            numberToPrint = mainScreenTV.getText().toString() + number;
            if (numberToPrint.length() >= 3) {
                numberToPrint = numberToPrint.replaceAll(",", "");
                mainScreenNumber = Double.valueOf(numberToPrint);
                formatter.applyPattern("#,###");
                numberToPrint = String.valueOf(formatter.format(mainScreenNumber));
            } else {
                numberToPrint = mainScreenTV.getText().toString() + number;
            }
            mainScreenTV.setText(numberToPrint);
        } else {
            numberToPrint = mainScreenTV.getText().toString() + number;
            mainScreenTV.setText(numberToPrint);
        }
    }
    */


/*
public void delButton(View v) {
        Double mainScreenNumber;
        DecimalFormat formatter = new DecimalFormat();
        String newNum;
        int length = mainScreenTV.getText().toString().length();
        if (length > 0) {
        newNum = mainScreenTV.getText().toString().substring(0, length -1);
        Log.d ("CALCULATOR","New Num: " + newNum);
        if (!decimal) {
        if (newNum.length() >= 3) {
        newNum = newNum.replaceAll(",", "");
        mainScreenNumber = Double.valueOf(newNum);
        formatter.applyPattern("#,###");
        newNum = String.valueOf(formatter.format(mainScreenNumber));
        } else {
        newNum = newNum.replaceAll(",", "");
        }
        } else {
        if (!newNum.contains(".")) {
        decimal = false;
        }//else {
        }
        mainScreenTV.setText(newNum);
        }
        } */

