(ns hfdp.template-method.ducks
  (:import [java.lang Comparable]))

(deftype Duck [name weight]
  Comparable
  (compareTo [this other] (cond
                            (> (.weight this) (.weight other)) 1
                            (< (.weight this) (.weight other)) -1
                            (= (.weight this) (.weight other)) 0)))

;;; test drive
(def ducks [(->Duck "Daffy" 8) (->Duck "Dewey" 2) (->Duck "Howard" 7) (->Duck "Louis" 2) (->Duck "Donald" 10) (->Duck "Huey" 2)])

;;; now we implemented only portion of the `sort` algorithm, the `compareTo` method which is necessary
;;; to sort the ducks...
(let [sts (sort ducks)]
  (doseq [it sts]
    (println (format "\n Duck name %s and weight %s" (.name it) (.weight it)))))
