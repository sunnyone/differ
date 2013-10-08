(ns differ-selenium.core
  (:gen-class)
  (:require [clojure.tools.cli :as c]
            [differ-selenium.pages.index :as page-index] 
            [differ-selenium.pages.comparison :as page-comparison]
            [differ-selenium.utils :as utils]            
            )
  )

(use 'clojure.test)
(use 'clj-webdriver.taxi)
(use 'clj-webdriver.window)

(defn compare-images [image-01-path image-02-path]
  (page-index/do-compare image-01-path image-02-path)
  (page-comparison/wait-for-page)
  )

(defn -main [& args]
  (let [[options args banner] (c/cli args
                                     "This program loads two images into web http://differ.nkp.cz using Firefox."
                                     ["-h" "--help" "Show help" :default false :flag true]
                                     ["-i1" "--image-01" "Path of the first image to compare"]
                                     ["-i2" "--image-02" "Path of the second image to compare"]
                                     )
        ]

    (when (or (:help options)
              (not (:image-01 options))
              (not (:image-02 options))
              ) 
      (println banner)
      (System/exit 0))
    
    (set-driver! {:browser :firefox} "http://differ.nkp.cz")
    (window-maximize)
    (Thread/sleep 2000)
    (compare-images (utils/normalize-path (:image-01 options)) (utils/normalize-path (:image-02 options)))
    (println "Press any key to quit.")
    (read-line)
    (quit)
    )
  )
