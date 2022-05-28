(ns c17)
;; Chapter 17. Threads, Promises, and Futures

;; main thread
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


;; Clojure fns implement Runnable interface, so we can pass them to Java Thread
(defn do-something-else []
  (println "Hello from a new thread")
  (Thread/sleep 3000)
  (println "Good bye from this new thread!"))

(.start (Thread. do-something-else))

(def del-thread (Thread. #(.delete (java.io.File. "temp-titles.txt"))))
(.start del-thread)
(.join del-thread)

;; promise
(def the-result (promise))
;; putting the value in the promise
(deliver the-result "Emma")

;; get the value out 
(deref the-result)
;; or 
@the-result


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

;; Futures
(def revenue-future 
    (future (apply + (map :revenue inventory))))

;; the value is also to be got with @deref 

;; Thread pool
(import java.util.concurrent.Executors)

;; a pool with at most three threads 
(def fixed-pool (Executors/newFixedThreadPool 3))

(.execute fixed-pool fn-1)
(.execute fixed-pool fn-2)
(.execute fixed-pool fn-3)
;; ...


;; wait half a second, if times out, return :oh-snap
(deref revenue-promise 500 :oh-snap)
