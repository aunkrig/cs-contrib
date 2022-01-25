# cs-contrib

This is an extension for the [CheckStyle](https://checkstyle.sourceforge.io/) tool which provides several checks and filters.

The project homepage in [here](http://cs-contrib.unkrig.de).

The documentation for the checks and filters is available [online](http://cs-contrib.unkrig.de/csdoc). (It was generated with the [CheckStyle doclet](http://cs-doclet.unkrig.de/).)

## Change log

### 1.0.8
* Fixed one NoClassDefFoundError that occurred with CS 9.2.1.

### 1.0.7
* Made the dependency on "doclet-cs-annotation" optional, so dependent artifacts don't inherit it. This is possible because doclet-cs-annotation contains only annotation classes, with retention SOURCE; thus, cs-contrib does not need them at runtime.
* Generation of the "Message.properties" files is now automated with an ANT script.
* Removed unnecessary dependency on GUAVA.
* Added new CS 8.45.1 tokens, mainly Java 17.
* Made cs-contrib more robust for unexpected token constellations.

### 1.0.5
* Fixed an NPE caused by a stray top-level semicolon.
* "Whitespace" check: "whitespaceAfter" and "noWhitespaceAfter" didnt work for "STATIC_INIT" (the "static" keyword in a static initializer) due to a terrible hack in the CheckStyle JavaRecognizer.

### 1.0.4
* SuppressionRegex filter: Added a parameter "influence", so now this filter is an almost-complete replacement for the Standard SuppressWithNearbyCommentFilter (which is limited to Checker-based checks). (And since CS version 8.21, LineLengthCheck is no longer Checker-based.)
* WrapTryCheck: Could not handle TRY statements with *only* a resource specification (and no CATCHES and no FINALLY).

### 1.0.3
* Cs-contrib now functions with CheckStyle <=8.20 *and* with Checkstyle >=8.21
* Implemented a workaround for the famous breaking API change in CheckStyle version 8.21. Now cs-contrib functions both with CS <= 8.20 AND with CS >= 8.21.

### 1.0.2
* Upgraded DOCLET-CS to 1.1.0 (type of "Rule.quickfixes" changed from "Class[]" to "String[]"). Still compiling against CS 8.19!
* 
