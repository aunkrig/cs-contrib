
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

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.cscontrib.LocalTokenType;
import de.unkrig.cscontrib.compat.Cs820;

/**
 * A helper with a zero-cost constructor for dumping an AST.
 */
public
class AstDumper {

    @Nullable private final DetailAST ast;

    AstDumper(@Nullable DetailAST ast) {
        this.ast = ast;
    }

    @Override @Nullable public String
    toString() {
        StringBuilder sb = new StringBuilder();
        AstDumper.dumpSiblings("", this.ast, sb);
        return sb.toString();
    }

    private static void
    dumpSiblings(String prefix, @Nullable DetailAST sibling, StringBuilder sb) {
        for (; sibling != null; sibling = Cs820.getNextSibling(sibling)) {
            String tokenName = LocalTokenType.localize(Cs820.getType(sibling)).toString();
            sb.append(prefix).append(sibling).append(tokenName).append('\n');
            AstDumper.dumpSiblings(prefix + "  ", Cs820.getFirstChild(sibling), sb);
        }
    }
}
