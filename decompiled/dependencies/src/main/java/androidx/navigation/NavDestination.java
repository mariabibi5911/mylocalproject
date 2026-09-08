package androidx.navigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import androidx.collection.SparseArrayCompat;
import androidx.collection.SparseArrayKt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavDeepLink;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;

/* compiled from: NavDestination.kt */
@Metadata(d1 = {"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\r\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0016\u0018\u0000 X2\u00020\u0001:\u0003WXYB\u0017\b\u0016\u0012\u000e\u0010\u0002\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00000\u0003¢\u0006\u0002\u0010\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0016\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\u00062\u0006\u00103\u001a\u00020\nJ\u000e\u00104\u001a\u0002012\u0006\u00105\u001a\u00020\u0014J\u000e\u00104\u001a\u0002012\u0006\u00106\u001a\u00020\u0006J\u0014\u00107\u001a\u0004\u0018\u0001082\b\u00109\u001a\u0004\u0018\u000108H\u0007J\u0014\u0010:\u001a\u00020;2\n\b\u0002\u0010<\u001a\u0004\u0018\u00010\u0000H\u0007J\u0013\u0010=\u001a\u00020>2\b\u0010?\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\u0012\u0010@\u001a\u0004\u0018\u00010\r2\b\b\u0001\u0010\u0018\u001a\u00020\u0019J\u0010\u0010A\u001a\u00020>2\u0006\u0010B\u001a\u00020CH\u0016J\u0010\u0010A\u001a\u00020>2\u0006\u0010D\u001a\u00020EH\u0016J\b\u0010F\u001a\u00020\u0019H\u0016J\u0012\u0010G\u001a\u0004\u0018\u00010H2\u0006\u0010I\u001a\u00020EH\u0017J\u0018\u0010J\u001a\u0002012\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020NH\u0017J\u0018\u0010O\u001a\u0002012\b\b\u0001\u0010P\u001a\u00020\u00192\u0006\u0010Q\u001a\u00020\rJ\u001a\u0010O\u001a\u0002012\b\b\u0001\u0010P\u001a\u00020\u00192\b\b\u0001\u0010R\u001a\u00020\u0019J\u0010\u0010S\u001a\u0002012\b\b\u0001\u0010P\u001a\u00020\u0019J\u000e\u0010T\u001a\u0002012\u0006\u00102\u001a\u00020\u0006J\b\u0010U\u001a\u00020>H\u0017J\b\u0010V\u001a\u00020\u0006H\u0016R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\u00068WX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R(\u0010\u0018\u001a\u00020\u00192\b\b\u0001\u0010\u0018\u001a\u00020\u00198G@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u001f\u001a\u0004\u0018\u00010 X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0017R(\u0010(\u001a\u0004\u0018\u00010'2\b\u0010&\u001a\u0004\u0018\u00010'@GX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R(\u0010-\u001a\u0004\u0018\u00010\u00062\b\u0010-\u001a\u0004\u0018\u00010\u0006@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0017\"\u0004\b/\u0010\u0007¨\u0006Z"}, d2 = {"Landroidx/navigation/NavDestination;", "", "navigator", "Landroidx/navigation/Navigator;", "(Landroidx/navigation/Navigator;)V", "navigatorName", "", "(Ljava/lang/String;)V", "_arguments", "", "Landroidx/navigation/NavArgument;", "actions", "Landroidx/collection/SparseArrayCompat;", "Landroidx/navigation/NavAction;", "arguments", "", "getArguments", "()Ljava/util/Map;", "deepLinks", "", "Landroidx/navigation/NavDeepLink;", "displayName", "getDisplayName", "()Ljava/lang/String;", "id", "", "getId", "()I", "setId", "(I)V", "idName", "label", "", "getLabel", "()Ljava/lang/CharSequence;", "setLabel", "(Ljava/lang/CharSequence;)V", "getNavigatorName", "<set-?>", "Landroidx/navigation/NavGraph;", "parent", "getParent", "()Landroidx/navigation/NavGraph;", "setParent", "(Landroidx/navigation/NavGraph;)V", "route", "getRoute", "setRoute", "addArgument", "", "argumentName", "argument", "addDeepLink", "navDeepLink", "uriPattern", "addInDefaultArgs", "Landroid/os/Bundle;", "args", "buildDeepLinkIds", "", "previousDestination", "equals", "", "other", "getAction", "hasDeepLink", "deepLink", "Landroid/net/Uri;", "deepLinkRequest", "Landroidx/navigation/NavDeepLinkRequest;", "hashCode", "matchDeepLink", "Landroidx/navigation/NavDestination$DeepLinkMatch;", "navDeepLinkRequest", "onInflate", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "putAction", "actionId", "action", "destId", "removeAction", "removeArgument", "supportsActions", "toString", "ClassType", "Companion", "DeepLinkMatch", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public class NavDestination {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Map<String, Class<?>> classes = new LinkedHashMap();
    private Map<String, NavArgument> _arguments;
    private final SparseArrayCompat<NavAction> actions;
    private final List<NavDeepLink> deepLinks;
    private int id;
    private String idName;
    private CharSequence label;
    private final String navigatorName;
    private NavGraph parent;
    private String route;

    /* compiled from: NavDestination.kt */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\u0002\u0018\u00002\u00020\u0001B\f\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003R\u0013\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Landroidx/navigation/NavDestination$ClassType;", "", "value", "Lkotlin/reflect/KClass;", "()Ljava/lang/Class;", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    @kotlin.annotation.Target(allowedTargets = {AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS})
    @Retention(RetentionPolicy.CLASS)
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    /* loaded from: classes.dex */
    public @interface ClassType {
        Class<?> value();
    }

    @JvmStatic
    public static final String getDisplayName(Context context, int i) {
        return INSTANCE.getDisplayName(context, i);
    }

    public static final Sequence<NavDestination> getHierarchy(NavDestination navDestination) {
        return INSTANCE.getHierarchy(navDestination);
    }

    @JvmStatic
    protected static final <C> Class<? extends C> parseClassFromName(Context context, String str, Class<? extends C> cls) {
        return INSTANCE.parseClassFromName(context, str, cls);
    }

    @JvmStatic
    public static final <C> Class<? extends C> parseClassFromNameInternal(Context context, String str, Class<? extends C> cls) {
        return INSTANCE.parseClassFromNameInternal(context, str, cls);
    }

    public final int[] buildDeepLinkIds() {
        return buildDeepLinkIds$default(this, null, 1, null);
    }

    public NavDestination(String navigatorName) {
        Intrinsics.checkNotNullParameter(navigatorName, "navigatorName");
        this.navigatorName = navigatorName;
        this.deepLinks = new ArrayList();
        this.actions = new SparseArrayCompat<>();
        this._arguments = new LinkedHashMap();
    }

    public final String getNavigatorName() {
        return this.navigatorName;
    }

    /* compiled from: NavDestination.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u0011\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0000H\u0096\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Landroidx/navigation/NavDestination$DeepLinkMatch;", "", "destination", "Landroidx/navigation/NavDestination;", "matchingArgs", "Landroid/os/Bundle;", "isExactDeepLink", "", "hasMatchingAction", "mimeTypeMatchLevel", "", "(Landroidx/navigation/NavDestination;Landroid/os/Bundle;ZZI)V", "getDestination", "()Landroidx/navigation/NavDestination;", "getMatchingArgs", "()Landroid/os/Bundle;", "compareTo", "other", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class DeepLinkMatch implements Comparable<DeepLinkMatch> {
        private final NavDestination destination;
        private final boolean hasMatchingAction;
        private final boolean isExactDeepLink;
        private final Bundle matchingArgs;
        private final int mimeTypeMatchLevel;

        public DeepLinkMatch(NavDestination destination, Bundle matchingArgs, boolean isExactDeepLink, boolean hasMatchingAction, int mimeTypeMatchLevel) {
            Intrinsics.checkNotNullParameter(destination, "destination");
            this.destination = destination;
            this.matchingArgs = matchingArgs;
            this.isExactDeepLink = isExactDeepLink;
            this.hasMatchingAction = hasMatchingAction;
            this.mimeTypeMatchLevel = mimeTypeMatchLevel;
        }

        public final NavDestination getDestination() {
            return this.destination;
        }

        public final Bundle getMatchingArgs() {
            return this.matchingArgs;
        }

        @Override // java.lang.Comparable
        public int compareTo(DeepLinkMatch other) {
            Intrinsics.checkNotNullParameter(other, "other");
            boolean z = this.isExactDeepLink;
            if (z && !other.isExactDeepLink) {
                return 1;
            }
            if (!z && other.isExactDeepLink) {
                return -1;
            }
            Bundle bundle = this.matchingArgs;
            if (bundle != null && other.matchingArgs == null) {
                return 1;
            }
            if (bundle == null && other.matchingArgs != null) {
                return -1;
            }
            if (bundle != null) {
                int size = bundle.size();
                Bundle bundle2 = other.matchingArgs;
                Intrinsics.checkNotNull(bundle2);
                int sizeDifference = size - bundle2.size();
                if (sizeDifference > 0) {
                    return 1;
                }
                if (sizeDifference < 0) {
                    return -1;
                }
            }
            boolean z2 = this.hasMatchingAction;
            if (z2 && !other.hasMatchingAction) {
                return 1;
            }
            if (!z2 && other.hasMatchingAction) {
                return -1;
            }
            return this.mimeTypeMatchLevel - other.mimeTypeMatchLevel;
        }
    }

    public final NavGraph getParent() {
        return this.parent;
    }

    public final void setParent(NavGraph navGraph) {
        this.parent = navGraph;
    }

    public final CharSequence getLabel() {
        return this.label;
    }

    public final void setLabel(CharSequence charSequence) {
        this.label = charSequence;
    }

    public final Map<String, NavArgument> getArguments() {
        return MapsKt.toMap(this._arguments);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public NavDestination(Navigator<? extends NavDestination> navigator) {
        this(NavigatorProvider.INSTANCE.getNameForNavigator$navigation_common_release(navigator.getClass()));
        Intrinsics.checkNotNullParameter(navigator, "navigator");
    }

    public void onInflate(Context context, AttributeSet attrs) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
        TypedArray $this$use$iv = context.getResources().obtainAttributes(attrs, androidx.navigation.common.R.styleable.Navigator);
        Intrinsics.checkNotNullExpressionValue($this$use$iv, "context.resources.obtain…s, R.styleable.Navigator)");
        setRoute($this$use$iv.getString(androidx.navigation.common.R.styleable.Navigator_route));
        if ($this$use$iv.hasValue(androidx.navigation.common.R.styleable.Navigator_android_id)) {
            setId($this$use$iv.getResourceId(androidx.navigation.common.R.styleable.Navigator_android_id, 0));
            this.idName = INSTANCE.getDisplayName(context, this.id);
        }
        this.label = $this$use$iv.getText(androidx.navigation.common.R.styleable.Navigator_android_label);
        Unit unit = Unit.INSTANCE;
        $this$use$iv.recycle();
    }

    public final int getId() {
        return this.id;
    }

    public final void setId(int id) {
        this.id = id;
        this.idName = null;
    }

    public final String getRoute() {
        return this.route;
    }

    public final void setRoute(String route) {
        Object element$iv;
        if (route == null) {
            setId(0);
        } else {
            if (!(!StringsKt.isBlank(route))) {
                throw new IllegalArgumentException("Cannot have an empty route".toString());
            }
            String internalRoute = INSTANCE.createRoute(route);
            setId(internalRoute.hashCode());
            addDeepLink(internalRoute);
        }
        Iterable iterable = this.deepLinks;
        List<NavDeepLink> list = (Collection) iterable;
        Iterable $this$firstOrNull$iv = iterable;
        Iterator it = $this$firstOrNull$iv.iterator();
        while (true) {
            if (it.hasNext()) {
                element$iv = it.next();
                NavDeepLink it2 = (NavDeepLink) element$iv;
                if (Intrinsics.areEqual(it2.getUriPattern(), INSTANCE.createRoute(this.route))) {
                    break;
                }
            } else {
                element$iv = null;
                break;
            }
        }
        TypeIntrinsics.asMutableCollection(list).remove(element$iv);
        this.route = route;
    }

    public String getDisplayName() {
        String str = this.idName;
        return str == null ? String.valueOf(this.id) : str;
    }

    public boolean hasDeepLink(Uri deepLink) {
        Intrinsics.checkNotNullParameter(deepLink, "deepLink");
        return hasDeepLink(new NavDeepLinkRequest(deepLink, null, null));
    }

    public boolean hasDeepLink(NavDeepLinkRequest deepLinkRequest) {
        Intrinsics.checkNotNullParameter(deepLinkRequest, "deepLinkRequest");
        return matchDeepLink(deepLinkRequest) != null;
    }

    public final void addDeepLink(String uriPattern) {
        Intrinsics.checkNotNullParameter(uriPattern, "uriPattern");
        addDeepLink(new NavDeepLink.Builder().setUriPattern(uriPattern).build());
    }

    public final void addDeepLink(NavDeepLink navDeepLink) {
        Intrinsics.checkNotNullParameter(navDeepLink, "navDeepLink");
        Map $this$filterValues$iv = getArguments();
        LinkedHashMap result$iv = new LinkedHashMap();
        Iterator<Map.Entry<String, NavArgument>> it = $this$filterValues$iv.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry entry$iv = it.next();
            NavArgument it2 = entry$iv.getValue();
            if ((it2.getIsNullable() || it2.getIsDefaultValuePresent()) ? false : true) {
                result$iv.put(entry$iv.getKey(), entry$iv.getValue());
            }
        }
        LinkedHashMap $this$filterValues$iv2 = result$iv;
        Iterable $this$filter$iv = $this$filterValues$iv2.keySet();
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            if (!navDeepLink.getArgumentsNames$navigation_common_release().contains((String) element$iv$iv)) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        List missingRequiredArguments = (List) destination$iv$iv;
        if (!missingRequiredArguments.isEmpty()) {
            throw new IllegalArgumentException(("Deep link " + navDeepLink.getUriPattern() + " can't be used to open destination " + this + ".\nFollowing required arguments are missing: " + missingRequiredArguments).toString());
        }
        this.deepLinks.add(navDeepLink);
    }

    public DeepLinkMatch matchDeepLink(NavDeepLinkRequest navDeepLinkRequest) {
        Intrinsics.checkNotNullParameter(navDeepLinkRequest, "navDeepLinkRequest");
        if (this.deepLinks.isEmpty()) {
            return null;
        }
        DeepLinkMatch bestMatch = null;
        for (NavDeepLink deepLink : this.deepLinks) {
            Uri uri = navDeepLinkRequest.getUri();
            Bundle matchingArguments = uri != null ? deepLink.getMatchingArguments(uri, getArguments()) : null;
            String requestAction = navDeepLinkRequest.getAction();
            boolean matchingAction = requestAction != null && Intrinsics.areEqual(requestAction, deepLink.getAction());
            String mimeType = navDeepLinkRequest.getMimeType();
            int mimeTypeMatchLevel = mimeType != null ? deepLink.getMimeTypeMatchRating(mimeType) : -1;
            if (matchingArguments != null || matchingAction || mimeTypeMatchLevel > -1) {
                DeepLinkMatch newMatch = new DeepLinkMatch(this, matchingArguments, deepLink.getIsExactDeepLink(), matchingAction, mimeTypeMatchLevel);
                if (bestMatch == null || newMatch.compareTo(bestMatch) > 0) {
                    bestMatch = newMatch;
                }
            }
        }
        return bestMatch;
    }

    public static /* synthetic */ int[] buildDeepLinkIds$default(NavDestination navDestination, NavDestination navDestination2, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: buildDeepLinkIds");
        }
        if ((i & 1) != 0) {
            navDestination2 = null;
        }
        return navDestination.buildDeepLinkIds(navDestination2);
    }

    public final int[] buildDeepLinkIds(NavDestination previousDestination) {
        ArrayDeque hierarchy = new ArrayDeque();
        NavGraph current = this;
        while (true) {
            Intrinsics.checkNotNull(current);
            NavGraph parent = current.parent;
            if ((previousDestination != null ? previousDestination.parent : null) != null) {
                NavGraph navGraph = previousDestination.parent;
                Intrinsics.checkNotNull(navGraph);
                if (navGraph.findNode(current.id) == current) {
                    hierarchy.addFirst(current);
                    break;
                }
            }
            if (parent == null || parent.getStartDestId() != current.id) {
                hierarchy.addFirst(current);
            }
            if (Intrinsics.areEqual(parent, previousDestination) || (current = parent) == null) {
                break;
            }
        }
        Iterable $this$map$iv = CollectionsKt.toList(hierarchy);
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            NavDestination it = (NavDestination) item$iv$iv;
            destination$iv$iv.add(Integer.valueOf(it.id));
        }
        return CollectionsKt.toIntArray((List) destination$iv$iv);
    }

    public boolean supportsActions() {
        return true;
    }

    public final NavAction getAction(int id) {
        NavAction destination = this.actions.isEmpty() ? null : this.actions.get(id);
        if (destination != null) {
            return destination;
        }
        NavGraph $this$getAction_u24lambda_u2d7 = this.parent;
        if ($this$getAction_u24lambda_u2d7 != null) {
            return $this$getAction_u24lambda_u2d7.getAction(id);
        }
        return null;
    }

    public final void putAction(int actionId, int destId) {
        putAction(actionId, new NavAction(destId, null, null, 6, null));
    }

    public final void putAction(int actionId, NavAction action) {
        Intrinsics.checkNotNullParameter(action, "action");
        if (!supportsActions()) {
            throw new UnsupportedOperationException("Cannot add action " + actionId + " to " + this + " as it does not support actions, indicating that it is a terminal destination in your navigation graph and will never trigger actions.");
        }
        if (!(actionId != 0)) {
            throw new IllegalArgumentException("Cannot have an action with actionId 0".toString());
        }
        this.actions.put(actionId, action);
    }

    public final void removeAction(int actionId) {
        this.actions.remove(actionId);
    }

    public final void addArgument(String argumentName, NavArgument argument) {
        Intrinsics.checkNotNullParameter(argumentName, "argumentName");
        Intrinsics.checkNotNullParameter(argument, "argument");
        this._arguments.put(argumentName, argument);
    }

    public final void removeArgument(String argumentName) {
        Intrinsics.checkNotNullParameter(argumentName, "argumentName");
        this._arguments.remove(argumentName);
    }

    public final Bundle addInDefaultArgs(Bundle args) {
        if (args == null) {
            Map<String, NavArgument> map = this._arguments;
            if (map == null || map.isEmpty()) {
                return null;
            }
        }
        Bundle defaultArgs = new Bundle();
        for (Map.Entry<String, NavArgument> entry : this._arguments.entrySet()) {
            entry.getValue().putDefaultValue(entry.getKey(), defaultArgs);
        }
        if (args != null) {
            defaultArgs.putAll(args);
            for (Map.Entry<String, NavArgument> entry2 : this._arguments.entrySet()) {
                String key = entry2.getKey();
                NavArgument value = entry2.getValue();
                if (!value.verify(key, defaultArgs)) {
                    throw new IllegalArgumentException(("Wrong argument type for '" + key + "' in argument bundle. " + value.getType().getName() + " expected.").toString());
                }
            }
        }
        return defaultArgs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(");
        String str = this.idName;
        if (str == null) {
            sb.append("0x");
            sb.append(Integer.toHexString(this.id));
        } else {
            sb.append(str);
        }
        sb.append(")");
        String str2 = this.route;
        if (!(str2 == null || StringsKt.isBlank(str2))) {
            sb.append(" route=");
            sb.append(this.route);
        }
        if (this.label != null) {
            sb.append(" label=");
            sb.append(this.label);
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:69:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean equals(Object other) {
        boolean equalActions;
        boolean equalArguments;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (other == null || !(other instanceof NavDestination)) {
            return false;
        }
        boolean equalDeepLinks = CollectionsKt.intersect(this.deepLinks, ((NavDestination) other).deepLinks).size() == this.deepLinks.size();
        if (this.actions.size() == ((NavDestination) other).actions.size()) {
            Sequence $this$all$iv = SequencesKt.asSequence(SparseArrayKt.valueIterator(this.actions));
            Iterator it = $this$all$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z3 = true;
                    break;
                }
                Object element$iv = it.next();
                if (!((NavDestination) other).actions.containsValue((NavAction) element$iv)) {
                    z3 = false;
                    break;
                }
            }
            if (z3) {
                Sequence $this$all$iv2 = SequencesKt.asSequence(SparseArrayKt.valueIterator(((NavDestination) other).actions));
                Iterator it2 = $this$all$iv2.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        z4 = true;
                        break;
                    }
                    Object element$iv2 = it2.next();
                    if (!this.actions.containsValue((NavAction) element$iv2)) {
                        z4 = false;
                        break;
                    }
                }
                if (z4) {
                    equalActions = true;
                    if (getArguments().size() == ((NavDestination) other).getArguments().size()) {
                        Sequence $this$all$iv3 = MapsKt.asSequence(getArguments());
                        Iterator it3 = $this$all$iv3.iterator();
                        while (true) {
                            if (it3.hasNext()) {
                                Object element$iv3 = it3.next();
                                Map.Entry it4 = (Map.Entry) element$iv3;
                                if (!(((NavDestination) other).getArguments().containsKey(it4.getKey()) && Intrinsics.areEqual(((NavDestination) other).getArguments().get(it4.getKey()), it4.getValue()))) {
                                    z = false;
                                    break;
                                }
                            } else {
                                z = true;
                                break;
                            }
                        }
                        if (z) {
                            Sequence $this$all$iv4 = MapsKt.asSequence(((NavDestination) other).getArguments());
                            Iterator it5 = $this$all$iv4.iterator();
                            while (true) {
                                if (it5.hasNext()) {
                                    Object element$iv4 = it5.next();
                                    Map.Entry it6 = (Map.Entry) element$iv4;
                                    if (!(getArguments().containsKey(it6.getKey()) && Intrinsics.areEqual(getArguments().get(it6.getKey()), it6.getValue()))) {
                                        z2 = false;
                                        break;
                                    }
                                } else {
                                    z2 = true;
                                    break;
                                }
                            }
                            if (z2) {
                                equalArguments = true;
                                return this.id == ((NavDestination) other).id ? false : false;
                            }
                        }
                    }
                    equalArguments = false;
                    return this.id == ((NavDestination) other).id ? false : false;
                }
            }
        }
        equalActions = false;
        if (getArguments().size() == ((NavDestination) other).getArguments().size()) {
        }
        equalArguments = false;
        if (this.id == ((NavDestination) other).id) {
        }
    }

    public int hashCode() {
        Iterable $this$forEach$iv;
        int result = this.id;
        int i = result * 31;
        String str = this.route;
        int result2 = i + (str != null ? str.hashCode() : 0);
        for (Object element$iv : this.deepLinks) {
            NavDeepLink it = (NavDeepLink) element$iv;
            int i2 = result2 * 31;
            String uriPattern = it.getUriPattern();
            int result3 = i2 + (uriPattern != null ? uriPattern.hashCode() : 0);
            int result4 = result3 * 31;
            String action = it.getAction();
            int result5 = (result4 + (action != null ? action.hashCode() : 0)) * 31;
            String mimeType = it.getMimeType();
            result2 = result5 + (mimeType != null ? mimeType.hashCode() : 0);
        }
        Iterator $this$forEach$iv2 = SparseArrayKt.valueIterator(this.actions);
        while ($this$forEach$iv2.hasNext()) {
            Object element$iv2 = $this$forEach$iv2.next();
            NavAction value = (NavAction) element$iv2;
            int result6 = ((result2 * 31) + value.getDestinationId()) * 31;
            NavOptions navOptions = value.getNavOptions();
            result2 = result6 + (navOptions != null ? navOptions.hashCode() : 0);
            Bundle defaultArguments = value.getDefaultArguments();
            if (defaultArguments != null && ($this$forEach$iv = defaultArguments.keySet()) != null) {
                Intrinsics.checkNotNullExpressionValue($this$forEach$iv, "keySet()");
                for (Object element$iv3 : $this$forEach$iv) {
                    int i3 = result2 * 31;
                    Bundle defaultArguments2 = value.getDefaultArguments();
                    Intrinsics.checkNotNull(defaultArguments2);
                    Object obj = defaultArguments2.get((String) element$iv3);
                    result2 = i3 + (obj != null ? obj.hashCode() : 0);
                }
            }
        }
        for (Object element$iv4 : getArguments().keySet()) {
            String it2 = (String) element$iv4;
            int result7 = ((result2 * 31) + it2.hashCode()) * 31;
            NavArgument navArgument = getArguments().get(it2);
            result2 = result7 + (navArgument != null ? navArgument.hashCode() : 0);
        }
        return result2;
    }

    /* compiled from: NavDestination.kt */
    @Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u0007J\u0018\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0007J:\u0010\u0015\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00160\u0006\"\u0004\b\u0000\u0010\u00162\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00052\u0010\u0010\u0018\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00160\u0006H\u0005J:\u0010\u0019\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00160\u0006\"\u0004\b\u0000\u0010\u00162\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00052\u0010\u0010\u0018\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00160\u0006H\u0007R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u0005\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b*\u00020\t8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r¨\u0006\u001a"}, d2 = {"Landroidx/navigation/NavDestination$Companion;", "", "()V", "classes", "", "", "Ljava/lang/Class;", "hierarchy", "Lkotlin/sequences/Sequence;", "Landroidx/navigation/NavDestination;", "getHierarchy$annotations", "(Landroidx/navigation/NavDestination;)V", "getHierarchy", "(Landroidx/navigation/NavDestination;)Lkotlin/sequences/Sequence;", "createRoute", "route", "getDisplayName", "context", "Landroid/content/Context;", "id", "", "parseClassFromName", "C", "name", "expectedClassType", "parseClassFromNameInternal", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public static /* synthetic */ void getHierarchy$annotations(NavDestination navDestination) {
        }

        private Companion() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @JvmStatic
        protected final <C> Class<? extends C> parseClassFromName(Context context, String name, Class<? extends C> expectedClassType) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(expectedClassType, "expectedClassType");
            String str = name;
            if (str.charAt(0) == '.') {
                str = context.getPackageName() + str;
            }
            Class clazz = (Class) NavDestination.classes.get(str);
            if (clazz == null) {
                try {
                    clazz = Class.forName(str, true, context.getClassLoader());
                    NavDestination.classes.put(name, clazz);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            Intrinsics.checkNotNull(clazz);
            if (!expectedClassType.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException((str + " must be a subclass of " + expectedClassType).toString());
            }
            return clazz;
        }

        @JvmStatic
        public final <C> Class<? extends C> parseClassFromNameInternal(Context context, String name, Class<? extends C> expectedClassType) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(expectedClassType, "expectedClassType");
            return NavDestination.parseClassFromName(context, name, expectedClassType);
        }

        @JvmStatic
        public final String getDisplayName(Context context, int id) {
            String valueOf;
            Intrinsics.checkNotNullParameter(context, "context");
            if (id <= 16777215) {
                return String.valueOf(id);
            }
            try {
                valueOf = context.getResources().getResourceName(id);
            } catch (Resources.NotFoundException e) {
                valueOf = String.valueOf(id);
            }
            Intrinsics.checkNotNullExpressionValue(valueOf, "try {\n                co….toString()\n            }");
            return valueOf;
        }

        public final String createRoute(String route) {
            return route != null ? "android-app://androidx.navigation/" + route : "";
        }

        public final Sequence<NavDestination> getHierarchy(NavDestination $this$hierarchy) {
            Intrinsics.checkNotNullParameter($this$hierarchy, "<this>");
            return SequencesKt.generateSequence($this$hierarchy, new Function1<NavDestination, NavDestination>() { // from class: androidx.navigation.NavDestination$Companion$hierarchy$1
                @Override // kotlin.jvm.functions.Function1
                public final NavDestination invoke(NavDestination it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    return it.getParent();
                }
            });
        }
    }
}
