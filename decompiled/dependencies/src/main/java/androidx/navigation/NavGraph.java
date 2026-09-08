package androidx.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.collection.SparseArrayCompat;
import androidx.collection.SparseArrayKt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavDestination;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;

/* compiled from: NavGraph.kt */
@Metadata(d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 >2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00010\u0002:\u0001>B\u0015\u0012\u000e\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00000\u0004¢\u0006\u0002\u0010\u0005J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0000J\u000e\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0001J\u001f\u0010\"\u001a\u00020\u001e2\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010#\"\u00020\u0001¢\u0006\u0002\u0010$J\u0016\u0010\"\u001a\u00020\u001e2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010%J\u0006\u0010&\u001a\u00020\u001eJ\u0013\u0010'\u001a\u00020(2\b\u0010\u001f\u001a\u0004\u0018\u00010)H\u0096\u0002J\u0012\u0010*\u001a\u0004\u0018\u00010\u00012\b\b\u0001\u0010+\u001a\u00020\u0011J\u001c\u0010*\u001a\u0004\u0018\u00010\u00012\b\b\u0001\u0010+\u001a\u00020\u00112\u0006\u0010,\u001a\u00020(H\u0007J\u001a\u0010*\u001a\u0004\u0018\u00010\u00012\u0006\u0010-\u001a\u00020\u00072\u0006\u0010,\u001a\u00020(H\u0007J\u0012\u0010*\u001a\u0004\u0018\u00010\u00012\b\u0010-\u001a\u0004\u0018\u00010\u0007J\b\u0010.\u001a\u00020\u0011H\u0007J\b\u0010/\u001a\u00020\u0011H\u0016J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000101H\u0086\u0002J\u0012\u00102\u001a\u0004\u0018\u0001032\u0006\u00104\u001a\u000205H\u0017J\u0018\u00106\u001a\u00020\u001e2\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020:H\u0016J\u000e\u0010;\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0001J\u000e\u0010<\u001a\u00020\u001e2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010<\u001a\u00020\u001e2\u0006\u0010\u0018\u001a\u00020\u0007J\b\u0010=\u001a\u00020\u0007H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078WX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0019\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b8G¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00078G¢\u0006\u0006\u001a\u0004\b\u000f\u0010\tR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00118G@BX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R(\u0010\u0019\u001a\u0004\u0018\u00010\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0007@BX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\t\"\u0004\b\u001b\u0010\u001c¨\u0006?"}, d2 = {"Landroidx/navigation/NavGraph;", "Landroidx/navigation/NavDestination;", "", "navGraphNavigator", "Landroidx/navigation/Navigator;", "(Landroidx/navigation/Navigator;)V", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "nodes", "Landroidx/collection/SparseArrayCompat;", "getNodes", "()Landroidx/collection/SparseArrayCompat;", "startDestDisplayName", "getStartDestDisplayName", "startDestId", "", "startDestIdName", "startDestinationId", "getStartDestinationId", "()I", "setStartDestinationId", "(I)V", "startDestRoute", "startDestinationRoute", "getStartDestinationRoute", "setStartDestinationRoute", "(Ljava/lang/String;)V", "addAll", "", "other", "addDestination", "node", "addDestinations", "", "([Landroidx/navigation/NavDestination;)V", "", "clear", "equals", "", "", "findNode", "resId", "searchParents", "route", "getStartDestination", "hashCode", "iterator", "", "matchDeepLink", "Landroidx/navigation/NavDestination$DeepLinkMatch;", "navDeepLinkRequest", "Landroidx/navigation/NavDeepLinkRequest;", "onInflate", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "remove", "setStartDestination", "toString", "Companion", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public class NavGraph extends NavDestination implements Iterable<NavDestination>, KMappedMarker {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final SparseArrayCompat<NavDestination> nodes;
    private int startDestId;
    private String startDestIdName;
    private String startDestinationRoute;

    @JvmStatic
    public static final NavDestination findStartDestination(NavGraph navGraph) {
        return INSTANCE.findStartDestination(navGraph);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NavGraph(Navigator<? extends NavGraph> navGraphNavigator) {
        super(navGraphNavigator);
        Intrinsics.checkNotNullParameter(navGraphNavigator, "navGraphNavigator");
        this.nodes = new SparseArrayCompat<>();
    }

    public final SparseArrayCompat<NavDestination> getNodes() {
        return this.nodes;
    }

    @Override // androidx.navigation.NavDestination
    public void onInflate(Context context, AttributeSet attrs) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
        super.onInflate(context, attrs);
        TypedArray $this$use$iv = context.getResources().obtainAttributes(attrs, androidx.navigation.common.R.styleable.NavGraphNavigator);
        Intrinsics.checkNotNullExpressionValue($this$use$iv, "context.resources.obtain…vGraphNavigator\n        )");
        setStartDestinationId($this$use$iv.getResourceId(androidx.navigation.common.R.styleable.NavGraphNavigator_startDestination, 0));
        this.startDestIdName = NavDestination.INSTANCE.getDisplayName(context, this.startDestId);
        Unit unit = Unit.INSTANCE;
        $this$use$iv.recycle();
    }

    @Override // androidx.navigation.NavDestination
    public NavDestination.DeepLinkMatch matchDeepLink(NavDeepLinkRequest navDeepLinkRequest) {
        Intrinsics.checkNotNullParameter(navDeepLinkRequest, "navDeepLinkRequest");
        NavDestination.DeepLinkMatch bestMatch = super.matchDeepLink(navDeepLinkRequest);
        NavGraph $this$mapNotNull$iv = this;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
            NavDestination child = (NavDestination) element$iv$iv$iv;
            NavDestination.DeepLinkMatch matchDeepLink = child.matchDeepLink(navDeepLinkRequest);
            if (matchDeepLink != null) {
                destination$iv$iv.add(matchDeepLink);
            }
        }
        NavDestination.DeepLinkMatch bestChildMatch = (NavDestination.DeepLinkMatch) CollectionsKt.maxOrNull(destination$iv$iv);
        return (NavDestination.DeepLinkMatch) CollectionsKt.maxOrNull((Iterable) CollectionsKt.listOfNotNull((Object[]) new NavDestination.DeepLinkMatch[]{bestMatch, bestChildMatch}));
    }

    public final void addDestination(NavDestination node) {
        Intrinsics.checkNotNullParameter(node, "node");
        int id = node.getId();
        String innerRoute = node.getRoute();
        if (!((id == 0 && innerRoute == null) ? false : true)) {
            throw new IllegalArgumentException("Destinations must have an id or route. Call setId(), setRoute(), or include an android:id or app:route in your navigation XML.".toString());
        }
        if (getRoute() != null && !(!Intrinsics.areEqual(innerRoute, getRoute()))) {
            throw new IllegalArgumentException(("Destination " + node + " cannot have the same route as graph " + this).toString());
        }
        if (!(id != getId())) {
            throw new IllegalArgumentException(("Destination " + node + " cannot have the same id as graph " + this).toString());
        }
        NavDestination existingDestination = this.nodes.get(id);
        if (existingDestination == node) {
            return;
        }
        if (!(node.getParent() == null)) {
            throw new IllegalStateException("Destination already has a parent set. Call NavGraph.remove() to remove the previous parent.".toString());
        }
        if (existingDestination != null) {
            existingDestination.setParent(null);
        }
        node.setParent(this);
        this.nodes.put(node.getId(), node);
    }

    public final void addDestinations(Collection<? extends NavDestination> nodes) {
        Intrinsics.checkNotNullParameter(nodes, "nodes");
        for (NavDestination node : nodes) {
            if (node != null) {
                addDestination(node);
            }
        }
    }

    public final void addDestinations(NavDestination... nodes) {
        Intrinsics.checkNotNullParameter(nodes, "nodes");
        for (NavDestination node : nodes) {
            addDestination(node);
        }
    }

    public final NavDestination findNode(int resId) {
        return findNode(resId, true);
    }

    public final NavDestination findNode(String route) {
        String str = route;
        if (str == null || StringsKt.isBlank(str)) {
            return null;
        }
        return findNode(route, true);
    }

    public final NavDestination findNode(int resId, boolean searchParents) {
        NavDestination destination = this.nodes.get(resId);
        if (destination != null) {
            return destination;
        }
        if (!searchParents || getParent() == null) {
            return null;
        }
        NavGraph parent = getParent();
        Intrinsics.checkNotNull(parent);
        return parent.findNode(resId);
    }

    public final NavDestination findNode(String route, boolean searchParents) {
        Intrinsics.checkNotNullParameter(route, "route");
        int id = NavDestination.INSTANCE.createRoute(route).hashCode();
        NavDestination destination = this.nodes.get(id);
        if (destination != null) {
            return destination;
        }
        if (!searchParents || getParent() == null) {
            return null;
        }
        NavGraph parent = getParent();
        Intrinsics.checkNotNull(parent);
        return parent.findNode(route);
    }

    @Override // java.lang.Iterable
    public final Iterator<NavDestination> iterator() {
        return new NavGraph$iterator$1(this);
    }

    public final void addAll(NavGraph other) {
        Intrinsics.checkNotNullParameter(other, "other");
        Iterator iterator = other.iterator();
        while (iterator.hasNext()) {
            NavDestination destination = iterator.next();
            iterator.remove();
            addDestination(destination);
        }
    }

    public final void remove(NavDestination node) {
        Intrinsics.checkNotNullParameter(node, "node");
        int index = this.nodes.indexOfKey(node.getId());
        if (index >= 0) {
            this.nodes.valueAt(index).setParent(null);
            this.nodes.removeAt(index);
        }
    }

    public final void clear() {
        Iterator iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    @Override // androidx.navigation.NavDestination
    public String getDisplayName() {
        return getId() != 0 ? super.getDisplayName() : "the root navigation";
    }

    @Deprecated(message = "Use getStartDestinationId instead.", replaceWith = @ReplaceWith(expression = "startDestinationId", imports = {}))
    public final int getStartDestination() {
        return getStartDestId();
    }

    /* renamed from: getStartDestinationId, reason: from getter */
    public final int getStartDestId() {
        return this.startDestId;
    }

    private final void setStartDestinationId(int startDestId) {
        if (!(startDestId != getId())) {
            throw new IllegalArgumentException(("Start destination " + startDestId + " cannot use the same id as the graph " + this).toString());
        }
        if (this.startDestinationRoute != null) {
            setStartDestinationRoute(null);
        }
        this.startDestId = startDestId;
        this.startDestIdName = null;
    }

    public final void setStartDestination(int startDestId) {
        setStartDestinationId(startDestId);
    }

    public final void setStartDestination(String startDestRoute) {
        Intrinsics.checkNotNullParameter(startDestRoute, "startDestRoute");
        setStartDestinationRoute(startDestRoute);
    }

    public final String getStartDestinationRoute() {
        return this.startDestinationRoute;
    }

    private final void setStartDestinationRoute(String startDestRoute) {
        int hashCode;
        if (startDestRoute == null) {
            hashCode = 0;
        } else {
            if (!(!Intrinsics.areEqual(startDestRoute, getRoute()))) {
                throw new IllegalArgumentException(("Start destination " + startDestRoute + " cannot use the same route as the graph " + this).toString());
            }
            if (!(!StringsKt.isBlank(startDestRoute))) {
                throw new IllegalArgumentException("Cannot have an empty start destination route".toString());
            }
            String internalRoute = NavDestination.INSTANCE.createRoute(startDestRoute);
            hashCode = internalRoute.hashCode();
        }
        this.startDestId = hashCode;
        this.startDestinationRoute = startDestRoute;
    }

    public final String getStartDestDisplayName() {
        if (this.startDestIdName == null) {
            String str = this.startDestinationRoute;
            if (str == null) {
                str = String.valueOf(this.startDestId);
            }
            this.startDestIdName = str;
        }
        String str2 = this.startDestIdName;
        Intrinsics.checkNotNull(str2);
        return str2;
    }

    @Override // androidx.navigation.NavDestination
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        NavDestination startDestination = findNode(this.startDestinationRoute);
        if (startDestination == null) {
            startDestination = findNode(getStartDestId());
        }
        sb.append(" startDestination=");
        if (startDestination == null) {
            String str = this.startDestinationRoute;
            if (str != null) {
                sb.append(str);
            } else {
                String str2 = this.startDestIdName;
                if (str2 != null) {
                    sb.append(str2);
                } else {
                    sb.append("0x" + Integer.toHexString(this.startDestId));
                }
            }
        } else {
            sb.append("{");
            sb.append(startDestination.toString());
            sb.append("}");
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }

    @Override // androidx.navigation.NavDestination
    public boolean equals(Object other) {
        if (other == null || !(other instanceof NavGraph)) {
            return false;
        }
        List copy = SequencesKt.toMutableList(SequencesKt.asSequence(SparseArrayKt.valueIterator(this.nodes)));
        Iterator $this$forEach$iv = SparseArrayKt.valueIterator(((NavGraph) other).nodes);
        while ($this$forEach$iv.hasNext()) {
            Object element$iv = $this$forEach$iv.next();
            NavDestination it = (NavDestination) element$iv;
            copy.remove(it);
        }
        if (!super.equals(other)) {
            return false;
        }
        SparseArrayCompat $receiver$iv = this.nodes;
        int size = $receiver$iv.size();
        SparseArrayCompat $receiver$iv2 = ((NavGraph) other).nodes;
        return size == $receiver$iv2.size() && getStartDestId() == ((NavGraph) other).getStartDestId() && copy.isEmpty();
    }

    @Override // androidx.navigation.NavDestination
    public int hashCode() {
        int result = getStartDestId();
        SparseArrayCompat $receiver$iv = this.nodes;
        int size = $receiver$iv.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = $receiver$iv.keyAt(index$iv);
            NavDestination value = $receiver$iv.valueAt(index$iv);
            result = (((result * 31) + key) * 31) + value.hashCode();
        }
        return result;
    }

    /* compiled from: NavGraph.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\f\u0010\u0003\u001a\u00020\u0004*\u00020\u0005H\u0007¨\u0006\u0006"}, d2 = {"Landroidx/navigation/NavGraph$Companion;", "", "()V", "findStartDestination", "Landroidx/navigation/NavDestination;", "Landroidx/navigation/NavGraph;", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final NavDestination findStartDestination(NavGraph $this$findStartDestination) {
            Intrinsics.checkNotNullParameter($this$findStartDestination, "<this>");
            return (NavDestination) SequencesKt.last(SequencesKt.generateSequence($this$findStartDestination.findNode($this$findStartDestination.getStartDestId()), new Function1<NavDestination, NavDestination>() { // from class: androidx.navigation.NavGraph$Companion$findStartDestination$1
                @Override // kotlin.jvm.functions.Function1
                public final NavDestination invoke(NavDestination it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    if (it instanceof NavGraph) {
                        return ((NavGraph) it).findNode(((NavGraph) it).getStartDestId());
                    }
                    return null;
                }
            }));
        }
    }
}
