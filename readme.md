Russ Olsen - Getting Clojure
============================

###[Book website @ Pragmatic Bookshelf](https://pragprog.com/titles/roclojure/getting-clojure/)

  * Pages: 288
  * Published: May 2018
  * ISBN: 9781680503005
  * read May 20th 2022.


Chapter 01. Hello, Clojure
--------------------------

Leiningen is a project manager, REPL runner and more. To start the (better than default) REPL, run

```clojure
lein repl
```

Clojure programmers adopted the convention to name the symbols "lower-case-with-dashes". That is referred to as *kebab case*

### Declarations

Usually, Clojure can only use symbols after they've been defined, but it's possible to declare them with

```clojure
(declare say-welcome ...)
```

Mostly clojurists stick to defining function before using them, reserving `declare` to mutually recursive fns.


Division will return a ratio, which is specific to Clojure.

```clojure
;; division / will give a ratio
(/ 8 3) ;=> 8/3

;; to get int truncation, use "quot"(ient)
(quot 8 3) ;=> 2
```


Chapter 02. Vectors and Lists
-----------------------------

`cons`(truct) will prepend and return a sequence.
`conj`(junction) will append and return a vector (if performed on a vector).

Vectors are continuous chunk of memory. Lists are implemented as linked lists. So getting 654'th element of a vector is faster than a list. For lists, it's simpler to prepend, for vectors to append. That's why `conj` will prepend a list, but append a vector.


Vectors can be created with literal syntax `[]` or with `vector` fn.

```clojure
;; the same as “[true 3 "four" 5]”
(vector true 3 "four" 5)
```

Most useful functions for vectors (and other collections) are `first`, `rest`, `nth`, and `count`.

```clojure
(def year-books ["1491" "1984" "2001" "April 1856"])

(count year-books)
;=> 4

(nth year-books 2)
;=> "2001"

;; it's also possible to call vector as a function
;; with index as an argument, so the next is eq to last call
(year-books 2)
```

When passing lists around, they need to be quoted, otherwise Clojure will try to evaluate them - since fn calls are lists where the first item is the fn name.

```clojure
'(1 2 3)
```

Lists can also be created with `list` function.

```clojure
(list 1 2 3 "four" 5 "six")
```

Chapter 03. Maps, Keywords and Sets
-----------------------------------

The method to create a map is `hash-map`.

Getting elements from map can be done like

```clojure
(get map-name "key")
;; this is the same
(map-name "key")
```

Order in maps is not guaranteed, there is `sorted-map` where order is important. 

Keywords are labels. Using keywords as keys in maps, make it possible to use keyword as a function call to get values from maps.

Adding stuff to maps is done with `assoc`

```clojure
(assoc book :page-count 362)
```

Sets have unique values. Look similar to maps
```clojure
(def genres #{:scifi :mystery :thriller})
```

Careful with fetching values if "nil" is possible value.
For example:

```clojure
(book3 :published)
;=> nil
(contains? book3 :published)
;=> true
```

Chapter 04. Logic
-----------------

`if` is not a fn, it's a special form. Because not all arguments will be evaluated.

```clojure
(if condition 
  (then)
  (else))
```

`=` is a function. So is `not=`. We also have a logical `not`, `or` and `and`.

What I expect as conditional operators are here too, `>`, `<`, `<=` and `>=`.

There's a number of "is that a?" functions:
`number?`, `string?`, `keyword?`, `map?`, `vector?`.

`false` and `nil` are only falsy values, _everything_ else is truthy. `0` is also truthy! Since no vector, even empty one equals `false`, every vector is truthy.

Since `if` only allows a single expression, we can use `do` to evaluate multiple expressions.

```clojure
(do
  (println "This is four expressions")
  (println "All grouped together as one")
  (println "That prints stuff and then evaluates to 44")
  44)
```

For situations where we only want to have `if...then`, without the `else`, Clojure has `when`, which has an implicit `do`.



`cond` is similar to a `switch`. If no predicates come back as truthy, it evaluates `nil`. 

```clojure
(defn shipping-charge [preferred-customer order-amount]
(cond 
  preferred-customer 0.0
  (< order-amount 50.0) 5.0
  (< order-amount 100.0) 10.0))
```
For a "default" kind of behavior, we have `:else` keyword. 

```clojure
(defn shipping-charge [preferred-customer order-amount]
(cond 
  preferred-customer 0.0
  (< order-amount 50.0) 5.0)
  (< order-amount 100.0) 10.0)
  :else (* 0.1 order-amount))
```


More similar to `switch` is `case`. The constants being checked are *constant* and won't get evaluated. 

```clojure
(defn customer-greeting [status]
(case status
      :gold  "Welcome welcome welcome back!!!"
      :preferred "Welcome back!"
      "Welcome to Blotts Books"))
```

### Exception handling
```clojure
(try 
  (publish-book book)
  (catch ArithmeticException e 
    (println "Math problem")))
```

#### Throwing exceptions
`ex-info` takes a string describing the error and a (possibly empty) map with any other important info. 

```clojure
(defn publish-book [book]
(when (not (:title book))
  (throw (ex-info "A book needs a title!" {:book book})))
  ;...
  )
```

