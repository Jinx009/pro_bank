define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/myInvest.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member/invest/myInvestList.html?status=99&borrowName='+ $(".sInput").val(),tpl);
		search.search('/member/invest/myInvestList.html?',tpl);
	});	
});
