
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

package de.unkrig.cscontrib.filters;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.TreeWalkerAuditEvent;
import com.puppycrawl.tools.checkstyle.TreeWalkerFilter;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.FileContents;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.csdoclet.annotation.RegexRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;

/**
 * Events (i.e&#46; CheckStyle warnings) are switched off by a "magic line" or back on by another magic line.
 * <p>
 *   After the "off" magic line, events do not show if at least one of the following conditions is true:
 * </p>
 * <ul>
 *   <li>The "checkNameFormat" (if set) is found in the check name (e.g. "de.unkrig.cscontrib.checks.Alignment")
 *   <li>The "messageFormat" (if set) is found in the event message
 *   <li>The "moduleIdFormat" (if set) is found in the ID of the module that generated the event
 * </ul>
 */
@Rule(
    group       = "%Filters.group",
    groupName   = "Filters",
    name        = "de.unkrig: Suppression line",
    parent      = "Checker",
    hasSeverity = false
)
@NotNullByDefault(false) public
class SuppressionLine extends AutomaticBean implements TreeWalkerFilter {

    /**
     * A Tag holds a magic line and its location, and determines whether the suppression turns CHECKSTYLE reporting on
     * or off.
     */
    public
    class Tag implements Comparable<Tag> {

        /** The text of the tag. */
        private final String text;

        /** The line number of the tag. */
        private final int lineNo;

        /** Determines whether the suppression turns checkstyle reporting on. */
        private final boolean on;

        /** The parsed check regex, expanded for the text of this tag. */
        private Pattern checkNameRegex;

        /** The parsed message regex, expanded for the text of this tag. */
        private Pattern messageRegex;

        /** The parsed module id regex, expanded for the text of this tag. */
        private Pattern moduleIdRegex;

        /**
         * Constructs a tag.
         *
         * @param lineNo The line number
         * @param text   The text of the suppression
         * @param on     <code>true</code> if the tag turns checkstyle reporting
         */
        public
        Tag(int lineNo, String text, boolean on) {
            this.lineNo = lineNo;
            this.text   = text;
            this.on     = on;

            // Expand regex for check and message
            // Does not intern Patterns with Utils.getPattern()
            try {
                Pattern regex = on ? SuppressionLine.this.onRegex : SuppressionLine.this.offRegex;
                if (SuppressionLine.this.checkNameFormat != null) {
                    this.checkNameRegex = Pattern.compile(
                        this.expandFromLine(text, SuppressionLine.this.checkNameFormat, regex)
                    );
                }
                if (SuppressionLine.this.messageFormat != null) {
                    this.messageRegex = Pattern.compile(
                        this.expandFromLine(text, SuppressionLine.this.messageFormat, regex)
                    );
                }
                if (SuppressionLine.this.moduleIdFormat != null) {
                    this.moduleIdRegex = Pattern.compile(
                        this.expandFromLine(text, SuppressionLine.this.moduleIdFormat, regex)
                    );
                }
            } catch (final PatternSyntaxException e) {
                throw new IllegalArgumentException("unable to parse expanded line " + e.getPattern(), e);
            }
        }

        /** @return the text of the tag. */
        public String
        getText() { return this.text; }

        /** @return the line number of the tag in the source file. */
        public int
        getLine() { return this.lineNo; }

        /**
         * Determines whether the suppression turns checkstyle reporting on or
         * off.
         * @return <code>true</code>if the suppression turns reporting on.
         */
        public boolean
        isOn() { return this.on; }

