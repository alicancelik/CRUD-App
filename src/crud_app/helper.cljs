(ns crud-app.helper
  (:require [clojure.string :as s]))


(defn uuid-generator
  []
  (str (random-uuid)))


(defn avatar-color-generator []
    (.toString (rand-int 16rFFFFFF) 16))


(defn dissoc-in
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))