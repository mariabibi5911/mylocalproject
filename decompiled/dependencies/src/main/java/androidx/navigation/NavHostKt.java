package androidx.navigation;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NavHost.kt */
@Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a=\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0003\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00042\u0017\u0010\u0006\u001a\u0013\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0002\b\nH\u0087\bø\u0001\u0000\u001a=\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0017\u0010\u0006\u001a\u0013\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0002\b\nH\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, d2 = {"createGraph", "Landroidx/navigation/NavGraph;", "Landroidx/navigation/NavHost;", "id", "", "startDestination", "builder", "Lkotlin/Function1;", "Landroidx/navigation/NavGraphBuilder;", "", "Lkotlin/ExtensionFunctionType;", "", "route", "navigation-runtime_release"}, k = 2, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class NavHostKt {
    public static /* synthetic */ NavGraph createGraph$default(NavHost $this$createGraph_u24default, int id, int startDestination, Function1 builder, int i, Object obj) {
        if ((i & 1) != 0) {
            id = 0;
        }
        Intrinsics.checkNotNullParameter($this$createGraph_u24default, "<this>");
        Intrinsics.checkNotNullParameter(builder, "builder");
        NavController $this$createGraph$iv = $this$createGraph_u24default.getNavController();
        NavigatorProvider $this$navigation$iv$iv = $this$createGraph$iv.get_navigatorProvider();
        NavGraphBuilder navGraphBuilder = new NavGraphBuilder($this$navigation$iv$iv, id, startDestination);
        builder.invoke(navGraphBuilder);
        return navGraphBuilder.build();
    }

    @Deprecated(message = "Use routes to create your NavGraph instead", replaceWith = @ReplaceWith(expression = "createGraph(startDestination = startDestination.toString(), route = id.toString()) { builder.invoke() }", imports = {}))
    public static final NavGraph createGraph(NavHost $this$createGraph, int id, int startDestination, Function1<? super NavGraphBuilder, Unit> builder) {
        Intrinsics.checkNotNullParameter($this$createGraph, "<this>");
        Intrinsics.checkNotNullParameter(builder, "builder");
        NavController $this$createGraph$iv = $this$createGraph.getNavController();
        NavigatorProvider $this$navigation$iv$iv = $this$createGraph$iv.get_navigatorProvider();
        NavGraphBuilder navGraphBuilder = new NavGraphBuilder($this$navigation$iv$iv, id, startDestination);
        builder.invoke(navGraphBuilder);
        return navGraphBuilder.build();
    }

    public static /* synthetic */ NavGraph createGraph$default(NavHost $this$createGraph_u24default, String startDestination, String route, Function1 builder, int i, Object obj) {
        if ((i & 2) != 0) {
            route = null;
        }
        Intrinsics.checkNotNullParameter($this$createGraph_u24default, "<this>");
        Intrinsics.checkNotNullParameter(startDestination, "startDestination");
        Intrinsics.checkNotNullParameter(builder, "builder");
        NavController $this$createGraph$iv = $this$createGraph_u24default.getNavController();
        NavigatorProvider $this$navigation$iv$iv = $this$createGraph$iv.get_navigatorProvider();
        NavGraphBuilder navGraphBuilder = new NavGraphBuilder($this$navigation$iv$iv, startDestination, route);
        builder.invoke(navGraphBuilder);
        return navGraphBuilder.build();
    }

    public static final NavGraph createGraph(NavHost $this$createGraph, String startDestination, String route, Function1<? super NavGraphBuilder, Unit> builder) {
        Intrinsics.checkNotNullParameter($this$createGraph, "<this>");
        Intrinsics.checkNotNullParameter(startDestination, "startDestination");
        Intrinsics.checkNotNullParameter(builder, "builder");
        NavController $this$createGraph$iv = $this$createGraph.getNavController();
        NavigatorProvider $this$navigation$iv$iv = $this$createGraph$iv.get_navigatorProvider();
        NavGraphBuilder navGraphBuilder = new NavGraphBuilder($this$navigation$iv$iv, startDestination, route);
        builder.invoke(navGraphBuilder);
        return navGraphBuilder.build();
    }
}
