(ns differ-selenium.utils
  (:import [org.apache.commons.io FilenameUtils])
  )

(defn normalize-path [path]
  (FilenameUtils/normalize (format "%s" (.toAbsolutePath (java.nio.file.Paths/get path (into-array [""])))))
  )
