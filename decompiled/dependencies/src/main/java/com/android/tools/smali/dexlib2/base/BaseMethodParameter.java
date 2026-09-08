package com.android.tools.smali.dexlib2.base;

import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue;
import java.util.Iterator;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseMethodParameter extends BaseTypeReference implements MethodParameter {
    @Override // com.android.tools.smali.dexlib2.iface.MethodParameter, com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        Annotation signatureAnnotation = null;
        Iterator<? extends Annotation> it = getAnnotations().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Annotation annotation = it.next();
            if (annotation.getType().equals("Ldalvik/annotation/Signature;")) {
                signatureAnnotation = annotation;
                break;
            }
        }
        if (signatureAnnotation == null) {
            return null;
        }
        ArrayEncodedValue signatureValues = null;
        Iterator<? extends AnnotationElement> it2 = signatureAnnotation.getElements().iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            AnnotationElement annotationElement = it2.next();
            if (annotationElement.getName().equals("value")) {
                EncodedValue encodedValue = annotationElement.getValue();
                if (encodedValue.getValueType() != 28) {
                    return null;
                }
                signatureValues = (ArrayEncodedValue) encodedValue;
            }
        }
        if (signatureValues == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (EncodedValue signatureValue : signatureValues.getValue()) {
            if (signatureValue.getValueType() != 23) {
                return null;
            }
            sb.append(((StringEncodedValue) signatureValue).getValue());
        }
        return sb.toString();
    }
}
