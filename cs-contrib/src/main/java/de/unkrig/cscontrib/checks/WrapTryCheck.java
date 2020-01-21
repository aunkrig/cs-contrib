
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

// Disable "WrapMethod" because we want multiple arguments per line in the "checkChildren()" calls:
// SUPPRESS CHECKSTYLE WrapMethod:9999

package de.unkrig.cscontrib.checks;

import static de.unkrig.cscontrib.LocalTokenType.LITERAL_CATCH;
import static de.unkrig.cscontrib.LocalTokenType.LITERAL_FINALLY;
import static de.unkrig.cscontrib.LocalTokenType.LPAREN;
import static de.unkrig.cscontrib.LocalTokenType.RESOURCES;
import static de.unkrig.cscontrib.LocalTokenType.RPAREN;
import static de.unkrig.cscontrib.LocalTokenType.SLIST;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.END;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK1;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK2;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK3;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.FORK4;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL1;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL2;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL3;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.LABEL4;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.SingleSelectRuleProperty;

/**
 * Verifies that TRY statements are uniformly wrapped and indented.
 * <p>
 *   The phrase "wrap before X" means that a line break and spaces appear right before "X", such that "X" is vertically
 *   aligned with the first token in the immediately preceding line.
 * </p>
 */
@Rule(
    group      = "%Whitespace.group",
    groupName  = "Whitespace",
    name       = "de.unkrig: Wrap TRY statement",
    parent     = "TreeWalker"
//    quickfixes = { WrapAndIndent1.class, WrapAndIndent2.class, WrapAndIndent3.class } TODO
)
@NotNullByDefault(false) public
class WrapTryCheck extends AbstractWrapCheck {

    // ============================================= BEGIN CONFIGURATION =============================================

    /**
     * Whether to wrap {@code TRY}-with-resources statements before the opening parenthesis of the resource
     * specification. Example:
     * <pre>
     * try
     * (...) { ... }
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapTryCheck.DEFAULT_WRAP_BEFORE_RESOURCE_SPECIFICATION
    ) public void
    setWrapBeforeResourceSpecification(String value) {
        this.wrapBeforeResourceSpecification = AbstractWrapCheck.toWrap(value);
    }

    private Control
    wrapBeforeResourceSpecification = AbstractWrapCheck.toWrap(WrapTryCheck.DEFAULT_WRAP_BEFORE_RESOURCE_SPECIFICATION);

    private static final String
    DEFAULT_WRAP_BEFORE_RESOURCE_SPECIFICATION = "never";

    /**
     * Whether to wrap {@code TRY} statements before the {@code CATCH} keyword. Example:
     * <pre>
     * try { ... }
     * catch { ... }
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapTryCheck.DEFAULT_WRAP_BEFORE_CATCH
    ) public void
    setWrapBeforeCatch(String value) { this.wrapBeforeCatch = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapBeforeCatch = AbstractWrapCheck.toWrap(WrapTryCheck.DEFAULT_WRAP_BEFORE_CATCH);

    private static final String
    DEFAULT_WRAP_BEFORE_CATCH = "optional";

    /**
     * Whether to wrap {@code TRY} statements before the body. Example:
     * <pre>
     * try
     * { ... } // &lt;- The body starts with '{'.
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapTryCheck.DEFAULT_WRAP_BEFORE_BODY
    ) public void
    setWrapBeforeBody(String value) { this.wrapBeforeBody = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapBeforeBody = AbstractWrapCheck.toWrap(WrapTryCheck.DEFAULT_WRAP_BEFORE_BODY);

    private static final String
    DEFAULT_WRAP_BEFORE_BODY = "never";

    /**
     * Whether to wrap {@code TRY} statements before the {@code FINALLY} keyword. Example:
     * <pre>
     * try { ... }
     * finally { ... }
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapTryCheck.DEFAULT_WRAP_BEFORE_FINALLY
    ) public void
    setWrapBeforeFinally(String value) { this.wrapBeforeFinally = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapBeforeFinally = AbstractWrapCheck.toWrap(WrapTryCheck.DEFAULT_WRAP_BEFORE_FINALLY);

    private static final String
    DEFAULT_WRAP_BEFORE_FINALLY = "optional";

    // ============================================= END CONFIGURATION =============================================

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(
            new LocalTokenType[] { LocalTokenType.LITERAL_TRY, LocalTokenType.RESOURCE_SPECIFICATION }
        );
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    visitToken(DetailAST ast) {
        assert ast != null;

        switch (LocalTokenType.localize(ast.getType())) {

        //  TRY -> ...
        //   v
        //  [ RESOURCE_SPECIFICATION -> ] SLIST -> { LITERAL_CATCH -> } [ LITERAL_FINALLY -> ] END
        case LITERAL_TRY:
            // SUPPRESS CHECKSTYLE WrapMethod:8
            this.checkChildren(
                ast,
                FORK1,
                this.wrapBeforeResourceSpecification, LocalTokenType.RESOURCE_SPECIFICATION,
                LABEL1, this.wrapBeforeBody, SLIST, FORK3,
                LABEL2, this.wrapBeforeCatch, LITERAL_CATCH, FORK2, FORK4,
                LABEL3, this.wrapBeforeFinally, LITERAL_FINALLY,
                LABEL4, END
            );
            break;

        case RESOURCE_SPECIFICATION:
            this.checkChildren(
                ast,
                LPAREN, RESOURCES, RPAREN, // SUPPRESS CHECKSTYLE WrapMethod
                END
            );
            break;

        default:
            throw new IllegalStateException(Integer.toString(ast.getType()));
        }
    }
}
