(ns hfdp.adapter.duck
  "Adapter Pattern: Convert the interface of one class to another interface
  that the client expects to find. This pattern allows that classes
  with incompatible interfaces work together.")

(defprotocol IDuck
  (quack [this])
  (fly [this]))

(defrecord MallardDuck []
  IDuck
  (quack [this] (println "Quack") this)
  (fly [this] (println "I'm flying") this))

(defprotocol ITurkey
  (gobble [this])
  (fly [this]))

(defrecord WildTurkey []
  ITurkey
  (gobble [this] (println "Gobble gobble") this)
  (fly [this] (println "I'm flying a short distance") this))

(defrecord TurkeyAdapter [turkey]
  IDuck
  (quack [this] (gobble (:turkey this)))
  (fly [this] (dotimes [_ 5]
                (fly (:turkey this)))))


;;; duck test drive
(let [duck (->MallardDuck)
      wild-turkey (->WildTurkey)]
  (quack duck)
  (quack (->TurkeyAdapter wild-turkey)))