To catch the exception thrown like this, I need to look for exceptions of type `clojure.lang.ExceptionInfo`.


Chapter 05. More Capable Functions
----------------------------------


### Multi-arity functions

Clojure supports functions with variable number of arguments. For example, a common pattern is a function which has one arity, usually with most arguments - which does something, and other arities, ones with fewer arguments, fill in the missing arguments and call that main version.

```clojure
(defn greet
  ([to-whom]
   (greet "Hello" to-whom))
  ([message to-whom]
   (println message to-whom)))
```

### Variadic functions

Variadic functions can take *any* number of arguments. They often use `&` symbol to break up the arguments. They are also called _varargs_.

```clojure
(defn first-argument [& args]
  (first args))
  
;; same!
(defn first-argument [x & args]
  x)
```

### Multimethods

Allow to have a single function with multiple implementations. Where multi-arity fns allow to pick the implementation based on the number of args, multimethods pick the implementation based on *any* characteristic of arguments.

Writing multimethods follows the recipe:

1. define the dispatch function - what are we differentiating between arguments - based on what are we going to handle them differently?
2. define the multimethod and the dispatch fn it uses
3. write the separate methods for each dispatch case


```clojure
;; 1. let's say we need to calculate royalties based on the copyright
(defn dispatch-published [book]
	(cond 
		(< (:published book) 1928) :public-domain
		(< (:published book) 1978) :old-copyright
		:else :new-copyright))
		
(defmulti compute-royalties dispatch-published)

(defmethod compute-royalties :public-domain [book]
	0)

(defmethod compute-royalties :old-copyright [book]
	;; compute royalties...
)

(defmethod compute-royalties :new-copyright [book]
	;; compute royalties...
)
```

### Recursion

To avoid blowing the stack, instead of calling the function name, call `recur`.

```clojure
(defn sum-copies-l [books]
  (loop [books books total 0]
    (if (empty? books)
      total
      (recur 
         (rest books)
         (+ total (:copies-sold (first books))))
    )))
```


### Pre and Post conditions

Useful to check arguments before continuing with the function
These accept a vector of expressions to evaluate. If any evaluates to false, the exception is raised. 

```clojure
(defn publish-book [book]
{:pre [(:title book)]}
  (print-book book)
  (ship-book book))
```

`:post` condition lets you check on the value returned from the function. E.g. to ensure that returned value is Boolean:

```clojure
(defn publish-book [book]
{:pre [(:title book)]
 :post [(boolean? %)]}
  (print-book book)
  (ship-book book))
```



Chapter 06. Functional Things
-----------------------------

Functions are values. Can be returned from other functions, can be passed to functions.

### `apply`

When I want to use a function on a collection of arguments.

E.g. concat a vector of strings:

```clojure

(def words ["A" " cheese " " is " " better " " than " " dirt!"])

(apply str words)
```

### `partial`

Constructs a fn with one or more of the parameters "baked in". It _partially_ fills in the arguments, producing a new fn.

```clojure
(def my-inc (partial + 1))
```

### `complement` 

Wraps a function supplied with a call to `not`. 

```clojure
(def not-number? (complement number?))
```

### `every-pred`

Combines predicate fns and *ands* them together.

```clojure
(def odd-number? (every-pred odd? number?))
```


### Function literal

It's the fn shorthand notation

```clojure
; a number doubler 
#(* % 2)
```

A style suggestion is not to use numbered literals, better use full anon fn notation. 


Chapter 7. Let
--------------

Calling `def` within a fn will make a value globally visible - its a big no-no. Might be useful for debugging purposes tho :)

So assigning values to symbols ("creating variables") in fn's local scope is what `let` does.

```clojure
(defn compute-discount-amount 
  [amount discount-percent min-charge]
  (let [discounted-amount (* amount (- 1.0 discount-percent))]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))
```


`if-let` will conditionally bind values.

Suppose we have these values:

```clojure
(def anon-book {:title "Sir Gawain and the Green Knight"})
(def with-author {:title "Once and Future King" :author "White"})
```

In the function which returns uppercase name of author, we need to check if the author key exists in the map, otherwise the code will blow up trying to uppercase `nil`.

```clojure
(defn uc-author [book]
(if-let [author (:author book)]
  (.toUpperCase author) ; then case
  "ANONYMOUS"           ; else case 
  ))
```

There is also a `when-let`.

```clojure
(defn uppercase-author [book]
  (when-let [author (:author book)]
  (.toUpperCase author)))
```


Chapter 8. Def, Symbols, and Vars
---------------------------------

`def` binds a symbol to a value.

Binding created by def exists until the program terminates or is overwritten. That makes is good for constants. 

```clojure
;; Everyone's favorite universal constant.
  (def PI 3.14)
```

When you evaluate a `def`, Clojure creates a *var* which holds the binding of a symbol to a value.

```
  +------------+------------+
  |     PI     |   3.14     |
  +------------+------------+
```

```clojure
(def author "Austen")

'author ; the symbol author, not it's value

#'author ; var for author
;;=>user/author

(def the-var #'author)

(.get the-var) ; get the value of the-var: "Austen"
(.-sym the-var); get the  symbol for the var: author
```

Changing the value of vars?

```clojure
(def debug-enabled false)

(defn debug [msg]
  (if debug-enabled
    (println msg)))
```

