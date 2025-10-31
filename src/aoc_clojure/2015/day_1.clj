(ns aoc-clojure.2015.day-1
  (:require
   [aoc-clojure.utils :as utils]))

(def ^:private directions {\( inc
                           \) dec})

(defn part-1
  "Calculate the floor Santa ends up on.
   
   Takes a string of parentheses where '(' means go up one floor
   and ')' means go down one floor. Returns the final floor number."
  [input]
  (reduce (fn [val char]
            (let [dir-fn (directions char)]
              (dir-fn val)))
          0
          input))

(defn part-2
  "Find the position of the first character that causes Santa to enter the basement (floor -1).
   
   Takes a string of parentheses and returns the 1-based position of the character
   that first causes the floor to become negative."
  [input]
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

(defn execute
  "Execute both parts of day 1 puzzle."
  []
  (utils/execute-day {:year 2015 :day 1 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
