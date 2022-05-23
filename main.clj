;; notes & exercises for the Russ Olsen - Getting Clojure book
(ns main)
(def chapters ["c01.clj" "c02.clj" "c03.clj" "c04.clj"])
(apply load-file chapters)
(require 'c01)
(require 'c02)
(require 'c03)
(require 'c04)