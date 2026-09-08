package com.google.thirdparty.publicsuffix;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Queues;
import java.util.Deque;

/* loaded from: classes7.dex */
final class TrieParser {
    private static final Joiner PREFIX_JOINER = Joiner.on("");

    TrieParser() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded) {
        ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
        int encodedLen = encoded.length();
        int idx = 0;
        while (idx < encodedLen) {
            idx += doParseTrieToBuilder(Queues.newArrayDeque(), encoded, idx, builder);
        }
        return builder.buildOrThrow();
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x004e, code lost:

        if (r2 != ',') goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0050, code lost:

        if (r1 >= r0) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0052, code lost:

        r1 = r1 + doParseTrieToBuilder(r8, r9, r1, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005b, code lost:

        if (r9.charAt(r1) == '?') goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0061, code lost:

        if (r9.charAt(r1) != ',') goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0063, code lost:

        r1 = r1 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int doParseTrieToBuilder(Deque<CharSequence> stack, CharSequence encoded, int start, ImmutableMap.Builder<String, PublicSuffixType> builder) {
        int encodedLen = encoded.length();
        int idx = start;
        char c = 0;
        while (idx < encodedLen && (c = encoded.charAt(idx)) != '&' && c != '?' && c != '!' && c != ':' && c != ',') {
            idx++;
        }
        stack.push(reverse(encoded.subSequence(start, idx)));
        if (c == '!' || c == '?' || c == ':' || c == ',') {
            String domain = PREFIX_JOINER.join(stack);
            if (domain.length() > 0) {
                builder.put(domain, PublicSuffixType.fromCode(c));
            }
        }
        int idx2 = idx + 1;
        if (c != '?') {
        }
        stack.pop();
        return idx2 - start;
    }

    private static CharSequence reverse(CharSequence s) {
        return new StringBuilder(s).reverse();
    }
}
