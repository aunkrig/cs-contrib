
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

package de.unkrig.cscontrib.util;

/**
 * The elements of the Java programming languages that are represented by exactly one token.
 *
 * @see AstUtil#toJavaElement(com.puppycrawl.tools.checkstyle.api.DetailAST)
 */
public
enum JavaElement {

    // SUPPRESS CHECKSTYLE NameSpelling:3000

    /**
     * The "{@code abstract}" keyword in a class or method declaration.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">abstract</font> class MyClass { ... }</tt></dd>
     *   <dd><tt><font color="red">abstract</font> myMethod(...) { ... }</tt></dd>
     * </dl>
     */
    ABSTRACT,

    /**
     * The "{@code &}" operator in an arithmetic expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">&amp;</font> b</tt></dd>
     * </dl>
     */
    AND__EXPR,

    /**
     * The "{@code &}" operator that separates the bounds in a wildcard type argument.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>&lt;T extends MyClass <font color="red">&amp;</font> MyInterface></tt></dd>
     * </dl>
     */
    AND__TYPE_BOUND,

    /**
     * The "{@code &=}" operator in an assignment.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">&amp;=</font> b</tt></dd>
     * </dl>
     */
    AND_ASSIGN,

    /**
     * The "{@code assert}" keyword in an assert statement.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">assert</font> x == 0;</tt></dd>
     *   <dd><tt><font color="red">assert</font> x == 0 : "x not zero";</tt></dd>
     * </dl>
     */
    ASSERT,

    /**
     * The "{@code =}" operator in an assignment.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">=</font> 7;</tt></dd>
     * </dl>
     */
    ASSIGN__ASSIGNMENT,

    /**
     * The "{@code =}" operator in a field or local variable declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>int a <font color="red">=</font> 7;</tt></dd>
     * </dl>
     */
    ASSIGN__VAR_DECL,

    /**
     * The "{@code @}" operator before an annotation.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">@</font>MyAnno</tt></dd>
     * </dl>
     */
    AT__ANNO,

    /**
     * The "{@code @}" operator in an annotation type declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>interface <font color="red">@</font>MyAnno {</tt></dd>
     * </dl>
     */
    AT__ANNO_DECL,

    /**
     * The "{@code ~}" bitwise complement operator in an arithmetic expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">~</font>a</tt></dd>
     * </dl>
     */
    BITWISE_COMPLEMENT,

    /**
     * The "{@code boolean}" keyword that designates the primitive type.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">boolean</font></tt></dd>
     * </dl>
     */
    BOOLEAN,

    /**
     * The "{@code break}" keyword in a BREAK statement.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">break</font>;</tt></dd>
     *   <dd><tt><font color="red">break</font> LABEL;</tt></dd>
     * </dl>
     */
    BREAK,

    /**
     * The "{@code byte}" keyword that designates the primitive type.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">byte</font></tt></dd>
     * </dl>
     */
    BYTE,

    /**
     * The "{@code case}" keyword that introduces a switch-block-statement-group within a SWITCH statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">case</font> 7:</tt></dd>
     * </dl>
     */
    CASE,

    /**
     * The "{@code catch}" keyword in a TRY statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">catch</font> (Exception e) {</tt></dd>
     * </dl>
     */
    CATCH,

    /**
     * The "{@code char}" keyword that designates the primitive type.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">char</font></tt></dd>
     * </dl>
     */
    CHAR,

    /**
     * A character literal in an arithmetic expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">'c'</font></tt></dd>
     * </dl>
     */
    CHAR_LITERAL,

    /**
     * The "{@code class}" keyword in a class declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">class</font> MyClass {</tt></dd>
     * </dl>
     */
    CLASS__CLASS_DECL,

    /**
     * The "{@code class}" keyword in a class literal.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>Class c = Object.<font color="red">class</font>;</tt></dd>
     * </dl>
     */
    CLASS__CLASS_LITERAL,

    /**
     * The "{@code :}" operator in a switch-block-statement-group in a SWITCH statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>case 77<font color="red">:</font></tt></dd>
     * </dl>
     */
    COLON__CASE,

    /**
     * The "{@code :}" operator after the "{@code default}" keyword in a SWITCH statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>default<font color="red">:</font></tt></dd>
     * </dl>
     */
    COLON__DEFAULT,

    /**
     * The "{@code :}" operator in an enhanced FOR statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (Object o <font color="red">:</font> list) {</tt></dd>
     * </dl>
     */
    COLON__ENHANCED_FOR,

    /**
     * The "{@code :}" in a labeled statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>LABEL<font color="red">:</font> while (...) {</tt></dd>
     * </dl>
     */
    COLON__LABELED_STAT,

    /**
     * The "{@code :}" operator in a conditional expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a ? b <font color="red">:</font> c</tt></dd>
     * </dl>
     */
    COLON__TERNARY,

    /**
     * The "{@code ,}" in a parameter list, an argument list, array initializer, or superinterfaces list.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">,</font></tt></dd>
     * </dl>
     */
    COMMA,

