
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
import static de.unkrig.cscontrib.LocalTokenType.MODIFIERS;
import static de.unkrig.cscontrib.checks.AbstractWrapCheck.Control.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.csdoclet.annotation.IntegerRuleProperty;
import de.unkrig.csdoclet.annotation.Message;

/**
 * Abstract base class for the "{@code Wrap...Check}" family of checks.
 */
@NotNullByDefault(false) public abstract
class AbstractWrapCheck extends AbstractCheck {

    /**
     * Message key as it appears in 'src/de/unkrig/cscontrib/checks/checkstyle-metadata.xml'.
     * <dl>
     *   <dt><code>{0}</code>
     *   <dd>Text of token <i>before</i> the (missing) line break
     *   <dt><code>{1}</code>
     *   <dd>Text of token <i>after</i> the (missing) line break
     * </dl>
     */
    @Message("Must wrap line before ''{1}''")
    public static final String
    MESSAGE_KEY_MUST_WRAP = "AbstractWrapCheck.mustWrap";

    /**
     * Message key as it appears in 'src/de/unkrig/cscontrib/checks/checkstyle-metadata.xml'.
     * <dl>
     *   <dt><code>{0}</code>
     *   <dd>Text of token <i>before</i> the (unwanted) line break
     *   <dt><code>{1}</code>
     *   <dd>Text of token <i>after</i> the (unwanted) line break
     * </dl>
     */
    @Message("''{0}'' must appear on same line as ''{1}''")
    public static final String
    MESSAGE_KEY_MUST_JOIN = "AbstractWrapCheck.mustJoin";

    /**
     * Message key as it appears in 'src/de/unkrig/cscontrib/checks/checkstyle-metadata.xml'.
     * <dl>
     *   <dt><code>{0}</code>
     *   <dd>Text the vertically misaligned token
     *   <dt><code>{1}</code>
     *   <dd>Current (wrong) column number of the token
     *   <dt><code>{2}</code>
     *   <dd>Correct column number of the token
     * </dl>
     */
    @Message("''{0}'' must appear in column {1}, not {2}")
    public static final String
    MESSAGE_KEY_WRONG_COLUMN = "AbstractWrapCheck.wrongColumn";

    /**
     * The constants of this enum may appear in the '{@code args}' of {@link
     * AbstractWrapCheck#checkChildren(DetailAST, Object...)} and modify the 'control flow'.
     */
    public
    enum Control {

        /**
         * Indicates that the previous and the next token <i>must</i> either:
         * <ul>
         *   <li>appear in the same line</li>
         *   <li>
         *     appear in different lines, and the next token must appear N columns right from the first
         *     non-space character in the preceding line
         *   </li>
         * </ul>
         */
        MAY_INDENT,

        /**
         * Same as {@link #MAY_INDENT}, but if the next token has no children, then the previous and the next token
         * must appear in the same line.
         */
        INDENT_IF_CHILDREN,

        /**
         * If the tokens of the matching {@link #MAY_INDENT} or {@link #INDENT_IF_CHILDREN} were actually indented,
         * the the previous and the next token must be 'unindented', i.e. the next token must appear in a different
         * line, and its first character must appear N positions left from the first non-space character of the
         * preceding line.
         */
        UNINDENT,

        /** Indicates that the previous and the next token <i>must</i> appear in the same line. */
        NO_WRAP,

        /**
         * Indicates that the previous and the next token <i>must</i> either:
         * <ul>
         *   <li>appear in the same line</li>
         *   <li>
         *     appear in different lines, and the next token must appear N columns right from the first
         *     non-space character in the preceding line
         *   </li>
         * </ul>
         */
        MAY_WRAP,

        /**
         * Indicates that the previous and the next token <i>must</i> appear in different lines, and the
         * next token must appear N columns right from the first non-space character in the preceding line.
         */
        MUST_WRAP,

        /**
         * Indicates that the next of {@code args} is a {@link LocalTokenType}, and that is consumed iff it equals
         * the next token.
         */
        OPTIONAL,

        /**
         * Indicates that at least one more token must exist.
         */
        ANY,

        /**
         * Indicates that the processing can either continue with the next element, or at the element with value
         * {@link #LABEL1}.
         * <p>
         * The same holds true for the other FORK-LABEL pairs.
         */
        FORK1,
        /** @see #FORK1 */
        FORK2,
        /** @see #FORK1 */
        FORK3,
        /** @see #FORK1 */
        FORK4,
        /** @see #FORK1 */
        FORK5,
        /** @see #FORK1 */
        FORK6,
        /** @see #FORK1 */
        FORK7,
        /** @see #FORK1 */
        FORK8,
        /** @see #FORK1 */
        FORK9,

