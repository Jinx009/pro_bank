var newsData;
var dataIndex = 0;

$(function () 
{
	$('#scroll').owlCarousel({
		items: 5,
		autoPlay: false,
		navigation: true,
		navigationText: ["",""],
		scrollPerPage: true
	});
	
	changeHeader("关于我们");
	showAsk("novice");
	
	changeWidth();
	
	$(window).scroll(function() 
	{
		changeWidth();
	});   
	$(window).bind("resize",changeWidth);
	
	getNewsData();
	getMediaData();
	
})

/**
 * 媒体报道
 */
function getMediaData()
{
	var adminUrl1 = "http://www.800bank.com.cn:8089/";
	$.ajax({
		url:"/article/list.html?nid=news&time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			var htmlStr = "";
			
			for(var i = 0;i<res.data.list.length;i++)
			{
				htmlStr += "<div class='item'>";
				htmlStr += "<div class='person-div-2' onclick=openUrl('/media.html?id="+res.data.list[i].id+"') >";
				htmlStr += "<img src="+adminUrl1+res.data.list[i].picPath+"  class='person-img-o' >";
				htmlStr += "<div class='space-div-1' ></div>";
				htmlStr += "<div class='width90 left  space-div-4' >";
				htmlStr += res.data.list[i].title+">";
				htmlStr += "</div>";
				htmlStr += "<div class='space-div-1' ></div>";
				htmlStr += "<div class='space-div-4 width90 right' >"+jsDateTimeOnly(res.data.list[i].addTime)+"</div>";
				htmlStr += "</div>";
				htmlStr += "</div>";
			}
			
			$("#scroll1").html(htmlStr);
			
			$('#scroll1').owlCarousel({
				items: 4,
				autoPlay: false,
				navigation: true,
				navigationText: ["",""],
				scrollPerPage: true
			});
		}
	})
}
/**
 * 最新动态数据
 */
function getNewsData()
{
	$.ajax({
		url:"/nb/pc/articleList.action?nid=notice&time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			newsData = res;
			
			for(var i = 0;i<5;i++)
			{
				var index = i+1;
				if(2!=i)
				{
					$("#time"+index).html(jsDateTimeOnly(res.errorMsg[i].addTime));
					$("#info"+index).html(getShortString(res.errorMsg[i].title));
					$("#news_info_"+index).attr("onclick","changeData('"+i+"')");
				}
				else
				{
					dataIndex = 2;
					$("#middle_time").html(jsDateTimeOnly(res.errorMsg[2].addTime)+"&nbsp;&nbsp;&nbsp;"+res.errorMsg[2].title);
					$("#middle_info").html(res.errorMsg[2].content);
					$(".middle-img").attr("src",res.errorMsg[2].picPath);
				}
			}
		}
	})
}
/**
 * 更改数据
 * @param index
 */
