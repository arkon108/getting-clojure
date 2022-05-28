(ns c20)
;; Chapter 20. Macros
(defn arithmetic-if [n pos zero neg]
 (cond 
    (pos? n) pos
    (zero? n) zero
    (neg? n) neg))

;; this would indeed return :meh 
(arithmetic-if 0 :great :meh :boring)

;; however, this would evaluate each println:

(defn print-rating [rating]
  (arithmetic-if rating 
    (println "Good book!")
    (println "Totally indifferent.")
    (println "Run away!")))

(defmacro arithmetic-if [n pos zero neg]
  (list 'cond (list 'pos? n) pos
              (list 'zero? n) zero 
              :else neg))

(arithmetic-if rating :loved-it :meh :hated-it)
;; gets turned into
(cond 
  (pos? rating) :loved-it
  (zero? n) :meh 
  :else :hated-it)


;; set up some values 
(def n 100)
(def pos "It's positive!")
(def zero "It's a zero!")
(def neg "It's negative!")

;; plug them in cond
`(cond 
  (pos? ~n) ~pos
  (zero? ~n) ~zero
  :else ~neg)


;; run this and we get 
(cond 
  (pos? 100) "It's positive!"
  (zero? 100) "It's a zero!"
  :else "It's negative!")

;; or actually, to avoid ns ambiguity 
(clojure.core/cond 
  (clojure.core/pos? 100) "It's positive!"
  (clojure.core/zero? 100) "It's a zero!"
  :else "It's negative!")


;; syntax quoting
(defmacro arithmetic-if [n pos zero neg]
  `(cond 
    (pos? ~n) ~pos 
    (zero? ~n) ~zero
    :else ~neg))


;; without syntax quoting 
(defmacro arithmetic-if [n pos zero neg]
  (list 'cond (list 'pos? n) pos
              (list 'zero? n) zero 
              :else neg))

;; debugging
(macroexpand-1 '(arithmetic-if 100 :pos :zero :neg))
;; outputs
(cond (pos? 100) :pos (zero? 100) :zero :else :neg)