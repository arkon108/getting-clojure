(ns c01)

(str "Concat" " " "strings")

(count "Will give a number of characters")
(count ["or" "also" :count :of :vector :members])

;; division / will give a ratio
(/ 8 3) ;=> 8/3

;; to get int truncation, use "quot"(ient)
(quot 8 3) ;=> 2


;; function definition
;; 
(defn hello-world [] (println "Hello, world!"))