    /**
     * The conditional "{@code &&}" operator in a logical expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">&&</font> b</tt></dd>
     * </dl>
     */
    CONDITIONAL_AND,

    /**
     * The conditional "{@code ||}" operator in a logical expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">||</font> b</tt></dd>
     * </dl>
     */
    CONDITIONAL_OR,

    /**
     * The "{@code continue}" keyword in a CONTINUE statement.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">continue</font>;</tt></dd>
     *   <dd><tt><font color="red">continue</font> LABEL;</tt></dd>
     * </dl>
     */
    CONTINUE,

    /**
     * The "{@code default}" keyword in an annotation element.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>interface @MyAnno { String engineer() <font color="red">default</font> "[unassigned]"; }</tt></dd>
     * </dl>
     */
    DEFAULT__ANNO_ELEM,

    /**
     * The "{@code default}" keyword in a SWITCH statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>switch (x) { <font color="red">default</font>: break; }</tt></dd>
     * </dl>
     */
    DEFAULT__SWITCH,

    /**
     * The "{@code default}" modifier in an interface method declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>interface Foo { <font color="red">default</font> void meth() {} }</tt></dd>
     * </dl>
     */
    DEFAULT__MOD,

    /**
     * The division operator "{@code /}" in an arithmetic expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">/</font> b</tt></dd>
     * </dl>
     */
    DIVIDE,

    /**
     * The "{@code /=}" operator in an assignment.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">/=</font> b</tt></dd>
     * </dl>
     */
    DIVIDE_ASSIGN,

    /**
     * The "{@code do}" keyword in a DO statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">do</font> { ... } while (...);</tt></dd>
     * </dl>
     */
    DO,

    /**
     * The "{@code .}" in an import declaration.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>import pkg<font color="red">.</font>*;</tt></dd>
     *   <dd><tt>import pkg<font color="red">.</font>Type;</tt></dd>
     * </dl>
     */
    DOT__IMPORT,

    /**
     * The "{@code .}" in a package declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>package pkg<font color="red">.</font>pkg;</tt></dd>
     * </dl>
     */
    DOT__PACKAGE_DECL,

    /**
     * The "{@code .}" in a qualified type.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>pkg<font color="red">.</font>MyType</tt></dd>
     *   <dd><tt>pkg<font color="red">.</font>MyType[]</tt></dd>
     * </dl>
     */
    DOT__QUALIFIED_TYPE,

    /**
     * The "{@code .}" that selects a member inan expression.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>a<font color="red">.</font>b</tt></dd>
     *   <dd><tt>a<font color="red">.</font>b()</tt></dd>
     * </dl>
     */
    DOT__SELECTOR,

    /**
     * The "{@code double}" keyword that designates the primitive type.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">double</font></tt></dd>
     * </dl>
     */
    DOUBLE,

    /**
     * A DOUBLE literal.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">1.0</font></tt></dd>
     *   <dd><tt><font color="red">.1</font></tt></dd>
     *   <dd><tt><font color="red">1E3</font></tt></dd>
     *   <dd><tt><font color="red">1D</font></tt></dd>
     * </dl>
     */
    DOUBLE_LITERAL,

    /**
     * The "{@code ...}" after the type in a method declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>meth(Object<font color="red">...</font> o)</tt></dd>
     * </dl>
     */
    ELLIPSIS,

    /**
     * The "{@code else}" keyword in an IF statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>if (...) { ... } <font color="red">else</font> { ... }</tt></dd>
     * </dl>
     */
    ELSE,

    /**
     * The "{@code enum}" keyword in an ENUM declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>public <font color="red">enum</font> Color { RED, BLUE, GREEN }</tt></dd>
     * </dl>
     */
    ENUM,

    /**
     * The "{@code ==}" operator in an expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">==</font> b</tt></dd>
     * </dl>
     */
    EQUAL,

    /**
     * The "{@code extends}" keyword in the EXTENDS clause of a class or interface declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass <font color="red">extends</font> BaseClass {</tt></dd>
     * </dl>
     */
    EXTENDS__TYPE,

    /**
     * The "{@code extends}" keyword in a type bound.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>List&lt;T <font color="red">extends</font> MyClass></tt></dd>
     * </dl>
     */
    EXTENDS__TYPE_BOUND,

    /**
     * The "{@code false}" keyword that designates the boolean constant.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">false</font></tt></dd>
     * </dl>
     */
    FALSE,

    /**
     * The "{@code final }" modifier in a field, parameter or local variable declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">final</font></tt></dd>
     * </dl>
     */
    FINAL,

    /**
     * The "{@code finally}" keyword in a SWITCH statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... } <font color="red">finally</font> { ... }</tt></dd>
     * </dl>
     */
    FINALLY,

    /**
     * The "{@code float}" keyword that designates the primitive type.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">float</font></tt></dd>
     * </dl>
     */
    FLOAT,

    /**
     * A FLOAT literal.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">1F</font></tt></dd>
     * </dl>
     */
    FLOAT_LITERAL,

