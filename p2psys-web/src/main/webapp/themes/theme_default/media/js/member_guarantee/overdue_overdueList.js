define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/member_guarantee/overdue_overdueList.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member_guarantee/overdue/getOverdueGuaranteeList.html?time=-1&borrowName='+ $(".sInput").val(),tpl);
		search.search('/member_guarantee/overdue/getOverdueGuaranteeList.html?',tpl);
	});	
});
