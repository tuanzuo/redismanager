(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[11],{akFF:function(a,e,t){"use strict";t.r(e);var n=t("p0pE"),r=t.n(n),c=t("d6i3"),s=t.n(c),u=t("dCQc");e["default"]={namespace:"rolemanager",state:{data:{list:[],pagination:{}}},effects:{fetch:s.a.mark(function a(e,t){var n,r,c,p,l;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,p=t.put,a.next=4,c(u["C"],n);case 4:return l=a.sent,a.next=7,p({type:"saveList",payload:l});case 7:r&&r(l);case 8:case"end":return a.stop()}},a)}),add:s.a.mark(function a(e,t){var n,r,c,p,l;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,p=t.put,a.next=4,c(u["e"],n);case 4:return l=a.sent,a.next=7,p({type:"onlySave",payload:l});case 7:r&&r(l);case 8:case"end":return a.stop()}},a)}),update:s.a.mark(function a(e,t){var n,r,c,p,l;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,p=t.put,a.next=4,c(u["T"],n);case 4:return l=a.sent,a.next=7,p({type:"onlySave",payload:l});case 7:r&&r(l);case 8:case"end":return a.stop()}},a)}),updateStatus:s.a.mark(function a(e,t){var n,r,c,p,l;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,p=t.put,a.next=4,c(u["U"],n);case 4:return l=a.sent,a.next=7,p({type:"onlySave",payload:l});case 7:r&&r(l);case 8:case"end":return a.stop()}},a)}),remove:s.a.mark(function a(e,t){var n,r,c,p,l;return s.a.wrap(function(a){while(1)switch(a.prev=a.next){case 0:return n=e.payload,r=e.callback,c=t.call,p=t.put,a.next=4,c(u["J"],n);case 4:return l=a.sent,a.next=7,p({type:"onlySave",payload:l});case 7:r&&r(l);case 8:case"end":return a.stop()}},a)})},reducers:{saveList:function(a,e){return r()({},a,{data:e.payload.datas||{}})},onlySave:function(a,e){return r()({},a)},save:function(a,e){return r()({},a,{data:e.payload})}}}}}]);