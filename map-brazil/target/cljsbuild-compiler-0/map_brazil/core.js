// Compiled by ClojureScript 0.0-2913 {}
goog.provide('map_brazil.core');
goog.require('cljs.core');
goog.require('clojure.string');
map_brazil.core.build_svg = (function build_svg(width,height){
    return d3.select(".app").append("svg").attr("width",width).attr("height",height);
});
map_brazil.core.feature_fn = (function feature_fn(){
    return topojson.feature;
});
map_brazil.core.geo_obj = (function geo_obj(){
    return d3.geo;
});
map_brazil.core.mercator_fn = (function mercator_fn(geo){
    return geo.mercator;
});
map_brazil.core.extract_map_objects = (function extract_map_objects(topo_obj,k1){

    return (topo_obj["objects"][k1]);
});
map_brazil.core.path_obj = (function path_obj(projection_obj){

    var geo = map_brazil.core.geo_obj.call(null);
    return geo.path().projection(projection_obj);
});
map_brazil.core.build_map = (function build_map(svg,topo_obj,feature_level){
    var subunits = map_brazil.core.extract_map_objects.call(null,topo_obj,feature_level);
    var geo = map_brazil.core.geo_obj.call(null);
    var feature = map_brazil.core.feature_fn.call(null);
    var mercator = map_brazil.core.mercator_fn.call(null,geo);
    return svg.append("path").datum(feature.call(null,topo_obj,subunits)).attr("d",geo.path().projection(mercator.call(null)));
});
map_brazil.core.styling = (function styling(svg,topo_obj,path,feature_level){
    var feature = map_brazil.core.feature_fn.call(null);
    var subunits = map_brazil.core.extract_map_objects.call(null,topo_obj,feature_level);
    return svg.selectAll(".subunit").data(feature.call(null,topo_obj,subunits).features).enter().append("path").attr("class",((function (feature,subunits){
        return (function (d){
            return clojure.string.join.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["subunit ",d.id], null));
        });})(feature,subunits))
                                                                                                                    ).attr("d",path);
});
map_brazil.core.main = (function main(json_file,feature_level){
    var width = (960);
    var height = (1160);
    var svg = map_brazil.core.build_svg.call(null,width,height);
    var json_map_data = d3.json(json_file);
    return console.log(json_map_data);
});
goog.exportSymbol('map_brazil.core.main', map_brazil.core.main);
