goog.addDependency("base.js", ['goog'], []);
goog.addDependency("../cljs/core.js", ['cljs.core'], ['goog.string', 'goog.object', 'goog.string.StringBuffer', 'goog.array']);
goog.addDependency("../topojson.inc.js", ['cljsjs.topojson'], []);
goog.addDependency("../clojure/string.js", ['clojure.string'], ['goog.string', 'cljs.core', 'goog.string.StringBuffer']);
goog.addDependency("../d3.inc.js", ['cljsjs.d3'], []);
goog.addDependency("../map_brazil/core.js", ['map_brazil.core'], ['cljs.core', 'cljsjs.topojson', 'clojure.string', 'cljsjs.d3']);