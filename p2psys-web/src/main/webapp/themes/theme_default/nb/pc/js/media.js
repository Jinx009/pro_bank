$(function()
{
	var id= $("#id").val();
	
	$.ajax({
		url:"/article/list.html?nid=news&time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			for(var i = 0;i<res.data.list.length;i++)
			{
				if(id==res.data.list[i].id)
				{
					$("#title").html(res.data.list[i].title);
					$("#info").html(res.data.list[i].content);
				}
			}
		}
	})
})