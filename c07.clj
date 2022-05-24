(ns c07)
;; Chapter 7. Let

(defn compute-discount-amount 
  [amount discount-percent min-charge]
  (let [discounted-amount (* amount (- 1.0 discount-percent))]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))

(compute-discount-amount 10 0.02 5.0)
;=> 9.8

(def anon-book {:title "Sir Gawain and the Green Knight"})
(def with-author {:title "Once and Future King" :author "White"})


;; if-let
(defn uc-author [book]
(if-let [author (:author book)]
  (.toUpperCase author) ; then case
  "ANONYMOUS"           ; else case 
  ))

;; when-let
(defn uppercase-author [book]
  (when-let [author (:author book)]
  (.toUpperCase author)))