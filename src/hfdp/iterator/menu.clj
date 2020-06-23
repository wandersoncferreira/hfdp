(ns hfdp.iterator.menu
  "Iterator Pattern: Provides a way to access elements of an aggregated object sequentially
  without exposing its internal representation."
  (:refer-clojure :exclude [next]))

(defrecord MenuItem [name description vegetarian price])

(defprotocol IMenu
  (addItem [this name description vegetarian price])
  (createIterator [this]))

(defprotocol Iterator
  (hasNext [this])
  (next [this]))

(defrecord DinerMenuIterator [menu-items state_]
  Iterator
  (hasNext [this] (let [position @state_]
                    (if (> position (count menu-items))
                      false
                      true)))
  (next [this] (let [menu (get menu-items @state_)]
                 (swap! state_ inc)
                 menu)))

(defrecord PancakeHouseIterator [menu-items state_]
  Iterator
  (hasNext [this] (if (>= @state_ (count menu-items))
                    false
                    true))
  (next [this] (let [menu (nth menu-items @state_)]
                 (swap! state_ inc)
                 menu)))

(defrecord PancakeHouseMenu []
  IMenu
  (addItem [this name description vegetarian price] (update this :menu-items conj (->MenuItem name description vegetarian price)))
  (createIterator [this] (->PancakeHouseIterator (:menu-items this) (atom 0))))

(defn make-pancake-house []
  (let [pancake (->PancakeHouseMenu)]
    (-> pancake
        (addItem "Waffles" "Waffes with your choice of blueberries or strawberries" true 3.59)
        (addItem "Blueberry Pancakes" "Pancakes made with fresh blueberries" true 3.49))))

(defrecord DinerMenu [max-items]
  IMenu
  (addItem [this name description vegetarian price]
    (let [menu (or (:menu-items this) {})
          number-of-items (count menu)]
      (if (>= number-of-items max-items)
        (println "Sorry, menu is full! Can't add item to menu!")
        (assoc this :menu-items
               (assoc menu (+ 1 number-of-items) (->MenuItem name description vegetarian price))))))
  (createIterator [this] (let [position (atom 1)]
                           (->DinerMenuIterator (:menu-items this) position))))

(defn make-diner-menu []
  (let [MAX_ITEMS 6
        diner (->DinerMenu MAX_ITEMS)]
    (-> diner
        (addItem "Vegetarian BLT" "(Fakin') Bacon with lettuce & tomato" true 2.99)
        (addItem "BLT" "Bacon with lettuce & tomato on whole wheat" false 2.99))))

(defprotocol IWaitress
  (printMenu [this])
  (printMenuIter [this iterator]))

(defrecord Waitress [diner-menu pancake-menu]
  IWaitress
  (printMenu [this] (let [diner-iter (createIterator diner-menu)
                          pancake-iter (createIterator pancake-menu)]
                      (println "MENU\n----- \nBREAKFAST")
                      (printMenuIter this diner-iter)
                      (println "\nLUNCH")
                      (printMenuIter this pancake-iter)))
  (printMenuIter [this iter] (while (hasNext iter)
                               (let [menu (next iter)]
                                 (print (:name menu) ",  ")
                                 (print (:price menu) " -- ")
                                 (println (:description menu))))))

(printMenu (->Waitress (make-diner-menu) (make-pancake-house)))
