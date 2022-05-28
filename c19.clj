(ns c19)
;; Chapter 19. Read and Eval

(ns codetool.core
  (:require [clojure.java.io :as io]))

(defn read-source [path]
  (with-open [r (java.io.PushbackReader. (io/reader path))]
    (loop [result []]
      (let [expr (read r false :eof)]
      (if (= expr :eof)
        result 
        (recur (conj result expr))))))
)

;; a toy REPL
(defn russ-repl []
  (loop []
    (println (eval (read)))
    (recur)))

;; my own eval
(defn reval [expr]
  (cond 
    (string? expr) expr 
    (keyword? expr) expr 
    (number? expr) expr
    (symbol? expr) (eval-symbol expr)
    (vector? expr) (eval-vector expr)
    (list? expr) (eval-list expr)
    :else :completely-confused
  ))

;; evaluate symbols by looking them up in current namespace 
(defn eval-symbol [expr]
  (.get (ns-resolve *ns* expr)))

;; for vectors recursively evaluate the contents
(defn eval-vector [expr]
  (vec (map reval expr)))

;; for lists evaluate all the contents like with vectors and then call apply with first element as the function name 
(defn eval-list [expr]
  (let [evaled-items (map reval expr)
        f (first evaled-items)
        args (rest evaled-items)]
        (apply f args)))