package org.example;

public class FractionCache implements Fractionable{
    private Fraction fraction;

    boolean isChanged;

    double tmp;

    public FractionCache(Fraction fraction) {
        isChanged = true;
        this.fraction = fraction;
    }

    public double doubleValue(){
        if (isChanged) tmp = fraction.doubleValue();
               else
            System.out.println("tmp double value");
        isChanged = false;
        return tmp;
    };

    public void setNum(int num){
        fraction.setNum(num);
        isChanged = true;
    };

    public void setDenum(int denum){
        fraction.setDenum(denum);
        isChanged = true;
    };

    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }
}
