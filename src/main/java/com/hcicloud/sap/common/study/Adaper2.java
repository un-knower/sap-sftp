package com.hcicloud.sap.common.study;

/**
 * 适配器模式，对象适配器模式
 * 引入源对象，来完成和目标对象的适配
 *
 */
public class Adaper2 implements Dst{

    private Src src;

    public Adaper2(Src src){
        this.src = src;
    }

    public void getSimple1(){
        this.src.getSimple1();
    }

    public void getSimple2(){
        System.out.println("适配器角色");
    }

    public static void main(String[] args){
        Adaper adaper = new Adaper();
        adaper.getSimple1();
        adaper.getSimple2();
    }
}
