(ns thunknyc.gcloud.util
  (:import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
           com.google.api.client.json.jackson2.JacksonFactory))

(def trusted-transport (GoogleNetHttpTransport/newTrustedTransport))
(def json-factory (JacksonFactory/getDefaultInstance))
