package com.main;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by flyboss on 2018/3/30.
 */
public class Test {

    public static void main(String[] args) {
        String shellString="ipconfig -all";
        callShell(shellString);
    }
    public static void callShell(String shellString) {
        Runtime runtime = Runtime.getRuntime();

        try {
            Process process = runtime.exec(shellString);
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
