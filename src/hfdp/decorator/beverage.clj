(ns hfdp.decorator.beverage)

(defprotocol Beverage
  (getDescription [this])
  (cost [this]))

(defrecord HouseBlend []
  Beverage
  (getDescription [this] "House Blend Coffe")
  (cost [this] 0.89))

(defrecord Expresso []
  Beverage
  (getDescription [this] "Expresso")
  (cost [this] 1.99))

(defrecord DarkRoast []
  Beverage
  (getDescription [this] "Dark Roast")
  (cost [this] 0.99))

(defrecord Decaf []
  Beverage
  (getDescription [this] "Decaf")
  (cost [this] 1.05))
