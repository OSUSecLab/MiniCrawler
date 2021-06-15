package com.example.vsa.xposedutility;

import android.util.Log;

public class MLog {
    public static void d(String tag, String msg) {

        if (msg.length() > 2000) {
            Log.v(tag, "sb.length = " + msg.length());
            int chunkCount = msg.length() / 2000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 2000 * (i + 1);
                if (max >= msg.length()) {
                    Log.v(tag,  msg.substring(2000 * i));
                } else {
                    Log.v(tag, msg.substring(2000 * i, max));
                }
            }
        } else {
            Log.v(tag, msg.toString());
        }
    }
}
