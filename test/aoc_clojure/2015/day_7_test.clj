(ns aoc-clojure.2015.day-7-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-7 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; Parsing Tests
;; ============================================================================

(t/deftest row->operation-test
  (t/testing "Binary operations"
    (t/are [in expected]
           (t/is (match? expected (sut/row->operation in)))
      "x AND y -> d" {:input ["x" "y"] :operator :and :output "d"}
      "x OR y -> e" {:input ["x" "y"] :operator :or :output "e"}
      "x LSHIFT 2 -> f" {:input ["x" "2"] :operator :l-shift :output "f"}
      "y RSHIFT 2 -> g" {:input ["y" "2"] :operator :r-shift :output "g"}))

  (t/testing "Unary operations"
    (t/are [in expected]
           (t/is (match? expected (sut/row->operation in)))
      "NOT a -> b" {:input ["a"] :operator :not :output "b"}
      "NOT y -> i" {:input ["y"] :operator :not :output "i"}))

  (t/testing "Assignment operations"
    (t/are [in expected]
           (t/is (match? expected (sut/row->operation in)))
      "123 -> x" {:input ["123"] :operator :assignment :output "x"}
      "456 -> y" {:input ["456"] :operator :assignment :output "y"}
      "a -> b" {:input ["a"] :operator :assignment :output "b"})))

(t/deftest build-dependency-graph-test
  (t/testing "Builds graph from single instruction"
    (let [graph (sut/build-dependency-graph {} "123 -> x")]
      (t/is (match? {"x" {:input ["123"] :operator :assignment :output "x"}}
                    graph))))

  (t/testing "Builds graph from multiple instructions"
    (let [graph (-> {}
                    (sut/build-dependency-graph "123 -> x")
                    (sut/build-dependency-graph "456 -> y")
                    (sut/build-dependency-graph "x AND y -> d"))]
      (t/is (match? 3 (count graph)))
      (t/is (contains? graph "x"))
      (t/is (contains? graph "y"))
      (t/is (contains? graph "d")))))

;; ============================================================================
;; Value Computation Tests
;; ============================================================================

(def test-graph
  "Sample circuit graph for testing"
  {"x" {:input ["123"] :operator :assignment :output "x"}
   "y" {:input ["456"] :operator :assignment :output "y"}
   "d" {:input ["x" "y"] :operator :and :output "d"}
   "e" {:input ["x" "y"] :operator :or :output "e"}
   "f" {:input ["x" "2"] :operator :l-shift :output "f"}
   "g" {:input ["y" "2"] :operator :r-shift :output "g"}
   "h" {:input ["x"] :operator :not :output "h"}
   "i" {:input ["y"] :operator :not :output "i"}})

(t/deftest get-node-value-test
  (t/testing "Literal values"
    (t/is (match? 123 (sut/get-node-value {} "123")))
    (t/is (match? 456 (sut/get-node-value {} "456")))
    (t/is (match? 0 (sut/get-node-value {} "0"))))

  (t/testing "Assignment operations"
    (t/is (match? 123 (sut/get-node-value test-graph "x")))
    (t/is (match? 456 (sut/get-node-value test-graph "y"))))

  (t/testing "Binary operations"
    (t/is (match? 72 (sut/get-node-value test-graph "d"))) ; 123 AND 456
    (t/is (match? 507 (sut/get-node-value test-graph "e"))) ; 123 OR 456
    (t/is (match? 492 (sut/get-node-value test-graph "f"))) ; 123 << 2
    (t/is (match? 114 (sut/get-node-value test-graph "g")))) ; 456 >> 2

  (t/testing "Unary operations"
    (t/is (match? 65412 (sut/get-node-value test-graph "h"))) ; NOT 123
    (t/is (match? 65079 (sut/get-node-value test-graph "i")))) ; NOT 456

  (t/testing "Unknown wire returns 0"
    (t/is (match? 0 (sut/get-node-value test-graph "unknown")))))

(t/deftest get-node-values-test
  (t/testing "Batch computation"
    (let [results (sut/get-node-values test-graph ["x" "y" "d" "e"])]
      (t/is (match? {"x" 123 "y" 456 "d" 72 "e" 507} results))))

  (t/testing "Empty list returns empty map"
    (t/is (match? {} (sut/get-node-values test-graph [])))))

;; ============================================================================
;; Complex Dependency Tests
;; ============================================================================

(t/deftest complex-dependencies-test
  (t/testing "Chain of dependencies"
    (let [graph {"a" {:input ["1"] :operator :assignment :output "a"}
                 "b" {:input ["a"] :operator :assignment :output "b"}
                 "c" {:input ["b"] :operator :assignment :output "c"}
                 "d" {:input ["c"] :operator :assignment :output "d"}}]
      (t/is (match? 1 (sut/get-node-value graph "d")))))

  (t/testing "Diamond dependency pattern"
    (let [graph {"a" {:input ["5"] :operator :assignment :output "a"}
                 "b" {:input ["3"] :operator :assignment :output "b"}
                 "c" {:input ["a" "b"] :operator :and :output "c"}
                 "d" {:input ["a" "b"] :operator :or :output "d"}
                 "e" {:input ["c" "d"] :operator :and :output "e"}}]
      (t/is (match? 1 (sut/get-node-value graph "c"))) ; 5 AND 3 = 1
      (t/is (match? 7 (sut/get-node-value graph "d"))) ; 5 OR 3 = 7
      (t/is (match? 1 (sut/get-node-value graph "e"))))) ; 1 AND 7 = 1

  (t/testing "Multiple levels of NOT operations"
    (let [graph {"a" {:input ["255"] :operator :assignment :output "a"}
                 "b" {:input ["a"] :operator :not :output "b"}
                 "c" {:input ["b"] :operator :not :output "c"}}]
      (t/is (match? 255 (sut/get-node-value graph "a")))
      (t/is (match? 65280 (sut/get-node-value graph "b"))) ; NOT 255
      (t/is (match? 255 (sut/get-node-value graph "c")))))) ; NOT (NOT 255)

;; ============================================================================
;; Edge Cases and 16-bit Masking Tests
;; ============================================================================

(t/deftest edge-cases-test
  (t/testing "16-bit masking"
    (let [graph {"a" {:input ["65535"] :operator :assignment :output "a"}
                 "b" {:input ["0"] :operator :assignment :output "b"}
                 "c" {:input ["a"] :operator :not :output "c"}
                 "d" {:input ["b"] :operator :not :output "d"}}]
      (t/is (match? 65535 (sut/get-node-value graph "a"))) ; Max 16-bit value
      (t/is (match? 0 (sut/get-node-value graph "b"))) ; Min value
      (t/is (match? 0 (sut/get-node-value graph "c"))) ; NOT 65535
      (t/is (match? 65535 (sut/get-node-value graph "d"))))) ; NOT 0

  (t/testing "Shift operations at boundaries"
    (let [graph {"a" {:input ["1"] :operator :assignment :output "a"}
                 "b" {:input ["a" "15"] :operator :l-shift :output "b"}
                 "c" {:input ["b" "15"] :operator :r-shift :output "c"}}]
      (t/is (match? 32768 (sut/get-node-value graph "b"))) ; 1 << 15
      (t/is (match? 1 (sut/get-node-value graph "c")))))) ; 32768 >> 15

;; ============================================================================
;; Integration Tests
;; ============================================================================

(t/deftest integration-test
  (t/testing "Full example from problem description"
    (let [input "123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i"
          graph (reduce sut/build-dependency-graph {} (clojure.string/split-lines input))]
      (t/is (match? 72 (sut/get-node-value graph "d")))
      (t/is (match? 507 (sut/get-node-value graph "e")))
      (t/is (match? 492 (sut/get-node-value graph "f")))
      (t/is (match? 114 (sut/get-node-value graph "g")))
      (t/is (match? 65412 (sut/get-node-value graph "h")))
      (t/is (match? 65079 (sut/get-node-value graph "i")))
      (t/is (match? 123 (sut/get-node-value graph "x")))
      (t/is (match? 456 (sut/get-node-value graph "y"))))))
