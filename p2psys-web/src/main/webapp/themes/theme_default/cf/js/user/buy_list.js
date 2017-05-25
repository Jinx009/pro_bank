var orderId,cacleId;

$(function(){
	showLi('user_li');
	$.ajax({
		url:"/cf/user/buy-list-data.html",
		type:"POST",
		dataType:"json",
		success:function(res){
			var htmlStr = "";
			if(null!=res.errorMsg&&res.errorMsg.length>0){
				for(var i = 0;i<res.errorMsg.length;i++){
					htmlStr += "<tr>";
					htmlStr += "<td><a href='/pro/detail.html?id="+res.errorMsg[i].projectBaseinfo.id+"' class='aDiy'>"+res.errorMsg[i].projectName+"</a></td>";
					htmlStr += "<td>"+getType(res.errorMsg[i].projectType)+"</td>";
					htmlStr += "<td>"+getStatus(res.errorMsg[i].modelStatus)+"</td>";
					htmlStr += "<td>"+jsDateTimeDate(res.errorMsg[i].addTime)+"</td>";
					htmlStr += "<td>"+formatCurrency(res.errorMsg[i].money)+"</td>";
					htmlStr += "<td>"+res.errorMsg[i].payMoney+"</td>";
					htmlStr += "<td>"+getStep(res.errorMsg[i].payStatus)+"</td>";
					if("1"==res.errorMsg[i].leaderStatus){
						htmlStr += "<td>领投</td";
					}else{
						htmlStr += "<td>跟投</td>";
					}
					if("1"==res.errorMsg[i].projectType){
						htmlStr += "<td>"+getAddress(res.errorMsg[i].payStatus,res.errorMsg[i].id)+"</td>";
					}else{
						htmlStr += "<td>"+getPayStatus(res.errorMsg[i].payStatus,res.errorMsg[i].id,res.errorMsg[i]);
						if('4'==res.errorMsg[i].modelStatus&&'2'==res.errorMsg[i].payStatus&&'精准医疗，让肿瘤君滚蛋吧'!=res.errorMsg[i].projectName){
							htmlStr += '<span class="error pointer" onclick=downProtocol("'+res.errorMsg[i].id+'") >下载合同</span>';
						}
						htmlStr += "</td>";
					}
					htmlStr += "</tr>";
				}
			}
			$("#dataDiv").html(htmlStr);
		}
	})
	layer.config({
	    extend: 'extend/layer.ext.js'
	});
})

/**
 * 支付剩余款项
 * @param id
 */
function doPay(payPwd){
	var params = "id="+orderId+"&payPwd="+payPwd;
	
	$.ajax({
		url:"/cf/user/payAll.html",
		type:"POST",
		dataType:"json",
		data:params,
		success:function(res){
			if("success"==res.result){
				layer.alert('恭喜您，支付成功！',{title:false,closeBtn: 0},function(){
					reloadPage();
				});
			}else{
				layer.alert(res.errorMsg,{title:false,closeBtn: 0});
			}
		}
	})
}

/**
 * 收货地址
 */
function getAddress(status,id){
	return "<span class='error pointer' onclick=openUrl('/cf/user/order-address.html?id="+id+"') >收货地址</span>";
}

function openUrl(url){
	location.href = url;
}

/**
 * 产品类别
 * @param type
 * @returns {String}
 */
function getType(type){
	if("1"==type){
		return "实物众筹";
	}else if("2"==type){
		return "股权众筹";
	}else  if("4"==type){
		return "公益众筹";
	}else{
		return "其他众筹";
	}
}

/**
 * 准备取消
 * @param id
 * @returns
 */
function showCacle(id){
	cacleId = id;
	layer.prompt({
	    title: '输入交易密码，取消订单！',
	    formType: 1 
	}, function(pass){
		doCacle(pass);
	})	
}

/**
 * 执行取消
 * @returns
 */
