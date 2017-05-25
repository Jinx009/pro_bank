define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/member_guarantee/collection_collectionList.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member_guarantee/collection/collectionListJSON.html?time=0&userName='+ $(".sInput").val(),tpl);
		search.search('/member_guarantee/collection/collectionListJSON.html?',tpl);
	});	
});
