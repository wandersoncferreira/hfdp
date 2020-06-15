(ns hfdp.strategy.game
  "Organizing and implementing the Game described at the end of the chapter as exercise.")

(defprotocol ICharacter
  (fight [this]))

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
  {:fight (fn [this] (weapon this) this)})

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
    fight)
