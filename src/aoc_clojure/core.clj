(ns aoc-clojure.core
  (:require [malli.instrument :as mi])
  (:gen-class))

(mi/instrument!)

(defn -main
  "Main entry point for the application.
   
   Currently prints 'Hello, World!' but can be extended to run
   specific Advent of Code solutions."
  [& args]
  (println "Hello, World!"))
