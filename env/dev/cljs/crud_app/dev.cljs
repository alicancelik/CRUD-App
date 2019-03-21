(ns ^:figwheel-no-load crud-app.dev
  (:require
    [crud-app.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
