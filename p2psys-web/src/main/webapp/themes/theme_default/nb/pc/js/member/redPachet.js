$(function()
{
	changeLeftMenue("我的福袋","我的红包");
	
	$.ajax({
		url:"/nb/pc/redPacketListJson.action?time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			var htmlStr1 = "",htmlStr2 = "",htmlStr3 = "";
			
			if(checkUser(res.result))
			{
				if("success"===res.result)
				{
					//未使用
					if(res.notUsedList.length>0)
					{
						for(var i = 0;i<res.notUsedList.length;i++)
						{
							htmlStr1 += "<tr>";
							htmlStr1 += "<td>"+res.notUsedList[i].serviceName+"</td>";
							htmlStr1 += "<td>"+jsDateTime(res.notUsedList[i].expiredTime)+"</td>";
							htmlStr1 += "<td>"+res.notUsedList[i].amount+"</td>";
							if("1"===res.notUsedList[i].redMyPacketType)
							{
								htmlStr1 += "<td><a class='btn btn-info td-btn' style='background: rgb(62,148,209);' onclick=doRedPacket('"+res.notUsedList[i].id+"') >去兑现</a></td>";
							}
							else
							{
								htmlStr1 += "<td><a class='btn btn-info td-btn' style='background: rgb(62,148,209);' onclick=openUrl('/nb/pc/product/product_list.html?id=1') >去投资</a></td>";
							}
						}
					}
					else
					{
						htmlStr1 += "<tr class='space-div-4' ><td colspan=4 class='warning-p'  >";
						htmlStr1 += "<div class='width100 left' >";
						htmlStr1 += "<h3 class='red-packet-h3' >您暂无可用红包啦</h3>";
						htmlStr1 += "<div class='space-div-05' >&nbsp;&nbsp;</div>";
						htmlStr1 += "<p class='red-packet-p' >赶快来邀请更多好友获得红包吧!&nbsp;&nbsp;";
						htmlStr1 += "<a class='btn btn-info td-btn' href='/nb/pc/member/userInvite.html' style='background: rgb(62,148,209);' >去邀请好友</a></p>";
						htmlStr1 += "</div></td></tr>";
					}
					
					//已使用
					if(res.usedList.length>0)
					{
						for(var i = 0;i<res.usedList.length;i++)
						{
							htmlStr2 += "<tr>";
							htmlStr2 += "<td>"+res.usedList[i].serviceName+"</td>";
							if(null!=res.usedList[i].usedTime&&"undefined"!=res.usedList[i].usedTime)
							{
								htmlStr2 += "<td>"+jsDateTime(res.usedList[i].usedTime)+"</td>";
							}
							else
							{
								htmlStr2 += "<td></td>";
							}
							htmlStr2 += "<td>"+res.usedList[i].amount+"</td>";
							htmlStr2 += "<td>"+res.usedList[i].tenderName+"</td>";
						}
					}
					else
					{
						htmlStr2 += "<tr  class='space-div-4' ><td colspan=4 class='warning-p center' >暂无数据</td></tr>"
					}
					
					//过期
					if(res.overdue.length>0)
					{
						for(var i = 0;i<res.overdue.length;i++)
						{
							htmlStr3 += "<tr>";
							htmlStr3 += "<td>"+res.overdue[i].serviceName+"</td>";
							htmlStr3 += "<td>"+jsDateTime(res.overdue[i].expiredTime)+"</td>";
							htmlStr3 += "<td>"+res.overdue[i].amount+"</td>";
							htmlStr3 += "</tr>";
						}
					}
					else
					{
						htmlStr3 += "<tr class='space-div-4' ><td colspan=4 class='warning-p center' >暂无数据</td></tr>"
					}
					
					$("#table_content1").append(htmlStr1);
					$("#table_content2").append(htmlStr2);
					$("#table_content3").append(htmlStr3);
				}
			}
			else
			{
				closeDiv("");
				showDiv("util_login");
			}
		}
	})
})
/**
 * 点击事件
 * @param id
 */
function changeTo(id)
{
	for(var i = 1;i<4;i++)
	{
		$("#li_"+i).removeClass("active");
		$("#table_content"+i).addClass("none");
	}
	
	$("#li_"+id).addClass("active");
	$("#table_content"+id).removeClass("none");
}
function doRedPacket(id)
{
	$.ajax({
		url:"/nb/pc/exchangeRedPacket.action",
		type:"POST",
		dataType:"json",
		data:"id="+id,
		success:function(res)
		{
			if(checkUser(res.result))
			{
				if("success"===res.result)
				{
					showAlertDiv(true,"恭喜您兑换成功!","","/nb/pc/member/redPacket.html");
				}
				else
				{
					showAlertDiv(false,null,"兑换失败",null);
				}
			}
			else
			{
				showDiv("util_login");
			}
		}
	})
}