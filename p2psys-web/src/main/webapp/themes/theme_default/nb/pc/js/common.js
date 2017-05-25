var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var errorMsg = "";
var adminUrl = "";
var webUrl = "";
/**
 * 样式加载
 */
$(function()
{
	userAgent();
	var windowWidth = $(window).width();
	var popudTopWidth = 330;
	var popudTopMiddleWidth = 72;
	
	$(".popup-top").css("left",(parseInt(windowWidth)-popudTopWidth)/2);
	$(".popup-top-min").css("left",(parseInt(windowWidth)-popudTopMiddleWidth)/2);
	$(".text-div").css("left",(parseInt(windowWidth)-popudTopWidth+2)/2);
	$(".text-div-min").css("left",(parseInt(windowWidth)-popudTopWidth+2)/2);
	$(".text-div-350").css("left",(parseInt(windowWidth)-popudTopWidth+2)/2);
	$(".text-div-320").css("left",(parseInt(windowWidth)-popudTopWidth+2)/2);
	$(".popup-top-close").css("right",(parseInt(windowWidth)-334)/2);
	
	$.ajax({
		url:"getHostUrl.action?time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			adminUrl = res.con_adminurl;
			webUrl = res.con_weburl;
			
			var sessionStorage = window.sessionStorage;
			sessionStorage.setItem("adminUrl",adminUrl);
			sessionStorage.setItem("webUrl",webUrl);
		}
	})
	
	if(window.sessionStorage)
	{
		
	}
	
	var width = $(window).width();
	var height = $(document).height();
	var left = (width-500)/2;
	$(".qq_popup").css("left",left+"px");

	$(".qq_close").bind("click",function()
	{
		$(".qq_popup").hide();
	})
})
/**
 * 关闭遮罩和弹窗
 * @param divId
 */
function closeDiv(divId)
{
	$(document.body).css("overflow-y","auto");
	
	$("#big_well").slideUp(100);
	$(".util_main").slideUp(100);
	
	if("util_register"==divId||"util_login"==divId||"util_setting_pay_pwd_success"==divId||"util_setting_pay_pwd"==divId)
	{
		location.reload();
	}
}

/**
 * 新窗口打开链接
 */
function openOtherUrl(url)
{
	window.open(url);
}
/**
 * 显示遮罩和谭庄
 * @param divId
 */
function showDiv(divId)
{
	$(document.body).css("overflow-y","hide");
	
	if("util_register"==divId||"util_setting_pwd"==divId)
	{
		$("#util_register_code").attr("src","");
		$("#util_setting_pwd_code").attr("src","");
		
		$("#"+divId+"_code").attr("src","/validimg.html?time="+jsStrToTime());
	}
	
	$("#big_well").hide();
	$(".util_main").hide();
	$("#big_well").slideDown(500);
	$("#"+divId).slideDown(500);
}
/**
 * 
 * @param title 提示title
 * @param msg 提示信息
 * @param leftClick 左边按钮click事件
 * @param rightClick 右边按钮click事件
 */
function showDoubleAlert(title,msg,leftClick,rightClick)
{
	if(null==title||""==title)
	{
		title = "";
	}
	if(null==msg||""==msg)
	{
		msg = "";
	}
	
	$("#double_alert_title").html(title);
	$("#double_alert_msg").html(msg);
	
	$("#doubleLeftBtn").attr("onclick",leftClick);
	$("#doubleRightBtn").attr("onclick",rightClick);
	
	showDiv("double_alert_div");
}
/**
 * 登陆
 */
function doLogin()
{
	var mobilePhone = $("#loginMobilePhone").val(),
		pwd = $("#loginPwd").val();
	
	var params = "mobilePhone="+mobilePhone+"&pwd="+pwd;
	
	if(false==validateTel(mobilePhone))
	{
		$("#loginErrorMsg").html(errorMsg);
	}
	else if(""===pwd||null===pwd||pwd.length<8)
	{
		$("#loginErrorMsg").html("密码至少为8位数字与字母组合");
	}
	else if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd))
	{
		$("#loginErrorMsg").html("密码至少为8位数字与字母组合");
	}
	else
	{
		$.ajax({
			url:"/nb/doLogin.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					closeDiv('util_login');
					location.reload();
				}
				else
				{
					$("#loginErrorMsg").html(res.errorMsg);
				}
			}
		})
	}
}

