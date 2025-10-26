(ns aoc-clojure.2015.day-3-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-3 :as sut]))

(t/deftest santa-visited-nodes-count
  (t/are [input expected]
         (t/is (= (sut/part-1 input) expected))
    ">" 2
    "^>v<" 4
    "^v^v^v^v^v" 2))

(t/deftest multi-actors-visited-nodes-count
  (t/are [input expected]
         (t/is (= (sut/part-2 input) expected))
    "^v" 3
    "^>v<" 3
    "^v^v^v^v^v" 11))
