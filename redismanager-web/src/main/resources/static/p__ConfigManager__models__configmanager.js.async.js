(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[6],{a0PR:function(a,e,t){"use strict";t.r(e);var n=t("p0pE"),r=t.n(n),c=t("d6i3"),s=t.n(c),p=t("dCQc");e["default"]={namespace:"configmanager",state:{data:{list:[],pagination:{}}},effects:{fetch:s.a.mark(function a(e,t){var n,r,c,u,o;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,u=t.put,a.next=4,c(p["r"],n);case 4:return o=a.sent,a.next=7,u({type:"saveList",payload:o});case 7:r&&r(o);case 8:case"end":return a.stop()}},a)}),add:s.a.mark(function a(e,t){var n,r,c,u,o;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,u=t.put,a.next=4,c(p["a"],n);case 4:return o=a.sent,a.next=7,u({type:"onlySave",payload:o});case 7:r&&r(o);case 8:case"end":return a.stop()}},a)}),update:s.a.mark(function a(e,t){var n,r,c,u,o;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,u=t.put,a.next=4,c(p["L"],n);case 4:return o=a.sent,a.next=7,u({type:"onlySave",payload:o});case 7:r&&r(o);case 8:case"end":return a.stop()}},a)}),remove:s.a.mark(function a(e,t){var n,r,c,u,o;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,u=t.put,a.next=4,c(p["D"],n);case 4:return o=a.sent,a.next=7,u({type:"onlySave",payload:o});case 7:r&&r(o);case 8:case"end":return a.stop()}},a)})},reducers:{saveList:function(a,e){return r()({},a,{data:e.payload.datas||{}})},onlySave:function(a,e){return r()({},a)},save:function(a,e){return r()({},a,{data:e.payload})}}}}}]);