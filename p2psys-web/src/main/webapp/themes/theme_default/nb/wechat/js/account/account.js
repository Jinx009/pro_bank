$(function()
{

	var flag1=false;
	var flag2=false;
	var flag3=false;
	
	$.ajax({
		url:"/nb/wechat/account/getAccountData.html?random="+Math.random(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			if(null!==res)
			{
				$("#total").html(res.total);
				$("#netProfit").html("&yen;"+res.netProfit);
				$("#collect_mb").html("&yen;"+res.collection);
				$("#useMoney").html("&yen;"+res.useMoney);
				$("#noUseMoney").html("&yen;"+res.noUseMoney);
				$("#experienceMoney").html("&yen;"+res.experienceMoney);
				if("0"==res.redPacketMoney){
					$("#redPacketMoney").html("暂无红包");
					$("#redPacketMoney").css("color","rgb(142,142,142)");
				}else{
					$("#redPacketMoney").html(res.redPacketMoney);
				}
				
				getSplit("total",res.total);
				getSplit("netProfit",res.netProfit);
				getSplit("collect_mb",res.collection);
				getSplit("useMoney",res.useMoney);
				getSplit("noUseMoney",res.noUseMoney);
			
				var flagShow = changeShow("useMoney",res.useMoney);
				if(flagShow){
					flag1=true;
				}
				
				var flagShow1 = changeShow("collect_mb",res.collection);
				if(flagShow1){
					flag2=true;
				}
				
				var flagShow2 = changeShow("noUseMoney",res.noUseMoney);
				if(flagShow2){
					flag3=true;
				}
				
				if(flag1){
					$("#collect_mb").css("font-size","1.1em");
					$("#noUseMoney").css("font-size","1.1em");
				}
				
				if(flag2){
					$("#useMoney").css("font-size","1.1em");
					$("#noUseMoney").css("font-size","1.1em");
				}
				
				if(flag3){
					$("#useMoney").css("font-size","1.1em");
					$("#collect_mb").css("font-size","1.1em");
				}
				

			}
		}
	});
	localStorage.type = "btn_one";
	
	
});
function retain(money)
{
	return Math.round(money*100)/100;
}

function getSplit(id,num)
{
	var pageMoney = parseFloat(num).toFixed(2).toString();
	var array = new Array();
	array=pageMoney.split(".")[0]; 
	console.log(array)
	pageUseMoney = "";
	var arrayFirst = new Array();
	var arraySecond = new Array();
	var j = 0;
	
	for(var i = array.length-1;i>=0;i--)
	{
		if(0===(array.length-i)%3&&i!==(array.length-1)&&0!==i)
		{
			arrayFirst[j] = array[i];
			j++;
			arrayFirst[j] = ",";
		}
		else
		{
			arrayFirst[j] = array[i];
		}
		j++;
	}

	
	for(var k = arrayFirst.length-1;k>=0;k--)
	{
	
		pageUseMoney += arrayFirst[k]; 
	}
	if(j>10)
	{
		$("#"+id).css("font-size","1.2em");
	}
	$("#"+id).html("¥"+pageUseMoney+"."+pageMoney.split(".")[1]);
}

function changeShow(id,num)
{
	var pageMoney = parseFloat(num).toString();
	var array = new Array();
	array=pageMoney; 
	var j = array.length;

	if(j>=7)
	{
		$("#"+id).css("font-size","1.1em");
		return true;

	}
	
	return false;
	
}
