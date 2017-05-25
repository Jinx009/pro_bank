define(function(require,exports,module){
	require('jquery');
	$(function(){
		$(".view-con li:nth-child(3n)").css("background","none")
  	});

  
	
	var financeSiteId = window.location.search.split("?");
	var financeArticleId = financeSiteId[1];
			$.ajax({
			url:"/lcschool/expertOpinion.html?financeArticleId=" + financeArticleId,
			type:"get",
			dataType:"json",
			success:function(data){
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
						var tpl = require('/themes/theme_default/media/tpl/viewDetail.tpl');//载入tpl模板
						var template = Handlebars.compile(tpl);
						var html = template(data);
						$(".schloolview").html(html);
						require.async('common1',function(){
							$(".schloolview").tabChange({
								isClick:true,
					            isHover:false,
								childLi:".view-tab1 span",//tab选项卡
								childContent:".view-detail",//tab内容
								hoverClassName:"active",//选中当前选项卡的样式
								callBack:false	
							});
							$(".expert-wx").mouseover(function(){
						  		$(".export-look-wx").removeClass("hide")
						  	})
						  	$(".expert-wx").mouseout(function(){
						  		$(".export-look-wx").addClass("hide")
						  	})
						});
					})
				})
			}
			});
			
			$.ajax({
				url:"/lcschool/financeArticleList.html?financeArticleId=" + financeArticleId,
				type:"get",
				dataType:"json",
				success:function(data){
					require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
						require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
							var tpl = require('/themes/theme_default/media/tpl/viewDetailList.tpl');//载入tpl模板
							var template = Handlebars.compile(tpl);
							var html = template(data);
							$("#detailList").html(html);
						})
					})
				}
				});
})



