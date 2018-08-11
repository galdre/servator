(ns servator.core-test
  (:require [clojure.test :refer :all]
            [servator.core :refer :all]
            [criterium.core :as cc]))

(defn signal-nothing
  [name payload]
  nil)

(defn signalling-nothing-1
  [xs]
  (reduce + (for [x xs]
              (do (signal :x/y (reduce + (range x)))
                  (reduce + (range x))))))

(defn signalling-nothing-2
  [xs]
  (reduce + (for [x xs]
              (do (signal-nothing :x/y (reduce + (range x)))
                  (reduce + (range x))))))

(defn signalling-nothing-3
  [xs]
  (reduce + (for [x xs] (reduce + (range x)))))

(def blah (atom []))
(observe :foo/bar (fn [x] (swap! blah conj x)))

(defn signalling-something
  [xs]
  (reduce + (for [x xs]
              (do (signal :foo/bar (reduce + (range x)))
                  (reduce + (range x))))))

(defn test
  []
  (cc/quick-bench
   (signalling-nothing-1 (range 10000)))
  (cc/quick-bench
   (signalling-nothing-2 (range 10000)))
  (cc/quick-bench
   (signalling-nothing-3 (range 10000)))
  (cc/quick-bench
   (signalling-something (range 10000))))

