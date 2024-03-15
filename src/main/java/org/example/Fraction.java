package org.example;

//import java.util.Objects;
import jdk.jfr.Description;

import java.lang.annotation.*;

import jdk.jfr.Description;
public class Fraction implements Fractionable{
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator()
    @Description("the power of an engine")
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator()
    @Description("the power of an engine")
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(1000)
    @Description("the power of an engine")
    public double doubleValue() {
        System.out.println("invoke double value");
        return (double) num/denum;
    }

    @Override
    public String toString() {
        return "Fraction{" +
                "num=" + num +
                ", denum=" + denum +
                '}';
    }
}

