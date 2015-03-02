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
                 [figwheel "0.2.5-SNAPSHOT"]
                 ]
  :main map-brazil.core
  :ring {:handler map-brazil.core.handler/app}
  :profiles
  {:dev
   { :dependencies [[javax.servlet/servlet-api "2.5"]
                    [ring-mock "0.1.5"]]}}
  :source-paths ["src/clj"]
  :plugins [ [lein-cljsbuild "1.0.4"]
             [lein-ring "0.8.13"]
             [lein-figwheel "0.2.5-SNAPSHOT"]]
  :cljsbuild {:builds
              [{:id "app"
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/app.js"
                           :output-dir "resources/public/js/out"
                           ;; :optimizations :whitespace
                           :optimizations :none
                           :pretty-print true}}]}
  :figwheel
  {
   :http-server-root "public" ;; this will be in resources/
   :server-port 3449          ;; default

   ;; CSS reloading (optional)
   ;; :css-dirs has no default value
   ;; if :css-dirs is set figwheel will detect css file changes and
   ;; send them to the browser
   :css-dirs ["resources/public/css"]

   ;; Server Ring Handler (optional)
   ;; if you want to embed a ring handler into the figwheel http-kit
   ;; server
   ;; :ring-handler map-brazil.core/handler

   ;; To be able to open files in your editor from the heads up display
   ;; you will need to put a script on your path.
   ;; that script will have to take a file path and a line number
   ;; ie. in  ~/bin/myfile-opener
   ;; #! /bin/sh
   ;; emacsclient -n +$2 $1
   ;;
   :open-file-command "myfile-opener"

   ;; if you want to disable the REPL
   ;; :repl false

   ;; to configure a different figwheel logfile path
   ;; :server-logfile "tmp/logs/figwheel-logfile.log"
   })
