define(function(require, exports, modlue) {
	require('jquery');

	// 通用显示函数
	$.ajax({
		type : 'get',
		url : '/invest/investJson.html?randomTime=' + (new Date()).getTime()
				+ "&&type=103&&status=1",
		dataType : 'json',
		success : function(json) {
			
		},
		error : function() {
		}
	})

	// 翻页置顶
	require.async("commonJS/rollTo", function() {
		$("#kkpager").rollTo({
			oFinish : ".investList ", // 要滚动到的元素
			sSpeed : "300", // 滚动速度
			bMonitor : false, // 是否楼层监听
			sClass : "", // 楼层监听时需要添加的样式
			iBias : -18,
			fnAdditional : "" // 追加方法
		});
	})

});