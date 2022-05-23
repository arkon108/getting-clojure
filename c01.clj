(ns c01)

;; Chapter 01. Hello, Clojure

(str "Concat" " " "strings")

(count "Will give a number of characters")
(count ["or" "also" :count :of :vector :members])

;; division / will give a ratio
(/ 8 3) ;=> 8/3

;; to get int truncation, use "quot"(ient)
(quot 8 3) ;=> 2


;; function definition
(defn hello-world [] (println "Hello, world!"))


;; declaring things (if needed before they are defined)
(declare some-fn)

(println some-fn "hello")

(defn some-fn [s] 
 (str s " there!"))