
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

import static de.unkrig.cscontrib.LocalTokenType.IDENT;
import static de.unkrig.cscontrib.LocalTokenType.LITERAL_THROWS;
import static de.unkrig.cscontrib.LocalTokenType.LPAREN;
import static de.unkrig.cscontrib.LocalTokenType.MODIFIERS;
import static de.unkrig.cscontrib.LocalTokenType.PARAMETERS;
import static de.unkrig.cscontrib.LocalTokenType.RPAREN;
import static de.unkrig.cscontrib.LocalTokenType.SLIST;
import static de.unkrig.cscontrib.LocalTokenType.TYPE_PARAMETERS;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.END;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK1;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK2;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.INDENT_IF_CHILDREN;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL1;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL2;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.MAY_WRAP;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.UNINDENT;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.csdoclet.annotation.BooleanRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.SingleSelectRuleProperty;

/**
 * Verifies that constructor declarations, parameters and arguments are uniformly wrapped and indented.
 * <p>
 *   The phrase "wrap before X" means that a line break and spaces appear right before "X", such that "X" is vertically
 *   aligned with the first token in the immediately preceding line.
 * </p>
 */
@Rule(
    group      = "%Whitespace.group",
    groupName  = "Whitespace",
    name       = "de.unkrig: Wrap constructor",
    parent     = "TreeWalker"
//    quickfixes = { WrapAndIndent1.class, WrapAndIndent2.class, WrapAndIndent3.class } TODO
)
@NotNullByDefault(false) public
class WrapCtorCheck extends AbstractWrapCheck {

    // ============================================= BEGIN CONFIGURATION =============================================

    /**
     * Whether to allow a complete constructor declaration in one single line. Example:
     * <pre>
     * protected MyClass() { super(null); }
     * </pre>
     */
    @BooleanRuleProperty(defaultValue = WrapCtorCheck.DEFAULT_ALLOW_ONE_LINE_DECL)
    public void
    setAllowOneLineDecl(boolean value) { this.allowOneLineDecl = value; }

    private boolean
    allowOneLineDecl = WrapCtorCheck.DEFAULT_ALLOW_ONE_LINE_DECL;

    private static final boolean
    DEFAULT_ALLOW_ONE_LINE_DECL = true;

    /**
     * Whether to wrap constructor declarations between the modifiers and the class name. Example:
     * <pre>
     * protected
     * MyClass(int x) {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapCtorCheck.DEFAULT_WRAP_DECL_BEFORE_NAME
    ) public void
    setWrapDeclBeforeName(String value) { this.wrapDeclBeforeName = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeName = AbstractWrapCheck.toWrap(WrapCtorCheck.DEFAULT_WRAP_DECL_BEFORE_NAME);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_NAME = "always";

    /**
     * Whether to wrap constructor declarations before the opening curly brace. Example:
     * <pre>
     * protected MyClass(int x)
     * {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapCtorCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY
    ) public void
    setWrapDeclBeforeLCurly(String value) { this.wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(WrapCtorCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_LCURLY = "never";

    /**
     * Whether multiple constructor call arguments in one line are allowed.
     */
    @BooleanRuleProperty(defaultValue = WrapCtorCheck.DEFAULT_ALLOW_MULTIPLE_ARGS_PER_LINE)
    public void
    setAllowMultipleArgsPerLine(boolean value) { this.allowMultipleArgsPerLine = value; }

    private boolean
    allowMultipleArgsPerLine = WrapCtorCheck.DEFAULT_ALLOW_MULTIPLE_ARGS_PER_LINE;

    private static final boolean
    DEFAULT_ALLOW_MULTIPLE_ARGS_PER_LINE = false;

    /**
     * Whether multiple constructor parameter declarations in one line are allowed.
     */
    @BooleanRuleProperty(defaultValue = WrapCtorCheck.DEFAULT_ALLOW_MULTIPLE_PARAMETERS_PER_LINE)
    public void
    setAllowMultipleParametersPerLine(boolean value) { this.allowMultipleParametersPerLine = value; }

    private boolean
    allowMultipleParametersPerLine = WrapCtorCheck.DEFAULT_ALLOW_MULTIPLE_PARAMETERS_PER_LINE;

    private static final boolean
    DEFAULT_ALLOW_MULTIPLE_PARAMETERS_PER_LINE = false;

    // ============================================= END CONFIGURATION =============================================

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] { LocalTokenType.CTOR_DEF });
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    visitToken(DetailAST ast) {
        assert ast != null;

        if (this.allowOneLineDecl && AbstractWrapCheck.isSingleLine(ast)) return;

        // SUPPRESS CHECKSTYLE WrapMethod:6
        this.checkChildren(
            ast,
            MODIFIERS, FORK1, TYPE_PARAMETERS,
            LABEL1, this.wrapDeclBeforeName, IDENT, LPAREN, INDENT_IF_CHILDREN, PARAMETERS, UNINDENT, RPAREN, FORK2, MAY_WRAP, LITERAL_THROWS, // SUPPRESS CHECKSTYLE LineLength
            LABEL2, this.wrapDeclBeforeLCurly, SLIST, END
        );
    }

    @Override protected boolean
    checkMultipleElementsPerLine(DetailAST child) {

        if (
            AstUtil.grandParentTypeIs(child, LocalTokenType.CTOR_CALL, LocalTokenType.SUPER_CTOR_CALL) // SUPPRESS CHECKSTYLE LineLength
            && !this.allowMultipleArgsPerLine
        ) return false;

        if (
            AstUtil.parentTypeIs(child, LocalTokenType.PARAMETERS)
            && AstUtil.grandParentTypeIs(child, LocalTokenType.CTOR_DEF)
            && !this.allowMultipleParametersPerLine
        ) return false;

        return true;
    }
}
