(ns hfdp.strategy.duck
  "The strategy pattern defines an algorithm family
  and makes them interchangeable.")

;;; Premises
;;; I will use `defrecords` and `protocols` because I want to group related
;;; behaviors/actions with its specific object.
;;; and `multimethods` to model the behavior also called algorithms by the definition.

(defprotocol IDuck
  (performQuack [this])
  (performFly [this]))

(defmulti quack :quack?)
(defmulti fly :fly?)

(defmethod fly :with-wings
  [_]
  (println "I'm flying!"))

(defmethod fly :no-way
  [_]
  (println "I can't fly!"))

(defmethod quack :quack
  [_]
  (println "Quack"))

(defmethod quack :mute
  [_]
  (println "<< silence >."))

;;; this would be concrete implementations in the base class.
(def Duck
  {:performQuack (fn [this] (quack this) this)
   :performFly (fn [this] (fly this) this)})


;;; now, create a new kind of duck and extend the base class.
(defrecord MallardDuck [fly? quack?])
(extend MallardDuck
  IDuck
  Duck)

(-> (->MallardDuck :with-wings :quack)
    performQuack
    performFly)

;;; change the behavior in run time!
(-> (->MallardDuck :with-wings :quack)
    performFly
    (assoc :fly? :no-way)
    performFly
    performQuack
    (assoc :quack? :mute)
    performQuack)

;;; you can also only override specific methods from the base class
(extend MallardDuck
  IDuck
  (assoc Duck :performQuack (fn [this] (println "NEW-QUACK") this)))

(-> (->MallardDuck :no-way :mute)
    performQuack
    performFly)
