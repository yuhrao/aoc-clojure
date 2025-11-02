(ns aoc-clojure.2015.day-7
  (:require
   [aoc-clojure.registry.core :as registry]
   [clojure.string :as str]))

(def ^:private raw-operator->operator {"AND" :and
                                       "OR" :or
                                       "NOT" :not
                                       "->" :assignment
                                       "LSHIFT" :l-shift
                                       "RSHIFT" :r-shift})

(defn- all-digits?
  ^Boolean [^String s]
  (and (pos? (.length s))
       (loop [i 0]
         (if (< i (.length s))
           (if (Character/isDigit (.charAt s i))
             (recur (inc i))
             false)
           true))))

(defn- parse-binary-operation
  [[in-a operator in-b _ out]]
  {:input [in-a in-b]
   :operator (raw-operator->operator operator)
   :output out})

(defn- parse-unary-operation
  [[operator in _ out]]
  {:input [in]
   :operator (raw-operator->operator operator)
   :output out})

(defn- parse-assignment
  [[in _ out]]
  {:input [in]
   :operator :assignment
   :output out})

(defn row->operation
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
  {:assignment (fn ^long [^long a _] a)
   :not (fn ^long [^long a _] (bit-and 0xFFFF (bit-not a)))
   :and (fn ^long [^long a ^long b] (bit-and a b))
   :or (fn ^long [^long a ^long b] (bit-or a b))
   :l-shift (fn ^long [^long a ^long b] (bit-shift-left a b))
   :r-shift (fn ^long [^long a ^long b] (bit-shift-right a b))})

(defn- parse-literal
  [v]
  (Integer/parseUnsignedInt v))

(defn- literal?
  [s]
  (all-digits? s))

(defn- apply-operator
  [operator input-vals]
  (let [operator-fn (operator-fns operator)
        a (long (first input-vals))
        b (long (second input-vals))]
    (bit-and 0xFFFF (long (operator-fn a b)))))

(defn get-node-value
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

(defn part-1
  [input]
  (let [graph (->> input
                   str/split-lines
                   (reduce build-dependency-graph {}))]
    (get-node-value graph "a")))

(defn part-2
  [input]
  (let [graph (->> input
                   str/split-lines
                   (reduce build-dependency-graph {}))
        initial-result (get-node-value graph "a")
        updated-graph (assoc-in graph ["b" :input] [(str initial-result)])]
    (get-node-value updated-graph "a")))

(registry/register {:year 2015 :day 7 :part-1 part-1 :part-2 part-2})

(defn execute []
  (registry/execute-day {:year 2015 :day 7}))

(comment
  (execute))
