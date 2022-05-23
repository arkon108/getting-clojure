(ns c10)
;; Chapter 10. Sequences

;; making any collection into a sequence makes it possible
;; to have single count method 

(defn my-count [col]
  (let [the-seq (seq col)]
    (loop [s the-seq n 0]
      (if (seq s) ; returns nil if passed an empty sequence
        (recur (rest s) (inc n))
        n))))


(def book-list ["2001" "Clarke" "Foundation" "Asimov"])
(partition 2 book-list)

(def titles ["Večernji akt" "Smogovci"])
(def authors ["Pavličić" "Hitrec"])
(interleave titles authors)

(def scary-animals ["tigers" "bears" "lions"])
(interpose "and" scary-animals)


; map
(def nums [1 11 22 13])
(def doubled (map #(* 2 %) nums))


(def books [{:title "Deep Six" :price 13.99 :genre :sci-fi :rating 6}
            {:title "Dracula" :price 1.99 :genre :horror :rating 7}
            {:title "Emma" :price 7.99 :genre :comedy :rating 9}
            {:title "2001" :price 10.50 :genre :sci-fi :rating 5}])

;; get the collection of titles
(map :title books)

;; let's get the count of titles 
(map #(count (:title %)) books)

;; a cleaner way would be to build the fn using `comp`
(map (comp count :title) books)


;; reduce

(def numbers [10 20 30 40 50])

;; let's get a sum total of numbers in this sequence 
(reduce + 0 numbers)

;; finding the highest priced book
(defn hi-price [hi book]
  (if (> (:price book) hi)
    (:price book)
    hi))

(reduce hi-price 0 books)

;; let's get the highest rating books
;; like this 
;; "Emma // Dracula // Deep Six"

(defn format-top-titles [books] (apply str
(interpose " // " (map :title (take 3 (reverse (sort-by :rating books)))))))

;; this is a bit hard to read, so let's rewrite it with a bit of threading 

(defn format-top-titles2 [books]
  (->> books
    (sort-by :rating)
    reverse
    (take 3)
    (map :title)
    (interpose " // ")
    (apply str)))