(ns crud-app.home
  (:require
    [reagent.core :as r]
    [antizer.reagent :as ant]
    [clojure.string :as s]
    [crud-app.helper :refer [uuid-generator avatar-color-generator]]
    [crud-app.db :refer [app-state]]
    ))

(defn home-page []
  [:div [:h2 "Employee management"]])

(def isEditClicked (r/atom false))


(defn handle-delete
  [id deleted?]
  (swap! app-state assoc :editing false)
    (if deleted? (ant/message-success "Successfully deleted"))
  (swap! app-state assoc :employees
         (vec (remove #(= (:id %) id) (:employees @app-state)))))

(defn handle-edit
  [employee-id]
  (swap! app-state assoc :current-employee (first (filter #(= (:id %) employee-id) (:employees @app-state))))
  (reset! isEditClicked true)
  )

(defn change-current-employee [value key]
  (swap! app-state assoc :current-employee (first (filter #(= (key %) value) (:employees @app-state))))
  )


(defn action-buttons-fn
  [employee-id]
  (r/as-element
    [:div
     [ant/button
      {:on-click #(handle-delete employee-id true)} "Delete"]
     [ant/button
      {:on-click #(handle-edit employee-id)} "Edit"]]))


(defn render-surname-fn
  [employee-surname]
  (r/as-element
    [ant/button
     {:on-click #(change-current-employee employee-surname :surname)
      :href "#/profile"
      :style {:border "none" :text-decoration "none" :box-shadow "none" :background-color "transparent"}
      } employee-surname])
  )


(defn render-name-fn
  [employee-name]
  (r/as-element
    [ant/button
     {:on-click #(change-current-employee employee-name :name)
      :href "#/profile"
      :style {:border "none" :text-decoration "none" :box-shadow "none" :background-color "transparent"}
      } employee-name])
  )

(def columns (r/atom [{:title "Name" :dataIndex "name" :key "name" :render render-name-fn}
                      {:title "Surname" :dataIndex "surname" :key "surname" :render render-surname-fn}
                      {:title "Action" :dataIndex "id" :key "action" :render action-buttons-fn
                       }]))


(defn input [value k margin-left]
  (fn []
    [:input
     {:value     (-> @app-state :current-employee k)
      :style     { :width "200px" :height "30px" :border-radius 5 :margin-left margin-left}
      :on-change #(swap! app-state assoc-in [:current-employee k] (-> % .-target .-value))}]))

(defn update-fn []
  (let [color (avatar-color-generator)]
  (handle-delete (:id (:current-employee @app-state)) false)
  (swap! app-state update :employees
         conj {:id (or (:id (:current-employee @app-state)) (uuid-generator))
               :avatar-color (or (:avatar-color (:current-employee @app-state)) (str "#" color))
               :name (:name (:current-employee @app-state))
               :surname (:surname (:current-employee @app-state))}
  )))

(defn submit [added?]
  (if added? (update-fn))
  (swap! app-state assoc :current-employee {})
  (if added? (ant/message-success "Successfully added!")))


(defn handle-add []
  [ant/button {:style    {:margin-left 10}
               :type     "primary"
               :on-click #(submit true)} (if (= @isEditClicked true)"Save" "Add")])

(defn handle-clear []
  [ant/button {:style    {:margin-left 10}
               :type     "danger"
               :on-click #(submit false)} "Clear"])

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

(defn render-table []
  [ant/table {
              :dataSource (:employees @app-state)
              :columns    @columns
              :pagination false
              :row-selection {:on-change
                              #(let [selected (js->clj %2 :keywordize-keys true)]
                                 (ant/message-info (str "You have selected: " (map :name selected))))}
              }]
  )


(defn home []
  [:<>
   [home-page]
   [render-inputs]
   [:div {:style {:margin-bottom 20}}
    [handle-add]
    [handle-clear]]
   [render-table]
    ])