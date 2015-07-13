(ns thunknyc.gcloud.storage
  (:require [thunknyc.gcloud.util :as util])
  (:import
   com.google.api.services.storage.Storage$Builder
   com.google.api.services.storage.StorageScopes))

(defn storage-client
  [cred app-name]
  (-> (Storage$Builder. util/trusted-transport
                        util/json-factory
                        (.createScoped cred (StorageScopes/all)))
      (.setApplicationName app-name)
      .build))

(defn bucket-metadata
  [client bucket-name]
  (-> client .buckets (.get bucket-name)))

(defn bucket-object-input-stream
  [client bucket-name object-name]
  (let [o (-> client
              .objects
              (.get bucket-name object-name))]
    (.executeMediaAsInputStream o)))

(defn bucket-object-metadata
  [client bucket-name object-name]
  (-> client .objects (.get bucket-name object-name) .execute))
