(ns hfdp.factory.simple-factory-pizza-store)

(defprotocol IPizza
  (prepare [this])
  (bake [this])
  (cut [this])
  (box [this]))

(defrecord CheesePizza []
  IPizza
  (prepare [this] "" this)
  (bake [this] "" this)
  (cut [this] "" this)
  (box [this] "" this))

(defrecord PepperoniPizza []
  IPizza
  (prepare [this] "" this)
  (bake [this] "" this)
  (cut [this] "" this)
  (box [this] "" this))

(defprotocol IFactory
  (createPizza [this type]))

(defrecord Factory []
  IFactory
  (createPizza [this type]
    (case type
      "cheese" (->CheesePizza)
      "pepperoni" (->PepperoniPizza))))

(defprotocol IPizzaStore
  (orderPizza [this type]))

(defrecord PizzaStore [factory]
  IPizzaStore
  (orderPizza [this type] (-> (createPizza factory type)
                              (prepare)
                              (bake)
                              (cut)
                              (box))))

(-> (->Factory)
    (->PizzaStore)
    (orderPizza "pepperoni")
    )


;;; if you want Nyc style pizza.. create a new factory.

(defrecord VeggiePizza []
  IPizza
  (prepare [this] "" this)
  (bake [this] "" this)
  (cut [this] "" this)
  (box [this] "" this))

(defrecord NYPizzaFactory []
  IFactory
  (createPizza [this type]
    (case type
      "veggie" (->VeggiePizza))))


(-> (->NYPizzaFactory)
    (->PizzaStore)
    (orderPizza "veggie")
    )
