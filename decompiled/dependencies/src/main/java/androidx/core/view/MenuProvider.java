package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.android.tools.r8.annotations.SynthesizedClassV2;

/* loaded from: classes.dex */
public interface MenuProvider {
    void onCreateMenu(Menu menu, MenuInflater menuInflater);

    void onMenuClosed(Menu menu);

    boolean onMenuItemSelected(MenuItem menuItem);

    void onPrepareMenu(Menu menu);

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: androidx.core.view.MenuProvider$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
        public static void $default$onPrepareMenu(MenuProvider _this, Menu menu) {
        }

        public static void $default$onMenuClosed(MenuProvider _this, Menu menu) {
        }
    }
}
