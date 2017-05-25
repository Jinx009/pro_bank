define(function(require,exports,module){
	require('jquery');
	$("#status a:eq(0)").addClass("current");
	var tpl = require('../../tpl/member/account_log.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member/account/logList.html?typeName='+0,tpl);
		search.search('/member/account/logList.html?',tpl);
	});
});