/**
 * 产品登陆
 */
function doProductLogin(url)
{
	var mobilePhone = $("#loginMobilePhone").val(),
		pwd = $("#loginPwd").val();
	
	var params = "mobilePhone="+mobilePhone+"&pwd="+pwd;
	
	if(false==validateTel(mobilePhone))
	{
		$("#loginErrorMsg").html(errorMsg);
	}
	else if(""===pwd||null===pwd||pwd.length<8)
	{
		$("#loginErrorMsg").html("密码至少为8位数字与字母组合");
	}
	else if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd))
	{
		$("#loginErrorMsg").html("密码至少为8位数字与字母组合");
	}
	else
	{
		$.ajax({
			url:"/nb/doLogin.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					location.href = url;
				}
				else
				{
					$("#loginErrorMsg").html(res.errorMsg);
				}
			}
		})
	}
}
/**
 * 修改密码
 */
function changePwd()
{
	var reg=new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
	
	var pwd = $("#changePwd").val(),
		rePwd = $("#reChangePwd").val();
	
	var params = "pwd="+pwd+"&rePwd="+rePwd;
	
	if(""===pwd||null===pwd||pwd.length<8)
	{
		$("#changePwdErrorMsg").html("密码至少为8位数字与字母组合!");
	}
	else if(""===rePwd||null===rePwd||rePwd.length<8)
	{
		$("#changePwdErrorMsg").html("确认密码至少为8位数字与字母组合!");
	}
	else if(pwd!=rePwd)
	{
		$("#changePwdErrorMsg").html("新密码与确认密码不一致!");
	}
	else if(!reg.test(pwd))
	{
		$("#changePwdErrorMsg").html("密码至少为8位数字与字母组合");
	}
	else if(!reg.test(rePwd))
	{
		$("#changePwdErrorMsg").html("确认密码至少为8位数字与字母组合");
	}
	else
	{
		$.ajax({
			url:"/nb/changePwd.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					$("#util_setteing_pwd_success_tel").html(res.errorMsg);
					showDiv("util_setting_pwd_success");
				}
				else
				{
					$("#changePwdErrorMsg").html(res.errorMsg);
				}
			}
		})
	}
}
/**
 * 判断注册手机是否已经存在
 */
function checkRegisterMobile()
{
	var mobilePhone = $("#registerMobilePhone").val();
	
	if(validateTel(mobilePhone))
	{
		$.ajax({
			url:"/nb/pc/userExists.action?time="+jsStrToTime(),
			type:"GET",
			dataType:"json",
			data:"mobilePhone="+mobilePhone,
			success:function(res)
			{
				if("success"!=res.result)
				{
					$("#registerErrorMsg").html("手机号码已存在!");
				}
				else
				{
					$("#registerErrorMsg").html("");
				}
			}
		})
	}
}
/**
 * 执行注册
 */
function doRegister()
{
	var mobilePhone = $("#registerMobilePhone").val(),
		pwd = $("#registerPwd").val(),
		rePwd = $("#registerRePwd").val(),
		inviteCode = $("#registerInviteCode").val(),
		validCode = $("#registerCode").val(),
		validMobileCode = $("#registerMobileCode").val();
	
	var params = "mobilePhone="+mobilePhone+"&pwd="+pwd+"&inviteCode="+inviteCode+"&validCode="+validCode+"&validMobileCode="+validMobileCode;
	
	if(false==validateTel(mobilePhone))
	{
		$("#registerErrorMsg").html("手机号码不正确!");
	}
	else if(""===pwd||null===pwd||pwd.length<8)
	{
		$("#registerErrorMsg").html("密码至少为8位数字与字母组合");
	}
	else if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd))
	{
		$("#registerErrorMsg").html("密码至少为8位数字与字母组合");
	}
	else if(pwd!==rePwd)
	{
		$("#registerErrorMsg").html("两次密码不一致!");
	}
	else if(!$("#registerCheck").prop("checked"))
	{
		$("#registerErrorMsg").html("请先同意服务条款!");
	}
	else
	{
		$.ajax({
			url:"/nb/doRegister.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					showDiv('util_register_success');
				}
				else
				{
					$("#registerErrorMsg").html(res.errorMsg);
				}
			}
		})
	}
}
/**
 * 获取注册验证码
 */
