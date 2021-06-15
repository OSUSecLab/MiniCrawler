package com.example.vsa.xposedutility;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface ICases {
    public void hook(XC_LoadPackage.LoadPackageParam pparam);
}
