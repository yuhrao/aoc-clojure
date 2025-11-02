(ns aoc-clojure.2015.day-4
  (:require [clojure.string :as str]
            [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]]
            [aoc-clojure.registry.core :as registry]))

(defn str->md5-str
  [val]
  (-> val
      hash/md5
      bytes->hex))

(defn mine
  [leading-zeroz-count secret]
  (loop [current-val 1]
    (if (str/starts-with? (str->md5-str (str secret current-val)) (apply str (repeat leading-zeroz-count "0")))
      current-val
      (recur (inc current-val)))))

(defn part-1
  [input]
  (mine 5 input))

(defn part-2
  [input]
  (mine 6 input))

(registry/register {:year 2015 :day 4 :part-1 part-1 :part-2 part-2})

(defn execute
  []
  (registry/execute-day {:year 2015 :day 4}))

(comment

  (execute))
