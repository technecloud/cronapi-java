!function(){window.parseXml=function(e){var n=null;if(window.DOMParser)try{n=(new DOMParser).parseFromString(e,"text/xml")}catch(e){n=null}else if(window.ActiveXObject)try{n=new ActiveXObject("Microsoft.XMLDOM"),n.async=!1,n.loadXML(e)||console.log(n.parseError.reason+n.parseError.srcText)}catch(e){n=null}return n},window.xml2json=function(e,n){var t={toObj:function(e){var n={};if(1==e.nodeType){if(e.attributes.length)for(var r=0;r<e.attributes.length;r++)n["@"+e.attributes[r].nodeName]=(e.attributes[r].nodeValue||"").toString();if(e.firstChild){for(var o=0,i=0,a=!1,l=e.firstChild;l;l=l.nextSibling)1==l.nodeType?a=!0:3==l.nodeType&&l.nodeValue.match(/[^ \f\n\r\t\v]/)?o++:4==l.nodeType&&i++;if(a)if(o<2&&i<2){t.removeWhite(e);for(var l=e.firstChild;l;l=l.nextSibling)3==l.nodeType?n["#text"]=t.escape(l.nodeValue):4==l.nodeType?n["#cdata"]=t.escape(l.nodeValue):n[t.normalizeName(l.nodeName)]?n[t.normalizeName(l.nodeName)]instanceof Array?n[t.normalizeName(l.nodeName)][n[t.normalizeName(l.nodeName)].length]=t.toObj(l):n[t.normalizeName(l.nodeName)]=[n[t.normalizeName(l.nodeName)],t.toObj(l)]:n[t.normalizeName(l.nodeName)]=t.toObj(l)}else e.attributes.length?n["#text"]=t.escape(t.innerXml(e)):n=t.escape(t.innerXml(e));else if(o)e.attributes.length?n["#text"]=t.escape(t.innerXml(e)):n=t.escape(t.innerXml(e));else if(i)if(i>1)n=t.escape(t.innerXml(e));else for(var l=e.firstChild;l;l=l.nextSibling)n["#cdata"]=t.escape(l.nodeValue)}e.attributes.length||e.firstChild||(n=null)}else 9==e.nodeType?n=t.toObj(e.documentElement):console.log("unhandled node type: "+e.nodeType);return n},normalizeName:function(e){if(e&&e.length>5&&0==e.indexOf("cron-")){e=e.substr(5);var n="",t=!1;for(var r in e){var o=e.charAt(r);t&&"-"!=o?(n+=o.toUpperCase(),t=!1):"-"==o?t=!0:n+=o.toLowerCase()}return n}},toJson:function(e,n,r){var o=n?'"'+n+'"':"";if(e instanceof Array){for(var i=0,a=e.length;i<a;i++)e[i]=t.toJson(e[i],"",r+"\t");o+=(n?":[":"[")+(e.length>1?"\n"+r+"\t"+e.join(",\n"+r+"\t")+"\n"+r:e.join(""))+"]"}else if(null==e)o+=(n&&":")+"null";else if("object"==typeof e){var l=[];for(var s in e)l[l.length]=t.toJson(e[s],s,r+"\t");o+=(n?":{":"{")+(l.length>1?"\n"+r+"\t"+l.join(",\n"+r+"\t")+"\n"+r:l.join(""))+"}"}else if("string"==typeof e){var c=$.trim(e.toString());o+=(n&&":")+'"'+t.htmlDecode(c)+'"'}else o+=(n&&":")+t.htmlDecode($.trim(e.toString()));return o},htmlDecode:function(e){return(new DOMParser).parseFromString(e,"text/html").documentElement.textContent},innerXml:function(e){var n="";if("innerHTML"in e)n=e.innerHTML;else for(var t=function(e){var n="";if(1==e.nodeType){n+="<"+e.nodeName;for(var r=0;r<e.attributes.length;r++)n+=" "+e.attributes[r].nodeName+'="'+(e.attributes[r].nodeValue||"").toString()+'"';if(e.firstChild){n+=">";for(var o=e.firstChild;o;o=o.nextSibling)n+=t(o);n+="</"+e.nodeName+">"}else n+="/>"}else 3==e.nodeType?n+=e.nodeValue:4==e.nodeType&&(n+="<![CDATA["+e.nodeValue+"]]>");return n},r=e.firstChild;r;r=r.nextSibling)n+=t(r);return n},escape:function(e){return e.trim().replace(/[\\]/g,"\\\\").replace(/[\"]/g,'\\"').replace(/[\n]/g,"\\n").replace(/[\r]/g,"\\r")},removeWhite:function(e){e.normalize();for(var n=e.firstChild;n;)if(3==n.nodeType)if(n.nodeValue.match(/[^ \f\n\r\t\v]/))n=n.nextSibling;else{var r=n.nextSibling;e.removeChild(n),n=r}else 1==n.nodeType?(t.removeWhite(n),n=n.nextSibling):n=n.nextSibling;return e}};9==e.nodeType&&(e=e.documentElement);var r=t.toJson(t.toObj(t.removeWhite(e)),e.nodeName,"\t");return"{\n"+n+(n?r.replace(/\t/g,n):r.replace(/\t|\n/g,""))+"\n}"},window.json2xml=function(e,n){var t=e,r=document.createElement(n),o=function(e){return{}.toString.call(e).split(" ")[1].slice(0,-1).toLowerCase()},i=function(e){var n="";for(var t in e){var r=e.charAt(t);t>0&&r==r.toUpperCase()&&(n+="-"),n+=r.toLowerCase()}return"cron-"+n},a=function(e,n,t,r){if("array"!=o(t)&&"object"!=o(t))"null"!=o(t)&&n.appendChild(document.createTextNode(t));else for(var a in t){var l=t[a];if("__type"==a&&"object"==o(t))n.setAttribute("__type",l);else if("object"==o(l)){var s=n.appendChild(document.createElementNS(null,i(a)));e(e,s,l)}else if("array"==o(l))for(var c in l){var s=n.appendChild(document.createElementNS(null,i(a)));e(e,s,l[c],!0)}else{var u=document.createElementNS(null,i(a));"null"!=o(l)&&("string"==o(l)&&(l=l.trim()),u.appendChild(document.createTextNode(l)));var s=n.appendChild(u)}}};return a(a,r,t,o(t)),r.outerHTML},window.buildElementOptions=function(e){var n=$(e).closest("[data-component]").find("cron-options"),t=parseXml("<cron-options>"+$(n).html()+"</cron-options>"),r=xml2json(t);return r&&(r=r.slice(1),r=r.substring(0,r.length-1),r=r.trim(),r=r.replace(/undefined"cron-options":/gm,""),r=r.replace(/"undefined"/gm,""),r=r.replace(/"undefined:"/gm,""),r=r.replace(/undefined:/gm,""),r=r.replace(/undefined/gm,""),r=r.replace(/"cron-options":/gm,"")),r},window.objectClone=function(e,n){var t;if(null==e||"object"!=typeof e)return e;if(e instanceof Date)return t=new Date,t.setTime(e.getTime()),t;if(e instanceof Array){t=[];for(var r=0,o=e.length;r<o;r++)t[r]=objectClone(e[r],void 0);return t}if(e instanceof Object){t={};for(var i in e)e.hasOwnProperty(i)&&void 0!=e[i]&&"_"!=i.substr(0,1)&&!function(e){return e&&"[object Function]"==={}.toString.call(e)}(e[i])&&function(e,n){if(n){for(var t in n)if(e==t)return!0;return!1}return!0}(i,n)&&(t[i]=objectClone(e[i],n[i]));return t}throw new Error("Unable to copy obj! Its type isn't supported.")},window.getOperatorODATA=function(e){return"="==e?" eq ":"!="==e?" ne ":">"==e?" gt ":">="==e?" ge ":"<"==e?" lt ":"<="==e?" le ":void 0},window.executeRight=function(right){var result="null";return null!=right&&(right.startsWith(":")?result=right:(result=eval(right))instanceof Date&&(result="datetimeoffset'"+result.toISOString()+"'")),result},window.parserOdata=function(e){for(var n="",t=e.type,r=0;r<e.args.length;r++){var o=e.args[r],i=t;0==r&&(i=""),n=o.args&&o.args.length>0?n+" "+i.toLowerCase()+" ( "+parserOdata(o)+" ) ":n+" "+i.toLowerCase()+" "+o.left+getOperatorODATA(o.type)+executeRight(o.right)}return n.trim()}}();