        /**
         * Indicates that the processing continues at the element with value {@link #LABEL1}.
         * <p>
         * The same holds true for the other BRANCH-LABEL pairs.
         */
        BRANCH1,
        /** @see #BRANCH1 */
        BRANCH2,
        /** @see #BRANCH1 */
        BRANCH3,
        /** @see #BRANCH1 */
        BRANCH4,
        /** @see #BRANCH1 */
        BRANCH5,
        /** @see #BRANCH1 */
        BRANCH6,
        /** @see #BRANCH1 */
        BRANCH7,
        /** @see #BRANCH1 */
        BRANCH8,
        /** @see #BRANCH1 */
        BRANCH9,

        /**
         * Merely a target for {@link #FORK1} and {@link #BRANCH1} elements.
         *
         * @see #FORK1
         * @see #BRANCH1
         */
        LABEL1,
        /** @see #LABEL1 */
        LABEL2,
        /** @see #LABEL1 */
        LABEL3,
        /** @see #LABEL1 */
        LABEL4,
        /** @see #LABEL1 */
        LABEL5,
        /** @see #LABEL1 */
        LABEL6,
        /** @see #LABEL1 */
        LABEL7,
        /** @see #LABEL1 */
        LABEL8,
        /** @see #LABEL1 */
        LABEL9,

        /** Indicates that the previous token must not have a sibling. */
        END,
    }

    // BEGIN CONFIGURATION

    /**
     * How many spaces to use for each new indentation level.
     */
    @IntegerRuleProperty(defaultValue = AbstractWrapCheck.DEFAULT_BASIC_OFFSET)
    public void
    setBasicOffset(int value) { this.basicOffset = value; }

    /** How many spaces to use for each new indentation level. */
    protected int
    basicOffset = AbstractWrapCheck.DEFAULT_BASIC_OFFSET;

    private static final int
    DEFAULT_BASIC_OFFSET = 4;

    // END CONFIGURATION

    /**
     * Whether line MUST, MAY or MUST NOT be wrapped at a particular point.
     */
    public enum Wrap { ALWAYS, OPTIONAL, NEVER } // SUPPRESS CHECKSTYLE JavadocVariable

    /**
     * Converts the string values "always", "optional", "never" into {@link Control#MUST_WRAP}, {@link
     * Control#MAY_WRAP} and {@link Control#NO_WRAP}.
     */
    protected static Control
    toWrap(String value) {

        return (
            "always".equals(value)   ? MUST_WRAP :
            "optional".equals(value) ? MAY_WRAP :
            "never".equals(value)    ? NO_WRAP :
            AbstractWrapCheck.throwException(
                RuntimeException.class,
                Control.class,
                "Invalid string value '" + value + "'"
            )
        );
    }

