define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/borrow_repayment.tpl');//载入tpl模板
	//初始化显示表格、搜索
	console.log(window.location.search.substr(1));
	if(window.location.search.indexOf('borrowId'))
	{
		require.async(['./showTable','./search'],function(showTable,search){
			showTable.ajaxUrl('/member/borrow/repaymentList.html?'+window.location.search.substr(1),tpl);
			search.search('/member/borrow/repaymentList.html?',tpl);
		});
	}
	else
	{
		require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/member/borrow/repaymentList.html?status='+99,tpl);
		search.search('/member/borrow/repaymentList.html?',tpl);
	});
	}
	
	//还款
	$("#toPayBtn").live("click",function(){
		var idVal = $(this).attr("data-val");
		window.open("/invest/repaySkip.html?repaymentId="+idVal);
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
			    type: 1,
			    closeBtn: false,
			    title: false,
			    area: ['220px', '100px'],
			    shade: [0.5, '#000'],
			    border: [10, 0.3, '#000'],
			    page: {
			        html: '<div class="fileconfirm"><span><i class="iconfont">&#xe614;</i>请选择操作？</span><div class="fileconfirmbtn"><a href="javascript:;" class="okBtn">还款成功</a><a href="javascript:;"  class="cancleBtn">还款失败</a></div></div>'
			    }
			});	
			//确认操作
			$(".okBtn").click(function(){
				$.ajax({
					url:"/themes/theme_default/media/json/msg.json",
					dataType:"json",
					success: function(data){
						if(data.result){
							location.reload();
						}						
					}			
				});				
			});
			//删除操作
			$(".cancleBtn").click(function(){
				layer.closeAll();
			});
		}); 
	})
	
	//弹窗控件
/*	function repay(url){
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			layer.prompt({title: '请输入交易密码',type: 1}, function(pass){
			$.ajax({
				type:"post",
				url:url + '&payPwd=' + pass,
				dataType:"json",
				success:function(data){
					if(!data.result){
						layer.msg(data.msg, 2, function(){});
					}else{
						layer.msg('还款成功', 2, function(){
						    location.reload();
						});
					}
				}
			});
		});
		})
	}*/
});