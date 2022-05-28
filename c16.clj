(ns c16)
;; Chapter 16. Interoperating with Java


;; Java class method for getting a file:
(def authors (java.io.File. "authors.txt"))

;; "authors" is an object
;; we can call methods on it
(if (.exists authors)
  (println "There is a file authors.txt")
  (println "No such file :'("))

(if (.canRead authors)
  (println "we can read it!"))

;; if we have appropriate permissions
(.setReadable authors true)


;; public fields of Java objects
(def rect (java.awt.Rectangle. 0 0 10 20))

(println "Width " (.-width rect))
(println "Height: " (.-height rect))


;; importing - like require/:as

;; in the REPL 
(import java.io.File)

;; a the top of the file 
(ns read-authors 
  (:import java.io.File))

;; once imported, we can refer to  the class without its package name 
(def author (File. "authors.txt"))

;; in clj file 
(ns read-authors
  (:import (java.io File InputStream)))

;; in the REPL 
(import '(java.io File InputStream))

(import java.io.File)
File/separator
;=> "/"

;; create a temporary file in the standard temp dir 
(def temp-authors-file (File/createTempFile "authors_list" ".txt"))


;; nope 
(def count-method .count)


(def files [(File. "authors.txt") (File. "titles.txt")])
(map (memfn exists) files)