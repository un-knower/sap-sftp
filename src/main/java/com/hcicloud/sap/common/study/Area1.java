package com.hcicloud.sap.common.study;

public class Area1 implements Bridge {
    @Override
    public void targetAreaB() {
        System.out.println("我要去B1");
    }
}
