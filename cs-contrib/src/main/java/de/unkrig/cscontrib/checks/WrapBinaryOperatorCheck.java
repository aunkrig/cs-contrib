
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

import static de.unkrig.cscontrib.LocalTokenType.COLON;
import static de.unkrig.cscontrib.LocalTokenType.LPAREN;
import static de.unkrig.cscontrib.LocalTokenType.QUESTION;
import static de.unkrig.cscontrib.LocalTokenType.RBRACK;
import static de.unkrig.cscontrib.LocalTokenType.RPAREN;
import static de.unkrig.cscontrib.LocalTokenType.TYPE_ARGUMENTS;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.compat.Cs820;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.SingleSelectRuleProperty;

/**
 * Verifies that binary operations are uniformly wrapped before and/or after the operator.
 * <p>
 *   The phrase "wrap before X" means that a line break and spaces appear right before "X", such that "X" is vertically
 *   aligned with the first token in the immediately preceding line.
 * </p>
 */
@Rule(
    group      = "%Whitespace.group",
    groupName  = "Whitespace",
    name       = "de.unkrig: Wrap binary operator",
    parent     = "TreeWalker",
    quickfixes = {
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent1",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent2",
		"de.unkrig.cscontrib.ui.quickfixes.WrapAndIndent3",
	}
)
@NotNullByDefault(false) public
class WrapBinaryOperatorCheck extends AbstractWrapCheck {

    // ============================================= BEGIN CONFIGURATION =============================================

    /**
     * Whether to wrap expressions before a binary operator. Example:
     * <pre>
     * a
     * + b
     * + c
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapBinaryOperatorCheck.DEFAULT_WRAP_BEFORE_OPERATOR
    ) public void
    setWrapBeforeOperator(String value) { this.wrapBeforeOperator = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapBeforeOperator = AbstractWrapCheck.toWrap(WrapBinaryOperatorCheck.DEFAULT_WRAP_BEFORE_OPERATOR);

    private static final String
    DEFAULT_WRAP_BEFORE_OPERATOR = "optional";

    /**
     * Whether to wrap expressions after a binary operator. Example:
     * <pre>
     * a +
     * b +
     * c
     * </pre>
     */
    @SingleSelectRuleProperty(
        optionProvider = Wrap.class,
        defaultValue   = WrapBinaryOperatorCheck.DEFAULT_WRAP_AFTER_OPERATOR
    ) public void
    setWrapAfterOperator(String value) { this.wrapAfterOperator = AbstractWrapCheck.toWrap(value); }

    private Control
    wrapAfterOperator = AbstractWrapCheck.toWrap(WrapBinaryOperatorCheck.DEFAULT_WRAP_AFTER_OPERATOR);

    private static final String
    DEFAULT_WRAP_AFTER_OPERATOR = "never";

