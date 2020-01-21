
/*
 * de.unkrig.cs-contrib - Additional checks, filters and quickfixes for CheckStyle and Eclipse-CS
 *
 * Copyright (c) 2013, Arno Unkrig
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

package de.unkrig.cscontrib.checks;

import static de.unkrig.cscontrib.LocalTokenType.*;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.csdoclet.annotation.BooleanRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.SingleSelectRuleProperty;

/**
 * Verifies that annotations and annotation type declarations are uniformly wrapped and indented.
 * <p>
 *   The phrase "wrap before X" means that a line break and spaces appear right before "X", such that "X" is vertically
 *   aligned with the first token in the immediately preceding line.
 * </p>
 */
@Rule(
    group      = "%Whitespace.group",
    groupName  = "Whitespace",
    name       = "de.unkrig: Wrap annotation",
    parent     = "TreeWalker"
//    quickfixes = { WrapAndIndent1.class, WrapAndIndent2.class, WrapAndIndent3.class } TODO
)
@NotNullByDefault(false) public
class WrapAnnotationCheck extends AbstractWrapCheck {

    // ============================================= BEGIN CONFIGURATION =============================================

    /**
     * Whether to allow a complete annotation declaration in one single line. Example:
     * <pre>
     * public &#64;interface MyAnno {}
     * </pre>
     */
    @BooleanRuleProperty(defaultValue = WrapAnnotationCheck.DEFAULT_ALLOW_ONE_LINE_DECL)
    public void
    setAllowOneLineDecl(boolean value) { this.allowOneLineDecl = value; }

    private boolean
    allowOneLineDecl = WrapAnnotationCheck.DEFAULT_ALLOW_ONE_LINE_DECL;

    private static final boolean
    DEFAULT_ALLOW_ONE_LINE_DECL = true;

    /**
     * Whether to wrap annotation declarations before "@". Example:
     * <pre>
     * private
     * &#64;interface MyAnno {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapAnnotationCheck.DEFAULT_WRAP_DECL_BEFORE_AT
    ) public void
    setWrapDeclBeforeAt(String value) { this.wrapDeclBeforeAt = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeAt = AbstractWrapCheck.toWrap(WrapAnnotationCheck.DEFAULT_WRAP_DECL_BEFORE_AT);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_AT = "always";

    /**
     * Whether to wrap annotation type declarations before the opening curly brace. Example:
     * <pre>
     * public interface &#64;MyAnnotationType
     * {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapAnnotationCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY
    ) public void
    setWrapDeclBeforeLCurly(String value) { this.wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(WrapAnnotationCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_LCURLY = "never";

    /**
     * Whether multiple annotations in one line are allowed. Example:
     * <pre>
     * &#64;Column &#64;NotNull
     * </pre>
     */
    @BooleanRuleProperty(defaultValue = WrapAnnotationCheck.DEFAULT_ALLOW_MULTIPLE_PER_LINE)
    public void
    setAllowMultiplePerLine(boolean value) { this.allowMultiplePerLine = value; }

    private boolean
    allowMultiplePerLine = WrapAnnotationCheck.DEFAULT_ALLOW_MULTIPLE_PER_LINE;

    private static final boolean
    DEFAULT_ALLOW_MULTIPLE_PER_LINE = false;

