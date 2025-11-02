(ns aoc-clojure.2015.day-9-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-9 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; parse-line Tests
;; ============================================================================

(t/deftest parse-line-test
  (t/testing "Parse single line with city names and distance"
    (t/is (match? [["London" "Dublin"] 464]
                  (sut/parse-line "London to Dublin = 464")))
    (t/is (match? [["Belfast" "London"] 518]
                  (sut/parse-line "Belfast to London = 518")))
    (t/is (match? [["Dublin" "Belfast"] 141]
                  (sut/parse-line "Dublin to Belfast = 141")))))

;; ============================================================================
;; build-graph Tests
;; ============================================================================

(t/deftest build-graph-test
  (t/testing "Build bidirectional graph from lines"
    (let [lines ["London to Dublin = 464"
                 "London to Belfast = 518"
                 "Dublin to Belfast = 141"]
          graph (sut/build-graph lines)]
      (t/is (match? 464 (get-in graph ["London" "Dublin"])))
      (t/is (match? 464 (get-in graph ["Dublin" "London"])))
      (t/is (match? 518 (get-in graph ["London" "Belfast"])))
      (t/is (match? 518 (get-in graph ["Belfast" "London"])))
      (t/is (match? 141 (get-in graph ["Dublin" "Belfast"])))
      (t/is (match? 141 (get-in graph ["Belfast" "Dublin"]))))))

;; ============================================================================
;; get-all-cities Tests
;; ============================================================================

(t/deftest get-all-cities-test
  (t/testing "Extract all unique cities from graph"
    (let [graph {"London" {"Dublin" 464 "Belfast" 518}
                 "Dublin" {"London" 464 "Belfast" 141}
                 "Belfast" {"London" 518 "Dublin" 141}}
          cities (sut/get-all-cities graph)]
      (t/is (match? #{"London" "Dublin" "Belfast"} cities)))))

;; ============================================================================
;; calculate-route-distance Tests
;; ============================================================================

(t/deftest calculate-route-distance-test
  (t/testing "Calculate total distance for a route"
    (let [graph {"London" {"Dublin" 464 "Belfast" 518}
                 "Dublin" {"London" 464 "Belfast" 141}
                 "Belfast" {"London" 518 "Dublin" 141}}]
      (t/is (match? 605
                    (sut/calculate-route-distance graph ["London" "Dublin" "Belfast"])))
      (t/is (match? 659
                    (sut/calculate-route-distance graph ["London" "Belfast" "Dublin"])))
      (t/is (match? 605
                    (sut/calculate-route-distance graph ["Belfast" "Dublin" "London"])))
      (t/is (match? 982
                    (sut/calculate-route-distance graph ["Dublin" "London" "Belfast"]))))))

;; ============================================================================
;; all-permutations Tests
;; ============================================================================

(t/deftest all-permutations-test
  (t/testing "Generate all permutations of a collection"
    (t/is (match? #{[1]}
                  (set (sut/all-permutations [1]))))
    (t/is (match? #{[1 2] [2 1]}
                  (set (sut/all-permutations [1 2]))))
    (t/is (match? #{[1 2 3] [1 3 2] [2 1 3] [2 3 1] [3 1 2] [3 2 1]}
                  (set (sut/all-permutations [1 2 3]))))))

;; ============================================================================
;; part-1 Tests (Shortest Route)
;; ============================================================================

(t/deftest part-1-test
  (t/testing "Official AoC example - shortest route"
    (let [input "London to Dublin = 464\nLondon to Belfast = 518\nDublin to Belfast = 141"]
      (t/is (match? 605 (sut/part-1 input)))))

  (t/testing "Empty input"
    (t/is (match? 0 (sut/part-1 ""))))

  (t/testing "Single route (only two cities)"
    (let [input "CityA to CityB = 100"]
      (t/is (match? 100 (sut/part-1 input)))))

  (t/testing "Triangle of cities"
    (let [input "A to B = 10\nB to C = 20\nA to C = 15"]
      (t/is (match? 25 (sut/part-1 input)))))

  (t/testing "Four cities square"
    (let [input "A to B = 1\nB to C = 1\nC to D = 1\nD to A = 1\nA to C = 100\nB to D = 100"]
      (t/is (match? 3 (sut/part-1 input))))))

;; ============================================================================
;; part-2 Tests (Longest Route)
;; ============================================================================

(t/deftest part-2-test
  (t/testing "Official AoC example - longest route"
    (let [input "London to Dublin = 464\nLondon to Belfast = 518\nDublin to Belfast = 141"]
      ;; Possible routes and distances:
      ;; Dublin -> London -> Belfast = 982
      ;; London -> Dublin -> Belfast = 605
      ;; London -> Belfast -> Dublin = 659
      ;; Dublin -> Belfast -> London = 659
      ;; Belfast -> Dublin -> London = 605
      ;; Belfast -> London -> Dublin = 982
      ;; Longest: 982
      (t/is (match? 982 (sut/part-2 input)))))

  (t/testing "Empty input"
    (t/is (match? 0 (sut/part-2 ""))))

  (t/testing "Single route (only two cities)"
    (let [input "CityA to CityB = 100"]
      (t/is (match? 100 (sut/part-2 input)))))

  (t/testing "Triangle of cities"
    (let [input "A to B = 10\nB to C = 20\nA to C = 15"]
      ;; Possible routes: A-B-C (30), A-C-B (35), B-A-C (25), B-C-A (35), C-A-B (25), C-B-A (30)
      ;; Longest: A-C-B or B-C-A = 35
      (t/is (match? 35 (sut/part-2 input)))))

  (t/testing "Four cities square"
    (let [input "A to B = 1\nB to C = 1\nC to D = 1\nD to A = 1\nA to C = 100\nB to D = 100"]
      ;; Longest path should use the diagonals
      ;; For example: A-C-B-D or similar patterns that use the 100-weight edges
      ;; A-C-B-D = 100 + (need B-C=1) + 100 = 201
      ;; Actually: A-C-D-B = 100 + 1 + 100 = 201 
      (t/is (match? 201 (sut/part-2 input))))))
