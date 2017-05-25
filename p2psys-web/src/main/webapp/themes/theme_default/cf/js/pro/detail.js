var realNameStatus = 0;
var moenyRule = 0;
var addRule = 0;
var minMoney = 0;
var companyMoney = 0;
var plandMoney = 0;
var meetMoney = 0;
var accountMoney = 0;
var startTime;
var likeStatus = false;
var attentionStatus = false;
var profitList = "";
var picStatus = false;

$(function(){
	layer.config({
	    extend: 'extend/layer.ext.js'
	});
	$("#dialog-box").hide();
	var type = $("#type").val();
	var id = $("#id").val();
	var params = "id="+id;
	realNameStatus = $("#realNameStatus").val();
	startTime = Date.parse(new Date());
	
	$.ajax({
		url:"/p/detail.html?id="+id,
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			var projectImg = "<div style='width:100%;'><img src ='/themes/theme_default/cf/img/product_img_big.png'  style='width:100%;' /></div>";
			var companyLogo = "";
			var profit = "";
			var detailLogo = "/themes/theme_default/cf/img/logo.png";
			var htmlStr  = "";
			var wechatCode = "";
			var projectActivityHtml = "<div class='space-4'></div>";
			var attention = $("#attention").val();
			var leaderStatus = $("#leaderStatus").val();
			
			if("0"==attention){
				$("#attentionImg").attr("src","/themes/theme_default/cf/img/black_heart.png");
			}
			getDataByType(res.errorMsg);
			moneyRule = 0;
			addRule = res.errorMsg.addAmount;
			minMoney = res.errorMsg.minMoney;
			companyMoney = res.errorMsg.companyMoney;
			plandMoney = res.errorMsg.wannaAccount;
			meetMoney = res.errorMsg.breach;
			accountMoney = res.errorMsg.account;
			countDown(res.errorMsg);
			
			if("1"==res.errorMsg.type){
				$("#cashName").html("什么是实物众筹？<span>&gt;</span>")
				$("#cashContent").html("实物众筹是投资人对实物众筹项目进行投资，获得相应的产品或服务。");
			}else if("4"==res.errorMsg.type){
				$("#cashName").html("什么是公益众筹？<span>&gt;</span>")
				$("#cashContent").html("公益众筹是投资人对公益众筹项目进行投资，资金用途仅用于公益性质。");
			}
			
			if(null!=res.errorMsg.materialsList){
				for(var j = 0;j<res.errorMsg.materialsList.length;j++){
					if("act_img"===res.errorMsg.materialsList[j].materialCode){//项目动态图片素材
						projectActivityHtml += "<div ><img src="+adminUrl+res.errorMsg.materialsList[j].materialContent+"  /></div>";
					}
					if("act_code"===res.errorMsg.materialsList[j].materialCode){//项目动态文字素材
						projectActivityHtml += "<div ><p>"+res.errorMsg.materialsList[j].materialContent+"</p></div>";
					}
					if("project_detail"===res.errorMsg.materialsList[j].materialCode){//项目详情头图
						if(!picStatus){
							projectImg = "";
							picStatus = true;
						}
						projectImg += "<div  style='width:100%;'><img src ='"+adminUrl+res.errorMsg.materialsList[j].materialContent+"'  style='width:100%;' /></div>";
					}
					if("company_logo"===res.errorMsg.materialsList[j].materialCode){//发起人公司Logo
						companyLogo = adminUrl+res.errorMsg.materialsList[j].materialContent;
					}
					if("detail_img"===res.errorMsg.materialsList[j].materialCode){//项目详情
						detailLogo = adminUrl+res.errorMsg.materialsList[j].materialContent;
					}
					if("business_book"===res.errorMsg.materialsList[j].materialCode){//项目详情
						$('#downFile').show();
						$("#downFile").attr("onclick","goOpen('"+adminUrl+res.errorMsg.materialsList[j].materialContent+"')");
					}
					if("wechat_code"===res.errorMsg.materialsList[j].materialCode){//微信群聊二维码
						wechatCode += "<img src='/themes/theme_default/cf/img/small_phone_icon.png' class='phone-icon'/>";
						wechatCode += "<div class='phone-code'>";
						wechatCode += "<img src='/themes/theme_default/cf/img/left_angel.png' class='left-angel' />";
						wechatCode += "<img src="+adminUrl+res.errorMsg.materialsList[j].materialContent+" class='code'/>";
						wechatCode += "<p>扫码进入群聊</p>";
						wechatCode += "</div>";
						$(".phone").html(wechatCode);
						$(".phone-icon").hover(function(){
							$(".phone-code").toggle();
						})
					}
				}
			}
			$("#profile").html(projectActivityHtml);
			$(".project-name").html(res.errorMsg.projectName);//项目名称
			$("#projectName").html(res.errorMsg.projectName);//项目名称
			$("#step").html(jsDateTimeDate(res.errorMsg.startTime)+"&nbsp;&nbsp;至&nbsp;&nbsp;"+jsDateTimeDate(res.errorMsg.endTime)+"<span>"+getRealStep(res.errorMsg)+"</span>");//项目进度计算百分比
			$("#detailLogo").attr("src",detailLogo);//详情页头图
			$("#info").html(res.errorMsg.info)//项目一句话简介
			$("#wannaAccount").html(getMoney(res.errorMsg.wannaAccount));//借款金额
			$("#minMoney").html(getMoney(res.errorMsg.minMoney));//起投金额
			$("#account").html(getAccount(res.errorMsg.account));//已投金额
			$("#stepStyle").css("width",getStepStyle(res.errorMsg.account,res.errorMsg.wannaAccount)+"%");//项目进度条
			//发起人信息
			$("#companyLogo").attr("src",companyLogo);
			htmlStr += "<p><b>发起人:</b>&nbsp;&nbsp;"+res.errorMsg.creater+"</p>";
			htmlStr += "<p><b>公司名称:</b>&nbsp;&nbsp;"+res.errorMsg.company+"</p>";
			htmlStr += "<p><b>发起人简介:</b>&nbsp;&nbsp;"+res.errorMsg.address+"</p>";
			$("#company").html(htmlStr);
			getOrderList(id);
			
			$("#home").html(projectImg);//产品详情长图
			//领投人
			if('2'==res.errorMsg.type){
				if(null!=res.errorMsg.leader){
					htmlStr = "";
					htmlStr += "<p><b>领投人:</b>&nbsp;&nbsp;"+res.errorMsg.leader.name+"</p>";
					htmlStr += "<p><b>个人简介:</b>&nbsp;&nbsp;"+res.errorMsg.leader.info+"</p>";
					htmlStr += "<p><b>领投理由:</b>&nbsp;&nbsp;"+res.errorMsg.leader.reason+"</p>";
					if(null!=res.errorMsg.leader.leaderFactory){
						if(null!=res.errorMsg.leader.leaderFactory.picPath&&""!=res.errorMsg.leader.leaderFactory.picPath){
							
						}else{
							$("#leaderImg").html("<img src='/themes/theme_default/cf/img/login_icon.png' class='img-circle'  />");
						}
					}else{
						if(null!=res.errorMsg.leader.picPath&&""!=res.errorMsg.leader.picPath){
							$("#leaderImg").html("<img src='"+res.errorMsg.leader.picPath+"' class='img-circle'  />");
						}else{
							$("#leaderImg").html("<img src='/themes/theme_default/cf/img/login_icon.png' class='img-circle'  />");
						}
					}
					$("#leaderDiv").html(htmlStr);
				}else{
					htmlStr = "";
					if("4" ==res.errorMsg.timeStatus){
						htmlStr="";
					}else{
						if(leaderStatus=="0"){
							htmlStr += "<div class='product-detail-about-text' id='leaderDiv' ><div class='support-btn'  style='width:193px;text-align:center;'><button class='product-detail-btn' >领投人申请审核中</button></div></div>";
						}
						if(leaderStatus=="-1"){
							htmlStr += "<div class='product-detail-about-text' id='leaderDiv' ><div class='support-btn'  style='width:193px;text-align:center;'><button class='product-detail-btn' onclick=checkLeaderStatus('"+id+"')  >申请成为领投人</button></div></div>";
						}
					}
					$("#leaderShowDiv").html(htmlStr);
				}
			}
			//微信群聊二维码
			if(""!=wechatCode){
				htmlStr = "";
				htmlStr += "<img src='/themes/theme_default/cf/img/left_angel.png' class='left-angel'/>";
				htmlStr += "<img src='"+wechatCode+"' class='code'/>";
				htmlStr += "<p>扫码进入群聊</p>"
			}
			htmlStr = "";
			profitList = res.errorMsg.profitRuleList;
			if(null!=res.errorMsg.profitRuleList){
				if(res.errorMsg.profitRuleList.length>1){
					for(var k = 0 ;k<res.errorMsg.profitRuleList.length;k++){
						htmlStr += "<div class='product-detail-right-box'>";
						htmlStr += "<div class='product-detail-right-box-col product-detail-small-title'>";
						htmlStr += "<h3>"+res.errorMsg.profitRuleList[k].name+"（&yen;"+res.errorMsg.profitRuleList[k].money+"）</h3>";
						htmlStr += "</div>";
						htmlStr += "<div class='product-detail-right-box-col product-detail-about support-detail'>";
						htmlStr += "<p>"+res.errorMsg.profitRuleList[k].content+"</p>";                     
						if(null!=res.errorMsg.profitRuleList[k].picPath&&""!=res.errorMsg.profitRuleList[k].picPath){
							htmlStr += "<p><img src='"+adminUrl+res.errorMsg.profitRuleList[k].picPath+"' width='120px' /></p>";
						}
						if("2"==res.errorMsg.timeStatus){
							htmlStr += "<div class='support-btn'  ><button class='product-detail-btn' onclick=doBuy('"+k+"') >支持&yen;"+res.errorMsg.profitRuleList[k].money+"</button></div>";
						}else{
							if("4"==res.errorMsg.timeStatus){
								$("#endMsg").html("项目完成募集，下次要手快哦！去看看其他项目吧！");
							}
							htmlStr += "<div class='support-btn'  ><button class='product-detail-btn' style='background:gray;' onclick=showEnd()  >支持&yen;"+res.errorMsg.profitRuleList[k].money+"</button></div>";
						}
						htmlStr += "</div></div>";
					}
				}else{
					htmlStr += "<div class='product-detail-right-box'>";
					htmlStr += "<div class='product-detail-right-box-col product-detail-small-title'>";
					htmlStr += "<h3>支持</h3>";
					htmlStr += "</div>";
					htmlStr += "<div class='product-detail-right-box-col product-detail-about support-detail'>";
					htmlStr += "<p>"+res.errorMsg.profitRuleList[0].content+"</p>";                     
					if(null!=res.errorMsg.profitRuleList[0].picPath&&""!=res.errorMsg.profitRuleList[0].picPath){
						htmlStr += "<p><img src='"+adminUrl+res.errorMsg.profitRuleList[0].picPath+"' width='120px' /></p>";
					}
					if("2"==res.errorMsg.timeStatus){
						htmlStr += "<div class='support-btn'><button class='product-detail-btn'  onclick=doBuy('"+0+"') >支持</button></div>";
					}else{
						if("4"==res.errorMsg.timeStatus){
							$("#endMsg").html("项目完成募集，下次要手快哦！去看看其他项目吧！");
						}
						htmlStr += "<div class='support-btn'><button class='product-detail-btn' style='background:gray;' onclick=showEnd() >支持</button></div>";
					}
					
					htmlStr += "</div></div>";
				}
			}
			$("#profit").html(htmlStr);
		}
	})
})

