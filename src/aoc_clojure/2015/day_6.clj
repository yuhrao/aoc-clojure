(ns aoc-clojure.2015.day-6
  (:require
   [aoc-clojure.utils :as utils]
   [clojure.string :as str]
   [clojure.set :as set]))

(defn apply-operation
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
  [input]
  (->> input
       str/split-lines
       (pmap parse-raw-operation)))

(defn part-1
  [input]
  (let [initial-lit-lights #{}]
    (->> input
         parse-input
         (reduce (fn [lit-lights [op start-coord end-coord]]
                   (apply-operation lit-lights op start-coord end-coord))
                 initial-lit-lights)
         count)))

(defn part-2
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
  []
  (utils/execute-day {:year 2015 :day 6 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
