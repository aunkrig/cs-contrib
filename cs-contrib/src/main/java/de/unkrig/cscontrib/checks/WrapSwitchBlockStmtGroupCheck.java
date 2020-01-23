
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

import static de.unkrig.cscontrib.LocalTokenType.CASE_GROUP;
import static de.unkrig.cscontrib.LocalTokenType.COMMA;
import static de.unkrig.cscontrib.LocalTokenType.EXPR;
import static de.unkrig.cscontrib.LocalTokenType.RCURLY;
import static de.unkrig.cscontrib.LocalTokenType.SEMI;
import static de.unkrig.cscontrib.LocalTokenType.VARIABLE_DEF;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.compat.Cs820;
import de.unkrig.csdoclet.annotation.BooleanRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;

/**
 * Verifies that switch block statement groups are uniformly wrapped and indented.
 */
@Rule(
    group      = "%Whitespace.group",
    groupName  = "Whitespace",
    name       = "de.unkrig: Wrap switch block statement group",
    parent     = "TreeWalker",
    quickfixes = {
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent1",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent2",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent3",
	}
)
@NotNullByDefault(false) public
class WrapSwitchBlockStmtGroupCheck extends AbstractWrapCheck {

    // ============================================= BEGIN CONFIGURATION =============================================

    /**
     * Whether to allow a complete {@code SWITCH} block statement group in one single line. Example:
     * <pre>
     * case 1: case 2: a = 3; break;
     * </pre>
     */
    @BooleanRuleProperty(defaultValue = WrapSwitchBlockStmtGroupCheck.DEFAULT_ALLOW_ONE_LINE_SWITCH_BLOCK_STMT_GROUP)
    public void
    setAllowOneLineSwitchBlockStmtGroup(boolean value) { this.allowOneLineSwitchBlockStmtGroup = value; }

    private boolean
    allowOneLineSwitchBlockStmtGroup = WrapSwitchBlockStmtGroupCheck.DEFAULT_ALLOW_ONE_LINE_SWITCH_BLOCK_STMT_GROUP;

    private static final boolean
    DEFAULT_ALLOW_ONE_LINE_SWITCH_BLOCK_STMT_GROUP = true;

    // ============================================= END CONFIGURATION =============================================

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] { LocalTokenType.SLIST });
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    visitToken(DetailAST ast) {
        assert ast != null;

        if (Cs820.getType(Cs820.getParent(ast)) != CASE_GROUP.delocalize()) return;

        // Single-line case group?
        if (
            this.allowOneLineSwitchBlockStmtGroup
            && AbstractWrapCheck.isSingleLine(ast)
            && Cs820.getLineNo(Cs820.getParent(ast)) == Cs820.getLineNo(ast)
        ) return;

        // SUPPRESS CHECKSTYLE WrapMethod:10
        this.checkChildren(
            ast,
            LABEL1, FORK2, MAY_INDENT, EXPR, SEMI, BRANCH1,
            LABEL2, FORK5, MAY_INDENT, VARIABLE_DEF,
            LABEL3, FORK4, COMMA, VARIABLE_DEF, BRANCH3,
            LABEL4, SEMI, BRANCH1,
            // SLIST in CASE_GROUP ends _without_ an RCURLY!
            LABEL5, FORK6, END,
            LABEL6, FORK7, UNINDENT, RCURLY, END,
            LABEL7, MAY_INDENT, ANY, BRANCH1
        );
    }
}
