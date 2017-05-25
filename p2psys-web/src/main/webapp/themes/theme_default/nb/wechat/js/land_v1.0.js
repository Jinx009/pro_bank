var error_msg = ""; 
var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
/**
 * 登陆方法
 */
function executeLogin()
{
  var tel = $("#tel").val();
  var pwd = $("#pwd").val();
  var params = "tel="+tel+"&pwd="+pwd;
  
  if(validateTel(tel))
  {
    console.log(validateTel(tel))
    
    $.ajax({
      url:"/nb/wechat/executeLogin.html",
      data:params,
      type:"POST",
      dataType:"json",
      success:function(res)
      {
        var redirectURL = $("#redirectURL").val();
        console.log(res)
        if("success"===res.result)
        {
          if(""!==redirectURL)
          {
            location.href = redirectURL;
          }
          else
          {
            location.href = "/nb/wechat/account/main.html";
          }
        }
        else
        {
          $("#error_msg").html(res.errorMsg);
        }
      }
    })
  }
  else
  {
    $("#error_msg").html(error_msg);
  }
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
       error_msg = "请输入手机号码!";
       
       return false;
    }    
    if(mobile.length!=11)
    {
      error_msg = "请输入有效的手机号码";
      
        return false;
    }
    
    if(!myreg.test(mobile))
    {
      error_msg = "请输入有效的手机号码";
      
        return false;
    }
    
    return true;
}
/**
 * 跳转注册
 */
function goToRegister()
{
  location.href = "/nb/wechat/register.html?redirectURL="+$("#redirectURL").val();
}
/**
 * 忘记密码
 */
function forgetPwd()
{
  location.href = "/nb/wechat/forgetPwd.html?redirectURL="+$("#redirectURL").val();
}