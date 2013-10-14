(ns differ-selenium.pages.index
  (:use clj-webdriver.taxi)
  )

(defstruct page-input  :button-compare :image-01 :image-02)
(defstruct image-input :button-upload :thumbnail)

(def page-elements
  (struct page-input
          ".compare-button-caption"
          (struct image-input
                  "div.v-absolutelayout-wrapper:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > form:nth-child(1) > div:nth-child(1) > input:nth-child(2)"
                  "div.v-absolutelayout-wrapper:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > img:nth-child(1)"
                  )
          (struct image-input
                  "div.v-absolutelayout-wrapper:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > form:nth-child(1) > div:nth-child(1) > input:nth-child(2)"
                  "div.v-absolutelayout-wrapper:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > img:nth-child(1)"
                  )
          )
  )

;; "/opt/differ/docs/examples/images_01/01.jpg"
;; "/opt/differ/docs/examples/images_01/02.jpg"

(defn wait-for-page [timeout]
  (wait-until #(exists? ((page-elements :image-01) :button-upload)) timeout)
  )

(defn do-compare [image-01-path image-02-path timeout]
  (let [el1 (find-element {:css ((page-elements :image-01) :button-upload) }) 
        el2 (find-element {:css ((page-elements :image-02) :button-upload) })
        ]
    (send-keys el1 image-01-path)
    (send-keys el2 image-02-path)
    (wait-until #(exists? ((page-elements :image-01) :thumbnail)) timeout)
    (wait-until #(exists? ((page-elements :image-02) :thumbnail)) timeout)
    (click (page-elements :button-compare))
    )
  )

