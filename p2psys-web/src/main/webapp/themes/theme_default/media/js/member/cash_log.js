define(function(require,exports,module){
    require('jquery');
    var tpl = require('../../tpl/member/cash_log.tpl');//载入tpl模板
    //初始化显示表格、搜索
    require.async(['./showTable','./search'],function(showTable,search){
        showTable.ajaxUrl('/member/cash/logList.html?status='+9,tpl);
        search.search('/member/cash/logList.html?',tpl);
    });
});