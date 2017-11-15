(ns csv.upload
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require
   [cljs.core.async :refer [put! chan <!]]
   [goog.labs.format.csv :as csv]
   [db.dao :as dao]))

(def extract-result
  (map #(-> % .-target .-result csv/parse js->clj)))

(def first-file
  (map (fn [e]
         (let [target (.-currentTarget e)
               file (-> target .-files (aget 0))]
           (set! (.-value target) "")
           file))))

(def upload-reqs (chan 1 first-file))
(def file-reads (chan 1 extract-result))

(defn put-upload [e]
  (put! upload-reqs e))

;; channel-based architecture credits https://mrmcc3.github.io/post/csv-with-clojurescript/
;; a loop to write to file-reads
(go-loop []
  (let [reader (js/FileReader.)
        file (<! upload-reqs)]
    (print (str "filename" (.-name file)))
    (set! (.-onload reader) #(put! file-reads %))
    (.readAsText reader file)
    (recur)))

;; a loop to read from file-reads
(go-loop []
  (let [data (<! file-reads)
        [header & rest] data]
    (doseq [datum rest]
      (let [payload {:date (get datum 0) :description (get datum 1) :amount (get datum 2)}]
        (dao/put-tx payload dao/debug-fn)
        )))
  (recur))
