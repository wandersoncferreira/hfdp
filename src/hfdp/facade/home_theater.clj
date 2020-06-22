(ns hfdp.facade.home-theater
  "The Facade Pattern: Provide an unified interface to simplify common operations
  on top of a collection of interfaces inside a subsystem.")

(defprotocol ICdPlayer
  (on [this])
  (off [this])
  )

(defrecord CdPlayer []
  ICdPlayer
  (on [this])
  (off [this]))

(defprotocol IHomeTheaterFacade
  (watchMovie [this])
  (endMovie [this])
  (listenToCd [this])
  (endCd [this])
  (listenToRadio [this])
  (endRadio [this]))

(defprotocol ILight
  (off [this])
  (dim [this]))

(defprotocol IScreen
  (up [this])
  (down [this]))

(defprotocol IProjector
  (wideScreenMode [this]))

(defrecord HomeTheaterFacade [popper lights screen projector]
  IHomeTheaterFacade
  (watchMovie [this] (println "Get ready to watch a movie...")
    (on popper)
    (pop popper)
    (dim lights)
    (down screen)
    (-> projector
        (wideScreenMode)))
  (endMovie [this] (println "Shutting movie theater down...")
    (off popper)
    (up screen)))