        /**
         * Compares the position of this tag in the file
         * with the position of another tag.
         * @param that the tag to compare with this one.
         * @return a negative number if this tag is before the other tag,
         * 0 if they are at the same position, and a positive number if this
         * tag is after the other tag.
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override public int
        compareTo(Tag that) { return this.lineNo - that.lineNo; }

        /**
         * Determines whether the audit event matches this tag.
         *
         * @param event The {@link AuditEvent} to check
         * @return      Whether the source of the {@code event} matches this tag
         */
        private boolean
        isMatch(TreeWalkerAuditEvent event) {

            // Match event's 'source name' against 'checkNameRegex'.
            if (
                this.checkNameRegex != null
                && this.checkNameRegex.matcher(event.getSourceName()).find()
            ) return true;

            // Match event's message against 'checkMessageRegex'.
            if (
                this.messageRegex != null
                && this.messageRegex.matcher(SuppressionLine.getEventMessage(event)).find()
            ) return true;

            // Match event's 'module ID' against 'moduleIdRegex'.
            if (
                this.moduleIdRegex != null
                && event.getModuleId() != null
                && this.moduleIdRegex.matcher(event.getModuleId()).find()
            ) return true;

            return false;
        }

        /**
         * Expand based on a matching line.
         *
         * @param replacement the string to expand.
         * @param regex the parsed expander.
         *
         * @return the expanded {@code replacement}
         */
        private String
        expandFromLine(String line, String replacement, Pattern regex) {
            final Matcher matcher = regex.matcher(line);

            if (!matcher.find()) return replacement;

            String result = replacement;
            for (int i = 0; i <= matcher.groupCount(); i++) {
                // $n expands line match like in Pattern.subst().
                result = result.replaceAll("\\$" + i, matcher.group(i));
            }
            return result;
        }

        @Override public final String
        toString() { return "Tag[line=" + this.getLine() + "; on=" + this.isOn() + "; text='" + this.getText() + "']"; }
    }

    /**
     * {@link AuditEvent#getMessage()} eventually invokes {@link MessageFormat#format(String, Object...)}, and does
     * not catch {@link IllegalArgumentException}. This method wraps {@link IllegalArgumentException} so that the
     * ill-formed format appears in the exception message.
     */
    private static String
    getEventMessage(TreeWalkerAuditEvent event) {
        try {
            return event.getMessage();
        } catch (RuntimeException e) {
            throw new RuntimeException((
                event.getFileName()
                + ' '
                + event.getLine()
                + ':'
                + event.getColumn()
                + ": Localizing '"
                + event.getLocalizedMessage().getKey()
                + "'"
            ), e);
        }
    }

    /** Control all checks */

    /** Parsed line regex that turns checkstyle reporting off. */
    private Pattern offRegex;

    /** Parsed line regex that turns checkstyle reporting on. */
    private Pattern onRegex;

    /** The pattern agains which the check name is matched. */
    private String checkNameFormat;

    /** The message format to suppress. */
    private String messageFormat;

    private String moduleIdFormat;

    private final List<Tag> magicLines = Lists.newArrayList();

    /**
     * References the current FileContents for this filter.
     * Since this is a weak reference to the FileContents, the FileContents
     * can be reclaimed as soon as the strong references in TreeWalker
     * and FileContentsHolder are reassigned to the next FileContents,
     * at which time filtering for the current FileContents is finished.
     */
    private WeakReference<FileContents> fileContentsReference = new WeakReference<FileContents>(null);

    public
    SuppressionLine() {}

    // BEGIN CONFIGURATION SETTERS

    /**
     * Line pattern to trigger filter to begin suppression.
     */
    @RegexRuleProperty(overrideDefaultValue = "CHECKSTYLE (.+):OFF")
    public void
    setOffFormat(String offFormat) {
        try {
            this.offRegex = Pattern.compile(offFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + offFormat, e);
        }
    }

    /**
     * Line pattern to trigger filter to end suppression.
     */
    @RegexRuleProperty(overrideDefaultValue = "CHECKSTYLE (.+):ON")
    public void
    setOnFormat(String onFormat) {
        try {
            this.onRegex = Pattern.compile(onFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + onFormat, e);
        }
    }

    /**
     * Check name pattern to suppress.
     */
    @RegexRuleProperty(overrideDefaultValue = "$1")
    public void
    setCheckNameFormat(String checkNameFormat) {

        try {
            Pattern.compile(checkNameFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + checkNameFormat, e);
        }

        this.checkNameFormat = checkNameFormat;
    }

