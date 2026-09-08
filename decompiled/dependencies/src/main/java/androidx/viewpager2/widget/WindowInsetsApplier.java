package androidx.viewpager2.widget;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.view.View;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public final class WindowInsetsApplier implements OnApplyWindowInsetsListener {
    private WindowInsetsApplier() {
    }

    public static boolean install(ViewPager2 viewPager) {
        ApplicationInfo appInfo = viewPager.getContext().getApplicationInfo();
        if (Build.VERSION.SDK_INT >= 30 && appInfo.targetSdkVersion >= 30) {
            return false;
        }
        ViewCompat.setOnApplyWindowInsetsListener(viewPager, new WindowInsetsApplier());
        return true;
    }

    @Override // androidx.core.view.OnApplyWindowInsetsListener
    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        ViewPager2 viewPager = (ViewPager2) v;
        WindowInsetsCompat applied = ViewCompat.onApplyWindowInsets(viewPager, insets);
        if (applied.isConsumed()) {
            return applied;
        }
        RecyclerView rv = viewPager.mRecyclerView;
        int count = rv.getChildCount();
        for (int i = 0; i < count; i++) {
            ViewCompat.dispatchApplyWindowInsets(rv.getChildAt(i), new WindowInsetsCompat(applied));
        }
        return consumeAllInsets(applied);
    }

    private WindowInsetsCompat consumeAllInsets(WindowInsetsCompat insets) {
        if (WindowInsetsCompat.CONSUMED.toWindowInsets() != null) {
            return WindowInsetsCompat.CONSUMED;
        }
        return insets.consumeSystemWindowInsets().consumeStableInsets();
    }
}
