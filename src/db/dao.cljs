(ns db.dao
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :refer [chan <! sub]]
   [db.idx :as idxdb]))

;; gubbins start
(def ^:dynamic db nil)
(defn save-db [{db-obj :db}]
  (println "DB bootstrapped!")
  (set! db db-obj))

(def store-txs "transactions")
(def store-tags "tags")

(defn db-schema [db-ref]
  (let [
        txstore (idxdb/delete-and-create-store db-ref store-txs {:auto-increment true})
        tagstore (idxdb/delete-and-create-store db-ref store-tags {:auto-increment true})]

    ;; create indexes
    ;;(idxdb/create-index "dateIdx" "date" {:unique false})
    ;;(idxdb/create-index "descriptionIdx" "description" {:unique false})
    ;;(idxdb/create-index "amountIdx" "amount" {:unique false})
    ))

(defn error-prn [& args] (println "Error: " args))

(defn init-db []
  (let [
        [_ ch] (idxdb/request-db "fin" 1 db-schema)
        success-chan (sub ch :success (chan))
        error-chan (sub ch :error (chan))]
    (go (-> (<! success-chan) save-db))
    (go (-> (<! error-chan) error-prn))))

(init-db)

;; gubbins end

(defn put [payload fn]
    (idxdb/add-item db store-txs payload fn fn))

(defn ^:private load [into-coll store]
  (go
    (doseq [tx (<! (idxdb/get-all-from db store 1 :keywordize-keys true))]
      (swap! into-coll conj tx))))

(defn load-txs [into-coll]
  (load into-coll store-txs))

(defn load-tags [into-coll]
  (load into-coll store-tags))
