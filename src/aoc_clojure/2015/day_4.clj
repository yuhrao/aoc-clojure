(ns aoc-clojure.2015.day-4
  (:require [clojure.string :as str]
            [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]]
            [aoc-clojure.utils :as utils]))

(defn str->md5-str
  "Convert a string to its MD5 hash representation.
   
   Takes a string and returns its MD5 hash as a hexadecimal string."
  [val]
  (-> val
      hash/md5
      bytes->hex))

(defn mine
  "Find the lowest positive number that produces an MD5 hash with a specified number of leading zeros.
   
   Takes a count of leading zeros and a secret key. Returns the lowest positive integer
   that when appended to the secret key produces an MD5 hash starting with that many zeros."
  [leading-zeroz-count secret]
  (loop [current-val 1]
    (if (str/starts-with? (str->md5-str (str secret current-val)) (apply str (repeat leading-zeroz-count "0")))
      current-val
      (recur (inc current-val)))))

(defn part-1
  "Mine AdventCoins for Santa by finding a hash with 5 leading zeros.
   
   Takes a secret key and returns the lowest number that produces a valid hash."
  [input]
  (mine 5 input))

(defn part-2
  "Mine AdventCoins for Santa by finding a hash with 6 leading zeros.
   
   Takes a secret key and returns the lowest number that produces a valid hash."
  [input]
  (mine 6 input))

(defn execute
  "Execute both parts of day 4 puzzle."
  []
  (utils/execute-day {:year 2015 :day 4 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
