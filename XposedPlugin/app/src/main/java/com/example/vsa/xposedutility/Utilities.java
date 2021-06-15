package com.example.vsa.xposedutility;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import de.robv.android.xposed.XC_MethodHook;

public class Utilities {

    public static void printParameter(String TAG, XC_MethodHook.MethodHookParam param) {
        int i = 0;
        if (param.args != null && param.args.length > 0)
            for (Object arg : param.args) {
                MLog.d(TAG, "arg:" + i++ + ":" + arg + ":-:" + (arg==null?"":arg.getClass()));

                if(arg instanceof Object[]){
                    for (Object aa :(Object[]) arg){
                        MLog.d(TAG, "  subarg:" + aa);
                    }
                }

            }

        if(param.getResult()!=null)
            Log.d(TAG, "ret:" + i++ + ":" + param.getResult() + ":-:" + param.getResult().getClass());

    }

    public static void printStackTrace(String TAG) {
        Log.d(TAG, "printStackTrace:");
        for (StackTraceElement tr : new Exception().getStackTrace()) {
            Log.d(TAG, "    " + tr);
        }
    }

    public static void writeToFile(String fpath, String data) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fpath, true);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getpidname() {
        final File cmdline = new File("/proc/" + android.os.Process.myPid() + "/cmdline");
        try (BufferedReader reader = new BufferedReader(new FileReader(cmdline))) {
            return cleanTextContent(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String cleanTextContent(String text)
    {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

}
