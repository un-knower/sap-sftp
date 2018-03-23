package com.hcicloud.sap.common.study;

/**
 * 缺省适配器模式
 * 定义统一的和尚接口，鲁智深是和尚，但是不吃斋，念经，打坐，撞钟。
 * 因此需要一个适配器抽象类，简单的实现接口，鲁智深类继承这个抽象类，重写自己的习武方法，变成和尚。
 *
 */
public class LuZhiShen extends TianXing {

    public void 习武() {
        System.out.println("鲁智深仅仅习武，变成和尚");
    }

    public String getName() {
        return null;
    }

    public static void main(String[] args){
        LuZhiShen luZhiShen = new LuZhiShen();
        luZhiShen.习武();
    }

}
