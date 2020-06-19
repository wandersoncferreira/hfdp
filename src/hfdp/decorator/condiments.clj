(ns hfdp.decorator.condiments
  (:require [hfdp.decorator.beverage :as bev]))

(defprotocol CondimentDecorator
  (getDescription [this]))

(defrecord Mocha [beverage])

(extend-type Mocha
  CondimentDecorator
  (getDescription [this] (str (bev/getDescription (:beverage this)) ", Mocha"))

  bev/Beverage
  (getDescription [this] (getDescription this))
  (cost [this] (+ 0.20 (bev/cost (:beverage this)))))

(defrecord Whip [beverage])

(extend-type Whip
  CondimentDecorator
  (getDescription [this] (str (bev/getDescription (:beverage this)) ", Whip"))

  bev/Beverage
  (getDescription [this] (getDescription this))
  (cost [this] (+ 0.10 (bev/cost (:beverage this)))))


(defrecord Soy [beverage])

(extend-type Soy
  CondimentDecorator
  (getDescription [this] (str (bev/getDescription (:beverage this)) ", Soy"))

  bev/Beverage
  (getDescription [this] (getDescription this))
  (cost [this] (+ 0.15 (bev/cost (:beverage this)))))


;;; the protocol implementation had to be dynamic because I could not
;;; implement two protocols with the same method name inline
;;; there is a ticket open at clojure core because of that:
;;; https://clojure.atlassian.net/browse/CLJ-1625
