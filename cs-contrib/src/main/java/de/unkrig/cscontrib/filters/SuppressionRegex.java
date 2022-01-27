
/*
 * de.unkrig.cs-contrib - Additional checks, filters and quickfixes for CheckStyle and Eclipse-CS
 *
 * Copyright (c) 2016, Arno Unkrig
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

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Filter;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.csdoclet.annotation.RegexRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;
import de.unkrig.csdoclet.annotation.StringRuleProperty;

/**
 * Specific events (i.e&#46; CheckStyle warnings) are suppressed in lines that match a given regex, and optionally in
 * a given number of lines following.
 */
@Rule(
    group       = "%Filters.group",
    groupName   = "Filters",
    name        = "de.unkrig: Suppression regex",
    parent      = "Checker",
    hasSeverity = false
)
@NotNullByDefault(false) public
class SuppressionRegex extends AutomaticBean implements Filter {

    private Pattern lineRegex;

    /** The check name to suppress. */
    private Pattern checkNameRegex;

    /** The message format to suppress. */
    private Pattern messageRegex;

    /** The module id format to suppress. */
    private Pattern moduleIdRegex;

    /** The number of lines below for which the filter is also effective. */
    private String influence = "0";

    /**
     * References the current FileContents for this filter. Since this is a weak reference to the FileContents, the
     * FileContents can be reclaimed as soon as the strong references in TreeWalker and FileContentsHolder are
     * reassigned to the next FileContents, at which time filtering for the current FileContents is finished.
     */
    private WeakReference<FileText> fileContentsReference = new WeakReference<FileText>(null);

    public
    SuppressionRegex() {}

    // BEGIN CONFIGURATION SETTERS

    /**
     * Line pattern to trigger suppression.
     */
    @RegexRuleProperty
    public void
    setLineRegex(String lineRegex) {
        try {
            this.lineRegex = Pattern.compile(lineRegex);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + lineRegex, e);
        }
    }

    /**
     * Check name pattern to suppress (e.g. "LineLength|Alignment").
     */
    @RegexRuleProperty
    public void
    setCheckNameFormat(String checkNameFormat) {

        try {
            this.checkNameRegex = Pattern.compile(checkNameFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + checkNameFormat, e);
        }
    }

    /**
     * Message pattern to suppress.
     */
    @RegexRuleProperty
    public void
    setMessageFormat(String messageFormat) {

        try {
            this.messageRegex = Pattern.compile(messageFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + messageFormat, e);
        }
    }

    /**
     * Module ID pattern to suppress.
     */
    @RegexRuleProperty
    public void
    setModuleIdFormat(String moduleIdFormat) {
        try {
            this.moduleIdRegex = Pattern.compile(moduleIdFormat);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException("unable to parse " + moduleIdFormat, e);
        }
    }

    /**
     * Number of lines after the suppression line for which it is effective.
     */
    @StringRuleProperty
    public void
    setInfluence(String influence) {
        this.influence = influence;
    }

    // END CONFIGURATION SETTERS

    /** @return the FileContents for this filter. */
    public FileText
    getFileContents() { return this.fileContentsReference.get(); }

    /**
     * Set the FileContents for this filter.
     *
     * @param fileContents the FileContents for this filter.
     */
    public void
    setFileContents(FileText fileContents) {
        this.fileContentsReference = new WeakReference<FileText>(fileContents);
    }

    private static FileText
    getFileText(String fileName) {

       File file = new File(fileName);
       if (file.isDirectory()) return null;

      try {
         return new FileText(file, "UTF-8");
      } catch (IOException var4) {
         throw new IllegalStateException("Cannot read source file: " + fileName, var4);
      }
    }

    @Override public boolean
    accept(AuditEvent event) {

        if (event.getLocalizedMessage() == null) return true;        // A special event.

        // Lazy update. If the first event for the current file, update file
        // contents and tag suppressions
        FileText currentContents = SuppressionRegex.getFileText(event.getFileName());

        if (currentContents == null) {
            // we have no contents, so we can not filter.
            // TODO: perhaps we should notify user somehow?
            return true;
        }
        if (this.getFileContents() != currentContents) {
            this.setFileContents(currentContents);
        }

        for (int lineNumber = event.getLine(); lineNumber >= 1 && lineNumber >= event.getLine() - 100; lineNumber--) {
            String line = currentContents.get(lineNumber - 1);
            Matcher m = this.lineRegex.matcher(line);

            if (m.find()) {

                // "this.influence" can be an integer string (e.g. "11"), or comprise replacement variables (e.g.
                // "$2").
                int inf;
                try {
                    inf = Integer.parseInt(expandSubsequenceReferences(this.influence, m));
                } catch (NumberFormatException nfe) {
                    inf = 0;
                }

                // Check that the event line is in the "influence range" of the suppression line.
                if (event.getLine() > lineNumber + inf) continue;

                if (this.checkNameRegex != null && matcher(this.checkNameRegex, m, event.getSourceName()).find()) {
                    return false;
                }

                if (this.messageRegex != null && matcher(this.messageRegex, m, event.getMessage()).find()) {
                    return false;
                }

                if (this.moduleIdRegex != null && matcher(this.moduleIdRegex, m, event.getModuleId()).find()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Same as {pattern.matcher(subject)}, except that iff the <var>pattern</var> contains  captured subsequence
     * references, then these are first expanded from the <var>capturedSubsequences</var> and then the
     * <var>pattern</var> is re-compiled before it is applied.
     */
    private Matcher
    matcher(Pattern pattern, Matcher capturedSubsequences, String subject) {
        String regex = pattern.pattern();
        String regex2 = expandSubsequenceReferences(regex, capturedSubsequences);
        if (!regex2.equals(regex)) {
            pattern = Pattern.compile(regex2);
        }
        Matcher m2 = pattern.matcher(subject);
        return m2;
    }

    /**
     * Returns the <var>subject</var>, with all "captured subsequence references" ({@code $1}, {@ode $2}, ...)
     * replaced with captured subsequences of the <var>matcher</var>.
     * Similar to {@link Matcher#appendReplacement(StringBuffer, String)}, but does not replace escaped characters
     * (e.g. "\\") with the unescaped, except "\$".
     * Precisely; a dollar-digit sequence is only replaced if it is preceeded with an even number of backslashes
     * (including zero backslashes).
     */
    private static String
    expandSubsequenceReferences(String subject, Matcher capturedSubsequences) {

        int state = 0;
        for (int i = 0; i < subject.length(); i++) {
            char c = subject.charAt(i);
            switch (state) {
            case 0:                         // '\\'*
                if (c == '\\') {            // '\\'* '\'
                    state = 1;
                } else
                if (c == '$') {             // '\\'* '$'
                    state = 2;
                }
                break;
            case 1:                         // '\\'* '\' any-char
                state = 0;
                break;
            case 2:
                if (c == '\\') {            // '\\'* '$\'
                    state = 1;
                } else
                if (Character.isDigit(c)) { // '\\'* '$' digit
                    int groupNumber = Character.digit(c, 10);
                    if (groupNumber <= capturedSubsequences.groupCount()) {
                        String capturedSubsequence = capturedSubsequences.group(groupNumber);
                        subject = subject.substring(0, i - 1) + capturedSubsequence + subject.substring(i + 1);
                        i += capturedSubsequence.length() - 2;
                    }
                    state = 0;
                } else
                {                           // '\\'* '$' non-digit
                    state = 0;
                }
            }
        }
        return subject;
    }

    @Override protected void
    finishLocalSetup() {}
}