For situations like this, Clojure gives `binding`. Very similar to `let`. However, any var used in *binding* needs to be declared as dynamic. Also, the convention is that dynamic vars should start and end with \*. Clojurists refer to surrounding asterisks as _earmuffs_. So we can at a glance see which vars are usable in *binding*.

```clojure
(def ^:dynamic *debug-enabled* false)

(defn debug [msg]
  (if *debug-enabled*
    (println msg)))

(binding [*debug-enabled* true]
  (debug "Calling that darn function")
  (some-troublemaking-function-needing-logging)
  (debug "back from nasty function"))
```

Keep in mind that `let` does not create vars. 

*print-length* is dynamic var used in Clojure. We can make it different with using `set!`

```clojure
(def books "2001" "1984" "2010" "Tribe of the Cave Bear")
(set! *print-length* 2)
books  
;=> ["2001" "1984"...]
```

`*1` is the last result from REPL. *2 next to last, *3 one before. *e is the last exception in REPL.


Chapter 9. Namespaces
---------------------

*Vars* live in namespaces. Basically, a ns is a big lookup table of vars, indexed by their symbols.

`ns` creates a namespace and makes it a current namespace. I can also use it to switch between namespaces.

To get a symbol from another ns, issue a *fully qualified symbol*, one that includes the namespace.

When working with files (.clj), we need to make sure that the file is _loaded_ before trying to use the ns.

For example, let's use `diff` fn from `clojure.data`.

```clojure
(def literature ["Emma" "Oliver Twist" "Posession"])
(def horror ["It" "Posession" "Carrie"])

;; this would throw an exception
(clojure.data/diff literature horror)

;; because we haven't loaded the namespaced function, we need to require it 
(require 'clojure.data)
```

Filename has to correspond to the namespace:
`blottsbooks.core` => `src/blottsbooks/core.clj`. To create a new namespace/file, let's say `pricing` - within `src/blottsbooks/pricing.clj` and `(ns blottsbooks.pricing)`.

Note that dashes in the namespace are converted to underscores in the file system. E.g. `blotts-books.current-pricing` gets converted to `blotts_books/current_pricing.clj`. 

To fold the require in a `ns` declaration:

```clojure
(ns blottsbooks.core
(:require blottsbooks.pricing)
(:gen-class))
```

When using `require` stand-alone, it's a symbol, used as `(require)` and when used in `ns` it's `:require`, a keyword. In standalone version the argument must be 'quoted. In `ns` version it _must not_ be quoted.

 We can alias the required namespace with `:as`.

 `:refer` pulls the vars from another namespace. They can then be used without fully qualified name. However, since it can overwrite an existing fn, it should be used sparingly. 

 ```clojure
 (require '[blottsbooks.pricing :refer [discount-price]])
 ```
Current namespace is always bound to `*ns*`.

It's possible to look up any existing namespace with `find-ns`.

```clojure
(find-ns 'user)
```

Now you can get everything that ns knows about with `ns-map`

```clojure
(ns-map (find-ns 'user))
```

Keywords also have a room for a namespace, like `:blottsbooks.pricing/author`. And if we're in the namespace, then `::author` works too. Double colon evaluates to "this namespace".

Getting to other projects written in Clojure is done via Leiningen's project file, within `:dependencies`.


Chapter 10. Sequences
---------------------

To wrap a collection in a sequence:

```clojure
(def title-seq (seq ["Emma" "Oliver Twist" "Carrie"]))
```

Calling seq on a map will give a sequence of key/value pairs.

```clojure
(seq {:title "Emma", :author "Austen", :published 1815})
;=>([:title "Emma"] [:author "Austen"] [:published 1815])
```

Calling `seq` on an empty collection gives `nil`.

`first` gives the first element. `rest` gives everything after the first elem. `next` gives everything _but_ the first element. Difference between `next` and `rest` is that `rest` of an empty sequence is empty sequence. `next` of an empty sequence is `nil`.

You can also add a new element on the start of the sequence with `cons`.

`rest`, `next` and `cons` always return sequences. `sort` also takes a collection, sorts it and returns a sequence. `reverse` as well. 

`partition` takes a flat structure and chops it up in a sequence of smaller sequences.

```clojure
(def book-list ["2001" "Clarke" "Foundation" "Asimov"])
(partition 2 book-list)
```

`interleave` weaves two sequences together.

```clojure
(def titles ["Večernji akt" "Smogovci"])
(def authors ["Pavličić" "Hitrec"])
(interleave titles authors)
```

`interpose` sprinkles a separator between sequence elements.

```clojure
(def scary-animals ["tigers" "bears" "lions"])
(interpose "and" scary-animals)
```

Like `filter`, `some` takes a predicate fn and a collection. It returns the first truthy value from the predicate fn or `nil` if it doesn't find any. 


`map` takes a function and a collection. 

```clojure
(map #(count (:title %)) books)
```

We also have a `for` function.

```clojure
(for [b books]
  (count (:title b)))
```


`reduce` takes all the elements in the collection and combines them into one. Takes a fn and a sequence, calls the fn for each element of the sequence. Passes two arguments to the fn. Element of the collection and current result. 

### Other sources of sequences

