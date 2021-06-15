package com.example.vsa.xposedutility.tests;

import android.util.Log;

import com.example.vsa.xposedutility.ICases;
import com.example.vsa.xposedutility.Utilities;

import java.lang.reflect.Member;
import java.security.SecureClassLoader;
import java.util.HashSet;

import dalvik.system.BaseDexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WechatSearchToken implements ICases {
    static String TAG = WechatSearchToken.class.getSimpleName();

    @Override
    public void hook(final XC_LoadPackage.LoadPackageParam pparam) {
        if (!pparam.packageName.equals("com.tencent.mm")) return;

        hookAll(pparam, pparam.classLoader);
        final String packageName = pparam.packageName;
        Class[] loaderClassList = {
                BaseDexClassLoader.class,
                SecureClassLoader.class,
        };

        for (final Class loaderClass : loaderClassList) {
            XposedBridge.hookAllConstructors(loaderClass, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader classLoader = (ClassLoader) param.thisObject;

                    hookAll(pparam, classLoader);

                }
            });
        }
    }

    public void hookAll(XC_LoadPackage.LoadPackageParam pparam, ClassLoader classLoader) {

        XC_MethodHook cb = new XC_MethodHook() {

            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                Log.d(TAG, Utilities.getpidname() + "---->>>:" + param.method.getName());

                if (param.args.length > 1 && (param.args[1] + "").contains("https://mp.weixin.qq.com/wxa-cgi/innersearch/subsearch")) {

                    Log.d(TAG, "HTTP_GET_DATA:" + param.args[1].toString().split("from_h5")[0]);
                }
            }

        };

        HashSet<String> clss = new HashSet<>();
        clss.add("com.tencent.mm.plugin.appbrand.jsapi.l");

        for (String cls : clss) {
            for (Member md : MyXposedHelper.hookAllDeclaredMethods(classLoader, cls, null, cb)) {
                Log.d(TAG, Utilities.getpidname() + "hooked:" + md);
            }
        }
    }

}
