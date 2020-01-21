
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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.util.AstUtil;
import de.unkrig.csdoclet.annotation.Message;
import de.unkrig.csdoclet.annotation.MultiCheckRuleProperty;
import de.unkrig.csdoclet.annotation.RegexRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.SingleSelectRuleProperty;

/**
 * Verifies that the names of Java elements match, respectively no not match given patterns.
 * <p>
 * This check makes name checking more powerful, compared with CheckStyle's standard "Naming Conventions" checks:
 * </p>
 * <ul>
 *   <li>Arbitrary sets of required/missing modifiers can be specified</li>
 *   <li>
 *     Name patterns can not only be enforced but also be forbidden (useful, e.g., to forbid certains styles of
 *     hungarian notation)
 *   </li>
 *   <li>
 *     Adds the possibility to check the names of annotations, annotation fields and {@code enum}s (which are missing
 *     from the standard checks)
 *   </li>
 * </ul>
 * <p>
 *   This check supersedes all of the CheckStyle standard "Naming Conventions" checks:
 * </p>
 * <ul>
 *   <li>Abstract Class Name</li>
 *   <li>Class Type Parameter Name</li>
 *   <li>Constant Names</li>
 *   <li>Enum Values Name</li>
 *   <li>Interface Type Parameter Name</li>
 *   <li>Local Final Variable Names</li>
 *   <li>Local Variable Names</li>
 *   <li>Member Names</li>
 *   <li>Method Names</li>
 *   <li>Method Type Parameter Name</li>
 *   <li>Package Names</li>
 *   <li>Parameter Names</li>
 *   <li>Static Variable Names</li>
 *   <li>Type Names</li>
 * </ul>
 */
@Rule(
    group     = "%Naming.group",
    groupName = "Naming Conventions",
    name      = "de.unkrig: Name spelling",
    parent    = "TreeWalker"
)
@NotNullByDefault(false) public
class NameSpelling extends AbstractCheck {

    @Message("{0} ''{1}'' does not comply with ''{2}''")
    private static final String MESSAGE_KEY_DOES_NOT_COMPLY = "NameSpelling.doesNotComply";

    @Message("{0} ''{1}'' must not match ''{2}''")
    private static final String MESSAGE_KEY_MUST_NOT_MATCH = "NameSpelling.mustNotMatch";

    /**
     * All elements that can be declared in the JAVA programming language.
     */
    public
    enum Elements {

        // SUPPRESS CHECKSTYLE JavadocVariable:15
        ANNOTATION("Annotation"),
        ANNOTATION_FIELD("Annotation field"),
        CATCH_PARAMETER("Catch parameter"),
        CLASS("Class"),
        ENUM("Enum"),
        ENUM_CONSTANT("Enum constant"),
        FOR_VARIABLE("For variable"),
        FOREACH_VARIABLE("Foreach variable"),
        FIELD("Field"),
        FORMAL_PARAMETER("Formal parameter"),
        INTERFACE("Interface"),
        LOCAL_VARIABLE("Local variable"),
        METHOD("Method"),
        PACKAGE("Package"),
        TYPE_PARAMETER("Type parameter");

        private final String name;

        Elements(String name) { this.name = name; }

        @Override public String
        toString() { return this.name; }
    }

    // BEGIN CONFIGURATION SETTERS

    /**
     * Elements to apply this check to.
     */
    @MultiCheckRuleProperty(optionProvider = Elements.class)
    public final void
    setElements(String[] elements) {
        for (final String element : elements) {
            this.elements.add(Enum.valueOf(Elements.class, element.toUpperCase()));
        }
    }
    private final EnumSet<Elements> elements = EnumSet.noneOf(Elements.class);

    /**
     * The 'option-provider' for the 'requiredModifiers' and 'missingModifiers' properties.
     */
    public
    enum Modifier {

        // SUPPRESS CHECKSTYLE JavadocVariable:11
        PUBLIC(LocalTokenType.LITERAL_PUBLIC),
        PROTECTED(LocalTokenType.LITERAL_PROTECTED),
        PRIVATE(LocalTokenType.LITERAL_PRIVATE),
        ABSTRACT(LocalTokenType.ABSTRACT),
        STATIC(LocalTokenType.LITERAL_STATIC),
        FINAL(LocalTokenType.FINAL),
        SYNCHRONIZED(LocalTokenType.LITERAL_SYNCHRONIZED),
        NATIVE(LocalTokenType.LITERAL_NATIVE),
        TRANSIENT(LocalTokenType.LITERAL_TRANSIENT),
        VOLATILE(LocalTokenType.LITERAL_VOLATILE),
        STRICTFP(LocalTokenType.STRICTFP),
        ;

        private final LocalTokenType ltt;

