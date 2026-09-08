package com.android.tools.smali.dexlib2;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.location.LocationRequestCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.android.tools.smali.dexlib2.dexbacked.raw.CdexHeaderItem;
import com.google.android.material.internal.ViewUtils;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* loaded from: classes.dex */
public enum Opcode {
    NOP(0, "nop", 7, Format.Format10x, 4),
    MOVE(1, "move", 7, Format.Format12x, 20),
    MOVE_FROM16(2, "move/from16", 7, Format.Format22x, 20),
    MOVE_16(3, "move/16", 7, Format.Format32x, 20),
    MOVE_WIDE(4, "move-wide", 7, Format.Format12x, 52),
    MOVE_WIDE_FROM16(5, "move-wide/from16", 7, Format.Format22x, 52),
    MOVE_WIDE_16(6, "move-wide/16", 7, Format.Format32x, 52),
    MOVE_OBJECT(7, "move-object", 7, Format.Format12x, 20),
    MOVE_OBJECT_FROM16(8, "move-object/from16", 7, Format.Format22x, 20),
    MOVE_OBJECT_16(9, "move-object/16", 7, Format.Format32x, 20),
    MOVE_RESULT(10, "move-result", 7, Format.Format11x, 20),
    MOVE_RESULT_WIDE(11, "move-result-wide", 7, Format.Format11x, 52),
    MOVE_RESULT_OBJECT(12, "move-result-object", 7, Format.Format11x, 20),
    MOVE_EXCEPTION(13, "move-exception", 7, Format.Format11x, 20),
    RETURN_VOID(14, "return-void", 7, Format.Format10x),
    RETURN(15, "return", 7, Format.Format11x),
    RETURN_WIDE(16, "return-wide", 7, Format.Format11x),
    RETURN_OBJECT(17, "return-object", 7, Format.Format11x),
    CONST_4(18, "const/4", 7, Format.Format11n, 20),
    CONST_16(19, "const/16", 7, Format.Format21s, 20),
    CONST(20, "const", 7, Format.Format31i, 20),
    CONST_HIGH16(21, "const/high16", 7, Format.Format21ih, 20),
    CONST_WIDE_16(22, "const-wide/16", 7, Format.Format21s, 52),
    CONST_WIDE_32(23, "const-wide/32", 7, Format.Format31i, 52),
    CONST_WIDE(24, "const-wide", 7, Format.Format51l, 52),
    CONST_WIDE_HIGH16(25, "const-wide/high16", 7, Format.Format21lh, 52),
    CONST_STRING(26, "const-string", 0, Format.Format21c, 21),
    CONST_STRING_JUMBO(27, "const-string/jumbo", 0, Format.Format31c, 21),
    CONST_CLASS(28, "const-class", 1, Format.Format21c, 21),
    MONITOR_ENTER(29, "monitor-enter", 7, Format.Format11x, 5),
    MONITOR_EXIT(30, "monitor-exit", 7, Format.Format11x, 5),
    CHECK_CAST(31, "check-cast", 1, Format.Format21c, 21),
    INSTANCE_OF(32, "instance-of", 1, Format.Format22c, 21),
    ARRAY_LENGTH(33, "array-length", 7, Format.Format12x, 21),
    NEW_INSTANCE(34, "new-instance", 1, Format.Format21c, 21),
    NEW_ARRAY(35, "new-array", 1, Format.Format22c, 21),
    FILLED_NEW_ARRAY(36, "filled-new-array", 1, Format.Format35c, 13),
    FILLED_NEW_ARRAY_RANGE(37, "filled-new-array/range", 1, Format.Format3rc, 13),
    FILL_ARRAY_DATA(38, "fill-array-data", 7, Format.Format31t, 4),
    THROW(39, "throw", 7, Format.Format11x, 1),
    GOTO(40, "goto", 7, Format.Format10t),
    GOTO_16(41, "goto/16", 7, Format.Format20t),
    GOTO_32(42, "goto/32", 7, Format.Format30t),
    PACKED_SWITCH(43, "packed-switch", 7, Format.Format31t, 4),
    SPARSE_SWITCH(44, "sparse-switch", 7, Format.Format31t, 4),
    CMPL_FLOAT(45, "cmpl-float", 7, Format.Format23x, 20),
    CMPG_FLOAT(46, "cmpg-float", 7, Format.Format23x, 20),
    CMPL_DOUBLE(47, "cmpl-double", 7, Format.Format23x, 20),
    CMPG_DOUBLE(48, "cmpg-double", 7, Format.Format23x, 20),
    CMP_LONG(49, "cmp-long", 7, Format.Format23x, 20),
    IF_EQ(50, "if-eq", 7, Format.Format22t, 4),
    IF_NE(51, "if-ne", 7, Format.Format22t, 4),
    IF_LT(52, "if-lt", 7, Format.Format22t, 4),
    IF_GE(53, "if-ge", 7, Format.Format22t, 4),
    IF_GT(54, "if-gt", 7, Format.Format22t, 4),
    IF_LE(55, "if-le", 7, Format.Format22t, 4),
    IF_EQZ(56, "if-eqz", 7, Format.Format21t, 4),
    IF_NEZ(57, "if-nez", 7, Format.Format21t, 4),
    IF_LTZ(58, "if-ltz", 7, Format.Format21t, 4),
    IF_GEZ(59, "if-gez", 7, Format.Format21t, 4),
    IF_GTZ(60, "if-gtz", 7, Format.Format21t, 4),
    IF_LEZ(61, "if-lez", 7, Format.Format21t, 4),
    AGET(68, "aget", 7, Format.Format23x, 21),
    AGET_WIDE(69, "aget-wide", 7, Format.Format23x, 53),
    AGET_OBJECT(70, "aget-object", 7, Format.Format23x, 21),
    AGET_BOOLEAN(71, "aget-boolean", 7, Format.Format23x, 21),
    AGET_BYTE(72, "aget-byte", 7, Format.Format23x, 21),
    AGET_CHAR(73, "aget-char", 7, Format.Format23x, 21),
    AGET_SHORT(74, "aget-short", 7, Format.Format23x, 21),
    APUT(75, "aput", 7, Format.Format23x, 5),
    APUT_WIDE(76, "aput-wide", 7, Format.Format23x, 5),
    APUT_OBJECT(77, "aput-object", 7, Format.Format23x, 5),
    APUT_BOOLEAN(78, "aput-boolean", 7, Format.Format23x, 5),
    APUT_BYTE(79, "aput-byte", 7, Format.Format23x, 5),
    APUT_CHAR(80, "aput-char", 7, Format.Format23x, 5),
    APUT_SHORT(81, "aput-short", 7, Format.Format23x, 5),
    IGET(82, "iget", 2, Format.Format22c, 21),
    IGET_WIDE(83, "iget-wide", 2, Format.Format22c, 53),
    IGET_OBJECT(84, "iget-object", 2, Format.Format22c, 21),
    IGET_BOOLEAN(85, "iget-boolean", 2, Format.Format22c, 21),
    IGET_BYTE(86, "iget-byte", 2, Format.Format22c, 21),
    IGET_CHAR(87, "iget-char", 2, Format.Format22c, 21),
    IGET_SHORT(88, "iget-short", 2, Format.Format22c, 21),
    IPUT(89, "iput", 2, Format.Format22c, 5),
    IPUT_WIDE(90, "iput-wide", 2, Format.Format22c, 5),
    IPUT_OBJECT(91, "iput-object", 2, Format.Format22c, 5),
    IPUT_BOOLEAN(92, "iput-boolean", 2, Format.Format22c, 5),
    IPUT_BYTE(93, "iput-byte", 2, Format.Format22c, 5),
    IPUT_CHAR(94, "iput-char", 2, Format.Format22c, 5),
    IPUT_SHORT(95, "iput-short", 2, Format.Format22c, 5),
    SGET(96, "sget", 2, Format.Format21c, 277),
    SGET_WIDE(97, "sget-wide", 2, Format.Format21c, 309),
    SGET_OBJECT(98, "sget-object", 2, Format.Format21c, 277),
    SGET_BOOLEAN(99, "sget-boolean", 2, Format.Format21c, 277),
    SGET_BYTE(100, "sget-byte", 2, Format.Format21c, 277),
    SGET_CHAR(TypedValues.TYPE_TARGET, "sget-char", 2, Format.Format21c, 277),
    SGET_SHORT(LocationRequestCompat.QUALITY_BALANCED_POWER_ACCURACY, "sget-short", 2, Format.Format21c, 277),
    SPUT(103, "sput", 2, Format.Format21c, 261),
    SPUT_WIDE(104, "sput-wide", 2, Format.Format21c, 261),
    SPUT_OBJECT(105, "sput-object", 2, Format.Format21c, 261),
    SPUT_BOOLEAN(106, "sput-boolean", 2, Format.Format21c, 261),
    SPUT_BYTE(107, "sput-byte", 2, Format.Format21c, 261),
    SPUT_CHAR(108, "sput-char", 2, Format.Format21c, 261),
    SPUT_SHORT(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY, "sput-short", 2, Format.Format21c, 261),
    INVOKE_VIRTUAL(110, "invoke-virtual", 3, Format.Format35c, 13),
    INVOKE_SUPER(111, "invoke-super", 3, Format.Format35c, 13),
    INVOKE_DIRECT(112, "invoke-direct", 3, Format.Format35c, 1037),
    INVOKE_STATIC(113, "invoke-static", 3, Format.Format35c, 13),
    INVOKE_INTERFACE(114, "invoke-interface", 3, Format.Format35c, 13),
    INVOKE_VIRTUAL_RANGE(CdexHeaderItem.DEBUG_INFO_OFFSETS_POS_OFFSET, "invoke-virtual/range", 3, Format.Format3rc, 13),
    INVOKE_SUPER_RANGE(117, "invoke-super/range", 3, Format.Format3rc, 13),
    INVOKE_DIRECT_RANGE(118, "invoke-direct/range", 3, Format.Format3rc, 1037),
    INVOKE_STATIC_RANGE(119, "invoke-static/range", 3, Format.Format3rc, 13),
    INVOKE_INTERFACE_RANGE(CdexHeaderItem.DEBUG_INFO_OFFSETS_TABLE_OFFSET, "invoke-interface/range", 3, Format.Format3rc, 13),
    NEG_INT(123, "neg-int", 7, Format.Format12x, 20),
    NOT_INT(CdexHeaderItem.DEBUG_INFO_BASE, "not-int", 7, Format.Format12x, 20),
    NEG_LONG(125, "neg-long", 7, Format.Format12x, 52),
    NOT_LONG(126, "not-long", 7, Format.Format12x, 52),
    NEG_FLOAT(WorkQueueKt.MASK, "neg-float", 7, Format.Format12x, 20),
    NEG_DOUBLE(128, "neg-double", 7, Format.Format12x, 52),
    INT_TO_LONG(129, "int-to-long", 7, Format.Format12x, 52),
    INT_TO_FLOAT(130, "int-to-float", 7, Format.Format12x, 20),
    INT_TO_DOUBLE(131, "int-to-double", 7, Format.Format12x, 52),
    LONG_TO_INT(132, "long-to-int", 7, Format.Format12x, 20),
    LONG_TO_FLOAT(133, "long-to-float", 7, Format.Format12x, 20),
    LONG_TO_DOUBLE(134, "long-to-double", 7, Format.Format12x, 52),
    FLOAT_TO_INT(135, "float-to-int", 7, Format.Format12x, 20),
    FLOAT_TO_LONG(136, "float-to-long", 7, Format.Format12x, 52),
    FLOAT_TO_DOUBLE(137, "float-to-double", 7, Format.Format12x, 52),
    DOUBLE_TO_INT(138, "double-to-int", 7, Format.Format12x, 20),
    DOUBLE_TO_LONG(139, "double-to-long", 7, Format.Format12x, 52),
    DOUBLE_TO_FLOAT(140, "double-to-float", 7, Format.Format12x, 20),
    INT_TO_BYTE(141, "int-to-byte", 7, Format.Format12x, 20),
    INT_TO_CHAR(142, "int-to-char", 7, Format.Format12x, 20),
    INT_TO_SHORT(143, "int-to-short", 7, Format.Format12x, 20),
    ADD_INT(144, "add-int", 7, Format.Format23x, 20),
    SUB_INT(145, "sub-int", 7, Format.Format23x, 20),
    MUL_INT(146, "mul-int", 7, Format.Format23x, 20),
    DIV_INT(147, "div-int", 7, Format.Format23x, 21),
    REM_INT(148, "rem-int", 7, Format.Format23x, 21),
    AND_INT(149, "and-int", 7, Format.Format23x, 20),
    OR_INT(150, "or-int", 7, Format.Format23x, 20),
    XOR_INT(151, "xor-int", 7, Format.Format23x, 20),
    SHL_INT(152, "shl-int", 7, Format.Format23x, 20),
    SHR_INT(153, "shr-int", 7, Format.Format23x, 20),
    USHR_INT(154, "ushr-int", 7, Format.Format23x, 20),
    ADD_LONG(155, "add-long", 7, Format.Format23x, 52),
    SUB_LONG(156, "sub-long", 7, Format.Format23x, 52),
    MUL_LONG(157, "mul-long", 7, Format.Format23x, 52),
    DIV_LONG(158, "div-long", 7, Format.Format23x, 53),
    REM_LONG(159, "rem-long", 7, Format.Format23x, 53),
    AND_LONG(160, "and-long", 7, Format.Format23x, 52),
    OR_LONG(161, "or-long", 7, Format.Format23x, 52),
    XOR_LONG(162, "xor-long", 7, Format.Format23x, 52),
    SHL_LONG(163, "shl-long", 7, Format.Format23x, 52),
    SHR_LONG(164, "shr-long", 7, Format.Format23x, 52),
    USHR_LONG(165, "ushr-long", 7, Format.Format23x, 52),
    ADD_FLOAT(166, "add-float", 7, Format.Format23x, 20),
    SUB_FLOAT(167, "sub-float", 7, Format.Format23x, 20),
    MUL_FLOAT(168, "mul-float", 7, Format.Format23x, 20),
    DIV_FLOAT(169, "div-float", 7, Format.Format23x, 20),
    REM_FLOAT(170, "rem-float", 7, Format.Format23x, 20),
    ADD_DOUBLE(171, "add-double", 7, Format.Format23x, 52),
    SUB_DOUBLE(172, "sub-double", 7, Format.Format23x, 52),
    MUL_DOUBLE(173, "mul-double", 7, Format.Format23x, 52),
    DIV_DOUBLE(174, "div-double", 7, Format.Format23x, 52),
    REM_DOUBLE(175, "rem-double", 7, Format.Format23x, 52),
    ADD_INT_2ADDR(176, "add-int/2addr", 7, Format.Format12x, 20),
    SUB_INT_2ADDR(177, "sub-int/2addr", 7, Format.Format12x, 20),
    MUL_INT_2ADDR(178, "mul-int/2addr", 7, Format.Format12x, 20),
    DIV_INT_2ADDR(179, "div-int/2addr", 7, Format.Format12x, 21),
    REM_INT_2ADDR(180, "rem-int/2addr", 7, Format.Format12x, 21),
    AND_INT_2ADDR(181, "and-int/2addr", 7, Format.Format12x, 20),
    OR_INT_2ADDR(182, "or-int/2addr", 7, Format.Format12x, 20),
    XOR_INT_2ADDR(183, "xor-int/2addr", 7, Format.Format12x, 20),
    SHL_INT_2ADDR(184, "shl-int/2addr", 7, Format.Format12x, 20),
    SHR_INT_2ADDR(185, "shr-int/2addr", 7, Format.Format12x, 20),
    USHR_INT_2ADDR(186, "ushr-int/2addr", 7, Format.Format12x, 20),
    ADD_LONG_2ADDR(187, "add-long/2addr", 7, Format.Format12x, 52),
    SUB_LONG_2ADDR(188, "sub-long/2addr", 7, Format.Format12x, 52),
    MUL_LONG_2ADDR(189, "mul-long/2addr", 7, Format.Format12x, 52),
    DIV_LONG_2ADDR(190, "div-long/2addr", 7, Format.Format12x, 53),
    REM_LONG_2ADDR(191, "rem-long/2addr", 7, Format.Format12x, 53),
    AND_LONG_2ADDR(192, "and-long/2addr", 7, Format.Format12x, 52),
    OR_LONG_2ADDR(193, "or-long/2addr", 7, Format.Format12x, 52),
    XOR_LONG_2ADDR(194, "xor-long/2addr", 7, Format.Format12x, 52),
    SHL_LONG_2ADDR(195, "shl-long/2addr", 7, Format.Format12x, 52),
    SHR_LONG_2ADDR(196, "shr-long/2addr", 7, Format.Format12x, 52),
    USHR_LONG_2ADDR(197, "ushr-long/2addr", 7, Format.Format12x, 52),
    ADD_FLOAT_2ADDR(198, "add-float/2addr", 7, Format.Format12x, 20),
    SUB_FLOAT_2ADDR(199, "sub-float/2addr", 7, Format.Format12x, 20),
    MUL_FLOAT_2ADDR(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, "mul-float/2addr", 7, Format.Format12x, 20),
    DIV_FLOAT_2ADDR(201, "div-float/2addr", 7, Format.Format12x, 20),
    REM_FLOAT_2ADDR(202, "rem-float/2addr", 7, Format.Format12x, 20),
    ADD_DOUBLE_2ADDR(203, "add-double/2addr", 7, Format.Format12x, 52),
    SUB_DOUBLE_2ADDR(204, "sub-double/2addr", 7, Format.Format12x, 52),
    MUL_DOUBLE_2ADDR(205, "mul-double/2addr", 7, Format.Format12x, 52),
    DIV_DOUBLE_2ADDR(206, "div-double/2addr", 7, Format.Format12x, 52),
    REM_DOUBLE_2ADDR(207, "rem-double/2addr", 7, Format.Format12x, 52),
    ADD_INT_LIT16(208, "add-int/lit16", 7, Format.Format22s, 20),
    RSUB_INT(209, "rsub-int", 7, Format.Format22s, 20),
    MUL_INT_LIT16(210, "mul-int/lit16", 7, Format.Format22s, 20),
    DIV_INT_LIT16(211, "div-int/lit16", 7, Format.Format22s, 21),
    REM_INT_LIT16(212, "rem-int/lit16", 7, Format.Format22s, 21),
    AND_INT_LIT16(213, "and-int/lit16", 7, Format.Format22s, 20),
    OR_INT_LIT16(214, "or-int/lit16", 7, Format.Format22s, 20),
    XOR_INT_LIT16(215, "xor-int/lit16", 7, Format.Format22s, 20),
    ADD_INT_LIT8(216, "add-int/lit8", 7, Format.Format22b, 20),
    RSUB_INT_LIT8(217, "rsub-int/lit8", 7, Format.Format22b, 20),
    MUL_INT_LIT8(218, "mul-int/lit8", 7, Format.Format22b, 20),
    DIV_INT_LIT8(219, "div-int/lit8", 7, Format.Format22b, 21),
    REM_INT_LIT8(220, "rem-int/lit8", 7, Format.Format22b, 21),
    AND_INT_LIT8(221, "and-int/lit8", 7, Format.Format22b, 20),
    OR_INT_LIT8(222, "or-int/lit8", 7, Format.Format22b, 20),
    XOR_INT_LIT8(223, "xor-int/lit8", 7, Format.Format22b, 20),
    SHL_INT_LIT8(224, "shl-int/lit8", 7, Format.Format22b, 20),
    SHR_INT_LIT8(225, "shr-int/lit8", 7, Format.Format22b, 20),
    USHR_INT_LIT8(226, "ushr-int/lit8", 7, Format.Format22b, 20),
    IGET_VOLATILE(firstApi(227, 9), "iget-volatile", 2, Format.Format22c, 151),
    IPUT_VOLATILE(firstApi(228, 9), "iput-volatile", 2, Format.Format22c, 135),
    SGET_VOLATILE(firstApi(229, 9), "sget-volatile", 2, Format.Format21c, 407),
    SPUT_VOLATILE(firstApi(230, 9), "sput-volatile", 2, Format.Format21c, 391),
    IGET_OBJECT_VOLATILE(firstApi(231, 9), "iget-object-volatile", 2, Format.Format22c, 151),
    IGET_WIDE_VOLATILE(firstApi(232, 9), "iget-wide-volatile", 2, Format.Format22c, 183),
    IPUT_WIDE_VOLATILE(firstApi(233, 9), "iput-wide-volatile", 2, Format.Format22c, 135),
    SGET_WIDE_VOLATILE(firstApi(234, 9), "sget-wide-volatile", 2, Format.Format21c, 439),
    SPUT_WIDE_VOLATILE(firstApi(235, 9), "sput-wide-volatile", 2, Format.Format21c, 391),
    THROW_VERIFICATION_ERROR(firstApi(237, 5), "throw-verification-error", 7, Format.Format20bc, 3),
    EXECUTE_INLINE(allApis(238), "execute-inline", 7, Format.Format35mi, 15),
    EXECUTE_INLINE_RANGE(firstApi(239, 8), "execute-inline/range", 7, Format.Format3rmi, 15),
    INVOKE_DIRECT_EMPTY(lastApi(240, 13), "invoke-direct-empty", 3, Format.Format35c, 1039),
    INVOKE_OBJECT_INIT_RANGE(firstApi(240, 14), "invoke-object-init/range", 3, Format.Format3rc, 1039),
    RETURN_VOID_BARRIER(combine(firstApi(241, 11), lastArtVersion(115, 59)), "return-void-barrier", 7, Format.Format10x, 2),
    RETURN_VOID_NO_BARRIER(firstArtVersion(115, 60), "return-void-no-barrier", 7, Format.Format10x, 2),
    IGET_QUICK(combine(allApis(242), allArtVersions(227)), "iget-quick", 7, Format.Format22cs, 87),
    IGET_WIDE_QUICK(combine(allApis(243), allArtVersions(228)), "iget-wide-quick", 7, Format.Format22cs, 119),
    IGET_OBJECT_QUICK(combine(allApis(244), allArtVersions(229)), "iget-object-quick", 7, Format.Format22cs, 87),
    IPUT_QUICK(combine(allApis(245), allArtVersions(230)), "iput-quick", 7, Format.Format22cs, 71),
    IPUT_WIDE_QUICK(combine(allApis(246), allArtVersions(231)), "iput-wide-quick", 7, Format.Format22cs, 71),
    IPUT_OBJECT_QUICK(combine(allApis(247), allArtVersions(232)), "iput-object-quick", 7, Format.Format22cs, 71),
    IPUT_BOOLEAN_QUICK(allArtVersions(235), "iput-boolean-quick", 7, Format.Format22cs, 71),
    IPUT_BYTE_QUICK(allArtVersions(236), "iput-byte-quick", 7, Format.Format22cs, 71),
    IPUT_CHAR_QUICK(allArtVersions(237), "iput-char-quick", 7, Format.Format22cs, 71),
    IPUT_SHORT_QUICK(allArtVersions(238), "iput-short-quick", 7, Format.Format22cs, 71),
    IGET_BOOLEAN_QUICK(allArtVersions(239), "iget-boolean-quick", 7, Format.Format22cs, 87),
    IGET_BYTE_QUICK(allArtVersions(240), "iget-byte-quick", 7, Format.Format22cs, 87),
    IGET_CHAR_QUICK(allArtVersions(241), "iget-char-quick", 7, Format.Format22cs, 87),
    IGET_SHORT_QUICK(allArtVersions(242), "iget-short-quick", 7, Format.Format22cs, 87),
    INVOKE_VIRTUAL_QUICK(combine(allApis(248), allArtVersions(233)), "invoke-virtual-quick", 7, Format.Format35ms, 15),
    INVOKE_VIRTUAL_QUICK_RANGE(combine(allApis(249), allArtVersions(234)), "invoke-virtual-quick/range", 7, Format.Format3rms, 15),
    INVOKE_SUPER_QUICK(lastApi(ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 25), "invoke-super-quick", 7, Format.Format35ms, 15),
    INVOKE_SUPER_QUICK_RANGE(lastApi(251, 25), "invoke-super-quick/range", 7, Format.Format3rms, 15),
    IPUT_OBJECT_VOLATILE(firstApi(252, 9), "iput-object-volatile", 2, Format.Format22c, 135),
    SGET_OBJECT_VOLATILE(firstApi(253, 9), "sget-object-volatile", 2, Format.Format21c, 407),
    SPUT_OBJECT_VOLATILE(betweenApi(254, 9, 19), "sput-object-volatile", 2, Format.Format21c, 391),
    PACKED_SWITCH_PAYLOAD(256, "packed-switch-payload", 7, Format.PackedSwitchPayload, 0),
    SPARSE_SWITCH_PAYLOAD(512, "sparse-switch-payload", 7, Format.SparseSwitchPayload, 0),
    ARRAY_PAYLOAD(ViewUtils.EDGE_TO_EDGE_FLAGS, "array-payload", 7, Format.ArrayPayload, 0),
    INVOKE_POLYMORPHIC(firstArtVersion(ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 87), "invoke-polymorphic", 3, 4, Format.Format45cc, 13),
    INVOKE_POLYMORPHIC_RANGE(firstArtVersion(251, 87), "invoke-polymorphic/range", 3, 4, Format.Format4rcc, 13),
    INVOKE_CUSTOM(firstArtVersion(252, 111), "invoke-custom", 5, Format.Format35c, 13),
    INVOKE_CUSTOM_RANGE(firstArtVersion(253, 111), "invoke-custom/range", 5, Format.Format3rc, 13),
    CONST_METHOD_HANDLE(firstArtVersion(254, 134), "const-method-handle", 6, Format.Format21c, 21),
    CONST_METHOD_TYPE(firstArtVersion(255, 134), "const-method-type", 4, Format.Format21c, 21);

