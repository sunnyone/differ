(defproject differ-installer "0.1.0-SNAPSHOT"
  :description "Installer for DIFFER project"
  :url "http://differ.nkp.cz"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [seesaw "1.4.4"]
                 ]
  :profiles {:dev {:plugins [[com.jakemccrary/lein-test-refresh "0.3.9"]
                             [seesaw "1.4.4"]
                             [lein-autoreload "0.1.0"]
                             ]}}
  :main differ-installer.core
  )
