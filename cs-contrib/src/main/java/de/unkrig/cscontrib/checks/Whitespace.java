
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

import java.util.EnumSet;
import java.util.regex.Pattern;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.compat.Cs820;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.cscontrib.util.JavaElement;
import de.unkrig.csdoclet.annotation.Message;
import de.unkrig.csdoclet.annotation.MultiCheckRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;

/**
 * Verifies that tokens are, respectively are not preceded with (and/or followed by) whitespace.
 * <p>
 *   This check supersedes all of CheckStyle's whitespace-related checks:
 * </p>
 * <ul>
 *   <li>Generic Whitespace</li>
 *   <li>Empty For Initializer Pad</li>
 *   <li>Empty For Iterator Pad</li>
 *   <li>No Whitespace After</li>
 *   <li>No Whitespace Before</li>
 *   <li>Method Parameter Pad</li>
 *   <li>Paren Pad</li>
 *   <li>Typecast Paren Pad</li>
 *   <li>Whitespace After</li>
 *   <li>Whitespace Around</li>
 * </ul>
 * <p>
 *   , as well as
 * </p>
 * <ul>
 *   <li>de.unkrig.ParenPad</li>
 *   <li>de.unkrig.WhitespaceAround</li>
 * </ul>
 */
@Rule(group = "%Whitespace.group", groupName = "Whitespace", name = "de.unkrig: Whitespace", parent = "TreeWalker")
@NotNullByDefault(false) public
class Whitespace extends AbstractCheck {

    @Message("''{0}'' is followed by whitespace (option ''{1}'')")
    private static final String MESSAGE_KEY_FOLLOWED = "Whitespace.followed";

    @Message("''{0}'' is not followed by whitespace (option ''{1}'')")
    private static final String MESSAGE_KEY_NOT_FOLLOWED = "Whitespace.notFollowed";

    @Message("''{0}'' is preceded with whitespace (option ''{1}'')")
    private static final String MESSAGE_KEY_PRECEDED = "Whitespace.preceded";

    @Message("''{0}'' is not preceded with whitespace (option ''{1}'')")
    private static final String MESSAGE_KEY_NOT_PRECEDED = "Whitespace.notPreceded";

    // BEGIN CONFIGURATION SETTERS

    // SUPPRESS CHECKSTYLE LineLength:6
    /**
     * The Java elements which must be preceded with whitespace (or a line break).
     */
    @MultiCheckRuleProperty(optionProvider = JavaElement.class, defaultValue = Whitespace.DEFAULT_WHITESPACE_BEFORE)
    public void
    setWhitespaceBefore(String[] sa) { this.whitespaceBefore = Whitespace.toEnumSet(sa, JavaElement.class); }

    private EnumSet<JavaElement>
    whitespaceBefore = Whitespace.toEnumSet(Whitespace.DEFAULT_WHITESPACE_BEFORE.toUpperCase(), JavaElement.class);

    private static final String // SUPPRESS CHECKSTYLE LineLength
    DEFAULT_WHITESPACE_BEFORE = "and__expr,and__type_bound,and_assign,assert,assign__assignment,assign__var_decl,break,case,catch,class__class_decl,colon__enhanced_for,colon__ternary,conditional_and,conditional_or,continue,default__anno_elem,default__switch,divide,divide_assign,do,else,enum,equal,extends__type,extends__type_bound,finally,for,greater,greater_equal,if,implements,import,import__static_import,instanceof,l_angle__meth_decl_type_params,l_curly__anon_class,l_curly__block,l_curly__catch,l_curly__do,l_curly__empty_anon_class,l_curly__empty_catch,l_curly__empty_meth_decl,l_curly__empty_type_decl,l_curly__enum_const,l_curly__finally,l_curly__for,l_curly__if,l_curly__instance_init,l_curly__labeled_stat,l_curly__meth_decl,l_curly__static_init,l_curly__switch,l_curly__synchronized,l_curly__try,l_curly__type_decl,l_curly__while,l_paren__catch,l_paren__do_while,l_paren__for,l_paren__for_no_init,l_paren__if,l_paren__resources,left_shift,left_shift_assign,less,less_equal,minus__additive,minus_assign,modulo,modulo_assign,multiply,multiply_assign,name__ctor_decl,name__meth_decl,name__param,name__type_decl,name__local_var_decl,not_equal,or,or_assign,package,plus__additive,plus_assign,question__ternary,r_curly__anno_array_init,r_curly__anon_class,r_curly__array_init,r_curly__block,r_curly__catch,r_curly__do,r_curly__else,r_curly__finally,r_curly__for,r_curly__if,r_curly__instance_init,r_curly__labeled_stat,r_curly__lambda,r_curly__meth_decl,r_curly__static_init,r_curly__switch,r_curly__synchronized,r_curly__try,r_curly__type_decl,r_curly__while,return__expr,return__no_expr,right_shift,right_shift_assign,semi__type_decl,static__static_import,static__static_init,super__type_bound,switch,synchronized__mod,synchronized__synchronized,this__ctor_call,throw,throws,try,unsigned_right_shift,unsigned_right_shift_assign,void,while__do,while__while,xor,xor_assign";

