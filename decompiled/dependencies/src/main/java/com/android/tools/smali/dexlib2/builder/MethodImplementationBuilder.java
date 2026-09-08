package com.android.tools.smali.dexlib2.builder;

import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class MethodImplementationBuilder {
    private MethodLocation currentLocation;

    @Nonnull
    private final MutableMethodImplementation impl;
    private final HashMap<String, Label> labels = new HashMap<>();

    public MethodImplementationBuilder(int registerCount) {
        MutableMethodImplementation mutableMethodImplementation = new MutableMethodImplementation(registerCount);
        this.impl = mutableMethodImplementation;
        this.currentLocation = mutableMethodImplementation.instructionList.get(0);
    }

    public MethodImplementation getMethodImplementation() {
        return this.impl;
    }

    @Nonnull
    public Label addLabel(@Nonnull String name) {
        Label label = this.labels.get(name);
        if (label != null) {
            if (label.isPlaced()) {
                throw new IllegalArgumentException("There is already a label with that name.");
            }
            this.currentLocation.getLabels().add(label);
            return label;
        }
        Label label2 = this.currentLocation.addNewLabel();
        this.labels.put(name, label2);
        return label2;
    }

    @Nonnull
    public Label getLabel(@Nonnull String name) {
        Label label = this.labels.get(name);
        if (label == null) {
            Label label2 = new Label();
            this.labels.put(name, label2);
            return label2;
        }
        return label;
    }

    public void addCatch(@Nullable TypeReference type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.impl.addCatch(type, from, to, handler);
    }

    public void addCatch(@Nullable String type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.impl.addCatch(type, from, to, handler);
    }

    public void addCatch(@Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.impl.addCatch(from, to, handler);
    }

    public void addLineNumber(int lineNumber) {
        this.currentLocation.addLineNumber(lineNumber);
    }

    public void addStartLocal(int registerNumber, @Nullable StringReference name, @Nullable TypeReference type, @Nullable StringReference signature) {
        this.currentLocation.addStartLocal(registerNumber, name, type, signature);
    }

    public void addEndLocal(int registerNumber) {
        this.currentLocation.addEndLocal(registerNumber);
    }

    public void addRestartLocal(int registerNumber) {
        this.currentLocation.addRestartLocal(registerNumber);
    }

    public void addPrologue() {
        this.currentLocation.addPrologue();
    }

    public void addEpilogue() {
        this.currentLocation.addEpilogue();
    }

    public void addSetSourceFile(@Nullable StringReference sourceFile) {
        this.currentLocation.addSetSourceFile(sourceFile);
    }

    public void addInstruction(@Nullable BuilderInstruction instruction) {
        this.impl.addInstruction(instruction);
        this.currentLocation = this.impl.instructionList.get(this.impl.instructionList.size() - 1);
    }
}