`line-seq` turns contents of a file in a sequence


### Threading

`->>` will put the result as  the last argument, while `->` puts the result as the first argument.


Chapter 11. Lazy sequences
--------------------------

Suppose we want a bunch of nonsense text, for testing purposes...

```clojure
(def jack "All work and no play makes Jack a dull boy")
(def repeated-text (repeat jack))
```

`repeat` will return a lazy sequence. _Lazy sequence_ is the one which waits until asked before generating it's elements. An _unbounded sequence_ is a lazy sequence which, in theory, could go on forever.

A bit more interesting fn is `cycle`. It takes a collection and returns a sequence with the collection repeated over and over. 

```clojure
(take 7 (cycle [1 2 3]))
;=> (1 2 3 1 2 3 1)
```

`iterate` can generate even more interesting sequences. 

```clojure
(def numbers (iterate inc 1))
(nth numbers 101)
;=> 102
```

^ `numbers` in principle contains all positive integers.


`take` is also lazy. It won't actually evaluate values until asked for them. 

```clojure
(def many-nums (take 1000000000 (iterate inc 1)))
(take 20 many-nums)
```

So is `map`:

```clojure
(def evens (map #(* 2 %) (iterate inc 1)))
(take 20 evens)
;=>(2 4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40)
```


`lazy-seq` is similar to `seq`. It constructs a seq, but values won't be evaluated until called.

```clojure

(defn my-repeat [x]
(cons x (lazy-seq (my-repeat x)))
)


(defn my-iterate [f x]
(cons x (lazy-seq (my-iterate f (f x)))))
```


Let's say we want to get 10 chapters from files

```clojure
(def chapters (take 10 
  (map slurp 
    (map #(str "chap" % ".txt") numbers))))
```

However, this sets up a pipeline for reading, but hasn't read anything from the disk yet. For these situations, there's a `doall` function.

```clojure
(doall chapters)
```


`doseq` is similar to for, to evaluate all the elements of the lazy sequence, but doesn't hold on to the evaluated thing.

```clojure
(doseq [c chapters]
  (println "The chapter text: " c))
```

Be careful, calling `count`, `sort` or `reduce` over an infinite sequence is a bad idea.


Chapter 12. Destructuring
-------------------------


Let's pry open some data:

```clojure
(def authors [:monet :austen])

;; destructure in let
(let [[painter novelist] authors]
  (println "Painter is " painter)
  (println "Novelist is " novelist))
```

What if there's more elements and we want to skip some? The convention is to use the underscore. 

```clojure
(def artists [:monet :austen :beethoven :dickinson])

(let [[_ _ composer poet] artists]
  (println "The composer is " composer)
  (println "The poet is " poet))
```

Destructuring also works in function arguments. You just don't supply the value, it comes from the function call.

```clojure
(defn artist-description [[novelist poet]]
  (str "The novelist is " novelist " and the poet is " poet))

; call  
(artist-description [:austen :dickinson])
```

### Maps

Maps can also be destructured. 

```clojure
(def artist-map {
                 :painter :monet
                 :novelist :austen 
})

(let [{painter :painter writer :novelist} artist-map]
  (println painter "is a painter")
  (println writer "is a novelist"))

```

Multilevel map destructuring

```clojure
(def sith {
:name "Darth Vader"
:parents {:mother "Shmi" :father "The Force"}
})

(let [{{dad :father mom :mother} :parents} sith]
  (println "Vader's dad is" dad)
  (println "Vader's mom is" mom))
```

Mixing and matching

```clojure
(def authors [
{:name "Jane Austen" :born 1775}
{:name "Charles Dickens" :born 1812}
])

(let [[{dob-1 :born} {dob-2 :born}] authors]
(println "One author was born in" dob-1)
(println "Other author was born in" dob-2))
```

We can pull out keyword keys and assign symbols of same name. 

```clojure
(def character {:name "Romeo" :age 16 :gender "male"})

; if we want to bind :name to name, :age to age and :gender to gender, you can pull out :keys 

(defn character-desc [{:keys [name age gender]}]
  (str "Name:" name ", age:" age ", gender: " gender))
```


When destructuring fn arguments, it's still possible to get the entire map which was destructured. The following funtions are equivalent

```clojure
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
```

### In the wild example

```clojure
(defn mysql
  "Create a database specification for a
   mysql database. Opts should include
   keys for :db, :user, and :password.
   You can also optionally set host and port.
   Delimiters are automatically set to \"`\"."
    [{:keys [host port db make-pool?]
      :or {host "localhost", port 3306, db "", make-pool? true}
      :as opts}]
  
      ;; Do something with host, port, db, make-pool? and opts
  )
```


Chapter 13. Records and Protocols
---------------------------------

Records are like maps with predefined keys. Maps can incur a resource penalty in more intensive use cases.

```clojure
(defrecord FictionalCharacter [name appears-in author])

