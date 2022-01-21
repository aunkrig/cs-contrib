
/*
 * cs-contrib - Additional checks, filters and quickfixes for CheckStyle and Eclipse-CS
 *
 * Copyright (c) 2014, Arno Unkrig
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.unkrig.cscontrib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * An internal ('local') representation of CheckStyle's {@link TokenTypes}. The reason being is that the values of the
 * integer constants declared in {@link TokenTypes} changed (slightly!) between CheckStyle 5.7 and 6.1, so they cannot
 * be used if one wants to be compatible with CS 5.7 <i>and</i> 6.1.
 */
public
enum LocalTokenType {

    // The following tokens exist in CHECKSTYLE versions 5.6 ... 5.8 and 6.0 ... 6.1.:

    // SUPPRESS CHECKSTYLE JavadocVariable:179
    ABSTRACT,
    ANNOTATION,
    ANNOTATIONS,
    ANNOTATION_ARRAY_INIT,
    ANNOTATION_DEF,
    ANNOTATION_FIELD_DEF,
    ANNOTATION_MEMBER_VALUE_PAIR,
    ARRAY_DECLARATOR,
    ARRAY_INIT,
    ASSIGN,
    AT,
    BAND,
    BAND_ASSIGN,
    BNOT,
    BOR,
    BOR_ASSIGN,
    BSR,
    BSR_ASSIGN,
    BXOR,
    BXOR_ASSIGN,
    CASE_GROUP,
    CHAR_LITERAL,
    CLASS_DEF,
    COLON,
    COMMA,
    CTOR_CALL,
    CTOR_DEF,
    DEC,
    DIV,
    DIV_ASSIGN,
    DOT,
    DO_WHILE,
    ELIST,
    ELLIPSIS,
    EMPTY_STAT,
    ENUM,
    ENUM_CONSTANT_DEF,
    ENUM_DEF,
    EOF,
    EQUAL,
    EXPR,
    EXTENDS_CLAUSE,
    FINAL,
    FOR_CONDITION,
    FOR_EACH_CLAUSE,
    FOR_INIT,
    FOR_ITERATOR,
    GE,
    GENERIC_END,
    GENERIC_START,
    GT,
    IDENT,
    IMPLEMENTS_CLAUSE,
    IMPORT,
    INC,
    INDEX_OP,
    INSTANCE_INIT,
    INTERFACE_DEF,
    LABELED_STAT,
    LAND,
    LCURLY,
    LE,
    LITERAL_ASSERT,
    LITERAL_BOOLEAN,
    LITERAL_BREAK,
    LITERAL_BYTE,
    LITERAL_CASE,
    LITERAL_CATCH,
    LITERAL_CHAR,
    LITERAL_CLASS,
    LITERAL_CONTINUE,
    LITERAL_DEFAULT,
    LITERAL_DO,
    LITERAL_DOUBLE,
    LITERAL_ELSE,
    LITERAL_FALSE,
    LITERAL_FINALLY,
    LITERAL_FLOAT,
    LITERAL_FOR,
    LITERAL_IF,
    LITERAL_INSTANCEOF,
    LITERAL_INT,
    LITERAL_INTERFACE,
    LITERAL_LONG,
    LITERAL_NATIVE,
    LITERAL_NEW,
    LITERAL_NULL,
    LITERAL_PRIVATE,
    LITERAL_PROTECTED,
    LITERAL_PUBLIC,
    LITERAL_RETURN,
    LITERAL_SHORT,
    LITERAL_STATIC,
    LITERAL_SUPER,
    LITERAL_SWITCH,
    LITERAL_SYNCHRONIZED,
    LITERAL_THIS,
    LITERAL_THROW,
    LITERAL_THROWS,
    LITERAL_TRANSIENT,
    LITERAL_TRUE,
    LITERAL_TRY,
    LITERAL_VOID,
    LITERAL_VOLATILE,
    LITERAL_WHILE,
    LNOT,
    LOR,
    LPAREN,
    LT,
    METHOD_CALL,
    METHOD_DEF,
    MINUS,
    MINUS_ASSIGN,
    MOD,
    MODIFIERS,
    MOD_ASSIGN,
    NOT_EQUAL,
    NUM_DOUBLE,
    NUM_FLOAT,
    NUM_INT,
    NUM_LONG,
    OBJBLOCK,
    PACKAGE_DEF,
    PARAMETERS,
    PARAMETER_DEF,
    PLUS,
    PLUS_ASSIGN,
    POST_DEC,
    POST_INC,
    QUESTION,
    RBRACK,
    RCURLY,
    RESOURCE,
    RESOURCES,
    RESOURCE_SPECIFICATION,
    RPAREN,
    SEMI,
    SL,
    SLIST,
    SL_ASSIGN,
    SR,
    SR_ASSIGN,
    STAR,
    STAR_ASSIGN,
    STATIC_IMPORT,
    STATIC_INIT,
    STRICTFP,
    STRING_LITERAL,
    SUPER_CTOR_CALL,
    TYPE,
    TYPECAST,
    TYPE_ARGUMENT,
    TYPE_ARGUMENTS,
    TYPE_EXTENSION_AND,
    TYPE_LOWER_BOUNDS,
    TYPE_PARAMETER,
    TYPE_PARAMETERS,
    TYPE_UPPER_BOUNDS,
    UNARY_MINUS,
    UNARY_PLUS,
    VARIABLE_DEF,
    WILDCARD_TYPE,

