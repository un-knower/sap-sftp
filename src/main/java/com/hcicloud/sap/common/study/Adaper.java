package com.hcicloud.sap.common.study;

/**
 * 适配器模式，类适配器模式
 * 继承源对象，来完成和目标对象的适配
 */
public class Adaper extends Src implements Dst {

    @Override
    public void getSimple2() {
        System.out.println("适配器角色");
    }

    public static void main(String[] args){
        Adaper adaper = new Adaper();
        adaper.getSimple1();
        adaper.getSimple2();
    }
}
