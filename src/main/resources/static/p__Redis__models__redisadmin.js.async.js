(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[5],{yXRw:function(e,t,a){"use strict";a.r(t);var r=a("mK77"),n=a.n(r),c=a("Ico4"),s=a.n(c),i=a("dCQc"),o=a("DWJA"),u=a("HPov"),p=a("34ay"),l=a("HZnN");t["default"]={namespace:"redisadmin",state:{configList:[],keyList:[],keyValue:{}},effects:{fetchConfigList:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["n"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(l),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),appendFetchConfigList:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["n"],r);case 4:return l=e.sent,e.next=7,p({type:"appendConfigList",payload:l});case 7:if(n&&n(l),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),addConfig:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["c"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),removeConfig:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["s"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),updateConfig:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["x"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),initContext:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["j"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),clearCache:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["d"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),testConnection:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["u"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(l),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),fetchKeyList:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["o"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(l),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),fetchKeyValue:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["p"],r);case 4:return l=e.sent,e.next=7,p({type:"saveKeyValue",payload:l});case 7:if(n&&n(l),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),delKeys:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["e"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),setKeyTTL:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["t"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),reNameKey:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["q"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),updateKeyValue:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["w"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)}),addKeyValue:s.a.mark(function e(t,a){var r,n,c,p,l;return s.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,n=t.callback,c=a.call,p=a.put,e.next=4,c(i["b"],r);case 4:return l=e.sent,e.next=7,p({type:"save",payload:l});case 7:if(n&&n(l),!(l&&l.code&&l.code.startsWith("7"))){e.next=11;break}return e.next=11,p(o["routerRedux"].push({pathname:"/user/login",search:Object(u["stringify"])({redirect:window.location.href})}));case 11:case"end":return e.stop()}},e)})},reducers:{save:function(e,t){var a=t.payload;return n()({},e,a)},handleAuth:function(e,t){var a=t.payload,r=[];return r.push("admin","user"),Object(p["b"])(r),Object(l["b"])(),n()({},e,a)},saveKeyValue:function(e,t){return e.keyValue=t.payload.keyValue,n()({},e,t.payload)},appendConfigList:function(e,t){return n()({},e,{configList:e.configList.concat(t.payload.configList)})}}}}}]);