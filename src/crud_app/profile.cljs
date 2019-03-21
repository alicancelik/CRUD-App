(ns crud-app.profile
  (:require
    [reagent.core :as r]
    [antizer.reagent :as ant]
    [clojure.string :as s]
    [crud-app.db :refer [app-state]]
    [crud-app.home :refer [submit]]
    [crud-app.actions :refer [get-data]]
    ))

(def change-button-clicked (r/atom false))
(def source-avatar (r/atom ""))
(def source-giphy (r/atom ""))
(def modal-visible (r/atom false))


(defn render-avatar []
  (fn []
  (r/as-element
    [ant/avatar {
                 :style {:background-color (:avatar-color (:current-employee @app-state))}
                 :class "va-middle"
                 :src @source-avatar
                 :shape "square"
                 :size  128
                 } (subs (clojure.string/upper-case (:name (:current-employee @app-state))) 0 1)])))

(defn change-condition []
  (get-data)
  (println @change-button-clicked)
  (if @change-button-clicked (reset! change-button-clicked false)
                             (reset! change-button-clicked true)))


(defn render-info-button []
  (fn []
    (r/as-element
      [ant/button {
                   :on-click change-condition
                   :type     "ghost"
                   :style    {:margin-top 10}
                   } "Change Info"])))


(defn input [value k margin-left]
  (fn []
    [:input {:value     (-> @app-state :current-employee k)
             :style     {:width "200px" :height "30px" :border-radius 5 :margin-right 50 :margin-top 20 :margin-left margin-left}
             :on-change #(swap! app-state assoc-in [:current-employee k] (-> % .-target .-value))}]))

(defn handle-change-name []
  (fn []
    [:div
     [:p "Name: " [input (:name {:current-employee @app-state}) :name 20]]]))


(defn handle-change-surname []
  (fn []
    [:div
     [:p "Surname: " [input (:surname {:current-employee @app-state}) :surname 0]]]))

(defn render-inputs []
  [:<>
   [handle-change-name]
   [handle-change-surname]
   ])

(defn change-avatar [title]
  (fn []
    (case
      title "The Matrix" ((reset! source-giphy "https://media.giphy.com/media/zhJR6HbK4fthC/giphy.gif")
                           (reset! source-avatar "https://upload.wikimedia.org/wikipedia/en/thumb/c/c6/NeoTheMatrix.jpg/220px-NeoTheMatrix.jpg"))
            "Inception" ((reset! source-giphy "https://media.giphy.com/media/z1meXneq0oUh2/giphy.gif")
                          (reset! source-avatar "https://vignette.wikia.nocookie.net/inception/images/2/2b/Dom_cobb.jpg/revision/latest?cb=20150125061140"))
            "Back to the Future" ((reset! source-giphy "https://media.giphy.com/media/zZeCRfPyXi9UI/giphy.gif")
                                   (reset! source-avatar "https://upload.wikimedia.org/wikipedia/en/thumb/d/db/Back_to_the_Future_%28time_travel_test%29_with_Michael_J._Fox_as_Marty_McFly.jpg/250px-Back_to_the_Future_%28time_travel_test%29_with_Michael_J._Fox_as_Marty_McFly.jpg"))
            "Star Wars" ((reset! source-giphy "https://media.giphy.com/media/l0K4k1O7RJSghST3a/giphy.gif") (reset! source-avatar "https://localtvkdvr.files.wordpress.com/2012/10/darth.jpg?quality=85&strip=all&w=400"))
            "Interstellar" ((reset! source-giphy "https://media1.giphy.com/media/z01pLEplVpc5O/giphy.gif")
                             (reset! source-avatar "https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg"))
            )))

(defn render-modal-fn []
    (fn []
      [ant/modal {
                  :visible @modal-visible
                  :title "CIKISS YAPIYORSUNNN"
                  :on-ok #(reset! modal-visible false)
                  :on-cancel #(reset! modal-visible false)
                  }
       [:img {
              :style {:width "100%" :height "100%"}
              :src "http://c12.incisozluk.com.tr/res/incisozluk//11508/4/3194194_o013c.jpg\n"
              }]
       ]
    )
  )


(defn favorite-movies []
  (fn []
    [:<>
     [:p [:h2 "Click to Choose Your Side"]]
     [:ul
      (doall
       (for [movie (:movies @app-state)]
        [:li {:key (:title movie)}
         [ant/button {:on-click (change-avatar (:title movie))
                      :style {:border "none"
                              :text-decoration "none"
                              :box-shadow "none"
                              :background-color "transparent"}
                      } (str (:title movie))]])
    )]]))


(defn cikis-yap-fn []
  [ant/button {
               :type "primary"
               :on-click #(reset! modal-visible true)} "CIKIS YAP"])


(defn update-employee []
  (fn []
    (r/as-element
      [ant/button {
                   :on-click #(submit true)
                   :href     "#/"
                   :type     "primary"
                   :style    {:margin-top 20 :margin-bottom 20}
                   } "Update"])))


(defn profile []
  (get-data)
     (fn []
       [:<>
         [:div
          {:style {:background-color (:avatar-color (:current-employee @app-state))
                :display          "flex"
                :justify-content  "center" :vertical-align   "center"}}
           [:p [:h1 {:style {:margin-top 20 :color "white"}} "Profile"]]
         ]
        [:div {:style {
                       :display "flex"
                        :justify-content "center"
                       }}
         [:div {:style {:display         "flex"
                    :justify-content "center"
                    :flex-direction  "column"
                    :vertical-align  "center"
                    :align-items     "center"
                    :margin-top      20
                    }}
      [render-avatar]
      [:div [:h2 (:name (:current-employee @app-state))]]
      [render-info-button]
      [:div (when @change-button-clicked
              [render-inputs])]
      [update-employee]
      [favorite-movies]
      [cikis-yap-fn]
       [render-modal-fn]
          ]
         [:div {:style {:height "100%" :margin-left 20}}
          [:img {:style {:float "right"
                         :position "absolute"
                         :margin-top 20}
                 :src @source-giphy}]
          ]
         ]
     ]))