    // These three token types were added in CheckStyle version 5.9 (Java 8):
    METHOD_REF,
    DOUBLE_COLON,
    LAMBDA,

    // These three token types were added in CheckStyle version 6.0:
    //   SINGLE_LINE_COMMENT
    //   BLOCK_COMMENT_BEGIN
    //   BLOCK_COMMENT_END
    //   COMMENT_CONTENT

    // These token types were added between CheckStyle versions 8.21 and 8.45.1:
    COMPACT_CTOR_DEF,
    LITERAL_NON_SEALED,
    LITERAL_PERMITS,
    LITERAL_RECORD,
    LITERAL_SEALED,
    LITERAL_YIELD,
    PATTERN_VARIABLE_DEF,
    PERMITS_CLAUSE,
    RECORD_COMPONENT_DEF,
    RECORD_COMPONENTS,
    RECORD_DEF,
    SWITCH_RULE,
    TEXT_BLOCK_CONTENT,
    TEXT_BLOCK_LITERAL_BEGIN,
    TEXT_BLOCK_LITERAL_END,

    // All other CS tokens map to THIS LocalTokenType.
    UNKNOWN_TOKEN
    ;

    LocalTokenType() {

        String name = this.name();
        if ("UNKNOWN_TOKEN".equals(name)) {

            this.delocalized = -1;
        } else {

            // Find the corresponding constant in 'TokenTypes'.
            try {
                this.delocalized = (Integer) TokenTypes.class.getField(name).get(null);
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
    private final int delocalized;

    private static final Map<Integer, LocalTokenType> TO_LOCAL;
    static {
        Map<Integer, LocalTokenType> toLocal   = new HashMap<Integer, LocalTokenType>();

        for (LocalTokenType ltt : LocalTokenType.values()) {
            toLocal.put(ltt.delocalized, ltt);
        }

        TO_LOCAL = Collections.unmodifiableMap(toLocal);
    }

    /**
     * @return The {@link LocalTokenType} corresponding with the given {@link TokenTypes} constant
     */
    public static LocalTokenType
    localize(int tt) {
        LocalTokenType ltt = LocalTokenType.TO_LOCAL.get(tt);
        return ltt == null ? LocalTokenType.UNKNOWN_TOKEN : ltt;
    }

    /**
     * @return The {@link TokenTypes} corresponding with the given {@link LocalTokenType} constant, or -1
     *         iff {@code ltt} has no counterpart in this CheckStyle version
     */
    public int
    delocalize() { return this.delocalized; }

    /** @return The values of the constants declared in {@link TokenTypes} that map the given {@code ltts} */
    public static int[]
    delocalize(LocalTokenType[] ltts) {

        int[] tts = new int[ltts.length];
        for (int i = 0; i < tts.length; i++) {

            LocalTokenType ltt = ltts[i];
            assert ltt != null;
            tts[i] = ltt.delocalize();
        }

        return tts;
    }
}