    /**
     * The Java elements which must not be preceded with whitespace (or are preceded with a line break).
     */
    @MultiCheckRuleProperty(optionProvider = JavaElement.class, defaultValue = Whitespace.DEFAULT_NO_WHITESPACE_BEFORE)
    public void
    setNoWhitespaceBefore(String[] sa) { this.noWhitespaceBefore = Whitespace.toEnumSet(sa, JavaElement.class); }

    private EnumSet<JavaElement>
    noWhitespaceBefore = Whitespace.toEnumSet(Whitespace.DEFAULT_NO_WHITESPACE_BEFORE.toUpperCase(), JavaElement.class);

    private static final String // SUPPRESS CHECKSTYLE LineLength
    DEFAULT_NO_WHITESPACE_BEFORE = "class__class_literal,colon__default,colon__case,colon__labeled_stat,comma,dot__import,dot__package_decl,dot__qualified_type,dot__selector,ellipsis,l_angle__meth_invocation_type_args,l_angle__type_args,l_angle__type_params,l_brack__array_decl,l_brack__index,l_paren__anno,l_paren__anno_elem_decl,l_paren__meth_invocation,l_paren__params,meth_ref,name__anno,post_decr,post_incr,r_angle__meth_decl_type_params,r_angle__meth_invocation_type_args,r_angle__type_args,r_angle__type_params,r_brack__array_decl,r_brack__index,r_curly__empty_anno_array_init,r_curly__empty_anon_class,r_curly__empty_array_init,r_curly__empty_catch,r_curly__empty_lambda,r_curly__empty_meth_decl,r_curly__empty_type_decl,r_curly__enum_const_decl,r_paren__anno,r_paren__anno_elem_decl,r_paren__meth_invocation,r_paren__cast,r_paren__catch,r_paren__do_while,r_paren__for,r_paren__for_no_update,r_paren__if,r_paren__params,r_paren__parenthesized,r_paren__resources,semi__abstract_meth_decl,semi__anno_elem_decl,semi__enum_decl,semi__field_decl,semi__for_condition_no_update,semi__for_condition_update,semi__for_init_condition,semi__for_init_no_condition,semi__for_no_condition_no_update,semi__for_no_condition_update,semi__for_no_init_condition,semi__for_no_init_no_condition,semi__import,semi__package_decl,semi__resources,semi__statement,semi__static_import,star__type_import_on_demand";

    /**
     * The Java elements which must be followed by whitespace (or a line break).
     */
    @MultiCheckRuleProperty(optionProvider = JavaElement.class, defaultValue = Whitespace.DEFAULT_WHITESPACE_AFTER)
    public void
    setWhitespaceAfter(String[] sa) { this.whitespaceAfter = Whitespace.toEnumSet(sa, JavaElement.class); }

    private EnumSet<JavaElement>
    whitespaceAfter = Whitespace.toEnumSet(Whitespace.DEFAULT_WHITESPACE_AFTER.toUpperCase(), JavaElement.class);

