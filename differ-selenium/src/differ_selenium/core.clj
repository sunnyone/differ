(ns differ-selenium.core
  (:gen-class)
  (:require clojure.test)
  )
(use 'clj-webdriver.taxi)

(defn -main [& args]
  ;; Start up a browser
  (set-driver! {:browser :firefox} "https://github.com/login")
  (input-text "#login_field" "jstavel")
  (input-text "#password" "aeLohjo9")
  (submit "#password")
  (click "a[href*='edeposit.policy']")
  (quit)
  )

