
$(function()
{
	var adminUrl = "";
	localStorage.type = "btn_one";
	
	$("#rate").html(getRate($("#rateValueLow").val(),$("#highestRefundRate").val(),$("#interestRateValue").val()));
	
	$.ajax({
		url:"/getHostUrl.action",
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			adminUrl = res.con_adminurl;
			
			$.ajax({
				url:"/nb/wechat/banner.action",
				type:"GET",
				dataType:"json",
				ansyc:false,
				success:function(res)
				{
					var htmlStr = "";
					if(0!=res.data.length)
					{
						for(var i = 0;i<res.data.length;i++)
						{
							htmlStr += "<div class='swiper-slide'>";
							htmlStr += "<a href="+res.data[i].bannerLinkUrl+" >";
							htmlStr += " <img src="+adminUrl+res.data[i].bannerPicUrl+" class='indexBg swiper-lazy'/>";
							htmlStr += "</a>";
							htmlStr += "</div>";
						}
					}
					else
					{
						htmlStr += "<div class='swiper-slide'>";
						htmlStr += "<a href='#' >";
						htmlStr += " <img src='/themes/theme_default/nb/wechat/img/indexIcon.jpg' class='indexBg swiper-lazy'/>";
						htmlStr += "</a>";
						htmlStr += "</div>";
					}
					
					$("#bannerList").html(htmlStr);
				}
			})
		}
	})
	
	
	// 首页轮播图
	var mySwiper=new Swiper('.swiper-container',{
		loop:true, //循环播放
		lazyLoading:true, //延迟加载
		pagination:'.swiper-pagination',  //导航分页
		paginationClickable :true //导航点击切换
	});
})
/**
 * 获取收益
 */
function getRate(low,high,interestRateValue)
{
	var rate;
	if(interestRateValue==0){
		rate="";
	}else{
		rate="<span style='color:#ff5858;font-size:1.2em;'>+"+interestRateValue+"%</span>";
	}
	if(low===high&&"0"==high)
	{
		return "浮动"+rate;
	}
	if(low===high&&"0"!==high)
	{
		return low+"%"+rate;
	}
	if(low!==high&&"0"==high)
	{
		return low+"%+浮动"+rate;
	}
	if(low!==high&&"0"!==high)
	{
		return low+"%-"+high+"%"+rate;
	}
}