function getRegisterMobileCode()
{
	var mobilePhone = $("#registerMobilePhone").val();
	
	if(validateTel(mobilePhone))
	{
		$.ajax({
			url:"/nb/registerCode.action",
			type:"POST",
			data:"mobilePhone="+mobilePhone,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					$("#registerErrorMsg").html("");
					changeGetCodeBtn('registerMobileCodeBtn','getRegisterMobileCode()');
				}
			}
		})
	}
	else
	{
		$("#registerErrorMsg").html(errorMsg);
	}
}
/**
 * 忘记交易密码
 */
function forgetPayPwdPwd()
{
	var mobilePhone = $("#forgetPayPwdMobilePhone").val(),
		mobileCode = $("#forgetPaycode").val();
	
	var params = "mobilePhone="+mobilePhone+"&mobileCode="+mobileCode;
	
	$.ajax({
		url:"/nb/checkPayPwdForgetMsg.action",
		type:"POST",
		dataType:"json",
		data:params,
		success:function(res)
		{
			if("success"==res.result)
			{
				$("#forgetPay").attr("href","javascript:forgetPayPwd();");
				showDiv("util_setting_pay_pwd");
			}
			else
			{
				$("#forgetPayPwdErrorMsg").html(res.errorMsg);
			}
		}
	})
}
/**
 * 忘记密码
 */
function forgetPwd()
{
	var mobilePhone = $("#forgetMobilePhone").val(),
		code = $("#forgetYzm").val(),
		mobileCode = $("#forgetTelCode").val();
	
	var params = "mobilePhone="+mobilePhone+"&code="+code+"&mobileCode="+mobileCode;
	
	$.ajax({
		url:"/nb/checkForgetMsg.action",
		type:"POST",
		dataType:"json",
		data:params,
		success:function(res)
		{
			if("success"==res.result)
			{
				showDiv("util_change_pwd");
			}
			else
			{
				$("#forgetErrorMsg").html(res.errorMsg);
			}
		}
	})
}
/**
 * 获取忘记密码验证码
 */
function getForgetMobileCode()
{
	var mobilePhone = $("#forgetMobilePhone").val();
	
	if(validateTel(mobilePhone))
	{
		$.ajax({
			url:"/nb/forgetPwdCode.action",
			type:"POST",
			data:"mobilePhone="+mobilePhone,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					$("#forgetErrorMsg").html("");
					changeGetCodeBtn('forgetMobileCodeBtn','getForgetMobileCode()');
				}
			}
		})
	}
	else
	{
		$("#forgetErrorMsg").html(errorMsg);
	}
}
/**
 * 获取忘记交易密码验证码
 */
function getForgetPayPwdMobileCode()
{
	var mobilePhone = $("#forgetPayPwdMobilePhone").val();
	
	if(validateTel(mobilePhone))
	{
		$.ajax({
			url:"/nb/forgetPwdCode.action",
			type:"POST",
			data:"mobilePhone="+mobilePhone,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					$("#forgetPayPwdErrorMsg").html("");
					changeGetCodeBtn('forgetPayPwdMobileCodeBtn','getForgetPayPwdMobileCode()');
				}
			}
		})
	}
	else
	{
		$("#forgetPayPwdErrorMsg").html(errorMsg);
	}
}

/**
 * 忘记交易密码
 */
function forgetPayPwd()
{
	var pwd = $("#settingPayPwd").val(),
		rePwd = $("#settingRePayPwd").val();
	
	var params = "payPwd="+pwd+"&rePayPwd="+rePwd;

	if(false===validateDigital(pwd))
	{
		$("#settingPayPwdErrorMsg").html("交易密码必须为6位数字!");
	}
	else if(false===validateDigital(rePwd))
	{
		$("#settingPayPwdErrorMsg").html("交易密码必须为6位数字!");
	}
	else
	{
		$.ajax({
			url:"/nb/settingPayPwd.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					showAlertDiv(true,"交易密码设置成功!","","")
				}
				else
				{
					$("#settingPayPwdErrorMsg").html(res.errorMsg);
				}
			}
		})
	}
}
/**
 * 设置交易密码
 */
