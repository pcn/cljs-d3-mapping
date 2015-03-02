// Compiled by ClojureScript 0.0-2913 {}
goog.provide('map_brazil.core');
goog.require('cljs.core');
goog.require('clojure.string');
map_brazil.core.build_svg = (function build_svg(scale_map){
return d3.select(".app").append("svg").attr("width",scale_map.call(null,new cljs.core.Keyword(null,"canvas-width","canvas-width",1321931102))).attr("height",scale_map.call(null,new cljs.core.Keyword(null,"canvas-height","canvas-height",291287231)));
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
map_brazil.core.projection_mercator = (function projection_mercator(geo,scale_map){

return geo.mercator().scale(scale_map.call(null,new cljs.core.Keyword(null,"scale","scale",-230427353))).translate([scale_map.call(null,new cljs.core.Keyword(null,"center-w","center-w",-1999967792)),scale_map.call(null,new cljs.core.Keyword(null,"center-h","center-h",-574190143))]);
});
map_brazil.core.calculate_size = (function calculate_size(bounds,scale,width,height){

var max = Math.max();
return (scale / max.call(null,(((bounds[(1)][(0)]) - (bounds[(0)][(0)])) / width),(((bounds[(1)][(1)]) - (bounds[(0)][(1)])) / height)));
});
map_brazil.core.calculate_translation = (function calculate_translation(bounds,scale,width,height){

return [((width - (scale * ((bounds[(1)][(0)]) + (bounds[(0)][(0)])))) / (2)),((height - (scale * ((bounds[(1)][(1)]) + (bounds[(0)][(1)])))) / (2))];
});
map_brazil.core.build_centered_projection = (function build_centered_projection(geo,surrounding_area,specific_area,width,height){

var projection = geo.albers();
var unit_projection = projection.scale((1)).translate([(0),(0)]);
var path = geo.path().projection(projection);
var b = path.bounds(specific_area);
var s = map_brazil.core.calculate_size.call(null,b,.95,width,height);
var t = map_brazil.core.calculate_translation.call(null,b,s,width,height);
projection.scale(s).translate(t);

return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [projection,path], null);
});
map_brazil.core.build_map_mercator_1 = (function build_map_mercator_1(svg,geo,topo_obj,feature_level,scale_map){
var subunits = map_brazil.core.extract_map_objects.call(null,topo_obj,feature_level);
var feature = map_brazil.core.feature_fn.call(null);
var mercator = map_brazil.core.mercator_fn.call(null,geo);
return svg.append("path").datum(feature.call(null,topo_obj,subunits)).attr("d",geo.path().projection(map_brazil.core.projection_mercator.call(null,geo,scale_map)));
});
map_brazil.core.build_map_via_path = (function build_map_via_path(svg,topo_obj,feature_level,path){
var subunits = map_brazil.core.extract_map_objects.call(null,topo_obj,feature_level);
var feature = map_brazil.core.feature_fn.call(null);
return svg.append("path").datum(feature.call(null,topo_obj,subunits)).attr("d",path);
});
map_brazil.core.main = (function main(json_file,feature_level){
var width_18839 = (1200);
var height_18840 = (1200);
var config_18841 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),width_18839,new cljs.core.Keyword(null,"height","height",1025178622),height_18840], null);
var svg_18842 = map_brazil.core.build_svg.call(null,config_18841);
var geo_18843 = map_brazil.core.geo_obj.call(null);
d3.json(json_file,((function (width_18839,height_18840,config_18841,svg_18842,geo_18843){
return (function (error,json){
var this_county = (json["objects"][feature_level]["properties"]["name"]["ANTAS"]);
var vec__18838 = map_brazil.core.build_centered_projection.call(null,geo_18843,json,this_county,width_18839,height_18840);
var projection = cljs.core.nth.call(null,vec__18838,(0),null);
var path = cljs.core.nth.call(null,vec__18838,(1),null);
debugger$();

svg_18842.append("path").attr("class","feature").attr("d",path);

svg_18842.append("path").datum(topojson.mesh(json,(json["objects"][feature_level]),((function (this_county,vec__18838,projection,path,width_18839,height_18840,config_18841,svg_18842,geo_18843){
return (function (a,b){
return cljs.core.not_EQ_.call(null,a,b);
});})(this_county,vec__18838,projection,path,width_18839,height_18840,config_18841,svg_18842,geo_18843))
)).attr("class","mesh").attr("d",path);

return svg_18842.append("path").datum(this_county).attr("class","outline").attr("d",path);
});})(width_18839,height_18840,config_18841,svg_18842,geo_18843))
);

cljs.core.enable_console_print_BANG_.call(null);

cljs.core.println.call(null,"You can change this line an see the changes in the dev console");

return fw.start.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-jsload","on-jsload",-395756602),(function (){
return cljs.core.print.call(null,"reloaded");
})], null));
});
goog.exportSymbol('map_brazil.core.main', map_brazil.core.main);
