(ns aoc-clojure.2015.day-5-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-5 :as sut]))

(t/deftest nice-string-v1?
  (t/are [input expected]
         (t/is (= (sut/nice-string-v1? input) expected))
    "ugknbfddgicrmopn" true
    "aaa" true
    "jchzalrnumimnmhp" false
    "haegwjzuvuyypxyu" false
    "dvszwmarrgswjxmb" false))

(t/deftest nice-string-v2?
  (t/are [input expected]
         (t/is (= (sut/nice-string-v2? input) expected))
    "qjhvhtzxzqqjkmpb" true
    "xxyxx" true
    "uurcxstgmygtbstg" false
    "ieodomkazucvgmuy" false))
