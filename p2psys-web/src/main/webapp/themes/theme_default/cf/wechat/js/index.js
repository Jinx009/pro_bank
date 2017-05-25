$(function(){
	getData();
})

$(function(){
	getData();
	getProjectData('2');
})
/**
 * 导航
 */
function getData(){
	$.ajax({
		url:"/cf/wechat/banner.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			var htmlStr = "";
			if(null!=res.data){
				for(var i = 0;i<res.data.length;i++){
					htmlStr += "<div class='swiper-slide' >";
					htmlStr += "<a href='#'><img src='"+adminUrl+res.data[i].bannerPicUrl+"' /></a>";
					htmlStr += "<div class='swiper-content'>";
					htmlStr += "<div class='swiper-table' >";
					htmlStr += "<div class='swiper-table-cell' >";
					htmlStr += "<a href='#'><button>查看详情</button></a>";
					htmlStr += "</div></div></div></div>";
				}
				$("#topDiv").html(htmlStr);
				
			   	var mySwiper = new Swiper('.swiper-container',{
				    loop: true,
					autoplay: false,
					pagination: '.swiper-pagination', //导航分页
					paginationClickable: true //导航点击切换
			    });
			}
		}
	})
}

/**
 * 获取产品列表
 * @param id
 */
function getProjectData(id){
	var params = "id="+id;
	$.ajax({
		url:"/cf/wechat/projectIndex.action",
		type:"POST",
		dataType:"json",
		data:params,
		success:function(res){
			var htmlStr = "";
			if(null!=res.errorMsg){
				for(var i = 0;i<res.errorMsg.length;i++){
					var detailLogo = "/themes/theme_default/cf/img/logo.png";
					
					if(null!=res.errorMsg[i].materialsList){
						for(var j = 0;j<res.errorMsg[i].materialsList.length;j++){
							if("detail_img"==res.errorMsg[i].materialsList[j].materialCode){
								detailLogo = adminUrl+res.errorMsg[i].materialsList[j].materialContent;
							}
						}
					}
					
					htmlStr += "<a href='/wechat/pro/detail.action?id="+res.errorMsg[i].id+"' >";
					htmlStr += "<div class='product-item' >";
					htmlStr += "<div class='product-img' >";
					htmlStr += "<img  src='"+detailLogo+"' class='product-big-img' >";
					htmlStr += "<div class='product-small-left-img'>";
					htmlStr += getTimeStatus(res.errorMsg[i].timeStatus);
					htmlStr += "</div>";
					htmlStr += "<img  src='/themes/theme_default/cf/wechat/img/attention_active.png' class='product-samll-right-img' >";
					htmlStr += "</div>";
					htmlStr += "<div class='product-title' >";
					htmlStr += "<p>"+res.errorMsg[i].info+"</p>";
					htmlStr += "</div>";
					htmlStr += "<div class='product-bottom' >";
					htmlStr += "<div class='product-bottom-left' >";
					htmlStr += "<p>";
					htmlStr += "<span class='product-type' >"+getType(id)+"</span>";
					htmlStr += "<span class='start-money' >起投<font>￥"+res.errorMsg[i].remark+"</font></span>";
					htmlStr += "</p>";
					htmlStr += "<p class='end-time'>截止时间："+jsDateTimeDate(res.errorMsg[i].endTime)+"</p>";
					htmlStr += "</div>";
					htmlStr += "<div class='product-bottom-right' >";
					htmlStr += "<a href='#' ><button>购买</button></a>";
					htmlStr += "</div>";
					htmlStr += "</div>";
					htmlStr += "</div>";
					htmlStr += "</a>";
				}
			}else{
				
			}
			$("#productData").html(htmlStr);
		}
	})
}

/**
 * 获取众筹类型
 * @param id
 * @returns {String}
 */
function getType(id){
	if("2"==id){
		return "股权众筹";
	}else if("1"==id){
		return "实物众筹";
	}else if("4"==id){
		return "公益众筹";
	}
}

/**
 * 获取项目状态
 * @param timeStatus
 */
function getTimeStatus(object){
	var step = "";
	if("1"==object){
		 step = "预热中";
	}else if("2"==object){
		 step = "众筹中";
	}else if("3"==object){
		 step = "已过期";
	}else{
		step = "众筹中";
	}
	console.log(step)
	return step;
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
	var o = {
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