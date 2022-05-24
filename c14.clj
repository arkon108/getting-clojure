(ns c14)
;; Chapter 14. Tests

;; creating a test
(deftest test-finding-books
  (is (not (nil? (i/find-by-title "Emma" books))))
  (is (nil? (i/find-by-title "XSDD" books))))

;; organize with "testing"
(deftest test-basic-inventory
 (testing "Finding books")
   (is (not (nil? (i/find-by-title "Emma" books))))
   (is (nil? (i/find-by-title "XSDD" books)))
 (testing "Copies in inventory"
   (is (= 10 (i/number-of-copies-of "Emma" books))))
 )

;; running tests in namespaces
(test/run-tests)
(test/run-tests *ns*)
(test/run-tests 'inventory.core-test)

;; property based testing

(ns inventory.core-gen-test
  (:require [inventory.core :as i])
  (:require [clojure.test.check.clojure-test :as ctest])
  (:require [clojure.test.check :as tc])
  (:require [clojure.test.check.generators :as gen])
  (:require [clojure.test.check.properties :as prop]))
;


(def title-gen gen/string-alphanumeric)
;; generates alnum strings

(def copies-gen gen/pos-int)
;; generates positive integers

;; picking non-empty non-zero values
(def title-gen 
  (gen/such-that not-empty gen/string-alphanumeric))

(def copies-gen 
  (gen/such-that (complement zero?) gen/pos-int))

(def book-gen
  (gen/hash-map :title title-gen :author author-gen :copies copies-gen))

;; now we can generate an endless supply of inventories
(def inventory-gen
  (gen/not-empty (gen/vector book-gen)))

(def inventory-and-book-gen
  (gen/let [inventory inventory-gen
            book (gen/elements inventory)]
            {:inventory inventory :book book}))

;; given
;; (:require [clojure.test.check.properties :as prop])
;; (:require [clojure.test.check.generators :as gen])
;; this will check that each generated int is smaller than next positive int
(prop/for-all [i gen/pos-int]
  (< i (inc i)))

;; given 
;; (:require [clojure.test.check :as tc])
(tc/quick-check 50
  (prop/for-all [i gen/pos-int]
  (< i (inc i))))

(tc/quick-check 50
  (prop/for-all [i-and-b inventory-and-book-gen]
    (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
       (:book i-and-b))))

(ctest/defspec find-by-title-finds-books 50
  (prop/for-all [i-and-b inventory-and-book-gen]
    (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
       (:book i-and-b))))