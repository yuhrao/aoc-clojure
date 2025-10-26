(ns aoc-clojure.2015.day-1-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-1 :as sut]))

(t/deftest should-correctly-calculate-resulting-loor
  (t/are [input expected]
         (t/is (= (sut/part-1 input) expected))
    "(())" 0
    "()()" 0
    "(((" 3
    "(()(()(" 3
    "))(((((" 3
    "())" -1
    "))(" -1
    ")))" -3
    ")())())" -3))

(t/deftest should-correctly-calculate-position-of-first-basement
  (t/are [input expected]
         (t/is (= (sut/part-2 input) expected))
    ")" 1
    "()())" 5))