    /**
     * Message pattern to suppress.
     */
    @RegexRuleProperty(overrideDefaultValue = "$1")
    public void
    setMessageFormat(String messageFormat) {

        try {
            Pattern.compile(messageFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + messageFormat, e);
        }

        this.messageFormat = messageFormat;
    }

    /**
     * Module ID pattern to suppress.
     */
    @RegexRuleProperty(overrideDefaultValue = "$1")
    public void
    setModuleIdFormat(String moduleIdFormat) {
        try {
            Pattern.compile(moduleIdFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + moduleIdFormat, e);
        }
        this.moduleIdFormat = moduleIdFormat;
    }

    // END CONFIGURATION SETTERS

    /** @return the FileContents for this filter. */
    public FileContents
    getFileContents() { return this.fileContentsReference.get(); }

    /**
     * Set the FileContents for this filter.
     *
     * @param fileContents the FileContents for this filter.
     */
    public void
    setFileContents(FileContents fileContents) {
        this.fileContentsReference = new WeakReference<FileContents>(fileContents);
    }

    @Override public boolean
    accept(TreeWalkerAuditEvent event) {

        if (event.getLocalizedMessage() == null) return true;        // A special event.

        // Lazy update. If the first event for the current file, update file
        // contents and tag suppressions
        final FileContents currentContents = event.getFileContents();
        if (currentContents == null) {
            // we have no contents, so we can not filter.
            // TODO: perhaps we should notify user somehow?
            return true;
        }
        if (this.getFileContents() != currentContents) {
            this.setFileContents(currentContents);
            this.processMagicLines();
        }
        final Tag matchTag = this.findNearestMatch(event);
        if ((matchTag != null) && !matchTag.isOn()) {
            return false;
        }
        return true;
    }

    /**
     * Finds the nearest tag that matches an audit event. The nearest tag is before the line and column of the event.
     *
     * @param event The {@code AuditEvent} to match.
     * @return      The {@code Tag} nearest {@code event}.
     */
    private Tag
    findNearestMatch(TreeWalkerAuditEvent event) {
        Tag result = null;
        // TODO: try binary search if sequential search becomes a performance
        // problem.
        for (Tag tag : this.magicLines) {
            if (tag.getLine() + 1 > event.getLine()) break;
            if (tag.isMatch(event)) result = tag;
        }
        return result;
    }

    /**
     * Collects all the suppression tags for all magic lines into a list and sorts the list.
     */
    private void
    processMagicLines() {
        this.magicLines.clear();
        String[] lines = this.getFileContents().getLines();
        for (int lineNo = 0; lineNo < lines.length; ++lineNo) {
            this.checkMagicness(lines[lineNo], lineNo);
        }
    }

    /**
     * Tags a string if it matches the format for turning
     * checkstyle reporting on or the format for turning reporting off.
     *
     * @param text the string to tag.
     * @param line the line number of {@code text}.
     */
    private void
    checkMagicness(String text, int line) {
        if (this.offRegex != null) {
            final Matcher offMatcher = this.offRegex.matcher(text);
            if (offMatcher.find()) {
                this.addTag(offMatcher.group(0), line, false);
            }
        }
        if (this.onRegex != null) {
            final Matcher onMatcher = this.onRegex.matcher(text);
            if (onMatcher.find()) {
                this.addTag(onMatcher.group(0), line, true);
            }
        }
    }

    /**
     * Adds a <code>Tag</code> to the list of all tags.
     *
     * @param text the text of the tag.
     * @param line the line number of the tag.
     * @param on   {@code true} if the tag turns checkstyle reporting on.
     */
    private void
    addTag(String text, int line, boolean on) {
        final Tag tag = new Tag(line, text, on);
        this.magicLines.add(tag);
    }

    @Override protected void
    finishLocalSetup() {}
}