    private static final int ALL_APIS = -65536;
    public static final int CAN_CONTINUE = 4;
    public static final int CAN_INITIALIZE_REFERENCE = 1024;
    public static final int CAN_THROW = 1;
    public static final int JUMBO_OPCODE = 512;
    public static final int ODEX_ONLY = 2;
    public static final int QUICK_FIELD_ACCESSOR = 64;
    public static final int SETS_REGISTER = 16;
    public static final int SETS_RESULT = 8;
    public static final int SETS_WIDE_REGISTER = 32;
    public static final int STATIC_FIELD_ACCESSOR = 256;
    public static final int VOLATILE_FIELD_ACCESSOR = 128;
    public final RangeMap<Integer, Short> apiToValueMap;
    public final RangeMap<Integer, Short> artVersionToValueMap;
    public final int flags;
    public final Format format;
    public final String name;
    public final int referenceType;
    public final int referenceType2;

    private static int minApi(int api) {
        return (65535 & api) | (-65536);
    }

    private static int maxApi(int api) {
        return api << 16;
    }

    Opcode(int opcodeValue, String opcodeName, int referenceType, Format format) {
        this(opcodeValue, opcodeName, referenceType, format, 0);
    }

    Opcode(int opcodeValue, String opcodeName, int referenceType, Format format, int flags) {
        this(allVersions(opcodeValue), opcodeName, referenceType, format, flags);
    }

