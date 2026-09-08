package androidx.window.layout;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.window.core.Version;
import androidx.window.layout.ExtensionInterfaceCompat;
import androidx.window.sidecar.SidecarDeviceState;
import androidx.window.sidecar.SidecarDisplayFeature;
import androidx.window.sidecar.SidecarInterface;
import androidx.window.sidecar.SidecarProvider;
import androidx.window.sidecar.SidecarWindowLayoutInfo;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SidecarCompat.kt */
@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0000\u0018\u0000 !2\u00020\u0001:\u0005!\"#$%B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u001b\b\u0007\u0012\n\b\u0001\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\fH\u0007J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\fH\u0016J\u0010\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\fH\u0016J\u0016\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\fJ\u0010\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\fH\u0002J\u0010\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\fH\u0002J\b\u0010\u001f\u001a\u00020 H\u0017R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Landroidx/window/layout/SidecarCompat;", "Landroidx/window/layout/ExtensionInterfaceCompat;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "sidecar", "Landroidx/window/sidecar/SidecarInterface;", "sidecarAdapter", "Landroidx/window/layout/SidecarAdapter;", "(Landroidx/window/sidecar/SidecarInterface;Landroidx/window/layout/SidecarAdapter;)V", "componentCallbackMap", "", "Landroid/app/Activity;", "Landroid/content/ComponentCallbacks;", "extensionCallback", "Landroidx/window/layout/ExtensionInterfaceCompat$ExtensionCallbackInterface;", "getSidecar", "()Landroidx/window/sidecar/SidecarInterface;", "windowListenerRegisteredContexts", "Landroid/os/IBinder;", "getWindowLayoutInfo", "Landroidx/window/layout/WindowLayoutInfo;", "activity", "onWindowLayoutChangeListenerAdded", "", "onWindowLayoutChangeListenerRemoved", "register", "windowToken", "registerConfigurationChangeListener", "setExtensionCallback", "unregisterComponentCallback", "validateExtensionInterface", "", "Companion", "DistinctElementCallback", "DistinctSidecarElementCallback", "FirstAttachAdapter", "TranslatingCallback", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class SidecarCompat implements ExtensionInterfaceCompat {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final String TAG = "SidecarCompat";
    private final Map<Activity, ComponentCallbacks> componentCallbackMap;
    private ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallback;
    private final SidecarInterface sidecar;
    private final SidecarAdapter sidecarAdapter;
    private final Map<IBinder, Activity> windowListenerRegisteredContexts;

    public SidecarCompat(SidecarInterface sidecar, SidecarAdapter sidecarAdapter) {
        Intrinsics.checkNotNullParameter(sidecarAdapter, "sidecarAdapter");
        this.sidecar = sidecar;
        this.sidecarAdapter = sidecarAdapter;
        this.windowListenerRegisteredContexts = new LinkedHashMap();
        this.componentCallbackMap = new LinkedHashMap();
    }

    public final SidecarInterface getSidecar() {
        return this.sidecar;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public SidecarCompat(Context context) {
        this(INSTANCE.getSidecarCompat$window_release(context), new SidecarAdapter(null, 1, null));
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @Override // androidx.window.layout.ExtensionInterfaceCompat
    public void setExtensionCallback(ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallback) {
        Intrinsics.checkNotNullParameter(extensionCallback, "extensionCallback");
        this.extensionCallback = new DistinctElementCallback(extensionCallback);
        SidecarInterface sidecarInterface = this.sidecar;
        if (sidecarInterface != null) {
            sidecarInterface.setSidecarCallback(new DistinctSidecarElementCallback(this.sidecarAdapter, new TranslatingCallback(this)));
        }
    }

    public final WindowLayoutInfo getWindowLayoutInfo(Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder windowToken = INSTANCE.getActivityWindowToken$window_release(activity);
        if (windowToken == null) {
            return new WindowLayoutInfo(CollectionsKt.emptyList());
        }
        SidecarInterface sidecarInterface = this.sidecar;
        SidecarWindowLayoutInfo windowLayoutInfo = sidecarInterface == null ? null : sidecarInterface.getWindowLayoutInfo(windowToken);
        SidecarAdapter sidecarAdapter = this.sidecarAdapter;
        SidecarInterface sidecarInterface2 = this.sidecar;
        SidecarDeviceState deviceState = sidecarInterface2 != null ? sidecarInterface2.getDeviceState() : null;
        if (deviceState == null) {
            deviceState = new SidecarDeviceState();
        }
        return sidecarAdapter.translate(windowLayoutInfo, deviceState);
    }

    @Override // androidx.window.layout.ExtensionInterfaceCompat
    public void onWindowLayoutChangeListenerAdded(Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder windowToken = INSTANCE.getActivityWindowToken$window_release(activity);
        if (windowToken != null) {
            register(windowToken, activity);
        } else {
            FirstAttachAdapter attachAdapter = new FirstAttachAdapter(this, activity);
            activity.getWindow().getDecorView().addOnAttachStateChangeListener(attachAdapter);
        }
    }

    public final void register(IBinder windowToken, Activity activity) {
        SidecarInterface sidecarInterface;
        Intrinsics.checkNotNullParameter(windowToken, "windowToken");
        Intrinsics.checkNotNullParameter(activity, "activity");
        this.windowListenerRegisteredContexts.put(windowToken, activity);
        SidecarInterface sidecarInterface2 = this.sidecar;
        if (sidecarInterface2 != null) {
            sidecarInterface2.onWindowLayoutChangeListenerAdded(windowToken);
        }
        if (this.windowListenerRegisteredContexts.size() == 1 && (sidecarInterface = this.sidecar) != null) {
            sidecarInterface.onDeviceStateListenersChanged(false);
        }
        ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface = this.extensionCallback;
        if (extensionCallbackInterface != null) {
            extensionCallbackInterface.onWindowLayoutChanged(activity, getWindowLayoutInfo(activity));
        }
        registerConfigurationChangeListener(activity);
    }

    private final void registerConfigurationChangeListener(final Activity activity) {
        if (this.componentCallbackMap.get(activity) == null) {
            ComponentCallbacks componentCallbacks = new ComponentCallbacks() { // from class: androidx.window.layout.SidecarCompat$registerConfigurationChangeListener$configChangeObserver$1
                @Override // android.content.ComponentCallbacks
                public void onConfigurationChanged(Configuration newConfig) {
                    Intrinsics.checkNotNullParameter(newConfig, "newConfig");
                    ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface = SidecarCompat.this.extensionCallback;
                    if (extensionCallbackInterface != null) {
                        Activity activity2 = activity;
                        extensionCallbackInterface.onWindowLayoutChanged(activity2, SidecarCompat.this.getWindowLayoutInfo(activity2));
                    }
                }

                @Override // android.content.ComponentCallbacks
                public void onLowMemory() {
                }
            };
            this.componentCallbackMap.put(activity, componentCallbacks);
            activity.registerComponentCallbacks(componentCallbacks);
        }
    }

    @Override // androidx.window.layout.ExtensionInterfaceCompat
    public void onWindowLayoutChangeListenerRemoved(Activity activity) {
        SidecarInterface sidecarInterface;
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder windowToken = INSTANCE.getActivityWindowToken$window_release(activity);
        if (windowToken == null) {
            return;
        }
        SidecarInterface sidecarInterface2 = this.sidecar;
        if (sidecarInterface2 != null) {
            sidecarInterface2.onWindowLayoutChangeListenerRemoved(windowToken);
        }
        unregisterComponentCallback(activity);
        boolean isLast = this.windowListenerRegisteredContexts.size() == 1;
        this.windowListenerRegisteredContexts.remove(windowToken);
        if (!isLast || (sidecarInterface = this.sidecar) == null) {
            return;
        }
        sidecarInterface.onDeviceStateListenersChanged(true);
    }

    private final void unregisterComponentCallback(Activity activity) {
        ComponentCallbacks configChangeObserver = this.componentCallbackMap.get(activity);
        activity.unregisterComponentCallbacks(configChangeObserver);
        this.componentCallbackMap.remove(activity);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0030 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006d A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0098 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c2 A[Catch: all -> 0x01df, TRY_LEAVE, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01a5 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00b5 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01b4 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x008b A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01c3 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0061 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01d1 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0023 A[Catch: all -> 0x01df, TryCatch #2 {all -> 0x01df, blocks: (B:3:0x0003, B:9:0x0027, B:11:0x0030, B:14:0x003a, B:17:0x0043, B:22:0x0065, B:24:0x006d, B:29:0x008f, B:31:0x0098, B:36:0x00b9, B:38:0x00c2, B:41:0x00c9, B:42:0x0104, B:45:0x0127, B:53:0x0134, B:55:0x0176, B:57:0x0181, B:58:0x0188, B:59:0x0189, B:60:0x0190, B:64:0x00cf, B:66:0x00fb, B:68:0x0191, B:69:0x019a, B:70:0x019b, B:71:0x01a4, B:72:0x01a5, B:73:0x01b3, B:74:0x00b5, B:75:0x009e, B:78:0x00a5, B:79:0x01b4, B:80:0x01c2, B:81:0x008b, B:82:0x0073, B:85:0x007a, B:86:0x01c3, B:87:0x01d0, B:88:0x0061, B:89:0x0049, B:92:0x0050, B:93:0x0040, B:94:0x0036, B:95:0x01d1, B:96:0x01de, B:97:0x0023, B:98:0x000b, B:101:0x0012), top: B:2:0x0003, inners: #0, #1 }] */
    @Override // androidx.window.layout.ExtensionInterfaceCompat
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean validateExtensionInterface() {
        Class<?> cls;
        Method method;
        Class rSetSidecarCallback;
        Class<?> cls2;
        Method methodGetWindowLayoutInfo;
        Class rtGetWindowLayoutInfo;
        Class<?> cls3;
        Method methodRegisterWindowLayoutChangeListener;
        Class rtRegisterWindowLayoutChangeListener;
        Class<?> cls4;
        Method methodUnregisterWindowLayoutChangeListener;
        try {
            SidecarInterface sidecarInterface = this.sidecar;
            Class rtUnregisterWindowLayoutChangeListener = null;
            if (sidecarInterface != null && (cls = sidecarInterface.getClass()) != null) {
                method = cls.getMethod("setSidecarCallback", SidecarInterface.SidecarCallback.class);
                Method methodSetSidecarCallback = method;
                rSetSidecarCallback = methodSetSidecarCallback != null ? null : methodSetSidecarCallback.getReturnType();
                if (Intrinsics.areEqual(rSetSidecarCallback, Void.TYPE)) {
                    throw new NoSuchMethodException(Intrinsics.stringPlus("Illegal return type for 'setSidecarCallback': ", rSetSidecarCallback));
                }
                SidecarInterface sidecarInterface2 = this.sidecar;
                if (sidecarInterface2 != null) {
                    sidecarInterface2.getDeviceState();
                }
                SidecarInterface sidecarInterface3 = this.sidecar;
                if (sidecarInterface3 != null) {
                    sidecarInterface3.onDeviceStateListenersChanged(true);
                }
                SidecarInterface sidecarInterface4 = this.sidecar;
                if (sidecarInterface4 != null && (cls2 = sidecarInterface4.getClass()) != null) {
                    methodGetWindowLayoutInfo = cls2.getMethod("getWindowLayoutInfo", IBinder.class);
                    rtGetWindowLayoutInfo = methodGetWindowLayoutInfo != null ? null : methodGetWindowLayoutInfo.getReturnType();
                    if (Intrinsics.areEqual(rtGetWindowLayoutInfo, SidecarWindowLayoutInfo.class)) {
                        throw new NoSuchMethodException(Intrinsics.stringPlus("Illegal return type for 'getWindowLayoutInfo': ", rtGetWindowLayoutInfo));
                    }
                    SidecarInterface sidecarInterface5 = this.sidecar;
                    if (sidecarInterface5 != null && (cls3 = sidecarInterface5.getClass()) != null) {
                        methodRegisterWindowLayoutChangeListener = cls3.getMethod("onWindowLayoutChangeListenerAdded", IBinder.class);
                        rtRegisterWindowLayoutChangeListener = methodRegisterWindowLayoutChangeListener != null ? null : methodRegisterWindowLayoutChangeListener.getReturnType();
                        if (Intrinsics.areEqual(rtRegisterWindowLayoutChangeListener, Void.TYPE)) {
                            throw new NoSuchMethodException(Intrinsics.stringPlus("Illegal return type for 'onWindowLayoutChangeListenerAdded': ", rtRegisterWindowLayoutChangeListener));
                        }
                        SidecarInterface sidecarInterface6 = this.sidecar;
                        if (sidecarInterface6 != null && (cls4 = sidecarInterface6.getClass()) != null) {
                            methodUnregisterWindowLayoutChangeListener = cls4.getMethod("onWindowLayoutChangeListenerRemoved", IBinder.class);
                            if (methodUnregisterWindowLayoutChangeListener == null) {
                                rtUnregisterWindowLayoutChangeListener = methodUnregisterWindowLayoutChangeListener.getReturnType();
                            }
                            if (Intrinsics.areEqual(rtUnregisterWindowLayoutChangeListener, Void.TYPE)) {
                                throw new NoSuchMethodException(Intrinsics.stringPlus("Illegal return type for 'onWindowLayoutChangeListenerRemoved': ", rtUnregisterWindowLayoutChangeListener));
                            }
                            SidecarDeviceState tmpDeviceState = new SidecarDeviceState();
                            try {
                                tmpDeviceState.posture = 3;
                            } catch (NoSuchFieldError e) {
                                Method methodSetPosture = SidecarDeviceState.class.getMethod("setPosture", Integer.TYPE);
                                methodSetPosture.invoke(tmpDeviceState, 3);
                                Method methodGetPosture = SidecarDeviceState.class.getMethod("getPosture", new Class[0]);
                                Object invoke = methodGetPosture.invoke(tmpDeviceState, new Object[0]);
                                if (invoke == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
                                }
                                int posture = ((Integer) invoke).intValue();
                                if (posture != 3) {
                                    throw new Exception("Invalid device posture getter/setter");
                                }
                            }
                            SidecarDisplayFeature displayFeature = new SidecarDisplayFeature();
                            Rect tmpRect = displayFeature.getRect();
                            Intrinsics.checkNotNullExpressionValue(tmpRect, "displayFeature.rect");
                            displayFeature.setRect(tmpRect);
                            displayFeature.getType();
                            displayFeature.setType(1);
                            SidecarWindowLayoutInfo windowLayoutInfo = new SidecarWindowLayoutInfo();
                            try {
                                List list = windowLayoutInfo.displayFeatures;
                            } catch (NoSuchFieldError e2) {
                                List featureList = new ArrayList();
                                featureList.add(displayFeature);
                                Method methodSetFeatures = SidecarWindowLayoutInfo.class.getMethod("setDisplayFeatures", List.class);
                                methodSetFeatures.invoke(windowLayoutInfo, featureList);
                                Method methodGetFeatures = SidecarWindowLayoutInfo.class.getMethod("getDisplayFeatures", new Class[0]);
                                Object invoke2 = methodGetFeatures.invoke(windowLayoutInfo, new Object[0]);
                                if (invoke2 == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<androidx.window.sidecar.SidecarDisplayFeature>");
                                }
                                List resultDisplayFeatures = (List) invoke2;
                                if (!Intrinsics.areEqual(featureList, resultDisplayFeatures)) {
                                    throw new Exception("Invalid display feature getter/setter");
                                }
                            }
                            return true;
                        }
                        methodUnregisterWindowLayoutChangeListener = null;
                        if (methodUnregisterWindowLayoutChangeListener == null) {
                        }
                        if (Intrinsics.areEqual(rtUnregisterWindowLayoutChangeListener, Void.TYPE)) {
                        }
                    }
                    methodRegisterWindowLayoutChangeListener = null;
                    if (methodRegisterWindowLayoutChangeListener != null) {
                    }
                    if (Intrinsics.areEqual(rtRegisterWindowLayoutChangeListener, Void.TYPE)) {
                    }
                }
                methodGetWindowLayoutInfo = null;
                if (methodGetWindowLayoutInfo != null) {
                }
                if (Intrinsics.areEqual(rtGetWindowLayoutInfo, SidecarWindowLayoutInfo.class)) {
                }
            }
            method = null;
            Method methodSetSidecarCallback2 = method;
            rSetSidecarCallback = methodSetSidecarCallback2 != null ? null : methodSetSidecarCallback2.getReturnType();
            if (Intrinsics.areEqual(rSetSidecarCallback, Void.TYPE)) {
            }
        } catch (Throwable th) {
            return false;
        }
    }

    /* compiled from: SidecarCompat.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u001c\u0010\u0007\u001a\u0010\u0012\f\u0012\n \t*\u0004\u0018\u00010\u00050\u00050\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Landroidx/window/layout/SidecarCompat$FirstAttachAdapter;", "Landroid/view/View$OnAttachStateChangeListener;", "sidecarCompat", "Landroidx/window/layout/SidecarCompat;", "activity", "Landroid/app/Activity;", "(Landroidx/window/layout/SidecarCompat;Landroid/app/Activity;)V", "activityWeakReference", "Ljava/lang/ref/WeakReference;", "kotlin.jvm.PlatformType", "onViewAttachedToWindow", "", "view", "Landroid/view/View;", "onViewDetachedFromWindow", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    private static final class FirstAttachAdapter implements View.OnAttachStateChangeListener {
        private final WeakReference<Activity> activityWeakReference;
        private final SidecarCompat sidecarCompat;

        public FirstAttachAdapter(SidecarCompat sidecarCompat, Activity activity) {
            Intrinsics.checkNotNullParameter(sidecarCompat, "sidecarCompat");
            Intrinsics.checkNotNullParameter(activity, "activity");
            this.sidecarCompat = sidecarCompat;
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.removeOnAttachStateChangeListener(this);
            Activity activity = this.activityWeakReference.get();
            IBinder token = SidecarCompat.INSTANCE.getActivityWindowToken$window_release(activity);
            if (activity == null || token == null) {
                return;
            }
            this.sidecarCompat.register(token, activity);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            Intrinsics.checkNotNullParameter(view, "view");
        }
    }

    /* compiled from: SidecarCompat.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0017J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0017¨\u0006\f"}, d2 = {"Landroidx/window/layout/SidecarCompat$TranslatingCallback;", "Landroidx/window/sidecar/SidecarInterface$SidecarCallback;", "(Landroidx/window/layout/SidecarCompat;)V", "onDeviceStateChanged", "", "newDeviceState", "Landroidx/window/sidecar/SidecarDeviceState;", "onWindowLayoutChanged", "windowToken", "Landroid/os/IBinder;", "newLayout", "Landroidx/window/sidecar/SidecarWindowLayoutInfo;", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public final class TranslatingCallback implements SidecarInterface.SidecarCallback {
        final /* synthetic */ SidecarCompat this$0;

        public TranslatingCallback(SidecarCompat this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        public void onDeviceStateChanged(SidecarDeviceState newDeviceState) {
            SidecarInterface sidecar;
            Intrinsics.checkNotNullParameter(newDeviceState, "newDeviceState");
            Iterable $this$forEach$iv = this.this$0.windowListenerRegisteredContexts.values();
            SidecarCompat sidecarCompat = this.this$0;
            for (Object element$iv : $this$forEach$iv) {
                Activity activity = (Activity) element$iv;
                IBinder windowToken = SidecarCompat.INSTANCE.getActivityWindowToken$window_release(activity);
                SidecarWindowLayoutInfo sidecarWindowLayoutInfo = null;
                if (windowToken != null && (sidecar = sidecarCompat.getSidecar()) != null) {
                    sidecarWindowLayoutInfo = sidecar.getWindowLayoutInfo(windowToken);
                }
                SidecarWindowLayoutInfo layoutInfo = sidecarWindowLayoutInfo;
                ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface = sidecarCompat.extensionCallback;
                if (extensionCallbackInterface != null) {
                    extensionCallbackInterface.onWindowLayoutChanged(activity, sidecarCompat.sidecarAdapter.translate(layoutInfo, newDeviceState));
                }
            }
        }

        public void onWindowLayoutChanged(IBinder windowToken, SidecarWindowLayoutInfo newLayout) {
            Intrinsics.checkNotNullParameter(windowToken, "windowToken");
            Intrinsics.checkNotNullParameter(newLayout, "newLayout");
            Activity activity = (Activity) this.this$0.windowListenerRegisteredContexts.get(windowToken);
            if (activity != null) {
                SidecarAdapter sidecarAdapter = this.this$0.sidecarAdapter;
                SidecarInterface sidecar = this.this$0.getSidecar();
                SidecarDeviceState deviceState = sidecar == null ? null : sidecar.getDeviceState();
                if (deviceState == null) {
                    deviceState = new SidecarDeviceState();
                }
                WindowLayoutInfo layoutInfo = sidecarAdapter.translate(newLayout, deviceState);
                ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface = this.this$0.extensionCallback;
                if (extensionCallbackInterface == null) {
                    return;
                }
                extensionCallbackInterface.onWindowLayoutChanged(activity, layoutInfo);
                return;
            }
            Log.w(SidecarCompat.TAG, "Unable to resolve activity from window token. Missing a call to #onWindowLayoutChangeListenerAdded()?");
        }
    }

    /* compiled from: SidecarCompat.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0007H\u0016R\u001c\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u00058\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Landroidx/window/layout/SidecarCompat$DistinctElementCallback;", "Landroidx/window/layout/ExtensionInterfaceCompat$ExtensionCallbackInterface;", "callbackInterface", "(Landroidx/window/layout/ExtensionInterfaceCompat$ExtensionCallbackInterface;)V", "activityWindowLayoutInfo", "Ljava/util/WeakHashMap;", "Landroid/app/Activity;", "Landroidx/window/layout/WindowLayoutInfo;", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "onWindowLayoutChanged", "", "activity", "newLayout", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    private static final class DistinctElementCallback implements ExtensionInterfaceCompat.ExtensionCallbackInterface {
        private final WeakHashMap<Activity, WindowLayoutInfo> activityWindowLayoutInfo;
        private final ExtensionInterfaceCompat.ExtensionCallbackInterface callbackInterface;
        private final ReentrantLock lock;

        public DistinctElementCallback(ExtensionInterfaceCompat.ExtensionCallbackInterface callbackInterface) {
            Intrinsics.checkNotNullParameter(callbackInterface, "callbackInterface");
            this.callbackInterface = callbackInterface;
            this.lock = new ReentrantLock();
            this.activityWindowLayoutInfo = new WeakHashMap<>();
        }

        @Override // androidx.window.layout.ExtensionInterfaceCompat.ExtensionCallbackInterface
        public void onWindowLayoutChanged(Activity activity, WindowLayoutInfo newLayout) {
            Intrinsics.checkNotNullParameter(activity, "activity");
            Intrinsics.checkNotNullParameter(newLayout, "newLayout");
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                WindowLayoutInfo lastInfo = this.activityWindowLayoutInfo.get(activity);
                if (Intrinsics.areEqual(newLayout, lastInfo)) {
                    return;
                }
                this.activityWindowLayoutInfo.put(activity, newLayout);
                reentrantLock.unlock();
                this.callbackInterface.onWindowLayoutChanged(activity, newLayout);
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    @Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001¢\u0006\u0002\u0010\u0005J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0007H\u0016J\u0018\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\rH\u0016R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0002@\u0002X\u0083\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b8\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Landroidx/window/layout/SidecarCompat$DistinctSidecarElementCallback;", "Landroidx/window/sidecar/SidecarInterface$SidecarCallback;", "sidecarAdapter", "Landroidx/window/layout/SidecarAdapter;", "callbackInterface", "(Landroidx/window/layout/SidecarAdapter;Landroidx/window/sidecar/SidecarInterface$SidecarCallback;)V", "lastDeviceState", "Landroidx/window/sidecar/SidecarDeviceState;", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "mActivityWindowLayoutInfo", "Ljava/util/WeakHashMap;", "Landroid/os/IBinder;", "Landroidx/window/sidecar/SidecarWindowLayoutInfo;", "onDeviceStateChanged", "", "newDeviceState", "onWindowLayoutChanged", "token", "newLayout", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    private static final class DistinctSidecarElementCallback implements SidecarInterface.SidecarCallback {
        private final SidecarInterface.SidecarCallback callbackInterface;
        private SidecarDeviceState lastDeviceState;
        private final ReentrantLock lock;
        private final WeakHashMap<IBinder, SidecarWindowLayoutInfo> mActivityWindowLayoutInfo;
        private final SidecarAdapter sidecarAdapter;

        public DistinctSidecarElementCallback(SidecarAdapter sidecarAdapter, SidecarInterface.SidecarCallback callbackInterface) {
            Intrinsics.checkNotNullParameter(sidecarAdapter, "sidecarAdapter");
            Intrinsics.checkNotNullParameter(callbackInterface, "callbackInterface");
            this.sidecarAdapter = sidecarAdapter;
            this.callbackInterface = callbackInterface;
            this.lock = new ReentrantLock();
            this.mActivityWindowLayoutInfo = new WeakHashMap<>();
        }

        public void onDeviceStateChanged(SidecarDeviceState newDeviceState) {
            Intrinsics.checkNotNullParameter(newDeviceState, "newDeviceState");
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                if (this.sidecarAdapter.isEqualSidecarDeviceState(this.lastDeviceState, newDeviceState)) {
                    return;
                }
                this.lastDeviceState = newDeviceState;
                this.callbackInterface.onDeviceStateChanged(newDeviceState);
                Unit unit = Unit.INSTANCE;
            } finally {
                reentrantLock.unlock();
            }
        }

        public void onWindowLayoutChanged(IBinder token, SidecarWindowLayoutInfo newLayout) {
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(newLayout, "newLayout");
            synchronized (this.lock) {
                SidecarWindowLayoutInfo lastInfo = this.mActivityWindowLayoutInfo.get(token);
                if (this.sidecarAdapter.isEqualSidecarWindowLayoutInfo(lastInfo, newLayout)) {
                    return;
                }
                this.mActivityWindowLayoutInfo.put(token, newLayout);
                this.callbackInterface.onWindowLayoutChanged(token, newLayout);
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    @Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0019\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0000¢\u0006\u0002\b\rJ\u0017\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0000¢\u0006\u0002\b\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u00068F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u0013"}, d2 = {"Landroidx/window/layout/SidecarCompat$Companion;", "", "()V", "TAG", "", "sidecarVersion", "Landroidx/window/core/Version;", "getSidecarVersion", "()Landroidx/window/core/Version;", "getActivityWindowToken", "Landroid/os/IBinder;", "activity", "Landroid/app/Activity;", "getActivityWindowToken$window_release", "getSidecarCompat", "Landroidx/window/sidecar/SidecarInterface;", "context", "Landroid/content/Context;", "getSidecarCompat$window_release", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Version getSidecarVersion() {
            try {
                String vendorVersion = SidecarProvider.getApiVersion();
                if (TextUtils.isEmpty(vendorVersion)) {
                    return null;
                }
                return Version.INSTANCE.parse(vendorVersion);
            } catch (NoClassDefFoundError e) {
                return null;
            } catch (UnsupportedOperationException e2) {
                return null;
            }
        }

        public final SidecarInterface getSidecarCompat$window_release(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            return SidecarProvider.getSidecarImpl(context.getApplicationContext());
        }

        public final IBinder getActivityWindowToken$window_release(Activity activity) {
            Window window;
            WindowManager.LayoutParams attributes;
            if (activity == null || (window = activity.getWindow()) == null || (attributes = window.getAttributes()) == null) {
                return null;
            }
            return attributes.token;
        }
    }
}
