Differ Selenium tests and utils
========================================================================================

The tests are written in Clojure.

You can even use the tests for using web application from command line.


You can:

- upload two images and compare them as anonymous

Installation
-----------------------

see detail: http://leiningen.org/#install

or:

http://leiningen-win-installer.djpowell.net/ for Windows

in shortly:

1. Make sure you have a Java JDK version 6 or later.
2. Download the script:

   `script for UNIX <https://raw.github.com/technomancy/leiningen/stable/bin/lein>`_

   `script for Windows <https://raw.github.com/technomancy/leiningen/stable/bin/lein.bat>`_
3. Place it on your $PATH. (~/bin is a good choice if it is on your path.)
4. Set it to be executable. (chmod 755 ~/bin/lein)

Usage
---------

From source
...........

download sources from repository:
https://github.com/Differ-GSOC/differ.git

- using source

  ::
     
     cd differ-selenium
     lein run -- --image-01 PATH_01 --image-02 PATH_02

- using jar

  ::

     cd differ-selenium
     lein uberjar
     java -jar target/differ-selenium-0.1.0-SNAPSHOT-standalone.jar --image-01 PATH_01 --image-02 PATH_02

- using bat file

  ::

     cd differ-selenium
     compare.bat PATH_01 PATH_02

Using jar
..........

- download jar file:

  https://github.com/Differ-GSOC/differ/blob/master/differ-selenium/target/differ-selenium-0.1.0-SNAPSHOT-standalone.jar

::
     java -jar differ-selenium-0.1.0-SNAPSHOT-standalone.jar --image-01 PATH_01 --image-02 PATH_02


  

License
--------------

Copyright © 2013 Jan Stavěl (stavel.jan at gmail.com)

Distributed under the Eclipse Public License, the same as Clojure.
