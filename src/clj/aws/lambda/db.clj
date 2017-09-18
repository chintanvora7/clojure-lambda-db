(ns aws.lambda.db
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as ql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//hackathon.crojfd1bljsr.us-east-1.rds.amazonaws.com:5432/hackathon"
   :user "hackathon"
   :password ""
   :min-pool-size 3
   :max-pool-size 5
   })

(defn fn-get-connection-info [pg-db-conn]
  "Dependency: A Post-Gres Connection
   Description: Given a postgres DB connection, retrieves information regarding active connections"
  (let [stmt (-> (ql/select :*)
                 (ql/from :pg_stat_activity)
                 sql/format)
        result (jdbc/query pg-db-conn stmt)]
    result))

(defn fn-get-connection-ips [db-conn]
  "Dependency: Post-Gres Connection, fn-get-connection-info
   Description: Given a DB conn, retrieves active an array of IPs"
  ;;output ["108.28.118.118", "108.28.118.119"]
  (->> (map #(:client_addr %) (fn-get-connection-info db-conn))
       (filter identity ,,,)
       (map #(.getValue %) ,,,)
       (into [] ,,,)))

(defn fn-test-query [db-conn]
  "Dependency: Post-Gres Connection
   Description: Given a DB conn, tries a test query. Useful for creating pool"
  (let [stmt (-> (ql/select "1")
                 sql/format)
        result (jdbc/query db-conn stmt)]
    result))