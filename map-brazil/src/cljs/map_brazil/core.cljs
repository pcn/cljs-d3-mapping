(ns map-brazil.core
  (:require cljsjs.d3
            cljsjs.topojson
            [clojure.string :as str]))

;; A list of functions that are bound by cljsjs:
;; https://github.com/cljsjs/packages/blob/master/d3/resources/cljsjs/common/d3.ext.js

;; The following is doing the same as this kind of function-chaining:
;;    var svg = d3.select("body").append("svg")
;;        .attr("width", width)
;;        .attr("height", height);
;; So the calling of the d3.select returns the body object in the above.
;; In this function, I'm following the example provided by
;; https://github.com/wtfleming/clojurescript-examples in the force-directed-graph
;; app, and using the ".app" element instead of the "body" element (I think)
;;
;; I'm supposing as a naif that this returns a binding to the
;; equivalent javascript object, bound somehow to the dom at the ".app"
;; element with the given width and hieght.
(defn- build-svg [scale-map]
  (.. js/d3 ;; XXX To learn: what is .. here?
      (select ".app")
      (append "svg")
      (attr "width" (scale-map :canvas-width))
      (attr "height" (scale-map :canvas-height))))

;; Some convience stuff
(defn- feature-fn []
  (.-feature js/topojson))

(defn- geo-obj []
  (.-geo js/d3))

(defn- mercator-fn [geo]
  (.-mercator geo))

(defn- extract-map-objects [topo-obj k1]
  "return the subunits.<whatever k1's value is> property of a topojson object"
  ;; Hooray for aget
  ;; http://stackoverflow.com/questions/17601792/clojurescript-property-accessor-function
  (aget topo-obj "objects" k1))

(defn- projection-mercator [geo scale-map]
  "This function is a mistake - it does too much."
    (.. geo
        (mercator)
        (scale (scale-map :scale))
        (translate #js [(scale-map :center-w)
                        (scale-map :center-h) ])))


(defn- calculate-size [bounds scale width height]
  "bounds is what is provided by javascript's path.bounds(feature).
This returns a decimal or float or whatever js wants to provide as
the result of a division"
  (let [ max (.max js/Math)]
    (/ scale (max (/ (- (aget bounds 1 0) (aget bounds 0 0)) width)
                  (/ (- (aget bounds 1 1) (aget bounds 0 1)) height)))))

(defn calculate-translation [bounds scale width height ]
  "bounds is what is provided by javascript's path.bounds(feature).
This returns a javascript array of two numbers."
  #js [(/ (- width (* scale (+ (aget bounds 1 0) (aget bounds 0 0)))) 2)
       (/ (- height (* scale (+ (aget bounds 1 1) (aget bounds 0 1)))) 2)])



;; Based on
;; http://stackoverflow.com/questions/14492284/center-a-map-in-d3-given-a-geojson-object
;; - mbostock's answer.
(defn- build-centered-projection [geo surrounding-area specific-area width height]
  "Surrounding area is the feature surrounding the fetaure of interest.
Specific area is the feature being dealt with.
The width and height are the width and height of the svg object that's
been created.

Returns an albers projection and a path in a vector"
  (let [projection (.albers geo)
        unit-projection (.. projection
                            (scale 1)
                            (translate #js [0 0]))
        path (.projection (.path geo) projection)
        b (.bounds path specific-area)
        s (calculate-size b .95 width height)
        t (calculate-translation b s width height)]
    (.. projection
        (scale s)
        (translate t))
    [projection path])) ;; I hope this will return the projection as it stands now


;; Note on the .. form: http://clojure.github.io/clojure/clojure.core-api.html#clojure.core/..
;; In the d3 example, what I'm calling "topo-geo" is the "uk" object of parsed
;; uk map data
(defn- build-map-mercator-1 [svg geo topo-obj feature-level scale-map]
  (let [subunits (extract-map-objects topo-obj feature-level)
        feature (feature-fn)
        mercator (mercator-fn geo)]
    (.. svg
        (append "path")
        (datum (feature topo-obj subunits))
        (attr "d"
              (.. geo
                  (path)
                  (projection (projection-mercator geo scale-map)))))))

(defn build-map-via-path [svg topo-obj feature-level path]
  (let [subunits (extract-map-objects topo-obj feature-level)
        feature (feature-fn)]
    (.. svg
        (append "path")
        (datum (feature topo-obj subunits))
        (attr "d" path ))))

;; Extrapolate how to use cljsjs.d3 and cljs with
;; the instructions from http://bost.ocks.org/mike/map/
(defn ^:export main [json-file feature-level]
  (let [width 1200
        height 1200
        config {:width width
                :height height}
        svg (build-svg config)
        geo (geo-obj)]
    (.json js/d3 json-file
           (fn [error json]
             (let [this-county (aget json "objects" feature-level "properties" "name" "ANTAS")
                   [projection path] (build-centered-projection geo json this-county width height)]
               ;; Side-effects start here.
               (js/debugger)
               (.. svg
                   (append "path")
                   (attr "class" "feature")
                   (attr "d" path))
               (.. svg
                   (append "path")
                   (datum (.mesh js/topojson json (aget json "objects" feature-level)
                                 (fn [a b] (not= a b))))
                   (attr "class" "mesh")
                   (attr "d" path))
               (.. svg
                   (append "path")
                   (datum this-county)
                   (attr "class" "outline")
                   (attr "d" path))))))
  (enable-console-print!)

  (println "You can change this line an see the changes in the dev console")

  (fw/start
   { ;; configure a websocket url if you are using your own server
    ;; :websocket-url "ws://localhost:3449/figwheel-ws"

    ;; optional callback
    :on-jsload (fn [] (print "reloaded"))

    ;; The heads up display is enabled by default
    ;; to disable it:
    ;; :heads-up-display false

    ;; when the compiler emits warnings figwheel
    ;; blocks the loading of files.
    ;; To disable this behavior:
    ;; :load-warninged-code true

    ;; if figwheel is watching more than one build
    ;; it can be helpful to specify a build id for
    ;; the client to focus on
    ;; :build-id "example"
    }))
