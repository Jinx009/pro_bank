define(function(require,exports,module){
	require('jquery');
	$(function(){
		$(".view-con li:nth-child(3n)").css("background","none")
  	});
	
	var financeSiteId = window.location.search.substr(1);
	
	$.ajax({
		url:"/lcschool/expertIntroduce.html?" + financeSiteId,
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/expert.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".expert-box").html(html);
					$("#expertPoint").attr("href","/lcschool/view.html?"+financeSiteId)
					$("li").each(function(){
						$(this).find(".expert-wx").mouseover(function(){
							$(this).parent().find(".export-look-wx").removeClass("hide")
					  	})
					  	$(this).find(".expert-wx").mouseout(function(){
							$(this).parent().find(".export-look-wx").addClass("hide")
					  	})
					  
					})
				})
			})
		}
	});

	$.ajax({
		url:"/lcschool/showFinanceSiteList.html?"+financeSiteId,
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/lm-title.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".view-banner").html(html);
				})
			})
		}
	});
	
})



