(ns ch04)

;; Chapter 04. Logic

(if true
 (println "The check was true")
 (println "The check was false"))


;; asking questions
(= "One" "Two")
;=> false

(not= "Anna" "Emma")
;=> true

;; evaluating multiple expressions with do
(do
  (println "This is four expressions")
  (println "All grouped together as one")
  (println "That prints stuff and then evaluates to 44")
  44)


;; when - implicit do
(def preferred-customer true)
(when preferred-customer
 (println "Hello preferred customer")
 (prinln "Welcome back to Blotts Books!"))


;; multiple conditions with cond
;; :else is default
(defn shipping-charge [preferred-customer order-amount]
(cond 
  preferred-customer       0.0
  (< order-amount 50.0)    5.0
  (< order-amount 100.0)   10.0
  :else                    (* 0.1 order-amount)))

;; switch checks the constants - status in this case WON'T get evaluated
(defn customer-greeting [status]
(case status
      :gold  "Welcome welcome welcome back!!!"
      :preferred "Welcome back!"
      "Welcome to Blotts Books"))


;; Exception handling
(try 
  (publish-book book)
  (catch ArithmeticException e 
    (println "Math problem")))

;; throwing
(defn publish-book [book]
(when (not (:title book))
  (throw (ex-info "A book needs a title!" {:book book})))
  ;...
  )
;; To catch the exception thrown like this, I need to look for exceptions of type clojure.lang.ExceptionInfo