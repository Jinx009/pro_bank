define(function(require,exports,module){
    require('jquery');
    var tpl = require('../../tpl/member/recharge_log.tpl');//载入tpl模板
    //初始化显示表格、搜索
    require.async(['./showTable','./search'],function(showTable,search){
        showTable.ajaxUrl('/member/recharge/logList.html?status='+9,tpl);
        search.search('/member/recharge/logList.html?',tpl);
    });
});