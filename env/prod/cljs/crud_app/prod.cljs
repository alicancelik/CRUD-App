(ns crud-app.prod
  (:require
    [crud-app.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
