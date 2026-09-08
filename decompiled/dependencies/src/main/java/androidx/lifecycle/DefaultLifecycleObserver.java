package androidx.lifecycle;

import com.android.tools.r8.annotations.SynthesizedClassV2;

/* loaded from: classes.dex */
public interface DefaultLifecycleObserver extends FullLifecycleObserver {
    @Override // androidx.lifecycle.FullLifecycleObserver
    void onCreate(LifecycleOwner lifecycleOwner);

    @Override // androidx.lifecycle.FullLifecycleObserver
    void onDestroy(LifecycleOwner lifecycleOwner);

    @Override // androidx.lifecycle.FullLifecycleObserver
    void onPause(LifecycleOwner lifecycleOwner);

    @Override // androidx.lifecycle.FullLifecycleObserver
    void onResume(LifecycleOwner lifecycleOwner);

    @Override // androidx.lifecycle.FullLifecycleObserver
    void onStart(LifecycleOwner lifecycleOwner);

    @Override // androidx.lifecycle.FullLifecycleObserver
    void onStop(LifecycleOwner lifecycleOwner);

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: androidx.lifecycle.DefaultLifecycleObserver$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
        public static void $default$onCreate(DefaultLifecycleObserver _this, LifecycleOwner owner) {
        }

        public static void $default$onStart(DefaultLifecycleObserver _this, LifecycleOwner owner) {
        }

        public static void $default$onResume(DefaultLifecycleObserver _this, LifecycleOwner owner) {
        }

        public static void $default$onPause(DefaultLifecycleObserver _this, LifecycleOwner owner) {
        }

        public static void $default$onStop(DefaultLifecycleObserver _this, LifecycleOwner owner) {
        }

        public static void $default$onDestroy(DefaultLifecycleObserver _this, LifecycleOwner owner) {
        }
    }
}
