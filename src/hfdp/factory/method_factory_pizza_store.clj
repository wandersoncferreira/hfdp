(ns hfdp.factory.method-factory-pizza-store
  "The Factory Method defines an interface to create objects, but allow to the class defer the
  decision of what should be instantiate to the sub-classes.")

(defprotocol IPizza
  (prepare [this])
  (bake [this])
  (cut [this])
  (box [this]))

(def Pizza
  {:prepare (fn [this]
              (println (str "Preparing " (:name this)))
              (println "Tossing dough...")
              (println "Adding sauce...")
              (println "Adding toppings: ")
              (doseq [it (:toppings this)]
                (println (str "  " it)))
              this)
   :bake (fn [this] (println "Bake for 25 minutes at 350.") this)
   :cut (fn [this] (println "Cutting the pizza into diagonal slices") this)
   :box (fn [this] (println "Place pizza in official PizzaStore box") this)})


;;; the only thing you are allowed to change is the parameters in the new style of pizza
;;; name, dough, sauce, and toppings.
(defrecord NYStyleCheesePizza [name dough sauce toppings])


;;; however in close this is the only way *I* know of reusing default implementations in a "base class"
(extend NYStyleCheesePizza
  IPizza
  Pizza)

(defn make-ny-style-cheese-pizza []
  (->NYStyleCheesePizza "NY Style Sauce and Cheese Pizza"
                        "Thin Crust Dough"
                        "Marinara Sauce"
                        ["Grated Reggiano Cheese"]))


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

;;; for a new franchise in new york you only need to inform which pizzas are you going to seel
(defrecord NYPizzaStore [])

(extend NYPizzaStore
  IPizzaStore
  (assoc PizzaStore :createPizza
         (fn [this type]
           (case type
             "cheese" (make-ny-style-cheese-pizza)))))



(-> (->NYPizzaStore)
    (orderPizza "cheese"))

;;; let's create a chicago store that only sells chicago veggie styled pizzas.
(declare make-chicago-style-veggie-pizza)

(defrecord ChicagoPizzaStore [])
(extend ChicagoPizzaStore
  IPizzaStore
  (assoc PizzaStore :createPizza
         (fn [this type]
           (case type
             "veggie" (make-chicago-style-veggie-pizza)))))

(defrecord ChicagoStyleVeggiePizza [name dough sauce toppings])

(extend ChicagoStyleVeggiePizza
  IPizza
  Pizza)

(defn make-chicago-style-veggie-pizza []
  (->ChicagoStyleVeggiePizza "Chicago Style Deep Dish Veggie Pizza"
                             "Extra Thick Crust Dough"
                             "Plum Tomato Sauce"
                             ["Shredded Mozzarella Cheese"]))

(-> (->ChicagoPizzaStore)
    (orderPizza "veggie"))
