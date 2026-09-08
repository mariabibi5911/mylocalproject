package androidx.navigation.fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavGraphBuilder;
import androidx.navigation.NavigatorProvider;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

/* compiled from: FragmentNavigatorDestinationBuilder.kt */
@Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a#\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0087\b\u001a?\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b¢\u0006\u0002\b\nH\u0087\bø\u0001\u0000\u001a!\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\u0006\u0010\u000b\u001a\u00020\fH\u0086\b\u001a=\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\u0006\u0010\u000b\u001a\u00020\f2\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b¢\u0006\u0002\b\nH\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, d2 = {"fragment", "", "F", "Landroidx/fragment/app/Fragment;", "Landroidx/navigation/NavGraphBuilder;", "id", "", "builder", "Lkotlin/Function1;", "Landroidx/navigation/fragment/FragmentNavigatorDestinationBuilder;", "Lkotlin/ExtensionFunctionType;", "route", "", "navigation-fragment_release"}, k = 2, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class FragmentNavigatorDestinationBuilderKt {
    @Deprecated(message = "Use routes to create your FragmentDestination instead", replaceWith = @ReplaceWith(expression = "fragment<F>(route = id.toString())", imports = {}))
    public static final /* synthetic */ <F extends Fragment> void fragment(NavGraphBuilder $this$fragment, int id) {
        Intrinsics.checkNotNullParameter($this$fragment, "<this>");
        NavigatorProvider $this$get$iv$iv = $this$fragment.getProvider();
        FragmentNavigator fragmentNavigator = (FragmentNavigator) $this$get$iv$iv.getNavigator(FragmentNavigator.class);
        Intrinsics.reifiedOperationMarker(4, "F");
        $this$fragment.destination(new FragmentNavigatorDestinationBuilder(fragmentNavigator, id, (KClass<? extends Fragment>) Reflection.getOrCreateKotlinClass(Fragment.class)));
    }

    @Deprecated(message = "Use routes to create your FragmentDestination instead", replaceWith = @ReplaceWith(expression = "fragment<F>(route = id.toString()) { builder.invoke() }", imports = {}))
    public static final /* synthetic */ <F extends Fragment> void fragment(NavGraphBuilder $this$fragment, int id, Function1<? super FragmentNavigatorDestinationBuilder, Unit> builder) {
        Intrinsics.checkNotNullParameter($this$fragment, "<this>");
        Intrinsics.checkNotNullParameter(builder, "builder");
        NavigatorProvider $this$get$iv = $this$fragment.getProvider();
        FragmentNavigator fragmentNavigator = (FragmentNavigator) $this$get$iv.getNavigator(FragmentNavigator.class);
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentNavigatorDestinationBuilder fragmentNavigatorDestinationBuilder = new FragmentNavigatorDestinationBuilder(fragmentNavigator, id, (KClass<? extends Fragment>) Reflection.getOrCreateKotlinClass(Fragment.class));
        builder.invoke(fragmentNavigatorDestinationBuilder);
        $this$fragment.destination(fragmentNavigatorDestinationBuilder);
    }

    public static final /* synthetic */ <F extends Fragment> void fragment(NavGraphBuilder $this$fragment, String route) {
        Intrinsics.checkNotNullParameter($this$fragment, "<this>");
        Intrinsics.checkNotNullParameter(route, "route");
        NavigatorProvider $this$get$iv$iv = $this$fragment.getProvider();
        FragmentNavigator fragmentNavigator = (FragmentNavigator) $this$get$iv$iv.getNavigator(FragmentNavigator.class);
        Intrinsics.reifiedOperationMarker(4, "F");
        $this$fragment.destination(new FragmentNavigatorDestinationBuilder(fragmentNavigator, route, (KClass<? extends Fragment>) Reflection.getOrCreateKotlinClass(Fragment.class)));
    }

    public static final /* synthetic */ <F extends Fragment> void fragment(NavGraphBuilder $this$fragment, String route, Function1<? super FragmentNavigatorDestinationBuilder, Unit> builder) {
        Intrinsics.checkNotNullParameter($this$fragment, "<this>");
        Intrinsics.checkNotNullParameter(route, "route");
        Intrinsics.checkNotNullParameter(builder, "builder");
        NavigatorProvider $this$get$iv = $this$fragment.getProvider();
        FragmentNavigator fragmentNavigator = (FragmentNavigator) $this$get$iv.getNavigator(FragmentNavigator.class);
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentNavigatorDestinationBuilder fragmentNavigatorDestinationBuilder = new FragmentNavigatorDestinationBuilder(fragmentNavigator, route, (KClass<? extends Fragment>) Reflection.getOrCreateKotlinClass(Fragment.class));
        builder.invoke(fragmentNavigatorDestinationBuilder);
        $this$fragment.destination(fragmentNavigatorDestinationBuilder);
    }
}
