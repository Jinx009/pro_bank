$(function(){
	$.ajax({
		url:"/cf/pro/myAttentionData.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			var htmlStr = "";
			if(null!=res.errorMsg){
				for(var i = 0;i<res.errorMsg.length;i++){
					htmlStr += "<tr>";
					htmlStr += "<td onclick=openUrl('/pro/detail.html?id="+res.errorMsg[i].projectId+"') class='cursor' >"+res.errorMsg[i].projectName+"</td>";
					htmlStr += "<td>"+getTimeStatus(res.errorMsg[i].timeStatus)+"</td>";
					htmlStr += "<td>"+getType(res.errorMsg[i].type)+"</td>";
				}
			}
			$("#dataTable").html(htmlStr);
		}
	})
})

/**
 * 获取项目状态
 * @param status
 * @returns
 */
function getTimeStatus(status){
	var step = "紧张众筹中";
	if("1"==status){
		 step = "奋力预热中";
	}else if("2"==status){
		 step = "紧张众筹中";
	}else if("3"==status){
		 step = "遗憾过期中";
	}else{
		 step = "顺利已完成";
	}
	return step;
}

/**
 * 众筹类型
 * @param type
 * @returns
 */
function getType(type){
	if(1==type){
		return "实物众筹";
	}else if(2==type){
		return "股权众筹";
	}else if(4==type){
		return "公益众筹";
	}
}

/**
 * 跳转页面  
 * @param url
 */
function openUrl(url){
	location.href = url;
}