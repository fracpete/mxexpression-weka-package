How to make a release
=====================

Preparation
-----------

* Change the artifact ID in `pom.xml` to today's date, e.g.:

  ```
  2018.2.19-SNAPSHOT
  ```

* Update the version, date and URL in `Description.props` to reflect new
  version, e.g.:

  ```
  Version=2018.2.19
  Date=2018-02-19
  PackageURL=https://github.com/fracpete/mxexpression-weka-package/releases/download/v2018.2.19/mxexpression-2018.2.19.zip
  ```

* Commit/push all changes


Weka package
------------

* Run the following command to generate the package archive for version
  `2018.2.19`:

  ```
  ant -f build_package.xml -Dpackage=mxexpression-2018.2.19 clean make_package
  ```

* Create a release tag on github (`v2018.2.19`)
* add release notes
* upload package archive from `dist`
* add link to this zip file in the `Releases` section of the `README.md` file


Maven Central
-------------

* Run the following command to deploy the artifact:

  ```
  mvn release:clean release:prepare release:perform
  ```

* After successful deployment, push the changes out:

  ```
  git push
  ```

* After the artifacts show up on central, update the artifact version used
  in the dependency fragment of the `README.md` file

