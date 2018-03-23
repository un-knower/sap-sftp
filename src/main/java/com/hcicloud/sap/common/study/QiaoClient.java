package com.hcicloud.sap.common.study;

/**
 * 桥接模式
 * 关键定一个桥接口。桥接口的实现类是要去的终点。抽象类是引入桥接口。抽象类的实现类是起点 + 桥。
 *
 */
public class QiaoClient {

    public static void main(String[] args){
        //抽象类的实现类     是起点 + 桥。
        AreaA a = new AreaA1();
        //桥接口的实现类     是要去的终点。
        a.bridge = new Area1();

        //开始从起点出发
        a.fromAreaA();
        //经过桥到达的终点
        a.bridge.targetAreaB();
    }
}
