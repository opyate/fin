(set-env!
  :dependencies '[[adzerk/boot-cljs          "2.1.4"]
                  [adzerk/boot-reload        "0.5.2"]
                  ;;[adzerk/boot-cljs-repl     "0.3.3"]
                  [hoplon/hoplon             "7.0.3"]
                  [org.clojure/clojure       "1.8.0"]
                  [org.clojure/core.async    "0.3.443"]
                  [org.clojure/clojurescript "1.9.946"]
                  ;;[cljs-idxdb "0.2.0-SNPASHOT"]
                  [tailrecursion/boot-jetty  "0.1.3"]]
  :source-paths #{"src"}
  :asset-paths  #{"assets"})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  ;;'[adzerk.boot-cljs-repl    :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[tailrecursion.boot-jetty :refer [serve]])

(deftask dev
  "Build hoplon-starter-project for local development."
  []
  (comp
    (watch)
    (speak)
    (hoplon)
    (reload)
    ;;(cljs-repl)
    (cljs)
    (serve :port 8000)))

(deftask prod
  "Build hoplon-starter-project for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (target :dir #{"target"})))
