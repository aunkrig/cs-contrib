
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

import static de.unkrig.cscontrib.LocalTokenType.ENUM;
import static de.unkrig.cscontrib.LocalTokenType.IDENT;
import static de.unkrig.cscontrib.LocalTokenType.MODIFIERS;
import static de.unkrig.cscontrib.LocalTokenType.OBJBLOCK;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.END;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.csdoclet.annotation.BooleanRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.SingleSelectRuleProperty;

/**
 * Verifies that enum declarations and constants are uniformly wrapped and indented.
 * <p>
 *   The phrase "wrap before X" means that a line break and spaces appear right before "X", such that "X" is vertically
 *   aligned with the first token in the immediately preceding line.
 * </p>
 */
@Rule(
    group      = "%Whitespace.group",
    groupName  = "Whitespace",
    name       = "de.unkrig: Wrap enum",
    parent     = "TreeWalker",
    quickfixes = {
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent1",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent2",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent3",
	}
)
@NotNullByDefault(false) public
class WrapEnumCheck extends AbstractWrapCheck {

    // ============================================= BEGIN CONFIGURATION =============================================

    /**
     * Whether to allow a complete enum declaration in one single line. Example:
     * <pre>
     * private enum Color { BLACK, WHITE }
     * </pre>
     */
    @BooleanRuleProperty(defaultValue = WrapEnumCheck.DEFAULT_ALLOW_ONE_LINE_DECL)
    public void
    setAllowOneLineDecl(boolean value) { this.allowOneLineDecl = value; }

    private boolean
    allowOneLineDecl = WrapEnumCheck.DEFAULT_ALLOW_ONE_LINE_DECL;

    private static final boolean
    DEFAULT_ALLOW_ONE_LINE_DECL = true;

    /**
     * Whether to wrap enum declarations before the {@code ENUM} keyword. Example:
     * <pre>
     * protected
     * enum MyEnum {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapEnumCheck.DEFAULT_WRAP_DECL_BEFORE_ENUM
    ) public void
    setWrapDeclBeforeEnum(String value) { this.wrapDeclBeforeEnum = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeEnum = AbstractWrapCheck.toWrap(WrapEnumCheck.DEFAULT_WRAP_DECL_BEFORE_ENUM);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_ENUM = "always";

    /**
     * Whether to wrap enum declarations before the opening curly brace. Example:
     * <pre>
     * public enum MyEnum
     * {
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapEnumCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY
    ) public void
    setWrapDeclBeforeLCurly(String value) { this.wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapDeclBeforeLCurly = AbstractWrapCheck.toWrap(WrapEnumCheck.DEFAULT_WRAP_DECL_BEFORE_LCURLY);

    private static final String
    DEFAULT_WRAP_DECL_BEFORE_LCURLY = "never";


    /**
     * Whether multiple enum constant declarations in one line are allowed.
     */
    @BooleanRuleProperty(defaultValue = WrapEnumCheck.DEFAULT_ALLOW_MULTIPLE_CONSTANTS_PER_LINE)
    public void
    setAllowMultipleConstantsPerLine(boolean value) { this.allowMultipleConstantsPerLine = value; }

    private boolean
    allowMultipleConstantsPerLine = WrapEnumCheck.DEFAULT_ALLOW_MULTIPLE_CONSTANTS_PER_LINE;

    private static final boolean
    DEFAULT_ALLOW_MULTIPLE_CONSTANTS_PER_LINE = true;

    // ============================================= END CONFIGURATION =============================================

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] { LocalTokenType.ENUM_DEF });
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
            MODIFIERS, this.wrapDeclBeforeEnum, ENUM, IDENT, this.wrapDeclBeforeLCurly, OBJBLOCK, END
        );
    }

    @Override protected boolean
    checkMultipleElementsPerLine(DetailAST child) {

        return this.allowMultipleConstantsPerLine || !AstUtil.grandParentTypeIs(child, LocalTokenType.ENUM_DEF);
    }
}
