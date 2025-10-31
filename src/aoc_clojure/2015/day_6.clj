(ns aoc-clojure.2015.day-6
  (:require
   [aoc-clojure.utils :as utils]
   [clojure.string :as str]
   [clojure.set :as set]))

(defn apply-operation
  "Apply a lighting operation to a grid of lights.
   
   Takes a set of currently lit light coordinates, an operation string ('turn on',
   'turn off', or 'toggle'), and start/end coordinates [x y]. Returns the updated
   set of lit lights after applying the operation to all lights in the range."
  [lit-lights op [start-x start-y] [end-x end-y]]
  (let [ranges-to-apply (set
                         (for [x (range start-x (inc end-x))
                               y (range start-y (inc end-y))]
                           [x y]))]

    (case op
      "turn on" (apply conj lit-lights ranges-to-apply)
      "turn off" (apply disj lit-lights ranges-to-apply)
      "toggle" (let [lit-lights-out-of-range (set/difference lit-lights ranges-to-apply)
                     toggled-in-range (set/difference ranges-to-apply lit-lights)]
                 (apply conj toggled-in-range lit-lights-out-of-range)))))

(defn parse-raw-operation
  "Parse a raw operation string into its components.
   
   Takes a string like 'turn on 0,0 through 999,999' and returns a vector of
   [operation start-coordinate end-coordinate] where coordinates are [x y] vectors."
  [row]
  (let [[rest end] (str/split row #" through ")
        [raw-op start-position-or-rest-op start-position] (str/split rest #" ")
        start (or start-position start-position-or-rest-op)
        op (if start-position
             (str raw-op " " start-position-or-rest-op)
             raw-op)
        start-coordinate (->> (str/split start #",")
                              (map Integer/parseInt)
                              vec)
        end-coordinate (->> (str/split end #",")
                            (map Integer/parseInt)
                            vec)]
    [op start-coordinate end-coordinate]))

(def ^:private brightness-per-op {"turn on" 1
                                  "turn off" -1
                                  "toggle" 2})

(defn calculate-brightness
  "Calculate brightness changes for a grid of lights.
   
   Takes a brightness map (coordinates to brightness level), an operation string,
   and start/end coordinates. Returns the updated brightness map after applying
   the operation (turn on +1, turn off -1 with min 0, toggle +2) to all lights
   in the range."
  [brightness-map op [start-x start-y] [end-x end-y]]
  (let [op-brightness (brightness-per-op op)
        ranges-to-apply (for [x (range start-x (inc end-x))
                              y (range start-y (inc end-y))]
                          [x y])]
    (reduce (fn [curr-brightness curr-pos]
              (update curr-brightness curr-pos #(max
                                                 (+ op-brightness (or % 0))
                                                 0))) brightness-map ranges-to-apply)))

(defn parse-input
  "Parse input string into a sequence of operation vectors.
   
   Takes input with one operation per line and returns a sequence of parsed operations."
  [input]
  (->> input
       str/split-lines
       (pmap parse-raw-operation)))

(defn part-1
  "Count how many lights are lit after following all instructions.
   
   Lights are either on or off. Takes input instructions and returns the count
   of lights that are on."
  [input]
  (let [initial-lit-lights #{}]
    (->> input
         parse-input
         (reduce (fn [lit-lights [op start-coord end-coord]]
                   (apply-operation lit-lights op start-coord end-coord))
                 initial-lit-lights)
         count)))

(defn part-2
  "Calculate total brightness after following all instructions.
   
   Lights have variable brightness levels. Takes input instructions and returns
   the sum of all brightness values."
  [input]
  (let [initial-brightness {}]
    (->> input
         parse-input
         (reduce (fn [brightness-balance [op start-coord end-coord]]
                   (calculate-brightness brightness-balance op start-coord end-coord))
                 initial-brightness)
         vals
         (apply +))))

(defn execute
  "Execute both parts of day 6 puzzle."
  []
  (utils/execute-day {:year 2015 :day 6 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