    /**
     * The "{@code for}" keyword in a FOR statement or an ENHANCED FOR statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">for</font> (int i = 0; i < 3; i++) {</tt></dd>
     *   <dd><tt><font color="red">for</font> (Object o : list) {</tt></dd>
     * </dl>
     */
    FOR,

    /**
     * The "{@code >}" operator in an arithmetic expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">></font> b</tt></dd>
     * </dl>
     */
    GREATER,

    /**
     * The "{@code >=}" operator in an arithmetic expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">>=</font> b</tt></dd>
     * </dl>
     */
    GREATER_EQUAL,

    /**
     * The "{@code if}" keyword in an IF statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">if</font> (a == 0) {</tt></dd>
     * </dl>
     */
    IF,

    /**
     * The "{@code implements}" keyword in a class declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>List&lt;T <font color="red">implements</font> MyInterface1, MyInterface2></tt></dd>
     * </dl>
     */
    IMPLEMENTS,

    /**
     * The "{@code import}" keyword in a (non-static) import declaration.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">import</font> pkg.MyClass;</tt></dd>
     *   <dd><tt><font color="red">import</font> pkg.*;</tt></dd>
     * </dl>
     */
    IMPORT,

    /**
     * The "{@code import}" keyword in a static import declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">import</font> static pkg.MyClass.member;</tt></dd>
     *   <dd><tt><font color="red">import</font> static pkg.MyClass.*;</tt></dd>
     * </dl>
     */
    IMPORT__STATIC_IMPORT,

    /**
     * The "{@code instanceof}" operator in an arithmetic expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">instanceof</font> MyClass</tt></dd>
     * </dl>
     */
    INSTANCEOF,

    /**
     * The "{@code int}" keyword that designates the primitive type.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">int</font></tt></dd>
     * </dl>
     */
    INT,

    /**
     * An INT literal.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">11</font></tt></dd>
     *   <dd><tt><font color="red">0xB</font></tt></dd>
     *   <dd><tt><font color="red">013</font></tt></dd>
     *   <dd><tt><font color="red">0B1011</font></tt></dd>
     * </dl>
     */
    INT_LITERAL,

    /**
     * The "{@code interface}" keyword in an interface or annotation type declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>public <font color="red">interface</font> MyInterface { ... }</tt></dd>
     * </dl>
     */
    INTERFACE,

    /**
     * The left angle bracket "{@code <}" in the type parameters of a method declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>public <font color="red">&lt;</font>T extends Number> void meth(T parm) {</tt></dd>
     * </dl>
     */
    L_ANGLE__METH_DECL_TYPE_PARAMS,

    /**
     * The left angle bracket "{@code <}" in the type arguments of a method invocation.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>MyClass.<font color="red">&lt;</font>Double>meth(x)</tt></dd>
     * </dl>
     */
    L_ANGLE__METH_INVOCATION_TYPE_ARGS,

    /**
     * The left angle bracket "{@code <}" in type arguments.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>MyClass<font color="red">&lt;</font>String> x;</tt></dd>
     * </dl>
     */
    L_ANGLE__TYPE_ARGS,

    /**
     * The left angle bracket "{@code <}" keyword in parametrized class or interface declarations.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass<font color="red">&lt;</font>T extends Number> {</tt></dd>
     * </dl>
     */
    L_ANGLE__TYPE_PARAMS,

    /**
     * The left bracket "{@code [}" in an array type.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>Object<font color="red">[</font>]</tt></dd>
     * </dl>
     */
    L_BRACK__ARRAY_DECL,

    /**
     * The left bracket "{@code [}" in an array element access.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a<font color="red">[</font>3]</tt></dd>
     * </dl>
     */
    L_BRACK__INDEX,

    /**
     * The left curly brace "<code>{</code>" in an annotation element array value.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@SuppressWarnings(<font color="red">{</font> "foo", "bar" })</tt></dd>
     * </dl>
     */
    L_CURLY__ANNO_ARRAY_INIT,

    /**
     * The left curly brace "<code>{</code>" in an anonymous class declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>new Object() <font color="red">{</font> ... }</tt></dd>
     * </dl>
     */
    L_CURLY__ANON_CLASS,

    /**
     * The left curly brace "<code>{</code>" in an array initializer.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>int[] ia = <font color="red">{</font> 1, 2 }</tt></dd>
     *   <dd><tt>new int[] <font color="red">{</font> 1, 2 }</tt></dd>
     * </dl>
     */
    L_CURLY__ARRAY_INIT,

    /**
     * The left curly brace "<code>{</code>" that starts a block.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">{</font> int i = 0; i++; }</tt></dd>
     * </dl>
     */
    L_CURLY__BLOCK,

    /**
     * The left curly brace "<code>{</code>" after the "{@code catch}" keyword in a TRY statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... } catch (...) <font color="red">{</font> ...</tt></dd>
     * </dl>
     */
    L_CURLY__CATCH,

    /**
     * The left curly brace "<code>{</code>" after the "{@code do}" keyword in a DO statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>do <font color="red">{</font> ... } while (...);</tt></dd>
     * </dl>
     */
    L_CURLY__DO,

