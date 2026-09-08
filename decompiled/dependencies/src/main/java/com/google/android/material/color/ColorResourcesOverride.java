package com.google.android.material.color;

import android.content.Context;
import android.os.Build;
import androidx.core.os.BuildCompat;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import java.util.Map;

/* loaded from: classes.dex */
public interface ColorResourcesOverride {
    boolean applyIfPossible(Context context, Map<Integer, Integer> map);

    Context wrapContextIfPossible(Context context, Map<Integer, Integer> map);

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: com.google.android.material.color.ColorResourcesOverride$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
        public static ColorResourcesOverride getInstance() {
            if (30 <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT <= 33) {
                return ResourcesLoaderColorResourcesOverride.getInstance();
            }
            if (BuildCompat.isAtLeastU()) {
                return ResourcesLoaderColorResourcesOverride.getInstance();
            }
            return null;
        }
    }
}
