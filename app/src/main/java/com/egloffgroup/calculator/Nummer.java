package com.egloffgroup.calculator;
import java.text.DecimalFormat;

public class Nummer {

    private String str;
    private Double dbl;
    private int precision;
    private boolean hasDecimal;

    public Nummer () {

    }

    public Nummer(String number) {
        this.str = number;
        this.dbl = Double.valueOf(number);
    }

    public Nummer (Double number, String pattern) {
        DecimalFormat formatter = new DecimalFormat();
        this.dbl = number;
        formatter.applyPattern(pattern);
        this.str = String.valueOf(formatter.format(number));
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Double getDbl() {
        return dbl;
    }

    public void setDbl(Double dbl) {
        this.dbl = dbl;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public boolean isHasDecimal() {
        return hasDecimal;
    }

    public void setHasDecimal(boolean hasDecimal) {
        this.hasDecimal = hasDecimal;
    }
}
