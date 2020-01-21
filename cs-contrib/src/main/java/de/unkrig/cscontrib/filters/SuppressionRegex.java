
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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Filter;

import de.unkrig.commons.nullanalysis.NotNullByDefault;
import de.unkrig.csdoclet.annotation.RegexRuleProperty;
import de.unkrig.csdoclet.annotation.Rule;

/**
 * Specific events (i.e&#46; CheckStyle warnings) are suppressed in lines that match a given regex.
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

    /** The pattern agains which the check name is matched. */
    private Pattern checkNameRegex;

    /** The message format to suppress. */
    private Pattern messageRegex;

    /** The module id format to suppress. */
    private Pattern moduleIdRegex;

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
     * Check name pattern to suppress.
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

    private static FileText getFileText(String fileName) {
       File file = new File(fileName);
       FileText result = null;


       if (!file.isDirectory()) {
          try {
             result = new FileText(file, "UTF-8");

          } catch (IOException var4) {
             throw new IllegalStateException("Cannot read source file: " + fileName, var4);
          }
       }

       return result;
    }

    @Override public boolean
    accept(AuditEvent event) {

//	@Override public boolean
//    accept(TreeWalkerAuditEvent event) {

        if (event.getLocalizedMessage() == null) return true;        // A special event.

        // Lazy update. If the first event for the current file, update file
        // contents and tag suppressions
//      final FileContents currentContents = event.getFileContents();
        FileText currentContents = SuppressionRegex.getFileText(event.getFileName());

        if (currentContents == null) {
            // we have no contents, so we can not filter.
            // TODO: perhaps we should notify user somehow?
            return true;
        }
        if (this.getFileContents() != currentContents) {
            this.setFileContents(currentContents);
        }

        String line = currentContents.get(event.getLine() - 1);
        if (this.lineRegex.matcher(line).find()) {

            if (
                SuppressionRegex.this.checkNameRegex != null
                && this.checkNameRegex.matcher(event.getSourceName()).find()
            ) return false;

            if (
                SuppressionRegex.this.messageRegex != null
                && this.moduleIdRegex.matcher(event.getMessage()).find()
            ) return false;

            if (
                SuppressionRegex.this.moduleIdRegex != null
                && this.moduleIdRegex.matcher(event.getModuleId()).find()
            ) return false;
        }

        return true;
    }

    @Override protected void
    finishLocalSetup() {}
}
