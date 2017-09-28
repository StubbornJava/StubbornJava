import $ from 'jquery';
import AnchorJS from 'anchor-js';

// Bootstrap
import 'bootstrap/scss/bootstrap.scss';
import 'bootstrap/js/dist/util.js';
import 'bootstrap/js/dist/dropdown.js';

// PrismJs for code highlighting
import Prism from 'prismjs';
import 'prismjs/components/prism-java.js';
//import 'prism-themes/themes/prism-base16-ateliersulphurpool.light.css';
import 'prismjs/themes/prism-coy.css';
import 'prismjs/plugins/toolbar/prism-toolbar.js';
import 'prismjs/plugins/toolbar/prism-toolbar.css';
import 'prismjs/plugins/show-language/prism-show-language.js';
//import 'prismjs/plugins/show-language/prism-show-language.css';
import 'prismjs/plugins/line-numbers/prism-line-numbers.js';
import 'prismjs/plugins/line-numbers/prism-line-numbers.css';

import 'font-awesome/scss/font-awesome.scss';

import './prism-coy-override.scss';

// Anchors stuff
var anchors = new AnchorJS();
anchors.options = {
  placement: 'left'
};
anchors.add('.anchored, .anchored-md h1, .anchored-md h2, .anchored-md h3, .anchored-md h4, .anchored-md h5');

Prism.highlightAll();

$(document).ready(function() {
  // $('pre code').each(function(i, block) {
  //   //console.log(block);
  //   hljs.highlightBlock(block);
  // });
});
