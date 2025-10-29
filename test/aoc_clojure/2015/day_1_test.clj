(ns aoc-clojure.2015.day-1-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-1 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; Part 1: Floor Calculation Tests
;; ============================================================================

(t/deftest floor-calculation-test
  (t/testing "Balanced parentheses result in floor 0"
    (t/is (match? 0 (sut/part-1 "(())")))
    (t/is (match? 0 (sut/part-1 "()()"))))

  (t/testing "Opening parentheses move up floors"
    (t/is (match? 3 (sut/part-1 "(((")))
    (t/is (match? 3 (sut/part-1 "(()(()(")))
    (t/is (match? 3 (sut/part-1 "))((((("))))

  (t/testing "Closing parentheses move down floors"
    (t/is (match? -1 (sut/part-1 "())")))
    (t/is (match? -1 (sut/part-1 "))(")))
    (t/is (match? -3 (sut/part-1 ")))")))
    (t/is (match? -3 (sut/part-1 ")())())")))))

;; ============================================================================
;; Part 2: Basement Position Tests
;; ============================================================================

(t/deftest basement-position-test
  (t/testing "Position of first character that enters basement (floor -1)"
    (t/is (match? 1 (sut/part-2 ")")))
    (t/is (match? 5 (sut/part-2 "()())")))))
