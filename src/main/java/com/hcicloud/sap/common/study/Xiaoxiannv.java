package com.hcicloud.sap.common.study;

public class Xiaoxiannv extends ConcreteA implements B{

    @Override
    public void getNameB() {
        getNameA();
        System.out.println("进入B的方法");

    }

    public static void main(String[] args){
    }
}
