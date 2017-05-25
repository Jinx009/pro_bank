		var activityId = "";
		var num = 0;
		
		$(function()
		{
			var status = $("#status").val();
			var userId = $("#userId").val();
			var url = $("#url").val();
			if("0"==status)
			{
				location.href = url;
			}
			else
			{
				$.ajax({
					url:"/nb/wechat/card/activityRecord.action",
					type:"POST",
					data:"userId="+userId,
					dataType:"json",
					ansyc:false,
					success:function(res)
					{
						num = parseInt(res.errorMsg.times);
						var htmlStr = "";
						activityId = res.errorMsg.id;
						if(res.errorMsg.cardFactory)
						{
							$(".red_msg1").html(res.errorMsg.cardFactory.cardDesc);
							showDiv("result_edit");
						}
						else
						{
							if(num<2)
							{
								showIndex();
							}
							else
							{
								showDiv("again");
							}
						}
					}
				})
			}
		})

		/**
		 * 再次发送短信
		 */
		function reSendMsg()
		{
			var mobilePhone = $("#mobilePhone").val();
			var params = "activityId="+activityId+"&mobilePhone="+mobilePhone;
			$.ajax({
				url:"/nb/wechat/reSendCode.action",
				type:"POST",
				data:params,
				dataType:"json",
				success:function(res)
				{
					if("success"==res.result)
					{
						alert("发送成功!");
					}
				}
			})
		}
				
		/*
		*跳转首页
		*/
		function showIndex()
		{
			location.href = $("#url").val();
		}
		
		/*
		*显示与隐藏
		*/
		function showDiv(divId)
		{
			$(".share").css("display","none");
			$(".code").css("display","none");
			$(".sure").css("display","none");
			$(".playAgain").css("display","none");
			$(".again").css("display","none");
			$(".rule").css("display","none");
			$(".result_edit").css("display","none");
			$(".result").css("display","none");
			$(".code").css("display","none");
			
			$("."+divId).css("display","block");
		}
		