        Modifier(LocalTokenType ltt) { this.ltt = ltt; }

        /** @return The {@link LocalTokenType} corresponding with this modifier */
        public LocalTokenType toLocalTokenType() { return this.ltt; }
    }

    /**
     * Apply only to declarations which have these modifiers.
     */
    @MultiCheckRuleProperty(optionProvider = Modifier.class, defaultValue = "(all declarations)")
    public final void
    setRequiredModifiers(String[] modifiers) {
        for (final String modifier : modifiers) {
            this.requiredModifiers.add(Modifier.valueOf(modifier.toUpperCase()).toLocalTokenType());
        }
    }
    private final Set<LocalTokenType> requiredModifiers = new HashSet<LocalTokenType>();

    /**
     * Apply only to declarations which do not have these modifiers.
     */
    @MultiCheckRuleProperty(optionProvider = Modifier.class, defaultValue = "(all declarations)")
    public final void
    setMissingModifiers(String[] modifiers) {
        for (final String modifier : modifiers) {
            this.missingModifiers.add(Modifier.valueOf(modifier.toUpperCase()).toLocalTokenType());
        }
    }
    private final Set<LocalTokenType> missingModifiers = new HashSet<LocalTokenType>();

    /**
     * Whether a name MUST match, or MUST NOT match.
     */
    public enum Options { REQUIRE, FORBID } // SUPPRESS CHECKSTYLE JavadocVariable

