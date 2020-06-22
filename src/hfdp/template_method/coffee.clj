(ns hfdp.template-method.coffee
  "Template Method Pattern: The sub classes decide which portion of the algorithms must be implemented.")


(defprotocol ICaffeineBeverage
  (prepareRecipe [this])
  (boilWater [this])
  (brew [this])
  (addCondiments [this])
  (pourInCup [this]))

(def CaffeineBeverage
  {:boilWater (fn [this] (println "Boiling Water") this)
   :prepareRecipe (fn [this] (-> this
                                 (boilWater)
                                 (brew)
                                 (pourInCup)
                                 (addCondiments)))
   :pourInCup (fn [this] (println "Pouring into cup") this)})

(defrecord Coffee [])

(extend Coffee
  ICaffeineBeverage
  (assoc CaffeineBeverage
         :brew (fn [this] (println "Dripping Coffee through filter") this)
         :addCondiments (fn [this] (println "Adding sugar and milk") this)))

(defrecord Tea [])

(extend Tea
  ICaffeineBeverage
  (assoc CaffeineBeverage
         :brew (fn [this] (println "Steeping the tea") this)
         :addCondiments (fn [this] (println "Adding Lemon") this)))


;;; test drive
(let [tea (->Tea)
      coffee (->Coffee)]
  (println "Making tea...")
  (prepareRecipe tea)

  (println "Making coffee...")
  (prepareRecipe coffee))
