(def tagged #{
{:description "BRGAS-GAS", :amount "-72.98", :date "16/10/2017", :tag "utilities"}
{:description "ENFIELD", :amount "-214.00", :date "01/09/2017", :tag "counciltax"}
{:description "INTEREST TO 21SEP2017", :amount "-2.07", :date "13/10/2017", :tag nil}
{:description "404758 00953180 INTERNAL TRANSFER", :amount "2000.00", :date "02/08/2017", :tag nil}
{:description "ENFIELD", :amount "-214.00", :date "01/08/2017", :tag "counciltax"}
{:description "HALIFAX", :amount "-1627.84", :date "01/08/2017", :tag "mortgage"}
{:description "ENFIELD", :amount "-214.00", :date "02/10/2017", :tag "counciltax"}
{:description "HALIFAX", :amount "-1627.84", :date "01/09/2017", :tag "mortgage"}
{:description "BRGAS-GAS", :amount "-72.98", :date "15/09/2017", :tag "utilities"}
{:description "404758 00953180 INTERNAL TRANSFER", :amount "2100.00", :date "25/09/2017", :tag nil}
{:description "AIL - HSBC", :amount "-28.17", :date "03/10/2017", :tag nil}
{:description "INTEREST TO 21AUG2017", :amount "-0.69", :date "12/09/2017", :tag nil}
{:description "BRGAS-ELECTRICITY", :amount "-60.95", :date "15/08/2017", :tag "utilities"}
{:description "BRGAS-GAS", :amount "-72.98", :date "15/08/2017", :tag "utilities"}
{:description "404758 00953180 INTERNAL TRANSFER", :amount "1971.67", :date "04/09/2017", :tag nil}
{:description "AIL - HSBC", :amount "-28.17", :date "02/08/2017", :tag nil}
{:description "BRGAS-GAS", :amount "7.08", :date "20/10/2017", :tag "utilities"}
{:description "BRGAS-ELECTRICITY", :amount "-60.95", :date "16/10/2017", :tag "utilities"}
{:description "HALIFAX", :amount "-1627.84", :date "02/10/2017", :tag "mortgage"}
{:description "BRGAS-ELECTRICITY", :amount "-60.95", :date "15/09/2017", :tag "utilities"}
{:description "AIL - HSBC", :amount "-28.17", :date "04/09/2017", :tag nil}
})

(def g1 (group-by :tag tagged))

(defn to-number [s]
  (Double/parseDouble s))


(map
 (fn [it]
   (let [[k v] it]
     {k (reduce + (map #(-> % :amount to-number) v))}))
 g1)
(def grouped (group-by :tag tagged))
(def summed-txs (map
                   (fn [it]
                     (let [[k v] it]
                       {:tag k :sum (reduce + (map #(-> % :amount Double/parseDouble) v))}))
                   grouped))

summed-txs



(comment
  (defn subst [[match-key match-val] 
               replace-key replace-val]
    (fn [thing]
      (if (= match-val (match-key thing))
        (assoc thing replace-key replace-val)
        thing)))
  (defn red [acc item]
    (let [tag (:tag item)
          amount (to-number (:amount item))]
      (println ". " tag " -> " amount)
      (cond (contains? acc tag)
            (do
              (println "> yes > " tag " in " acc)
              (update-in acc tag #(+ % amount)))
            :else
            (do
              (println "> no > " tag)
              (assoc acc :tag tag :amount amount))
            )))
  (reduce red {}  tagged)

  ;; https://stackoverflow.com/a/3052706/51280
  ;; https://stackoverflow.com/a/25665098/51280
  (let [m #{{:tag "tag1" :amount 4} {:tag "tag2" :amount 10}}
        m2 (into {} (map (juxt :tag identity) m))]
    (update-in m2 ["tag1" :amount] #(+ % 100)))


  )


