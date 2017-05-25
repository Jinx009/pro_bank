var data;
var matList;
var product_id;
$(function()
{
	
	var product_child_id = $("#product_child_id").val();
	product_id = $("#product_id").val();

	if("WC205"==product_child_id){
		$("#WC205").show();
		$(".backTitle").html("产品详情");
	}
	if("WC207"==product_child_id){
		$("#WC207").show();
		$(".backTitle").html("收益详情");
	}
	if("WC209"==product_child_id){
		$("#WC209").show();
		$(".backTitle").html("保障方式");
	}

	$.ajax({
		url:"/product/showProductDetail.html?prodId="+product_id,
		type:"GET",
		ansyc:false,
		dataType:"json",
		success:function(res)
		{
			data = res.data;
			matList = res.matList;
			changeHtml();

		},
		error:function(res){
			alert(res);
		}
	})

})


/**
 * 拼接html
 */
function changeHtml()
{	
	if(matList.length>0)
	{
		for(var i = 0;i<matList.length;i++)
		{
			if("WC206"===matList[i].materialType)
			{	
				$("#WC206").html(matList[i].material);
			}
			if("WC204"===matList[i].materialType)
			{
				$("#WC204").html(matList[i].material);
			}
			if("WC208"===matList[i].materialType)
			{
				$("#WC208").html(matList[i].material);
			}
			if("WC205"===matList[i].materialType)
			{
				if(null!==matList[i].material&&""!==matList[i].material)
				{
					$("#WC205").html(matList[i].material);
				}
			}
			if("WC209"===matList[i].materialType)
			{
				if(null!==matList[i].material&&""!==matList[i].material)
				{
					$("#WC209").html(matList[i].material);
				}
			}
			if("WC207"===matList[i].materialType)
			{
				if(null!==matList[i].material&&""!==matList[i].material)
				{
					$("#WC207").html(matList[i].material);
				}

			}			
		}
	}

}

/**
 * 返回
 */
function goBack()
{
	location.href = "/nb/wechat/product/productDetail.html?product_id="+$("#product_id").val();	
}

