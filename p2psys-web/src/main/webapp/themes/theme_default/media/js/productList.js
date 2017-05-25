define(function(require,exports,module){
	require('jquery');
	var list_data;
	$(function(){
		
		/**
		 * 获取所有的标签
		 */
		$.ajax({
			url:"/product/showProductTypeFlagList.html",
			type:"GET",
			ansyc:false,
			dataType:"json",
			success:function(res)
			{
				list_data = res.data;
				getListData();
			}
		})
		
		
  	});

	function getListData(){
		var htmlStr = "";
		var htmlStrTitle = "";
		for(var i = 0;i<list_data.length;i++){
			var picUrl = list_data[i].iconUrl;
			if(picUrl==""||picUrl==null){
				picUrl = "/themes/theme_default/images/products-1.png";
			}
			if(i%2==0){
				htmlStr+="<div class='products-con-border' id=products"+list_data[i].id+" style='background:#f9f9f9;'>" +
				"<div class='products-con clearfix'>"+
				"<div class='products-left'><img src="+picUrl+" width='125'></div>"+
				"<div class='products-right'>" +
				"<div class='products-right-title'>"+list_data[i].flagName+"</div>"+
				"<div class='clearfix products-right-con'><p>"+list_data[i].flagDescription+"</p>" +
				"<a href='/nb/pc/product/productList.html?id="+list_data[i].id+"'>我要投资</a></div></div></div></div>";
				
				
			}else{
				htmlStr+="<div class='products-con-border' id=products"+list_data[i].id+" style='background:#FFFFFF;'>" +
				"<div class='products-con clearfix'>"+
				"<div class='products-left'><img src="+picUrl+" width='125'></div>"+
				"<div class='products-right'>" +
				"<div class='products-right-title'>"+list_data[i].flagName+"</div>"+
				"<div class='clearfix products-right-con'><p>"+list_data[i].flagDescription+"</p>" +
				"<a href='/nb/pc/product/productList.html?id="+list_data[i].id+"'>我要投资</a></div></div></div></div>";
			}
			
			if(i==0){
				htmlStrTitle+= "<a href='javascript:void(0)' class='active a"+list_data[i].id+"' >"+list_data[i].flagName+"</a>";
			}else{
				htmlStrTitle+= "<a href='javascript:void(0)' class=a"+list_data[i].id+">"+list_data[i].flagName+"</a>";
			}
			
		}
		
		$("#productFlagList").html(htmlStr);
		$("#productFlagTitle").html(htmlStrTitle);
		
		
		var oli = $("#productFlagTitle a");
		oli.each(function(){	 
			$(this).click(function(){
				oli.removeClass("active");
				$(this).addClass("active");
			})
		});
		
		require.async(["commonJS/keep","commonJS/rollTo"],function(){
			for(var i=0;i<list_data.length;i++){
				$("#productFlagTitle .a"+list_data[i].id).rollTo({
					oFinish: "#products"+list_data[i].id, //要滚动到的元素
					sSpeed: "300",  //滚动速度
					bMonitor: true, //是否楼层监听
					sClass: "active", //楼层监听时需要添加的样式
					iBias:-110,
					fnAdditional: "" //追加方法
				}); 
			}
			
		});
	}

})

