var data = "";
var dataLength = 0;
$(function(){
	var id = $("#type").val();
	var params = "id="+id;
	var htmlStr = "";
	
	for(var i = 0;i<6;i++){
		if($("#type"+i).length){
			$("#type"+i).removeClass("active");
		}
	}
	$("#type"+id).addClass("active");
	
	$.ajax({
		url:"/p/list.html",
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			if(null!=res.errorMsg){
				data = res.errorMsg;
				if(dataLength<res.errorMsg.length){
					dataLength = res.errorMsg.length;
					$("#loadMore").hide();
				}
				for(var i = 0;i<dataLength;i++){
					var projectImg = "/themes/theme_default/cf/img/product_img_big.png";
				
					if(null!=res.errorMsg[i].materialsList){
						for(var j = 0;j<res.errorMsg[i].materialsList.length;j++){
							if("project_img"==res.errorMsg[i].materialsList[j].materialCode){
								projectImg = adminUrl+res.errorMsg[i].materialsList[j].materialContent;
							}
						}
					}
					
					htmlStr += "<li onmouseover=getStepModel('"+i+"') onmouseleave=getBaseModel('"+i+"') onclick=openUrl('/pro/detail.html?id="+res.errorMsg[i].id+"')  >";
					htmlStr += "<div class='product-list-top'   id=over"+i+" >";
					htmlStr += "<img src="+projectImg+" class='img-responsive img'/>";
					htmlStr += "<i class='mask' id=mask"+i+"></i>";
					htmlStr += "<div class='product-list-box' id=box"+i+">";
					htmlStr += getRealStep(res.errorMsg[i]);
					htmlStr += "</div>";
					htmlStr += getLeader(res.errorMsg[i],i);
					htmlStr += "</div></div>";
					htmlStr += "<div class='product-list-bottom'>";
					htmlStr += "<h3>"+res.errorMsg[i].projectName+"</h3>";
					htmlStr += "<p>"+res.errorMsg[i].info+"</p>";
					htmlStr += "<div class='row product-list-info product-list-info-ing'>";
					htmlStr += "<dl class='col-md-4 text-center'>";
					htmlStr += "<dt>"+getMoney(res.errorMsg[i].wannaAccount)+"</dt>";
					htmlStr += "<dd>目标额</dd></dl>";
					htmlStr += "<dl class='col-md-4 text-center'>";
					htmlStr += "<dt>"+getStep(res.errorMsg[i].account,res.errorMsg[i].wannaAccount)+"%</dt>";
					htmlStr += "<dd>已完成</dd></dl>";
					htmlStr += "<dl class='col-md-4 text-center'>";
					htmlStr += "<dt>"+getMoney(res.errorMsg[i].account)+"</dt>";
					htmlStr += "<dd>实投额</dd></dl></div>";
					htmlStr += "<div class='progress progress-ing'>";
					htmlStr += "<div class='progress-bar' role='progressbar' aria-valuenow='80' aria-valuemin='0' aria-valuemax='100' style='width:"+getStepStyle(res.errorMsg[i].account,res.errorMsg[i].wannaAccount)+"%;'></div>";
					htmlStr += "</div></div></li>";
				}
				$("#container").html(htmlStr);
//				waterfallFlow();
			}else{
				$("#loadMore").hide();
			}
		}
	})
})
/**
 * 领投人信息
 */
function getLeader(data,i){
	var leaderHtml = "";
	if(data.leader){
		if(data.leader.status == 1){
			leaderHtml +="<div class=product-list-ltr id=ltr"+i+"><h3>领投人："+data.leader.name+"</h3><p>"+data.leader.reason+"</p></div>";
			return leaderHtml;
		}else{
			return leaderHtml;
		}
		
	}else{
		return leaderHtml;
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
 * 鼠标浮动事件
 * @param index
 */
function getStepModel(index){
	$("#on"+index).css("display","none");
	$("#over"+index).css("border-top","4px solid #e6243b");
	$("#mask"+index).css("display","block");
	$("#box"+index).css("display","block");
	$("#ltr"+index).css("display","block");
}
/**
 * 鼠标移开事件
 * @param index
 */
function getBaseModel(index){
	$("#on"+index).css("display","block");
	$("#over"+index).css("border-top","0px solid #e6243b");
	$("#mask"+index).css("display","none");
	$("#box"+index).css("display","none");
	$("#ltr"+index).css("display","none");
}
/**
 * 获取带%进度
 * @param min
 * @param max
 * @returns
 */
function getStep(account,money){
	var step = parseFloat(parseFloat(account)/parseFloat(money)).toFixed(2);
	step = parseFloat(step*100).toFixed(0);
	return step;
}
/**
 * 进度条
 * @param min
 * @param max
 * @returns
 */
function getStepStyle(account,money){
	var step = parseFloat(account)/parseFloat(money)*100;
	if(step>=100){
		return 100;
	}
	return step;
}
/**
 * 进度文字
 * @param index
 * @returns {String}
 */
function getRealStep(object){
	var step = "<p>众<br/>筹<br/>中<br/><img src=<img src='/themes/theme_default/cf/img/little_bottom.png'/></p>";
	if("1"==object.timeStatus){
		 step = "<p>预<br/>热<br/>中<br/><img src='/themes/theme_default/cf/img/little_bottom.png'/></p>";
	}else if("2"==object.timeStatus){
		 step = "<p>众<br/>筹<br/>中<br/><img src='/themes/theme_default/cf/img/little_bottom.png'/></p>";
	}else if("3"==object.timeStatus){
		 step = "<p>已<br/>过<br/>期<br/><img src='/themes/theme_default/cf/img/little_bottom.png'/></p>";
	}else{
		 step = "<p>已<br/>完<br/>成<br/><img src='/themes/theme_default/cf/img/little_bottom.png'/></p>";
	}
	return step;
}

function checkStatus(url,status){
	if("1"==status){
		msg ="项目正在预热中,暂时无法投资";
	}else if("3"== status){
		msg ="项目已超过募集期, 去看看其他项目吧~"
	}else if("2"== status){
		open(url);
	}else if("1"!=status && "2"!=status && "3" !=status){
		msg ="项目已完成募集, 去看看其他项目吧~";
	}
	$("#errorMsg").html(msg);
	$("#successBtn").attr("onclick","goReload()");
	$("#errorDiv").show();
}

function openUrl(url){
	location.href = url;
}

/**
 * 重载页面
 */
function goReload(){
	location.reload();
}

/**
 * 瀑布流
 */
/*function waterfallFlow(){
	var margin = 24;
	var li=$(".product-box");
	var	li_W = li[0].offsetWidth+margin;
	var h=[];
	var n = 4;
	for(var i = 0;i < li.length;i++) {
		li_H = li[i].offsetHeight;
		if(i < n) {
			h[i]=li_H;
			li.eq(i).css("top",0);
			li.eq(i).css("left",i * li_W);
		}else{
			min_H =Math.min.apply(null,h) ;
			minKey = getarraykey(h, min_H);
			h[minKey] += li_H+margin ;
			li.eq(i).css("top",min_H+margin);
			li.eq(i).css("left",minKey * li_W);
		}
	}
}*/
	 
/*function getarraykey(s, v) {
	for(k in s) {
		if(s[k] == v) {
			return k;
		}
	}
}*/