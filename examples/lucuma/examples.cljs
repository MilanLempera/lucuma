(ns lucuma.examples
  (:require [lucuma.custom-elements :refer [get-chan register render-content render-style]]
            [lucuma.range-with-threshold :refer [lucu-range-with-threshold]]
            [lucuma.overlay :refer [lucu-overlay]]
            [lucuma.flipbox :refer [lucu-flipbox]]
            [dommy.core :refer [prepend!]]
            [garden.core :refer [css]]
            [cljs.core.async :as async :refer [<!]])
  (:require-macros [lucuma :refer [defwebcomponent]]
                   [dommy.macros :refer [node sel1]]
                   [cljs.core.async.macros :refer [go]]))

(defwebcomponent ex-hello
  :content "Hello world!")

(defwebcomponent ex-lifecycle
  :created-fn #(prepend! (sel1 :#lifecycle-events) [:li "element created"])
  :entered-view-fn #(prepend! (sel1 :#lifecycle-events) [:li "element entered view"])
  :left-view-fn #(prepend! (sel1 :#lifecycle-events) [:li "element left view"]))

(defwebcomponent ex-content-template
  :content #(sel1 :#template-id))

(derive PersistentVector ::vector)
(defmethod render-content ::vector [v] (node v))

(defwebcomponent ex-content-hiccup
  :content [:div "Hello hiccup!"])

(defwebcomponent ex-style
  :content [:button "Hello styled!"]
  :style "button { background: blue; color: white; border: 0; border-radius: 4px;}")

(defmethod render-style ::vector [v] (css v))

(defwebcomponent ex-style-garden
  :content [:button "Hello garden!"]
  :style [:button {:background "#3d7c45" :color "white" :border 0 :border-radius "4px"}])

(defwebcomponent ex-style-scope
  :content [:span {:class "icon-exclamation-sign"} "Hello style scope!"]
  :apply-author-styles true)

(defn ^:export delegate-attributes
  [el f]
  (let [c (get-chan el)]
    (go (while true
          (let [u (<! c)]
            (.log js/console u))))))

(defwebcomponent ex-attributes
  :attributes #{:attribute})

(defn alert-fn
  [el]
  (.alert js/window  (str "Hello methods from '" (.-id el) "' !")))

(defwebcomponent ex-methods
  :methods {:alert alert-fn})

(defwebcomponent ex-constructor
  :constructor "MyConstructor"
  :content [:div "Hello constructor!"])

(defwebcomponent ex-extend
  :base-type "button"
  :content [:span {:class "icon-exclamation-sign"} [:content]]
  :style [:button {:color "red"}]
  :apply-author-styles true)

(defn ^:export register-all
  []
  (register ex-hello)
  (register ex-lifecycle)
  (register ex-content-template)
  (register ex-content-hiccup)
  (register ex-style)
  (register ex-style-garden)
  (register ex-style-scope)
  (register ex-extend)
  (register ex-attributes)
  (register ex-methods)
  (register ex-constructor)

  (register lucu-range-with-threshold)
  (register lucu-overlay)
  (register lucu-flipbox))
