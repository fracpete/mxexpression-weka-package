MxExpression
==============

Weka package that uses the [mXparser](https://sourceforge.net/projects/mxparser/) 
library for mathematical expressions on numeric attributes.

The following schemes are available:

* `weka.filters.unsupervised.attribute.MXExpression` - applies the expression
  to the specified attributes.
* `weka.classifiers.functions.MXExpression` - simple classifier that just
  applies a formula to the data to make a prediction.

Releases
--------

Click on one of the following links to download the corresponding Weka package:

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
      <version>2018.2.19</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>nz.ac.waikato.cms.weka</groupId>
          <artifactId>weka-dev</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```
