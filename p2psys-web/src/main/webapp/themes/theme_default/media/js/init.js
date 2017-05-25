define(function(require , exports , module){
	var common = require('./common');
	var $cookie = require('./cookie');
	//var slideFun = require('./jquery.flexslider-min');
	require.async([
		'./tipswindown',
		'./usejs',//执行插件
		'footer',//底部链接
		'../css/layer.css'//加载layer.css
	]);
	
});
