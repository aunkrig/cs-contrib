
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

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.checks.whitespace.WhitespaceAroundCheck;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.csdoclet.annotation.BooleanRuleProperty;
import de.unkrig.csdoclet.annotation.MultiCheckRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;

/**
 * Verifies that a token is surrounded by whitespace.
 * <p>
 *   <span style="color: red"><b>This check is superseded by {@code de.unkrig.Whitespace}.</b></span>
 * </p>
 * <p>
 *   Empty constructor bodies, method bodies, catch blocks and type bodies of the form
 * </p>
 * <pre>
 * public MyClass() {}           // empty constructor body
 *
 * public void func() {}         // empty method body
 *
 * public void func() {
 *     new Object() {
 *         // ...
 *     }.hashCode();             // No space between "}" and "." -- always allowed
 *     try {
 *         // ...
 *     } catch {}                // empty catch block
 * }
 *
 * interface MyInterface {}      // emtpy type body
 * </pre>
 * <p>
 *   may optionally be exempted from the policy using the allowEmptyMethods, allowEmptyConstructors, allowEmptyCatches
 *   and allowEmptyTypes properties.
 * </p>
 */
@Rule(
    group     = "%Whitespace.group",
    groupName = "Whitespace",
    name      = "de.unkrig: Whitespace around",
    parent    = "TreeWalker"
)
@NotNullByDefault(false) public
class WhitespaceAround extends WhitespaceAroundCheck {

    // CONFIGURATION SETTERS

    // SUPPRESS CHECKSTYLE LineLength:6
    /**
     * Tokens to check.
     */
    @MultiCheckRuleProperty(valueOptions = {
        "assign", "band", "band_assign", "bor", "bor_assign", "bsr", "bsr_assign", "bxor", "bxor_assign", "colon",
        "div", "div_assign", "equal", "ge", "gt", "land", "lcurly", "le", "literal_assert", "literal_catch",
        "literal_do", "literal_else", "literal_finally", "literal_for", "literal_if", "literal_return",
        "literal_synchronized", "literal_try", "literal_while", "lor", "lt", "minus", "minus_assign", "mod",
        "mod_assign", "not_equal", "plus", "plus_assign", "question", "rcurly", "sl", "slist", "sl_assign", "sr",
        "sr_assign", "star", "star_assign", "literal_assert", "type_extension_and", "wildcard_type"
    }, defaultValue = {
        "assign", "band", "band_assign", "bor", "bor_assign", "bsr", "bsr_assign", "bxor", "bxor_assign", "colon",
        "div", "div_assign", "equal", "ge", "gt", "land", "lcurly", "le", "literal_assert", "literal_catch",
        "literal_do", "literal_else", "literal_finally", "literal_for", "literal_if", "literal_return",
        "literal_synchronized", "literal_try", "literal_while", "lor", "lt", "minus", "minus_assign", "mod",
        "mod_assign", "not_equal", "plus", "plus_assign", "question", "rcurly", "sl", "slist", "sl_assign", "sr",
        "sr_assign", "star", "star_assign", "literal_assert", "type_extension_and", "wildcard_type"
    }) private void
    setTokens(int x) {}

    /**
     * Allow empty constructor bodies.
     */
    @BooleanRuleProperty(defaultValue = false)
    @Override public void
    setAllowEmptyConstructors(boolean value) { super.setAllowEmptyConstructors(value); }

    /**
     * Allow empty method bodies.
     */
    @BooleanRuleProperty(defaultValue = false)
    @Override public void
    setAllowEmptyMethods(boolean value) { super.setAllowEmptyMethods(value); }

    /**
     * Allow empty catch blocks.
     */
    @BooleanRuleProperty(defaultValue = false)
    @Override public void
    setAllowEmptyCatches(boolean value) { this.allowEmptyCatches = value; }
    private boolean allowEmptyCatches;

    /**
     * Allow empty class and interface bodies.
     */
    @BooleanRuleProperty(defaultValue = false)
    @Override  // CS 6 adds this method.
    public void
    setAllowEmptyTypes(boolean value) {
        super.setAllowEmptyTypes(value);
        this.allowEmptyTypes = value;
    }
    private boolean allowEmptyTypes;

    /**
     * Ignore the colon (":") token in enhanced {@code FOR} statements ("{@code for (x : y) ...}").
     */
    @BooleanRuleProperty(defaultValue = true)
    @Override public void
    setIgnoreEnhancedForColon(boolean value) { super.setIgnoreEnhancedForColon(value); }

    // END CONFIGURATION SETTERS

    @Override public void
    visitToken(DetailAST ast) {
        switch (LocalTokenType.localize(ast.getType())) {

        case LCURLY:

            // Conditionally allow empty type body.
            if (
                this.allowEmptyTypes
                && AstUtil.nextSiblingTypeIs(ast, LocalTokenType.RCURLY)
                && AstUtil.parentTypeIs(ast, LocalTokenType.OBJBLOCK)
                && AstUtil.grandParentTypeIs(
                    ast,
                    LocalTokenType.CLASS_DEF,
                    LocalTokenType.INTERFACE_DEF,
                    LocalTokenType.LITERAL_NEW,
                    LocalTokenType.ANNOTATION_DEF
                )
            ) return;
            break;

        case SLIST:

            // Conditionally allow empty catch block.
            if (
                this.allowEmptyCatches
                && AstUtil.parentTypeIs(ast, LocalTokenType.LITERAL_CATCH)
                && AstUtil.firstChildTypeIs(ast, LocalTokenType.RCURLY)
            ) return;
            break;

        case RCURLY:

            // Check for anonymous class instantiation; unconditionally allow "}.".
            if (
                AstUtil.parentTypeIs(ast, LocalTokenType.OBJBLOCK)
                && AstUtil.grandParentTypeIs(ast, LocalTokenType.LITERAL_NEW)
            ) return;

            // Conditionally allow empty catch block.
            if (
                this.allowEmptyCatches
                && AstUtil.parentTypeIs(ast, LocalTokenType.SLIST)
                && AstUtil.grandParentTypeIs(ast, LocalTokenType.LITERAL_CATCH)
            ) return;

            // Conditionally allow empty class or interface body.
            if (
                this.allowEmptyTypes
                && AstUtil.parentTypeIs(ast, LocalTokenType.OBJBLOCK)
                && AstUtil.grandParentTypeIs(
                    ast,
                    LocalTokenType.CLASS_DEF,
                    LocalTokenType.INTERFACE_DEF,
                    LocalTokenType.ANNOTATION_DEF
                )
                && AstUtil.previousSiblingTypeIs(ast, LocalTokenType.LCURLY)
            ) return;
            break;

        default:
            ;
            break;
        }

        // None of the exceptions apply; pass control to the "original" WhitespaceAroundCheck.
        super.visitToken(ast);
    }
}
