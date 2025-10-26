(ns aoc-clojure.2015.day-4-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-4 :as sut]))

(t/deftest mine-adventcoins
  (t/are [input expected]
         (t/is (= (sut/mine 5 input) expected))
    "abcdef" 609043
    "pqrstuv" 1048970))