function showEnd(){
	$("#endDiv").css("display","block");
}

function hideEnd(){
	$("#endDiv").css("display","none");
}

/**
 * 打开新页面
 * @param url
 */
function goOpen(url){
	window.open(url);
}
/**
 * 申请领投人条件
 * @param projectId
 */
function checkLeaderStatus(projectId){
	location .href= "/cf/user/beLeader.html?projectId="+projectId;
}

/**
 *获取额外数据
 */
function getDataByType(data){
	if("2"==data.type){
		$("#financing").html(getFinancing(data.financing));
		var progress = parseFloat(data.wannaAccount/1.05)/(data.companyMoney+data.wannaAccount/1.05)*100;
		progress = parseFloat(progress).toFixed(0);
		if(data.id=="41"){
			$("#giveNum").html("7.5%");  
		}if(data.id=="43"){
			$("#giveNum").html("83.33%");  
		}else{
			$("#giveNum").html(progress+"%");  
		}
		$("#mainDiv").show();
		$("#leaderShowDiv").css("display","block");
		$("#business").css("display","block");
	}
}

/**
 * 融资进度
 * @param type
 * @returns {String}
 */
function getFinancing(type){
	if("1"==type){
		return "种子轮";
	}else if("2"==type){
		return "天使轮";
	}else if("3"==type){
		return "Pre-A轮";
	}else if("4"==type){
		return "A轮";
	}else if("5"==type){
		return "Pre-B轮";
	}else if("6"==type){
		return "B轮";
	}else if("7"==type){
		return "C轮";
	}else if("8"==type){
		return "D轮";
	}else if('9'==type){
		return "Pre-IPO轮";
	}
	
}

