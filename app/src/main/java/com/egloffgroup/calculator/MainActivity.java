package com.egloffgroup.calculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity {

    private TextView mainScreenTV;
    private TextView formulaScreenTV;
    private double numberA;
    private double numberB;
    private int NONE = 0;
    private int ADD = 1;
    private int SUB = 2;
    private int DIV = 3;
    private int MUL = 4;
    private int operation = NONE;

    private boolean decimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainScreenTV = (findViewById(R.id.textView_mainScreen));
        formulaScreenTV = (findViewById(R.id.textView_formulaScreen));
        decimal = false;
        operation = NONE;
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

    public void clearButton (View v) {
        decimal = false;
        mainScreenTV.setText("");
        formulaScreenTV.setText("");
        operation = NONE;
        numberA = 0;
        numberB = 0;
    }

    public void addNumberToScreen (View v) {
        Button button = (Button) v;
        printNumber(button.getText().toString());
    }

    public void addDecimalToScreen(View v) {
        if (!decimal) {
            decimal = true;
            Button button = (Button) v;
            String numberToPrint = mainScreenTV.getText().toString();
            mainScreenTV.setText(numberToPrint + button.getText());
        }
    }

    public void sumOperation (View v) {
        String numberToSave;
        numberToSave = mainScreenTV.getText().toString();
        formulaScreenTV.setText(numberToSave + " + ");
        numberA = Double.valueOf(numberToSave.replaceAll(",",""));
        Log.d("CALCULATOR","NumeroA: " + numberA);
    }

    private void printNumber (String number) {
        DecimalFormat formatter = new DecimalFormat();
        String numberToPrint;
        double mainScreenNumber;
        if (!decimal) {
            /*if (mainScreenTV.getText().length() < 1) {
                numberToPrint = number;
            } else {
                numberToPrint = mainScreenTV.getText().toString() + number;
            }*/
            //Log.d("CALCULATOR", "CASE 0, NUMBER TO PRINT: " + numberToPrint.length());
            numberToPrint = mainScreenTV.getText().toString() + number;
            if (numberToPrint.length() >= 3) {
                numberToPrint = numberToPrint.replaceAll(",","");
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

}
