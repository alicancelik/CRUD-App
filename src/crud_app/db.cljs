(ns crud-app.db
  (:require [reagent.core :as r]
            [crud-app.helper :refer [uuid-generator avatar-color-generator]]))

(defonce api-url "https://facebook.github.io/react-native/movies.json")

(defonce
  app-state (r/atom {:employees        [{:id (uuid-generator) :name "Alican" :surname "Celik" :avatar-color (str "#" (avatar-color-generator))}
                                               {:id (uuid-generator) :name "Ergenekon" :surname "Yigit" :avatar-color (str "#" (avatar-color-generator))}
                                               {:id (uuid-generator) :name "Sabire" :surname "Avci" :avatar-color (str "#" (avatar-color-generator))}]
                            :editing          false
                            :movies {}
                            :current-employee {:name "" :surname "" :id ""}}))