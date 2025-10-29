(ns aoc-clojure.2015.day-3-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-3 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; Part 1: Single Santa Tests
;; ============================================================================

(t/deftest single-santa-visited-houses-test
  (t/testing "Counts unique houses visited by Santa"
    (t/is (match? 2 (sut/part-1 ">")))
    (t/is (match? 4 (sut/part-1 "^>v<")))
    (t/is (match? 2 (sut/part-1 "^v^v^v^v^v")))))

;; ============================================================================
;; Part 2: Santa and Robo-Santa Tests
;; ============================================================================

(t/deftest santa-and-robo-santa-visited-houses-test
  (t/testing "Counts unique houses visited by Santa and Robo-Santa"
    (t/is (match? 3 (sut/part-2 "^v")))
    (t/is (match? 3 (sut/part-2 "^>v<")))
    (t/is (match? 11 (sut/part-2 "^v^v^v^v^v")))))