    private static <ET extends Exception, RT> RT
    throwException(Class<ET> exceptionType, Class<RT> returnType, String message) throws ET {
        ET exception;

        try {
            exception = exceptionType.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw exception;
    }

    /**
     * @return Whether all children of the given <var>ast</var> appear in the same line.
     */
    protected static boolean
    isSingleLine(DetailAST ast) {
        return (
            AbstractWrapCheck.getLeftmostDescendant(ast).getLineNo()
            == AbstractWrapCheck.getRightmostDescendant(ast).getLineNo()
        );
    }

    /**
     * @return The leftmost descendant of the given <var>ast</var>
     */
    protected static DetailAST
    getLeftmostDescendant(DetailAST ast) {
        for (;;) {
            DetailAST tmp = ast.getFirstChild();
            if (tmp == null && ast.getType() == MODIFIERS.delocalize()) tmp = ast.getNextSibling();
            if (
                tmp == null
                || tmp.getLineNo() > ast.getLineNo()
                || (tmp.getLineNo() == ast.getLineNo() && tmp.getColumnNo() > ast.getColumnNo())
            ) return ast;
            ast = tmp;
        }
    }

    /**
     * @return The rightmost descendant of the given <var>ast</var>
     */
    protected static DetailAST
    getRightmostDescendant(DetailAST ast) {
        for (;;) {
            DetailAST tmp = ast.getLastChild();
            if (
                tmp == null
                || tmp.getLineNo() < ast.getLineNo()
                || (tmp.getLineNo() == ast.getLineNo() && tmp.getColumnNo() < ast.getColumnNo())
            ) return ast;
            ast = tmp;
        }
    }

    /**
     * Verifies that the children of the given {@code ast} are positioned as specified.
     *
     * @param args A sequence of {@link LocalTokenType}s and {@link Control}s
     */
    protected final void
    checkChildren(DetailAST ast, Object... args) {

        @SuppressWarnings("unused") AstDumper astDumper = new AstDumper(ast); // For debugging

        DetailAST child = ast.getFirstChild();

        // Determine the "indentation parent".
        switch (LocalTokenType.localize(ast.getType())) {

        case ELIST:      // There's an ELIST between the METH_CALL ('(') and the argument EXPRs.
            ast = ast.getParent();
            break;

        case SLIST:
            if (ast.getParent().getType() == CASE_GROUP.delocalize()) {
                ast = ast.getParent().getParent();
            }
            break;

        case PARAMETERS:
            ast = ast.getPreviousSibling(); // Use the LPAREN, not the PARAMETERS.
            break;

        case DOT:
            ast = AbstractWrapCheck.getLeftmostDescendant(ast);
            break;

        default:
            ;
        }

        DetailAST previousAst = ast;
        int       mode        = 0; // SUPPRESS CHECKSTYLE UsageDistance
        for (int idx = 0;;) {
            Object tokenType = args[idx++];

            if (tokenType instanceof Control) {

                Control control = (Control) tokenType;
                switch (control) {

                case END:
                    if (child == null) return;
                    this.log(child, "Unexpected extra token ''{0}''", child.getText());
                    return;

                case OPTIONAL:
                    tokenType = args[idx++];
                    while (AbstractWrapCheck.SKIPPABLES.contains(tokenType)) tokenType = args[idx++];
                    if (
                        child != null
                        && (tokenType == ANY || tokenType == LocalTokenType.localize(child.getType()))
                    ) {
                        previousAst = child;
                        child       = child.getNextSibling();
                    }
                    break;

                case FORK1:
                case FORK2:
                case FORK3:
                case FORK4:
                case FORK5:
                case FORK6:
                case FORK7:
                case FORK8:
                case FORK9:
                    {
                        Control label = Control.values()[control.ordinal() - FORK1.ordinal() + LABEL1.ordinal()];

                        int destination = Arrays.asList(args).indexOf(label);
                        assert destination != -1 : tokenType + ": Label '" + label + "' undefined";

                        destination++;

                        // Decide whether to branch or to continue;
                        boolean doBranch;
                        DO_BRANCH:
                        for (int i = destination;; i++) {
                            Object da = args[i];
                            if (AbstractWrapCheck.SKIPPABLES.contains(da)) {
                                ;
                            } else
                            if (da == END) {
                                doBranch = child == null;
                                break DO_BRANCH;
                            } else
                            if (da instanceof LocalTokenType) {
                                doBranch = child != null && ((LocalTokenType) da).delocalize() == child.getType();
                                break DO_BRANCH;
                            } else
                            {
                                for (int j = idx;; j++) {
                                    Object na = args[j];
                                    if (AbstractWrapCheck.SKIPPABLES.contains(na)) {
                                        ;
                                    } else
                                    if (na == END) {
                                        doBranch = child != null;
                                        break DO_BRANCH;
                                    } else
                                    if (na instanceof LocalTokenType) {
                                        doBranch = child == null || ((LocalTokenType) na).delocalize() != child.getType(); // SUPPRESS CHECKSTYLE LineLength
                                        break DO_BRANCH;
                                    } else
                                    if (na == ANY) {
                                        assert da != ANY;
                                        doBranch = child == null;
                                        break DO_BRANCH;
                                    } else
                                    if (da == ANY) {
                                        doBranch = child != null;
                                        break DO_BRANCH;
                                    } else
                                    {
                                        assert false : na + " / " + da;
                                    }
                                }
                            }
                        }

                        if (doBranch) idx = destination;
                    }
                    break;

                case BRANCH1:
                case BRANCH2:
                case BRANCH3:
                case BRANCH4:
                case BRANCH5:
                case BRANCH6:
                case BRANCH7:
                case BRANCH8:
                case BRANCH9:
                    {
                        Control label = Control.values()[control.ordinal() - BRANCH1.ordinal() + LABEL1.ordinal()];

                        int destination = Arrays.asList(args).indexOf(label);
                        if (destination == -1) {
                            throw new AssertionError(tokenType + ": Label '" + label + "' undefined");
                        }

                        idx = destination + 1;
                    }
                    break;

                case LABEL1:
                case LABEL2:
                case LABEL3:
                case LABEL4:
                case LABEL5:
                case LABEL6:
                case LABEL7:
                case LABEL8:
                case LABEL9:
                    ;
                    break;

                case ANY:
                    if (child == null) {
                        this.log(
                            previousAst,
                            "Token missing after ''{0}''",
                            previousAst.getText()
                        );
                        return;
                    }

                    previousAst = AbstractWrapCheck.getRightmostDescendant(child);
                    child       = child.getNextSibling();
                    break;

                case INDENT_IF_CHILDREN:
                    assert child != null;
                    if (child.getFirstChild() == null) break;
                    /*FALLTHROUGH*/

                case MAY_INDENT:
                    assert child != null;
                    switch (mode) {

                    case 0:
                        {
                            DetailAST c = AbstractWrapCheck.getLeftmostDescendant(child);
                            if (c.getLineNo() == previousAst.getLineNo()) {
                                mode = 1;
                            } else {
                                mode = 2;
                                if (child.getType() == CASE_GROUP.delocalize()) {
                                    this.checkWrapped(ast, c);
                                } else {
                                    this.checkIndented(ast, c);
                                }
                            }
                        }
                        break;

                    case 1:
                        this.checkSameLine(previousAst, AbstractWrapCheck.getLeftmostDescendant(child));
                        break;

                    case 2:
                        {
                            DetailAST l = AbstractWrapCheck.getLeftmostDescendant(child);
                            if (l.getLineNo() == previousAst.getLineNo()) {

                                if (!this.checkMultipleElementsPerLine(child)) {
                                    this.log(
                                        l,
                                        AbstractWrapCheck.MESSAGE_KEY_MUST_WRAP,
                                        previousAst.getText(),
                                        l.getText()
                                    );
                                }
                            } else {
                                if (child.getType() == CASE_GROUP.delocalize()) {
                                    this.checkWrapped(ast, l);
                                } else {
                                    this.checkIndented(ast, l);
                                }
                            }
                        }
                        break;
                    }
                    break;

                case UNINDENT:
                    assert child != null;
                    switch (mode) {

                    case 0:
                        if (previousAst.getLineNo() != child.getLineNo()) {
                            this.checkWrapped(ast, child);
                        }
                        break;

                    case 1:
                        this.checkSameLine(previousAst, child);
                        break;

                    case 2:
                        this.checkWrapped(ast, child);
                        break;
                    }
                    mode = 0;
                    break;

                case MAY_WRAP:
                    assert child != null;
                    assert mode == 0;
                    if (child.getLineNo() != previousAst.getLineNo()) {
                        this.checkWrapped(previousAst, child);
                    }
                    break;

                case MUST_WRAP:
                    assert mode == 0;
                    if (previousAst.getType() == MODIFIERS.delocalize()) {
                        ;
                    } else
                    {
                        this.checkWrapped(previousAst, child);
                    }
                    break;

                case NO_WRAP:
                    this.checkSameLine(previousAst, AbstractWrapCheck.getLeftmostDescendant(child));
                    break;
                }
            } else
            if (tokenType instanceof LocalTokenType) {

                if (child == null) {
                    this.log(
                        previousAst,
                        "''{0}'' after ''{1}''",
                        tokenType,
                        previousAst.getText()
                    );
                    return;
                }

                if (child.getType() != ((LocalTokenType) tokenType).delocalize()) {
                    this.log(
                        child,
                        "Expected ''{0}'' instead of ''{1}''",
                        tokenType,
                        child.getText() + "'"
                    );
                    return;
                }

                assert child != null;
                previousAst = AbstractWrapCheck.getRightmostDescendant(child);
                child       = child.getNextSibling();
            } else
            {
                throw new AssertionError(tokenType);
            }
        }
    }

    /**
     * Hook for derived classes to check whether multiple elements in one line (e.g. method call arguments) are
     * allowed.
     *
     * @return Whether to <i>not</i> log a "must wrap" issue
     */
    protected boolean
    checkMultipleElementsPerLine(DetailAST child) { return true; }

    private static final Set<Object> SKIPPABLES;
    static {
        Set<Object> ss = new HashSet<Object>();

        // SUPPRESS CHECKSTYLE WrapMethod:6
        ss.addAll(Arrays.asList(
            MAY_INDENT, UNINDENT, INDENT_IF_CHILDREN,
            MAY_WRAP,  MUST_WRAP, NO_WRAP,
            LABEL1, LABEL2, LABEL3, LABEL4, LABEL5, LABEL6, LABEL7, LABEL8, LABEL9
        ));
        SKIPPABLES = Collections.unmodifiableSet(ss);
    }

    /**
     * Checks that the line where {@code next} occurs is indented by {@link #basicOffset}, compared to the line
     * where {@code previous} occurs.
     */
    protected void
    checkIndented(DetailAST previous, DetailAST next) {
        if (next.getLineNo() == previous.getLineNo()) {
            this.log(next, AbstractWrapCheck.MESSAGE_KEY_MUST_WRAP, previous.getText(), next.getText());
        } else {
            this.checkAlignment(next, this.calculateIndentation(previous) + this.basicOffset);
        }
    }

    /**
     * Checks that the line where {@code next} occurs is unindented by {@link #basicOffset}, compared to the
     * line where {@code previous} occurs.
     */
    protected void
    checkUnindented(DetailAST previous, DetailAST next) {
        if (next.getLineNo() == previous.getLineNo()) {
            this.log(next, AbstractWrapCheck.MESSAGE_KEY_MUST_WRAP, previous.getText(), next.getText());
        } else {
            this.checkAlignment(next, this.calculateIndentation(previous) - this.basicOffset);
        }
    }

    /**
     * Checks that the line where {@code next} occurs is indented exactly as the line where {@code previous} occurs.
     */
    protected void
    checkWrapped(DetailAST previous, DetailAST next) {
        if (next.getLineNo() == previous.getLineNo()) {
            this.log(next, AbstractWrapCheck.MESSAGE_KEY_MUST_WRAP, previous.getText(), next.getText());
        } else {
            this.checkAlignment(next, this.calculateIndentation(previous));
        }
    }

    /**
     * Checks that {@code left} and {@code right} appear in the same line.
     */
    protected void
    checkSameLine(DetailAST left, DetailAST right) {
        if (left.getLineNo() != right.getLineNo()) {
            this.log(
                right,
                AbstractWrapCheck.MESSAGE_KEY_MUST_JOIN,
                right.getText(),
                left.getText()
            );
        }
    }

    /**
     * Logs a problem iff the given {@code ast} is not vertically positioned at the given {@code targetColumnNo}.
     *
     * @param targetColumnNo Counting from zero
     */
    private void
    checkAlignment(DetailAST ast, int targetColumnNo) {
        int actualColumnNo = AbstractWrapCheck.lengthExpandedTabs(
            this.getLines()[ast.getLineNo() - 1],
            ast.getColumnNo(),
            this.getTabWidth()
        );
        if (actualColumnNo != targetColumnNo) {
            this.log(
                ast,
                AbstractWrapCheck.MESSAGE_KEY_WRONG_COLUMN,
                ast.getText(),
                targetColumnNo + 1,
                actualColumnNo + 1
            );
        }
    }

    /**
     *  The "Utils" class moved some time between CS 6.2 and 6.5 from package
     *    com.puppycrawl.tools.checkstyle
     * to package
     *    com.puppycrawl.tools.checkstyle.api
     * , so we have our own copy here.
     */
    public static int
    lengthExpandedTabs(String string, int toIdx, int tabWidth) {

        int len = 0;
        for (int idx = 0; idx < toIdx; idx++) {
            if (string.charAt(idx) == '\t') {
                len = (len / tabWidth + 1) * tabWidth;
            } else {
                len++;
            }
        }

        return len;
    }

    /**
     * Calculate the indentation of the line of the given {@code ast}, honoring TAB characters. Notice that the
     * {@code ast} need not be the FIRST element in that line.
     */
    private int
    calculateIndentation(DetailAST ast) {
        String line = this.getLines()[ast.getLineNo() - 1];

        int result = 0;
        for (int i = 0; i < line.length(); ++i) {
            switch (line.charAt(i)) {

            case ' ':
                ++result;
                break;

            case '\t':
                {
                    int tabWidth = this.getTabWidth();
                    result += tabWidth - (result % tabWidth);
                }
                break;

            default:
                return result;
            }
        }
        return 0;
    }
}
