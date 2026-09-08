package androidx.navigation.ui;

import android.view.MenuItem;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MenuItem.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, d2 = {"onNavDestinationSelected", "", "Landroid/view/MenuItem;", "navController", "Landroidx/navigation/NavController;", "navigation-ui_release"}, k = 2, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class MenuItemKt {
    public static final boolean onNavDestinationSelected(MenuItem $this$onNavDestinationSelected, NavController navController) {
        Intrinsics.checkNotNullParameter($this$onNavDestinationSelected, "<this>");
        Intrinsics.checkNotNullParameter(navController, "navController");
        return NavigationUI.onNavDestinationSelected($this$onNavDestinationSelected, navController);
    }
}