function settingPayPwd()
{
	var pwd = $("#settingPayPwd").val(),
		rePwd = $("#settingRePayPwd").val();
	
	var params = "payPwd="+pwd+"&rePayPwd="+rePwd;

	if(false===validateDigital(pwd))
	{
		$("#settingPayPwdErrorMsg").html("交易密码必须为6位数字!");
	}
	else if(false===validateDigital(rePwd))
	{
		$("#settingPayPwdErrorMsg").html("交易密码必须为6位数字!");
	}
	else
	{
		$.ajax({
			url:"/nb/settingPayPwd.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					showDiv("util_setting_pay_pwd_success");
				}
				else
				{
					$("#settingPayPwdErrorMsg").html(res.errorMsg);
				}
			}
		})
	}
}
/**
 * 更换注册验证码
 */
function changeYzm(codeId)
{
	console.log(jsStrToTime())
	$("#"+codeId).attr("src","/validimg.html?time="+jsStrToTime());
}
/**
 * 更改获取邀请码按钮
 * @param btnId
 */
function changeGetCodeBtn(btnId,functionName)
{
	var time=60;
	
	var timeFun=setInterval(function()
	{
		time--;
		
		if(time>0)
		{
			$("#"+btnId).html(time+"秒后重发").attr("onclick","");
		}
		else
		{
			time=60;
				
			$("#"+btnId).html("获取验证码").attr("onclick",functionName);
				
			clearInterval(timeFun);
		}
	},1000)
}
/**
 * 手机号码校验
 * @param mobile
 * @returns {Boolean}
 */
function validateTel(mobile)
{
    if(mobile.length==0)
    {
       errorMsg = "请输入手机号码!";
       return false;
    }    
    if(mobile.length!=11)
    {
    	errorMsg = "请输入有效的手机号码";
        return false;
    }
    if(!myreg.test(mobile))
    {
    	errorMsg = "请输入有效的手机号码";
        return false;
    }
    if(!myreg.test(mobile))
    {
    	errorMsg = "请输入有效的手机号码";
        return false;
    }
    return true;
}
/**
 * 校验数字
 */
function validateDigital(data)
{
	var thisReg = /^[0-9]*$/;
	
	if(!thisReg.test(data))
	{
		errorMsg = "交易密码必须为数字!";
		return false;
	}
	return true;
}
/**
 * 金额格式化（保留小数点后两位）
 * @param money
 * @returns
 */
