define(function(require, exports, modlue) {
	require('jquery');
	$.ajax({
			url : "/wx/recommend.html?type=index&random="+ Math.random(),
			type : "get",
			dataType : "json",
			success : function(data) {
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',
					function() {
						require.async('/plugins/handlebars-v1.3.0/transFormatJson',
							function() {
								if(isWeiXin()){
									if (data.ppfund != undefined) {
										var tpl = require('/themes/theme_default/wx/tpl/index_andriod.tpl');// 载入tpl模板
									} else {
										var tpl = require('/themes/theme_default/wx/tpl/indexborrow2.tpl');// 载入tpl模板
									}
									var template = Handlebars.compile(tpl);
									var html = template(data);
									$("#index-borrow").html(html);
								}else{
									if (data.ppfund != undefined) {
										var tpl = require('/themes/theme_default/wx/tpl/index.tpl');// 载入tpl模板
									} else {
										var tpl = require('/themes/theme_default/wx/tpl/indexborrow2.tpl');// 载入tpl模板
									}
									var template = Handlebars.compile(tpl);
									var html = template(data);
									$("#index-borrow").html(html);
									require.async("../../wx/js/jquery.circliful.min.js",
										function() {
											$('.circle').circliful();
									});
								}
							})
					})
			}
	})
	/*判断是否是微信浏览器-主要是安卓机*/
	function isWeiXin(){ 
		var ua = window.navigator.userAgent.toLowerCase(); 
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){ 
			return true; 
		}else{ 
			return false; 
		} 
	} 
});