    private static final String // SUPPRESS CHECKSTYLE LineLength
    DEFAULT_WHITESPACE_AFTER = "abstract,and__expr,and__type_bound,and_assign,assert,assign__assignment,assign__var_decl,case,catch,class__class_decl,colon__case,colon__default,colon__enhanced_for,colon__labeled_stat,colon__ternary,comma,conditional_and,conditional_or,default__anno_elem,default__mod,divide,divide_assign,do,ellipsis,else,enum,equal,extends__type,extends__type_bound,final,finally,for,greater,greater_equal,if,implements,import,import__static_import,instanceof,interface,l_curly__anno_array_init,l_curly__anon_class,l_curly__array_init,l_curly__block,l_curly__catch,l_curly__do,l_curly__enum_const,l_curly__finally,l_curly__for,l_curly__if,l_curly__instance_init,l_curly__labeled_stat,l_curly__meth_decl,l_curly__static_init,l_curly__switch,l_curly__synchronized,l_curly__try,l_curly__type_decl,l_curly__while,left_shift,left_shift_assign,less,less_equal,minus__additive,minus_assign,modulo,modulo_assign,multiply,multiply_assign,name__anno_member,native,new,not_equal,or,or_assign,package,plus__additive,plus_assign,private,protected,public,question__ternary,r_angle__meth_decl_type_params,r_curly__block,r_curly__catch,r_curly__do,r_curly__else,r_curly__empty_catch,r_curly__empty_meth_decl,r_curly__empty_type_decl,r_curly__finally,r_curly__for,r_curly__if,r_curly__instance_init,r_curly__labeled_stat,r_curly__meth_decl,r_curly__static_init,r_curly__switch,r_curly__synchronized,r_curly__try,r_curly__type_decl,r_curly__while,r_paren__cast,r_paren__catch,r_paren__if,r_paren__resources,return__expr,right_shift,right_shift_assign,semi__abstract_meth_decl,semi__anno_elem_decl,semi__empty_stat,semi__enum_decl,semi__field_decl,semi__for_condition_update,semi__for_init_condition,semi__for_no_condition_update,semi__for_no_init_condition,semi__import,semi__package_decl,semi__resources,semi__statement,semi__static_import,semi__type_decl,static__mod,static__static_import,static__static_init,super__type_bound,switch,synchronized__mod,synchronized__synchronized,throw,throws,transient,try,unsigned_right_shift,unsigned_right_shift_assign,volatile,while__do,while__while,xor,xor_assign";

    /**
     * The Java elements which must not be followed by whitespace (or are followed by a line break).
     */
    @MultiCheckRuleProperty(optionProvider = JavaElement.class, defaultValue = Whitespace.DEFAULT_NO_WHITESPACE_AFTER)
    public void
    setNoWhitespaceAfter(String[] sa)  { this.noWhitespaceAfter = Whitespace.toEnumSet(sa, JavaElement.class); }

    private EnumSet<JavaElement>
    noWhitespaceAfter = Whitespace.toEnumSet(Whitespace.DEFAULT_NO_WHITESPACE_AFTER.toUpperCase(), JavaElement.class);

    private static final String // SUPPRESS CHECKSTYLE LineLength
    DEFAULT_NO_WHITESPACE_AFTER = "at__anno,at__anno_decl,bitwise_complement,default__switch,dot__import,dot__package_decl,dot__qualified_type,dot__selector,l_angle__meth_decl_type_params,l_angle__meth_invocation_type_args,l_angle__type_args,l_angle__type_params,l_brack__array_decl,l_brack__index,l_curly__empty_anno_array_init,l_curly__empty_anon_class,l_curly__empty_array_init,l_curly__empty_catch,l_curly__empty_meth_decl,l_curly__empty_type_decl,l_paren__anno,l_paren__anno_elem_decl,l_paren__meth_invocation,l_paren__cast,l_paren__catch,l_paren__do_while,l_paren__for,l_paren__for_no_init,l_paren__if,l_paren__lambda_params,l_paren__params,l_paren__parenthesized,l_paren__resources,logical_complement,meth_ref,minus__unary,name__anno_elem_decl,name__ctor_decl,name__import_component,name__import_type,name__inferred_param,name__meth_decl,name__package_decl,name__param,plus__unary,pre_decr,pre_incr,r_angle__meth_invocation_type_args,r_paren__do_while,return__no_expr,semi__for_condition_no_update,semi__for_init_no_condition,semi__for_no_condition_no_update,semi__for_no_init_no_condition,star__type_import_on_demand,super__ctor_call,super__expr,this__ctor_call";