function changeData(index)
{
	index = parseInt(index);
	dataIndex = index;
	
	$("#middle_time").html(jsDateTimeOnly(newsData.errorMsg[index].addTime)+"&nbsp;&nbsp;&nbsp;"+newsData.errorMsg[index].title);
	$("#middle_info").html(newsData.errorMsg[index].content);
	$(".middle-img").attr("src",newsData.errorMsg[index].picPath);
	
	if(index===(newsData.errorMsg.length-1))
	{
		$("#time5").html("");
		$("#info5").html("");
		$("#news_info_5").attr("onclick","");
		$("#time4").html("");
		$("#info4").html("");
		$("#news_info_4").attr("onclick","");
		$("#time2").html(jsDateTimeOnly(newsData.errorMsg[index-1].addTime));
		$("#info2").html(getShortString(newsData.errorMsg[index-1].title));
		$("#news_info_2").attr("onclick","changeData('"+(index-1)+"')");
		$("#time1").html(jsDateTimeOnly(newsData.errorMsg[index-2].addTime));
		$("#info1").html(getShortString(newsData.errorMsg[index-2].title));
		$("#news_info_1").attr("onclick","changeData('"+(index-2)+"')");
		
	}
	else if(index===(newsData.errorMsg.length-2))
	{
		$("#time5").html("");
		$("#info5").html("");
		$("#news_info_5").attr("onclick","");
		$("#time4").html(jsDateTimeOnly(newsData.errorMsg[index+1].addTime));
		$("#info4").html(getShortString(newsData.errorMsg[index+1].title));
		$("#news_info_4").attr("onclick","changeData('"+(index+1)+"')");
		$("#time2").html(jsDateTimeOnly(newsData.errorMsg[index-1].addTime));
		$("#info2").html(getShortString(newsData.errorMsg[index-1].title));
		$("#news_info_2").attr("onclick","changeData('"+(index-1)+"')");
		$("#time1").html(jsDateTimeOnly(newsData.errorMsg[index-2].addTime));
		$("#info1").html(getShortString(newsData.errorMsg[index-2].title));
		$("#news_info_1").attr("onclick","changeData('"+(index-2)+"')");
	}
	else if(index===1)
	{
		$("#time5").html(jsDateTimeOnly(newsData.errorMsg[index+2].addTime));
		$("#info5").html(getShortString(newsData.errorMsg[index+2].title));
		$("#news_info_5").attr("onclick","changeData('"+(index+2)+"')");
		$("#time4").html(jsDateTimeOnly(newsData.errorMsg[index+1].addTime));
		$("#info4").html(getShortString(newsData.errorMsg[index+1].title));
		$("#news_info_4").attr("onclick","changeData('"+(index+1)+"')");
		$("#time2").html(jsDateTimeOnly(newsData.errorMsg[index-1].addTime));
		$("#info2").html(getShortString(newsData.errorMsg[index-1].title));
		$("#news_info_2").attr("onclick","changeData('"+(index-1)+"')");
		$("#time1").html("");
		$("#info1").html("");
		$("#news_info_1").attr("onclick","");
	}
	else if(index===0)
	{
		$("#time5").html(jsDateTimeOnly(newsData.errorMsg[index+2].addTime));
		$("#info5").html(getShortString(newsData.errorMsg[index+2].title));
		$("#news_info_5").attr("onclick","changeData('"+(index+2)+"')");
		$("#time4").html(jsDateTimeOnly(newsData.errorMsg[index+1].addTime));
		$("#info4").html(getShortString(newsData.errorMsg[index+1].title));
		$("#news_info_4").attr("onclick","changeData('"+(index+1)+"')");
		$("#time2").html("");
		$("#info2").html("");
		$("#news_info_2").attr("onclick","");
		$("#time1").html("");
		$("#info1").html("");
		$("#news_info_1").attr("onclick","");
	}
	else
	{
		$("#time5").html(jsDateTimeOnly(newsData.errorMsg[index+2].addTime));
		$("#info5").html(getShortString(newsData.errorMsg[index+2].title));
		$("#news_info_5").attr("onclick","changeData('"+(index+2)+"')");
		$("#time4").html(jsDateTimeOnly(newsData.errorMsg[index+1].addTime));
		$("#info4").html(getShortString(newsData.errorMsg[index+1].title));
		$("#news_info_4").attr("onclick","changeData('"+(index+1)+"')");
		$("#time2").html(jsDateTimeOnly(newsData.errorMsg[index-1].addTime));
		$("#info2").html(getShortString(newsData.errorMsg[index-1].title));
		$("#news_info_2").attr("onclick","changeData('"+(index-1)+"')");
		$("#time1").html(jsDateTimeOnly(newsData.errorMsg[index-2].addTime));
		$("#info1").html(getShortString(newsData.errorMsg[index-2].title));
		$("#news_info_1").attr("onclick","changeData('"+(index-2)+"')");
	}
}
/**
 * 换页数据
 * @param value
 */
function changePageData(value)
{
	dataIndex = dataIndex+parseInt(value);
	
	if(dataIndex>=newsData.length-1)
	{
		dataIndex = newsData.length-1;
	}
	if(dataIndex<=0)
	{
		dataIndex = 0;
	}
	
	changeData(dataIndex);
}
/**
 * 短位文字
 * @param data
 * @returns
 */
function getShortString(data)
{
	return data;
}
/**
 * 改变文字位置
 */
