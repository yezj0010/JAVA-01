package com.supercoin.common.test.study.javagaojie.week01.bytecode;

/**
 * created by DengJin on 2021/1/12 15:00
 */
public class Hello {

    public static void main(String[] args) {
        new Hello().test();
    }

    public void test(){
        //bool
        boolean a = true;
        a = false;
        //char
        char b = 'a';
        b = '1';
        //number
        byte h = (byte) 254;
        short c = 5;
        int d = 99;
        long e = 578;
        float f = 45.7f;
        double g = 66.34;

        //add
        int sum = 5+7;
        double sum2 = g + f;
        //sub
        int sub = d - 56;
        double sub2 = g - f;
        //multi
        int mul = d * c;
        double mul2 = f * d;
        //divide
        int div = d/c;
        double div2 = d/f;

        //if
        if(sum2 > mul){
            System.out.println(666);
        }else{
            System.out.println(555);
        }

        //for
        for(int i=0;i<10;i++){
            System.out.println("current is " + i);
        }

        System.out.println(3+1.3+0.5);
    }
}
