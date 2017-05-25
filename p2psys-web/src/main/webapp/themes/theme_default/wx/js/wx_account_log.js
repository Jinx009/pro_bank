define(function(require,exports,module){
	require('jquery');
	var tpl = require('/themes/theme_default/wx/tpl/account_log.tpl');//载入tpl模板
	//初始化显示表格
	require.async('./showTable',function(showTable){
		showTable.ajaxUrl('/member/account/logList.html?time=-1&status=0',tpl);
	});
});