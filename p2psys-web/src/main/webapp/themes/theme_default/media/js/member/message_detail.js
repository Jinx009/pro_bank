define(function(require,exports,module){
	require('jquery');
	$.ajax({
		url:'/member/message/box.html?randomTime='+(new Date()).getTime(),
		type:'get',
		dataType:'json',
		success:function(json){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
				var tpl = require('../../tpl/member/message_detail.tpl');
				var template = Handlebars.compile(tpl);
				var html = template(json);
				$("#messagelist").html(html);
				if(json.data.page.pages > 0)
				{
					require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function(){
						kkpager.generPageHtml({
								pno : json.data.page.currentPage,//当前页码
								total : json.data.page.pages,//总页码
								totalRecords : json.data.page.total,//总数据条数
								isShowFirstPageBtn	: false, 
								isShowLastPageBtn	: false, 
								isShowTotalPage 	: false, 
								isShowTotalRecords 	: false, 
								isGoPage 			: false,
								lang:{
									prePageText				: '<',
									nextPageText			: '>'
								},
								mode:'click',
								click:function(n){
						        	$.ajax({
						        		type:"get",
						        		url:"/member/message/box.html?page="+n,
						        		dataType:"json",
						        		success:function(json){
												var template = Handlebars.compile(tpl);
												var html    = template(json);
												$('#messagelist').html(html);
						        		}
						        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
						});
					});
				}
				else
				{
					$("#kkpager").html('暂无数据');
				}
				})
			})

			
		}
	});

	//查看详细消息
	$(".showdrop").live("click",function(){
		$(this).toggleClass("hover");
		$(this).parent().next(".hideContent").slideToggle().parents("li").siblings("li").find(".showdrop").removeClass("hover").parent().next().slideUp();
		$(this).parent().siblings(".showbox_left").find(".readRemark").html('<i class="iconfont readed">&#xe60e;</i>已读')
		$.ajax({
			url:'/member/message/set.html?type=4&id='+$(this).data('id')
		});
	})

	//全选
	$(".checkAll_label").click(function() {
		if($(".checkAll").prop("checked"))
		{
			$(".checkAll").prop("checked",false);
			$(this).removeClass("checkAll_check_label");
			$(".messagelist input[type='checkbox']").prop("checked",false);
			$(".user_pic").removeClass("pic_selected");
		}
		else
		{
			$(".checkAll").prop("checked",true);
			$(this).addClass("checkAll_check_label");
			$(".messagelist input[type='checkbox']").prop("checked",true);
			$(".user_pic").addClass("pic_selected");
		}
	})
	
	//单选选择框
	$(".user_pic").live("click",function(){
		$(this).find("input").prop("checked",!$(this).find("input").prop("checked"));
		$(this).toggleClass("pic_selected");
	})

	//标记已读
	$("#signRead").click(function(){
		if($("#messagelist").find("input:checked").length > 0)
		{
			$('#type').val('4'); 
			require.async('jquery.form',function(){
				$('form').ajaxSubmit(function(data){
					if(data.result)
					{
						tip('标记已读成功！');
						$("#messagelist").find("input:checked").parent().next(".showbox ").find(".readRemark").html('<i class="iconfont readed">&#xe60e;</i>已读');
					}
					else
					{
						tip('操作失败！');
					}
				});
			})
			
		}
		else
		{
			tip('请至少选择一条信息！');
		}
	})

	//标记未读
	$("#signUnRead").click(function(){
		if($("#messagelist").find("input:checked").length > 0)
		{
			$('#type').val('3'); 
			require.async('jquery.form',function(){
				$('form').ajaxSubmit(function(data){
					if(data.result)
					{
						tip('标记未读成功！');
						$("#messagelist").find("input:checked").parent().next(".showbox ").find(".readRemark").html('<i class="iconfont unread">&#xe616;</i>未读');
					}
					else
					{
						tip('操作失败！');
					}
				});
			})
		}
		else
		{
			tip('请至少选择一条信息！');
		}
	})

	//删除信息
	$("#delMsg").click(function(){
		if($("#messagelist").find("input:checked").length > 0)
		{
			$('#type').val('1'); 
			require.async('jquery.form',function(){
				$('form').ajaxSubmit(function(data){
					if(data.result)
					{
						// 删除效果处理
						// $("#messagelist").find("input:checked").parents("li").animate({left: '+898px'}, 1000).slideUp(300);
						// tip('删除信息成功！');
						window.location.reload();
					}
					else
					{
						tip('操作失败！');
					}
				});
			})
		}
		else
		{
			tip('请至少选择一条信息！');
		}
	})
	//tip提示框
	function tip(content)
	{
		$("#msgTip").animate({opacity: "show"}, 0).html(content).animate({opacity: "hide"}, 2000);
	}
});