(ns c12)
;; Chapter 12. Destructuring

(def authors [:monet :austen])

;; destructure in let
(let [[painter novelist] authors]
  (println "Painter is " painter)
  (println "Novelist is " novelist))


;; skipping some 
(def artists [:monet :austen :beethoven :dickinson])

(let [[_ _ composer poet] artists]
  (println "The composer is " composer)
  (println "The poet is " poet))


;; desctructuring in function argument list
(defn artist-description [[novelist poet]]
  (str "The novelist is " novelist " and the poet is " poet))

; call  
(artist-description [:austen :dickinson])


;; Maps
(def artist-map {
                 :painter :monet
                 :novelist :austen 
})

(let [{painter :painter writer :novelist} artist-map]
  (println painter "is a painter")
  (println writer "is a novelist"))

(def sith {
:name "Darth Vader"
:parents {:mother "Shmi" :father "The Force"}
})

(let [{{dad :father mom :mother} :parents} sith]
  (println "Vader's dad is" dad)
  (println "Vader's mom is" mom))


;; mixing and matching 
(def authors [
{:name "Jane Austen" :born 1775}
{:name "Charles Dickens" :born 1812}
])

(let [[{dob-1 :born} {dob-2 :born}] authors]
(println "One author was born in" dob-1)
(println "Other author was born in" dob-2))


; if we want to bind :name to name, :age to age and :gender to gender, you can pull out :keys 
(def character {:name "Romeo" :age 16 :gender "male"})

(defn character-desc [{:keys [name age gender]}]
  (str "Name:" name ", age:" age ", gender: " gender))


;; pulling keys out
(defn add-greeting [character]
(let [{:keys [name age]} character]
  (assoc character
  :greeting 
  (str "Hello, my name is " name " and I'm " age " years old.")
  )))

;; same, simpler, using :as 
(defn add-greeting[{:keys [name age] :as character}]
(assoc character
  :greeting 
  (str "Hello, my name is " name " and I'm " age " years old.")))