    /**
     * Whether to wrap element value array initializers before the opening curly brace. Example:
     * <pre>
     * &#64;SuppressWarnings(
     * { ...
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapAnnotationCheck.DEFAULT_WRAP_ELEMENT_VALUE_ARRAY_INITIALIZER_BEFORE_LCURLY
    ) public void
    setWrapElementValueArrayInitializerBeforeLCurly(String value) { this.wrapElementValueArrayInitializerBeforeLCurly = AbstractWrapCheck.toWrap(value); } // SUPPRESS CHECKSTYLE LineLength

    private Control
    wrapElementValueArrayInitializerBeforeLCurly = AbstractWrapCheck.toWrap(WrapAnnotationCheck.DEFAULT_WRAP_ELEMENT_VALUE_ARRAY_INITIALIZER_BEFORE_LCURLY); // SUPPRESS CHECKSTYLE LineLength

    private static final String
    DEFAULT_WRAP_ELEMENT_VALUE_ARRAY_INITIALIZER_BEFORE_LCURLY = "never";

    /**
     * Whether multiple element value array initializers in one line are allowed. Example:
     * <pre>
     * &#64;SuppressWarnings {
     *     "unchecked", "rawtypes"
     * }
     */
    @BooleanRuleProperty(defaultValue = WrapAnnotationCheck.DEFAULT_ALLOW_MULTIPLE_ELEMENT_VALUE_ARRAY_INITIALIZERS_PER_LINE) // SUPPRESS CHECKSTYLE LineLength
    public void
    setAllowMultipleElementValueArrayInitializersPerLine(boolean value) { this.allowMultipleElementValueArrayInitializersPerLine = value; } // SUPPRESS CHECKSTYLE LineLength

    private boolean
    allowMultipleElementValueArrayInitializersPerLine = WrapAnnotationCheck.DEFAULT_ALLOW_MULTIPLE_ELEMENT_VALUE_ARRAY_INITIALIZERS_PER_LINE; // SUPPRESS CHECKSTYLE LineLength

    private static final boolean
    DEFAULT_ALLOW_MULTIPLE_ELEMENT_VALUE_ARRAY_INITIALIZERS_PER_LINE = false;

    // ============================================= END CONFIGURATION =============================================

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] {
            LocalTokenType.ANNOTATION_DEF,
            LocalTokenType.ANNOTATION,
            LocalTokenType.ANNOTATION_MEMBER_VALUE_PAIR,
        });
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    visitToken(DetailAST ast) {
        assert ast != null;

        switch (LocalTokenType.localize(ast.getType())) {

        case ANNOTATION_DEF:
            if (this.allowOneLineDecl && AbstractWrapCheck.isSingleLine(ast)) return;

            // SUPPRESS CHECKSTYLE WrapMethod:6
            this.checkChildren(
                ast,
                MODIFIERS, this.wrapDeclBeforeAt, AT, LITERAL_INTERFACE, IDENT, this.wrapDeclBeforeLCurly, OBJBLOCK, END // SUPPRESS CHECKSTYLE LineLength
            );
            break;

        case ANNOTATION:
            // SUPPRESS CHECKSTYLE WrapMethod:12
            this.checkChildren(
                ast,
                AT, FORK1, DOT, BRANCH2,
                LABEL1, IDENT,
                LABEL2, FORK3, END,
                LABEL3, LPAREN, BRANCH5,
                LABEL4, COMMA,
                LABEL5, FORK6, MAY_INDENT, ANNOTATION_MEMBER_VALUE_PAIR, BRANCH9,
                LABEL6, FORK7, MAY_INDENT, ANNOTATION, BRANCH9,
                LABEL7, FORK8, MAY_INDENT, EXPR, BRANCH9,
                LABEL8, MAY_INDENT, this.wrapElementValueArrayInitializerBeforeLCurly, ANNOTATION_ARRAY_INIT,
                LABEL9, FORK4, UNINDENT, RPAREN, END
            );
            break;

        case ANNOTATION_MEMBER_VALUE_PAIR:
            // SUPPRESS CHECKSTYLE WrapMethod:6
            this.checkChildren(
                ast,
                IDENT, ASSIGN, FORK1, this.wrapElementValueArrayInitializerBeforeLCurly, ANNOTATION_ARRAY_INIT, END,
                LABEL1, ANY, END
            );
            break;

        default:
            throw new IllegalStateException(String.valueOf(ast));
        }
    }

    @Override protected boolean
    checkMultipleElementsPerLine(DetailAST child) {

        if (AstUtil.parentTypeIs(child, LocalTokenType.ANNOTATION) && !this.allowMultiplePerLine) return false;

        if (
            AstUtil.parentTypeIs(child, LocalTokenType.ANNOTATION_ARRAY_INIT)
            && !this.allowMultipleElementValueArrayInitializersPerLine
        ) return false;

        return true;
    }
}
