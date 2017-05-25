var map_data;
$(function()
{
	var product_id = $("#product_id").val();
	/**
	 * 组合标数据
	 */
	$.ajax({
		url:"/product/showProductSetDetail.html?prodId="+product_id,
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			map_data = res.data;
			
			getMapData();
			
		}
	})
})
function buy(product_id)
{
	location.href = "/nb/wechat/account/pay.html?productBasicId="+product_id;
}

/**
 * 组合标数据呈现
 */

function getMapData()
{

	var htmlStr = "";
	
	var lightspot = map_data.lightspot;
	if(""==lightspot||null==lightspot){
		lightspot="暂无介绍。。。";
	}
	htmlStr += "<div class='productList detailBottom' >";
	htmlStr += "<p><span class='indexType' >["+map_data.typeDesc+"]</span>";
	htmlStr += "<span class='indexTitle' >"+map_data.productName+"</span>";
	htmlStr += "<span class='indexHasCast' ></span></p>";
	htmlStr += "<span class='indexYieldDesc' >组合预期年化收益率</span><div class='productListBtn' onclick=buy("+map_data.id+") >投资</div><br />";
	htmlStr += "<span class='indexYield' >"+getRate(map_data.lowestRefundRate,map_data.highestRefundRate)+"</span>";
	
	htmlStr += "<p class='popTip'>"+lightspot+"</p>";
		
	for(var j = 0;j<map_data.subProductList.length;j++)
	{
		if(j!=map_data.subProductList.length-1)
		{
			htmlStr += "<div class='popDetail' onclick=lookDetail('"+map_data.subProductList[j].id+"') >";
			htmlStr += "<span class='indexType'>["+map_data.subProductList[j].typeDesc+"]";
			htmlStr += "</span><span class='indexTitle'>"+map_data.subProductList[j].productName+"</span>";
			htmlStr += "<span class='allocation'>配置比例<span class='allocation_yield'>"+map_data.subProductList[j].rate+"%</span></span></div>";
		}
		else
		{
			htmlStr += "<div class='popDetail popDetail_other'  onclick=lookDetail('"+map_data.subProductList[j].id+"')  >";
			htmlStr += "<span class='indexType'>["+map_data.subProductList[j].typeDesc+"]";
			htmlStr += "</span><span class='indexTitle'>"+map_data.subProductList[j].productName+"</span>";
			htmlStr += "<span class='allocation'>配置比例<span class='allocation_yield'>"+map_data.subProductList[j].rate+"%</span></span></div>";
		}
	}
		
	
	$("#map").html(htmlStr);
}

function lookDetail(productId)
{
	var redirectURL = $("#redirectURL").val();
	
	if(null!==redirectURL&&""!==redirectURL)
	{
		location.href = "/nb/wechat/product/productDetail.action?product_id="+productId+"%26redirectURL=/nb/wechat/product/mapProductDetail.action?product_id="+$("#product_id").val()+"%26redirectURL="+redirectURL;
	}
	else
	{
		location.href = "/nb/wechat/product/productDetail.action?product_id="+productId+"%26redirectURL=/nb/wechat/product/mapProductDetail.action?product_id="+$("#product_id").val();
	}
}

/**
 * 获取总金额
 */
function getAccount(account)
{
	var real_account = parseFloat(account).toFixed(2);
	
	return parseFloat(real_account/10000).toFixed(2);
}
function getRate(low,high)
{
	var lowArr = new Array();
	var highArr = new Array();
	var lowStr = parseFloat(low).toFixed(2).toString();
	var highStr = parseFloat(high).toFixed(2).toString();
	
	
	lowArr[0] = lowStr.split(".")[0]; 
	highArr[0] = highStr.split(".")[0];
	
	lowArr[1] = lowStr.split(".")[1]; 
	highArr[1] = highStr.split(".")[1];
	
	if(low===high&&"0"==high)
	{
		return "<span class=smallYield>浮动</span>";
	}
	if(low===high&&"0"!==high)
	{
		return "<span class=bigYield>"+lowArr[0]+"</span>.<span class=smallYield>"+lowArr[1]+"%</span>";
	}
	if(low!==high&&"0"==high)
	{
		return "<span class=bigYield>"+lowArr[0]+"</span>.<span class=smallYield>"+lowArr[1]+"%+浮动</span>";
		
	}
	if(low!==high&&"0"!==high)
	{
		
		return "<span class=bigYield>"+lowArr[0]+"</span>."+lowArr[1]+"<span class=smallYield>%</span>-<span class=bigYield>"+highArr[0]+"</span>."+highArr[1]+"<span class=smallYield>%</span>";
	}
}
/**
 * 真正剩余时间
 * 
 * @param day_time
 * @returns {String}
 */
function getTimeLimit(day_time)
{
	if("0"==day_time)
	{
		return "无期限可随时赎回";
	}
	else
	{
		return day_time+"天";
	}
}

function goBack()
{
	var redirectURL = $("#redirectURL").val();
	
	if(null!==redirectURL&&""!==redirectURL)
	{
		location.href = redirectURL;
	}
	else
	{
		location.href = "/nb/wechat/product/product_menue.html";
	}
}