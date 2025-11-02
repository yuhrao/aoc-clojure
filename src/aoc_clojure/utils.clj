(ns aoc-clojure.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- read-file
  "Read and parse an input file from the resources directory.
   
   Takes a resource path and returns the trimmed file contents as a string."
  [path]
  (-> path
      io/resource
      slurp
      str/trim))

(defn read-aoc-input-file [year day]
  (read-file (format "%d/input-day-%d.txt" year day)))

(defn execute-day
  "Execute both parts of an Advent of Code day puzzle.
   
   Takes a map with :year, :day, :part-1, and :part-2 keys where the part functions
   take the input string and return the solution. Runs both parts concurrently and
   prints the results. Returns a map with :part-1 and :part-2 result values."
  [{:keys [year day part-1 part-2]}]
  (let [input (read-aoc-input-file year day)
        result-part-1 (future (part-1 input))
        result-part-2 (future (part-2 input))]
    (println "Advent of Code" year "- Day" day)
    (println "Part 1 result:" @result-part-1)
    (println "Part 2 result:" @result-part-2)
    {:part-1 @result-part-1 :part-2 @result-part-2}))
