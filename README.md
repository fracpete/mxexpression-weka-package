MxExpression
==============

Weka package that uses the [mXparser](https://sourceforge.net/projects/mxparser/) 
library for mathematical expressions on numeric attributes.

The following schemes are available:

* `weka.filters.unsupervised.attribute.MXExpression` - updates the specified
  target attribute with the value derived from the the expression.
* `weka.classifiers.functions.MXExpression` - simple classifier that just
  applies a formula to the data and outputs the calculated value as prediction.
  This can be used to compare algorithms based on mathematical formulas with
  other Weka classifiers.
  
You can use attribute values via their 1-based index (as in `attX` and *X* being the
1-based index) or their name (lower case name gets stripped off of all non-alphanumeric 
characters, eg 'My Att 1' becomes 'myatt1'). 

It is also possible to restrict the evaluation to a subset of the attributes in
the data, by using the `-attributes` option to define the attribute range. The
default is to operate on all attributes. 
  
Main functionality of the [mXparser](https://sourceforge.net/projects/mxparser/) library:
* basic operators, i.e.: +, -, *, ^, !
* Boolean logic operators i.e.: or, and, xor
* binary relations i.e.: =, <, >
* math functions (large library of 1-arg, 2-arg, 3-arg - - functions) i.e.: sin, cos, Stirling numbers, log, inverse functions
* constants (large library), i.e.: pi, e, golden ratio
* n-args functions i.e.: greatest common divisor
* iterated summation and product operators
* differentiation and integration


Releases
--------

Click on one of the following links to download the corresponding Weka package:

* [2018.3.2](https://github.com/fracpete/mxexpression-weka-package/releases/download/v2018.3.2/mxexpression-2018.3.2.zip)
* [2018.2.19](https://github.com/fracpete/mxexpression-weka-package/releases/download/v2018.2.19/mxexpression-2018.2.19.zip)


How to use packages
-------------------

For more information on how to install the package, see:

http://weka.wikispaces.com/How+do+I+use+the+package+manager%3F


Maven
-----

Add the following dependency in your `pom.xml` to include the package:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>mxexpression-weka-package</artifactId>
      <version>2018.3.2</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>nz.ac.waikato.cms.weka</groupId>
          <artifactId>weka-dev</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```