    /**
     * The left curly brace "<code>{</code>" in an empty annotation element array value.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@SuppressWarnings(<font color="red">{</font>})</tt></dd>
     * </dl>
     */
    L_CURLY__EMPTY_ANNO_ARRAY_INIT,

    /**
     * The left curly brace "<code>{</code>" in an empty anonymous class declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>new Object() <font color="red">{</font>}</tt></dd>
     * </dl>
     */
    L_CURLY__EMPTY_ANON_CLASS,

    /**
     * The left curly brace "<code>{</code>" in an empty array initializer.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>int[] ia = <font color="red">{</font>};</tt></dd>
     *   <dd><tt>new int[] <font color="red">{</font>}</tt></dd>
     * </dl>
     */
    L_CURLY__EMPTY_ARRAY_INIT,

    /**
     * The left curly brace "<code>{</code>" of an empty CATCH clause in a SWITCH statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... } catch (...) <font color="red">{</font>}</tt></dd>
     * </dl>
     */
    L_CURLY__EMPTY_CATCH,

    /**
     * The left curly brace "<code>{</code>" that designates the beginning of the method body in method declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>void meth(...) <font color="red">{</font>}</tt></dd>
     * </dl>
     */
    L_CURLY__EMPTY_METH_DECL,

    /**
     * The left curly brace of an empty type declaration.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>class MyClass() <font color="red">{</font>}</tt></dd>
     *   <dd><tt>interface MyInterface() <font color="red">{</font>}</tt></dd>
     *   <dd><tt>interface @MyAnnotation <font color="red">{</font>}</tt></dd>
     *   <dd><tt>enum MyEnum <font color="red">{</font>}</tt></dd>
     * </dl>
     */
    L_CURLY__EMPTY_TYPE_DECL,

    /**
     * The left curly brace of the class body of an enum constant.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd>
     *     <pre>enum MyEnum {
     *    FOO <font color="red">{</font>
     *        &#64;Override public String toString() { return ""; }
     *    }
     *}</pre>
     *   </dd>
     * </dl>
     */
    L_CURLY__ENUM_CONST,

    /**
     * The left curly brace following the FINALLY keyword.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>finally <font color="red">{</font> ... }</tt></dd>
     * </dl>
     */
    L_CURLY__FINALLY,

    /**
     * The left curly brace of a FOR statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (...) <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__FOR,

    /**
     * The left curly brace of an IF statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>if (...) <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__IF,

    /**
     * The left curly brace of an instance initializer.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass { <font color="red">{</font> ... } }</tt></dd>
     * </dl>
     */
    L_CURLY__INSTANCE_INIT,

    /**
     * The left curly brace of a the block that follows a label.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>LABEL: <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__LABELED_STAT,

    /**
     * The left curly brace of a method declaration.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>void meth(...) <font color="red">{</font> ... }</tt></dd>
     * </dl>
     */
    L_CURLY__METH_DECL,

    /**
     * The left curly brace of a static initializer.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass { static <font color="red">{</font> ... } }</tt></dd>
     * </dl>
     */
    L_CURLY__STATIC_INIT,

