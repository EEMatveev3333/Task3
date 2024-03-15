package org.example;

//import java.util.*;
//import jdk.internal.reflect.ReflectionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.lang.*;
//import java.util.Queue;
//import java.lang.reflect.InvocationTargetException;
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {


    public static void main(String[] args)  throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException, InstantiationException  {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.

/*        // This method does not copy the returned Method object!
        private static Method searchMethods(Method[] methods,
                String name,
                Class<?>[] parameterTypes)
        {
            ReflectionFactory fact = getReflectionFactory();
            Method res = null;
            for (Method m : methods) {
                if (m.getName().equals(name)
                        && arrayContentsEq(parameterTypes,
                        fact.getExecutableSharedParameterTypes(m))
                        && (res == null
                        || (res.getReturnType() != m.getReturnType()
                        && res.getReturnType().isAssignableFrom(m.getReturnType()))))
                    res = m;
            }
            return res;
        }*/
/*        System.out.println("Лог выполнения");

        Fraction fr = new Fraction(2,3);*/
/*        System.out.println("Лог выполнения");
        System.out.println(Arrays.toString(Fraction.class.getDeclaredFields()));
        //System.out.println(Fraction.class.getField("num"));
        //System.out.println(Fraction.class.getField("denum"));
        for (Field f : Fraction.class.getDeclaredFields()) System.out.println(f.getName());
        for (Field f : Fraction.class.getDeclaredFields()) System.out.println(f.getName());*/
/*        System.out.println(Arrays.toString(Fraction.class.getMethods()));
        System.out.println(Arrays.toString(Fraction.class.getDeclaredMethods()));
        for (Method m : Fraction.class.getDeclaredMethods()){
            System.out.println(m.getName());
            System.out.println(m.toString());

            if (m.isAnnotationPresent(Cache.class))
                System.out.println("Аннотация Cache найдена");
            else
                System.out.println("Аннотация Cache не найдена");

            if (m.isAnnotationPresent(Mutator.class))
                System.out.println("Аннотация Mutator найдена");
            else
                System.out.println("Аннотация Mutator не найдена");
        }*/
/*        Method meth1 = Fraction.class.getDeclaredMethod("doubleValue");
        Method meth2 = Fraction.class.getDeclaredMethod("setDenum");
        Method meth3 = Fraction.class.getDeclaredMethod("setNum");*/




        //--- Тестовый пример
/*        FractionCache frC= new FractionCache(fr);

        Fractionable num1 = frC;
        System.out.println("===!");
        System.out.println("            " + String.valueOf(num1.doubleValue()));// sout сработал
        System.out.println("            " + String.valueOf(num1.doubleValue()));// sout молчит
        System.out.println("            " + String.valueOf(num1.doubleValue()));// sout молчит
        num1.setNum(5);
        System.out.println("            " + String.valueOf(num1.doubleValue()));// sout сработал
        System.out.println("            " + String.valueOf(num1.doubleValue()));// sout молчит*/
        //--- Тестовый пример
        //Class clazz = Fraction.class;
        //

        try {
            System.out.println("Лог выполнения");

            Fraction fr = new Fraction(2, 3);

            System.out.println("===!" + fr.toString());
            Fractionable num = Utils.cache(fr);
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout сработал
//0 тест - корректный расчет без кэша
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout молчит
//0 тест - корректный расчет из кэша
            Thread.currentThread().sleep(2000);
//1 тест - перевызов invoke после протухания кэша
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
            System.out.println("мутаторы меняем setNum(50) setDenum(250)");// sout молчит
            num.setNum(50);
            num.setDenum(250);
            System.out.println("===!" + fr.toString());
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout сработал
            System.out.println("            РЕЗУЛЬТАТ num.doubleValue():" + String.valueOf(num.doubleValue()));// sout молчит
            Thread.currentThread().sleep(2000);
            System.out.println("sleep(2000), мутаторы не меняем, сразу повторный вызов, кэш протух");// sout молчит
            System.out.println("            " + String.valueOf(num.doubleValue()));// sout молчит
            //num = null;
            Utils.referPersonInvocationHandler.KillerThread.interrupt();
            System.gc();
            System.out.println("===!");
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
//            Thread.interrupt();
        }

    }
}