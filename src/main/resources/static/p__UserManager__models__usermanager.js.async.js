(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[8],{HKq5:function(e,a,t){"use strict";t.r(a);var n=t("mK77"),r=t.n(n),c=t("Ico4"),s=t.n(c),u=t("dCQc");a["default"]={namespace:"usermanager",state:{data:{list:[],pagination:{}}},effects:{fetch:s.a.mark(function e(a,t){var n,r,c,p;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return n=a.payload,r=t.call,c=t.put,e.next=4,r(u["s"],n);case 4:return p=e.sent,e.next=7,c({type:"save",payload:p});case 7:case"end":return e.stop()}},e)}),add:s.a.mark(function e(a,t){var n,r,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return n=a.payload,r=a.callback,c=t.call,p=t.put,e.next=4,c(u["d"],n);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:r&&r();case 8:case"end":return e.stop()}},e)}),remove:s.a.mark(function e(a,t){var n,r,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return n=a.payload,r=a.callback,c=t.call,p=t.put,e.next=4,c(u["w"],n);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:r&&r();case 8:case"end":return e.stop()}},e)}),updateStatus:s.a.mark(function e(a,t){var n,r,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return n=a.payload,r=a.callback,c=t.call,p=t.put,e.next=4,c(u["E"],n);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:r&&r();case 8:case"end":return e.stop()}},e)}),resetPwd:s.a.mark(function e(a,t){var n,r,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return n=a.payload,r=a.callback,c=t.call,p=t.put,e.next=4,c(u["x"],n);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:r&&r();case 8:case"end":return e.stop()}},e)})},reducers:{save:function(e,a){return r()({},e,{data:a.payload})}}}}}]);