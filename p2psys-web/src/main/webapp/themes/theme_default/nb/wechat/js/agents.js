var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");    
var flag = true; 

function isPC()
{    
	var userAgentInfo = navigator.userAgent; 
	
    for (var v = 0; v < Agents.length; v++) 
    {    
        if (userAgentInfo.indexOf(Agents[v]) > 0) 
        { 
        	flag = false; 
        	break; 
        }    
    }    
    return flag;    
}  
function isWechat()
{
	var ua = navigator.userAgent.toLowerCase();
	
	if(ua.match(/MicroMessenger/i)=="micromessenger") 
	{
		return true;
 	} 
	else 
	{
		return false;
	}
}