    /**
     * The left curly brace of a SWITCH statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>switch (a) <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__SWITCH,

    /**
     * The left curly brace of a SYNCHRONIZED statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>synchronized (a) <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__SYNCHRONIZED,

    /**
     * The left curly brace after the TRY keyword.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__TRY,

    /**
     * The left curly brace "<code>{</code>" in a class, interface, enum or annotation type declaration.
     * <dl>
     *   <dd><tt>class MyClass() <font color="red">{</font></tt></dd>
     *   <dd><tt>interface MyInterface() <font color="red">{</font></tt></dd>
     *   <dd><tt>interface @MyAnno <font color="red">{</font></tt></dd>
     *   <dd><tt>enum MyEnum <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__TYPE_DECL,

    /**
     * The left curly brace of a WHILE statement.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>while (...) <font color="red">{</font></tt></dd>
     * </dl>
     */
    L_CURLY__WHILE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@SuppressWarnings<font color="red">(</font>"foo")</tt></dd>
     * </dl>
     */
    L_PAREN__ANNO,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>interface @MyAnno { String engineer<font color="red">(</font>); }</tt></dd>
     * </dl>
     */
    L_PAREN__ANNO_ELEM_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">(</font>int) a</tt></dd>
     * </dl>
     */
    L_PAREN__CAST,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... } catch <font color="red">(</font>Exception e) {</tt></dd>
     * </dl>
     */
    L_PAREN__CATCH,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>do { ... } while <font color="red">(</font>...);</tt></dd>
     * </dl>
     */
    L_PAREN__DO_WHILE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for <font color="red">(</font>int i = 0; i  10; i++) {</tt></dd>
     * </dl>
     */
    L_PAREN__FOR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for <font color="red">(</font>; i  10; i++) {</tt></dd>
     * </dl>
     */
    L_PAREN__FOR_NO_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>if <font color="red">(</font>...) {</tt></dd>
     * </dl>
     */
    L_PAREN__IF,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>Runnable ru = <font color="red">(</font>) -> { ...</tt></dd>
     * </dl>
     */
    L_PAREN__LAMBDA_PARAMS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a<font color="red">(</font>x, y)</tt></dd>
     * </dl>
     */
    L_PAREN__METH_INVOCATION,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>void meth<font color="red">(</font>int x, int y) {</tt></dd>
     * </dl>
     */
    L_PAREN__PARAMS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">(</font>a + b) * c</tt></dd>
     * </dl>
     */
    L_PAREN__PARENTHESIZED,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try <font color="red">(</font>Reader r = ...) { ...</tt></dd>
     * </dl>
     */
    L_PAREN__RESOURCES,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">&lt;&lt;</font> 3</tt></dd>
     * </dl>
     */
    LEFT_SHIFT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">&lt;&lt;=</font> 1</tt></dd>
     * </dl>
     */
    LEFT_SHIFT_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">&lt;</font> b</tt></dd>
     * </dl>
     */
    LESS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">&lt;=</font> b</tt></dd>
     * </dl>
     */
    LESS_EQUAL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">!</font>a</tt></dd>
     * </dl>
     */
    LOGICAL_COMPLEMENT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">long</font></tt></dd>
     * </dl>
     */
    LONG,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">11L</font></tt></dd>
     *   <dd><tt><font color="red">0xBL</font></tt></dd>
     *   <dd><tt><font color="red">013L</font></tt></dd>
     *   <dd><tt><font color="red">0B1011L</font></tt></dd>
     * </dl>
     */
    LONG_LITERAL,

    /**
     * The {@code ::} operator in a method reference.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>System.out<font color="red">::</font>println</tt></dd>
     *   <dd><tt>String<font color="red">::</font>length</tt></dd>
     *   <dd><tt>super<font color="red">::</font>toString</tt></dd>
     *   <dd><tt>ArrayList<String><font color="red">::</font>new</tt></dd>
     *   <dd><tt>int[]<font color="red">::</font>new</tt></dd>
     * </dl>
     */
    METH_REF,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">-</font> b</tt></dd>
     * </dl>
     */
    MINUS__ADDITIVE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">-</font>a</tt></dd>
     * </dl>
     */
    MINUS__UNARY,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">-=</font> b</tt></dd>
     * </dl>
     */
    MINUS_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">%</font> b</tt></dd>
     * </dl>
     */
    MODULO,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">%=</font> b</tt></dd>
     * </dl>
     */
    MODULO_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">*</font> b</tt></dd>
     * </dl>
     */
    MULTIPLY,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">*=</font> b</tt></dd>
     * </dl>
     */
    MULTIPLY_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">a</font>.<font color="red">b</font>.<font color="red">c</font></tt></dd>
     * </dl>
     */
    NAME__AMBIGUOUS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@<font color="red">MyAnnotation</font>("x")</tt></dd>
     * </dl>
     */
    NAME__ANNO,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>interface @MyAnno { String <font color="red">engineer</font>(); }</tt></dd>
     * </dl>
     */
    NAME__ANNO_ELEM_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@MyAnnotation(<font color="red">value</font> = "x")</tt></dd>
     * </dl>
     */
    NAME__ANNO_MEMBER,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">MyClass</font>(...) {</tt></dd>
     * </dl>
     */
    NAME__CTOR_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>import <font color="red">pkg</font>.<font color="red">pkg</font>.*;</tt></dd>
     * </dl>
     */
    NAME__IMPORT_COMPONENT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>import pkg.pkg.<font color="red">MyType</font>;</tt></dd>
     * </dl>
     */
    NAME__IMPORT_TYPE,

    /**
     * The name of an implicitly typed parameter in a lambda.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>Foo foo = (<font color="red">param</font>) -> {};</tt></dd>
     * </dl>
     *
     * @see #NAME__PARAM
     */
    NAME__INFERRED_PARAM,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>void <font color="red">main</font>(...) {</tt></dd>
     * </dl>
     */
    NAME__METH_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>package <font color="red">pkg</font>.<font color="red">pkg</font>.
     *<font color="red">pkg</font>;</tt></dd>
     * </dl>
     */
    NAME__PACKAGE_DECL,

    /**
     * The name of a parameter in method or constructor declaration, or the name of an explicitly typed parameter in a
     * lambda.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>void meth(String <font color="red">param</font>)</tt></dd>
     *   <dd><tt>Foo foo = (String <font color="red">param</font>) -> {};</tt> (since Java 8)</dd>
     * </dl>
     *
     * @see #NAME__INFERRED_PARAM
     */
    NAME__PARAM,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">pkg</font>.<font color="red">MyType</font></tt></dd>
     * </dl>
     */
    NAME__QUALIFIED_TYPE,

    /**
     * TODO.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">MyType</font> x;</tt></dd>
     *   <dd><tt>y = new <font color="red">MyType</font>();</tt></dd>
     * </dl>
     */
    NAME__SIMPLE_TYPE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class <font color="red">MyClass</font> { ... }</tt></dd>
     *   <dd><tt>interface <font color="red">MyInterface</font> { ... }</tt></dd>
     *   <dd><tt>interface @<font color="red">MyAnnotation</font> { ... }</tt></dd>
     *   <dd><tt>enum <font color="red">MyEnum</font> { ... }</tt></dd>
     * </dl>
     */
    NAME__TYPE_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>int <font color="red">a</font>;</tt></dd>
     * </dl>
     */
    NAME__LOCAL_VAR_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">native</font></tt></dd>
     * </dl>
     */
    NATIVE,

    /**
     * The {@code new} keyword in a class instance or array creation expression.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">new</font> ArrayList()</tt></dd>
     *   <dd><tt><font color="red">new</font> int[3]</tt></dd>
     * </dl>
     */
    NEW,

    /**
     * The {@code new} keyword in a method reference.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>ArrayList::<font color="red">new</font></tt></dd>
     *   <dd><tt>int[]::<font color="red">new</font></tt></dd>
     * </dl>
     */
    NEW__METH_REF,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">!=</font> b</tt></dd>
     * </dl>
     */
    NOT_EQUAL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">null</font></tt></dd>
     * </dl>
     */
    NULL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">|</font> b</tt></dd>
     * </dl>
     */
    OR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">|=</font> b</tt></dd>
     * </dl>
     */
    OR_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">package</font> ...;</tt></dd>
     * </dl>
     */
    PACKAGE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">+</font> b</tt></dd>
     * </dl>
     */
    PLUS__ADDITIVE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">+</font>(a + b)</tt></dd>
     * </dl>
     */
    PLUS__UNARY,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">+=</font> b</tt></dd>
     * </dl>
     */
    PLUS_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>x<font color="red">--</font></tt></dd>
     * </dl>
     */
    POST_DECR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>x<font color="red">++</font></tt></dd>
     * </dl>
     */
    POST_INCR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">--</font>x</tt></dd>
     * </dl>
     */
    PRE_DECR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">++</font>x</tt></dd>
     * </dl>
     */
    PRE_INCR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">private</font></tt></dd>
     * </dl>
     */
    PRIVATE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">protected</font></tt></dd>
     * </dl>
     */
    PROTECTED,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">public</font></tt></dd>
     * </dl>
     */
    PUBLIC,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">?</font> b : c</tt></dd>
     * </dl>
     */
    QUESTION__TERNARY,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>List&lt;<font color="red">?</font> extends InputStream></tt></dd>
     * </dl>
     */
    QUESTION__WILDCARD_TYPE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>public &lt;T extends Number<font color="red">></font> void meth(T parm) {</tt></dd>
     * </dl>
     */
    R_ANGLE__METH_DECL_TYPE_PARAMS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>MyClass.&lt;Double<font color="red">></font>meth(x)</tt></dd>
     * </dl>
     */
    R_ANGLE__METH_INVOCATION_TYPE_ARGS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>MyClass&lt;String<font color="red">></font></tt></dd>
     * </dl>
     */
    R_ANGLE__TYPE_ARGS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass&lt;T extends Number<font color="red">></font> {</tt></dd>
     * </dl>
     */
    R_ANGLE__TYPE_PARAMS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>Object[<font color="red">]</font></tt></dd>
     * </dl>
     */
    R_BRACK__ARRAY_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a[3<font color="red">]</font></tt></dd>
     * </dl>
     */
    R_BRACK__INDEX,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@SuppressWarnings({ "foo", "bar" <font color="red">}</font>)</tt></dd>
     * </dl>
     */
    R_CURLY__ANNO_ARRAY_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>new Object() { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__ANON_CLASS,

    /**
     * TODO.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>int[] ia = { 1, 2 <font color="red">};</font></tt></dd>
     *   <dd><tt>b = new int[] { 1, 2 <font color="red">}</font>;</tt></dd>
     * </dl>
     */
    R_CURLY__ARRAY_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>{ int i = 0; i++; <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__BLOCK,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... } catch (...) { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__CATCH,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>do { ... <font color="red">}</font> while (...);</tt></dd>
     * </dl>
     */
    R_CURLY__DO,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>else { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__ELSE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@SuppressWarnings({<font color="red">}</font>)</tt></dd>
     * </dl>
     */
    R_CURLY__EMPTY_ANNO_ARRAY_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>new Object() {<font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__EMPTY_ANON_CLASS,

    /**
     * TODO.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>int[] ia = {<font color="red">}</font>;</tt></dd>
     *   <dd><tt>b = new int[] {<font color="red">}</font>;</tt></dd>
     * </dl>
     */
    R_CURLY__EMPTY_ARRAY_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... } catch (...) {<font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__EMPTY_CATCH,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>() {<font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__EMPTY_LAMBDA,

    /**
     * TODO.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>public MyClass(...) {<font color="red">}</font></tt></dd>
     *   <dd><tt>public method(...) {<font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__EMPTY_METH_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass {<font color="red">}</font></tt></dd>
     *   <dd><tt>interface MyInterface {<font color="red">}</font></tt></dd>
     *   <dd><tt>@interface MyAnnotation {<font color="red">}</font></tt></dd>
     *   <dd><tt>enum MyEnum {<font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__EMPTY_TYPE_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>enum MyEnum { FOO { ... <font color="red">}</font> }</tt></dd>
     * </dl>
     */
    R_CURLY__ENUM_CONST_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>finally { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__FINALLY,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (...) { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__FOR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>if (...) { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__IF,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass { { ... <font color="red">}</font> }</tt></dd>
     * </dl>
     */
    R_CURLY__INSTANCE_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>LABEL: { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__LABELED_STAT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>() { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__LAMBDA,

    /**
     * The curly right brace "<code>}</code>" at the end of a method declaration.
     * <dl>
     *   <dd><tt>public MyClass(...) { ... <font color="red">}</font></tt></dd>
     *   <dd><tt>public method(...) { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__METH_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass { static { ... <font color="red">}</font> }</tt></dd>
     * </dl>
     */
    R_CURLY__STATIC_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>switch (a) { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__SWITCH,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>synchronized (a) { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__SYNCHRONIZED,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... <font color="red">} catch {</font></tt></dd>
     * </dl>
     */
    R_CURLY__TRY,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass { ... <font color="red">}</font></tt></dd>
     *   <dd><tt>interface MyInter { ... <font color="red">}</font></tt></dd>
     *   <dd><tt>@interface MyAnno { ... <font color="red">}</font></tt></dd>
     *   <dd><tt>enum MyEnum { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__TYPE_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>while (...) { ... <font color="red">}</font></tt></dd>
     * </dl>
     */
    R_CURLY__WHILE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>@SuppressWarnings("foo"<font color="red">)</font></tt></dd>
     * </dl>
     */
    R_PAREN__ANNO,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>interface @MyAnno { String engineer(<font color="red">)</font>; }</tt></dd>
     * </dl>
     */
    R_PAREN__ANNO_ELEM_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>(int<font color="red">)</font> a</tt></dd>
     * </dl>
     */
    R_PAREN__CAST,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try { ... } catch (Exception e<font color="red">)</font> {</tt></dd>
     * </dl>
     */
    R_PAREN__CATCH,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>do { ... } while (...<font color="red">)</font>;</tt></dd>
     * </dl>
     */
    R_PAREN__DO_WHILE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (int i = 0; i &lt; 10; i++<font color="red">)</font> {</tt></dd>
     * </dl>
     */
    R_PAREN__FOR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (int i = 0; i &lt; 10;<font color="red">)</font> {</tt></dd>
     * </dl>
     */
    R_PAREN__FOR_NO_UPDATE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>if (...<font color="red">)</font> {</tt></dd>
     * </dl>
     */
    R_PAREN__IF,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a(x, y<font color="red">)</font></tt></dd>
     * </dl>
     */
    R_PAREN__METH_INVOCATION,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>void meth(int a, int b<font color="red">)</font> {</tt></dd>
     * </dl>
     */
    R_PAREN__PARAMS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>(a + b<font color="red">)</font> * c</tt></dd>
     * </dl>
     */
    R_PAREN__PARENTHESIZED,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try (Reader r  = ...<font color="red">)</font> {</tt></dd>
     * </dl>
     */
    R_PAREN__RESOURCES,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">return</font> x;</tt></dd>
     * </dl>
     */
    RETURN__EXPR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">return</font>;</tt></dd>
     * </dl>
     */
    RETURN__NO_EXPR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">>></font> 3</tt></dd>
     * </dl>
     */
    RIGHT_SHIFT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">>>=</font> 2</tt></dd>
     * </dl>
     */
    RIGHT_SHIFT_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>abstract meth(int x)<font color="red">;</font></tt></dd>
     * </dl>
     */
    SEMI__ABSTRACT_METH_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>interface @MyAnno { String engineer()<font color="red">;</font> }</tt></dd>
     * </dl>
     */
    SEMI__ANNO_ELEM_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">;</font></tt></dd>
     * </dl>
     */
    SEMI__EMPTY_STAT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>enum MyEnum { A, B, C<font color="red">;</font> public String toString() { ... } }</tt></dd>
     * </dl>
     */
    SEMI__ENUM_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>public int i<font color="red">;</font></tt></dd>
     * </dl>
     */
    SEMI__FIELD_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (...; i < 3<font color="red">;</font>) {</tt></dd>
     * </dl>
     */
    SEMI__FOR_CONDITION_NO_UPDATE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (...; i < 3<font color="red">;</font> i++) {</tt></dd>
     * </dl>
     */
    SEMI__FOR_CONDITION_UPDATE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (int i = 0<font color="red">;</font> i < 3;...</tt></dd>
     * </dl>
     */
    SEMI__FOR_INIT_CONDITION,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (int i = 0<font color="red">;</font>;...</tt></dd>
     * </dl>
     */
    SEMI__FOR_INIT_NO_CONDITION,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (...;<font color="red">;</font>) {</tt></dd>
     * </dl>
     */
    SEMI__FOR_NO_CONDITION_NO_UPDATE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (...;<font color="red">;</font> i++) {</tt></dd>
     * </dl>
     */
    SEMI__FOR_NO_CONDITION_UPDATE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (<font color="red">;</font> ...; ...) {</tt></dd>
     * </dl>
     */
    SEMI__FOR_NO_INIT_CONDITION,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>for (<font color="red">;</font>;...) {</tt></dd>
     * </dl>
     */
    SEMI__FOR_NO_INIT_NO_CONDITION,

    /**
     * TODO.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt>import pkg.*<font color="red">;</font></tt></dd>
     *   <dd><tt>import pkg.MyClass<font color="red">;</font></tt></dd>
     * </dl>
     */
    SEMI__IMPORT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>package pkg.pkg<font color="red">;</font></tt></dd>
     * </dl>
     */
    SEMI__PACKAGE_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>try (Reader r = ...<font color="red">;</font> OutputStream os = ...</tt>) { ...</dd>
     * </dl>
     */
    SEMI__RESOURCES,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a = 7<font color="red">;</font></tt></dd>
     * </dl>
     */
    SEMI__STATEMENT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>import static MyClass.*<font color="red">;</font></tt></dd>
     * </dl>
     */
    SEMI__STATIC_IMPORT,
    
    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>switch (x) { case 1 -> "It's a ONE"<font color="red">;</font></tt> }</dd>
     * </dl>
     */
    SEMI__SWITCH_RULE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass { <font color="red">;</font> }</tt></dd>
     * </dl>
     */
    SEMI__TYPE_DECL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">short</font></tt></dd>
     * </dl>
     */
    SHORT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>import pkg.pkg.<font color="red">*</font>;</tt></dd>
     * </dl>
     */
    STAR__TYPE_IMPORT_ON_DEMAND,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>import <font color="red">static</font> java.util.Map.Entry;</tt></dd>
     * </dl>
     */
    STATIC__STATIC_IMPORT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">static</font> int x;</tt></dd>
     *   <dd><tt><font color="red">static</font> class MyClass {</tt></dd>
     *   <dd><tt><font color="red">static</font> void meth() {</tt></dd>
     * </dl>
     */
    STATIC__MOD,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>class MyClass { <font color="red">static</font> { ... } }</tt></dd>
     * </dl>
     */
    STATIC__STATIC_INIT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">"hello"</font></tt></dd>
     * </dl>
     */
    STRING_LITERAL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">super</font>(x, y);</tt></dd>
     * </dl>
     */
    SUPER__CTOR_CALL,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">super</font>.meth();</tt></dd>
     *   <dd><tt>outer.<font color="red">super</font>.meth();</tt></dd>
     * </dl>
     */
    SUPER__EXPR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>List&lt;T <font color="red">super</font> MyClass></tt></dd>
     * </dl>
     */
    SUPER__TYPE_BOUND,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">switch</font> (a) {</tt></dd>
     * </dl>
     */
    SWITCH,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">synchronized</font> Object o;</tt></dd>
     * </dl>
     */
    SYNCHRONIZED__MOD,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">synchronized</font> (x) { ... }</tt></dd>
     * </dl>
     */
    SYNCHRONIZED__SYNCHRONIZED,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">this</font>(a, b);</tt></dd>
     * </dl>
     */
    THIS__CTOR_CALL,

    /**
     * TODO.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">this</font>.meth()</tt></dd>
     *   <dd><tt><font color="red">this</font>.field</tt></dd>
     * </dl>
     */
    THIS__EXPR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">throw</font> new IOException();</tt></dd>
     * </dl>
     */
    THROW,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">throws</font></tt></dd>
     * </dl>
     */
    THROWS,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">transient</font></tt></dd>
     * </dl>
     */
    TRANSIENT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">true</font></tt></dd>
     * </dl>
     */
    TRUE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">try</font> { ... } catch (...) { ... }</tt></dd>
     * </dl>
     */
    TRY,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">>>></font> 3</tt></dd>
     * </dl>
     */
    UNSIGNED_RIGHT_SHIFT,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">>>>=</font> 2</tt></dd>
     * </dl>
     */
    UNSIGNED_RIGHT_SHIFT_ASSIGN,

    /**
     * TODO.
     * <dl>
     *   <dt>Examples:</dt>
     *   <dd><tt><font color="red">void</font> meth() {</tt></dd>
     *   <dd><tt><font color="red">void</font>.class</tt></dd>
     * </dl>
     */
    VOID,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">volatile</font></tt></dd>
     * </dl>
     */
    VOLATILE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>do { ... } <font color="red">while</font> (a > 0);</tt></dd>
     * </dl>
     */
    WHILE__DO,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt><font color="red">while</font> (a > 0) { ... }</tt></dd>
     * </dl>
     */
    WHILE__WHILE,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">^</font> b</tt></dd>
     * </dl>
     */
    XOR,

    /**
     * TODO.
     * <dl>
     *   <dt>Example:</dt>
     *   <dd><tt>a <font color="red">^=</font> b</tt></dd>
     * </dl>
     */
    XOR_ASSIGN,
}
