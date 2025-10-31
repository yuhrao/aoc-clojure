(ns aoc-clojure.2015.day-2
  (:require
   [clojure.string :as str]
   [aoc-clojure.utils :as utils]))

(defn- calc-sides-area
  "Calculate the area of all three sides of a rectangular prism.
   
   Takes a vector [length width height] and returns a vector of the three side areas."
  [[l w h]]
  [(* l w)
   (* w h)
   (* h l)])

(defn calc-total-area
  "Calculate the total wrapping paper needed for a present.
   
   Takes a vector [length width height] and returns the total area needed,
   which is 2*l*w + 2*w*h + 2*h*l plus the area of the smallest side."
  [[l w h]]
  (let [sides-area (calc-sides-area [l w h])
        min-side-area (apply min sides-area)
        prism-area (->> sides-area
                        (map (partial * 2))
                        (apply +))]
    (+ prism-area min-side-area)))

(defn calc-ribbon-base-length
  "Calculate the ribbon needed to wrap around the smallest perimeter.
   
   Takes a vector of measurements [length width height] and returns the
   perimeter of the smallest side."
  [measurements]
  (let [sorted-measurements (sort measurements)
        shortest-side (first sorted-measurements)
        second-shortest-side (second sorted-measurements)]
    (+ shortest-side
       shortest-side
       second-shortest-side
       second-shortest-side)))

(defn calc-prism-volume
  "Calculate the volume of a rectangular prism.
   
   Takes a vector of measurements and returns their product."
  [measurements]
  (apply * measurements))

(defn calc-ribbon-length
  "Calculate the total ribbon needed for a present.
   
   Takes a vector of measurements [length width height] and returns the total
   ribbon needed, which is the smallest perimeter plus the volume for the bow."
  [measurements]
  (let [base-length (calc-ribbon-base-length measurements)
        volume (calc-prism-volume measurements)]
    (+ base-length volume)))

(defn parse-input
  "Parse the input string into a list of dimension vectors.
   
   Takes a string with dimensions in format 'LxWxH' (one per line) and returns
   a sequence of [L W H] integer vectors."
  [input]
  (->> input
       str/split-lines
       (map #(->> (str/split % #"x")
                  (map Integer/parseInt)))))

(defn part-1
  "Calculate how many square feet of wrapping paper the elves need.
   
   Takes the input string and returns the total square feet of wrapping paper."
  [input]
  (->> input
       parse-input
       (map calc-total-area)
       (apply +)))

(defn part-2
  "Calculate how many feet of ribbon the elves need.
   
   Takes the input string and returns the total feet of ribbon."
  [input]
  (->> input
       parse-input
       (map calc-ribbon-length)
       (apply +)))

(defn execute
  "Execute both parts of day 2 puzzle."
  []
  (utils/execute-day {:year 2015 :day 2 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
