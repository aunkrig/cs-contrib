
/*
 * cs-contrib - Additional checks, filters and quickfixes for CheckStyle and Eclipse-CS
 *
 * Copyright (c) 2020, Arno Unkrig
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

package de.unkrig.cscontrib.compat;

import java.lang.reflect.Method;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * Declares proxy methods for the "DetailAST" interface.
 * <p>
 *   <b>Background:</b>
 * </p>
 * <p>
 *   In version 8.21, the CheckStyle team made a terrible mistake and turned the class {@link DetailAST}
 *   into an <i>interface</i>.
 *   This is fatal because it breaks the binary compatibility of any CheckStyle extension (including cs-contrib) that
 *   uses that interface.
 *   The reason being is that <b>interface</b> methods must be referenced with an InterfaceMethodRef, while
 *   <b>class</b> method must be referenced with a MethodRef.
 * </p>
 * <p>
 *   The most painless workaround is to execute these invocations though reflection instead, which is what this class
 *   does.
 * </p>
 * <p>
 *   In order to check whether any of the CheckStyle extension's classes still invokes {@link DetailAST}'s methods
 *   directly, execute this shell command:
 * </p>
 * <pre>
 * $ zzfind target/classes -name '**.class' -echo '*** ${path}' -disassemble |
 * > egrep '^\*\*\*|invokeinterface com.puppycrawl.tools.checkstyle.api.DetailAST' |
 * > less
 * $
 * </pre>
 */
public
class Cs820 {

	private static final Method addPreviousSibling_DetailAST_ = meth("addPreviousSibling", DetailAST.class);
	private static final Method addNextSibling_DetailAST_     = meth("addNextSibling",     DetailAST.class);
	private static final Method getChildCount_                = meth("getChildCount");
	private static final Method getChildCount_int_            = meth("getChildCount",      int.class);
	private static final Method getParent_                    = meth("getParent");
	private static final Method getText_                      = meth("getText");
	private static final Method setText_String_               = meth("setText",            String.class);
	private static final Method getType_                      = meth("getType");
	private static final Method setType_int_                  = meth("setType",            int.class);
	private static final Method getLineNo_                    = meth("getLineNo");
	private static final Method setLineNo_int_                = meth("setLineNo",          int.class);
	private static final Method getColumnNo_                  = meth("getColumnNo");
	private static final Method setColumnNo_int_              = meth("setColumnNo",        int.class);
	private static final Method getLine_                      = meth("getLine");
	private static final Method getColumn_                    = meth("getColumn");
	private static final Method getLastChild_                 = meth("getLastChild");
	private static final Method branchContains_int_           = meth("branchContains",     int.class);
	private static final Method getPreviousSibling_           = meth("getPreviousSibling");
	private static final Method findFirstToken_int_           = meth("findFirstToken",     int.class);
	private static final Method getNextSibling_               = meth("getNextSibling");
	private static final Method getFirstChild_                = meth("getFirstChild");
	private static final Method getNumberOfChildren_          = meth("getNumberOfChildren");
	private static final Method removeChildren_               = meth("removeChildren");
    
	public static void      addPreviousSibling(DetailAST that, DetailAST ast) { ;                  inv(addPreviousSibling_DetailAST_, that, ast); }
	public static void      addNextSibling(DetailAST that, DetailAST ast)     { ;                  inv(addNextSibling_DetailAST_, that, ast);     }
    public static int       getChildCount(DetailAST that)                     { return (Integer)   inv(getChildCount_, that);                     }
    public static int       getChildCount(DetailAST that, int type)           { return (Integer)   inv(getChildCount_int_, that, type);           }
    public static DetailAST getParent(DetailAST that)                         { return (DetailAST) inv(getParent_, that);                         }
    public static String    getText(DetailAST that)                           { return (String)    inv(getText_, that);                           }
    public static void      setText(DetailAST that, String text)              { ;                  inv(setText_String_, that, text);              }
    public static int       getType(DetailAST that)                           { return (Integer)   inv(getType_, that);                           }
    public static void      setType(DetailAST that, int type)                 { ;                  inv(setType_int_, that, type);                 }
    public static int       getLineNo(DetailAST that)                         { return (Integer)   inv(getLineNo_, that);                         }
    public static void      setLineNo(DetailAST that, int lineNo)             { ;                  inv(setLineNo_int_, that, lineNo);             }
    public static int       getColumnNo(DetailAST that)                       { return (Integer)   inv(getColumnNo_, that);                       }
    public static void      setColumnNo(DetailAST that, int columnNo)         { ;                  inv(setColumnNo_int_, that, columnNo);         }
    public static int       getLine(DetailAST that)                           { return (Integer)   inv(getLine_, that);                           }
    public static int       getColumn(DetailAST that)                         { return (Integer)   inv(getColumn_, that);                         }
    public static DetailAST getLastChild(DetailAST that)                      { return (DetailAST) inv(getLastChild_, that);                      }
    public static boolean   branchContains(DetailAST that, int type)          { return (Boolean)   inv(branchContains_int_, that, type);          }
    public static DetailAST getPreviousSibling(DetailAST that)                { return (DetailAST) inv(getPreviousSibling_, that);                }
    public static DetailAST findFirstToken(DetailAST that, int type)          { return (DetailAST) inv(findFirstToken_int_, that, type);          }
    public static DetailAST getNextSibling(DetailAST that)                    { return (DetailAST) inv(getNextSibling_, that);                    }
    public static DetailAST getFirstChild(DetailAST that)                     { return (DetailAST) inv(getFirstChild_, that);                     }
    public static int       getNumberOfChildren(DetailAST that)               { return (Integer)   inv(getNumberOfChildren_, that);               }
    public static void      removeChildren(DetailAST that)                    { ;                  inv(removeChildren_, that);                    }

	private static Method
	meth(String methodName, Class<?>... parameterTypes) {
		try {
			return DetailAST.class.getDeclaredMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			return null; // Method not declared, e.g. "getText()" and "setText(String)" in CS 8.20
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	private static Object
    inv(Method method, Object target, Object... arguments) {
		assert method != null : "Method missing in that CheckStyle version";
		try {
			return method.invoke(target, arguments);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
