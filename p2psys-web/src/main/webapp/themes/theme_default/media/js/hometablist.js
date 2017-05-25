define(function(require,exports,modlue){
	require('jquery');		
	//ppfundtablist
	$.ajax({
		url:"/index/recommend.html?type=ppfund&random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					if(data.ppfund!= undefined){
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow1.tpl');//载入tpl模板
					}
					else{
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow2.tpl');//载入tpl模板	
					}
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$("#product-right-con1").html(html);
				})
			})
		}
	})

	$.ajax({
		url:"/index/recommend.html?borrowType=119&status=1&random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					if(data.borrow!= undefined){
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow3.tpl');//载入tpl模板
					}
					else{
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow2.tpl');//载入tpl模板	
					}
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$("#product-right-con2").html(html);
				})
			})
		}
	})

	$.ajax({
		url:"/index/recommend.html?borrowType=103&status=1&random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					if(data.borrow!= undefined){
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow3.tpl');//载入tpl模板
					}
					else{
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow2.tpl');//载入tpl模板	
					}
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$("#product-right-con3").html(html);
				})
			})
		}
	})


	$.ajax({
		url:"/index/recommend.html?borrowType=122&status=1&random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					if(data.borrow!= undefined){
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow3.tpl');//载入tpl模板
					}
					else{
					  var tpl = require('/themes/theme_default/media/tpl/indexborrow2.tpl');//载入tpl模板	
					}
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$("#product-right-con4").html(html);
				})
			})
		}
	})


});

