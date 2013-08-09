package com.abreqadhabra.nflight.application.server.examples;

public class NanomilTest {  // illustrates clock "drift"

    public static void main(String args[]) throws Exception {
        long startN = System.nanoTime();
        long startM = System.currentTimeMillis();
        while (true) {
            Thread.sleep(1000);
            long endM = System.currentTimeMillis();
            long endN = System.nanoTime();
            double diffN = endN - startN;
       //     diffN /= 1000 * 1000;
            double secondsN = diffN / 1.0E09;
            double diffM =  endM - startM;
            double secondsM = diffM / 1.0E03;

            System.out.println(secondsN + "\t" + secondsM);
        }
    }
}