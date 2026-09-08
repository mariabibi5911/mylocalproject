package androidx.navigation;

import android.net.Uri;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* compiled from: NavDeepLink.kt */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010!\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 F2\u00020\u0001:\u0004EFGHB\u000f\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B%\b\u0000\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\bJ$\u0010)\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\n\u0010*\u001a\u00060+j\u0002`,2\u0006\u0010-\u001a\u00020\u001cH\u0002J\u0013\u0010.\u001a\u00020\u00122\b\u0010/\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J(\u00100\u001a\u0004\u0018\u0001012\u0006\u00102\u001a\u0002032\u0014\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010504H\u0007J\u0010\u00106\u001a\u0002072\u0006\u0010\u0007\u001a\u00020\u0003H\u0007J\b\u00108\u001a\u000207H\u0016J\u0012\u00109\u001a\u00020\u00122\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003H\u0002J\u0012\u0010:\u001a\u00020\u00122\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0002J\u0012\u0010;\u001a\u00020\u00122\b\u0010\u0002\u001a\u0004\u0018\u000103H\u0002J\u0015\u0010<\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u000203H\u0000¢\u0006\u0002\b=J\u0015\u0010<\u001a\u00020\u00122\u0006\u0010>\u001a\u00020?H\u0000¢\u0006\u0002\b=J*\u0010@\u001a\u00020\u00122\u0006\u0010A\u001a\u0002012\u0006\u0010B\u001a\u00020\u00032\u0006\u0010C\u001a\u00020\u00032\b\u0010D\u001a\u0004\u0018\u000105H\u0002R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R&\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u00128G@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\nR\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001d\u0010\u001b\u001a\u0004\u0018\u00010\u001c8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001f\u0010 \u001a\u0004\b\u001d\u0010\u001eR\u001a\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020#0\"X\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010$\u001a\u0004\u0018\u00010\u001c8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b&\u0010 \u001a\u0004\b%\u0010\u001eR\u0010\u0010'\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\n¨\u0006I"}, d2 = {"Landroidx/navigation/NavDeepLink;", "", "uri", "", "(Ljava/lang/String;)V", "uriPattern", "action", "mimeType", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAction", "()Ljava/lang/String;", "arguments", "", "argumentsNames", "", "getArgumentsNames$navigation_common_release", "()Ljava/util/List;", "<set-?>", "", "isExactDeepLink", "()Z", "setExactDeepLink$navigation_common_release", "(Z)V", "isParameterizedQuery", "isSingleQueryParamValueOnly", "getMimeType", "mimeTypeFinalRegex", "mimeTypePattern", "Ljava/util/regex/Pattern;", "getMimeTypePattern", "()Ljava/util/regex/Pattern;", "mimeTypePattern$delegate", "Lkotlin/Lazy;", "paramArgMap", "", "Landroidx/navigation/NavDeepLink$ParamQuery;", "pattern", "getPattern", "pattern$delegate", "patternFinalRegex", "getUriPattern", "buildPathRegex", "uriRegex", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "fillInPattern", "equals", "other", "getMatchingArguments", "Landroid/os/Bundle;", "deepLink", "Landroid/net/Uri;", "", "Landroidx/navigation/NavArgument;", "getMimeTypeMatchRating", "", "hashCode", "matchAction", "matchMimeType", "matchUri", "matches", "matches$navigation_common_release", "deepLinkRequest", "Landroidx/navigation/NavDeepLinkRequest;", "parseArgument", "bundle", "name", "value", "argument", "Builder", "Companion", "MimeType", "ParamQuery", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class NavDeepLink {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    private static final Pattern SCHEME_PATTERN = Pattern.compile("^[a-zA-Z]+[+\\w\\-.]*:");
    private final String action;
    private final List<String> arguments;
    private boolean isExactDeepLink;
    private boolean isParameterizedQuery;
    private boolean isSingleQueryParamValueOnly;
    private final String mimeType;
    private String mimeTypeFinalRegex;

    /* renamed from: mimeTypePattern$delegate, reason: from kotlin metadata */
    private final Lazy mimeTypePattern;
    private final Map<String, ParamQuery> paramArgMap;

    /* renamed from: pattern$delegate, reason: from kotlin metadata */
    private final Lazy pattern;
    private String patternFinalRegex;
    private final String uriPattern;

    public NavDeepLink(String uriPattern, String action, String mimeType) {
        this.uriPattern = uriPattern;
        this.action = action;
        this.mimeType = mimeType;
        this.arguments = new ArrayList();
        this.paramArgMap = new LinkedHashMap();
        this.pattern = LazyKt.lazy(new Function0<Pattern>() { // from class: androidx.navigation.NavDeepLink$pattern$2
            /* JADX INFO: Access modifiers changed from: package-private */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final Pattern invoke() {
                String it;
                it = NavDeepLink.this.patternFinalRegex;
                if (it != null) {
                    return Pattern.compile(it, 2);
                }
                return null;
            }
        });
        this.mimeTypePattern = LazyKt.lazy(new Function0<Pattern>() { // from class: androidx.navigation.NavDeepLink$mimeTypePattern$2
            /* JADX INFO: Access modifiers changed from: package-private */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final Pattern invoke() {
                String it;
                it = NavDeepLink.this.mimeTypeFinalRegex;
                if (it != null) {
                    return Pattern.compile(it);
                }
                return null;
            }
        });
        if (uriPattern != null) {
            Uri parameterizedUri = Uri.parse(uriPattern);
            boolean z = true;
            this.isParameterizedQuery = parameterizedUri.getQuery() != null;
            StringBuilder uriRegex = new StringBuilder("^");
            if (!SCHEME_PATTERN.matcher(uriPattern).find()) {
                uriRegex.append("http[s]?://");
            }
            Pattern fillInPattern = Pattern.compile("\\{(.+?)\\}");
            if (this.isParameterizedQuery) {
                Matcher matcher = Pattern.compile("(\\?)").matcher(uriPattern);
                if (matcher.find()) {
                    String substring = uriPattern.substring(0, matcher.start());
                    Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                    Intrinsics.checkNotNullExpressionValue(fillInPattern, "fillInPattern");
                    this.isExactDeepLink = buildPathRegex(substring, uriRegex, fillInPattern);
                }
                for (String paramName : parameterizedUri.getQueryParameterNames()) {
                    StringBuilder argRegex = new StringBuilder();
                    String queryParam = parameterizedUri.getQueryParameter(paramName);
                    if (queryParam == null) {
                        queryParam = paramName;
                        this.isSingleQueryParamValueOnly = z;
                    }
                    Matcher matcher2 = fillInPattern.matcher(queryParam);
                    int appendPos = 0;
                    ParamQuery param = new ParamQuery();
                    while (matcher2.find()) {
                        String group = matcher2.group(1);
                        if (group == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
                        }
                        param.addArgumentName(group);
                        Intrinsics.checkNotNullExpressionValue(queryParam, "queryParam");
                        String substring2 = queryParam.substring(appendPos, matcher2.start());
                        Intrinsics.checkNotNullExpressionValue(substring2, "this as java.lang.String…ing(startIndex, endIndex)");
                        argRegex.append(Pattern.quote(substring2));
                        argRegex.append("(.+?)?");
                        appendPos = matcher2.end();
                    }
                    if (appendPos < queryParam.length()) {
                        Intrinsics.checkNotNullExpressionValue(queryParam, "queryParam");
                        String substring3 = queryParam.substring(appendPos);
                        Intrinsics.checkNotNullExpressionValue(substring3, "this as java.lang.String).substring(startIndex)");
                        argRegex.append(Pattern.quote(substring3));
                    }
                    String sb = argRegex.toString();
                    Intrinsics.checkNotNullExpressionValue(sb, "argRegex.toString()");
                    param.setParamRegex(StringsKt.replace$default(sb, ".*", "\\E.*\\Q", false, 4, (Object) null));
                    Map<String, ParamQuery> map = this.paramArgMap;
                    Intrinsics.checkNotNullExpressionValue(paramName, "paramName");
                    map.put(paramName, param);
                    z = true;
                }
            } else {
                Intrinsics.checkNotNullExpressionValue(fillInPattern, "fillInPattern");
                this.isExactDeepLink = buildPathRegex(uriPattern, uriRegex, fillInPattern);
            }
            String sb2 = uriRegex.toString();
            Intrinsics.checkNotNullExpressionValue(sb2, "uriRegex.toString()");
            this.patternFinalRegex = StringsKt.replace$default(sb2, ".*", "\\E.*\\Q", false, 4, (Object) null);
        }
        if (this.mimeType == null) {
            return;
        }
        Pattern mimeTypePattern = Pattern.compile("^[\\s\\S]+/[\\s\\S]+$");
        Matcher mimeTypeMatcher = mimeTypePattern.matcher(this.mimeType);
        if (!mimeTypeMatcher.matches()) {
            throw new IllegalArgumentException(("The given mimeType " + this.mimeType + " does not match to required \"type/subtype\" format").toString());
        }
        MimeType splitMimeType = new MimeType(this.mimeType);
        String mimeTypeRegex = "^(" + splitMimeType.getType() + "|[*]+)/(" + splitMimeType.getSubType() + "|[*]+)$";
        this.mimeTypeFinalRegex = StringsKt.replace$default(mimeTypeRegex, "*|[*]", "[\\s\\S]", false, 4, (Object) null);
    }

    public final String getUriPattern() {
        return this.uriPattern;
    }

    public final String getAction() {
        return this.action;
    }

    public final String getMimeType() {
        return this.mimeType;
    }

    private final Pattern getPattern() {
        return (Pattern) this.pattern.getValue();
    }

    private final Pattern getMimeTypePattern() {
        return (Pattern) this.mimeTypePattern.getValue();
    }

    public final List<String> getArgumentsNames$navigation_common_release() {
        List<String> list = this.arguments;
        Iterable $this$flatMap$iv = this.paramArgMap.values();
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$flatMap$iv) {
            ParamQuery it = (ParamQuery) element$iv$iv;
            Iterable list$iv$iv = it.getArguments();
            CollectionsKt.addAll(destination$iv$iv, list$iv$iv);
        }
        return CollectionsKt.plus((Collection) list, destination$iv$iv);
    }

    /* renamed from: isExactDeepLink, reason: from getter */
    public final boolean getIsExactDeepLink() {
        return this.isExactDeepLink;
    }

    public final void setExactDeepLink$navigation_common_release(boolean z) {
        this.isExactDeepLink = z;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public NavDeepLink(String uri) {
        this(uri, null, null);
        Intrinsics.checkNotNullParameter(uri, "uri");
    }

    private final boolean buildPathRegex(String uri, StringBuilder uriRegex, Pattern fillInPattern) {
        Matcher matcher = fillInPattern.matcher(uri);
        int appendPos = 0;
        boolean exactDeepLink = !StringsKt.contains$default((CharSequence) uri, (CharSequence) ".*", false, 2, (Object) null);
        while (matcher.find()) {
            String argName = matcher.group(1);
            if (argName == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
            this.arguments.add(argName);
            String substring = uri.substring(appendPos, matcher.start());
            Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            uriRegex.append(Pattern.quote(substring));
            uriRegex.append("([^/]+?)");
            appendPos = matcher.end();
            exactDeepLink = false;
        }
        if (appendPos < uri.length()) {
            String substring2 = uri.substring(appendPos);
            Intrinsics.checkNotNullExpressionValue(substring2, "this as java.lang.String).substring(startIndex)");
            uriRegex.append(Pattern.quote(substring2));
        }
        uriRegex.append("($|(\\?(.)*)|(\\#(.)*))");
        return exactDeepLink;
    }

    public final boolean matches$navigation_common_release(Uri uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return matches$navigation_common_release(new NavDeepLinkRequest(uri, null, null));
    }

    public final boolean matches$navigation_common_release(NavDeepLinkRequest deepLinkRequest) {
        Intrinsics.checkNotNullParameter(deepLinkRequest, "deepLinkRequest");
        if (matchUri(deepLinkRequest.getUri()) && matchAction(deepLinkRequest.getAction())) {
            return matchMimeType(deepLinkRequest.getMimeType());
        }
        return false;
    }

    private final boolean matchUri(Uri uri) {
        if ((uri == null) == (getPattern() != null)) {
            return false;
        }
        if (uri == null) {
            return true;
        }
        Pattern pattern = getPattern();
        Intrinsics.checkNotNull(pattern);
        return pattern.matcher(uri.toString()).matches();
    }

    private final boolean matchAction(String action) {
        boolean z = action == null;
        String str = this.action;
        if (z == (str != null)) {
            return false;
        }
        return action == null || Intrinsics.areEqual(str, action);
    }

    private final boolean matchMimeType(String mimeType) {
        if ((mimeType == null) == (this.mimeType != null)) {
            return false;
        }
        if (mimeType == null) {
            return true;
        }
        Pattern mimeTypePattern = getMimeTypePattern();
        Intrinsics.checkNotNull(mimeTypePattern);
        return mimeTypePattern.matcher(mimeType).matches();
    }

    public final int getMimeTypeMatchRating(String mimeType) {
        Intrinsics.checkNotNullParameter(mimeType, "mimeType");
        if (this.mimeType != null) {
            Pattern mimeTypePattern = getMimeTypePattern();
            Intrinsics.checkNotNull(mimeTypePattern);
            if (mimeTypePattern.matcher(mimeType).matches()) {
                return new MimeType(this.mimeType).compareTo(new MimeType(mimeType));
            }
        }
        return -1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v6, types: [android.os.Bundle, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r4v7 */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    public final Bundle getMatchingArguments(Uri deepLink, Map<String, NavArgument> arguments) {
        Matcher argMatcher;
        Uri deepLink2 = deepLink;
        Map<String, NavArgument> arguments2 = arguments;
        Intrinsics.checkNotNullParameter(deepLink2, "deepLink");
        Intrinsics.checkNotNullParameter(arguments2, "arguments");
        Pattern pattern = getPattern();
        ?? r4 = 0;
        Matcher matcher = pattern != null ? pattern.matcher(deepLink.toString()) : null;
        if (matcher == null) {
            return null;
        }
        Matcher matcher2 = matcher;
        if (!matcher2.matches()) {
            return null;
        }
        Bundle bundle = new Bundle();
        int size = this.arguments.size();
        for (int index = 0; index < size; index++) {
            String argumentName = this.arguments.get(index);
            String value = Uri.decode(matcher2.group(index + 1));
            NavArgument argument = arguments2.get(argumentName);
            try {
                Intrinsics.checkNotNullExpressionValue(value, "value");
                if (parseArgument(bundle, argumentName, value, argument)) {
                    return null;
                }
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        if (this.isParameterizedQuery) {
            for (String paramName : this.paramArgMap.keySet()) {
                ParamQuery storedParam = this.paramArgMap.get(paramName);
                String inputParams = deepLink2.getQueryParameter(paramName);
                if (this.isSingleQueryParamValueOnly) {
                    String deepLinkString = deepLink.toString();
                    Intrinsics.checkNotNullExpressionValue(deepLinkString, "deepLink.toString()");
                    String argValue = StringsKt.substringAfter$default(deepLinkString, '?', (String) r4, 2, (Object) r4);
                    if (!Intrinsics.areEqual(argValue, deepLinkString)) {
                        inputParams = argValue;
                    }
                }
                if (inputParams == null) {
                    argMatcher = null;
                } else {
                    Intrinsics.checkNotNull(storedParam);
                    Matcher argMatcher2 = Pattern.compile(storedParam.getParamRegex(), 32).matcher(inputParams);
                    if (argMatcher2.matches()) {
                        argMatcher = argMatcher2;
                    } else {
                        return r4;
                    }
                }
                Bundle queryParamBundle = new Bundle();
                int index2 = 0;
                try {
                    Intrinsics.checkNotNull(storedParam);
                    int size2 = storedParam.size();
                    while (index2 < size2) {
                        String value2 = null;
                        if (argMatcher != null) {
                            String group = argMatcher.group(index2 + 1);
                            if (group == null) {
                                group = "";
                            }
                            value2 = group;
                        }
                        String argName = storedParam.getArgumentName(index2);
                        NavArgument argument2 = arguments2.get(argName);
                        if (value2 != null && !Intrinsics.areEqual(value2, '{' + argName + '}')) {
                            if (parseArgument(queryParamBundle, argName, value2, argument2)) {
                                return null;
                            }
                        }
                        index2++;
                        arguments2 = arguments;
                    }
                    bundle.putAll(queryParamBundle);
                    deepLink2 = deepLink;
                    arguments2 = arguments;
                    r4 = 0;
                } catch (IllegalArgumentException e2) {
                    deepLink2 = deepLink;
                    arguments2 = arguments;
                    r4 = 0;
                }
            }
        }
        for (Map.Entry<String, NavArgument> entry : arguments.entrySet()) {
            String argName2 = entry.getKey();
            NavArgument argument3 = entry.getValue();
            boolean argumentIsRequired = (argument3 == null || argument3.getIsNullable() || argument3.getIsDefaultValuePresent()) ? false : true;
            if (argumentIsRequired && !bundle.containsKey(argName2)) {
                return null;
            }
        }
        return bundle;
    }

    private final boolean parseArgument(Bundle bundle, String name, String value, NavArgument argument) {
        if (argument != null) {
            NavType type = argument.getType();
            type.parseAndPut(bundle, name, value);
            return false;
        }
        bundle.putString(name, value);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NavDeepLink.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0005J\u000e\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u0012R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006\u0014"}, d2 = {"Landroidx/navigation/NavDeepLink$ParamQuery;", "", "()V", "arguments", "", "", "getArguments", "()Ljava/util/List;", "paramRegex", "getParamRegex", "()Ljava/lang/String;", "setParamRegex", "(Ljava/lang/String;)V", "addArgumentName", "", "name", "getArgumentName", "index", "", "size", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class ParamQuery {
        private final List<String> arguments = new ArrayList();
        private String paramRegex;

        public final String getParamRegex() {
            return this.paramRegex;
        }

        public final void setParamRegex(String str) {
            this.paramRegex = str;
        }

        public final List<String> getArguments() {
            return this.arguments;
        }

        public final void addArgumentName(String name) {
            Intrinsics.checkNotNullParameter(name, "name");
            this.arguments.add(name);
        }

        public final String getArgumentName(int index) {
            return this.arguments.get(index);
        }

        public final int size() {
            return this.arguments.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NavDeepLink.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0011\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0000H\u0096\u0002R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\u0004R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\u0004¨\u0006\u000f"}, d2 = {"Landroidx/navigation/NavDeepLink$MimeType;", "", "mimeType", "", "(Ljava/lang/String;)V", "subType", "getSubType", "()Ljava/lang/String;", "setSubType", "type", "getType", "setType", "compareTo", "", "other", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class MimeType implements Comparable<MimeType> {
        private String subType;
        private String type;

        public MimeType(String mimeType) {
            List emptyList;
            Intrinsics.checkNotNullParameter(mimeType, "mimeType");
            List $this$dropLastWhile$iv = new Regex("/").split(mimeType, 0);
            if (!$this$dropLastWhile$iv.isEmpty()) {
                ListIterator iterator$iv = $this$dropLastWhile$iv.listIterator($this$dropLastWhile$iv.size());
                while (iterator$iv.hasPrevious()) {
                    String it = iterator$iv.previous();
                    if (!(it.length() == 0)) {
                        emptyList = CollectionsKt.take($this$dropLastWhile$iv, iterator$iv.nextIndex() + 1);
                        break;
                    }
                }
            }
            emptyList = CollectionsKt.emptyList();
            List typeAndSubType = emptyList;
            this.type = (String) typeAndSubType.get(0);
            this.subType = (String) typeAndSubType.get(1);
        }

        public final String getType() {
            return this.type;
        }

        public final void setType(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.type = str;
        }

        public final String getSubType() {
            return this.subType;
        }

        public final void setSubType(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.subType = str;
        }

        @Override // java.lang.Comparable
        public int compareTo(MimeType other) {
            Intrinsics.checkNotNullParameter(other, "other");
            int result = 0;
            if (Intrinsics.areEqual(this.type, other.type)) {
                result = 0 + 2;
            }
            if (Intrinsics.areEqual(this.subType, other.subType)) {
                return result + 1;
            }
            return result;
        }
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof NavDeepLink) && Intrinsics.areEqual(this.uriPattern, ((NavDeepLink) other).uriPattern) && Intrinsics.areEqual(this.action, ((NavDeepLink) other).action) && Intrinsics.areEqual(this.mimeType, ((NavDeepLink) other).mimeType);
    }

    public int hashCode() {
        int i = 0 * 31;
        String str = this.uriPattern;
        int result = i + (str != null ? str.hashCode() : 0);
        int result2 = result * 31;
        String str2 = this.action;
        int result3 = (result2 + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.mimeType;
        return result3 + (str3 != null ? str3.hashCode() : 0);
    }

    /* compiled from: NavDeepLink.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0007\b\u0017¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004J\u000e\u0010\n\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Landroidx/navigation/NavDeepLink$Builder;", "", "()V", "action", "", "mimeType", "uriPattern", "build", "Landroidx/navigation/NavDeepLink;", "setAction", "setMimeType", "setUriPattern", "Companion", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Builder {

        /* renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);
        private String action;
        private String mimeType;
        private String uriPattern;

        @JvmStatic
        public static final Builder fromAction(String str) {
            return INSTANCE.fromAction(str);
        }

        @JvmStatic
        public static final Builder fromMimeType(String str) {
            return INSTANCE.fromMimeType(str);
        }

        @JvmStatic
        public static final Builder fromUriPattern(String str) {
            return INSTANCE.fromUriPattern(str);
        }

        public final Builder setUriPattern(String uriPattern) {
            Intrinsics.checkNotNullParameter(uriPattern, "uriPattern");
            this.uriPattern = uriPattern;
            return this;
        }

        public final Builder setAction(String action) {
            Intrinsics.checkNotNullParameter(action, "action");
            if (!(action.length() > 0)) {
                throw new IllegalArgumentException("The NavDeepLink cannot have an empty action.".toString());
            }
            this.action = action;
            return this;
        }

        public final Builder setMimeType(String mimeType) {
            Intrinsics.checkNotNullParameter(mimeType, "mimeType");
            this.mimeType = mimeType;
            return this;
        }

        public final NavDeepLink build() {
            return new NavDeepLink(this.uriPattern, this.action, this.mimeType);
        }

        /* compiled from: NavDeepLink.kt */
        @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0006H\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u0006H\u0007¨\u0006\u000b"}, d2 = {"Landroidx/navigation/NavDeepLink$Builder$Companion;", "", "()V", "fromAction", "Landroidx/navigation/NavDeepLink$Builder;", "action", "", "fromMimeType", "mimeType", "fromUriPattern", "uriPattern", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
        /* loaded from: classes.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            @JvmStatic
            public final Builder fromUriPattern(String uriPattern) {
                Intrinsics.checkNotNullParameter(uriPattern, "uriPattern");
                Builder builder = new Builder();
                builder.setUriPattern(uriPattern);
                return builder;
            }

            @JvmStatic
            public final Builder fromAction(String action) {
                Intrinsics.checkNotNullParameter(action, "action");
                if (!(action.length() > 0)) {
                    throw new IllegalArgumentException("The NavDeepLink cannot have an empty action.".toString());
                }
                Builder builder = new Builder();
                builder.setAction(action);
                return builder;
            }

            @JvmStatic
            public final Builder fromMimeType(String mimeType) {
                Intrinsics.checkNotNullParameter(mimeType, "mimeType");
                Builder builder = new Builder();
                builder.setMimeType(mimeType);
                return builder;
            }
        }
    }

    /* compiled from: NavDeepLink.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Landroidx/navigation/NavDeepLink$Companion;", "", "()V", "SCHEME_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "navigation-common_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
