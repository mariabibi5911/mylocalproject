package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.instruction.InlineIndexInstruction;
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod;
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference;
import com.android.tools.smali.dexlib2.immutable.util.ParamUtil;
import com.bumptech.glide.load.Key;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class CustomInlineMethodResolver extends InlineMethodResolver {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Pattern longMethodPattern = Pattern.compile("(L[^;]+;)->([^(]+)\\(([^)]*)\\)(.+)");

    @Nonnull
    private final ClassPath classPath;

    @Nonnull
    private final Method[] inlineMethods;

    public CustomInlineMethodResolver(@Nonnull ClassPath classPath, @Nonnull String inlineTable) {
        this.classPath = classPath;
        StringReader reader = new StringReader(inlineTable);
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(reader);
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.length() > 0) {
                    lines.add(line);
                }
            }
            this.inlineMethods = new Method[lines.size()];
            int i = 0;
            while (true) {
                Method[] methodArr = this.inlineMethods;
                if (i < methodArr.length) {
                    methodArr[i] = parseAndResolveInlineMethod(lines.get(i));
                    i++;
                } else {
                    return;
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error while parsing inline table", ex);
        }
    }

    public CustomInlineMethodResolver(@Nonnull ClassPath classPath, @Nonnull File inlineTable) throws IOException {
        this(classPath, Files.toString(inlineTable, Charset.forName(Key.STRING_CHARSET_NAME)));
    }

    @Override // com.android.tools.smali.dexlib2.analysis.InlineMethodResolver
    @Nonnull
    public Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
        InlineIndexInstruction instruction = (InlineIndexInstruction) analyzedInstruction.instruction;
        int methodIndex = instruction.getInlineIndex();
        if (methodIndex >= 0) {
            Method[] methodArr = this.inlineMethods;
            if (methodIndex < methodArr.length) {
                return methodArr[methodIndex];
            }
        }
        throw new RuntimeException("Invalid method index: " + methodIndex);
    }

    @Nonnull
    private Method parseAndResolveInlineMethod(@Nonnull String inlineMethod) {
        int accessFlags;
        boolean resolved;
        Matcher m = longMethodPattern.matcher(inlineMethod);
        if (!m.matches()) {
            throw new AssertionError();
        }
        String className = m.group(1);
        String methodName = m.group(2);
        Iterable<ImmutableMethodParameter> methodParams = ParamUtil.parseParamString(m.group(3));
        String methodRet = m.group(4);
        ImmutableMethodReference methodRef = new ImmutableMethodReference(className, methodName, methodParams, methodRet);
        TypeProto typeProto = this.classPath.getClass(className);
        if (typeProto instanceof ClassProto) {
            ClassDef classDef = ((ClassProto) typeProto).getClassDef();
            for (Method method : classDef.getMethods()) {
                if (method.equals(methodRef)) {
                    int accessFlags2 = method.getAccessFlags();
                    accessFlags = accessFlags2;
                    resolved = true;
                    break;
                }
            }
        }
        accessFlags = 0;
        resolved = false;
        if (!resolved) {
            throw new RuntimeException("Cannot resolve inline method: " + inlineMethod);
        }
        return new ImmutableMethod(className, methodName, methodParams, methodRet, accessFlags, (Set<? extends Annotation>) null, (Set<HiddenApiRestriction>) null, (MethodImplementation) null);
    }
}
