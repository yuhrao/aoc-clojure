(ns aoc-clojure.2015.day-3
  (:require
   [clojure.set :as set]
   [aoc-clojure.registry.core :as registry]))

(def ^:private santa-move-fns {\> (fn [[x y]]
                                    [(inc x) y])
                               \< (fn [[x y]]
                                    [(dec x) y])
                               \^ (fn [[x y]]
                                    [x (inc y)])
                               \v (fn [[x y]]
                                    [x (dec y)])})

(def ^:private robot-santa-move-fns {\> (fn [[x y]]
                                          [(inc x) y])
                                     \< (fn [[x y]]
                                          [(dec x) y])
                                     \^ (fn [[x y]]
                                          [x (inc y)])
                                     \v (fn [[x y]]
                                          [x (dec y)])})

(defn execute-navigation
  [movement-map movements]
  (loop [movement-seq (seq movements)
         visited-nodes #{[0 0]}
         current-pos [0 0]]
    (if (empty? movement-seq)
      visited-nodes
      (let [movement (first movement-seq)
            move-fn (movement-map movement)
            next-position (move-fn current-pos)]
        (recur (rest movement-seq)
               (conj visited-nodes next-position)
               next-position)))))

(defn part-1
  [input]
  (count (execute-navigation santa-move-fns input)))

(defn part-2
  [input]
  (let [inputs-per-actor (partition 2 input)
        santa-moves (map first inputs-per-actor)
        robot-santa-moves (map second inputs-per-actor)
        santa-visits (future (execute-navigation santa-move-fns santa-moves))
        robot-santa-visits (future (execute-navigation robot-santa-move-fns robot-santa-moves))]
    (count (set/union @santa-visits @robot-santa-visits))))

(registry/register {:year 2015 :day 3 :part-1 part-1 :part-2 part-2})

(defn execute
  []
  (registry/execute-day {:year 2015 :day 3}))

(comment

  (execute))
