(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[30],{gMQw:function(t,e,n){"use strict";n.r(e);var r,a,o=n("2Taf"),i=n.n(o),c=n("vZ4D"),u=n.n(c),s=n("MhPg"),l=n.n(s),f=n("l4Ni"),p=n.n(f),h=n("ujKo"),v=n.n(h),d=n("q1tI"),y=n.n(d),m=n("HTZB"),w=n("RFWI"),g=n("iPxP"),R=n.n(g);function x(t){var e=E();return function(){var n,r=v()(t);if(e){var a=v()(this).constructor;n=Reflect.construct(r,arguments,a)}else n=r.apply(this,arguments);return p()(this,n)}}function E(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch(t){return!1}}var F=(r=Object(w["a"])(),r(a=function(t){l()(n,t);var e=x(n);function n(){return i()(this,n),e.apply(this,arguments)}return u()(n,[{key:"render",value:function(){var t=this.props,e=t.height,n=t.forceFit,r=void 0===n||n,a=t.color,o=void 0===a?"#1890FF":a,i=t.data,c=void 0===i?[]:i,u={x:{type:"cat"},y:{min:0}},s=[36,5,30,5],l=["x*y",function(t,e){return{name:t,value:e}}],f=e+54;return y.a.createElement("div",{className:R.a.miniChart,style:{height:e}},y.a.createElement("div",{className:R.a.chartContent},y.a.createElement(m["Chart"],{scale:u,height:f,forceFit:r,data:c,padding:s},y.a.createElement(m["Tooltip"],{showTitle:!1,crosshairs:!1}),y.a.createElement(m["Geom"],{type:"interval",position:"x*y",color:o,tooltip:l}))))}}]),n}(y.a.Component))||a);e["default"]=F}}]);