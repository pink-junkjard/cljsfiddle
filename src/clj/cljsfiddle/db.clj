(ns cljsfiddle.db
  (:refer-clojure :exclude (drop))
  (:require [clojure.java.jdbc :as sql]
            [environ.core :refer (env)]))

(defn create []
  (sql/with-connection (env :database-url)
    (sql/create-table :fiddles
      [:ns :varchar "PRIMARY KEY"]
      [:cljs :text "NOT NULL"]
      [:html :text]
      [:css :text]
      [:created :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn drop []
  (sql/with-connection (env :database-url)
    (sql/drop-table :fiddles)))

(defn upsert [fiddle]
  (let [ns (:ns fiddle)]
    (sql/with-connection (env :database-url)
      (sql/update-or-insert-values :fiddles ["ns = ?" ns] fiddle))))

(defn find-by-ns [ns]
  (first
   (sql/with-connection (env :database-url)
     (sql/with-query-results results
       ["SELECT * FROM fiddles WHERE ns = ?" ns]
       (into [] results)))))

(find-by-ns "jonase.bezier")
