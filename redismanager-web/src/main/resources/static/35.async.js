(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[35],{b2ve:function(e,a,t){"use strict";t.r(a);t("BFtC");var n=t("ZzqE"),r=(t("z7iZ"),t("O4+N")),s=(t("NWD0"),t("2L3m")),l=(t("4ADO"),t("wty1")),i=(t("6/yB"),t("SE39")),o=(t("Mf8d"),t("4OHk")),d=t("q1tI"),c=t.n(d),m=t("LLXN"),u=t("YR7N"),g=t("ZhIB"),p=t.n(g),y=t("lVjH"),E=t.n(y),f=t("LOQS"),h=t("KTCi"),M=[{title:c.a.createElement(m["FormattedMessage"],{id:"app.analysis.table.rank",defaultMessage:"Rank"}),dataIndex:"index",key:"index"},{title:c.a.createElement(m["FormattedMessage"],{id:"app.analysis.table.search-keyword",defaultMessage:"Search keyword"}),dataIndex:"keyword",key:"keyword",render:function(e){return c.a.createElement("a",{href:"/"},e)}},{title:c.a.createElement(m["FormattedMessage"],{id:"app.analysis.table.users",defaultMessage:"Users"}),dataIndex:"count",key:"count",sorter:function(e,a){return e.count-a.count},className:E.a.alignRight},{title:c.a.createElement(m["FormattedMessage"],{id:"app.analysis.table.weekly-range",defaultMessage:"Weekly Range"}),dataIndex:"range",key:"range",sorter:function(e,a){return e.range-a.range},render:function(e,a){return c.a.createElement(u["a"],{flag:1===a.status?"down":"up"},c.a.createElement("span",{style:{marginRight:4}},e,"%"))},align:"right"}],w=Object(d["memo"])(function(e){var a=e.loading,t=e.visitData2,d=e.searchData,u=e.dropdownGroup;return c.a.createElement(n["a"],{loading:a,bordered:!1,title:c.a.createElement(m["FormattedMessage"],{id:"app.analysis.online-top-search",defaultMessage:"Online Top Search"}),extra:u,style:{marginTop:24}},c.a.createElement(s["a"],{gutter:68},c.a.createElement(l["a"],{sm:12,xs:24,style:{marginBottom:24}},c.a.createElement(f["a"],{subTitle:c.a.createElement("span",null,c.a.createElement(m["FormattedMessage"],{id:"app.analysis.search-users",defaultMessage:"search users"}),c.a.createElement(i["a"],{title:c.a.createElement(m["FormattedMessage"],{id:"app.analysis.introduce",defaultMessage:"introduce"})},c.a.createElement(o["a"],{style:{marginLeft:8},type:"info-circle-o"}))),gap:8,total:p()(12321).format("0,0"),status:"up",subTotal:17.1}),c.a.createElement(h["d"],{line:!0,height:45,data:t})),c.a.createElement(l["a"],{sm:12,xs:24,style:{marginBottom:24}},c.a.createElement(f["a"],{subTitle:c.a.createElement("span",null,c.a.createElement(m["FormattedMessage"],{id:"app.analysis.per-capita-search",defaultMessage:"Per Capita Search"}),c.a.createElement(i["a"],{title:c.a.createElement(m["FormattedMessage"],{id:"app.analysis.introduce",defaultMessage:"introduce"})},c.a.createElement(o["a"],{style:{marginLeft:8},type:"info-circle-o"}))),total:2.7,status:"down",subTotal:26.2,gap:8}),c.a.createElement(h["d"],{line:!0,height:45,data:t}))),c.a.createElement(r["a"],{rowKey:function(e){return e.index},size:"small",columns:M,dataSource:d,pagination:{style:{marginBottom:0},pageSize:5}}))});a["default"]=w}}]);