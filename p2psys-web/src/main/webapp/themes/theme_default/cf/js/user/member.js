var flag = 0,payFlag =0;
var url="/cf/user/infor.html"
$(function(){
	$.ajax({
		url:"/cf/userInfor.html",
		type:"get",
		dataType:"json",
		success:function(res){
			if("success"==res.result){
				$("#nickName").val(res.theUser.userCache.nickName);
				if(res.theUser.userCache.birthday){
					$("#birthday").val(res.theUser.userCache.birthday);
				}else{
					$("#birthday").val();
				}
				if(0==res.theUser.userCache.sex){
					$("#sexF").prop("checked",true);
				}else{
					$("#sexM").prop("checked",true);
				}
				if(res.theUser.userCache.cardPositive===""||res.theUser.userCache.cardPositive===undefined || res.theUser.userCache.cardPositive===null){
					$("#headerImg").attr("src","/themes/theme_default/cf/img/team_icon.jpg");
				}else{
					$("#headerImg").attr("src","http://"+window.location.host+res.theUser.userCache.cardPositive);
				}
				
				$("#email").val(res.theUser.email);
				$("#mobilePhone").val(res.theUser.mobilePhone);
				$("#realName").val(res.theUser.realName);
				$("#userId").val(res.theUser.userId)
				if(""==res.theUser.payPwd || null ==res.theUser.payPwd){
					$("#payStatus").val(0);
				}else{
					$("#payStatus").val(1);
				}
			}else{
				$("#errorMsg").html(res.errorMsg);
			}
		}
	});
	
	
	
	$(".saveInfor").click(function(){
		var sex =$("input[name='sex']:checked").val();
		$.ajax({
			url:"/cf/set/infor.html",
			type:"POST",
			dataType:"json",
			data:{
				id:$("#userId").val(),
				nickName:$("#nickName").val(),
				birthday:$("#birthday").val(),
				sex:sex,
				email:$("#email").val(),
				mobilePhone:$("#mobilePhone").val(),
				realName:$("#realName").val(),
				pwd:$("#pwd").val(),
				newPwd:$("#newPwd").val(),
				confirmNewPwd:$("#confirmNewPwd").val(),
				payPwd:$("#paypwd").val(),
				newPayPwd:$("#newpayPwd").val(),
				confirmNewPayPwd:$("#confirmNewpayPwd").val(),
				flag:flag,
				payFlag:payFlag
			},
			success:function(res){
				if(res.result =="success"){
					url="/cf/user/infor.html";
					showDiv(res.msg);
				}else{
					showDiv(res.errorMsg);
					url="/cf/user/infor.html";
				}
			}
		});
	});
	
	
	$(".up").click(function(){
		$("#noShow").show();
		$("#show").slideUp();
		$("#save_").show();
		flag =0;
	});
	
	$(".payUp").click(function(){
		$("#noPayShow").show();
		$("#payShow").slideUp();
		$("#save_").show();
		payFlag =0;
	});
	
	$("#modifyPwd").click(function(){
		$("#noShow").hide();
		$("#noPayShow").show();
		$("#payShow").hide();
		$("#show").slideDown();
		$("#save_").hide();
		flag =1;
		payFlag =0
	});
	
	$("#modifyPayPwd").click(function(){
		if(0==$("#payStatus").val()){
			url="/cf/user/set-pay.html?redirectUrl=/cf/user/infor.html"
			showDiv("您当前还没有设定交易密码,点击确定前往设定");
		}else{
			$("#noPayShow").hide();
			$("#show").hide();
			$("#noShow").show();
			$("#payShow").slideDown();
			$("#save_").hide();
			payFlag =1;
			flag =0;
		}
		
	});
});
function showDiv(msg){
	$("#errorMsg").html(msg);
	$("#successBtn").attr("onclick","goUrl()");
	$("#errorDiv").show();
}

function goUrl(){
	location.href=url;
}