function format_(money){
	if(money == 0)
	{
		return money;
	}
	else if(money == 'undefined')
	{
		return 0;
	}
	else
	{
		n = 2; 
		money = parseFloat((money + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
		var l = money.split(".")[0].split("").reverse(), r = money.split(".")[1]; 
		t = ""; 
		for (i = 0; i < l.length; i++) { 
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
		} 
		return  t.split("").reverse().join("")+ "." + r;
	}
}
/**
 * 日期时间各式话（2015.08.27 12:00）
 * @param value
 * @returns {String}
 */
function noticeDateFormat_(value)
{
	function formatDate(now) 
	{ 
		var year=now.getFullYear();
		var month=now.getMonth()+1; 
		var date=now.getDate(); 
		var hour=now.getHours(); 
		var minute=now.getMinutes(); 
		var second=now.getSeconds(); 
		if(month < 10)
		{
			month = '0' + month;
		}
		if(date < 10)
		{
			date = '0' + date;
		}
		if(hour < 10)
		{
			hour = '0' + hour;
		}
		if(minute < 10)
		{
			minute = '0' + minute;
		}
		return year+"."+month+"."+date+" "+hour+":"+minute; 
	} 
	if(value==null ||value=='')
	{
		return '';
	}
	var d = new Date(value);
	return formatDate(d);
}
/**
 * 判断json中是否有登陆
 * @param userStatus
 * @returns {Boolean}
 */
function checkUser(userStatus)
{
	if("no_login"===userStatus)
	{
		return false;
	}
	else
	{
		return true;
	}
}
/**
 * 更改导航颜色
 * @param headerName
 */
function changeHeader(headerName)
{
	$("#util_ul li").each(function()
	{
		$(this).removeClass("active");
		
		var textValue = $(this).children("a").text();
		
		if(textValue==headerName)
		{
			$(this).addClass("active");
		}
	})
}
/**
 * 修改账户中心左边导航选中状态
 * @param leftMenueName
 * @param itemName
 */
function changeLeftMenue(leftMenueName,itemName)
{
	$("#collapseOne").removeClass("in");
	
	$("#leftMenue .user_menue_li").each(function()
	{
		$(this).removeClass("user_menue_li_actived");
		
		var textValue = $(this).find(".user_menue_li_text").children("a").text();

		if(textValue==leftMenueName)
		{
			$(this).addClass("user_menue_li_actived");
			
			if(null!=itemName&&""!=itemName)
			{
				$("#collapseOne").addClass("in");
				$("#collapseOne div").each(function()
				{
					$(this).addClass("gray");
					$(this).removeClass("left-menu-inner-back");
							
					var textValue = $(this).children("a").text();
							
					if(textValue==itemName)
					{
						$(this).removeClass("gray");
						$(this).addClass("left-menu-inner-back");
					}
				})
			}
		}
	})
}
/**
 * 转时间戳
 * @param str_time
 * @returns {Number}
 */
function jsStrToTime()  
{  
	var str_time = Math.random();
    return str_time;
}  
/**
 * 错误或成功弹窗
 * @param alertMsg
 * @param url
 */
function showAlertDiv(status,alertTitle,alertMsg,url)
{
	if(!status)
	{
		if(""!==url&&null!==url)
		{
			$("#urlId").attr("onclick","openUrl('"+url+"')");
		}
		else
		{
			$("#urlId").attr("onclick","closeDiv()");
		}
		if(""!==alertTitle&&null!==alertTitle)
		{
			$("#alert_title").text(alertTitle);
		}
		else
		{
			$("#alert_title").text("很遗憾出了一点小问题!");
		}
		$("#alert_msg").text(alertMsg);
		
		showDiv('alert_div');
	}
	else
	{
		if(""!==url&&null!==url)
		{
			$("#successUrlId").attr("onclick","openUrl('"+url+"')");
		}
		else
		{
			$("#successUrlId").attr("onclick","closeDiv()");
		}
		if(""!==alertTitle&&null!==alertTitle)
		{
			$("#success_alert_title").text(alertTitle);
		}
		else
		{
			$("#success_alert_title").text("很遗憾出了一点小问题!");
		}
		$("#success_alert_msg").text(alertMsg);
		
		showDiv('success_alert_div');
	}
}
/**
 * 验证身份证号
 * @param num
 * @returns {Boolean}
 */
function isIdCardNo(num) 
{
    var factorArr = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
    var parityBit = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
    var varArray = new Array();
    var intValue;
    var lngProduct = 0;
    var intCheckDigit;
    var intStrLen = num.length;
    var idNumber = num;
  
    if ((intStrLen != 15) && (intStrLen != 18)) 
    {
        return false;
    }
    for (i = 0; i < intStrLen; i++) 
    {
        varArray[i] = idNumber.charAt(i);
        if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) 
        {
            return false;
        } 
        else if (i < 17) 
        {
            varArray[i] = varArray[i] * factorArr[i];
        }
    }
    if (intStrLen == 18) 
    {
       
        var date8 = idNumber.substring(6, 14);
        if (isDate8(date8) == false) 
        {
            return false;
        }
        for (i = 0; i < 17; i++) 
        {
            lngProduct = lngProduct + varArray[i];
        }
        intCheckDigit = parityBit[lngProduct % 11];
        if (varArray[17] != intCheckDigit) 
        {
            return false;
        }
    }
    else 
    {        
        var date6 = idNumber.substring(6, 12);
        if (isDate6(date6) == false) 
        {
            return false;
        }
    }
    return true;
}

function isDate6(sDate) 
{
    if (!/^[0-9]{6}$/.test(sDate)) 
    {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    if (year < 1700 || year > 2500) return false
    if (month < 1 || month > 12) return false
    return true;
}

function isDate8(sDate) 
{
    if (!/^[0-9]{8}$/.test(sDate)) 
    {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    day = sDate.substring(6, 8);
    var iaMonthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    if (year < 1700 || year > 2500) return false
    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1] = 29;
    if (month < 1 || month > 12) return false
    if (day < 1 || day > iaMonthDays[month - 1]) return false
    return true;
}
/**
 * 跳转页面
 * @param url
 */
function openUrl(url)
{
	location.href = url;
}
/**
 * 尾部QQ,电话
 * @param id
 */
function changeFooter(id)
{
	$(".footer-2").hide();
	$(".footer-div-2").hide();
	$(".footer-0").hide();
	$(".footer-div-0").hide();
	$(".footer-"+id).show();
	$(".footer-div-"+id).show();
}

function changeFooterHide(id)
{

		$(".footer-0").hide();
		$(".footer-div-2").hide();
		$(".footer-0").hide();
		$(".footer-div-0").hide();
	
}
/**
 * qq弹窗
 */
function showQQ()
{
	$(".qq_popup").show();
}
/**
 * 时间戳转换标准时间
 * @param unixtime
 * @returns
 */
function jsDateTime(unixtime)  
{  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd hh:mm:ss"); 
}  
/**
 * 时间戳转换标准时间
 * @param unixtime
 * @returns
 */
function jsDateTimeOnly(unixtime)  
{  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd"); 
} 
Date.prototype.format = function(format)
{
	var o = 
	{
	"M+" : this.getMonth()+1, 
	"d+" : this.getDate(), 
	"h+" : this.getHours(),
	"m+" : this.getMinutes(), 
	"s+" : this.getSeconds(), 
	"q+" : Math.floor((this.getMonth()+3)/3), 
	"S" : this.getMilliseconds() 
	}

	if(/(y+)/.test(format)) 
	{
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o) 
	{
		if(new RegExp("("+ k +")").test(format))
		{
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
} 
/**
 * 显示协议
 * @param id
 */
function showAgreeMent(id)
{
	$("#"+id).show();
}
/**
 * 隐藏协议
 * @param id
 */
function hideAgreeMent(id)
{
	$("#"+id).hide();
}
/**
 * 账户中心左边导航上部
 */
function Get_Greetings() 
{  
    var now = new Date();  
    var times = now.getHours();  
    var whe=parseInt(times);  
    var str ="";
    if(times>=0 && times<=3){str ="嘘～大家都睡着啦!"}
    if(times>3 && times<=6){str = "数钱数到手抽筋,今夜做梦也会笑!"}  
    if(times>6 && times<=8){str = "起床起床！太阳晒屁股啦～"}  
    if(times>8 && times<=11){str = "红日初升,其道大光!"}  
    if(times>11 && times<=13){str = "尝一顿美味的午餐,放松一下吧～"}  
    if(times>13 && times<=19){str = "锄禾日当午,汗滴禾下土."}  
    if(times>19 && times<=22){str = "掐指一算,夜观天象～"}  
    if(times>22 && times<=24){str = "夜深了,早点休息吧. 晚安～"}  
    $("#welcome").html(str);
}  

/**
 * 登出
 */
function loginOut(){
	$.ajax({
		url:"/nb/loginOut.action",
		type:"POST",
		dataType:"json",
		ansyc:false,
		success:function(res){
			if("success"==res.result){
				location.href = "/index.html";
			}
		}
	})
}

/**
 *判断浏览器是否是移动端
 */
function  userAgent(){
	var browser = {
			versions:function(){ 
			var u = navigator.userAgent, app = navigator.appVersion; 
			return {//移动终端浏览器版本信息 
				trident: u.indexOf("Trident") > -1, //IE内核
				presto: u.indexOf("Presto") > -1, //opera内核
				webKit: u.indexOf("AppleWebKit") > -1, //苹果、谷歌内核
				gecko: u.indexOf("Gecko") > -1 && u.indexOf("KHTML") == -1, //火狐内核
				mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
				ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
				android: u.indexOf("Android") > -1 || u.indexOf("Linux") > -1, //android终端或者uc浏览器
				iPhone: u.indexOf("iPhone") > -1 , //是否为iPhone或者QQHD浏览器
				iPad: u.indexOf("iPad") > -1, //是否iPad
				webApp: u.indexOf("Safari") == -1 //是否web应该程序，没有头部与底部
				};
			}(),
			language:(navigator.browserLanguage || navigator.language).toLowerCase()
		} 
	
	if(browser.versions.mobile||browser.versions.ios||browser.versions.android||browser.versions.iPhone||browser.versions.iPad){
		location.href="/nb/wechat/account/800bank.action";
	}
	
}


/**
 * 
 * @param leftClick 左边按钮click事件
 * @param rightClick 右边按钮click事件
 * @param redeemMoney 本金
 */
function showRedeem_(leftClick,rightClick,redeemMoney,ppfundId)
{
	$("#doubleLeftBtn_").attr("onclick",leftClick);
	$("#doubleRightBtn_").attr("onclick",rightClick);
	$("#redeemMoney").val(redeemMoney);
	$("#ppfundId").val(ppfundId)
	showDiv("redeem_");
}
