define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/invest_mine.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member/invest/mineList.html?time=-1&status=-1&borrowName='+ $(".sInput").val(),tpl);
		search.search('/member/invest/mineList.html?',tpl);
	});	
});
