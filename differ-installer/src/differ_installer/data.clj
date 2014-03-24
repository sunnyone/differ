(ns differ-installer.data
  )

(defmulti install (juxt :os :extractor :install-path))
