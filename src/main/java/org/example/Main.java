package org.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.lang.*;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException, InstantiationException {
        try {
            System.out.println("Лог выполнения");
            Fraction fr = new Fraction(2, 3);
            System.out.println("===!" + fr.toString());
            Fractionable num = Utils.cache(fr);
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout сработал
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout молчит
            Thread.currentThread().sleep(2000);
            System.out.println("            sleep(2000), мутаторы не меняем, сразу повторный вызов, кэш протух");// sout молчит
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout молчит
            System.out.println("мутаторы меняем setNum(5) setDenum(15)");// sout молчит
            num.setNum(5);
            num.setDenum(15);
            System.out.println("===!" + fr.toString());
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout сработал
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout молчит
            Thread.currentThread().sleep(2000);
            System.out.println("sleep(2000), мутаторы не меняем, сразу повторный вызов, кэш протух");// sout молчит
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout молчит
            System.out.println("==========================!");// + fr.toString());
            System.out.println("мутаторы меняем setNum(50) setDenum(250)");// sout молчит
            num.setNum(50);
            num.setDenum(250);
            System.out.println("===!" + fr.toString());
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout сработал
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout молчит
            Thread.currentThread().sleep(2000);
            System.out.println("sleep(2000), мутаторы не меняем, сразу повторный вызов, кэш протух");// sout молчит
            System.out.println("            " + String.valueOf(num.doubleValue()));// sout молчит
            Utils.referPersonInvocationHandler.KillerThread.interrupt();
            System.gc();
            System.out.println("===!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}