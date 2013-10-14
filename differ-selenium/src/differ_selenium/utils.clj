(ns differ-selenium.utils
  (:import [org.apache.commons.io FilenameUtils])
  (:require me.raynes.fs)
  )

(defn normalize-path [path]
  (FilenameUtils/normalize (format "%s" (.toAbsolutePath (java.nio.file.Paths/get path (into-array [""])))))
  )


(defn files-in-dir [path]
  (sort (filter  me.raynes.fs/file?
                 (map (partial clojure.java.io/file path)
                      (me.raynes.fs/list-dir path)
                      )
                 )
        )
  )

;(files-in-dir "/opt/differ/docs/examples/images_01")



