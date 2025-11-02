(ns aoc-clojure.registry.core
  (:require [malli.core :as malli]
            [malli.experimental :as mx]
            [aoc-clojure.utils :as utils]))

(defonce ^:private aoc-registry (atom {}))

(def RegisterParams (malli/schema [:map
                                   [:year :int]
                                   [:day :int]
                                   [:description {:optional true} :string]
                                   [:part-1 [:=> [:cat :string] any?]]
                                   [:part-2 [:=> [:cat :string] any?]]]))

(mx/defn register [params :- RegisterParams]
  (let [{:keys [year day description part-1 part-2]} params
        input (delay (utils/read-aoc-input-file year day))]
    (swap! aoc-registry assoc-in [year day] {:description description
                                             :part-1 part-1
                                             :part-2 part-2
                                             :input input})
    nil))

(defn execute-day
  "Execute both parts of an Advent of Code day puzzle.
   
   Takes a map with :year, :day, :part-1, and :part-2 keys where the part functions
   take the input string and return the solution. Runs both parts concurrently and
   prints the results. Returns a map with :part-1 and :part-2 result values."
  [{:keys [year day]}]
  (if-let [exercise (get-in @aoc-registry [year day])]
    (let [{:keys [part-1 part-2]} exercise
          input (-> exercise :input deref)
          result-part-1 (future (part-1 input))
          result-part-2 (future (part-2 input))]
      (println "Advent of Code" year "- Day" day)
      (println "Part 1 result:" @result-part-1)
      (println "Part 2 result:" @result-part-2)
      {:part-1 @result-part-1 :part-2 @result-part-2})
    (throw (ex-info "AOC day not found"
                    {:day day
                     :year year}))))
