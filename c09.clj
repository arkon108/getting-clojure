(ns c09)
;; Chapter 9. Namespaces

(def literature ["Emma" "Oliver Twist" "Posession"])
(def horror ["It" "Posession" "Carrie"])

;; this would throw an exception
(clojure.data/diff literature horror)

;; because we haven't loaded the namespaced function, we need to require it 

(require 'clojure.data)