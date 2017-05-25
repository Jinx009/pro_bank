		var activityId = "";
		var num = 0;
		
		$(function()
		{
			var status = $("#status").val();
			var userId = $("#userId").val();
			if("0"==status)
			{
				location.href = $("#url").val();
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
							var url =  $("#url2").val();
							location.href = url;
						}
						else
						{
							showDiv("rule");
						}
					}
				})
			}
		})
		var resultData;
		var sendStatus = false;
		
		/**
		 * 执行结果
		 */
		function doActivity()
		{
			var userId = $("#userId").val();
			
			if(!sendStatus)
			{
				sendStatus = true;
				$.ajax({
					url:"/nb/wechat/card/activityResult.action",
					type:"POST",
					data:"userId="+userId,
					dataType:"json",
					ansyc:false,
					success:function(res)
					{
						sendStatus = false;
						resultData = res;
						num++;
						showDiv("share");
					}
				})	
			}
		}
		
		/**
		 * 抽奖结果
		 */
		function showResult()
		{
			var htmlStr = "";
			if("success"==resultData.result)
			{
				$(".red_msg1").html(resultData.errorMsg);
				showDiv("result_edit");
			}
			else
			{
				if(num<2)
				{
					showDiv("playAgain");
				}
				else
				{
					showDiv("again");
				}
			}
		}
		
		/**
		 * 发送短信
		 */
		function sendMsg()
		{
			var mobilePhone = $("#mobilePhone").val();
			var params = "tel="+mobilePhone+"&cardNo="+resultData.cardNo+"&cardPassword="+resultData.cardPassword+"&cardDesc="+resultData.errorMsg+"&activityId="+resultData.msg;
			$.ajax({
				url:"/nb/wechat/sendCardCode.action",
				type:"POST",
				data:params,
				dataType:"json",
				ansyc:false,
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
			if("rule"==divId)
			{
				$(".red_bg_one").css("display","block");
				$(".red_bg").css("display","none");
			}
			else
			{
				$(".red_bg_one").css("display","none");
				$(".red_bg").css("display","block");
			}
			 
			$("."+divId).css("display","block");
		}
		