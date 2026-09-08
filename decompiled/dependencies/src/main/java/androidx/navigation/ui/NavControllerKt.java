package androidx.navigation.ui;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.ui.AppBarConfiguration;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NavController.kt */
@Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, d2 = {"navigateUp", "", "Landroidx/navigation/NavController;", "drawerLayout", "Landroidx/customview/widget/Openable;", "appBarConfiguration", "Landroidx/navigation/ui/AppBarConfiguration;", "navigation-ui_release"}, k = 2, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class NavControllerKt {
    public static final boolean navigateUp(NavController $this$navigateUp, Openable drawerLayout) {
        Intrinsics.checkNotNullParameter($this$navigateUp, "<this>");
        NavGraph navGraph$iv = $this$navigateUp.getGraph();
        Function0 fallbackOnNavigateUpListener$iv = AppBarConfigurationKt$AppBarConfiguration$1.INSTANCE;
        return NavigationUI.navigateUp($this$navigateUp, new AppBarConfiguration.Builder(navGraph$iv).setOpenableLayout(drawerLayout).setFallbackOnNavigateUpListener(new AppBarConfigurationKt$sam$i$androidx_navigation_ui_AppBarConfiguration_OnNavigateUpListener$0(fallbackOnNavigateUpListener$iv)).build());
    }

    public static final boolean navigateUp(NavController $this$navigateUp, AppBarConfiguration appBarConfiguration) {
        Intrinsics.checkNotNullParameter($this$navigateUp, "<this>");
        Intrinsics.checkNotNullParameter(appBarConfiguration, "appBarConfiguration");
        return NavigationUI.navigateUp($this$navigateUp, appBarConfiguration);
    }
}
