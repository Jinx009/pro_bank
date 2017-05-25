define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/member_borrow/borrow_mine.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		if(window.location.search.split("?")[1] == undefined)
		{
			showTable.ajaxUrl('/member/borrow/mineList.html?status=99&time=9',tpl);
		}
		else
		{
			showTable.ajaxUrl('/member/borrow/mineList.html'+window.location.search+'&time=9',tpl);
			$("#status a:eq(0)").removeClass("current");
			$("#status a:eq(2)").addClass("current");
		}
		search.search('/member/borrow/mineList.html?',tpl);
	});
	
});