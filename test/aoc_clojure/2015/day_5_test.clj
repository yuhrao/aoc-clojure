(ns aoc-clojure.2015.day-5-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-5 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; Part 1: Nice String V1 Tests
;; ============================================================================

(t/deftest nice-string-v1-test
  (t/testing "Nice strings (version 1 rules)"
    (t/is (match? true (sut/nice-string-v1? "ugknbfddgicrmopn")))
    (t/is (match? true (sut/nice-string-v1? "aaa"))))

  (t/testing "Naughty strings (version 1 rules)"
    (t/is (match? false (sut/nice-string-v1? "jchzalrnumimnmhp")))
    (t/is (match? false (sut/nice-string-v1? "haegwjzuvuyypxyu")))
    (t/is (match? false (sut/nice-string-v1? "dvszwmarrgswjxmb")))))

;; ============================================================================
;; Part 2: Nice String V2 Tests
;; ============================================================================

(t/deftest nice-string-v2-test
  (t/testing "Nice strings (version 2 rules)"
    (t/is (match? true (sut/nice-string-v2? "qjhvhtzxzqqjkmpb")))
    (t/is (match? true (sut/nice-string-v2? "xxyxx"))))

  (t/testing "Naughty strings (version 2 rules)"
    (t/is (match? false (sut/nice-string-v2? "uurcxstgmygtbstg")))
    (t/is (match? false (sut/nice-string-v2? "ieodomkazucvgmuy")))))
