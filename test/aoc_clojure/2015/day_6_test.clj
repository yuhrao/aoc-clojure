(ns aoc-clojure.2015.day-6-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-6 :as sut]
            [matcher-combinators.test]))

(t/deftest parse-raw-operation
  (t/are [input expected]
         (t/is (match? expected (sut/parse-raw-operation input)))
    "turn on 0,1 through 2,3" ["turn on" [0 1] [2 3]]
    "turn off 0,1 through 2,3" ["turn off" [0 1] [2 3]]
    "toggle 0,1 through 2,3" ["toggle" [0 1] [2 3]]
    "turn on 489,959 through 759,964" ["turn on" [489 959] [759 964]]))

(t/deftest apply-operation
  (t/testing "turning on lights"
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1]} (sut/apply-operation #{} "turn on" [0 0] [1 1])))
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1]} (sut/apply-operation #{[0 0] [0 1] [1 0] [1 1]} "turn on" [0 0] [1 1])))
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1] [2 2]} (sut/apply-operation #{[2 2]} "turn on" [0 0] [1 1]))))
  (t/testing "turning off lights"
    (t/is (match? #{} (sut/apply-operation #{} "turn off" [0 0] [1 1])))
    (t/is (match? #{} (sut/apply-operation #{[0 0] [0 1] [1 0] [1 1]} "turn off" [0 0] [1 1])))
    (t/is (match? #{[2 2]} (sut/apply-operation #{[2 2]} "turn off" [0 0] [1 1])))
    (t/is (match? #{[2 2]} (sut/apply-operation #{[0 0] [2 2]} "turn off" [0 0] [1 1]))))
  (t/testing "toggling lights"
    (t/is (match? #{[0 0] [0 1] [1 0] [1 1]} (sut/apply-operation #{} "toggle" [0 0] [1 1])))
    (t/is (match? #{[1 0] [2 0] [0 1] [0 2]} (sut/apply-operation #{[1 0] [2 0]
                                                                    [0 1] [1 1] [2 1]
                                                                    [0 2] [1 2] [2 2]} "toggle" [1 1] [2 2])))
    (t/is (match? #{[0 1] [1 0] [1 1]} (sut/apply-operation #{[0 0]} "toggle" [0 0] [1 1])))
    (t/is (match? #{[0 1] [1 0] [1 1] [2 2]} (sut/apply-operation #{[0 0] [2 2]} "toggle" [0 0] [1 1])))))

(t/deftest part-1
  (t/testing "a single instruction"
    (let [input "turn on 0,0 through 2,2"]
      (t/is (match? 9 (sut/part-1 input)))))
  (t/testing "two instructions"
    (let [input "turn on 0,0 through 2,2\nturn off 0,0 through 0,0"]
      (t/is (match? 8 (sut/part-1 input)))))
  (t/testing "multiple instructions"
    (let [input "turn on 0,0 through 2,2\nturn off 0,0 through 0,0\ntoggle 1,1 through 2,2"]
      (t/is (match? 4 (sut/part-1 input))))))

(t/deftest calculate-brightness
  (t/testing "turn on lights"
    (t/is (match? {[0 0] 1} (sut/calculate-brightness {} "turn on" [0 0] [0 0])))
    (t/is (match? {[0 0] 1
                   [1 0] 1
                   [1 1] 1} (sut/calculate-brightness {} "turn on" [0 0] [1 1]))))
  (t/testing "turn off lights"
    (t/is (match? {[0 0] 0} (sut/calculate-brightness {} "turn off" [0 0] [0 0])))
    (t/is (match? {[0 0] 0} (sut/calculate-brightness {[0 0] 0} "turn off" [0 0] [0 0])))
    (t/is (match? {[0 0] 0} (sut/calculate-brightness {[0 0] 1} "turn off" [0 0] [0 0])))
    (t/is (match? {[0 0] 1
                   [1 0] 0} (sut/calculate-brightness {[0 0] 2
                                                       [1 0] 1} "turn off" [0 0] [1 0]))))
  (t/testing "toggle lights"
    (t/is (match? {[0 0] 2} (sut/calculate-brightness {} "toggle" [0 0] [0 0])))
    (t/is (match? {[0 0] 2
                   [1 0] 2
                   [1 1] 2} (sut/calculate-brightness {} "toggle" [0 0] [1 1])))))

(t/deftest part-2
  (t/testing "a single instruction"
    (let [input "turn on 0,0 through 2,2"]
      (t/is (match? 9 (sut/part-2 input)))))
  (t/testing "two instructions"
    (let [input "turn on 0,0 through 2,2\nturn off 0,0 through 0,0"]
      (t/is (match? 8 (sut/part-2 input)))))
  (t/testing "multiple instructions"
    (let [input "turn on 0,0 through 2,2\nturn off 0,0 through 0,0\ntoggle 1,1 through 2,2"]
      (t/is (match? 16 (sut/part-2 input))))))
