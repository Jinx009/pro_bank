define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/member_guarantee/compensatory_compensatoryList.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member_guarantee/compensatory/getCompensatedList.html?time=-1&borrowName='+ $(".sInput").val(),tpl);
		search.search('/member_guarantee/compensatory/getCompensatedList.html?',tpl);
	});	
});
