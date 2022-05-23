(ns c13)
;; Chapter 13. Records and Protocols


(defrecord FictionalCharacter [name appears-in author])

;; defrecord creates a couple of fns 
;; ->FictionalCharacter
;; map->FictionalCharacter

;; create record instance 
(def watson (->FictionalCharacter "John Watson" "Sign of the Four" "Doyle"))

;; alternatively, we can create a record instance with a map
(def elizabeth (map->FictionalCharacter {
                  :name "Elizabeth Bennet"
                  :appears-in "Pride and Prejudice"
                  :author "Austen"}))


(:name elizabeth)
(:appears-in watson)

;; let's inspect different records 
(defrecord Supercomputer [cpu no-cpus storage-gb])
(def watson-2 (->Supercomputer "Power7" 2880 4000))

(class watson)
;=>user.FictionalCharacter
(class watson-2)
;=>user.Supercomputer

(instance? FictionalCharacter watson)
(instance? Supercomputer watson-2)


;; PROTOCOLS

(defrecord Employee[first-name last-name department])
(def alice (->Employee "Alice" "Smith" "Engineering"))

(defprotocol Person 
  (full-name [this])
  (greeting [this msg])
  (description [this]))


;; to provide implementation for each record type 
;; which implements Person we need to define methods
(defrecord FictionalCharacter[name appears-in author]
  Person
  (full-name [this] (:name this))
  (greeting [this msg] (str msg " " (:name this)))
  (description [this] (str (:name this)
                           " is a character in "
                           (:appears-in this))))

(defrecord Employee[first-name last-name department]
  Person 
  (full-name [this] (str (:first-name this) " " (:last-name this)))
  (greeting [this msg] (str msg " " (:first-name this)))
  (description [this] (str (:first-name this) " works in " (:department this))))

;; let's define some instances 

(def sofia (->Employee "Sofia" "Diego" "Finance"))
(def ian (->FictionalCharacter "Ian Malcolm" "Jurassic Park" "Crichton"))

(full-name sofia)
;=> "Sofia Diego"

(description ian)
;=> "Ian Malcolm is a character in Jurassic Park"


;; how to add a protocol to existing records?

(defprotocol Marketable
  (make-slogan [this]))

(extend-protocol Marketable
  Employee
  (make-slogan [e] (str (:first-name e) " is the BEST!" ))
  FictionalCharacter
  (make-slogan [fc] (str (:name fc) " is the GREATEST character!"))
  Supercomputer
  (make-slogan [sc] (str "This computer has " (:no-cpus sc) " CPUs!")))