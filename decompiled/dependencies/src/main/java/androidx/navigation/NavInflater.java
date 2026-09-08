package androidx.navigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavArgument;
import androidx.navigation.NavDeepLink;
import androidx.navigation.NavOptions;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: NavInflater.kt */
@Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J(\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0012\u0010\u0007\u001a\u00020\u00112\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u0007J0\u0010\u0012\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J \u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J(\u0010\u0019\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J(\u0010\u001c\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J \u0010\u001d\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Landroidx/navigation/NavInflater;", "", "context", "Landroid/content/Context;", "navigatorProvider", "Landroidx/navigation/NavigatorProvider;", "(Landroid/content/Context;Landroidx/navigation/NavigatorProvider;)V", "inflate", "Landroidx/navigation/NavDestination;", "res", "Landroid/content/res/Resources;", "parser", "Landroid/content/res/XmlResourceParser;", "attrs", "Landroid/util/AttributeSet;", "graphResId", "", "Landroidx/navigation/NavGraph;", "inflateAction", "", "dest", "inflateArgument", "Landroidx/navigation/NavArgument;", "a", "Landroid/content/res/TypedArray;", "inflateArgumentForBundle", "bundle", "Landroid/os/Bundle;", "inflateArgumentForDestination", "inflateDeepLink", "Companion", "navigation-runtime_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class NavInflater {
    public static final String APPLICATION_ID_PLACEHOLDER = "${applicationId}";
    private static final String TAG_ACTION = "action";
    private static final String TAG_ARGUMENT = "argument";
    private static final String TAG_DEEP_LINK = "deepLink";
    private static final String TAG_INCLUDE = "include";
    private final Context context;
    private final NavigatorProvider navigatorProvider;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final ThreadLocal<TypedValue> sTmpValue = new ThreadLocal<>();

    public NavInflater(Context context, NavigatorProvider navigatorProvider) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(navigatorProvider, "navigatorProvider");
        this.context = context;
        this.navigatorProvider = navigatorProvider;
    }

    public final NavGraph inflate(int graphResId) {
        int it;
        Resources res = this.context.getResources();
        XmlResourceParser parser = res.getXml(graphResId);
        Intrinsics.checkNotNullExpressionValue(parser, "res.getXml(graphResId)");
        AttributeSet attrs = Xml.asAttributeSet(parser);
        do {
            try {
                try {
                    it = parser.next();
                    if (it == 2) {
                        break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Exception inflating " + res.getResourceName(graphResId) + " line " + parser.getLineNumber(), e);
                }
            } finally {
                parser.close();
            }
        } while (it != 1);
        if (it != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        String rootElement = parser.getName();
        Intrinsics.checkNotNullExpressionValue(res, "res");
        Intrinsics.checkNotNullExpressionValue(attrs, "attrs");
        NavDestination destination = inflate(res, parser, attrs, graphResId);
        if (destination instanceof NavGraph) {
            return (NavGraph) destination;
        }
        throw new IllegalArgumentException(("Root element <" + rootElement + "> did not inflate into a NavGraph").toString());
    }

    private final NavDestination inflate(Resources res, XmlResourceParser parser, AttributeSet attrs, int graphResId) throws XmlPullParserException, IOException {
        int it;
        NavigatorProvider navigatorProvider = this.navigatorProvider;
        String name = parser.getName();
        Intrinsics.checkNotNullExpressionValue(name, "parser.name");
        Navigator navigator = navigatorProvider.getNavigator(name);
        NavDestination dest = navigator.createDestination();
        dest.onInflate(this.context, attrs);
        int i = 1;
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int it2 = parser.next();
            if (it2 == i || ((it = parser.getDepth()) < innerDepth && it2 == 3)) {
                break;
            }
            if (it2 == 2 && it <= innerDepth) {
                String name2 = parser.getName();
                if (Intrinsics.areEqual(TAG_ARGUMENT, name2)) {
                    inflateArgumentForDestination(res, dest, attrs, graphResId);
                } else if (Intrinsics.areEqual(TAG_DEEP_LINK, name2)) {
                    inflateDeepLink(res, dest, attrs);
                } else if (Intrinsics.areEqual(TAG_ACTION, name2)) {
                    inflateAction(res, dest, attrs, parser, graphResId);
                } else if (Intrinsics.areEqual(TAG_INCLUDE, name2) && (dest instanceof NavGraph)) {
                    TypedArray $this$use$iv = res.obtainAttributes(attrs, R.styleable.NavInclude);
                    Intrinsics.checkNotNullExpressionValue($this$use$iv, "res.obtainAttributes(att…n.R.styleable.NavInclude)");
                    int id = $this$use$iv.getResourceId(R.styleable.NavInclude_graph, 0);
                    ((NavGraph) dest).addDestination(inflate(id));
                    Unit unit = Unit.INSTANCE;
                    $this$use$iv.recycle();
                } else if (dest instanceof NavGraph) {
                    ((NavGraph) dest).addDestination(inflate(res, parser, attrs, graphResId));
                }
            }
            i = 1;
        }
        return dest;
    }

    private final void inflateArgumentForDestination(Resources res, NavDestination dest, AttributeSet attrs, int graphResId) throws XmlPullParserException {
        TypedArray $this$use$iv = res.obtainAttributes(attrs, androidx.navigation.common.R.styleable.NavArgument);
        Intrinsics.checkNotNullExpressionValue($this$use$iv, "res.obtainAttributes(att… R.styleable.NavArgument)");
        String name = $this$use$iv.getString(androidx.navigation.common.R.styleable.NavArgument_android_name);
        if (name == null) {
            throw new XmlPullParserException("Arguments must have a name");
        }
        Intrinsics.checkNotNullExpressionValue(name, "array.getString(R.stylea…uments must have a name\")");
        NavArgument argument = inflateArgument($this$use$iv, res, graphResId);
        dest.addArgument(name, argument);
        Unit unit = Unit.INSTANCE;
        $this$use$iv.recycle();
    }

    private final void inflateArgumentForBundle(Resources res, Bundle bundle, AttributeSet attrs, int graphResId) throws XmlPullParserException {
        TypedArray $this$use$iv = res.obtainAttributes(attrs, androidx.navigation.common.R.styleable.NavArgument);
        Intrinsics.checkNotNullExpressionValue($this$use$iv, "res.obtainAttributes(att… R.styleable.NavArgument)");
        String name = $this$use$iv.getString(androidx.navigation.common.R.styleable.NavArgument_android_name);
        if (name == null) {
            throw new XmlPullParserException("Arguments must have a name");
        }
        Intrinsics.checkNotNullExpressionValue(name, "array.getString(R.stylea…uments must have a name\")");
        NavArgument argument = inflateArgument($this$use$iv, res, graphResId);
        if (argument.getIsDefaultValuePresent()) {
            argument.putDefaultValue(name, bundle);
        }
        Unit unit = Unit.INSTANCE;
        $this$use$iv.recycle();
    }

    private final NavArgument inflateArgument(TypedArray a, Resources res, int graphResId) throws XmlPullParserException {
        NavType<?> navType;
        NavArgument.Builder builder = new NavArgument.Builder();
        int i = 0;
        builder.setIsNullable(a.getBoolean(androidx.navigation.common.R.styleable.NavArgument_nullable, false));
        ThreadLocal<TypedValue> threadLocal = sTmpValue;
        TypedValue typedValue = threadLocal.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            threadLocal.set(typedValue);
        }
        Object obj = null;
        String string = a.getString(androidx.navigation.common.R.styleable.NavArgument_argType);
        if (string == null) {
            navType = null;
        } else {
            navType = NavType.INSTANCE.fromArgType(string, res.getResourcePackageName(graphResId));
        }
        if (a.getValue(androidx.navigation.common.R.styleable.NavArgument_android_defaultValue, typedValue)) {
            if (navType == NavType.ReferenceType) {
                if (typedValue.resourceId != 0) {
                    i = typedValue.resourceId;
                } else if (typedValue.type != 16 || typedValue.data != 0) {
                    throw new XmlPullParserException("unsupported value '" + ((Object) typedValue.string) + "' for " + navType.getName() + ". Must be a reference to a resource.");
                }
                obj = Integer.valueOf(i);
            } else if (typedValue.resourceId != 0) {
                if (navType == null) {
                    navType = NavType.ReferenceType;
                    obj = Integer.valueOf(typedValue.resourceId);
                } else {
                    throw new XmlPullParserException("unsupported value '" + ((Object) typedValue.string) + "' for " + navType.getName() + ". You must use a \"" + NavType.ReferenceType.getName() + "\" type to reference other resources.");
                }
            } else if (navType == NavType.StringType) {
                obj = a.getString(androidx.navigation.common.R.styleable.NavArgument_android_defaultValue);
            } else {
                switch (typedValue.type) {
                    case 3:
                        String obj2 = typedValue.string.toString();
                        if (navType == null) {
                            navType = NavType.INSTANCE.inferFromValue(obj2);
                        }
                        obj = navType.parseValue(obj2);
                        break;
                    case 4:
                        navType = INSTANCE.checkNavType$navigation_runtime_release(typedValue, navType, NavType.FloatType, string, TypedValues.Custom.S_FLOAT);
                        obj = Float.valueOf(typedValue.getFloat());
                        break;
                    case 5:
                        navType = INSTANCE.checkNavType$navigation_runtime_release(typedValue, navType, NavType.IntType, string, TypedValues.Custom.S_DIMENSION);
                        obj = Integer.valueOf((int) typedValue.getDimension(res.getDisplayMetrics()));
                        break;
                    case 18:
                        navType = INSTANCE.checkNavType$navigation_runtime_release(typedValue, navType, NavType.BoolType, string, TypedValues.Custom.S_BOOLEAN);
                        obj = Boolean.valueOf(typedValue.data != 0);
                        break;
                    default:
                        if (typedValue.type >= 16 && typedValue.type <= 31) {
                            if (navType == NavType.FloatType) {
                                navType = INSTANCE.checkNavType$navigation_runtime_release(typedValue, navType, NavType.FloatType, string, TypedValues.Custom.S_FLOAT);
                                obj = Float.valueOf(typedValue.data);
                                break;
                            } else {
                                navType = INSTANCE.checkNavType$navigation_runtime_release(typedValue, navType, NavType.IntType, string, TypedValues.Custom.S_INT);
                                obj = Integer.valueOf(typedValue.data);
                                break;
                            }
                        } else {
                            throw new XmlPullParserException("unsupported argument type " + typedValue.type);
                        }
                }
            }
        }
        if (obj != null) {
            builder.setDefaultValue(obj);
        }
        if (navType != null) {
            builder.setType(navType);
        }
        return builder.build();
    }

    private final void inflateDeepLink(Resources res, NavDestination dest, AttributeSet attrs) throws XmlPullParserException {
        String str;
        NavDeepLink.Builder builder;
        String str2;
        TypedArray $this$use$iv = res.obtainAttributes(attrs, androidx.navigation.common.R.styleable.NavDeepLink);
        Intrinsics.checkNotNullExpressionValue($this$use$iv, "res.obtainAttributes(att… R.styleable.NavDeepLink)");
        String uri = $this$use$iv.getString(androidx.navigation.common.R.styleable.NavDeepLink_uri);
        String action = $this$use$iv.getString(androidx.navigation.common.R.styleable.NavDeepLink_action);
        String mimeType = $this$use$iv.getString(androidx.navigation.common.R.styleable.NavDeepLink_mimeType);
        String str3 = uri;
        boolean z = true;
        if (str3 == null || str3.length() == 0) {
            String str4 = action;
            if (str4 == null || str4.length() == 0) {
                String str5 = mimeType;
                if (str5 == null || str5.length() == 0) {
                    throw new XmlPullParserException("Every <deepLink> must include at least one of app:uri, app:action, or app:mimeType");
                }
            }
        }
        NavDeepLink.Builder builder2 = new NavDeepLink.Builder();
        if (uri != null) {
            String packageName = this.context.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName, "context.packageName");
            str = "context.packageName";
            builder = builder2;
            builder.setUriPattern(StringsKt.replace$default(uri, APPLICATION_ID_PLACEHOLDER, packageName, false, 4, (Object) null));
        } else {
            str = "context.packageName";
            builder = builder2;
        }
        String str6 = action;
        if (str6 != null && str6.length() != 0) {
            z = false;
        }
        if (!z) {
            String packageName2 = this.context.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName2, str);
            str2 = str;
            builder.setAction(StringsKt.replace$default(action, APPLICATION_ID_PLACEHOLDER, packageName2, false, 4, (Object) null));
        } else {
            str2 = str;
        }
        if (mimeType != null) {
            String packageName3 = this.context.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName3, str2);
            builder.setMimeType(StringsKt.replace$default(mimeType, APPLICATION_ID_PLACEHOLDER, packageName3, false, 4, (Object) null));
        }
        dest.addDeepLink(builder.build());
        Unit unit = Unit.INSTANCE;
        $this$use$iv.recycle();
    }

    private final void inflateAction(Resources res, NavDestination dest, AttributeSet attrs, XmlResourceParser parser, int graphResId) throws IOException, XmlPullParserException {
        int it;
        int depth;
        int innerDepth;
        Context $this$withStyledAttributes$iv = this.context;
        int[] NavAction = androidx.navigation.common.R.styleable.NavAction;
        Intrinsics.checkNotNullExpressionValue(NavAction, "NavAction");
        int defStyleAttr$iv = 0;
        TypedArray $this$inflateAction_u24lambda_u2d10 = $this$withStyledAttributes$iv.obtainStyledAttributes(attrs, NavAction, 0, 0);
        int id = $this$inflateAction_u24lambda_u2d10.getResourceId(androidx.navigation.common.R.styleable.NavAction_android_id, 0);
        int destId = $this$inflateAction_u24lambda_u2d10.getResourceId(androidx.navigation.common.R.styleable.NavAction_destination, 0);
        NavAction action = new NavAction(destId, null, null, 6, null);
        NavOptions.Builder builder = new NavOptions.Builder();
        builder.setLaunchSingleTop($this$inflateAction_u24lambda_u2d10.getBoolean(androidx.navigation.common.R.styleable.NavAction_launchSingleTop, false));
        builder.setRestoreState($this$inflateAction_u24lambda_u2d10.getBoolean(androidx.navigation.common.R.styleable.NavAction_restoreState, false));
        builder.setPopUpTo($this$inflateAction_u24lambda_u2d10.getResourceId(androidx.navigation.common.R.styleable.NavAction_popUpTo, -1), $this$inflateAction_u24lambda_u2d10.getBoolean(androidx.navigation.common.R.styleable.NavAction_popUpToInclusive, false), $this$inflateAction_u24lambda_u2d10.getBoolean(androidx.navigation.common.R.styleable.NavAction_popUpToSaveState, false));
        builder.setEnterAnim($this$inflateAction_u24lambda_u2d10.getResourceId(androidx.navigation.common.R.styleable.NavAction_enterAnim, -1));
        builder.setExitAnim($this$inflateAction_u24lambda_u2d10.getResourceId(androidx.navigation.common.R.styleable.NavAction_exitAnim, -1));
        builder.setPopEnterAnim($this$inflateAction_u24lambda_u2d10.getResourceId(androidx.navigation.common.R.styleable.NavAction_popEnterAnim, -1));
        builder.setPopExitAnim($this$inflateAction_u24lambda_u2d10.getResourceId(androidx.navigation.common.R.styleable.NavAction_popExitAnim, -1));
        action.setNavOptions(builder.build());
        Bundle args = new Bundle();
        int innerDepth2 = parser.getDepth() + 1;
        while (true) {
            int it2 = parser.next();
            int it3 = defStyleAttr$iv;
            if (it2 != 1 && ((it = parser.getDepth()) >= innerDepth2 || it2 != 3)) {
                if (it2 != 2) {
                    innerDepth = innerDepth2;
                    depth = it;
                } else {
                    depth = it;
                    if (depth > innerDepth2) {
                        innerDepth = innerDepth2;
                    } else {
                        String name = parser.getName();
                        innerDepth = innerDepth2;
                        if (Intrinsics.areEqual(TAG_ARGUMENT, name)) {
                            inflateArgumentForBundle(res, args, attrs, graphResId);
                        }
                    }
                }
                defStyleAttr$iv = it3;
                innerDepth2 = innerDepth;
            }
        }
        if (!args.isEmpty()) {
            action.setDefaultArguments(args);
        }
        dest.putAction(id, action);
        $this$inflateAction_u24lambda_u2d10.recycle();
    }

    /* compiled from: NavInflater.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JE\u0010\f\u001a\u0006\u0012\u0002\b\u00030\r2\u0006\u0010\u000e\u001a\u00020\u000b2\f\u0010\u000f\u001a\b\u0012\u0002\b\u0003\u0018\u00010\r2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\r2\b\u0010\u0011\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0012\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0013R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Landroidx/navigation/NavInflater$Companion;", "", "()V", "APPLICATION_ID_PLACEHOLDER", "", "TAG_ACTION", "TAG_ARGUMENT", "TAG_DEEP_LINK", "TAG_INCLUDE", "sTmpValue", "Ljava/lang/ThreadLocal;", "Landroid/util/TypedValue;", "checkNavType", "Landroidx/navigation/NavType;", "value", "navType", "expectedNavType", "argType", "foundType", "checkNavType$navigation_runtime_release", "navigation-runtime_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final NavType<?> checkNavType$navigation_runtime_release(TypedValue value, NavType<?> navType, NavType<?> expectedNavType, String argType, String foundType) throws XmlPullParserException {
            Intrinsics.checkNotNullParameter(value, "value");
            Intrinsics.checkNotNullParameter(expectedNavType, "expectedNavType");
            Intrinsics.checkNotNullParameter(foundType, "foundType");
            if (navType == null || navType == expectedNavType) {
                return navType == null ? expectedNavType : navType;
            }
            throw new XmlPullParserException("Type is " + argType + " but found " + foundType + ": " + value.data);
        }
    }
}
