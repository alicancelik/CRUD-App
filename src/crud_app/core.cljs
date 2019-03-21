(ns crud-app.core
  (:require [reagent.core :as r]
            [crud-app.routes :refer [app-routes current-page]]))

(defn app
  []
  (app-routes)
  (r/render [current-page] (.getElementById js/document "app")))


(defn init!
  []
  (app)) []