(ns thunknyc.gcloud.dns
  (:require [thunknyc.gcloud.util :as util])
  (:import
   com.google.api.services.dns.Dns$Builder
   com.google.api.services.dns.DnsScopes
   com.google.api.services.dns.model.Change
   com.google.api.services.dns.model.ResourceRecordSet))

(defn dns-client
  [cred app-name]
  (-> (Dns$Builder. util/trusted-transport
                    util/json-factory
                    (.createScoped cred (DnsScopes/all)))
      (.setApplicationName app-name)
      .build))

(defn resource-record-sets [client project zone]
  (-> (.resourceRecordSets client)
      (.list project zone)
      .execute
      (get "rrsets")))

(defn change [client project zone change]
  (-> (.changes client)
      (.create project zone change)
      .execute))

(defn add [client project zone rrsets]
  (change client project zone (-> (Change.) (.setAdditions (into [] rrsets)))))

(defn add-one [client project zone rrset]
  (add client project zone (conj [] rrset)))

(defn delete [client project zone rrsets]
  (change client project zone (-> (Change.) (.setDeletions (into [] rrsets)))))

(defn delete-one [client project zone rrset]
  (delete client project zone (conj [] rrset)))

(defn record-filter*
  ([rss pred]
   (filter pred rss))
  ([rss pred k v & more]
   (apply record-filter* rss #(and (pred %) (= (get % (name k)) v)) more)))

(defn record-filter
  ([rss] rss)
  ([rss & more]
   (apply record-filter* rss (constantly true) more)))

(defn map->resource-record-set [col]
  (-> (ResourceRecordSet.)
      (.setKind "dns#resourceRecordSet")
      (.setName (:name col))
      (.setRrdatas (:rrdatas col))
      (.setType (:type col))
      (.setTtl (Integer. (:ttl col)))))
