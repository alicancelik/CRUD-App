(ns crud-app.routes
  (:import goog.history.Html5History)
  (:require [goog.events :as events]
            [goog.history.EventType :as EventType]
            [secretary.core :as secretary :include-macros true]
            [crud-app.db :refer [app-state]]
            [crud-app.home :refer [home]]
            [crud-app.profile :refer [profile]]))

(defn hook-browser-navigation!
  []
  (doto (Html5History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))


(defn app-routes
  []
  (secretary/set-config! :prefix "#")

  (secretary/defroute "/"
                      []
                      (swap! app-state assoc :page :home))

  (secretary/defroute "/profile"
                      []
                      (swap! app-state assoc :page :profile))

  (hook-browser-navigation!))


(defmulti current-page #(@app-state :page))

(defmethod current-page :home []
  [home])

(defmethod current-page :profile []
  [profile])

(defmethod current-page :default []
  [:div ])