/**
 * 赞
 */
function doLike(){
	var id = $("#id").val();
	var params = "id="+id;
	if(!likeStatus){
		likeStatus = true;
		$.ajax({
			url:"/cf/pro/addLike.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					$("#likeNum").html(res.errorMsg);
				}
			}
		})
	}else if(compareTime()){
		$.ajax({
			url:"/cf/pro/addLike.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					$("#likeNum").html(res.errorMsg);
				}
			}
		})
	}else{
		layer.alert('您的操作过于频繁!',{title:false,closeBtn: 0});
	}
}

/**
 * 关注
 */
function doAttention(){
	var id = $("#id").val();
	var params = "id="+id;
	if(!attentionStatus){
		attentionStatus = true;
		$.ajax({
			url:"/cf/pro/addAttention.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				attentionStatus = false;
				if("success"==res.result){
					$("#attentionImg").attr("src","/themes/theme_default/cf/img/support.png");
					$("#attentionNum").html(res.errorMsg);
				}else{
					layer.alert(res.errorMsg,{title:false,closeBtn: 0});
				}
			}
		})
	}
}

/**
 * 获取购买记录
 * @param id
 */
function getOrderList(id){
	var params = "id="+id;
	var orderNum = 0;
	$.ajax({
		url:"/pro/buyList.action",
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			if(res.errorMsg){
				var htmlStr = "";
				orderNum = res.errorMsg.length;
				for(var i = 0;i<res.errorMsg.length;i++){
					htmlStr += "<tr>";
					if(null!=res.errorMsg[i].userPic&&""!=res.errorMsg[i].userPic){
						htmlStr += "<td><img height='20px;' width='20px;' src='http://"+window.location.host+res.errorMsg[i].userPic+"' /></td>";
					}else{
						htmlStr += "<td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td>";
					}
					htmlStr += "<td>"+res.errorMsg[i].userName+"</td>";
					htmlStr += "<td>"+jsDateTimeDate(res.errorMsg[i].addTime)+"</td></tr>";
				}
				htmlStr += "<div class='space-4' ></div>";
				$("#orderList").html(htmlStr);
			}else {
				if(39==id){
					orderNum = 9;
					var htmlStr = "";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>137****1338</td><td>2015年9月6日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>139****3999</td><td>2015年9月5日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>139****0818</td><td>2015年9月5日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>138****0599</td><td>2015年9月4日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>157****0288</td><td>2015年9月4日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>135****2286</td><td>2015年9月2日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>139****5459</td><td>2015年9月2日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>135****3338</td><td>2015年9月1日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>186****1524</td><td>2015年9月1日</td></tr>";
					htmlStr += "<div class='space-4' ></div>";
					$("#orderList").html(htmlStr);
				}else if(38==id){
					orderNum = 9;
					var htmlStr = "";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>138****0599</td><td>2015年5月4日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>157****0288</td><td>2015年5月3日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>137****1338</td><td>2015年5月3日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>139****3999</td><td>2015年5月3日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>139****0818</td><td>2015年5月2日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>135****2286</td><td>2015年5月2日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>139****5459</td><td>2015年5月2日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>135****3338</td><td>2015年5月1日</td></tr>";
					htmlStr += "<tr><td><img height='20px;' width='20px;' src='/themes/theme_default/cf/wechat/img/attention_img.png' /></td><td>186****1524</td><td>2015年5月1日</td></tr>";
					htmlStr += "<div class='space-4' ></div>";
					$("#orderList").html(htmlStr);
				}
			}
			$(".support-num").html(orderNum);
		}
	})
}

