(ns hfdp.decorator.starbuzz
  "Decorator Pattern: Attach additional responsibilities to an object dynamically."
  (:require [hfdp.decorator.beverage :as bev :refer [getDescription cost]]
            [hfdp.decorator.condiments :as con]))


(-> (bev/->HouseBlend)
    (con/->Mocha)
    (con/->Soy)
    (con/->Mocha)
    cost
    )

(cost (bev/->Expresso))

