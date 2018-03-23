package com.hcicloud.sap.common.encrypt;


public class Encrypt
{
    public static void main(String[] args)
    {
        args = "123456".split(",");
        System.out.println("输入的值为：" + args[0]);
        if ((args == null) || (args.length != 1))
        {
            System.out.println("Parameter error . You can use a string as parameter . ");
            return;
        }
        String result = Security.encryptByCbc(args[0]);
        System.out.println("加密后的字符串为：" + result);
        
        String result2 = Security.decryptByCbc(result);
        System.out.println("解密后的字符串为：" + result2);
    }
}