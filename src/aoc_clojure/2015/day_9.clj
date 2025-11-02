(ns aoc-clojure.2015.day-9
  (:require
   [aoc-clojure.utils :as utils]
   [clojure.string :as str]))

(defn parse-line
  [line]
  (let [[_ from to distance] (re-matches #"(\w+) to (\w+) = (\d+)" line)]
    [[from to] (Integer/parseInt distance)]))

(defn build-graph
  [lines]
  (reduce (fn [graph line]
            (let [[[from to] distance] (parse-line line)]
              (-> graph
                  (assoc-in [from to] distance)
                  (assoc-in [to from] distance))))
          {}
          lines))

(defn get-all-cities
  [graph]
  (set (mapcat keys (vals graph))))

(defn calculate-route-distance
  [graph route]
  (reduce + (map (fn [[from to]]
                   (get-in graph [from to]))
                 (partition 2 1 route))))

(defn all-permutations
  [coll]
  (if (= 1 (count coll))
    [(vec coll)]
    (for [x coll
          xs (all-permutations (remove #{x} coll))]
      (cons x xs))))

(defn find-optimal-route
  [graph optimization-fn]
  (let [cities (get-all-cities graph)
        all-routes (all-permutations cities)
        route-distances (map (fn [route]
                               [(vec route) (calculate-route-distance graph route)])
                             all-routes)
        optimal-route (apply optimization-fn second route-distances)]
    (second optimal-route)))

(defn part-1
  "Find the shortest route visiting all cities exactly once"
  [input]
  (if (empty? input)
    0
    (let [lines (str/split-lines input)
          graph (build-graph lines)]
      (find-optimal-route graph (partial min-key)))))

(defn part-2
  "Find the longest route visiting all cities exactly once"
  [input]
  (if (empty? input)
    0
    (let [lines (str/split-lines input)
          graph (build-graph lines)]
      (find-optimal-route graph (partial max-key)))))

(defn execute []
  (utils/execute-day {:year 2015 :day 9 :part-1 part-1 :part-2 part-2}))

(comment
  (execute))
