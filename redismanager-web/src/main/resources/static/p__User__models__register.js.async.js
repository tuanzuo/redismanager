(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[13],{"0Fdr":function(e,t,a){"use strict";a.r(t);var r=a("mK77"),s=a.n(r),n=a("Ico4"),c=a.n(n),o=a("dCQc");a("34ay"),a("HZnN");t["default"]={namespace:"register",state:{status:void 0,registerCode:void 0},effects:{submit:c.a.mark(function e(t,a){var r,s,n,u,d;return c.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,s=t.callback,n=a.call,u=a.put,e.next=4,n(o["j"],r);case 4:return d=e.sent,e.next=7,u({type:"registerHandle",payload:d});case 7:s&&s(d);case 8:case"end":return e.stop()}},e)})},reducers:{registerHandle:function(e,t){var a=t.payload;return s()({},e,{status:a.status,registerCode:a.code})}}}}}]);