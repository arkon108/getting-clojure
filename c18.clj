(ns c18)
;; Chapter 18. State 

;; Atoms
(def counter (atom 0))

(defn greeting-message [req]
  (swap! counter inc)
  (if (= @counter 500)
    (str "Congrats! You are the " @counter " visitor")
    (str "Welcome to Blotts Books!")))

(swap! counter + 12)


(ns inventory)

(def by-title (atom {}))

(defn add-book [{title :title :as book}]
  (swap! by-title #(assoc % title book)))

(defn del-book [title]
  (swap! by-title #(dissoc % title)))

(defn find-book [title]
  (get @by-title title))


;; Refs
(def by-title (ref {}))
(def total-copies (ref 0))

(defn add-book [{title :title :as book}]
  (dosync
    (alter by-title #(assoc % title book))
    (alter total-copies + (:copies book))))

;; Agents
(def by-title (agent {}))

(defn add-book [{title :title :as book}]
  (send
    by-title
    (fn [by-title-map]
      ;; side-effect causing fn 
      (notify-inventory-change :add book) 
      ;; update to agent via fn
      (assoc by-title-map title book))))

; create an agent
(def title-agent (agent "Pride and Prejudice"))

;; update value 
(send title-agent #(str % " and Zombies"))

(if (agent-error title-agent)
  (restart-agent
    title-agent
    "Poseidon Adventure"
    :clear-actions true))

(defn -main []
  ;; doing stuff with agents 
  (shutdown-agents)
)