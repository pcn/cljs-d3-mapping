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
(defn- build-svg [width height]
  (.. js/d3 ;; XXX To learn: what is .. here?
      (select ".app")
      (append "svg")
      (attr "width" width)
      (attr "height" height)))


;; Some convience stuff
(defn- feature-fn []
  (.-feature js/topojson))

(defn- geo-obj []
  (.-geo js/d3))

(defn- mercator-fn [geo]
  (.-mercator geo))

(defn- extract-map-objects [topo-obj k1]
  "Extract the subunits property of a topology/topojson object"
  ;; Hooray for aget
  ;; http://stackoverflow.com/questions/17601792/clojurescript-property-accessor-function
  ;; (.-subunits (.-objects topo-obj))
  (aget topo-obj "objects" k1)
  )

(defn- path-obj [projection-obj]
  "Path generator over a type of projection"
  (let [geo (geo-obj)]
    (.. geo
        path
        (projection projection-obj))))

;; Note on the .. form: http://clojure.github.io/clojure/clojure.core-api.html#clojure.core/..
;; In the d3 example, what I'm calling "topo-geo" is the "uk" object of parsed
;; uk map data
(defn- build-map [svg topo-obj feature-level]
  (let [subunits (extract-map-objects topo-obj feature-level)
        geo (geo-obj)
        feature (feature-fn)
        mercator (mercator-fn geo)]
    (.. svg
        (append "path")
        (datum (feature topo-obj subunits))
        (attr "d"
              (.. geo
                  (path)
                  (projection (mercator)))))))

;; Next part of the tutorial:
;; svg.selectAll(".subunit")
;;     .data(topojson.feature(uk, uk.objects.subunits).features)
;;   .enter().append("path")
;;     .attr("class", function(d) { return "subunit " + d.id; })
;;     .attr("d", path);
(defn- styling [svg topo-obj path feature-level]
  (let [feature (feature-fn)
        subunits (extract-map-objects topo-obj feature-level)
        ]
    (.. svg
        (selectAll ".subunit")
        (data (.-features  (feature topo-obj subunits)))
        (enter)
        (append "path")
        (attr "class" (fn [d] (str/join ["subunit " (.-id d)] )))
        (attr "d" path))))


;; Extrapolate how to use cljsjs.d3 and cljs with
;; the instructions from http://bost.ocks.org/mike/map/
(defn ^:export main [json-file feature-level]
  (let [width 960
        height 1160
        svg (build-svg width height)]
    (.json js/d3 json-file
           (fn [error json]
             (build-map svg json feature-level)
             (.log js/console json-map-data)
             ))
;;    (build-map svg json-map-data feature-level)
    ))