    Opcode(List list, String opcodeName, int referenceType, Format format, int flags) {
        this(list, opcodeName, referenceType, -1, format, flags);
    }

    Opcode(List list, String opcodeName, int referenceType, int referenceType2, Format format, int flags) {
        ImmutableRangeMap.Builder<Integer, Short> apiToValueBuilder = ImmutableRangeMap.builder();
        ImmutableRangeMap.Builder<Integer, Short> artVersionToValueBuilder = ImmutableRangeMap.builder();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            VersionConstraint versionConstraint = (VersionConstraint) it.next();
            if (!versionConstraint.apiRange.isEmpty()) {
                apiToValueBuilder.put(versionConstraint.apiRange, Short.valueOf((short) versionConstraint.opcodeValue));
            }
            if (!versionConstraint.artVersionRange.isEmpty()) {
                artVersionToValueBuilder.put(versionConstraint.artVersionRange, Short.valueOf((short) versionConstraint.opcodeValue));
            }
        }
        this.apiToValueMap = apiToValueBuilder.build();
        this.artVersionToValueMap = artVersionToValueBuilder.build();
        this.name = opcodeName;
        this.referenceType = referenceType;
        this.referenceType2 = referenceType2;
        this.format = format;
        this.flags = flags;
    }

    private static List<VersionConstraint> firstApi(int opcodeValue, int api) {
        return Lists.newArrayList(new VersionConstraint(Range.atLeast(Integer.valueOf(api)), Range.openClosed(0, 0), opcodeValue));
    }

    private static List<VersionConstraint> lastApi(int opcodeValue, int api) {
        return Lists.newArrayList(new VersionConstraint(Range.atMost(Integer.valueOf(api)), Range.openClosed(0, 0), opcodeValue));
    }

    private static List<VersionConstraint> betweenApi(int opcodeValue, int minApi, int maxApi) {
        return Lists.newArrayList(new VersionConstraint(Range.closed(Integer.valueOf(minApi), Integer.valueOf(maxApi)), Range.openClosed(0, 0), opcodeValue));
    }

    private static List<VersionConstraint> firstArtVersion(int opcodeValue, int artVersion) {
        return Lists.newArrayList(new VersionConstraint(Range.openClosed(0, 0), Range.atLeast(Integer.valueOf(artVersion)), opcodeValue));
    }

    private static List<VersionConstraint> lastArtVersion(int opcodeValue, int artVersion) {
        return Lists.newArrayList(new VersionConstraint(Range.openClosed(0, 0), Range.atMost(Integer.valueOf(artVersion)), opcodeValue));
    }

    private static List<VersionConstraint> allVersions(int opcodeValue) {
        return Lists.newArrayList(new VersionConstraint(Range.all(), Range.all(), opcodeValue));
    }

    private static List<VersionConstraint> allApis(int opcodeValue) {
        return Lists.newArrayList(new VersionConstraint(Range.all(), Range.openClosed(0, 0), opcodeValue));
    }

    private static List<VersionConstraint> allArtVersions(int opcodeValue) {
        return Lists.newArrayList(new VersionConstraint(Range.openClosed(0, 0), Range.all(), opcodeValue));
    }

    private static List<VersionConstraint> combine(List<VersionConstraint>... versionConstraints) {
        List<VersionConstraint> combinedList = Lists.newArrayList();
        for (List<VersionConstraint> versionConstraintList : versionConstraints) {
            combinedList.addAll(versionConstraintList);
        }
        return combinedList;
    }

    public final boolean canThrow() {
        return (this.flags & 1) != 0;
    }

    public final boolean odexOnly() {
        return (this.flags & 2) != 0;
    }

    public final boolean canContinue() {
        return (this.flags & 4) != 0;
    }

    public final boolean setsResult() {
        return (this.flags & 8) != 0;
    }

    public final boolean setsRegister() {
        return (this.flags & 16) != 0;
    }

    public final boolean setsWideRegister() {
        return (this.flags & 32) != 0;
    }

    public final boolean isQuickFieldaccessor() {
        return (this.flags & 64) != 0;
    }

    public final boolean isVolatileFieldAccessor() {
        return (this.flags & 128) != 0;
    }

    public final boolean isStaticFieldAccessor() {
        return (this.flags & 256) != 0;
    }

    public final boolean isJumboOpcode() {
        return (this.flags & 512) != 0;
    }

    public final boolean canInitializeReference() {
        return (this.flags & 1024) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VersionConstraint {

        @Nonnull
        public final Range<Integer> apiRange;

        @Nonnull
        public final Range<Integer> artVersionRange;
        public final int opcodeValue;

        public VersionConstraint(@Nonnull Range<Integer> apiRange, @Nonnull Range<Integer> artVersionRange, int opcodeValue) {
            this.apiRange = apiRange;
            this.artVersionRange = artVersionRange;
            this.opcodeValue = opcodeValue;
        }
    }
}
