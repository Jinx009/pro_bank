define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/invest_collection.tpl');//载入tpl模板
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member/invest/collectionList.html?time=-1&status=-1&borrowName='+ $(".sInput").val(),tpl);
		search.search('/member/invest/collectionList.html?',tpl);
	});	
});