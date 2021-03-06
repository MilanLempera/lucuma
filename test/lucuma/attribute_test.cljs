(ns lucuma.attribute-test
  (:require [cljs.test :refer-macros [deftest is]]
            [lucuma.attribute :as att])
  (:refer-clojure :exclude [get set]))

(deftest property->attribute
  (is (= nil (att/property->attribute nil)))
  (is (= nil (att/property->attribute "")))
  (is (= "string" (att/property->attribute "string")))
  (is (= "keyword" (att/property->attribute :keyword)))
  (is (= "lucuma.attribute-test/keyword" (att/property->attribute :lucuma.attribute-test/keyword)))
  (is (= "5.2" (att/property->attribute 5.2)))
  (is (= "" (att/property->attribute true)))
  (is (= nil (att/property->attribute false))))

(deftest attribute->property
  (is (= "string" (att/attribute->property [:string "string"])))
  (is (= nil (att/attribute->property [:string ""])))
  (is (= nil (att/attribute->property [:string nil])))
  (is (= :keyword (att/attribute->property [:keyword "keyword"])))
  (is (= :lucuma.attribute-test/keyword (att/attribute->property [:keyword "lucuma.attribute-test/keyword"])))
  (is (= nil (att/attribute->property [:keyword nil])))
  (is (= nil (att/attribute->property [:keyword ""])))
  (is (= 5.2 (att/attribute->property [:number "5.2"])))
  (is (= nil (att/attribute->property [:number nil])))
  (is (= true (att/attribute->property [:boolean "true"])))
  (is (= true (att/attribute->property [:boolean ""])))
  (is (= false (att/attribute->property [:boolean nil]))))

(defn create-element
  ([n] (create-element n {}))
  ([n m]
   (let [el (.createElement js/document n)]
     (doseq [kv m]
       (.setAttribute el (name (key kv)) (val kv)))
     el)))

(deftest attributes
  (is (= {:a "a" :b "b"} (att/attributes (create-element "div" {:a "a" :b "b"})))))

(deftest get
  (is (= "value" (att/get (create-element "div" {:key "value"}) :key :string)))
  (is (= true (att/get (create-element "div" {:key ""}) :key :boolean)))
  (is (= nil (att/get (create-element "div" {}) :unknown-key :string))))

(deftest set
  (let [el (create-element "div")]
    (att/set! el :key1 true)
    (att/set! el :key2 false)
    (att/set! el :key3 :keyword)
    (is (= true (att/get el :key1 :boolean)))
    (is (= false (att/get el :key2 :boolean)))
    (is (= :keyword (att/get el :key3 :keyword)))))
