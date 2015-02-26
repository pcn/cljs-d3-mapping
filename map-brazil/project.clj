(defproject map-brazil "0.1.0-SNAPSHOT"
  :description "Clojure+clojurescript project to show data about brazil on a d3 map"
  :url "https://github.com/pcn"
  :min-lein-version "2.0.0"
  :license {:name "Apache 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [org.clojure/clojurescript "0.0-2913"]
                 [cljsjs/d3 "3.5.5-0"]
                 [cljsjs/topojson "1.6.18-0"]
                 ]
;;   ;; Use lein-git-deps https://github.com/tobyhede/lein-git-deps
;;   ;; so I can use my fork until it's in good enough shape for public consumption
;;   :dev-dependencies [[lein-git-deps "0.0.1-SNAPSHOT"]]
;;   :git-dependencies [["https://github.com/pcn/packages.git"]]
;;   :extra-classpath-dirs [".lein-git-deps/monger/src/"]
;;   ;; End lein-git-deps work
  :ring {:handler map-brazil.core.handler/app}
  :profiles
  {:dev
   { :dependencies [[javax.servlet/servlet-api "2.5"]
                    [ring-mock "0.1.5"]]}}
  :source-paths ["src/clj"]
  :plugins [ [lein-cljsbuild "1.0.4"]
             [lein-ring "0.8.13"]]
  :cljsbuild {:builds
              [{:id "app"
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/app.js"
                           :optimizations :whitespace
                           ;; :optimizations :none
                           :pretty-print true}}]})
