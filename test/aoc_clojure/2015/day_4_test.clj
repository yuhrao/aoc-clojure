(ns aoc-clojure.2015.day-4-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-4 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; AdventCoin Mining Tests
;; ============================================================================

(t/deftest mine-adventcoins-test
  (t/testing "Finds lowest positive number that produces hash with 5 leading zeros"
    (t/is (match? 609043 (sut/mine 5 "abcdef")))
    (t/is (match? 1048970 (sut/mine 5 "pqrstuv")))))
