
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.compat.Cs820;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.csdoclet.annotation.Message;
import de.unkrig.csdoclet.annotation.Rule;

/**
 * Verifies that C++-style comments ("<code>// ..&#46;</code>") are correctly aligned.
 * <p>
 * C++-style comments must appear on the same column iff
 * <ul>
 *   <li>They appear in immediately consecutive lines (only for statement lists and SWITCH statements), and
 *   <li>All of these lines are of the same "kind" (see below)
 * </ul>
 * Each line of code relates to one of the following "kinds":
 * <ul>
 *   <li>A line which contains only a C++-style comment
 *   <li>A line which contains only a switch label ("{@code case x:}" or "{@code default:}") and a C++-style comment
 *   <li>Any other line
 * </ul>
 */
@Rule(
    group     = "%Whitespace.group",
    groupName = "Whitespace",
    name      = "de.unkrig: C++-style comment alignment",
    parent    = "TreeWalker"
)
@NotNullByDefault(false)
public
class CppCommentAlignment extends AbstractCheck {

    @Message("C++ comment must appear on column {0}, not {1}")
    private static final String MESSAGE_KEY_MISALIGNED = "CppCommentAlignment.misaligned";

    private Map<Integer /*lineNumber*/, TextBlock> cppComments;

    @Override public int[]
    getAcceptableTokens() {

        return LocalTokenType.delocalize(new LocalTokenType[] {

            // Binary operators.
            LocalTokenType.ASSIGN,
            LocalTokenType.BAND,
            LocalTokenType.BAND_ASSIGN,
            LocalTokenType.BOR,
            LocalTokenType.BOR_ASSIGN,
            LocalTokenType.BSR,
            LocalTokenType.BSR_ASSIGN,
            LocalTokenType.BXOR,
            LocalTokenType.BXOR_ASSIGN,
            LocalTokenType.DIV,
            LocalTokenType.DIV_ASSIGN,
            LocalTokenType.DOT,
            LocalTokenType.EQUAL,
            LocalTokenType.GE,
            LocalTokenType.GT,
            LocalTokenType.INDEX_OP,
            LocalTokenType.LAND,
            LocalTokenType.LE,
            LocalTokenType.LITERAL_INSTANCEOF,
            LocalTokenType.LOR,
            LocalTokenType.LT,
            LocalTokenType.MINUS,
            LocalTokenType.MINUS_ASSIGN,
            LocalTokenType.MOD,
            LocalTokenType.MOD_ASSIGN,
            LocalTokenType.NOT_EQUAL,
            LocalTokenType.PLUS,
            LocalTokenType.PLUS_ASSIGN,
            LocalTokenType.SL,
            LocalTokenType.SL_ASSIGN,
            LocalTokenType.SR,
            LocalTokenType.SR_ASSIGN,
            LocalTokenType.STAR,
            LocalTokenType.STAR_ASSIGN,

            // Unary operators.
            LocalTokenType.BNOT,
            LocalTokenType.LNOT,
//          LocalTokenType.UNARY_MINUS,
//          LocalTokenType.UNARY_PLUS,

            // Other tokens.
//            LocalTokenType.ABSTRACT,
            LocalTokenType.ANNOTATION,
            LocalTokenType.ANNOTATION_ARRAY_INIT,
            LocalTokenType.ANNOTATION_DEF,
            LocalTokenType.ANNOTATION_FIELD_DEF,
//            LocalTokenType.ANNOTATION_MEMBER_VALUE_PAIR,
            LocalTokenType.ANNOTATIONS,
            LocalTokenType.ARRAY_DECLARATOR,
            LocalTokenType.ARRAY_INIT,
//            LocalTokenType.AT,
            LocalTokenType.CASE_GROUP,
//            LocalTokenType.CHAR_LITERAL,
            LocalTokenType.CLASS_DEF,
//            LocalTokenType.COLON,
//            LocalTokenType.COMMA,
            LocalTokenType.CTOR_CALL,
            LocalTokenType.CTOR_DEF,
//            LocalTokenType.DEC,
//            LocalTokenType.DO_WHILE,
            LocalTokenType.ELIST,
//            LocalTokenType.ELLIPSIS,
//            LocalTokenType.EMPTY_STAT,
//            LocalTokenType.ENUM,
            LocalTokenType.ENUM_CONSTANT_DEF,
            LocalTokenType.ENUM_DEF,
//            LocalTokenType.EOF,
//            LocalTokenType.EXPR, // Pseudo token, e.g. EXPR { '(' '+' ')' }
            LocalTokenType.EXTENDS_CLAUSE,
//            LocalTokenType.FINAL,
            LocalTokenType.FOR_CONDITION,
            LocalTokenType.FOR_EACH_CLAUSE,
            LocalTokenType.FOR_INIT,
            LocalTokenType.FOR_ITERATOR,
//            LocalTokenType.GENERIC_END,
//            LocalTokenType.GENERIC_START,
//            LocalTokenType.IDENT,
            LocalTokenType.IMPLEMENTS_CLAUSE,
//            LocalTokenType.IMPORT,
//            LocalTokenType.INC,
//            LocalTokenType.INSTANCE_INIT,
            LocalTokenType.INTERFACE_DEF,
            LocalTokenType.LABELED_STAT,
//            LocalTokenType.LCURLY,
            LocalTokenType.LITERAL_ASSERT,
//            LocalTokenType.LITERAL_BOOLEAN,
            LocalTokenType.LITERAL_BREAK,
//            LocalTokenType.LITERAL_BYTE,
            LocalTokenType.LITERAL_CASE,
            LocalTokenType.LITERAL_CATCH,
//            LocalTokenType.LITERAL_CHAR,
//            LocalTokenType.LITERAL_CLASS,
            LocalTokenType.LITERAL_CONTINUE,
//            LocalTokenType.LITERAL_DEFAULT,
            LocalTokenType.LITERAL_DO,
//            LocalTokenType.LITERAL_DOUBLE,
            LocalTokenType.LITERAL_ELSE,
//            LocalTokenType.LITERAL_FALSE,
//            LocalTokenType.LITERAL_FINALLY,
//            LocalTokenType.LITERAL_FLOAT,
            LocalTokenType.LITERAL_FOR,
            LocalTokenType.LITERAL_IF,
//            LocalTokenType.LITERAL_INT,
//            LocalTokenType.LITERAL_INTERFACE,
//            LocalTokenType.LITERAL_LONG,
//            LocalTokenType.LITERAL_NATIVE,
//            LocalTokenType.LITERAL_NEW, // Irrelevant: "new { FormalParameter'(' ELIST ')' }
//            LocalTokenType.LITERAL_NULL,
//            LocalTokenType.LITERAL_PRIVATE,
//            LocalTokenType.LITERAL_PROTECTED,
//            LocalTokenType.LITERAL_PUBLIC,
            LocalTokenType.LITERAL_RETURN,
//            LocalTokenType.LITERAL_SHORT,
//            LocalTokenType.LITERAL_STATIC,
//            LocalTokenType.LITERAL_SUPER,
            LocalTokenType.LITERAL_SWITCH,
            LocalTokenType.LITERAL_SYNCHRONIZED,
//            LocalTokenType.LITERAL_THIS,
            LocalTokenType.LITERAL_THROW,
            LocalTokenType.LITERAL_THROWS,
//            LocalTokenType.LITERAL_TRANSIENT,
//            LocalTokenType.LITERAL_TRUE,
            LocalTokenType.LITERAL_TRY,
//            LocalTokenType.LITERAL_VOID,
//            LocalTokenType.LITERAL_VOLATILE,
            LocalTokenType.LITERAL_WHILE,
//            LocalTokenType.LPAREN,
//            LocalTokenType.METHOD_CALL  METHOD_CALL { meth ELIST ')' }
            LocalTokenType.METHOD_DEF,
            LocalTokenType.MODIFIERS,
//            LocalTokenType.NUM_DOUBLE,
//            LocalTokenType.NUM_FLOAT,
//            LocalTokenType.NUM_INT,
//            LocalTokenType.NUM_LONG,
            LocalTokenType.OBJBLOCK,
            LocalTokenType.PACKAGE_DEF,
            LocalTokenType.PARAMETER_DEF,
            LocalTokenType.PARAMETERS,
//            LocalTokenType.POST_DEC,
//            LocalTokenType.POST_INC,
            LocalTokenType.QUESTION,
//            LocalTokenType.RBRACK,
//            LocalTokenType.RCURLY,
//            LocalTokenType.RESOURCE,
//            LocalTokenType.RESOURCE_SPECIFICATION,
//            LocalTokenType.RESOURCES,
//            LocalTokenType.RPAREN,
//            LocalTokenType.SEMI,
            LocalTokenType.SLIST,
            LocalTokenType.STATIC_IMPORT,
//            LocalTokenType.STATIC_INIT,
//            LocalTokenType.STRICTFP,
//            LocalTokenType.STRING_LITERAL,
            LocalTokenType.SUPER_CTOR_CALL,
            LocalTokenType.TYPE,
            LocalTokenType.TYPECAST,
            LocalTokenType.TYPE_ARGUMENT,
            LocalTokenType.TYPE_ARGUMENTS,
            LocalTokenType.TYPE_PARAMETER,
//            LocalTokenType.TYPE_EXTENSION_AND,
//            LocalTokenType.TYPE_LOWER_BOUNDS,
            LocalTokenType.TYPE_PARAMETERS,
            LocalTokenType.TYPE_UPPER_BOUNDS,
//            LocalTokenType.VARIABLE_DEF,  // Pseudo token, e.g. VARIABLE_DEF { MODIFIERS TYPE name init }
//            LocalTokenType.WILDCARD_TYPE,
        });
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    beginTree(DetailAST ast) {
        this.cppComments = CppCommentAlignment.getSingleLineComments(this.getFileContents());
    }

    @SuppressWarnings("unchecked") private static Map<Integer, TextBlock>
    getSingleLineComments(FileContents fileContents) {

        // The signature of "FileContents.getSingleLineComments()" keeps changing across CS versions:
        // 8.0.0  public com.google.common.collect.ImmutableMap<Integer, TextBlock> getSingleLineComments()
        // 8.5.1  public com.google.common.collect.ImmutableMap<Integer, TextBlock> getSingleLineComments()
        // 8.12.0 public                          java.util.Map<Integer, TextBlock> getSingleLineComments()
        // So...:
//      return fileContents.getSingleLineComments();
        try {
            return (Map<Integer, TextBlock>) (
                fileContents
                .getClass()
                .getDeclaredMethod("getSingleLineComments")
                .invoke(fileContents)
            );
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override public void
    visitToken(DetailAST ast) {

        @SuppressWarnings("unused") AstDumper astDumper = new AstDumper(ast);

        if (Cs820.getChildCount(ast) <= 1) return;

        List<DetailAST> children = this.getChildren(ast);

        Map<Integer /*lineNo*/, Integer /*colNo*/> commentCoordinates = new HashMap<Integer, Integer>();

        // Collect all C++-style comments that appear right from all children.
        int prevLineNo = Integer.MAX_VALUE; // SUPPRESS CHECKSTYLE UsageDistance
        for (DetailAST child : children) {

            final int lineNo = Cs820.getLineNo(child);

            // Special case "CASE_GROUP { 'case' 'case' SLIST }".
            if (AstUtil.typeIs(child, LocalTokenType.SLIST)) continue;

            // For statement lists and SWITCH statements...
            if (
                AstUtil.typeIs(ast, LocalTokenType.LITERAL_SWITCH)
                || AstUtil.typeIs(ast, LocalTokenType.SLIST)
            ) {

                // ... tolerate different alignments if lines are not consecutive.
                TextBlock tb = this.cppComments.get(lineNo);
                if (tb == null || tb.getStartColNo() == 1) continue;

                if (tb.getStartLineNo() - 1 > prevLineNo) {
                    this.analyze(commentCoordinates);
                    commentCoordinates.clear();
                    prevLineNo = Integer.MAX_VALUE;
                    continue;
                }
            }

            // Do not regard the '{' as a child of an OBJBLOCK.
            if (AstUtil.typeIs(ast, LocalTokenType.OBJBLOCK) && AstUtil.typeIs(child, LocalTokenType.LCURLY)) continue;

            // Do not regard the PARAMETERS as a child of a METHOD_DEF.
            if (
                AstUtil.typeIs(ast, LocalTokenType.METHOD_DEF)
                && AstUtil.typeIs(child, LocalTokenType.PARAMETERS)
            ) continue;

            if (AstUtil.typeIs(child, LocalTokenType.RCURLY)) continue;

            TextBlock tb = this.cppComments.get(lineNo);
            if (tb == null /*|| tb.getStartColNo() == 1*/) continue;

            if (commentCoordinates.containsKey(lineNo)) continue;

            commentCoordinates.put(lineNo, tb.getStartColNo());
            prevLineNo = lineNo;
        }

        // Verify that these C++-style comments are properly aligned.
        this.analyze(commentCoordinates);
    }

    private void
    analyze(Map<Integer /*lineNo*/, Integer /*colNo*/> commentCoordinates) {

        if (commentCoordinates.size() <= 1) return;

        int maxCommentColNo = 0;
        for (Integer commentColNo : commentCoordinates.values()) {
            if (commentColNo > maxCommentColNo) maxCommentColNo = commentColNo;
        }

        for (Entry<Integer /*lineNo*/, Integer /*colNo*/> e : commentCoordinates.entrySet()) {
            Integer commentLineNo = e.getKey();
            Integer commentColNo  = e.getValue();
            if (commentColNo != maxCommentColNo) {
                this.log(
                    commentLineNo,
                    commentColNo,
                    CppCommentAlignment.MESSAGE_KEY_MISALIGNED,
                    maxCommentColNo + 1,
                    commentColNo  + 1
                );
            }
        }
    }

    /**
     * Returns the children of the given node, but sometimes flattened, e.g. "(a + b) + (c + d)" is "a + b + c + d".
     */
    private List<DetailAST>
    getChildren(DetailAST ast) {

        {
            DetailAST parent = Cs820.getParent(ast);
            if (parent != null && Cs820.getText(parent).equals(Cs820.getText(ast))) return Collections.emptyList();
        }

        List<DetailAST> result = new ArrayList<DetailAST>();
        this.getChildren2(ast, result);
        return result;
    }

    private void
    getChildren2(DetailAST ast, List<DetailAST> result) {

        for (DetailAST child = Cs820.getFirstChild(ast); child != null; child = Cs820.getNextSibling(child)) {

            if (LocalTokenType.localize(Cs820.getType(child)) == LocalTokenType.LPAREN) {
                result.add(child);
                for (
                    child = Cs820.getNextSibling(child);
                    LocalTokenType.localize(Cs820.getType(child)) != LocalTokenType.RPAREN;
                    child = Cs820.getNextSibling(child)
                );
            }

            if (Cs820.getText(child).equals(Cs820.getText(ast))) {
                this.getChildren2(child, result);
                continue;
            }

            result.add(child);
        }
    }
}
