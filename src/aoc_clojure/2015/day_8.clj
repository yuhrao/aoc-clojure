(ns aoc-clojure.2015.day-8
  (:require
   [aoc-clojure.registry.core :as registry]
   [clojure.string :as str]))

(defn in-memory-count [s]
  (-> s
      (subs 1 (dec (count s))) ; remove outer quotes
      (clojure.string/replace
       #"\\\\|\\\"|\\x[0-9a-fA-F]{2}"
       (fn [m]
         (cond
           (= m "\\\\") "\\"
           (= m "\\\"") "\""
           :else (str (char (Integer/parseInt (subs m 2) 16))))))
      count))

(defn encoded-count
  [s]
  (-> s
      (str/replace "\\" "\\\\") ; escape backslashes first
      (str/replace "\"" "\\\"") ; then escape quotes
      (#(str "\"" % "\"")) ; wrap in quotes
      count))

(defn part-1
  [input]
  (->> (str/split-lines input)
       (mapcat (juxt count (comp (partial * -1) in-memory-count)))
       (apply +)))

(defn part-2
  [input]
  (if (empty? input)
    0
    (->> (str/split-lines input)
         (mapcat (juxt encoded-count (comp (partial * -1) count)))
         (reduce +))))

(registry/register {:year 2015 :day 8 :part-1 part-1 :part-2 part-2})

(defn execute []
  (registry/execute-day {:year 2015 :day 8}))

(comment
  (execute))