    // ============================================= END CONFIGURATION =============================================

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] { LocalTokenType.EXPR });
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    visitToken(DetailAST ast) {
        assert ast != null;

        DetailAST child = Cs820.getFirstChild(ast);
        if (Cs820.getType(child) == LPAREN.delocalize()) {
            child = this.checkParenthesizedExpression(child, false);
            assert child == null;
        } else {
            boolean inline;
            switch (LocalTokenType.localize(Cs820.getType(Cs820.getParent(ast)))) {

            case INDEX_OP:                     // a[#]
            case ANNOTATION:                   // @SuppressWarnings(#)
            case ANNOTATION_ARRAY_INIT:        // @SuppressWarnings({ "rawtypes", "unchecked" })
            case ANNOTATION_MEMBER_VALUE_PAIR: // @Author(@Name(first = "Joe", last = "Hacker"))
            case ASSIGN:                       // a = #
            case FOR_CONDITION:                // for (; #;)
            case FOR_EACH_CLAUSE:              // for (Object o : #)
            case LITERAL_ASSERT:               // assert #
            case LITERAL_CASE:                 // case #:
            case LITERAL_DEFAULT:              // @interface MyAnnotation { boolean value() default true; }
            case LITERAL_ELSE:                 // else #;
            case LITERAL_FOR:                  // for (...; ...; ...) #;
            case LITERAL_RETURN:               // return #
            case LITERAL_THROW:                // throw #
            case SLIST:                        // #;
                inline = true;
                break;

            case ARRAY_DECLARATOR:     // new String[#]
            case ARRAY_INIT:           // int[] a = { # }
            case LITERAL_DO:           // do { ... } while (#)
            case LITERAL_IF:           // if (#)
            case LITERAL_SWITCH:       // switch (#)
            case LITERAL_SYNCHRONIZED: // synchronized (#)
            case LITERAL_WHILE:        // while (#)
                inline = Cs820.getLineNo(Cs820.getParent(ast)) == Cs820.getLineNo(ast);
                break;

            case ELIST:                // meth(#, #, #)
                inline = Cs820.getChildCount(Cs820.getParent(ast)) != 1;
                break;

            default:
                assert false : (
                    this.getFileContents().getFileName()
                    + ":"
                    + Cs820.getLineNo(ast)
                    + ": EXPR has unexpected parent "
                    + LocalTokenType.localize(Cs820.getType(Cs820.getParent(ast)))
                );
                inline = false;
                break;
            }
            this.checkExpression(child, inline);
        }
    }

    /**
     * @return The {@link DetailAST} <b>after</b> the parenthesized expression
     */
    private DetailAST
    checkParenthesizedExpression(DetailAST previous, boolean inline) {

        if (Cs820.getType(previous) != LPAREN.delocalize()) {
            this.checkExpression(previous, inline);
            return Cs820.getNextSibling(previous);
        }

        @SuppressWarnings("unused") AstDumper dumper = new AstDumper(previous); // For debugging

        DetailAST next = Cs820.getNextSibling(previous);
        for (;;) {
            if (Cs820.getType(next) != LPAREN.delocalize()) {
                break;
            }
            this.checkSameLine(previous, next);
            previous = next;
            next     = Cs820.getNextSibling(next);
        }

        if (Cs820.getLineNo(previous) == Cs820.getLineNo(AbstractWrapCheck.getLeftmostDescendant(next))) {
            this.checkExpression(next, true);
            previous = next;
            next     = Cs820.getNextSibling(next);
            this.checkSameLine(AbstractWrapCheck.getRightmostDescendant(previous), next);
        } else {
            this.checkIndented(previous, AbstractWrapCheck.getLeftmostDescendant(next));
            this.checkExpression(next, false);
            previous = next;
            next     = Cs820.getNextSibling(next);
            this.checkUnindented(AbstractWrapCheck.getRightmostDescendant(previous), next);
        }

        previous = next;
        assert Cs820.getType(next) == RPAREN.delocalize();
        return Cs820.getNextSibling(next);
    }

    /**
     * @param inline Iff {@code true}, then the entire expression must appear on one line.
     */
    private void
    checkExpression(DetailAST expression, boolean inline) {

        if (Cs820.getType(expression) == QUESTION.delocalize()) {
            System.currentTimeMillis();
        }
        switch (LocalTokenType.localize(Cs820.getType(expression))) {

        case QUESTION: // Ternary operation.
            {
                DetailAST c = this.checkParenthesizedExpression(Cs820.getFirstChild(expression), inline);
                c = this.checkParenthesizedExpression(c, inline);
                assert Cs820.getType(c) == COLON.delocalize();
                c = Cs820.getNextSibling(c);
                c = this.checkParenthesizedExpression(c, inline);
                assert c == null;
            }
            break;

        case INDEX_OP:
            {
                DetailAST c = this.checkParenthesizedExpression(Cs820.getFirstChild(expression), inline);
                assert c != null;
                this.checkSameLine(AbstractWrapCheck.getRightmostDescendant(Cs820.getFirstChild(expression)), expression);
                this.checkSameLine(expression, AbstractWrapCheck.getLeftmostDescendant(c));
                c = this.checkParenthesizedExpression(c, inline);
                assert c != null;
                assert Cs820.getType(c) == RBRACK.delocalize();
                this.checkSameLine(expression, c);
            }
            break;

        // Binary operations
        case ASSIGN:
        case BAND:
        case BAND_ASSIGN:
        case BOR:
        case BOR_ASSIGN:
        case BSR:
        case BSR_ASSIGN:
        case BXOR:
        case BXOR_ASSIGN:
        case DIV:
        case DIV_ASSIGN:
        case DOT:
        case EQUAL:
        case GE:
        case GT:
        case LAND:
        case LITERAL_INSTANCEOF:
        case LOR:
        case LE:
        case LT:
        case MINUS:
        case MINUS_ASSIGN:
        case MOD:
        case MOD_ASSIGN:
        case NOT_EQUAL:
        case PLUS:
        case PLUS_ASSIGN:
        case SL:
        case SL_ASSIGN:
        case SR:
        case SR_ASSIGN:
        case STAR:
        case STAR_ASSIGN:
            {
                DetailAST c = this.checkParenthesizedExpression(Cs820.getFirstChild(expression), inline);
                if (c != null && Cs820.getType(c) == TYPE_ARGUMENTS.delocalize()) {

                    // TYPE_ARGUMENTS checked by "visitToken()".
                    ;
                    c = Cs820.getNextSibling(c);
                }
                assert c != null : (
                    this.getFileContents().getFileName()
                    + ":"
                    + Cs820.getLineNo(expression)
                    + ": Second operand for '"
                    + LocalTokenType.localize(Cs820.getType(expression))
                    + "' missing"
                );

                // Check wrapping and alignment of LHS and operator.
                {
                    DetailAST lhs = AbstractWrapCheck.getRightmostDescendant(Cs820.getPreviousSibling(c));
                    switch (inline ? Control.NO_WRAP : this.wrapBeforeOperator) {

                    case NO_WRAP:
                        this.checkSameLine(lhs, expression);
                        break;

                    case MAY_WRAP:
                        if (Cs820.getLineNo(lhs) != Cs820.getLineNo(expression)) {
                            this.checkWrapped(
                                AbstractWrapCheck.getLeftmostDescendant(Cs820.getFirstChild(expression)),
                                expression
                            );
                        } else {
                            this.checkSameLine(lhs, expression);
                        }
                        break;

                    case MUST_WRAP:
                        this.checkWrapped(lhs, AbstractWrapCheck.getLeftmostDescendant(Cs820.getFirstChild(expression)));
                        if (Cs820.getLineNo(lhs) == Cs820.getLineNo(expression)) {
                            this.log(
                                expression,
                                AbstractWrapCheck.MESSAGE_KEY_MUST_WRAP,
                                Cs820.getText(lhs),
                                Cs820.getText(expression)
                            );
                        } else {
                            this.checkWrapped(
                                AbstractWrapCheck.getLeftmostDescendant(Cs820.getFirstChild(expression)),
                                expression
                            );
                        }
                        break;

                    default:
                        throw new IllegalStateException();
                    }
                }

                // Check wrapping and alignment of operator and RHS.
                {
                    DetailAST rhs = AbstractWrapCheck.getLeftmostDescendant(c);
                    switch (inline ? Control.NO_WRAP : this.wrapAfterOperator) {

                    case NO_WRAP:
                        this.checkSameLine(expression, rhs);
                        break;

                    case MAY_WRAP:
                        if (Cs820.getLineNo(expression) != Cs820.getLineNo(rhs)) {
                            this.checkWrapped(AbstractWrapCheck.getLeftmostDescendant(Cs820.getFirstChild(expression)), rhs);
                        } else {
                            this.checkSameLine(expression, rhs);
                        }
                        break;

                    case MUST_WRAP:
                        if (Cs820.getLineNo(expression) == Cs820.getLineNo(rhs)) {
                            this.log(
                                rhs,
                                AbstractWrapCheck.MESSAGE_KEY_MUST_WRAP,
                                Cs820.getText(expression),
                                Cs820.getText(rhs)
                            );
                        } else {
                            this.checkWrapped(AbstractWrapCheck.getLeftmostDescendant(Cs820.getFirstChild(expression)), rhs);
                        }
                        break;

                    default:
                        throw new IllegalStateException();
                    }
                }

                c = this.checkParenthesizedExpression(c, inline);
                assert c == null : (
                    this.getFileContents().getFileName()
                    + ":"
                    + Cs820.getLineNo(expression)
                    + ": Unexpected third operand "
                    + LocalTokenType.localize(Cs820.getType(c))
                    + "/'"
                    + Cs820.getText(c)
                    + "' for '"
                    + LocalTokenType.localize(Cs820.getType(expression))
                    + "'"
                );
            }
            break;

        // Unary operations
        case BNOT:
        case DEC:
        case EXPR:
        case INC:
        case LNOT:
        case POST_DEC:
        case POST_INC:
        case UNARY_MINUS:
        case UNARY_PLUS:
            {
                DetailAST c = this.checkParenthesizedExpression(Cs820.getFirstChild(expression), inline);
                assert c == null;
            }
            break;

        case ARRAY_DECLARATOR:
            {
                DetailAST c = this.checkParenthesizedExpression(Cs820.getFirstChild(expression), inline);
                assert Cs820.getType(c) == RBRACK.delocalize();
            }
            break;

        case CHAR_LITERAL:
        case IDENT:
        case LITERAL_CLASS:
        case LITERAL_FALSE:
        case LITERAL_NULL:
        case LITERAL_SUPER:
        case LITERAL_THIS:
        case LITERAL_TRUE:
        case NUM_DOUBLE:
        case NUM_FLOAT:
        case NUM_INT:
        case NUM_LONG:
        case STRING_LITERAL:
        case LITERAL_BOOLEAN:
        case LITERAL_BYTE:
        case LITERAL_SHORT:
        case LITERAL_INT:
        case LITERAL_LONG:
        case LITERAL_CHAR:
        case LITERAL_FLOAT:
        case LITERAL_DOUBLE:
        case LITERAL_VOID:
            {
                DetailAST c = Cs820.getFirstChild(expression);
                assert c == null : Integer.toString(Cs820.getChildCount(expression));
            }
            break;

        case TYPE:
            break;

        case METHOD_CALL:
            {
                DetailAST method = Cs820.getFirstChild(expression); // Everything up to and including the method name.
                this.checkExpression(method, inline);
                this.checkSameLine(method, expression);

                DetailAST arguments = Cs820.getNextSibling(method);
                DetailAST rparen    = Cs820.getNextSibling(arguments);

                assert Cs820.getType(rparen) == RPAREN.delocalize();
                assert Cs820.getNextSibling(rparen) == null;

                DetailAST firstArgument = Cs820.getFirstChild(arguments);
                if (
                    firstArgument == null
                    || Cs820.getLineNo(AbstractWrapCheck.getLeftmostDescendant(firstArgument)) == Cs820.getLineNo(expression)
                ) {
                    this.checkSameLine(AbstractWrapCheck.getRightmostDescendant(arguments), rparen);
                } else {
                    this.checkWrapped(AbstractWrapCheck.getLeftmostDescendant(expression), rparen);
                }
            }
            break;

        case LITERAL_NEW:
        case ARRAY_INIT:
        case TYPECAST:

            // Checked by "visitToken()".
            ;
            break;

        default:
            this.log(
                expression,
                "Uncheckable: " + LocalTokenType.localize(Cs820.getType(expression)) + " / " + expression.toString()
            );
        }
    }
}
