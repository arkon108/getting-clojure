(ns c15)
;; Chapter 15. Spec

(ns inventory.core
(:require [clojure.spec.alpha :as s]))

;; key fn supplied by spec is "valid?"
(s/valid? number? 44) ;true
(s/valid? number? :hello) ;false


(def n-gt-10 (s/and number? #(> % 10)))

(def n-or-s (s/or :a-number number? :a-string string?))

(s/valid? n-or-s 10)
(s/valid? n-or-s "ha")

(def coll-of-strings (s/coll-of string?))

;; or a collection of numbers or strings
(def coll-of-n-or-s (s/coll-of n-or-s))

(def s-n-s-n (s/cat :s1 string? :n1 number? :s2 string? :n2 number?))

(s/valid? s-n-s-n ["Emma" 1815 "Jaws" 1974])
;; true


(def book-s 
    (s/keys :req-un [:inventory.core/title
                     :inventory.core/author
                     :inventory.core/copies]))


(s/def 
  :inventory.core/book
  (s/keys 
  :req-un
  [:inventory.core/title :inventory.core/author :inventory.core/copies]))

(s/valid? :inventory.core/book {:title "Dracula" :author "Stoker" :copies 10})


;; inventory.core assumed, hence ::
(s/def ::title string?)
(s/def ::author string?)
(s/def ::copies int?)

(s/def ::book (s/keys :req-un [::title ::author ::copies]))

(s/explain ::book {:author :austen :title :emma})

(s/conform s-n-s-n ["Emma" 1815 "Jaws" 1974])

;; an inventory is a collection of books
(s/def :inventory.core/inventory (s/coll-of ::book))

(defn find-by-title 
  [title inventory]
  {:pre [(s/valid? ::title title)
         (s/valid? ::inventory inventory)]}
  (some #(when (= (:title %) title) %) inventory))

 ;; Define the fn
(defn find-by-title
  [title inventory]
  (some #(when (= (:title %) title) %) inventory))

;; Register a spec 
  (s/fdef find-by-title 
    :args (s/cat :title ::title
           s/cat :inventory ::inventory))

 (require '[clojure.spec.test.alpha :as st])

 (st/instrument 'inventory.core/find-by-title)

 (defn book-blurb [book]
  (str "The best selling book " (:title book) " by " (:author book)))

(s/fdef book-blurb :args (s/cat :book ::book))


(require '[clojure.spec.test.alpha :as stest])
(stest/check 'inventory.core/book-blurb)

(s/fdef book-blurb
  :args (s/cat :book ::book)
  :ret (s/and string? (partial re-find #"The best selling"))

(defn check-return [{:keys [args ret]}]
  (let [author (-> args :book :author)]
  (not (neg? (.indexOf ret author))))
)

(s/fdef book-blurb
  :args (s/cat :book ::book)
  :ret (s/and string? (partial re-find #"The best selling"))
  :fn check-return
)