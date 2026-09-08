package com.ngi_pro.core;

/* loaded from: classes3.dex */
public class NativeEngine {
    public native void startAutoSDKGen(String str);

    static {
        System.loadLibrary("UENGINE");
    }
}
