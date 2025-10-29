(ns aoc-clojure.2015.day-6-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-6 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; Parsing Tests
;; ============================================================================

(t/deftest parse-raw-operation-test
  (t/testing "Parses turn on operations"
    (t/is (match? ["turn on" [0 1] [2 3]]
                  (sut/parse-raw-operation "turn on 0,1 through 2,3")))
    (t/is (match? ["turn on" [489 959] [759 964]]
                  (sut/parse-raw-operation "turn on 489,959 through 759,964"))))

  (t/testing "Parses turn off operations"
    (t/is (match? ["turn off" [0 1] [2 3]]
                  (sut/parse-raw-operation "turn off 0,1 through 2,3"))))

  (t/testing "Parses toggle operations"
    (t/is (match? ["toggle" [0 1] [2 3]]
                  (sut/parse-raw-operation "toggle 0,1 through 2,3")))))

;; ============================================================================
;; Part 1: Boolean Light Grid Tests
;; ============================================================================

(t/deftest apply-operation-test
  (t/testing "Turn on lights"
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1]}
                  (sut/apply-operation #{} "turn on" [0 0] [1 1])))
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1]}
                  (sut/apply-operation #{[0 0] [0 1] [1 0] [1 1]} "turn on" [0 0] [1 1])))
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1] [2 2]}
                  (sut/apply-operation #{[2 2]} "turn on" [0 0] [1 1]))))

  (t/testing "Turn off lights"
    (t/is (match? #{}
                  (sut/apply-operation #{} "turn off" [0 0] [1 1])))
    (t/is (match? #{}
                  (sut/apply-operation #{[0 0] [0 1] [1 0] [1 1]} "turn off" [0 0] [1 1])))
    (t/is (match? #{[2 2]}
                  (sut/apply-operation #{[2 2]} "turn off" [0 0] [1 1])))
    (t/is (match? #{[2 2]}
                  (sut/apply-operation #{[0 0] [2 2]} "turn off" [0 0] [1 1]))))

  (t/testing "Toggle lights"
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1]}
                  (sut/apply-operation #{} "toggle" [0 0] [1 1])))
    (t/is (match? #{[1 0] [2 0] [0 1] [0 2]}
                  (sut/apply-operation #{[1 0] [2 0]
                                         [0 1] [1 1] [2 1]
                                         [0 2] [1 2] [2 2]}
                                       "toggle" [1 1] [2 2])))
    (t/is (match? #{[0 1] [1 0] [1 1]}
                  (sut/apply-operation #{[0 0]} "toggle" [0 0] [1 1])))
    (t/is (match? #{[0 1] [1 0] [1 1] [2 2]}
                  (sut/apply-operation #{[0 0] [2 2]} "toggle" [0 0] [1 1])))))

(t/deftest part-1-test
  (t/testing "Single instruction"
    (t/is (match? 9 (sut/part-1 "turn on 0,0 through 2,2"))))

  (t/testing "Multiple instructions"
    (t/is (match? 8 (sut/part-1 "turn on 0,0 through 2,2\nturn off 0,0 through 0,0")))
    (t/is (match? 4 (sut/part-1 "turn on 0,0 through 2,2\nturn off 0,0 through 0,0\ntoggle 1,1 through 2,2")))))

;; ============================================================================
;; Part 2: Brightness Grid Tests
;; ============================================================================

(t/deftest calculate-brightness-test
  (t/testing "Turn on increases brightness by 1"
    (t/is (match? {[0 0] 1}
                  (sut/calculate-brightness {} "turn on" [0 0] [0 0])))
    (t/is (match? {[0 0] 1 [1 0] 1 [1 1] 1}
                  (sut/calculate-brightness {} "turn on" [0 0] [1 1]))))

  (t/testing "Turn off decreases brightness by 1 (minimum 0)"
    (t/is (match? {[0 0] 0}
                  (sut/calculate-brightness {} "turn off" [0 0] [0 0])))
    (t/is (match? {[0 0] 0}
                  (sut/calculate-brightness {[0 0] 0} "turn off" [0 0] [0 0])))
    (t/is (match? {[0 0] 0}
                  (sut/calculate-brightness {[0 0] 1} "turn off" [0 0] [0 0])))
    (t/is (match? {[0 0] 1 [1 0] 0}
                  (sut/calculate-brightness {[0 0] 2 [1 0] 1} "turn off" [0 0] [1 0]))))

  (t/testing "Toggle increases brightness by 2"
    (t/is (match? {[0 0] 2}
                  (sut/calculate-brightness {} "toggle" [0 0] [0 0])))
    (t/is (match? {[0 0] 2 [1 0] 2 [1 1] 2}
                  (sut/calculate-brightness {} "toggle" [0 0] [1 1])))))

(t/deftest part-2-test
  (t/testing "Single instruction"
    (t/is (match? 9 (sut/part-2 "turn on 0,0 through 2,2"))))

  (t/testing "Multiple instructions"
    (t/is (match? 8 (sut/part-2 "turn on 0,0 through 2,2\nturn off 0,0 through 0,0")))
    (t/is (match? 16 (sut/part-2 "turn on 0,0 through 2,2\nturn off 0,0 through 0,0\ntoggle 1,1 through 2,2")))))
