(ns aoc-clojure.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- read-input-file [path]
  (-> path
      io/resource
      slurp
      str/trim))

(defn execute-day [{:keys [year day part-1 part-2]}]
  (let [input-path (format "%d/input-day-%d.txt" year day)
        input (read-input-file input-path)
        result-part-1 (future (part-1 input))
        result-part-2 (future (part-2 input))]
    (println "Advent of Code" year "- Day" day)
    (println "Part 1 result:" @result-part-1)
    (println "Part 2 result:" @result-part-2)
    {:part-1 @result-part-1 :part-2 @result-part-2}))
