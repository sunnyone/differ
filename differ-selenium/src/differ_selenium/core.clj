(ns differ-selenium.core
  (:gen-class)
  (:require [clojure.tools.cli :as c]
            [differ-selenium.pages.index :as page-index] 
            [differ-selenium.pages.comparison :as page-comparison]
            [differ-selenium.utils :as utils]
            )
  (:use clojure.test
        clj-webdriver.taxi
        clj-webdriver.window
        )
  )

;; (is (thrown? ArithmeticException (/ 1 1)) "haze vyjimku")
;; (is (not (thrown? ArithmeticException (/ 1 1))) "haze vyjimku")

;; (deftest prvni-test
;;   (testing "prvni test"
;;     (is (= 1 4) "ma byt jednicka")
;;     (is (thrown? ArithmeticException (/ 1 1)) "haze vyjimku")
;;     )
;;   )

;; (deftest druhy-test
;;   (testing "druhy test"
;;     (is (= 1 4) "ma byt jednicka")
;;     (is (thrown? ArithmeticException (/ 1 1)) "haze vyjimku")
;;     )
;;   )

;; (prvni-test)

(defn compare-images [image-01-path image-02-path timeout]
  (page-index/do-compare image-01-path image-02-path timeout)
  (page-comparison/wait-for-page timeout)
  )

(defmacro create-testings [pairs] 
  (for [[img01 img02] pairs]
    (list 'testing (format "compare image %s with %s" img01 img02)
          (list 'is (list '= '2 '3))
          )
    )
  )

;;(macroexpand '(create-compare-test "compare-01" "/opt/differ/docs/examples/images_01/01.jpg" "/opt/differ/docs/examples/images_02/01.jpg"))

(defn -main [& args]
  (let [[options args banner] (c/cli args
                                     "This program loads two images into web http://differ.nkp.cz using Firefox."
                                     ["-h" "--help" "Show help" :default false :flag true]
                                     ["-i1" "--image-01" "Path of the first image to compare"]
                                     ["-i2" "--image-02" "Path of the second image to compare"]
                                     ["-d1" "--dir-01" "Path of a directory from first image will be taken to compare"]
                                     ["-d2" "--dir-02" "Path of a directory from the second image will be taken to compare"]
                                     ["-t" "--timeout" "Default timeout for responses [sec]" :default 200000 :parse-fn #(* 1000 (Integer. %))]
                                     ["-u" "--url" "URL of differ web application]" :default "http://differ.nkp.cz"]
                                     )
        ]
    (when (or (:help options)
              (and (or (:image-01 options)
                       (:image-02 options)
                       (not (:dir-01 options))
                       (not (:dir-02 options)))
                   (or (not (:image-01 options))
                       (not (:image-02 options))
                       (:dir-01 options)
                       (:dir-02 options))))
      (println banner)
      (System/exit 0))

    (set-driver! {:browser :firefox})
    (window-maximize)

    (when (and (:dir-01 options) (:dir-02 options))
      (doseq [[img01 img02] (map vector
                                 (differ-selenium.utils/files-in-dir (:dir-01 options))
                                 (differ-selenium.utils/files-in-dir (:dir-02 options))
                                 )
              ]
        (do
          (to (:url options))
          (page-index/wait-for-page (:timeout options))
          (compare-images (utils/normalize-path (.toString img01)) (utils/normalize-path (.toString img02)) (:timeout options))
          (page-comparison/go-back (:timeout options))
          )
        )
      )
    (when (and (:image-01 options)
               (:image-02 options)
               )
      (do
        (to (:url options))
        (page-index/wait-for-page (:timeout options))
        (compare-images (utils/normalize-path (:image-01 options))
                        (utils/normalize-path (:image-02 options))
                        (:timeout options)
                        )
        )
      )


    ;;(run-tests 'differ-selenium.core)
    (println "Press any key to quit.")
    (read-line)
    
    ;;(run-tests 'differ-selenium.core)
    ;; (set-driver! {:browser :firefox} (:url options))
    ;; (window-maximize)
    ;; (Thread/sleep 2000)
    ;; (compare-images (utils/normalize-path (:image-01 options)) (utils/normalize-path (:image-02 options)))
    ;; (println "Press any key to quit.")
    ;; (read-line)
    (quit)
    )
  )

;; lein run -- --dir-01 ../docs/examples/images_01/--dir-02 ../docs/examples/images_02/
