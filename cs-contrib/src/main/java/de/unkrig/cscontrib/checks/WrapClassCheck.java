
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

import static de.unkrig.cscontrib.LocalTokenType.CLASS_DEF;
import static de.unkrig.cscontrib.LocalTokenType.EXTENDS_CLAUSE;
import static de.unkrig.cscontrib.LocalTokenType.IDENT;
import static de.unkrig.cscontrib.LocalTokenType.IMPLEMENTS_CLAUSE;
import static de.unkrig.cscontrib.LocalTokenType.LITERAL_CLASS;
import static de.unkrig.cscontrib.LocalTokenType.MODIFIERS;
import static de.unkrig.cscontrib.LocalTokenType.OBJBLOCK;
import static de.unkrig.cscontrib.LocalTokenType.TYPE_PARAMETERS;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.END;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK1;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK2;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK3;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL1;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL2;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL3;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.MAY_WRAP;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.csdoclet.annotation.BooleanRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.SingleSelectRuleProperty;

/**
 * Verifies that class declarations are uniformly wrapped and indented.
 * <p>
 *   The phrase "wrap before X" means that a line break and spaces appear right before "X", such that "X" is vertically
 *   aligned with the first token in the immediately preceding line.
 * </p>
 */
@Rule(
    group      = "%Whitespace.group",
    groupName  = "Whitespace",
    name       = "de.unkrig: Wrap class",
    parent     = "TreeWalker",
    quickfixes = {
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent1",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent2",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent3",
	}
)
@NotNullByDefault(false) public
class WrapClassCheck extends AbstractWrapCheck {

    // ============================================= BEGIN CONFIGURATION =============================================

    /**
     * Whether to allow a complete class declaration in one single line. Example:
     * <pre>
     * public class Pojo { int fld; }
     * </pre>
     */
    @BooleanRuleProperty(defaultValue = WrapClassCheck.DEFAULT_ALLOW_ONE_LINE_DECL)
    public void
    setAllowOneLineDecl(boolean value) { this.allowOneLineDecl = value; }

    private boolean
    allowOneLineDecl = WrapClassCheck.DEFAULT_ALLOW_ONE_LINE_DECL;

    private static final boolean
    DEFAULT_ALLOW_ONE_LINE_DECL = true;

    /**
     * Whether to wrap class declarations before the {@code CLASS} keyword. Example:
     * <pre>
     * public static final
     * class MyClass {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapClassCheck.DEFAULT_WRAP_DECL_BEFORE_CLASS
    ) public void
    setWrapDeclBeforeClass(String value) { this.wrapDeclBeforeClass = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeClass = AbstractWrapCheck.toWrap(WrapClassCheck.DEFAULT_WRAP_DECL_BEFORE_CLASS);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_CLASS = "always";

    /**
     * Whether to wrap class declarations before the opening curly brace. Example:
     * <pre>
     * public class MyClass
     * {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapClassCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY
    ) public void
    setWrapDeclBeforeLCurly(String value) { this.wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(WrapClassCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_LCURLY = "never";

    // ============================================= END CONFIGURATION =============================================

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] { CLASS_DEF });
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
            MODIFIERS, this.wrapDeclBeforeClass, LITERAL_CLASS, IDENT, FORK1, TYPE_PARAMETERS,
            LABEL1, FORK2, MAY_WRAP, EXTENDS_CLAUSE,
            LABEL2, FORK3, MAY_WRAP, IMPLEMENTS_CLAUSE,
            LABEL3, this.wrapDeclBeforeLCurly, OBJBLOCK, END
        );
    }
}
