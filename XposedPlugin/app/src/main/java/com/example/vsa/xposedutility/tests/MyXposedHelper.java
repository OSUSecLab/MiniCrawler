package com.example.vsa.xposedutility.tests;


import android.util.Log;

import com.example.vsa.xposedutility.Utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class MyXposedHelper {

    public static HashSet<String>  IGNORES = new HashSet<String>();

    static{
        IGNORES.add("notifyAll");
        IGNORES.add("equals");
        IGNORES.add("notify");
        IGNORES.add("hashCode");
        IGNORES.add("wait");
        IGNORES.add("getClass");
        IGNORES.add("toString");
    }

    public static HashSet<Method> hookAllbut(ClassLoader classLoader, String classp, HashSet<String> ignores, XC_MethodHook callback){
        HashSet<Method> hooked = new HashSet();
        try {
            Class clazz = classLoader.loadClass(classp);
            for(Method md : getAllMethods(clazz)) {
                if (ignores == null || (ignores != null && !ignores.contains(md.getName()))) {
                    XposedBridge.hookMethod(md, callback);
                    hooked.add(md);
                }
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return hooked;
    }

    public static HashSet<Method> hookAll(ClassLoader classLoader, String classp, HashSet<String> shoulds, XC_MethodHook callback){
        HashSet<Method> hooked = new HashSet();
        try {
            Class clazz = classLoader.loadClass(classp);
            for(Method md : getAllMethods(clazz)) {
                if (shoulds == null || (shoulds != null && shoulds.contains(md.getName()))) {
                    XposedBridge.hookMethod(md, callback);
                    hooked.add(md);
                }
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return hooked;
    }

    public static HashSet<Member> hookAllDeclaredMethods(ClassLoader classLoader, String classp, HashSet<String> shoulds, XC_MethodHook callback){
        HashSet<Member> hooked = new HashSet();
        try {
            Class clazz = classLoader.loadClass(classp);
            for(Member md : getDeclaredMethods(clazz)) {
                if (shoulds == null || (shoulds != null && shoulds.contains(md.getName()))) {
                    XposedBridge.hookMethod(md, callback);
                    hooked.add(md);
                }
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return hooked;
    }

    public static HashSet<Member> hookAllConstructor(ClassLoader classLoader, String classp, HashSet<String> shoulds, XC_MethodHook callback){
        HashSet<Member> hooked = new HashSet();
        try {
            Class clazz = classLoader.loadClass(classp);
            for (Member md : clazz.getDeclaredConstructors()) {
                if (shoulds == null || (shoulds != null && shoulds.contains(md.getName()))) {
                    XposedBridge.hookMethod(md, callback);
                    hooked.add(md);
                }
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return hooked;
    }

    public static HashSet<Method> hookbyname(ClassLoader classLoader, String classp, String mname, int argscount, XC_MethodHook callback){
        HashSet<Method> hooked = new HashSet();
        try {
            Class clazz = classLoader.loadClass(classp);
            for(Method md : getAllMethods(clazz)) {

                if (md.getName().equals(mname) &&(argscount == -1 || md.getParameterTypes().length == argscount)) {
                    XposedBridge.hookMethod(md, callback);
                    hooked.add(md);
                }
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return hooked;
    }


    public static HashSet<Method> getAllMethods(Class clazz){
        HashSet<Method> ms = new HashSet();
        for (Method md : clazz.getDeclaredMethods()) {
            ms.add(md);
        }
        for (Method md : clazz.getMethods()) {
            ms.add(md);
        }
        return ms;
    }

    public static HashSet<Member> getDeclaredMethods(Class clazz){
        HashSet<Member> ms = new HashSet();
        for (Method md : clazz.getDeclaredMethods()) {
            ms.add(md);
        }
        for (Constructor md : clazz.getDeclaredConstructors()) {
            ms.add(md);
        }
        return ms;
    }
}
