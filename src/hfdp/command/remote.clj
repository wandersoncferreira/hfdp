(ns hfdp.command.remote
  "Command Pattern: Encapsulate a REQUEST ACTION as an object, so you can parameterize
  other objects with different actions as a response to the common interface.")

(defprotocol Command
  (execute [this]))

(defprotocol ILight
  (on [this])
  (off [this]))

(defrecord Light []
  ILight
  (on [this] (println "Light is ON") this)
  (off [this] (println "Light is OFF") this))

(defrecord NoCommand []
  Command
  (execute [this] (println "No command configured in this slot")))

(defrecord LightOnCommand [light]
  Command
  (execute [this] (on (:light this))))

(defrecord LightOffCommand [light]
  Command
  (execute [this] (off (:light this))))

(defprotocol IStereo
  (on [this])
  (off [this])
  (setCD [this])
  (setVolume [this]))

(defrecord Stereo []
  IStereo
  (on [this] (println "Stereo ON!") this)
  (setCD [this] (println "Stereo CD set.") this)
  (off [this] (println "Stereo OFF!") this)
  (setVolume [this] (println "Stereo volume set.") this))

(defrecord StereoOnWithCDCommand [stereo]
  Command
  (execute [this] (-> (on (:stereo this))
                      (setCD)
                      (setVolume))))

(defrecord StereoOffWithCDCommand [stereo]
  Command
  (execute [this] (off (:stereo this))))

(defprotocol IRemoteControl
  (setCommand [this slot onCommand offCommand])
  (onButtonWasPushed [this slot])
  (offButtonWasPushed [this slot]))

(defrecord RemoteControl [on-commands off-commands]
  IRemoteControl
  (setCommand [this slot onCommand offCommand] (let [on-commands (assoc (:on-commands this) slot onCommand)
                                                     off-commands (assoc (:off-commands this) slot offCommand)]
                                                 (assoc this :on-commands on-commands :off-commands off-commands)))
  (onButtonWasPushed [this slot] (execute (get (:on-commands this) slot)))
  (offButtonWasPushed [this slot] (execute (get (:off-commands this) slot))))

(defn make-remote-control []
  (let [no-commands (apply hash-map (interleave (range 1 8) (repeatedly 7 #(NoCommand.))))]
    (->RemoteControl no-commands no-commands)))


;;; remote control test
(let [remote-control (make-remote-control)
      stereo (->Stereo)
      light (->Light)
      remote-control-configured (-> remote-control
                                    (setCommand 2 (->StereoOnWithCDCommand stereo) (->StereoOffWithCDCommand stereo))
                                    (setCommand 1 (->LightOnCommand light) (->LightOffCommand light)))]
  (onButtonWasPushed remote-control-configured 1)
  (onButtonWasPushed remote-control-configured 2))

;;; in this implementation, for the remote control the only thing that matters is the `execute` method
;;; however, each command encapsulate an object that provides the particular meaning to `execute`.
;;; For example, to the the action: "Turn the lights on" you have a LightOnCommand that implements a call to `on` operation in the `Light` object
;;; But, to the action "Turn the stereo on" you have a StereoOnCommand that implements a call to `setOn`, `setCD`, and `setVolume` operations in the `Stereo` object
;;; to the remote control concerns, they are equivalent.
