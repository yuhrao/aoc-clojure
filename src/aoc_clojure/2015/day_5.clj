(ns aoc-clojure.2015.day-5
  (:require
   [aoc-clojure.utils :as utils]
   [clojure.string :as str]))

(def ^:private nice-string-v1-re #"^(?=(?:.*[aeiou]){3})(?=.*(.)\1)(?!.*(?:ab|cd|pq|xy)).*$")

(defn nice-string-v1?
  "- It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
   - It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
   - It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements."
  [s]
  (boolean (re-matches nice-string-v1-re s)))

(def ^:private nice-string-v2-re #"^(?=.*(..).*\1)(?=.*(.).\2).*$")

(defn nice-string-v2?
  "- It contains a pair of any two letters that appears at least twice in the string without overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
   - It contains at least one letter which repeats with exactly one letter between them, like xyx, abcdefeghi (efe), or even aaa."
  [s]
  (boolean (re-matches nice-string-v2-re s)))

(defn part-1
  "Mining AdventCoins for santa"
  [input]
  (->> input
       str/split-lines
       (pmap nice-string-v1?)
       (filter true?)
       count))

(defn part-2
  "Calculate total ribbon feets are needed for elves"
  [input]
  (->> input
       str/split-lines
       (pmap nice-string-v2?)
       (filter true?)
       count))

(defn execute []
  (utils/execute-day {:year 2015 :day 5 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
