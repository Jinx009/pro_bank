var client_ip = "127.0.0.1";

/**
 * url参数格式化
 * @param str
 * @param is_global
 * @returns
 */
function Trim(str,is_global){
   var result;
   result = str.replace(/(^\s+)|(\s+$)/g,"");
   if(is_global.toLowerCase()=="g") result = result.replace(/\s/g,"");
   return result;
}

/**
 * 去除换行
 * @param key
 * @returns
 */
function clearBr(key){
   key = Trim(key,"g");
   key = key.replace(/<\/?.+?>/g,"");
   key = key.replace(/[\r\n]/g, "");
   return key;
}

/**
 * 获取随机数
 * @returns
 */
function getANumber(){
   var date = new Date();
   var times1970 = date.getTime();
   var times = date.getDate() + "" + date.getHours() + "" + date.getMinutes() + "" + date.getSeconds();
   var encrypt = times * times1970;
   if(arguments.length == 1){
 	  return arguments[0] + encrypt;
   }else{
      return encrypt;
   }
}

var oldPackageString;

/**
 * 获取appId
 * @returns {String}
 */
function getAppId(){
    return "wxad7987ba40989a97";
}


var oldTimeStamp ;
var oldNonceStr ; 

/**
 * 获取时间戳
 * @returns
 */
function getTimeStamp(){
    var timestamp=new Date().getTime();
    var timestampstring = timestamp.toString();//一定要转换字符串
    oldTimeStamp = timestampstring;
    return timestampstring;
}

/**
 * 获取随机戳
 * @returns {String}
 */
function getNonceStr(){
     var $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
     var maxPos = $chars.length;
     var noceStr = "";
     for (i = 0; i < 32; i++) {
          noceStr += $chars.charAt(Math.floor(Math.random() * maxPos));
     }
     oldNonceStr = noceStr;
     return noceStr;
}

/**
 * 获取加密方式
 * @returns {String}
 */
function getSignType(){
     return "MD5";
}
var time;
var nonce;
var sign;

/**
 * 签名
 * @returns {___anonymous_sign}
 */
function getSign(){
	 time = getTimeStamp();
	 nonce = getNonceStr();
     var params = "appId=wxad7987ba40989a97&nonceStr="+nonce+"&package=prepay_id="+pay_id+"&signType=MD5&timeStamp="+time+"&key=jinxjinxjinxjinxjinxjinxjinxjinx";
    // alert(params);
     sign = CryptoJS.MD5(params).toString().toUpperCase();
     
     return sign;
}

var pay_id;

/**
 * 支付回调
 */
function callPlay(){
   var status = false;
	var payPwd = $('#payPwd').val(),
	id = $('#projectId').val();
	var profit = $('#ruleId').val();
	if($('#profit').length){
		profit = $('#profit').val();
	}
	var params = 'payPwd='+payPwd+'&investMoney='+money+'&virtual='+virtual+'&id='+id+'&profitRule='+profit+'&payType=1'; 
	$.ajax({
		url:'/cf/user/buyData.action',
		type:'POST',
		data:params,
		dataType:'json',
		success:function(res){
			if('success'==res.result){
				sign = getSign();
				WeixinJSBridge.invoke('getBrandWCPayRequest', {
			           "appId":"wxad7987ba40989a97",    
			           "timeStamp":time,        
			           "nonceStr":nonce, 
			           "package":"prepay_id="+pay_id,     
			           "signType":"MD5",           
			           "paySign":sign 
			       },
			       function(res){     
			    	   WeixinJSBridge.log(res.err_msg);
			           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
			        	 //实物众筹
			        	    $.ajax({
			        	    	url:'/wechat/doWechatPay.action',
			        	    	data:params,
			        	    	type:'POST',
			        	    	dataType:'json',
			        	    	success:function(res){
			        	    		if(200==res.code){
			        	    			layer.alert('恭喜您支持成功！',{title:false,closeBtn: 0},function(){
											location.href = '/cf/wechat/user/buyList.html';
										});
			        	    		}else{
			        	    			layer.alert('微信支付失败，请联系客服！',{title:false,closeBtn: 0});
			        	    		}
			        	    	}
			        	    })
			           }     
			       }
			   ); 
			}
		}
	})
}

/**
 * 发起支付
 */
function wechatPay(){
	var timeId = $("#time").val();
	var param = "time="+timeId;
	$.ajax({
		url:"/customer/data/checkTutorTime.html",
		data:param,
		dataType:"json",
		type:"POST",
		success:function(res){
			if("success"==res.result){
				var order_id = "800"+$("#orderId").val();    
				var total_fee = parseFloat($("#money").val())*100;   
				var openId = $("#openid").val();
				var nonceStr = getNonceStr();
				var str = "appid=wxad7987ba40989a97"+
						   "&body=800众服-众筹，让天下没有沉睡的资源！"+
						   "&mch_id=1304560401"+
						   "&nonce_str="+nonceStr+
						   "&notify_url=http://www.baidu.com"+
						   "&openid="+openId+
						   "&out_trade_no="+order_id+
						   "&spbill_create_ip=127.0.0.1"+
						   "&total_fee="+total_fee+
						   "&trade_type=JSAPI"+
						   "&key=jinxjinxjinxjinxjinxjinxjinxjinx";
				var md5 = CryptoJS.MD5(str).toString();
				
				var params = "sign="+md5+"&openId="+openId+"&fee="+total_fee+"&nonce_str="+nonceStr+"&client_ip=127.0.0.1&order_id="+order_id;
				if(null==openId||""==openId){
					alert('请在微信打开！');
				}else{
					$.ajax({
						url:"/wechat/getPayId.html",
						type:"POST",
						data:params,
						dataType:"json",
						success:function(res){
							pay_id = res.errorMsg;
							callPlay();
						}
					})
				}
			}
		}
	})
}

