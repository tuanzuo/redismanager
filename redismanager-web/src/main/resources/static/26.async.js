(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[26],{LJjV:function(e,t,n){"use strict";n.r(t);var i,a,o,r,s,c,l=n("2Taf"),u=n.n(l),f=n("vZ4D"),h=n.n(f),d=n("MhPg"),p=n.n(d),v=n("l4Ni"),y=n.n(v),m=n("ujKo"),w=n.n(m),b=n("SQvw"),g=n.n(b),E=n("q1tI"),L=n.n(E),R=n("HTZB"),k=n("fqkP"),x=n.n(k),z=n("UjoV"),H=n.n(z),P=n("RFWI"),j=n("iPxP"),D=n.n(j);function X(e){var t=S();return function(){var n,i=w()(e);if(t){var a=w()(this).constructor;n=Reflect.construct(i,arguments,a)}else n=i.apply(this,arguments);return y()(this,n)}}function S(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch(e){return!1}}var T=(i=Object(P["a"])(),a=H()(),o=x()(400),i((c=function(e){p()(n,e);var t=X(n);function n(){var e;u()(this,n);for(var i=arguments.length,a=new Array(i),o=0;o<i;o++)a[o]=arguments[o];return e=t.call.apply(t,[this].concat(a)),e.state={autoHideXLabels:!1},e.handleRoot=function(t){e.root=t},e.handleRef=function(t){e.node=t},e}return h()(n,[{key:"componentDidMount",value:function(){window.addEventListener("resize",this.resize,{passive:!0})}},{key:"componentWillUnmount",value:function(){window.removeEventListener("resize",this.resize)}},{key:"resize",value:function(){if(this.node){var e=this.node.parentNode.clientWidth,t=this.props,n=t.data,i=void 0===n?[]:n,a=t.autoLabel,o=void 0===a||a;if(o){var r=30*i.length,s=this.state.autoHideXLabels;e<=r?s||this.setState({autoHideXLabels:!0}):s&&this.setState({autoHideXLabels:!1})}}}},{key:"render",value:function(){var e=this.props,t=e.height,n=e.title,i=e.forceFit,a=void 0===i||i,o=e.data,r=e.color,s=void 0===r?"rgba(24, 144, 255, 0.85)":r,c=e.padding,l=this.state.autoHideXLabels,u={x:{type:"cat"},y:{min:0}},f=["x*y",function(e,t){return{name:e,value:t}}];return L.a.createElement("div",{className:D.a.chart,style:{height:t},ref:this.handleRoot},L.a.createElement("div",{ref:this.handleRef},n&&L.a.createElement("h4",{style:{marginBottom:20}},n),L.a.createElement(R["Chart"],{scale:u,height:n?t-41:t,forceFit:a,data:o,padding:c||"auto"},L.a.createElement(R["Axis"],{name:"x",title:!1,label:!l&&{},tickLine:!l&&{}}),L.a.createElement(R["Axis"],{name:"y",min:0}),L.a.createElement(R["Tooltip"],{showTitle:!1,crosshairs:!1}),L.a.createElement(R["Geom"],{type:"interval",position:"x*y",color:s,tooltip:f}))))}}]),n}(E["Component"]),s=c,g()(s.prototype,"resize",[a,o],Object.getOwnPropertyDescriptor(s.prototype,"resize"),s.prototype),r=s))||r);t["default"]=T}}]);