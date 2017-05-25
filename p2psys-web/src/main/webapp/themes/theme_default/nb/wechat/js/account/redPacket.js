$(function()
{
	//底部导航颜色切换
    $("#foot_nav_my_a").css("color", "#326eaf").find("svg").css({
        "fill": "#326eaf",
        "stroke-width": "0"
    });
    
	$.ajax({
		url:"/nb/wechat/account/redPacketListJson.html?userId="+$("#userId").val(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			if(res.notUsedList.length)
			{
				var cashNum = 0;
				var investNum = 0;
				for(var i = 0;i<res.notUsedList.length;i++)
				{
					if("1"===res.notUsedList[i].redMyPacketType)
					{
						cashNum++;
						var htmlStr = "";
						
						htmlStr += "<tr><td>"+res.notUsedList[i].serviceName+"</td>";
						htmlStr += "<td>"+jsDateTime(res.notUsedList[i].expiredTime)+"</td>";
						htmlStr += "<td>"+res.notUsedList[i].amount+"</td>";
						htmlStr += "<td><a class='btn btn-info' onclick=getMoney('"+res.notUsedList[i].id+"') >兑换</a></td>";
						htmlStr += "</tr>";
						
						$("#one_cash_info").append(htmlStr);
					}
					if("2"===res.notUsedList[i].redMyPacketType)
					{
						investNum++;
						var htmlStr = "";
						
						if("recommend"===res.notUsedList[i].serviceType)
						{
							htmlStr += "<tr><td style='color:red;'>"+res.notUsedList[i].serviceName+"</td>";
							htmlStr += "<td>"+jsDateTime(res.notUsedList[i].expiredTime)+"</td>";
							htmlStr += "<td>"+res.notUsedList[i].amount+"</td>";
							htmlStr += "<td><a class='btn btn-info' href='/nb/wechat/product/product_menue.action' >投资</a></td>";
							htmlStr += "</tr>";
						}
						else
						{
							htmlStr += "<tr><td>"+res.notUsedList[i].serviceName+"</td>";
							htmlStr += "<td>"+jsDateTime(res.notUsedList[i].expiredTime)+"</td>";
							htmlStr += "<td>"+res.notUsedList[i].amount+"</td>";
							htmlStr += "<td><a class='btn btn-info' href='/nb/wechat/product/product_menue.action' >投资</a></td>";
							htmlStr += "</tr>";
						}
						
						
						$("#one_info").append(htmlStr);
					}
				}
				if(0===cashNum)
				{
					$("#div_one").hide();
				}
				if(0===investNum)
				{
					$("#div_two").hide();
				}
			}
			else
			{
				$("#group_div").show();
				$("#no_redPacket").hide();
				$("#one_info").html("<tr><td  colspan=4 >暂无可用投资红包!</td></tr>");
				$("#one_cash_info").html("<tr><td  colspan=4 >暂无可用投资红包!</td></tr>");
				$("#btn_a").show();
				$("#bg-success").html("温馨提示:您暂无可用红包，赶快去投资来领取更多福利吧！");
				
			}
			if(res.overdue.length)
			{
				for(var i = 0;i<res.overdue.length;i++)
				{
					var htmlStr = "";
					
					htmlStr += "<tr><td>"+res.overdue[i].serviceName+"</td>";
					htmlStr += "<td>"+jsDateTime(res.overdue[i].expiredTime)+"</td>";
					htmlStr += "<td>"+res.overdue[i].amount+"</td></tr>";
					
					$("#two_info").append(htmlStr);
				}
			}
			else
			{
				$("#two_wrong").hide();
				var htmlStr = "";
				
				htmlStr += "<tr><td  colspan='4' style='text-align:center' ><span class='gray-span' >抱歉，您暂无已失效红包信息。</span></td></tr>";
				
				$("#two_info").append(htmlStr);	
			}
			if(res.usedList.length)
			{
				for(var i = 0;i<res.usedList.length;i++)
				{
					var htmlStr = "";
					
					htmlStr += "<tr><td>"+res.usedList[i].serviceName+"</td>";
					if(res.usedList[i].usedTime)
					{
						htmlStr += "<td>"+jsDateTime(res.usedList[i].usedTime)+"</td>";
					}
					else
					{
						htmlStr += "<td></td>";
					}
					htmlStr += "<td>"+res.usedList[i].amount+"</td></tr>";
					
					$("#three_info").append(htmlStr);
				}
			}
			else
			{
				$("#three_wrong").hide();
				var htmlStr = "";
				
				htmlStr += "<tr><td  colspan='4' style='text-align:center' ><span class='gray-span' >抱歉，您暂无已使用红包信息。</span></td></tr>";
				
				$("#three_info").append(htmlStr);
			}
			if(!res.usedList.length&&!res.overdue.length&&!res.notUsedList.length)
			{
				$("#no_redPacket").show();
				$("#btn_a").show();
				$("#accordion").hide();
				$("#bg-success").html("温馨提示:您暂无可用红包，赶快去投资来领取更多福利吧！");
			}
		}
	})
})
/**
 * 显示不同红包类别
 * @param index
 */
function show(index)
{
	$("#table_one").hide();
	$("#table_two").hide();
	$("#table_three").hide();
	
	$("#btn_one").removeClass("btn-info");
	$("#btn_two").removeClass("btn-info");
	$("#btn_three").removeClass("btn-info");
	
	$("#table_"+index).show();
	$("#btn_"+index).addClass("btn-info");
}
/**
 * 现金红包兑换
 * @param id
 */
function getMoney(id)
{
	var rpt = $("#redPacketToken").val();
	$.ajax({
		url:"/nb/wechat/account/exchangeRedPacket.html",
		type:"POST",
		data:"id="+id+"&redPacketToken="+rpt,
		dataType:"json",
		success:function(res)
		{
			if("success"===res.result)
			{
				$("#mask").show();
				$("#errorMsg").html("兑换成功!");
				$("#success_div").show();
			}
			else
			{
				$("#mask").show();
				$("#errorMsg").html("该红包已经被兑换!");
				$("#success_div").show();
			}
		}
	})
}

function hideSuccess()
{
	$("#mask").hide();
	$("#success_div").hide();
	location.reload();
}
/**
 * 时间戳转换
 * 
 * @param unixtime
 * @returns
 */
function jsDateTime(unixtime)  
{  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd"); 
}  
Date.prototype.format = function(format)
{
	var o = 
	{
	"M+" : this.getMonth()+1, 
	"d+" : this.getDate(), 
	"h+" : this.getHours(),
	"m+" : this.getMinutes(), 
	"s+" : this.getSeconds(), 
	"q+" : Math.floor((this.getMonth()+3)/3), 
	"S" : this.getMilliseconds() 
	}

	if(/(y+)/.test(format)) 
	{
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o) 
	{
		if(new RegExp("("+ k +")").test(format))
		{
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
} 