(ns aoc-clojure.2015.day-3
  (:require
   [clojure.set :as set]
   [aoc-clojure.utils :as utils]))

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
  "Execute a sequence of movements and track all visited positions.
   
   Takes a movement map (character to movement function) and a sequence of movement
   characters. Returns a set of all [x y] coordinates visited, including the starting
   position [0 0]."
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
  "Find how many houses receive at least one present with only Santa delivering.
   
   Takes a string of directions (^v<>) and returns the count of unique houses visited."
  [input]
  (count (execute-navigation santa-move-fns input)))

(defn part-2
  "Find how many houses receive at least one present with Santa and Robo-Santa delivering.
   
   Takes a string of directions where Santa and Robo-Santa alternate turns (Santa takes
   odd-indexed moves, Robo-Santa takes even-indexed moves). Returns the count of unique
   houses visited by either."
  [input]
  (let [inputs-per-actor (partition 2 input)
        santa-moves (map first inputs-per-actor)
        robot-santa-moves (map second inputs-per-actor)
        santa-visits (future (execute-navigation santa-move-fns santa-moves))
        robot-santa-visits (future (execute-navigation robot-santa-move-fns robot-santa-moves))]
    (count (set/union @santa-visits @robot-santa-visits))))

(defn execute
  "Execute both parts of day 3 puzzle."
  []
  (utils/execute-day {:year 2015 :day 3 :part-1 part-1 :part-2 part-2}))

(comment

  (execute))
