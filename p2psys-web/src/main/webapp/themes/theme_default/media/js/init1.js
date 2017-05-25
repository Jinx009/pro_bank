
define(function(require , exports , module){
	require.async('jquery',function(){
		var common = require('./common1');
		var $cookie = require('./cookie');
		var slideFun = require.async('./jquery.flexslider-min');
		require.async([
			'./tipswindown',
			'./usejs',//执行插件
			//'footer',//底部链接
			'../css/layer.css'//加载layer.css
		]);
	});
	
	
});
