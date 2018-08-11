(ns servator.core
  (:require [servator.sign]
            [criterium.core :as cc])
  (:import [servator.sign Sign]))

(defmulti signal* (fn [^Sign sign] (.-name sign)))
(defmethod signal* :default [^Sign sign] nil)

(def signal->observer-fns {})

(defmacro signal
  "name: qualified keyword representing the signal to emit.
  body: the code that will produce the signal payload."
  [name & body]
  (assert (qualified-keyword? name))
  `(when (contains? (methods signal*) ~name)
     (signal* (Sign. ~name (do ~@body)))))

(defmacro observe
  "name: qualified keyword representing the signal to observe.
  fn: function of one argument (the signal payload) to be called by the signaller."
  [name fn]
  (assert (qualified-keyword? name))
  (let [has-method? (contains? (methods signal*) name)]
    (when-not has-method?
      (defmethod signal* name
        [^Sign sign]
        (doseq [observer-fn (signal->observer-fns name)]
          (observer-fn (.-payload sign)))))
    `(alter-var-root #'signal->observer-fns
                     update ~name (fnil conj []) ~fn)))

