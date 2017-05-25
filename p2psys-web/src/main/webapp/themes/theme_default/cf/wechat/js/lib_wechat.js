var Vue = window.Vue || {};
if(typeof Vue.filter !=='function'){Vue.filter=new Function();}

//Vue过滤器，添加特殊字符
Vue.filter('addPer', function(value,before,after) {
    return before + value + (after || '');
});

// Vue过滤器，金额格式化（数字，精确度【默认为0】，分割符）
Vue.filter('moneyFormat', function(num, precision, separator) {
    var parts;
    if (!isNaN(parseFloat(num)) && isFinite(num)) {
        num = Number(num);
        num = (typeof precision !== 'undefined' ? num.toFixed(precision) : num).toString();
        parts = num.split('.');
        parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1' + (separator || ','));
        return parts.join('.');
    }
    return 0;
})

/**      
 * 对Date的扩展，将 Date 转化为指定格式的String      
 * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符      
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)      
 * eg:      
 * (new Date()).pattern("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423      
 * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04      
 * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04      
 * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04      
 * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18      
 */        
Date.prototype.pattern=function(fmt) {         
    var o = {         
    "M+" : this.getMonth()+1, //月份         
    "d+" : this.getDate(), //日         
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
    "H+" : this.getHours(), //小时         
    "m+" : this.getMinutes(), //分         
    "s+" : this.getSeconds(), //秒         
    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
    "S" : this.getMilliseconds() //毫秒         
    };         
    var week = {         
    "0" : "日",         
    "1" : "一",         
    "2" : "二",         
    "3" : "三",         
    "4" : "四",         
    "5" : "五",         
    "6" : "六"        
    };         
    if(/(y+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
    }         
    if(/(E+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "星期" : "周") : "")+week[this.getDay()+""]);         
    }         
    for(var k in o){         
        if(new RegExp("("+ k +")").test(fmt)){         
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        }         
    }         
    return fmt;         
}

// Vue过滤器，日期格式化，fmt格式见上面
Vue.filter('timeFormat', function(value, fmt) {
    value=new Date(+value);
    return value.pattern(fmt);
})

/**
 * 金额化万元
 */
Vue.filter('thousandFormat', function(value) {
	value = parseFloat(value);
	if(value>=10000){
		return parseFloat(value/10000).toFixed(0)+"万";
	}else{
		return value+"元";
	}
})

/**
 * 获取融资轮次文本
 */
Vue.filter('financingText', function(value) {
	if(1==value){
		return 	'种子轮';
	}else if(2==value){
		return '天使轮';
	}else if(3==value){
		return 'Pre-A轮';
	}else if(4==value){
		return 'A轮';
	}else if(5==value){
		return 'Pre-B轮';
	}else if(6==value){
		return 'B轮';
	}else if(7==value){
		return "C轮";
	}else if(8==value){
		return 'D轮';
	}else{
		return "Pre-IPO轮";
	}
})

/**
 * 获取项目进度
 */
Vue.filter('projectStep', function(value) {
	if(1==value){
		 step = '预热中';
	}else if(2==value){
		 step = '众筹中';
	}else if(3==value){
		 step = '已过期';
	}else{
		 step = '已完成';
	}
	return step;
})

/**
 * 平台密码校验
 * @param pwd
 * @returns {String}
 */
function validatePwd(pwd){
    if(pwd.length==0){
       return '密码不能为空！';
    }    
    if(pwd.length<8){
        return '密码至少8位！';
    }
    if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd)){
    	return '密码必须是字母与数字组合！';
    }
    return 'ok';
}

/**
 * 平台交易密码校验
 * @param pwd
 * @returns {Boolean}
 */
function validatePayPwd(pwd){
	var myreg = /^[0-9\ ]+$/;
    if(pwd.length==0){
       return '请输入原始交易密码！';
    }    
    if(pwd.length!=6){
        return '请输入6位有效数字交易密码！';
    }
    if(!myreg.test(pwd)){
        return '交易密码必须为有效的数字！';
    }
    return 'ok';
}

/**
 * 获取除法运算结果
 * @param a
 * @param b
 * @returns
 */
function getDivisionResult(a,b){
	var _a = parseFloat(a);
	var _b = parseFloat(b);
	return parseFloat(_a/_b).toFixed(2);
}

/**
 * 获取url中参数
 * @param key
 * @returns
 */
function getElementByUrl(key){
    var url=location.search;  
    var Request = new Object();  
    if(url.indexOf("?")!=-1){  
           var str = url.substr(1);  
           strs= str.split("&");  
           for(var i=0;i<str.length;i++){  
              Request[strs[i].split("=")[0]]=(strs[i].split("=")[1]);  
          }  
    }  
    return Request[key];    
}

// 为避免命名冲突，库函数使用命名空间
// 基本函数写到fn中，其他需求自行扩展xlib对象
window.xlib={
    v:'1.0'
}

xlib.fn={
    desc:'工具函数',
    eg:function () {
        alert('welcome !');
    },
    eg2:function (a) {
        alert('hello, '+a);
    }
}

// 调用方式：
// xlib.fn.eg()  //输出welcome ！
// xlib.fn.eg('xlib')  //输出hello, xlib


//常用正则表达式：
//手机号正则：/^1[34578]\d{9}$/
//密码正则（8到16位必须包含字母和数字）：/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/
//交易密码正则（必须为6位数字）：/^\d{6}$/

//为避免公用库文件过大，与业务逻辑相关的函数拆分到对应的页面中，
//或者页面公用逻辑库中，库文件只保留最基本的库函数。
