(ns aoc-clojure.2015.day-2-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-2 :as sut]))

(t/deftest total-area-calculation
  (t/are [input expected]
         (t/is (= (sut/calc-total-area input) expected))
    [2 3 4] 58
    [1 1 10] 43))

(t/deftest calc-ribbon-base-length
  (t/are [input expected]
         (t/is (= (sut/calc-ribbon-base-length input) expected))
    [2 3 4] 10
    [1 1 10] 4))

(t/deftest calc-ribbon-length
  (t/are [input expected]
         (t/is (= (sut/calc-ribbon-length input) expected))
    [2 3 4] 34
    [1 1 10] 14))
