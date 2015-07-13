(ns thunknyc.gcloud.auth
  (:import
   com.google.api.client.googleapis.auth.oauth2.GoogleCredential))

(defn credential [] (-> (GoogleCredential/getApplicationDefault)))