/**
 * 时间比较
 * @returns {Boolean}
 */
function compareTime(){
	var nowTime = Date.parse(new Date());
	var result = parseInt((nowTime-startTime)/1000);
	console.log(result)
	if(result>=260){
		return true;
	}else{
		return false;
	}
}


/**
 * 点击支持触发事件
 * @param index
 */
function doBuy(index){
	var id = $("#id").val();
	var profitId = profitList[parseInt(index)].id;
	if('43'==id&&'75'==profitId){
		layer.prompt({
		    title: '请输入优先定向码！',
		    formType: 1 
		}, function(pass){
			doGoBuy(pass,index);
		});
	}else{
		window.open("/cf/user/buy.html?id="+id+"&profit="+profitId);
	}
}
function doGoBuy(pass,index){
	var id = $("#id").val();
	var profitId = profitList[parseInt(index)].id;
	if('800ZF'==pass){
		window.open("/cf/user/buy.html?id="+id+"&profit="+profitId);
	}else{
		layer.closeAll();
		layer.alert('定向密码不正确！',{title:false,closeBtn:0});
	}
}
/**
 * 打开页面
 * @param url
 */
function openUrl(url){
	location.href = url;
}


/**
 * 投资进度条
 */
function getStepStyle(account,money){
	var step = parseFloat(account)/parseFloat(money)*100;
	if(step>=100){
		return 100;
	}
	return step;
}
/**
 * 投资金额格式化
 */
