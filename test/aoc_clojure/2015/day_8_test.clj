(ns aoc-clojure.2015.day-8-test
  (:require [clojure.test :as t]
            [aoc-clojure.2015.day-8 :as sut]
            [matcher-combinators.test]))

;; ============================================================================
;; in-memory-count Tests
;; ============================================================================

(t/deftest in-memory-count-test
  (t/testing "Empty string (just quotes)"
    (t/is (match? 0 (sut/in-memory-count "\"\""))))

  (t/testing "Simple strings without escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"abc\"" 3
      "\"aaa\"" 3
      "\"hello\"" 5
      "\"x\"" 1
      "\"12345\"" 5
      "\"   \"" 3))

  (t/testing "Escaped backslash (\\\\)"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"\\\\\"" 1
      "\"a\\\\b\"" 3
      "\"\\\\\\\\\"" 2
      "\"x\\\\y\\\\z\"" 5
      "\"\\\\\\\\\\\\\"" 3))

  (t/testing "Escaped quotes (\\\")"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"\\\"\"" 1
      "\"a\\\"b\"" 3
      "\"aaa\\\"aaa\"" 7
      "\"\\\"\\\"\"" 2
      "\"\\\"\\\"\\\"\"" 3))

  (t/testing "Strings with both backslash and quote escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"a\\\\b\\\"c\"" 5
      "\"\\\"\\\\\"" 2
      "\"\\\\\\\"\"" 2
      "\"\\\\\\\"\\\\\"" 3))

  (t/testing "Official examples from AoC Day 8 problem"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"\"" 0
      "\"abc\"" 3
      "\"aaa\\\"aaa\"" 7))

  (t/testing "Complex real-world-like strings"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"path\\\\to\\\\file\"" 12 ; "path\to\file"
      "\"She said \\\"Hello!\\\"\"" 17 ; "She said "Hello!""
      "\"line1\\\\nline2\"" 12 ; "line1\nline2" (not actual newline)
      "\"start\\\\\\\"end\"" 10)) ; "start\\"end"

  (t/testing "Hex escape sequences (\\xHH)"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"\\x27\"" 1
      "\"\\x41\"" 1
      "\"a\\x27b\"" 3
      "\"\\x00\"" 1
      "\"\\xff\"" 1
      "\"\\xAB\"" 1
      "\"\\xab\"" 1))

  (t/testing "Hex escapes mixed with other escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/in-memory-count in)))
      "\"\\x27\\\\\\\"\"" 3
      "\"test\\x20string\"" 11
      "\"\\\"\\\\\\x41\"" 3
      "\"\\x48\\x65\\x6c\\x6c\\x6f\"" 5)))

;; ============================================================================
;; encoded-count Tests
;; ============================================================================

(t/deftest encoded-count-test
  (t/testing "Official AoC examples"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"\"" 6
      "\"abc\"" 9
      "\"aaa\\\"aaa\"" 16
      "\"\\x27\"" 11))

  (t/testing "Empty string literal"
    (t/is (match? 6 (sut/encoded-count "\"\""))))

  (t/testing "Simple strings without escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"abc\"" 9
      "\"ab\"" 8
      "\"a\"" 7
      "\"hello world\"" 17
      "\"x\"" 7))

  (t/testing "Strings with backslash escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"a\\\\b\"" 12
      "\"a\\\\b\\\\c\"" 17
      "\"\\\\\"" 10
      "\"\\\\\\\\\"" 14
      "\"x\\\\y\\\\z\"" 17))

  (t/testing "Strings with quote escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"a\\\"b\"" 12
      "\"a\\\"b\\\"c\"" 17
      "\"\\\"\"" 10
      "\"\\\"\\\"\"" 14
      "\"aaa\\\"aaa\"" 16))

  (t/testing "Strings with mixed escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"a\\\\b\\\"c\"" 17
      "\"\\\\\\\"x\"" 15
      "\"\\\"\\\\\"" 14))

  (t/testing "Strings with hex escapes"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"\\x27\"" 11
      "\"\\xab\"" 11
      "\"\\x27\\x41\\x42\"" 21
      "\"a\\x27b\"" 13))

  (t/testing "Complex mixed strings"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"a\\\\b\\\"c\\x27d\"" 23
      "\"\\x27\\\\\\\"test\\\"\"" 27
      "\"x\\\\y\\\"z\\x01\\x02\\x03\"" 32))

  (t/testing "Real-world complex examples"
    (t/are [in expected]
           (t/is (match? expected (sut/encoded-count in)))
      "\"vqsremfk\\x8fxiknektafj\"" 29
      "\"foo\\\"bar\\\"baz\\\"qux\"" 30)))

;; ============================================================================
;; part-2 Tests
;; ============================================================================

(t/deftest part-2-test
  (t/testing "Official AoC example"
    (let [input "\"\"\n\"abc\"\n\"aaa\\\"aaa\"\n\"\\x27\""]
      (t/is (match? 19 (sut/part-2 input)))))

  (t/testing "Single string examples"
    (t/is (match? 4 (sut/part-2 "\"\"")))
    (t/is (match? 4 (sut/part-2 "\"abc\"")))
    (t/is (match? 6 (sut/part-2 "\"aaa\\\"aaa\"")))
    (t/is (match? 5 (sut/part-2 "\"\\x27\""))))

  (t/testing "Multiple simple strings"
    (let [input "\"a\"\n\"b\"\n\"c\""]
      (t/is (match? 12 (sut/part-2 input)))))

  (t/testing "Strings with various escapes"
    (let [input "\"a\\\\b\"\n\"c\\\"d\"\n\"e\\x01f\""]
      (t/is (match? 17 (sut/part-2 input)))))

  (t/testing "Empty input"
    (t/is (match? 0 (sut/part-2 ""))))

  (t/testing "Complex real-world example"
    (let [input "\"vqsremfk\\x8fxiknektafj\"\n\"foo\\\"bar\\\"baz\\\"qux\""]
      ;; "vqsremfk\x8fxiknektafj" -> 24 chars, encoded -> 29 chars, diff = 5
      ;; "foo\"bar\"baz\"qux" -> 20 chars, encoded -> 30 chars, diff = 10
      ;; Total difference = 5 + 10 = 15
      (t/is (match? 15 (sut/part-2 input))))))
