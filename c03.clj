(ns c03)

  (def book {
             "title" "Oliver Twist"
             "author" "Charles Dickens"
             "published" 1838})

;; method to create would be `hash-map`, 
;; so the previous is equivalent to

(def book (hash-map "title" "Oliver Twist"
             "author" "Charles Dickens"
             "published" 1838))

;; get the date from the map
(get book "published")

;; more commonly, map itself is used as fn
(book "published")

;; when using keywords, it's possible to use keyword
;; as an invocation to retrieve values OutOfMemoryError

(def book2 {:title "Oliver Twist"
            :author "Charles Dickens"
            :published 1838})

;; these are same 
(book2 :title)
(:title book2)

;; adding to map
(assoc book2 :page-count 362)

;; removing from map 
(dissoc book2 :title)

;; getting keys from a map
(keys book2)
;=> (:title :author :published)

;; getting values
(vals book2)

;; sets
(def genres #{:scifi :mystery :thriller})

;; checking for members
(contains? genres :romance)

;; set can also be used as a function
(genres :scifi) ; if member not in set, will return nil

;; adding / conj 
(def more-genres (conj genres :biography))

;; removing / disj
(def less-genres (disj genres :scifi))

;; nil can be a value so be careful
(def book3 {:title "Alice in Wonderland"
            :author "Lewis Carrol"
            :published nil})

(book3 :published)
;=> nil
(contains? book3 :published)
;=> true

