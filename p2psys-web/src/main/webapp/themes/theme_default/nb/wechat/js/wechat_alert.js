/***
 * 微信端提示框
 * @param errorMsg 提示信息
 * @param errorType 信息类型 0错误，1正确
 * @param errorUrl 提示框关闭后跳转的路径，传空或不传表示只关闭该窗口
 */
function wechatAlert(errorMsg,errorType,errorUrl)
{
	 if($("#success_div"))
	 {
		$("#success_div").remove();
	 }
     var htmlStr = "";
	 if(null!=errorUrl&&""!=errorMsg&&undefined!=errorMsg)
	 {
		 htmlStr += "<div class='success-alert'  id='success_div' onclick=showError('"+errorUrl+"') >";
	 }
     if(null==errorUrl || ""===errorMsg || undefined==errorMsg)
	 {
		 htmlStr += "<div class='success-alert'  id='success_div' onclick=hideError() >";
	 }
     htmlStr += "<div class='space-div' ></div>";
     /**
      * 错误
      */
     if('0'===errorType)
	 {
    	 htmlStr += "<img src='/themes/theme_default/nb/wechat/images/close.png' width='45px'  >";
	 }
     /**
      * 正确
      */
     if('1'===errorType)
     {
    	 htmlStr += "<img src='/themes/theme_default/nb/wechat/images/open.png' width='45px'  >";
     }
	 htmlStr += "<div class='space-div' ></div>"; 
	 htmlStr += "<p class='error-p' id='errorMsg' >"+errorMsg+"</p>";
	 htmlStr += "	<div class='space-div' ></div>";
	 htmlStr += "<div class='space-div' ></div><div class='sureBtn'  >确定</div>";
	 htmlStr += "<div class='space-div' ></div></div>";
	 htmlStr += "<div class='well big-well' id='mask_div'  ></div>";
	   	
	$(document.body).append(htmlStr);
	   
	$("#success_div").show();
	$("#mask_div").show();
}

/***
 * 隐藏提示框
 */
function hideError()
{
	$("#success_div").hide();
	$("#mask_div").hide();
}

/***
 * 页面跳转至该url
 * @param url 跳转路径
 */
function showError(url)
{
	location.href = url;
}