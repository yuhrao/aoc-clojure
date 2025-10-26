(ns aoc-clojure.2015.day-1
  (:require
   [aoc-clojure.utils :as utils]))

(def ^:private directions {\( inc
                           \) dec})

(defn part-1 [input]
  (reduce (fn [val char]
            (let [dir-fn (directions char)]
              (dir-fn val)))
          0
          input))

(defn part-2 [input]
  (loop [char-seq input
         curr-floor 0
         char-floor 1]
    (let [curr-char (first char-seq)
          char-fn (directions curr-char)
          next-floor (char-fn curr-floor)]
      (if (neg? next-floor)
        char-floor
        (recur (rest char-seq)
               next-floor
               (inc char-floor))))))

(defn execute []
  (utils/execute-day {:year 2015 :day 1 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