(def watson (->FictionalCharacter "John Watson" "Sign of the Four" "Doyle"))
```

Once created, I treat records just like a map `(:appears-in watson)`. Any fn which works with a map, will work with a record.

```clojure
(count elizabeth)
(keys watson)
```

Getting keys from a record is faster than from a map.

Even though it's possible to inspect the type of the record with `class` and `instance?`, these should not be used in the code. For those type-sensitive situations we have _protocols_


### Protocols

Enable to treat different records in a unified way. They are polymorphic and behave depending on the type of their first argument. 

Example from the Component library:

```clojure
(defprotocol Lifecycle
  (start [component]
    "Begins operation of this component. Synchronous, does not return
  until the component is started. Returns an updated version of this
  component.")
  (stop [component]
    "Ceases operation of this component. Synchronous, does not return
  until the component is stopped. Returns an updated version of this
  component."))
```

In cases I want to test the protocol, I can create a one-off implementation. To do that, Clojure provides a function `reify`. Not all methods from the protocol need to be implemented. 

```clojure
(def test-component (reify Lifecycle
                      (start [this]
                      (println "START") this)
                      (stop [this]
                      (println "STOP") this)))
```



Chapter 14. Tests
-----------------

Clojure comes with the library for writing unit tests - `clojure.test`. In a Clojure project the tests usually live in the `test` subdir. The convention is to put tests for a namespace in a parallel `-test` module.

```clojure
(deftest test-finding-books
  (is (not (nil? (i/find-by-title "Emma" books))))
  (is (nil? (i/find-by-title "XSDD" books))))
```


It's convenient to organize tests into subtests/contexts with `testing`. 

```clojure
(deftest test-basic-inventory
  (testing "Finding books")
    (is (not (nil? (i/find-by-title "Emma" books))))
    (is (nil? (i/find-by-title "XSDD" books)))
  (testing "Copies in inventory"
    (is (= 10 (i/number-of-copies-of "Emma" books))))
  )
```


Ways to run tests in a namespace:

```clojure
(test/run-tests)
(test/run-tests *ns*)
(test/run-tests 'inventory.core-test)
```

Or, even better, run tests from the command line with `lein test`


### Property-based testing

This enables us to state the property we want to test, along with the description of the input data for which that property should hold. 

See also [inventory example tests](inventory/test/inventory/core_gen_test.clj).

Clojure library which enables us to do this is [test.check](https://github.com/clojure/test.check). It provides a number of generators to create more or less random data for testing.

```clojure
(def title-gen gen/string-alphanumeric)
;; generates alnum strings
(def copies-gen gen/pos-int)
;; generates positive integers
```

Careful! These generators can also generate empty strings or zeros. To use only viable values, we can use `such-that` from the library.

```clojure
(def title-gen 
  (gen/such-that not-empty gen/string-alphanumeric))

(def copies-gen 
  (gen/such-that (complement zero?) gen/pos-int))
```

The test.check implementation of `hash-map` allow us to pass the generators and end up with an endless supply of maps with the keys associated with values from the generators. 

```clojure
(def book-gen
  (gen/hash-map :title title-gen :author author-gen :copies copies-gen))

;; now we can generate an endless supply of inventories
(def inventory-gen
  (gen/not-empty (gen/vector book-gen)))
```

We also need a book, as an element from an inventory along with the inventor which contains it.

```clojure
(def inventory-and-book-gen
  (gen/let [inventory inventory-gen
            book (gen/elements inventory)]
            {:inventory inventory :book book}))
```


Final part is expressing the property.

```clojure
;; given
;; (:require [clojure.test.check.properties :as prop])
;; (:require [clojure.test.check.generators :as gen])
;; this will check that each generated int is smaller than next positive int
(prop/for-all [i gen/pos-int]
  (< i (inc i)))
```

Well, but we need to limit the test to a certain number of tests. 

```clojure
;; given 
;; (:require [clojure.test.check :as tc])
(tc/quick-check 50
  (prop/for-all [i gen/pos-int]
  (< i (inc i))))
```

The check for the books is:

```clojure
(tc/quick-check 50
  (prop/for-all [i-and-b inventory-and-book-gen]
    (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
       (:book i-and-b))))
```

There is also clojure.test integration in the form of `defspec`, from `clojure.test.check.clojure-test` namespace. 

```clojure
(ctest/defspec find-by-title-finds-books 50
  (prop/for-all [i-and-b inventory-and-book-gen]
    (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
       (:book i-and-b))))
```

This gives us `clojure.test` test which runs the property test.


Chapter 15. Spec
----------------

[https://clojure.org/about/spec](https://clojure.org/about/spec)

Relatively new addition is `clojure.spec` which enables us to validate the shape of values - to ensure the proper data types.

```clojure
(ns inventory.core
(:require [clojure.spec.alpha :as s]))

;; key fn supplied by spec is "valid?"

(s/valid? number? 44) ;true
(s/valid? number? :hello) ;false
```

Using `spec/and` we can combine several checks, for example, to see if the value is a number and greater than 10

```clojure
(def n-gt-10 (s/and number? #(> % 10)))
```

There is also `spec/or`, but note how it's using keywords with checks, that is to supply coherent feedback when it fails.

```clojure
(def n-or-s (s/or :a-number number? :a-string string?))

(s/valid? n-or-s 10)
(s/valid? n-or-s "ha")

```

We can also spec collection to see if they are of certain shape.

```clojure
(def coll-of-strings (s/coll-of string?))

;; or a collection of numbers or strings
(def coll-of-n-or-s (s/coll-of n-or-s))
```

  `cat` helps us specify _this should follow that_. For example, if we wanted to match only four element collections of alternating strings and numbers:

```clojure
(def s-n-s-n (s/cat :s1 string? :n1 number? :s2 string? :n2 number?))

(s/valid? s-n-s-n ["Emma" 1815 "Jaws" 1974])
;; true
```

Using `keys` fn, we can also write specs for maps.

```clojure
(def book-s 
    (s/keys :req-un [:inventory.core/title
                     :inventory.core/author
                     :inventory.core/copies]))

```

This spec will match any map which has :title, :author and :copies.

### Registering specs

Using `clojure.spec/def` we can register a spec to be used globally, or JVM-wide registry of specs. 

```clojure
(s/def 
  :inventory.core/book
  (s/keys 
  :req-un
  [:inventory.core/title :inventory.core/author :inventory.core/copies]))
```

This registers our book under the kw :inventory.core/book, now we can use it as a spec

```clojure
(s/valid? :inventory.core/book {:title "Dracula" :author "Stoker" :copies 10})
```

Tightening up a spec:

```clojure
;; inventory.core assumed, hence ::
(s/def ::title string?)
(s/def ::author string?)
(s/def ::copies int?)

(s/def ::book (s/keys :req-un [::title ::author ::copies]))
```

To understand why a spec is failing, we can use `explain`, which takes same arguments as `valid`:

```clojure
(s/explain ::book {:author :austen :title :emma})
```

While `explain` prints what went wrong with a spec, `conform` will tell about the successful match.

```clojure
(s/conform s-n-s-n ["Emma" 1815 "Jaws" 1974])
```

### Function Specs

Automatically check the arguments of a function, by taking advantage of pre and post conditions.

```clojure
;; an inventory is a collection of books
(s/def :inventory.core/inventory (s/coll-of ::book))

(defn find-by-title 
  [title inventory]
  {:pre [(s/valid? ::title title)
         (s/valid? ::inventory inventory)]}
  (some #(when (= (:title %) title) %) inventory))
```

However, `clojure.spec` provides a fn to enable this, `spec/fdef`.

```clojure
;; Define the fn
(defn find-by-title
  [title inventory]
  (some #(when (= (:title %) title) %) inventory))

;; Register a spec 
  (s/fdef find-by-title 
    :args (s/cat :title ::title
           s/cat :inventory ::inventory))
```


Because of the performance penalty, the checking is disabled by default. For that reason, it's the best to use it only during testing and development. 

To enable argument checking, we need to require another namespace.

```clojure
(require '[clojure.spec.test.alpha :as st])
```

And explicitly instrument our fn 

```clojure
(st/instrument 'inventory.core/find-by-title)
```


### Spec driven testing

We can use `fdef` to drive `test.check` generative tests. 

```clojure
(defn book-blurb [book]
  (str "The best selling book " (:title book) " by " (:author book)))

(s/fdef book-blurb :args (s/cat :book ::book))
```

Now all we need is `check` function 

```clojure
(require '[clojure.spec.test.alpha :as stest])
(stest/check 'inventory.core/book-blurb)
```

Even though we're calling the fn with randomly generated data, we're not checking the return value.

```clojure
(s/fdef book-blurb
  :args (s/cat :book ::book)
  :ret (s/and string? (partial re-find #"The best selling"))
)
```

We can also use a fn

```clojure
(defn check-return [{:keys [args ret]}]
  (let [author (-> args :book :author)]
  (not (neg? (.indexOf ret author))))
)

(s/fdef book-blurb
  :args (s/cat :book ::book)
  :ret (s/and string? (partial re-find #"The best selling"))
  :fn check-return
)
```


Chapter 16. Interoperating with Java
------------------------------------

Java class method for getting a file:

```clojure
(def authors (java.io.File. "authors.txt"))
```

This creates a new instance of `java.io.File`, calling the constructor, and returns that instance. Now we can call methods on that instance. To check if the file exists:

```clojure
(if (.exists authors)
  (println "There is a file authors.txt")
  (println "No such file :'("))

(if (.canRead authors)
  (println "we can read it!"))

;; if we have appropriate permissions
(.setReadable authors true)
```

Accessing public fields of Java objects

```clojure
(def rect (java.awt.Rectangle. 0 0 10 20))

(println "Width " (.-width rect))
(println "Height: " (.-height rect))
```

To avoid using fully qualified Java package names, we can use `import` much in the same way as `require/:as`

```clojure
;; in the REPL 
(import java.io.File)

;; a the top of the file 
(ns read-authors 
  (:import java.io.File))

;; once imported, we can refer to  the class without its package name 

(def author (File. "authors.txt"))
```

Note that importing a single class doesn't need a quote, while importing multiple classes from the same package _does require quote_.

```clojure
;; in clj file 
(ns read-authors
  (:import (java.io File InputStream)))

;; in the REPL 
(import '(java.io File InputStream))
```

The package `java.lang` is automatically imported.


### Class Methods and Fields

Java classes themselves are objects of instance `java.lang.Class` with fields and methods of their own. These _static_ fields and methods are independent of any particular instance. 

```clojure
(import java.io.File)
File/separator
;=> "/"
```

Accessing static methods uses same syntax:


```clojure
;; create a temporary file in the standard temp dir 

(def temp-authors-file (File/createTempFile "authors_list" ".txt"))
```

### Methods are not functions

Keep in mind that `.method` and `class/staticMethod` even if looking like fns, aren't. So you can't bind them:

```clojure
;; nope 
(def count-method .count)
```

It is possible to use built in `memfn` to turn method name into a function. Eg. `.exists` is not a fn, but `(memfn exists)` is.

E.g. if we want to check a collection of files if they exist:

```clojure
(def files [(File. "authors.txt") (File. "titles.txt")])

(map (memfn exists) files)
```


Chapter 17. Threads, Promises, and Futures
------------------------------------------

The default thread we get by simply running a program is called _main thread_

```clojure
(ns blottsbooks.threads)
(defn -main []
  (println "Coming live from the main thread"))

;; make a thread 
(defn do-something-in-a-thread []
  (println "Hello from the thread")
  (println "Good bye from the thread"))

(def the-thread (Thread. do-something-in-a-thread))

;; and run it 
(.start the-thread)
```

Note that Java *Thread* class takes any object which implements the *Runnable* interface. Clojure fns implement that interface, so we can pass any Clojure fn.

```clojure
(defn do-something-else []
  (println "Hello from a new thread")
  (Thread/sleep 3000)
  (println "Good bye from this new thread!"))

(.start (Thread. do-something-else))
```

With threads, it's not possible to know which one will finish first, last or in any order - we simply don't know the speed of their execution. Threads are independent engines of execution. Beware of the *race conditions*.

### Promise 

How do we get a value from a thread? How do we know it's done?

We can use `join` which will pause and return only when the thread passed to it is finished. However, it always returns `nil`.

```clojure 
(def del-thread (Thread. #(.delete (java.io.File. "temp-titles.txt"))))
(.start del-thread)
(.join del-thread)
```

`promise` is like a value trap, once it catches a value, it snaps shut.

```clojure
(def the-result (promise))
;; putting the value in the promise
(deliver the-result "Emma")

;; get the value out 
(deref the-result)
;; or 
@the-result
```

If you try to deref or `@` a promise with no value, deref will pause until there is a value. We can run calculations in separate threads and communicate results back to our original, default thread.

```clojure
;; given a book inventory map ... 
(defn sum-copies-sold [inventory] 
  (apply + (map :sold inventory)))

(defn sum-revenue [inventory] 
  (apply + (map :revenue inventory)))

(let [copies-promise (promise)
      revenue-promise (promise)]
  (.start (Thread. #(deliver copies-promise 
    (sum-copies-sold inventory))))
  (.start (Thread. #(deliver revenue-promise 
    (sum-revenue inventory)))))

;; at some later time we can use values 
;; @copies-promise and @revenue-promise
```

### Futures

Since the pattern of firing off a computation in another thread and getting a result back in a promise is so common, Clojure provides a prepackaged version - a `future`, basically a promise with it's own thread.

```clojure
(def revenue-future 
    (future (apply + (map :revenue inventory))))

;; the value is also to be got with @deref 
```

### Thread pool

```clojure
(import java.util.concurrent.Executors)

;; a pool with at most three threads 
(def fixed-pool (Executors/newFixedThreadPool 3))

(.execute fixed-pool fn-1)
(.execute fixed-pool fn-2)
(.execute fixed-pool fn-3)
;; ...

```

The pool will queue work and perform it as the pool frees up.

### Gotchas

The dereferencing of a promise/future might take forever, so in real world it's wise to use a timeout. 

```clojure
;; wait half a second, if times out, return :oh-snap
    (deref revenue-promise 500 :oh-snap)
```

Also, be aware that JVM will refuse to stop if there are running threads.



Chapter 18. State 
-----------------

Since Clojure philosophy is to avoid mutable state, but state is needed, there are certain ways of dealing with state. 

### Atoms

When we need to hold a changing state:

```clojure
(def counter (atom 0))

(defn greeting-message [req]
  (swap! counter inc)
  (if (= @counter 500)
    (str "Congrats! You are the " @counter " visitor")
    (str "Welcome to Blotts Books!")))
```

`swap!` function takes the atom to change and a function to produce the next value of the atom. `swap!` also passes any additional arguments to the fn, so this also works:

```clojure
(swap! counter + 12)
```

`swap!` is also thread-safe.

Atoms can be wrapped around any Clojure value, and serve as containers for mutable values.

```clojure
(ns inventory)

(def by-title (atom {}))

(defn add-book [{title :title :as book}]
  (swap! by-title #(assoc % title book)))

(defn del-book [title]
  (swap! by-title #(dissoc % title)))

(defn find-book [title]
  (get @by-title title))
```

### Refs

When we want to update multiple values at the same time, we can use `refs`, which update values in transaction-like way.

```clojure
(ns inventory)

(def by-title (ref {}))
(def total-copies (ref 0))

(defn add-book [{title :title :as book}]
  (dosync
    (alter by-title #(assoc % title book))
    (alter total-copies + (:copies book))))
```

`alter` behaves like `swap!`, but the changes must happen within a `dosync` block. And these changes either all happen or none do.


### Agents

Since `swap!` can be called multiple times (e.g. if different threads change it), it's a bad place to cause side effects (sending notifications, for example). Same goes for `alter` and refs. For cases where we want side effects to happen in a consistent manner (where they would happen multiple times for `swap!` or `alter`), we use `agents`.

Agents use "update via function" pattern seen with refs and atoms. The update however happens async _off in another thread_. 

```clojure
(ns inventory)
(def by-title (agent {}))

(defn add-book [{title :title :as book}]
  (send
    by-title
    (fn [by-title-map]
      ;; side-effect causing fn 
      (notify-inventory-change :add book) 
      ;; update to agent via fn
      (assoc by-title-map title book))))
```

If the update to agent fails, it will raise an exception. We can check state of an agent with `agent-error` and `restart-agent`.

```clojure
;; create an agent
(def title-agent (agent "Pride and Prejudice"))

;; update value 
(send title-agent #(str % " and Zombies"))

(if (agent-error title-agent)
  (restart-agent
    title-agent
    "Poseidon Adventure"
    :clear-actions true))
```

Since agents rely on behind-the-scenes thread pool manageed by Clojure, and JVM can be finicky about terminating when there are live threads around - we can use `shutdown-agents1` to shutdown agents in the thread pool. 

It's always good idea to call `shutdown-agents` just before the end of the program that uses agents.

```clojure
(defn -main []
  ;; doing stuff with agents 
  (shutdown-agents)
)
```


### How to choose?

Between vars and atoms and refs and agents?

1. If the value is stable, with perhaps thread-wide variations -> `var`
2. If there's multiple values which _need_ to be updated together, but *don't* involve side-effects -> `refs`
3. If there are side-effects needing to happen when updating mutable state _or_ the update fn is slow -> `agent`
4. When we have mutable value, free of side-effects and no need to keep several values consistent -> `atom`


Chapter 19. Read and Eval
-------------------------

  Clojure uses the same syntax to represent code and data. `read` turns characters into data structures and then `eval` turns those into action.


Let's read a file full of Clojure code:

```clojure
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
```

This is how simple it is to write a toy REPL:

```clojure
(defn russ-repl []
  (loop []
    (println (eval (read)))
    (recur)))
```


### My own eval

Strings, keywords and numbers are simple since they evaluate to themselves. Other types can be delegated to their own functions.


```clojure
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
```

Check out [MAL (Make A Lisp) project](https://github.com/kanaka/mal). 


Chapter 20. Macros
------------------

To illustrat need for and use of macros, let's suppose we have a rating system which has three ratings - positive, neutral and negative, reprezented by a number which can be less than, equal to or greater than zero.

To avoid repeating checks for tratings, we could use a reusable construct. Similar construct was in Fortran, and names `arithmetic-if`.

A naive approach would be

```clojure
(defn arithmetic-if [n pos zero neg]
  (cond 
    (pos? n) pos
    (zero? n) zero
    (neg? n) neg))

;; this would indeed return :meh 
(arithmetic-if 0 :great :meh :boring)

;; however, this would evaluate each println:

(defn print-rating [rating]
  (arithmetic-if rating 
    (println "Good book!")
    (println "Totally indifferent.")
    (println "Run away!")))
```

The macro which does this

```clojure
(defmacro arithmetic-if [n pos zero neg]
  (list 'cond (list 'pos? n) pos
              (list 'zero? n) zero 
              :else neg))
```

So now we can know that this 

```clojure
(arithmetic-if rating :loved-it :meh :hated-it)
```

Will get turned into this

```clojure
(cond 
  (pos? rating) :loved-it
  (zero? n) :meh 
  :else :hated-it)
```

### Syntax quoting

To make writing code macros easier Clojure gives us the code templating system of syntax quoting. To set off the syntax quoted expressions, use backtick `.

Syntax quoting uses tilde character ~ to mark the places where values should get inserted.

```clojure
;; set up some values 
(def n 100)
(def pos "It's positive!")
(def zero "It's a zero!")
(def neg "It's negative!")

;; plug them in cond
`(cond 
  (pos? ~n) ~pos
  (zero? ~n) ~zero
  :else ~neg)


;; run this and we get 
(cond 
  (pos? 100) "It's positive!"
  (zero? 100) "It's a zero!"
  :else "It's negative!")

;; or actually, to avoid ns confusion 
(clojure.core/cond 
  (clojure.core/pos? 100) "It's positive!"
  (clojure.core/zero? 100) "It's a zero!"
  :else "It's negative!")
```

Compare the syntax quoted version with original macro:

```clojure
(defmacro arithmetic-if [n pos zero neg]
  `(cond 
    (pos? ~n) ~pos 
    (zero? ~n) ~zero
    :else ~neg))


;; without syntax quoting 
(defmacro arithmetic-if [n pos zero neg]
  (list 'cond (list 'pos? n) pos
              (list 'zero? n) zero 
              :else neg))
```

### Debugging macros

Rembember that macros get themselves felt two times - the first time when they generate the code, and the second time when the code they have generated runs.

When you run into trouble with macros, use the `macroexpand-11` fn. For example: 

```clojure
(macroexpand-1 '(arithmetic-if 100 :pos :zero :neg))
;; outputs
(cond (pos? 100) :pos (zero? 100) :zero :else :neg)
```