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

(defn- extract-subunits [topo-obj]
  "Extract the subunits property of a topology/topojson object"
  (.-subunits (.-objects topo-obj)))

(defn- path-obj [projection-obj]
  "Path generator over a type of projection"
  (let [geo (geo-obj)]
    (.. geo
        path
        (projection projection-obj))))

;; Note on the .. form: http://clojure.github.io/clojure/clojure.core-api.html#clojure.core/..
;; In the d3 example, what I'm calling "topo-geo" is the "uk" object of parsed
;; uk map data
(defn- build-map [svg topo-obj]
  (let [subunits (extract-subunits topo-obj)
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
(defn- styling [svg topo-obj path]
  (let [feature (feature-fn)
        subunits (extract-subunits)]
    (.. svg
        (selectAll ".subunit")
        (data (.-features  (feature topo-obj subunits)))
        (enter)
        (append "path")
        (attr "class" (fn [d] (str/join ["subunit " (.-id d)] )))
        (attr "d" path))))


;; Extrapolate how to use cljsjs.d3 and cljs with
;; the instructions from http://bost.ocks.org/mike/map/
(defn ^:export main [json-file]
  (let [width 960
        height 1160
        svg (build-svg width height)
        json-map-data (.json js/d3 json-file)]
    (.log console json-map-data)))
