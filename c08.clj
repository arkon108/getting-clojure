(ns c08)
;; Chapter 8. Def, Symbols, and Vars

;; Everyone's favorite universal constant
(def PI 3.14)

(def author "Austen")

'author ; the symbol author, not it's value

#'author ; var for author
;;=>user/author

(def the-var #'author)

(.get the-var) ; get the value of the-var: "Austen"
(.-sym the-var); get the  symbol for the var: author


(def debug-enabled false)

(defn debug [msg]
  (if debug-enabled
    (println msg)))


;; bindings which can be changed
;; they have earmuffs!
(def ^:dynamic *debug-enabled* false)

(defn debug [msg]
  (if *debug-enabled*
    (println msg)))

(binding [*debug-enabled* true]
  (debug "Calling that darn function")
  (some-troublemaking-function-needing-logging)
  (debug "back from nasty function"))


;; print-length
(def books "2001" "1984" "2010" "Tribe of the Cave Bear")
(set! *print-length* 2)
books  
;=> ["2001" "1984"...]