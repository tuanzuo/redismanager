(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[6],{JAxp:function(e,t,n){e.exports={login:"antd-pro-components-login-index-login",getCaptcha:"antd-pro-components-login-index-getCaptcha",icon:"antd-pro-components-login-index-icon",other:"antd-pro-components-login-index-other",register:"antd-pro-components-login-index-register",prefixIcon:"antd-pro-components-login-index-prefixIcon",submit:"antd-pro-components-login-index-submit"}},Y5yc:function(e,t,n){"use strict";n.r(t);n("sXHz");var a=n("fIvO"),r=(n("aPXm"),n("nF3z")),o=n("mK77"),c=n.n(o),i=n("43Yg"),s=n.n(i),u=n("/tCh"),l=n.n(u),p=n("8aBX"),f=n.n(p),m=n("scpF"),d=n.n(m),h=n("O/V9"),g=n.n(h),v=n("ZZRV"),y=n.n(v),b=n("LneV"),C=n("wqNP"),E=(n("tW+f"),n("fZ2R")),x=(n("fg+8"),n("37yK")),w=n("rXjv"),S=n.n(w),R=(n("EH+i"),n("iczh")),D=n.n(R),N=(n("kcJG"),n("XBJ2")),P=(n("BLL0"),n("Os1Z")),T=(n("sR1m"),n("4PY0")),k=(n("CH3h"),n("oomf")),I=n("zAuD"),M=n.n(I),O=n("BG4o"),A=n.n(O),q=n("B1rl"),F=n("JAxp"),j=n.n(F),z=(n("+lKC"),n("yZ8h")),G={UserName:{props:{size:"large",id:"userName",prefix:y.a.createElement(z["a"],{type:"user",className:j.a.prefixIcon}),placeholder:"admin"},rules:[{required:!0,message:"Please enter username!"}]},Password:{props:{size:"large",prefix:y.a.createElement(z["a"],{type:"lock",className:j.a.prefixIcon}),type:"password",id:"password",placeholder:"888888"},rules:[{required:!0,message:"Please enter password!"}]},Mobile:{props:{size:"large",prefix:y.a.createElement(z["a"],{type:"mobile",className:j.a.prefixIcon}),placeholder:"mobile number"},rules:[{required:!0,message:"Please enter mobile number!"},{pattern:/^1\d{10}$/,message:"Wrong mobile number format!"}]},Captcha:{props:{size:"large",prefix:y.a.createElement(z["a"],{type:"mail",className:j.a.prefixIcon}),placeholder:"captcha"},rules:[{required:!0,message:"Please enter Captcha!"}]}},B=Object(v["createContext"])(),L=B;function K(e){var t=U();return function(){var n,a=g()(e);if(t){var r=g()(this).constructor;n=Reflect.construct(a,arguments,r)}else n=a.apply(this,arguments);return d()(this,n)}}function U(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch(e){return!1}}var V=E["a"].Item,J=function(e){f()(n,e);var t=K(n);function n(e){var a;return s()(this,n),a=t.call(this,e),a.onGetCaptcha=function(){var e=a.props.onGetCaptcha,t=e?e():null;!1!==t&&(t instanceof Promise?t.then(a.runGetCaptchaCountDown):a.runGetCaptchaCountDown())},a.getFormItemOptions=function(e){var t=e.onChange,n=e.defaultValue,a=e.customprops,r=e.rules,o={rules:r||a.rules};return t&&(o.onChange=t),n&&(o.initialValue=n),o},a.runGetCaptchaCountDown=function(){var e=a.props.countDown,t=e||59;a.setState({count:t}),a.interval=setInterval(function(){t-=1,a.setState({count:t}),0===t&&clearInterval(a.interval)},1e3)},a.state={count:0},a}return l()(n,[{key:"componentDidMount",value:function(){var e=this.props,t=e.updateActive,n=e.name;t&&t(n)}},{key:"componentWillUnmount",value:function(){clearInterval(this.interval)}},{key:"render",value:function(){var e=this.state.count,t=this.props.form.getFieldDecorator,n=this.props,a=(n.onChange,n.customprops),r=(n.defaultValue,n.rules,n.name),o=n.getCaptchaButtonText,c=n.getCaptchaSecondText,i=(n.updateActive,n.type),s=A()(n,["onChange","customprops","defaultValue","rules","name","getCaptchaButtonText","getCaptchaSecondText","updateActive","type"]),u=this.getFormItemOptions(this.props),l=s||{};if("Captcha"===i){var p=Object(q["a"])(l,["onGetCaptcha","countDown"]);return y.a.createElement(V,null,y.a.createElement(N["a"],{gutter:8},y.a.createElement(T["a"],{span:16},t(r,u)(y.a.createElement(k["a"],M()({},a,p)))),y.a.createElement(T["a"],{span:8},y.a.createElement(P["a"],{disabled:e,className:j.a.getCaptcha,size:"large",onClick:this.onGetCaptcha},e?"".concat(e," ").concat(c):o))))}return y.a.createElement(V,null,t(r,u)(y.a.createElement(k["a"],M()({},a,l))))}}]),n}(v["Component"]);J.defaultProps={getCaptchaButtonText:"captcha",getCaptchaSecondText:"second"};var X={};Object.keys(G).forEach(function(e){var t=G[e];X[e]=function(n){return y.a.createElement(L.Consumer,null,function(a){return y.a.createElement(J,M()({customprops:t.props,rules:t.rules},n,{type:e,updateActive:a.updateActive,form:a.form}))})}});var Z=X;function H(e){var t=W();return function(){var n,a=g()(e);if(t){var r=g()(this).constructor;n=Reflect.construct(a,arguments,r)}else n=a.apply(this,arguments);return d()(this,n)}}function W(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch(e){return!1}}var Y=x["a"].TabPane,$=function(){var e=0;return function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:"";return e+=1,"".concat(t).concat(e)}}(),Q=function(e){f()(n,e);var t=H(n);function n(e){var a;return s()(this,n),a=t.call(this,e),a.uniqueId=$("login-tab-"),a}return l()(n,[{key:"componentDidMount",value:function(){var e=this.props.tabUtil;e.addTab(this.uniqueId)}},{key:"render",value:function(){var e=this.props.children;return y.a.createElement(Y,this.props,e)}}]),n}(v["Component"]),_=function(e){return y.a.createElement(L.Consumer,null,function(t){return y.a.createElement(Q,M()({tabUtil:t.tabUtil},e))})};_.typeName="LoginTab";var ee=_,te=E["a"].Item,ne=function(e){var t=e.className,n=A()(e,["className"]),a=D()(j.a.submit,t);return y.a.createElement(te,null,y.a.createElement(P["a"],M()({size:"large",className:a,type:"primary",htmlType:"submit"},n)))},ae=ne;function re(e){var t=oe();return function(){var n,a=g()(e);if(t){var r=g()(this).constructor;n=Reflect.construct(a,arguments,r)}else n=a.apply(this,arguments);return d()(this,n)}}function oe(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch(e){return!1}}var ce=function(e){f()(n,e);var t=re(n);function n(e){var a;return s()(this,n),a=t.call(this,e),a.onSwitch=function(e){a.setState({type:e});var t=a.props.onTabChange;t(e)},a.getContext=function(){var e=a.state.tabs,t=a.props.form;return{tabUtil:{addTab:function(t){a.setState({tabs:[].concat(S()(e),[t])})},removeTab:function(t){a.setState({tabs:e.filter(function(e){return e!==t})})}},form:t,updateActive:function(e){var t=a.state,n=t.type,r=t.active;r[n]?r[n].push(e):r[n]=[e],a.setState({active:r})}}},a.handleSubmit=function(e){e.preventDefault();var t=a.state,n=t.active,r=t.type,o=a.props,c=o.form,i=o.onSubmit,s=n[r];c.validateFields(s,{force:!0},function(e,t){i(e,t)})},a.state={type:e.defaultActiveKey,tabs:[],active:{}},a}return l()(n,[{key:"render",value:function(){var e=this.props,t=e.className,n=e.children,a=this.state,r=a.type,o=a.tabs,c=[],i=[];return y.a.Children.forEach(n,function(e){e&&("LoginTab"===e.type.typeName?c.push(e):i.push(e))}),y.a.createElement(L.Provider,{value:this.getContext()},y.a.createElement("div",{className:D()(t,j.a.login)},y.a.createElement(E["a"],{onSubmit:this.handleSubmit},o.length?y.a.createElement(y.a.Fragment,null,y.a.createElement(x["a"],{animated:!1,className:j.a.tabs,activeKey:r,onChange:this.onSwitch},c),i):n)))}}]),n}(v["Component"]);ce.defaultProps={className:"",defaultActiveKey:"",onTabChange:function(){},onSubmit:function(){}},ce.Tab=ee,ce.Submit=ae,Object.keys(Z).forEach(function(e){ce[e]=Z[e]});var ie,se,ue,le=E["a"].create()(ce),pe=n("w2qy"),fe=n.n(pe);n("zDPs");function me(e){var t=de();return function(){var n,a=g()(e);if(t){var r=g()(this).constructor;n=Reflect.construct(a,arguments,r)}else n=a.apply(this,arguments);return d()(this,n)}}function de(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch(e){return!1}}var he=le.Tab,ge=le.UserName,ve=le.Password,ye=(le.Mobile,le.Captcha,le.Submit),be=(ie=Object(b["connect"])(function(e){var t=e.login,n=e.loading;return{login:t,submitting:n.effects["login/login"]}}),ie((ue=function(e){f()(n,e);var t=me(n);function n(){var e;s()(this,n);for(var o=arguments.length,i=new Array(o),u=0;u<o;u++)i[u]=arguments[u];return e=t.call.apply(t,[this].concat(i)),e.state={type:"account",autoLogin:!0},e.onTabChange=function(t){e.setState({type:t})},e.onGetCaptcha=function(){return new Promise(function(t,n){e.loginForm.validateFields(["mobile"],{},function(a,r){if(a)n(a);else{var o=e.props.dispatch;o({type:"login/getCaptcha",payload:r.mobile}).then(t).catch(n)}})})},e.handleSubmit=function(t,n){var a=e.state.type;if(!t){var r=e.props.dispatch;r({type:"login/login",payload:c()({},n,{type:a}),callback:function(t){e.tipMsg(t)}})}},e.tipMsg=function(e){var t=!1,n="warning",a="\u767b\u5f55\u5931\u8d25! ",o=4.5;return e&&"200"==e.code?(t=!0,t):(e&&e.msg&&""!=e.msg&&(a+=e.msg,o=10),r["a"][n]({message:"\u63d0\u793a\u4fe1\u606f",description:a,duration:o}),t)},e.changeAutoLogin=function(t){e.setState({autoLogin:t.target.checked})},e.renderMessage=function(e){return y.a.createElement(a["a"],{style:{marginBottom:24},message:e,type:"error",showIcon:!0})},e}return l()(n,[{key:"componentDidMount",value:function(){}},{key:"render",value:function(){var e=this,t=this.props,n=t.login,a=t.submitting,r=this.state,o=r.type;r.autoLogin;return y.a.createElement("div",{className:fe.a.main},y.a.createElement(le,{defaultActiveKey:o,onTabChange:this.onTabChange,onSubmit:this.handleSubmit,ref:function(t){e.loginForm=t}},y.a.createElement(he,{key:"account",tab:Object(C["formatMessage"])({id:"app.login.tab-login-credentials"})},"error"===n.status&&"account"===n.type&&!a&&this.renderMessage(Object(C["formatMessage"])({id:"app.login.message-invalid-credentials"})),y.a.createElement(ge,{name:"name",placeholder:"".concat(Object(C["formatMessage"])({id:"app.login.userName"}),": admin or test or develop"),rules:[{required:!0,message:Object(C["formatMessage"])({id:"validation.userName.required"})}]}),y.a.createElement(ve,{name:"pwd",placeholder:"".concat(Object(C["formatMessage"])({id:"app.login.password"}),": admin or test or develop"),rules:[{required:!0,message:Object(C["formatMessage"])({id:"validation.password.required"})}],onPressEnter:function(t){t.preventDefault(),e.loginForm.validateFields(e.handleSubmit)}})),y.a.createElement(ye,{loading:a},y.a.createElement(C["FormattedMessage"],{id:"app.login.login"}))))}}]),n}(v["Component"]),se=ue))||se);t["default"]=be},w2qy:function(e,t,n){e.exports={main:"antd-pro-pages-user-login-main",icon:"antd-pro-pages-user-login-icon",other:"antd-pro-pages-user-login-other",register:"antd-pro-pages-user-login-register"}}}]);