    // END CONFIGURATION SETTERS

    private static <E extends Enum<E>> E
    toEnum(String s, Class<E> enumType) {
        try {
            return Enum.valueOf(enumType, s.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException("Unable to parse " + s, iae);
        }
    }

    private static <E extends Enum<E>> EnumSet<E>
    toEnumSet(String values, Class<E> enumClass) {
        return Whitespace.toEnumSet(values, Whitespace.COMMA_PATTERN, enumClass);
    }
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    private static <E extends Enum<E>> EnumSet<E>
    toEnumSet(String values, Pattern separatorPattern, Class<E> enumClass) {
        return Whitespace.toEnumSet(separatorPattern.split(values), enumClass);
    }

    private static <E extends Enum<E>> EnumSet<E>
    toEnumSet(String[] values, Class<E> enumClass) {
        EnumSet<E> result = EnumSet.noneOf(enumClass);
        for (String value : values) result.add(Whitespace.toEnum(value, enumClass));
        return result;
    }

    @Override public void
    visitToken(DetailAST ast) {
        assert ast != null;

        @SuppressWarnings("unused") AstDumper dumper = new AstDumper(ast); // For debugging

        JavaElement javaElement = AstUtil.toJavaElement(ast);

        if (javaElement == null) {
            return;
        }

//        log(ast, "CHECK {0}={1} => {2}", ast, ast.getType(), whitespaceable);

        boolean mustBeWhitespaceBefore    = this.whitespaceBefore.contains(javaElement);
        boolean mustNotBeWhitespaceBefore = this.noWhitespaceBefore.contains(javaElement);

        boolean mustBeWhitespaceAfter    = this.whitespaceAfter.contains(javaElement);
        boolean mustNotBeWhitespaceAfter = this.noWhitespaceAfter.contains(javaElement);

        // Short-circuit.
        if (
            !mustBeWhitespaceBefore
            && !mustNotBeWhitespaceBefore
            && !mustBeWhitespaceAfter
            && !mustNotBeWhitespaceAfter
        ) return;

        final String line = this.getLines()[Cs820.getLineNo(ast) - 1];

        // Check whitespace BEFORE token.
        if (mustBeWhitespaceBefore || mustNotBeWhitespaceBefore) {
            int before = Cs820.getColumnNo(ast) - 1;

            if (before > 0 && !Whitespace.LINE_PREFIX.matcher(line).region(0, before).matches()) {
                boolean isWhitespace = Character.isWhitespace(line.charAt(before));
                if (mustBeWhitespaceBefore && !isWhitespace) {
                    this.log(ast, Whitespace.MESSAGE_KEY_NOT_PRECEDED, Cs820.getText(ast), javaElement);
                } else
                if (mustNotBeWhitespaceBefore && isWhitespace) {
                    this.log(ast, Whitespace.MESSAGE_KEY_PRECEDED, Cs820.getText(ast), javaElement);
                }
            }
        }

        // Check whitespace AFTER token.
        if (mustBeWhitespaceAfter || mustNotBeWhitespaceAfter) {
            int after = Cs820.getColumnNo(ast) + Cs820.getText(ast).length();

            if (after < line.length() && !Whitespace.LINE_SUFFIX.matcher(line).region(after, line.length()).matches()) {
                boolean isWhitespace = Character.isWhitespace(line.charAt(after));
                if (mustBeWhitespaceAfter && !isWhitespace) {
                    this.log(Cs820.getLineNo(ast), after, Whitespace.MESSAGE_KEY_NOT_FOLLOWED, Cs820.getText(ast), javaElement);
                } else
                if (mustNotBeWhitespaceAfter && isWhitespace) {
                    this.log(Cs820.getLineNo(ast), after, Whitespace.MESSAGE_KEY_FOLLOWED, Cs820.getText(ast), javaElement);
                }
            }
        }
    }
    private static final Pattern LINE_PREFIX = Pattern.compile("\\s*");
    private static final Pattern LINE_SUFFIX = Pattern.compile("\\s*(?://.*)?");

    @Override public int[]
    getAcceptableTokens() {
        return LocalTokenType.delocalize(new LocalTokenType[] {
            LocalTokenType.ABSTRACT,
            LocalTokenType.ANNOTATION,
            LocalTokenType.ANNOTATION_ARRAY_INIT,
            LocalTokenType.ANNOTATION_DEF,
            LocalTokenType.ANNOTATION_FIELD_DEF,
            LocalTokenType.ANNOTATION_MEMBER_VALUE_PAIR,
            LocalTokenType.ANNOTATIONS,
            LocalTokenType.ARRAY_DECLARATOR,
            LocalTokenType.ARRAY_INIT,
            LocalTokenType.ASSIGN,
            LocalTokenType.AT,
            LocalTokenType.BAND,
            LocalTokenType.BAND_ASSIGN,
            LocalTokenType.BNOT,
            LocalTokenType.BOR,
            LocalTokenType.BOR_ASSIGN,
            LocalTokenType.BSR,
            LocalTokenType.BSR_ASSIGN,
            LocalTokenType.BXOR,
            LocalTokenType.BXOR_ASSIGN,
            LocalTokenType.CASE_GROUP,
            LocalTokenType.CHAR_LITERAL,
            LocalTokenType.CLASS_DEF,
            LocalTokenType.COLON,
            LocalTokenType.COMMA,
            LocalTokenType.CTOR_CALL,
            LocalTokenType.CTOR_DEF,
            LocalTokenType.DEC,
            LocalTokenType.DIV,
            LocalTokenType.DIV_ASSIGN,
            LocalTokenType.DO_WHILE,
            LocalTokenType.DOT,
            LocalTokenType.ELIST,
            LocalTokenType.ELLIPSIS,
            LocalTokenType.EMPTY_STAT,
            LocalTokenType.ENUM,
            LocalTokenType.ENUM_CONSTANT_DEF,
            LocalTokenType.ENUM_DEF,
            LocalTokenType.EOF,
            LocalTokenType.EQUAL,
            LocalTokenType.EXPR,
            LocalTokenType.EXTENDS_CLAUSE,
            LocalTokenType.FINAL,
            LocalTokenType.FOR_CONDITION,
            LocalTokenType.FOR_EACH_CLAUSE,
            LocalTokenType.FOR_INIT,
            LocalTokenType.FOR_ITERATOR,
            LocalTokenType.GE,
            LocalTokenType.GENERIC_END,
            LocalTokenType.GENERIC_START,
            LocalTokenType.GT,
            LocalTokenType.IDENT,
            LocalTokenType.IMPLEMENTS_CLAUSE,
            LocalTokenType.IMPORT,
            LocalTokenType.INC,
            LocalTokenType.INDEX_OP,
            LocalTokenType.INSTANCE_INIT,
            LocalTokenType.INTERFACE_DEF,
            LocalTokenType.LABELED_STAT,
            LocalTokenType.LAND,
            LocalTokenType.LCURLY,
            LocalTokenType.LE,
            LocalTokenType.LITERAL_ASSERT,
            LocalTokenType.LITERAL_BOOLEAN,
            LocalTokenType.LITERAL_BREAK,
            LocalTokenType.LITERAL_BYTE,
            LocalTokenType.LITERAL_CASE,
            LocalTokenType.LITERAL_CATCH,
            LocalTokenType.LITERAL_CHAR,
            LocalTokenType.LITERAL_CLASS,
            LocalTokenType.LITERAL_CONTINUE,
            LocalTokenType.LITERAL_DEFAULT,
            LocalTokenType.LITERAL_DO,
            LocalTokenType.LITERAL_DOUBLE,
            LocalTokenType.LITERAL_ELSE,
            LocalTokenType.LITERAL_FALSE,
            LocalTokenType.LITERAL_FINALLY,
            LocalTokenType.LITERAL_FLOAT,
            LocalTokenType.LITERAL_FOR,
            LocalTokenType.LITERAL_IF,
            LocalTokenType.LITERAL_INSTANCEOF,
            LocalTokenType.LITERAL_INT,
            LocalTokenType.LITERAL_INTERFACE,
            LocalTokenType.LITERAL_LONG,
            LocalTokenType.LITERAL_NATIVE,
            LocalTokenType.LITERAL_NEW,
            LocalTokenType.LITERAL_NULL,
            LocalTokenType.LITERAL_PRIVATE,
            LocalTokenType.LITERAL_PROTECTED,
            LocalTokenType.LITERAL_PUBLIC,
            LocalTokenType.LITERAL_RETURN,
            LocalTokenType.LITERAL_SHORT,
            LocalTokenType.LITERAL_STATIC,
            LocalTokenType.LITERAL_SUPER,
            LocalTokenType.LITERAL_SWITCH,
            LocalTokenType.LITERAL_SYNCHRONIZED,
            LocalTokenType.LITERAL_THIS,
            LocalTokenType.LITERAL_THROW,
            LocalTokenType.LITERAL_THROWS,
            LocalTokenType.LITERAL_TRANSIENT,
            LocalTokenType.LITERAL_TRUE,
            LocalTokenType.LITERAL_TRY,
            LocalTokenType.LITERAL_VOID,
            LocalTokenType.LITERAL_VOLATILE,
            LocalTokenType.LITERAL_WHILE,
            LocalTokenType.LNOT,
            LocalTokenType.LOR,
            LocalTokenType.LPAREN,
            LocalTokenType.LT,
            LocalTokenType.METHOD_CALL,
            LocalTokenType.METHOD_DEF,
            LocalTokenType.METHOD_REF,
            LocalTokenType.MINUS,
            LocalTokenType.MINUS_ASSIGN,
            LocalTokenType.MOD,
            LocalTokenType.MOD_ASSIGN,
            LocalTokenType.MODIFIERS,
            LocalTokenType.NOT_EQUAL,
            LocalTokenType.NUM_DOUBLE,
            LocalTokenType.NUM_FLOAT,
            LocalTokenType.NUM_INT,
            LocalTokenType.NUM_LONG,
            LocalTokenType.OBJBLOCK,
            LocalTokenType.PACKAGE_DEF,
            LocalTokenType.PARAMETER_DEF,
            LocalTokenType.PARAMETERS,
            LocalTokenType.PLUS,
            LocalTokenType.PLUS_ASSIGN,
            LocalTokenType.POST_DEC,
            LocalTokenType.POST_INC,
            LocalTokenType.QUESTION,
            LocalTokenType.RBRACK,
            LocalTokenType.RCURLY,
            LocalTokenType.RESOURCE,
            LocalTokenType.RESOURCE_SPECIFICATION,
            LocalTokenType.RESOURCES,
            LocalTokenType.RPAREN,
            LocalTokenType.SEMI,
            LocalTokenType.SL,
            LocalTokenType.SL_ASSIGN,
            LocalTokenType.SLIST,
            LocalTokenType.SR,
            LocalTokenType.SR_ASSIGN,
            LocalTokenType.STAR,
            LocalTokenType.STAR_ASSIGN,
            LocalTokenType.STATIC_IMPORT,
            LocalTokenType.STATIC_INIT,
            LocalTokenType.STRICTFP,
            LocalTokenType.STRING_LITERAL,
            LocalTokenType.SUPER_CTOR_CALL,
            LocalTokenType.TYPE,
            LocalTokenType.TYPE_ARGUMENT,
            LocalTokenType.TYPE_ARGUMENTS,
            LocalTokenType.TYPE_EXTENSION_AND,
            LocalTokenType.TYPE_LOWER_BOUNDS,
            LocalTokenType.TYPE_PARAMETER,
            LocalTokenType.TYPE_PARAMETERS,
            LocalTokenType.TYPE_UPPER_BOUNDS,
            LocalTokenType.TYPECAST,
            LocalTokenType.UNARY_MINUS,
            LocalTokenType.UNARY_PLUS,
            LocalTokenType.VARIABLE_DEF,
            LocalTokenType.WILDCARD_TYPE,
        });
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }
}
