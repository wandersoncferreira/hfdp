(ns hfdp.singleton.chocolate)

(defprotocol IChocolate
  (fill [this])
  (drain [this])
  (boil [this])
  (isEmpty [this])
  (isBoiled [this]))

(def Chocolate
  {:fill (fn [this] (if (isEmpty this)
                      (assoc this
                             :empty false
                             :boiled false)
                      this))
   :drain (fn [this] (if (and (not (isEmpty this)) (isBoiled this))
                       (assoc this :empty true)
                       this))
   :boil (fn [this] (if (and (not (isEmpty this)) (not (isBoiled this)))
                      (assoc this :boiled true)
                      this))
   :isEmpty (fn [this] (:empty this))
   :isBoiled (fn [this] (:boiled this))})

(defrecord ChocolateBoiler [empty boiled])

;;; the protocol implementation had to be dynamic because I could not
;;; implement two protocols with the same method name inline
;;; and all records already have isEmpty implemented
;;; there is a ticket open at clojure core because of that:
;;; https://clojure.atlassian.net/browse/CLJ-1625
(extend ChocolateBoiler
  IChocolate
  Chocolate)

(def singleton-boiler (atom nil))

(defn make-chocolate-boiler []
  (when-not @singleton-boiler
    (reset! singleton-boiler (->ChocolateBoiler true false)))
  @singleton-boiler)

(make-chocolate-boiler)
