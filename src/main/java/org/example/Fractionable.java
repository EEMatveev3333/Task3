package org.example;

public interface Fractionable {
    //@Cache(1000)
    double doubleValue();

    //@Mutator()
    void setNum(int num);

    //@Mutator()
    void setDenum(int denum);
}
