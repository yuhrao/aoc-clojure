(ns aoc-clojure.2015.day-7
  (:require
   [aoc-clojure.utils :as utils]
   [clojure.string :as str]))

(def ^:private raw-operator->operator {"AND" :and
                                       "OR" :or
                                       "NOT" :not
                                       "->" :assignment
                                       "LSHIFT" :l-shift
                                       "RSHIFT" :r-shift})

(defn- all-digits?
  "Fast check if string contains only digits (more efficient than regex)"
  ^Boolean [^String s]
  (and (pos? (.length s))
       (loop [i 0]
         (if (< i (.length s))
           (if (Character/isDigit (.charAt s i))
             (recur (inc i))
             false)
           true))))

(defn- parse-binary-operation
  "Parse a binary operation like 'x AND y -> d'"
  [[in-a operator in-b _ out]]
  {:input [in-a in-b]
   :operator (raw-operator->operator operator)
   :output out})

(defn- parse-unary-operation
  "Parse a unary operation like 'NOT x -> h'"
  [[operator in _ out]]
  {:input [in]
   :operator (raw-operator->operator operator)
   :output out})

(defn- parse-assignment
  "Parse an assignment operation like '123 -> x'"
  [[in _ out]]
  {:input [in]
   :operator :assignment
   :output out})

(defn row->operation
  "Parse a wire instruction row into an operation map.
   Returns a map with :input, :operator, and :output keys."
  [row]
  (let [pieces (str/split row #"\s")]
    (case (count pieces)
      5 (parse-binary-operation pieces)
      4 (parse-unary-operation pieces)
      3 (parse-assignment pieces))))

(defn build-dependency-graph [graph row]
  (let [{:keys [output] :as operation} (row->operation row)]
    (assoc graph output operation)))

(def ^:private operator-fns
  "Optimized operator functions with type hints for performance"
  {:assignment (fn ^long [^long a _] a)
   :not (fn ^long [^long a _] (bit-and 0xFFFF (bit-not a)))
   :and (fn ^long [^long a ^long b] (bit-and a b))
   :or (fn ^long [^long a ^long b] (bit-or a b))
   :l-shift (fn ^long [^long a ^long b] (bit-shift-left a b))
   :r-shift (fn ^long [^long a ^long b] (bit-shift-right a b))})

(defn- parse-literal
  "Parse a string literal to an unsigned 16-bit integer"
  [v]
  (Integer/parseUnsignedInt v))

(defn- literal?
  "Check if a string represents a numeric literal"
  [s]
  (all-digits? s))

(defn- apply-operator
  "Apply a bitwise operator to input values, masking to 16 bits"
  [operator input-vals]
  (let [operator-fn (operator-fns operator)
        a (long (first input-vals))
        b (long (second input-vals))]
    (bit-and 0xFFFF (long (operator-fn a b)))))

(defn get-node-value
  "Compute the value of a wire in the circuit graph using optimized transient maps.
   
   Args:
     graph - The circuit dependency graph
     value-or-ref - Either a wire name or a numeric literal
   
   Returns:
     The computed integer value (0-65535)"
  [graph value-or-ref]
  (letfn [(compute [node tmemo]
            (if-let [cached (get tmemo node)]
              [cached tmemo]
              (if (literal? node)
                (let [v (parse-literal node)]
                  [v (assoc! tmemo node v)])
                (if-let [{:keys [operator input]} (get graph node)]
                  (let [[input-vals tmemo']
                        (case (count input)
                          1 (let [[v1 tm1] (compute (first input) tmemo)]
                              [[v1 0] tm1])
                          2 (let [[v1 tm1] (compute (first input) tmemo)
                                  [v2 tm2] (compute (second input) tm1)]
                              [[v1 v2] tm2]))
                        result (apply-operator operator input-vals)]
                    [result (assoc! tmemo' node result)])
                  [0 (assoc! tmemo node 0)]))))]
    (let [[result _] (compute value-or-ref (transient {}))]
      result)))

(defn get-node-values
  "Efficiently compute multiple node values with shared memoization.
   Returns a map of node names to their values."
  [graph nodes]
  (reduce (fn [results node]
            (assoc results node (get-node-value graph node)))
          {}
          nodes))

(defn part-1
  "What signal is ultimately provided to wire a?"
  [input]
  (let [graph (->> input
                   str/split-lines
                   (reduce build-dependency-graph {}))]
    (get-node-value graph "a")))

(defn part-2
  ""
  [input])

(defn execute []
  (utils/execute-day {:year 2015 :day 7 :part-1 part-1 :part-2 part-2}))

(comment
  (execute))