function getAccount(account){
	account = parseFloat(account);
	if(account>10000){
		return parseFloat(account/10000).toFixed(0)+"万";;
	}else{
		return account+"元";
	}
}
/**
 * 金钱格式化
 */
function getMoney(money){
	money = parseFloat(money);
	if(money>=10000){
		return parseFloat(money/10000).toFixed(0)+"万";
	}else{
		return money+"元";
	}
}
/**
 * 项目进度
 */
function getRealStep(object){
	var step = "紧张募集中";
	if("1"==object.timeStatus){
		 step = "奋力预热中";
	}else if("2"==object.timeStatus){
		 step = "紧张募集中";
	}else if("3"==object.timeStatus){
		 step = "遗憾已过期";
	}else{
		 step = "已顺利完成";
	}
	return step;
}

/**
 * 时间戳转换标准时间
 * @param unixtime
 * @returns
 */
function jsDateTimeOnly(unixtime){  
	 var date = new Date(unixtime);
	 return  date.format("yyyy/MM/dd hh:mm:ss"); 
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
function jsDateTimeDate(unixtime){  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd"); 
} 
Date.prototype.format = function(format){
	var o ={
	"M+" : this.getMonth()+1, 
	"d+" : this.getDate(), 
	"h+" : this.getHours(),
	"m+" : this.getMinutes(), 
	"s+" : this.getSeconds(), 
	"q+" : Math.floor((this.getMonth()+3)/3), 
	"S" : this.getMilliseconds() 
	}

	if(/(y+)/.test(format)){
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o){
		if(new RegExp("("+ k +")").test(format)){
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
} 

/**
 * 倒计时
 * 
	 status==1//预热中
	 status==2//众筹中
	 status==3//已过期
	 status==4//已完成
	
 */
function countDown(data){
	var startTime = parseInt(new Date().getTime());
	var endTime = parseInt(data.endTime);
	var sys_second =(endTime-startTime)/1000;
	var status = data.timeStatus;
	if("2"==status){
		$("#leftTimeName").html("剩余时间");
		count(sys_second);
	}else if("1"==status){
		$("#leftTimeName").html("结束倒计时");
		count(sys_second);
	}else{
		$("#leftTimeName").html("剩余时间");
		$("#leftTime").html("募集结束");
		
	}
}
function count(sys_second){
	var showDay,showHour,showMinute,showSecond;
	var timer = setInterval(function(){
		if (sys_second > 1) {
			sys_second -= 1;
			var day = Math.floor((sys_second / 3600) / 24);
			var hour = Math.floor((sys_second / 3600) % 24);
			var minute = Math.floor((sys_second / 60) % 60);
			var second = Math.floor(sys_second % 60);
			showDay = day;
			if(showDay<=0){
				$("#leftTime").html("募集结束");
			}else{						
				$("#leftTime").html(showDay+"天");

			}
		} else {
			location.reload();
		}
	}, 1000);
}