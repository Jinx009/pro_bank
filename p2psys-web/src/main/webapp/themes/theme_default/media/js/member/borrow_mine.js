define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/borrow_mine.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member/borrow/mineList.html?status='+99,tpl);
		search.search('/member/borrow/mineList.html?',tpl);
	});
	
});