    /**
     * Whether to require or forbid that names match.
     */
    @SingleSelectRuleProperty(optionProvider = Options.class, defaultValue = NameSpelling.DEFAULT_OPTION)
    public final void
    setOption(String option) {
        try {
            this.option = Enum.valueOf(Options.class, option.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(option, iae);
        }
    }
    private Options              option         = Options.valueOf(NameSpelling.DEFAULT_OPTION.toUpperCase());
    private static final String  DEFAULT_OPTION = "require";

    /**
     * The pattern to match the name against.
     */
    @RegexRuleProperty public void
    setFormat(String format) {

        try {
            this.formatPattern = Pattern.compile(format);
        } catch (PatternSyntaxException ex) {
            throw new IllegalArgumentException("unable to parse " + format, ex);
        }
    }
    private Pattern formatPattern = Pattern.compile("");

    // END CONFIGURATION SETTERS

    @Override public int[]
    getAcceptableTokens() {

        // Calculate the minimal set of tokens required to perform the check.
        List<LocalTokenType> tokens = new ArrayList<LocalTokenType>();

        if (this.elements.contains(Elements.ANNOTATION))       tokens.add(LocalTokenType.ANNOTATION_DEF);
        if (this.elements.contains(Elements.ANNOTATION_FIELD)) tokens.add(LocalTokenType.ANNOTATION_FIELD_DEF);
        if (this.elements.contains(Elements.CLASS))            tokens.add(LocalTokenType.CLASS_DEF);
        if (this.elements.contains(Elements.ENUM))             tokens.add(LocalTokenType.ENUM_DEF);
        if (this.elements.contains(Elements.ENUM_CONSTANT))    tokens.add(LocalTokenType.ENUM_CONSTANT_DEF);
        if (this.elements.contains(Elements.INTERFACE))        tokens.add(LocalTokenType.INTERFACE_DEF);
        if (this.elements.contains(Elements.METHOD))           tokens.add(LocalTokenType.METHOD_DEF);
        if (this.elements.contains(Elements.PACKAGE))          tokens.add(LocalTokenType.PACKAGE_DEF);
        if (this.elements.contains(Elements.TYPE_PARAMETER))   tokens.add(LocalTokenType.TYPE_PARAMETER);

        if (
            this.elements.contains(Elements.CATCH_PARAMETER)
            || this.elements.contains(Elements.FORMAL_PARAMETER)
        ) tokens.add(LocalTokenType.PARAMETER_DEF);

        if (
            this.elements.contains(Elements.LOCAL_VARIABLE)
            || this.elements.contains(Elements.FOR_VARIABLE)
            || this.elements.contains(Elements.FOREACH_VARIABLE)
            || this.elements.contains(Elements.FIELD)
        ) tokens.add(LocalTokenType.VARIABLE_DEF);

        LocalTokenType[] tokensArray = tokens.toArray(new LocalTokenType[tokens.size()]);
        assert tokensArray != null;
        return LocalTokenType.delocalize(tokensArray);
    }

    @Override public int[]
    getDefaultTokens() { return this.getAcceptableTokens(); }

    @Override public int[]
    getRequiredTokens() { return this.getAcceptableTokens(); }

    @Override public void
    visitToken(DetailAST ast) {
        try {

            // Determine the element type from the given AST.
            Elements element;
            switch (LocalTokenType.localize(ast.getType())) {

            case ANNOTATION_DEF:
                element = Elements.ANNOTATION;
                break;

            case ANNOTATION_FIELD_DEF:
                element = Elements.ANNOTATION_FIELD;
                break;

            case CLASS_DEF:
                element = Elements.CLASS;
                break;

            case ENUM_DEF:
                element = Elements.ENUM;
                break;

            case ENUM_CONSTANT_DEF:
                element = Elements.ENUM_CONSTANT;
                break;

            case INTERFACE_DEF:
                element = Elements.INTERFACE;
                break;

            case METHOD_DEF:
                element = Elements.METHOD;
                break;

            case PACKAGE_DEF:
                element = Elements.PACKAGE;
                break;

            case TYPE_PARAMETER:
                element = Elements.TYPE_PARAMETER;
                break;

            case PARAMETER_DEF:
                element = (
                    AstUtil.parentTypeIs(ast, LocalTokenType.PARAMETERS)      ? Elements.FORMAL_PARAMETER
                    : AstUtil.parentTypeIs(ast, LocalTokenType.LITERAL_CATCH) ? Elements.CATCH_PARAMETER
                    : null
                );
                break;

            case VARIABLE_DEF:
                element = (
                    AstUtil.parentTypeIs(ast, LocalTokenType.SLIST)             ? Elements.LOCAL_VARIABLE
                    : AstUtil.parentTypeIs(ast, LocalTokenType.FOR_INIT)        ? Elements.FOR_VARIABLE
                    : AstUtil.parentTypeIs(ast, LocalTokenType.FOR_EACH_CLAUSE) ? Elements.FOREACH_VARIABLE
                    : AstUtil.parentTypeIs(ast, LocalTokenType.OBJBLOCK)        ? Elements.FIELD
                    : null
                );
                break;

            default:
                throw new IllegalStateException(Integer.toString(ast.getType()));
            }
            if (element == null) throw new IllegalStateException(Integer.toString(ast.getType()));

            // Verify that this element should be checked.
            if (!this.elements.contains(element)) return;

            // Now determine the modifiers and the name.
            DetailAST modifiersAst, nameAst;
            switch (element) {

            case ANNOTATION:
            case ANNOTATION_FIELD:
            case CATCH_PARAMETER:
            case CLASS:
            case ENUM:
            case ENUM_CONSTANT:
            case FOR_VARIABLE:
            case FOREACH_VARIABLE:
            case FIELD:
            case FORMAL_PARAMETER:
            case INTERFACE:
            case LOCAL_VARIABLE:
            case METHOD:
            case TYPE_PARAMETER:
                modifiersAst = ast.findFirstToken(LocalTokenType.MODIFIERS.delocalize());
                nameAst      = ast.findFirstToken(LocalTokenType.IDENT.delocalize());
                break;

            case PACKAGE:
                modifiersAst = null;
                nameAst      = ast.getLastChild().getPreviousSibling();
                break;

            default:
                throw new IllegalStateException(element.toString());
            }

            // Check if the modifiers match the configuration.
            if (modifiersAst == null) {
                assert this.requiredModifiers.isEmpty() : "Must not set 'requiredModifiers' for element 'package'";
                assert this.missingModifiers.isEmpty() : "Must not set 'missingModifiers' for element 'package'";
            } else {
                for (LocalTokenType modifier : this.requiredModifiers) {
                    if (modifiersAst.findFirstToken(modifier.delocalize()) == null) return;
                }
                for (LocalTokenType modifier : this.missingModifiers) {
                    if (modifiersAst.findFirstToken(modifier.delocalize()) != null) return;
                }
            }

            // Eventually check the element name.
            FullIdent fullName = FullIdent.createFullIdent(nameAst);
            switch (this.option) {

            case REQUIRE:
                if (!this.formatPattern.matcher(fullName.getText()).find()) {
                    this.log(
                        fullName.getLineNo(),
                        fullName.getColumnNo(),
                        NameSpelling.MESSAGE_KEY_DOES_NOT_COMPLY,
                        element.toString(),
                        fullName.getText(),
                        this.formatPattern.toString()
                    );
                }
                break;

            case FORBID:
                if (this.formatPattern.matcher(fullName.getText()).find()) {
                    this.log(
                        fullName.getLineNo(),
                        fullName.getColumnNo(),
                        NameSpelling.MESSAGE_KEY_MUST_NOT_MATCH,
                        element.toString(),
                        fullName.getText(),
                        this.formatPattern.toString()
                    );
                }
                break;
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException(
                this.getFileContents().getFileName() + ":" + ast.getLineNo() + "x" + ast.getColumnNo(),
                rte
            );
        }
    }
}
