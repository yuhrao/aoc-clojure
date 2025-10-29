(ns aoc-clojure.2015.day-5
  (:require
   [aoc-clojure.utils :as utils]
   [clojure.string :as str]))

(def ^:private nice-string-v1-re #"^(?=(?:.*[aeiou]){3})(?=.*(.)\1)(?!.*(?:ab|cd|pq|xy)).*$")

(defn nice-string-v1?
  "Check if a string is 'nice' according to version 1 rules.
   
   A string is nice if:
   - It contains at least three vowels (aeiou only)
   - It contains at least one letter that appears twice in a row
   - It does not contain the strings ab, cd, pq, or xy"
  [s]
  (boolean (re-matches nice-string-v1-re s)))

(def ^:private nice-string-v2-re #"^(?=.*(..).*\1)(?=.*(.).\2).*$")

(defn nice-string-v2?
  "Check if a string is 'nice' according to version 2 rules.
   
   A string is nice if:
   - It contains a pair of any two letters that appears at least twice without overlapping
   - It contains at least one letter which repeats with exactly one letter between them"
  [s]
  (boolean (re-matches nice-string-v2-re s)))

(defn part-1
  [input]
  (->> input
       str/split-lines
       (pmap nice-string-v1?)
       (filter true?)
       count))

(defn part-2
  [input]
  (->> input
       str/split-lines
       (pmap nice-string-v2?)
       (filter true?)
       count))

(defn execute
  "Execute both parts of day 5 puzzle."
  []
  (utils/execute-day {:year 2015 :day 5 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
