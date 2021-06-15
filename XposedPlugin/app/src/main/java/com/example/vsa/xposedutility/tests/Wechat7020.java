package com.example.vsa.xposedutility.tests;

import android.util.Log;

import com.example.vsa.xposedutility.ICases;
import com.example.vsa.xposedutility.Utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.security.SecureClassLoader;
import java.util.HashSet;

import dalvik.system.BaseDexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Wechat7020 implements ICases {
    static String TAG = Wechat7020.class.getSimpleName();

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
    static boolean did = false;
    public void hookAll(XC_LoadPackage.LoadPackageParam pparam, ClassLoader classLoader) {


        XC_MethodHook cb  =new XC_MethodHook() {
            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                Log.d(TAG, "---->>>:" + param.method.getName());
                Utilities.printParameter(TAG, param);

                if(param.method.getName().equals("invokeHandler") && !did){
                    did = true;

                    final Object thiobj =  param.thisObject;
                    final Method invokeHandler = thiobj.getClass().getMethod("invokeHandler",String.class, String.class, int.class);


                }

                if(param.method.getDeclaringClass().getName().equals("com.tencent.mm.plugin.appbrand.appcache.ba")) {
                    if (param.method instanceof Constructor && ((Constructor) param.method).getParameterTypes().length == 4) {

                        String appid = param.args[0] + "";
                        String version = param.args[2] + "";
                        String url = param.args[3] + "";

                        Utilities.writeToFile("/sdcard/apps.txt", appid + " " + version + " " + url + "\n");
                    }
                }

                if(param.method.getDeclaringClass().getName().equals("com.tencent.mm.plugin.appbrand.jsapi.l")) {
                    if (!WechatMiniAppsDownloader.wvs.contains(param.thisObject))
                        WechatMiniAppsDownloader.wvs.add(param.thisObject);
                }
            }
        };



        HashSet<String> clss = new HashSet<>();

        clss.add("com.tencent.mm.plugin.appbrand.jsapi.l");

        clss.add("com.tencent.mm.plugin.appbrand.appcache.ba");



        for(String cls : clss){
            for(Member md:MyXposedHelper.hookAllDeclaredMethods(classLoader, cls, null, cb)) {
                Log.d(TAG, "hooked:" + md);
            }
        }


    }

}
