$(function(){
	showLi('user_li');
	$.ajax({
		url:"/cf/user/myLeaderData.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			var htmlStr = "";
			if(null!=res.errorMsg&&res.errorMsg.length>0){
				for(var i = 0;i<res.errorMsg.length;i++){
					htmlStr += "<tr>";
					htmlStr += "<td>"+res.errorMsg[i].projectName+"</td>";
					htmlStr += "<td>"+getType(res.errorMsg[i].type)+"</td>";
					htmlStr += "</tr>";
				}
			}
			$("#dataDiv").html(htmlStr);
		}
	})
})

/**
 * 产品类别
 * @param type
 * @returns {String}
 */
function getType(type){
	if("1"==type){
		return "实物众筹";
	}else if("2"==type){
		return "股权众筹";
	}else  if("4"==type){
		return "公益众筹";
	}else{
		return "其他众筹";
	}
}
