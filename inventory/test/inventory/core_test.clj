(ns inventory.core-test
  (:require [clojure.test :refer :all]
            [inventory.core :as i]))

(def books
[{:title "2001" :author "Clarke" :copies 21},
 {:title "Emma" :author "Austen" :copies 10}
 {:title "Misery" :author "King" :copies 101}
])

(deftest test-finding-books
(is (not (nil? (i/find-by-title "Emma" books)))))