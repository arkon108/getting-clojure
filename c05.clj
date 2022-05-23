(ns c05)

;; multi-arity functions

(defn greet
  ([to-whom]
   (greet "Hello" to-whom))
  ([message to-whom]
   (println message to-whom)))


;; varargs or variadic functions
;; takes ANY number of args

(defn print-any-args [& args]
  (println "My arguments: " args))

(print-any-args 7 true nil "hello")

;; returns first argument
(defn first-argument [& args]
  (first args))

;; same!

(defn first-argument [x & args]
  x)


;;; Multimethods

(def b1 {:title "Hobbit" :author "JRR Tolkien"})
(def b2 {:book "Insomnia" :by "Stephen King"})
(def b3 ["1984" "George Orwell"])

; let's decide on the book format
(defn dispatch-book-format [book]
  (cond 
    (vector? book) :vector-book
    (contains? book :title) :standard-map
    (contains? book :book) :alt-map
  ))

; declare the multimethod which uses the previous fn 
; to categorize its arguments
(defmulti normalize-book dispatch-book-format)
; now we need to define implementations 
(defmethod normalize-book :vector-book [book]
  {:title (first book) :author (second book)})

(defmethod normalize-book :standard-map [book]
  book)

(defmethod normalize-book :alt-map [book]
  {:title (:book book) :author (:by book)})

; defmethod for :default; will handle everything else


;; Recursion
(def books [
            {:title "Jaws" :copies-sold 2000000}
            {:title "Emma" :copies-sold 3000000}
            {:title "2001" :copies-sold 4000000}
            ])

; we can write a recursive fn to get the total amount of sold books

(defn sum-copies 
  ([books] (sum-copies books 0))
  ([books total]
   (if (empty? books)
     total
     (sum-copies (rest books)
                 (+ total (:copies-sold (first books)))))))

;; there's a problem with sum-copies, its eatin the Stack
;; so it could overflow when th collection grows above a certain limit

(defn sum-copies-r 
  ([books] (sum-copies books 0))
  ([books total]
   (if (empty? books)
     total
     (recur 
      (rest books)
      (+ total (:copies-sold (first books)))))))

;; avoiding multi-arity fn with loop version

(defn sum-copies-l [books]
  (loop [books books total 0]
    (if (empty? books)
      total
      (recur 
         (rest books)
         (+ total (:copies-sold (first books))))
    )))

;; Docstrings

(defn average 
  "Return the average of a and b."
  [a b]
  (/ (+ a b) 2.0))

;; supported by macros and records too
;; e.g. `def`

(def ISBN-LENGTH 
  "Length of ISBN code"
  13)


;; Pre and Post conditions

(defn publish-book [book]
{:pre [(:title book)]}
  (print-book book)
  (ship-book book))


(defn publish-book [book]
{:pre [(:title book)]
 :post [(boolean? %)]}
  (print-book book)
  (ship-book book))
