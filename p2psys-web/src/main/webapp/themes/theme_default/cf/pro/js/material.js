$('.edit').click(function(){
	$(this).closest('td').siblings().html(function(i,html){
		if(i==4){
			return html;
		}else{			
	        return '<input type="text" value='+html+' />';
		}
    });
    $(this).hide();
    $(this).siblings(".save").show();
})

/**
 *根据权益名称动态修改素材Code和素材类型 
 */
$("#rightsName button").click(function(){

	var dataValue = $(this).attr("data-value");
	//type=1  表示文字
	//type=2  表示文件
	//type=3 表示图片
	var type; 
	var typeValue = "";//素材类型 
	dataValue=="act_code"?type=1:(dataValue=="business_book"?type=2:type=3);
	type===1?typeValue="文字":(type===2?typeValue="文件":typeValue="图片");
	$("#materialCode").val(dataValue);
	$("#materialTypeValue").val(typeValue);
	if(type===1){
		$("#uploadImg").hide();
		$("#textarea").show();
	}else{
		$("#uploadImg").show();
		$("#textarea").hide();
	}
})