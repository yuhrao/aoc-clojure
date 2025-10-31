(ns aoc-clojure.2015.day-2-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-2 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; Wrapping Paper Tests
;; ============================================================================

(t/deftest total-area-calculation-test
  (t/testing "Calculates total wrapping paper area including slack"
    (t/is (match? 58 (sut/calc-total-area [2 3 4])))
    (t/is (match? 43 (sut/calc-total-area [1 1 10])))))

;; ============================================================================
;; Ribbon Tests
;; ============================================================================

(t/deftest ribbon-base-length-test
  (t/testing "Calculates ribbon needed to wrap around smallest perimeter"
    (t/is (match? 10 (sut/calc-ribbon-base-length [2 3 4])))
    (t/is (match? 4 (sut/calc-ribbon-base-length [1 1 10])))))

(t/deftest ribbon-total-length-test
  (t/testing "Calculates total ribbon length (wrap + bow)"
    (t/is (match? 34 (sut/calc-ribbon-length [2 3 4])))
    (t/is (match? 14 (sut/calc-ribbon-length [1 1 10])))))
