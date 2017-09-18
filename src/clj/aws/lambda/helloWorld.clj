(ns aws.lambda.helloWorld
  (:require [clojure.data.json :as json]
            [clojure.string :as s]
            [clojure.java.io :as io]
            [jdbc.pool.c3p0 :as pool]

            [aws.lambda.db :as db]
            [aws.lambda.env :as env])
  (:gen-class
   :init init
   :constructors {[] []}   
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))

(def db-conn (atom nil))

(defn key->keyword [key-string]
  (-> key-string
      (s/replace #"([a-z])([A-Z])" "$1-$2")
      (s/replace #"([A-Z]+)([A-Z])" "$1-$2")
      (s/lower-case)
      (keyword)))

;; init gets a DB-Password set in Lambda Environment and Creates a DB Pool
;; Pool adds efficiency by reusing connections in container init
;; Please prolong container life by setting a cloudwatch so you don't recreate container often
;; DB Pool is very efficient in small traffic bursts where less containers are spawned
;; Taking performance hit once is better than opening a new connection for every request

(defn -init
  ;; matches empty constructor
  ([][[]
      (let [db-pass (env/fn-get-value-from-env "db_password")
            min-pool-size (-> (env/fn-get-value-from-env "min_pool_size")
                              Integer/parseInt
                              long)
            max-pool-size (-> (env/fn-get-value-from-env "max_pool_size")
                              Integer/parseInt
                              long)
            db-spec (db/db-spec db-pass min-pool-size max-pool-size)]
        (do (prn "Lambda Container Setup")
            (prn "Min Pool Size: " min-pool-size)
            (prn "Max Pool Size: " max-pool-size)
            ;; Create a DB Pool Object
            (swap! db-conn (constantly (pool/make-datasource-spec db-spec)))
            ;; Create a Test Query which initiates a pool
            (db/fn-test-query @db-conn)))]))

(defn handle-request [request-map]
  (let [sz-input (prn-str request-map)]
  (do
    (println "Process Request: " request-map))
  ;; some response
  {:return 0
   :input request-map
   :pool-size (System/getenv "min_pool_size")
   :db-connections (db/fn-get-connection-ips @db-conn)
   :message "Hello World"}))


;; implements interface RequestStreamHandler
;; [see](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/RequestStreamHandler.java)
(defn -handleRequest [this is os context]
  (let [w (io/writer os)]
    (->
     ;; parse input to clojure map
     (json/read (io/reader is) :key-fn key->keyword)
     ;; pass request to handle-request
     (handle-request)
     ;; pass return of handle-request to json writer
     (json/write w))
    ;; flush output stream
    (.flush w)))
