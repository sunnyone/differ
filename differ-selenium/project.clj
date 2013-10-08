(defproject differ-selenium "0.1.0-SNAPSHOT"
  :description "Selenium tests for project Differ"
  :url "http://differ.nkp.cz"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-webdriver/clj-webdriver "0.6.0"
                  :exclusions [org.seleniumhq.selenium/selenium-server]]
                 [org.seleniumhq.selenium/selenium-server "2.35.0"]
                 [org.clojure/tools.cli "0.2.4"]
                 [org.apache.commons/commons-io "1.3.2"]
                 ]
  :main differ-selenium.core
  )
