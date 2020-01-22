
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

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.csdoclet.annotation.Message;
import de.unkrig.csdoclet.annotation.Rule;

/**
 * Assignments in expressions must be parenthesized, like "a = (b = c)" or "while ((a = b))".
 * <p>
 *   An enhanced version of "InnerAssignment": It comes with a quickfix for ECLIPSE-CS.
 * </p>
 */
@Rule(
    group      = "%Coding.group",
    groupName  = "Coding Problems",
    name       = "de.unkrig: Inner assignment",
    parent     = "TreeWalker",
    quickfixes = "de.unkrig.cscontrib.ui.quickfixes.InnerAssignment"
) @NotNullByDefault(false) public
class InnerAssignment extends AbstractCheck {

    @Message("Assignments in expressions must be parenthesized")
    private static final String
    MESSAGE_KEY_MUST_PARENTHESIZE = "InnerAssignment.mustParenthesize";

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] {
            LocalTokenType.ASSIGN,       // "="
            LocalTokenType.DIV_ASSIGN,   // "/="
            LocalTokenType.PLUS_ASSIGN,  // "+="
            LocalTokenType.MINUS_ASSIGN, // "-="
            LocalTokenType.STAR_ASSIGN,  // "*="
            LocalTokenType.MOD_ASSIGN,   // "%="
            LocalTokenType.SR_ASSIGN,    // ">>="
            LocalTokenType.BSR_ASSIGN,   // ">>>="
            LocalTokenType.SL_ASSIGN,    // "<<="
            LocalTokenType.BXOR_ASSIGN,  // "^="
            LocalTokenType.BOR_ASSIGN,   // "|="
            LocalTokenType.BAND_ASSIGN,  // "&="
        });
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    visitToken(DetailAST ast) {
        assert ast != null;

        // Field or variable initializer?
        if (AstUtil.parentTypeIs(ast, LocalTokenType.VARIABLE_DEF)) return; // int a = 3;

        // Try-with-resource?
        if (AstUtil.parentTypeIs(ast, LocalTokenType.RESOURCE)) return; // try { InputStream is = new ... }

        // Assignment statement?
        if (AstUtil.parentTypeIs(ast, LocalTokenType.EXPR) && (
            AstUtil.grandParentTypeIs(ast, LocalTokenType.SLIST)           // { ... a = b
            || AstUtil.previousUncleTypeIs(ast, LocalTokenType.RPAREN)     // if (...) a = b
            || AstUtil.grandParentTypeIs(ast, LocalTokenType.LITERAL_ELSE) // if (...) {...} else a = b
            || (                                                           // for (...; ...; a += b)
                AstUtil.grandParentTypeIs(ast, LocalTokenType.ELIST)
                && AstUtil.grandGrandParentTypeIs(ast, LocalTokenType.FOR_ITERATOR)
            )
            || (                                                           // for (a = b; ...; ...)
                AstUtil.grandParentTypeIs(ast, LocalTokenType.ELIST)
                && AstUtil.grandGrandParentTypeIs(ast, LocalTokenType.FOR_INIT)
            )
        )) return;

        // For iterator?
        if (
            AstUtil.parentTypeIs(ast, LocalTokenType.EXPR)
            && AstUtil.grandParentTypeIs(ast, LocalTokenType.ELIST)
            && AstUtil.grandGrandParentTypeIs(ast, LocalTokenType.FOR_ITERATOR)
        ) return; // for (...; ...; a += b)

        // Parenthesized assignment?
        if (ast.getPreviousSibling() != null && AstUtil.previousSiblingTypeIs(ast, LocalTokenType.LPAREN)) return;

        // Annotation member-value pair?
        if (AstUtil.parentTypeIs(ast, LocalTokenType.ANNOTATION_MEMBER_VALUE_PAIR)) return;

        this.log(ast.getLineNo(), ast.getColumnNo(), InnerAssignment.MESSAGE_KEY_MUST_PARENTHESIZE);
    }
}
