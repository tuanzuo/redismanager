(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[7],{"5WY0":function(e,t,a){e.exports={main:"antd-pro-pages-user-register-main",getCaptcha:"antd-pro-pages-user-register-getCaptcha",submit:"antd-pro-pages-user-register-submit",login:"antd-pro-pages-user-register-login",error:"antd-pro-pages-user-register-error",success:"antd-pro-pages-user-register-success",warning:"antd-pro-pages-user-register-warning","progress-pass":"antd-pro-pages-user-register-progress-pass",progress:"antd-pro-pages-user-register-progress"}},cq3J:function(e,t,a){"use strict";a.r(t);a("kcJG");var r,s,n,i,o=a("XBJ2"),l=(a("BLL0"),a("Os1Z")),c=(a("sR1m"),a("4PY0")),p=(a("HJnI"),a("PCLf")),m=(a("rHl/"),a("53e8")),d=a("mK77"),u=a.n(d),g=a("43Yg"),f=a.n(g),h=a("/tCh"),v=a.n(h),w=a("8aBX"),E=a.n(w),b=a("scpF"),y=a.n(b),M=a("O/V9"),O=a.n(M),S=(a("CH3h"),a("oomf")),k=(a("ie9k"),a("EvS/")),P=(a("tW+f"),a("fZ2R")),j=a("ZZRV"),C=a.n(j),F=a("LneV"),q=a("wqNP"),x=a("szSh"),N=a.n(x),z=a("Sr5h"),D=a.n(z),R=a("5WY0"),V=a.n(R);function I(e){var t=J();return function(){var a,r=O()(e);if(t){var s=O()(this).constructor;a=Reflect.construct(r,arguments,s)}else a=r.apply(this,arguments);return y()(this,a)}}function J(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch(e){return!1}}var L=P["a"].Item,W=k["a"].Option,B=S["a"].Group,G={ok:C.a.createElement("div",{className:V.a.success},C.a.createElement(q["FormattedMessage"],{id:"validation.password.strength.strong"})),pass:C.a.createElement("div",{className:V.a.warning},C.a.createElement(q["FormattedMessage"],{id:"validation.password.strength.medium"})),poor:C.a.createElement("div",{className:V.a.error},C.a.createElement(q["FormattedMessage"],{id:"validation.password.strength.short"}))},Y={ok:"success",pass:"normal",poor:"exception"},Z=(r=Object(F["connect"])(function(e){var t=e.register,a=e.loading;return{register:t,submitting:a.effects["register/submit"]}}),s=P["a"].create(),r(n=s((i=function(e){E()(a,e);var t=I(a);function a(){var e;f()(this,a);for(var r=arguments.length,s=new Array(r),n=0;n<r;n++)s[n]=arguments[n];return e=t.call.apply(t,[this].concat(s)),e.state={count:0,confirmDirty:!1,visible:!1,help:"",prefix:"86"},e.onGetCaptcha=function(){var t=59;e.setState({count:t}),e.interval=setInterval(function(){t-=1,e.setState({count:t}),0===t&&clearInterval(e.interval)},1e3)},e.getPasswordStatus=function(){var t=e.props.form,a=t.getFieldValue("password");return a&&a.length>9?"ok":a&&a.length>5?"pass":"poor"},e.handleSubmit=function(t){t.preventDefault();var a=e.props,r=a.form,s=a.dispatch;r.validateFields({force:!0},function(t,a){if(!t){var r=e.state.prefix;s({type:"register/submit",payload:u()({},a,{prefix:r})})}})},e.handleConfirmBlur=function(t){var a=t.target.value,r=e.state.confirmDirty;e.setState({confirmDirty:r||!!a})},e.checkConfirm=function(t,a,r){var s=e.props.form;a&&a!==s.getFieldValue("password")?r(Object(q["formatMessage"])({id:"validation.password.twice"})):r()},e.checkPassword=function(t,a,r){var s=e.state,n=s.visible,i=s.confirmDirty;if(a)if(e.setState({help:""}),n||e.setState({visible:!!a}),a.length<6)r("error");else{var o=e.props.form;a&&i&&o.validateFields(["confirm"],{force:!0}),r()}else e.setState({help:Object(q["formatMessage"])({id:"validation.password.required"}),visible:!!a}),r("error")},e.changePrefix=function(t){e.setState({prefix:t})},e.renderPasswordProgress=function(){var t=e.props.form,a=t.getFieldValue("password"),r=e.getPasswordStatus();return a&&a.length?C.a.createElement("div",{className:V.a["progress-".concat(r)]},C.a.createElement(m["a"],{status:Y[r],className:V.a.progress,strokeWidth:6,percent:10*a.length>100?100:10*a.length,showInfo:!1})):null},e}return v()(a,[{key:"componentDidUpdate",value:function(){var e=this.props,t=e.form,a=e.register,r=t.getFieldValue("mail");"ok"===a.status&&D.a.push({pathname:"/user/register-result",state:{account:r}})}},{key:"componentWillUnmount",value:function(){clearInterval(this.interval)}},{key:"render",value:function(){var e=this.props,t=e.form,a=e.submitting,r=t.getFieldDecorator,s=this.state,n=s.count,i=s.prefix,m=s.help,d=s.visible;return C.a.createElement("div",{className:V.a.main},C.a.createElement("h3",null,C.a.createElement(q["FormattedMessage"],{id:"app.register.register"})),C.a.createElement(P["a"],{onSubmit:this.handleSubmit},C.a.createElement(L,null,r("mail",{rules:[{required:!0,message:Object(q["formatMessage"])({id:"validation.email.required"})},{type:"email",message:Object(q["formatMessage"])({id:"validation.email.wrong-format"})}]})(C.a.createElement(S["a"],{size:"large",placeholder:Object(q["formatMessage"])({id:"form.email.placeholder"})}))),C.a.createElement(L,{help:m},C.a.createElement(p["a"],{getPopupContainer:function(e){return e.parentNode},content:C.a.createElement("div",{style:{padding:"4px 0"}},G[this.getPasswordStatus()],this.renderPasswordProgress(),C.a.createElement("div",{style:{marginTop:10}},C.a.createElement(q["FormattedMessage"],{id:"validation.password.strength.msg"}))),overlayStyle:{width:240},placement:"right",visible:d},r("password",{rules:[{validator:this.checkPassword}]})(C.a.createElement(S["a"],{size:"large",type:"password",placeholder:Object(q["formatMessage"])({id:"form.password.placeholder"})})))),C.a.createElement(L,null,r("confirm",{rules:[{required:!0,message:Object(q["formatMessage"])({id:"validation.confirm-password.required"})},{validator:this.checkConfirm}]})(C.a.createElement(S["a"],{size:"large",type:"password",placeholder:Object(q["formatMessage"])({id:"form.confirm-password.placeholder"})}))),C.a.createElement(L,null,C.a.createElement(B,{compact:!0},C.a.createElement(k["a"],{size:"large",value:i,onChange:this.changePrefix,style:{width:"20%"}},C.a.createElement(W,{value:"86"},"+86"),C.a.createElement(W,{value:"87"},"+87")),r("mobile",{rules:[{required:!0,message:Object(q["formatMessage"])({id:"validation.phone-number.required"})},{pattern:/^\d{11}$/,message:Object(q["formatMessage"])({id:"validation.phone-number.wrong-format"})}]})(C.a.createElement(S["a"],{size:"large",style:{width:"80%"},placeholder:Object(q["formatMessage"])({id:"form.phone-number.placeholder"})})))),C.a.createElement(L,null,C.a.createElement(o["a"],{gutter:8},C.a.createElement(c["a"],{span:16},r("captcha",{rules:[{required:!0,message:Object(q["formatMessage"])({id:"validation.verification-code.required"})}]})(C.a.createElement(S["a"],{size:"large",placeholder:Object(q["formatMessage"])({id:"form.verification-code.placeholder"})}))),C.a.createElement(c["a"],{span:8},C.a.createElement(l["a"],{size:"large",disabled:n,className:V.a.getCaptcha,onClick:this.onGetCaptcha},n?"".concat(n," s"):Object(q["formatMessage"])({id:"app.register.get-verification-code"}))))),C.a.createElement(L,null,C.a.createElement(l["a"],{size:"large",loading:a,className:V.a.submit,type:"primary",htmlType:"submit"},C.a.createElement(q["FormattedMessage"],{id:"app.register.register"})),C.a.createElement(N.a,{className:V.a.login,to:"/User/Login"},C.a.createElement(q["FormattedMessage"],{id:"app.register.sign-in"})))))}}]),a}(j["Component"]),n=i))||n)||n);t["default"]=Z}}]);