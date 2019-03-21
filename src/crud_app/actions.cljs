
(ns crud-app.actions
  (:require [ajax.core :refer [GET]]
            [crud-app.db :refer [app-state api-url]]))

(defn handler [response]
  (swap! app-state assoc :movies (:movies response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "Something bad happened: " status " " status-text)))


(defn get-data
  ([]
   (GET api-url {
                 :handler handler
                 :error-handler error-handler
                 :response-format :json
                 :keywords? true})))