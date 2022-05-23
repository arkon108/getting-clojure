(ns c06)
;; Functional Things
 
(def dracula { :title "Dracula"
               :author "Stoker"
               :price 1.99
               :genre "Horror"})


(defn cheap? [book]
 (when (<= (:price book) 9.99)
 book))

(defn pricey? [book]
 (when (> (:price book) 9.99)
 book))

(defn horror? [book]
 (when (= (:genre book) "Horror")
 book))

(defn adventure? [book]
 (when (= (:genre book) "Horror")
 book))


(def words ["A" " cheese " " is " " better " " than " " dirt!"])
(apply str words)


;; a function which returns a function
(defn cheaper-f [max-price]
 (fn [book]
  (when (<= (:price book) max-price)
   book)))

;; some helpful functions
(def real-cheap? (cheaper-f 1.00))
(def kind-of-cheap? (cheaper-f 1.99))
(def marginally-cheap? (cheaper-f 5.99))


;; partial
(def my-inc (partial + 1))

(defn cheaper-than [max-price book]
 (when (<= (:price book) max-price)
 book))

(def real-cheap? (partial cheaper-than 1.00))
(def kind-of-cheap? (partial cheaper-than 1.99))
(def marginally-cheap? (partial cheaper-than 5.99))

;; complement
(def not-number? (complement number?))

;; every-pred
(def cheap-horror-possesion? 
(every-pred 
 cheap? 
 horror? 
 (fn [book] (= (:title book) "Posession"))))