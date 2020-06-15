(ns hfdp.strategy.game
  "Organizing and implementing the Game described at the end of the chapter as exercise.")

(defprotocol ICharacter
  (fight [this])
  (set-weapon [this w]))

(defmulti weapon :weapon?)

(defmethod weapon :knife
  [_]
  (println "Cutting with a KNIFE!"))

(defmethod weapon :axe
  [_]
  (println "Cutting with a AXE!"))

(defmethod weapon :sword
  [_]
  (println "Cutting with a SWORD!"))

(def Character
  {:fight (fn [this] (weapon this) this)
   :set-weapon (fn [this w] (assoc this :weapon? w))})

(defrecord Queen [weapon?])

(extend Queen
  ICharacter
  Character)

(-> (->Queen :knife)
    fight)

(defrecord King [weapon?])

(extend King
  ICharacter
  Character)

(-> (->King :axe)
    fight)


(defrecord Troll [weapon?])

(extend Troll
  ICharacter
  Character)

(-> (->Troll :sword)
    fight
    (set-weapon :axe)
    fight)