function changeWidth()
{
	var width = $(window).width();
	
	if(width>1440)
	{
		var imgHeight = width*0.26/1.35;
		var topHeight = (700-imgHeight)/2;
		var textWidth = width*0.74/4;
		
		$(".middle-img").css({"top":topHeight,"left":"37%","width":"26%"});
		changeCommonWidth(textWidth,imgHeight);
	}
	else if(width<=1440&&width>=1280)
	{
		var imgHeight = width*0.33/1.35;
		var topHeight = (700-imgHeight)/2;
		var textWidth = width*0.68/4;
		
		$(".middle-img").css({"top":topHeight,"left":"33%","width":"34%"});
		changeCommonWidth(textWidth,imgHeight);
	}
	else
	{
		width = 1280;
		
		var imgHeight = width*0.33/1.35;
		var topHeight = (700-imgHeight)/2;
		var textWidth = width*0.68/4;
		
		$(".middle-img").css({"top":topHeight,"left":"33%","width":"34%"});
		changeCommonWidth(textWidth,imgHeight);
	}
}
/**
 * 通用改宽度
 */
function changeCommonWidth(textWidth,imgHeight)
{
	$(".height60").css({"width":textWidth,"display":"block"});
	$("#news_info_1").css("left",0*textWidth);
	$("#news_info_2").css("left",1*textWidth);
	$("#news_info_4").css("right",1*textWidth);
	$("#news_info_5").css("right",0*textWidth);
	$("#news_circle_1").css("left",textWidth/2-10);
	$("#news_circle_2").css("left",textWidth+textWidth/2-10);
	$("#news_circle_4").css("right",textWidth+textWidth/2-10);
	$("#news_circle_5").css("right",textWidth/2-10);
}
/**
 * 点击大图
 * @param id
 */
function showBig(id)
{
	var arrayLength = 6;
	
	for(var i = 1;i<arrayLength;i++)
	{
		$("#img0"+i).removeClass("person-div-1");
		$("#img0"+i).removeClass("person-div");
		$("#img0"+i).addClass("person-div");
		$("#right_info0"+i).addClass("hide");
		$("#left_info0"+i).addClass("hide");
	}
	
	$("#img0"+id).addClass("person-div-1");
	$("#right_info0"+id).removeClass("hide");
	$("#left_info0"+id).removeClass("hide");
}
/**
 * 问答
 * @param nid
 */
function showAsk(nid)
{
	$.ajax({
		url:"/nb/pc/articleList.action?nid="+nid+"&time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			$(".question-col").removeClass("all-question-title-active");
			$("#"+nid).addClass("all-question-title-active");
			
			var htmlStr = "";
			
			if(1==res.errorMsg.length)
			{
				htmlStr += "<div class='space-div-2' ></div>";
				htmlStr += "<p class='question-title'></p>";
				htmlStr += "<div class='question-ask'>"+res.errorMsg[0].content+"</div>";
				htmlStr += "<div class='space-div-2' ></div>";
			}
			else
			{
				htmlStr += "<div class='space-div-2' ></div>";
				for(var i = 0;i<res.errorMsg.length;i++)
				{
					htmlStr += "<p class='question-title'><span>"+(i+1)+"</span>"+res.errorMsg[i].title+"</p>";
					htmlStr += "<div class='question-ask'>"+res.errorMsg[i].content+"</div>";
				}
				htmlStr += "<div class='space-div-2' ></div>";
			}
			
			$("#question_div").html(htmlStr);
			
			$("#question_div ol li").css("list-style-type","none");
		}
	})
}
/**
 * 最新动态鼠标浮动事件
 * @param index
 */
function changeColor(index)
{
	$("#news_circle_"+index).css("background","rgba(245,239,215,0.7)");
	$("#news_inner_circle_"+index).css("background","rgb(241,190,37)");
}
/**
 * 最新动态鼠标移开事件
 * @param index
 */
function hideColor(index)
{
	$("#news_circle_"+index).css("background","rgba(199,220,237,0.7)");
	$("#news_inner_circle_"+index).css("background","rgb(51,127,185)");
}