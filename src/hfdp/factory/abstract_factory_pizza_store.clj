(ns hfdp.factory.abstract-factory-pizza-store)

;;; INGREDIENTS

(defprotocol IPizzaIngredientFactory
  (createDough [this])
  (createSauce [this])
  (createCheese [this])
  (createVeggies [this])
  (createPepperoni [this])
  (createClam [this]))

(defrecord ThinCrustDough [])
(defrecord MarinaraSauce [])
(defrecord MostardSauce [])
(defrecord ReggianoCheese [])
(defrecord Garlic [])
(defrecord Onion [])
(defrecord Mushroom [])
(defrecord RedPepper [])
(defrecord SlicedPepperoni [])
(defrecord FreshClam [])

(defrecord NYPizzaIngredientFactory []
  IPizzaIngredientFactory
  (createDough [this] (->ThinCrustDough))
  (createSauce [this] (->MarinaraSauce))
  (createCheese [this] (->ReggianoCheese))
  (createVeggies [this] [(->Garlic) (->Onion) (->Mushroom) (->RedPepper)])
  (createPepperoni [this] (->SlicedPepperoni))
  (createClam [this] (->FreshClam)))

(defrecord ChicagoPizzaIngredientFactory []
  IPizzaIngredientFactory
  (createDough [this] (->ThinCrustDough))
  (createSauce [this] (->MostardSauce))
  (createVeggies [this] [(->Garlic) (->Onion) (->Mushroom) (->RedPepper)]))

;;; notice that each ingredient is reused by different stores when possible.

;;; PIZZAS

(defprotocol IPizza
  (prepare [this])
  (bake [this])
  (cut [this])
  (box [this]))

;;; we removed the prepare default method, because now it needs to be implemented by the concrete class.
(def Pizza
  {:bake (fn [this] (println "Bake for 25 minutes at 350.") this)
   :cut (fn [this] (println "Cutting the pizza into diagonal slices") this)
   :box (fn [this] (println "Place pizza in official PizzaStore box") this)})

(defrecord CheesePizza [ingredientFactory])
(extend CheesePizza
  IPizza
  (assoc Pizza :prepare
         (fn [this]
           (println (str "Preparing " (:name this)))
           (let [ingredient-factory (:ingredientFactory this)]
             (-> this
                 (assoc :dough (createDough ingredient-factory))
                 (assoc :sauce (createSauce ingredient-factory))
                 (assoc :cheese (createCheese ingredient-factory))
                 (assoc :veggies (createVeggies ingredient-factory))
                 (assoc :pepperoni (createPepperoni ingredient-factory))
                 (assoc :clam (createClam ingredient-factory)))))))

(defrecord VeggiePizza [ingredientFactory])
(extend VeggiePizza
  IPizza
  (assoc Pizza :prepare
         (fn [this]
           (println (str "Preparing " (:name this)))
           (let [ingredient-factory (:ingredientFactory this)]
             (-> this
                 (assoc :dough (createDough ingredient-factory))
                 (assoc :sauce (createSauce ingredient-factory))
                 (assoc :veggies (createVeggies ingredient-factory)))))))

(defrecord ClamPizza [ingredientFactory])
(extend ClamPizza
  IPizza
  (assoc Pizza :prepare
         (fn [this]
           (println (str "Preparing " (:name this)))
           (let [ingredient-factory (:ingredientFactory this)]
                      (-> this
                          (assoc :dough (createDough ingredient-factory))
                          (assoc :sauce (createSauce ingredient-factory))
                          (assoc :cheese (createCheese ingredient-factory))
                          (assoc :clam (createClam ingredient-factory)))))))

;;; STORES

(defprotocol IPizzaStore
  (orderPizza [this type])
  (createPizza [this type]))

(def PizzaStore
  {:orderPizza (fn [this type]
                 (-> (createPizza this type)
                     (prepare)
                     (bake)
                     (cut)
                     (box)))})

;;; in this implementation, the method `createPizza` is responsible to dispatch to the correct type
;;; and create the instance.
(defrecord NYPizzaStore [])

(extend NYPizzaStore
  IPizzaStore
  (assoc PizzaStore :createPizza
         (fn [this type]
           (case type
             "cheese" (assoc (->CheesePizza (->NYPizzaIngredientFactory)) :name "Cheese Pizza New York Style")
             "clam" (assoc (->ClamPizza (->NYPizzaIngredientFactory)) :name "Clam Pizza New York Style")))))

;;; let's create a chicago store that only sells chicago veggie styled pizzas.
(defrecord ChicagoPizzaStore [])
(extend ChicagoPizzaStore
  IPizzaStore
  (assoc PizzaStore :createPizza
         (fn [this type]
           (case type
             "veggie" (assoc (->VeggiePizza (->ChicagoPizzaIngredientFactory)) :name "Veggie Pizza Chicago Style")))))

;;; ORDERS
(-> (->NYPizzaStore)
    (orderPizza "clam"))

(-> (->ChicagoPizzaStore)
    (orderPizza "veggie"))
