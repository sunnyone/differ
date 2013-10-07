(defproject differ-selenium "0.1.0-SNAPSHOT"
  :description "Selenium tests for project Differ"
  :url "http://differ.nkp.cz"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-webdriver/clj-webdriver "0.6.0"]
                 ]
  :main differ-selenium.core
  :aot [differ-selenium.core]
  )