function doCacle(pwd){
	var params = "id="+cacleId+"&payPwd="+pwd;
	$.ajax({
		url:"/cf/user/caclePay.html",
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			if("success"==res.result){
				layer.alert('恭喜您，取消成功！',{title:false,closeBtn: 0},function(){
					reloadPage();
				});
			}else{
				layer.alert(res.errorMsg,{title:false,closeBtn: 0});
			}
		}
	})
}

/**
 * 
 * @param id
 */
function payAll(id,money,payMoney){
	orderId = id;
	var money = parseFloat(parseFloat(money)-parseFloat(payMoney)).toFixed(2);
	layer.prompt({
	    title: '输入交易密码，支付剩余'+money+'元',
	    formType: 1 
	}, function(pass){
		doPay(pass);
	});
}

/**
 * 刷新页面
 */
function reloadPage(){
	location.reload();
}

/**
 * 隐藏弹窗
 */
function hideAlert(){
	$("#dialog-box-pwd").hide();
	$("#dialog-box-info").hide();
	$("#dialog-box-cacle").hide();
	$("#dialog-box-info-cacle").hide();
}

/**
 * 操作
 * @param index
 * @param id
 * @returns {String}
 */
function getPayStatus(index,id,data){
	if(1==index){
		return "<span class='error pointer' onclick='payAll("+id+","+data.money+","+data.payMoney+")' >付全款</span>&nbsp;&nbsp;<span class='error pointer' onclick=showCacle('"+id+"') >取消</span>";
	}else if(2==index){
		return "";
	}else if(0==index){
		return "";
	}else if(3==index){
		return "";
	}
}

/**
 * 标状态
 * @param status
 * @returns {String}
 */
function getStatus(status){
	if(1==status){
		return "预热中";
	}else if(2==status){
		return "众筹中";
	}else if(3==status){
		return "已过期";
	}else if(5==status){
		return '已撤回';
	}else{
		return "募集完成";
	}
}

/**
 * 购买状态
 * @param step
 * @returns {String}
 */
function getStep(step){
	if(1==step){
		return "预约购买";
	}else if(2==step){
		return "全额购买";
	}else if(0==step){
		return "已扣除违约金";
	}else if(3==step){
		return "已取消";
	}
}

/**
 * 金钱格式化
 * @param num
 * @returns {String}
 */
function formatCurrency(num) {  
    num = num.toString().replace(/\$|\,/g,'');  
    if(isNaN(num))  
        num = "0";  
    sign = (num == (num = Math.abs(num)));  
    num = Math.floor(num*100+0.50000000001);  
    cents = num%100;  
    num = Math.floor(num/100).toString();  
    if(cents<10)  
    cents = "0" + cents;  
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
    num = num.substring(0,num.length-(4*i+3))+','+  
    num.substring(num.length-(4*i+3));  
    return (((sign)?'':'-') + num + '.' + cents);  
}  
/**
 * 时间戳转换标准时间
 * @param unixtime
 * @returns
 */
function jsDateTimeDate(unixtime)  {  
	 var date = new Date(unixtime);
	 return  date.format("yyyy-MM-dd"); 
} 
Date.prototype.format = function(format){
	var o = {
	"M+" : this.getMonth()+1, 
	"d+" : this.getDate(), 
	"h+" : this.getHours(),
	"m+" : this.getMinutes(), 
	"s+" : this.getSeconds(), 
	"q+" : Math.floor((this.getMonth()+3)/3), 
	"S" : this.getMilliseconds() 
	}

	if(/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o) {
		if(new RegExp("("+ k +")").test(format)){
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
} 

function downProtocol(id){
	window.open('/create/protocol.action?id='+id+'&protocolCode=second_protocol','_self');
//	$.ajax({
//		url:'',
//		type:'POST',
//		dataType:'json',
//		success:function(res){
//			if(200==res.code){
//				window.open('/data/protocol/'+res.data+'/second_protocol'+id+'.pdf','_self');
//			}
//		}
//	})
}