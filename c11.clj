(ns c11)
;; Chapter 11. Lazy sequences

;; repeat 
(def jack "All work and no play makes Jack a dull boy")
(def repeated-text (repeat jack))


;; cycle
(take 7 (cycle [1 2 3]))
;=> (1 2 3 1 2 3 1)


;; iterate 
(def numbers (iterate inc 1))
(nth numbers 101)
;=> 102

;; lazy take
(def many-nums (take 1000000000 (iterate inc 1)))
(take 20 many-nums)

;; lazy map 

(def evens (map #(* 2 %) (iterate inc 1)))
(take 20 evens)
;=>(2 4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40)


;; lazily generated random book titles

(def sequels (iterate inc 1))
(def titles (map #(str "Wheel of Time, Book " %) numbers))

(def first-names ["Kim" "Arian" "Saša" "Maja" "Bubi" "Milivoj" "Flekica"])

(def last-names ["Karničar" "Mataić" "Marinković" "Puntarić" "Mijaukalić" "Lajoš"])


(defn combine-names [fname lname]
  (str fname " " lname))

; lazy infinite list of authors
(def authors (map combine-names
                  (cycle first-names)
                  (cycle last-names)))

; lazy infinite list of books

(defn make-book [title author]
  {:title title :author author})

(def test-books (map make-book titles authors))