(ns aoc-clojure.2015.day-2
  (:require
   [clojure.string :as str]
   [aoc-clojure.utils :as utils]))

(defn- calc-sides-area
  "Calculate the area of all three sides of a rectangular prism."
  [[l w h]]
  [(* l w)
   (* w h)
   (* h l)])

(defn calc-total-area
  "Calculate the total wrapping paper needed for a present."
  [[l w h]]
  (let [sides-area (calc-sides-area [l w h])
        min-side-area (apply min sides-area)
        prism-area (->> sides-area
                        (map (partial * 2))
                        (apply +))]
    (+ prism-area min-side-area)))

(defn calc-ribbon-base-length
  [measurements]
  (let [sorted-measurements (sort measurements)
        shortest-side (first sorted-measurements)
        second-shortest-side (second sorted-measurements)]
    (+ shortest-side
       shortest-side
       second-shortest-side
       second-shortest-side)))

(defn calc-prism-volume
  [measurements]
  (apply * measurements))

(defn calc-ribbon-length
  [measurements]
  (let [base-length (calc-ribbon-base-length measurements)
        volume (calc-prism-volume measurements)]
    (+ base-length volume)))

(defn parse-input
  [input]
  (->> input
       str/split-lines
       (map #(->> (str/split % #"x")
                  (map Integer/parseInt)))))

(defn part-1
  [input]
  (->> input
       parse-input
       (map calc-total-area)
       (apply +)))

(defn part-2
  [input]
  (->> input
       parse-input
       (map calc-ribbon-length)
       (apply +)))

(defn execute
  []
  (utils/execute-day {:year 2015 :day 2 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
