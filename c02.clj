(ns c02)
;; Chapter 02. Vectors and Lists

;; the same as “[true 3 "four" 5]”
(vector true 3 "four" 5)

(def novels ["Emma" "Coma" "War and Peace"])
(first novels) ;=> Emma
(rest novels) ;=> "Coma" "War and Peace"


(def year-books ["1491" "1984" "2001" "April 1856"])
(nth year-books 2)
;; it's also possible to call vector as a function
;; with index as an argument, so the next is eq to last 
(year-books 2)

;; conj(unction)
;; to grow a vector at the end
(conj novels "Carrie")
;=> ["Emma" "Coma" "War and Peace" "Carrie"]


;; cons(truct)
;; will append, but also return a seq

(cons "It" novels)
;=> ("It" "Emma" "Coma" "War and Peace")