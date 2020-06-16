(ns hfdp.observer.weather
  "The Observer Pattern defines a dependency one-to-many between objects
  in a way that when an object change its state, all the its dependents are
  notified and updated."
  (:refer-clojure :exclude [update]))

(defprotocol ISubject
  (notify-observers [this])
  (set-measurements [this temperature humidity pressure]))

(defrecord WeatherData [state]
  ISubject
  (notify-observers [this] (:state this))
  (set-measurements [this t h p] (doseq [ob @(:state this)]
                                   (update ob t h p))))

;;; The only way I could think of keeping track of the observers registered
;;; was using a mutable data structure to hold mutate the value as
;;; `weather-data` was passing around different observers.
(defn weather-data []
  (let [state (atom [])]
    (->WeatherData state)))

;;; displays
(defprotocol IDisplay
  (display [this])
  (update [this temperature humidity pressure]))

(defrecord CurrentConditionDisplay []
  IDisplay
  (display [this] (println (format "Current Condition: %dF degrees" (:temperature this))))
  (update [this t h p] (display (assoc this :temperature t :humidity h :pressure p))))

(defn current-condition-display
  "Constructor for CurrentConditionDisplay class."
  [weather-data]
  (let [ccd (->CurrentConditionDisplay)]
    (swap! (:state weather-data) conj ccd)
    ccd))

(defrecord ForecastDisplay []
  IDisplay
  (display [this] (println "Forecast: "))
  (update [this t h p] (display (assoc this :t t :h h :p p))))

(defn forecast-display
  "Constructor for ForecastDisplay class."
  [weather-data]
  (let [fd (->ForecastDisplay)]
    (swap! (:state weather-data) conj fd)
    fd))

(defrecord StatisticsDisplay []
  IDisplay
  (display [this] (println "Statistics Display: "))
  (update [this t h p] (display (assoc this :t t :h h :p p))))

(defn statistics-display
  "Constructor for StatisticsDisplay class."
  [weather-data]
  (let [sd (->StatisticsDisplay)]
    (swap! (:state weather-data) conj sd)
    sd))

;;; now if we want to add a new display, we just call it before the
;;; `set-measurements` functions below.
(defn weather-station []
  "Start informing data to registered observers."
  (let [wd (weather-data)]
    (current-condition-display wd)
    (forecast-display wd)
    (statistics-display wd)
    (set-measurements wd 80 65 30.4)
    (set-measurements wd 70 65 30.4)))

(comment
  (weather-station)
  )
