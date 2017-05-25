
function ajaxFileUpload(sId, _url) {
	$.ajaxFileUpload({
		url: _url,
		secureuri: false,
		fileElementId: sId,
		dataType: 'json',
		data:{typeCode:'aaa'},
		success: function(data) {
			if(data.state=="SUCCESS"){
				var sHtml = "";
				sHtml += '<li data-mum="' + data.tmpId + '">';
				sHtml += '<a href="' + data.url + '" class="fileshow"><img src="' + data.url + '" alt="" style="width:100px;height:100px;float:left;"></a>';
				sHtml += '<input name="file" value="'+data.url+'" type="hidden">';
				sHtml += '<a class="closePic" style="float:left;" href="javascript:;" title="删除图片">x</a>';
				sHtml += '</li>';
				$("#"+sId).parents("ul").append(sHtml);
			}else{
				alert("上传文件失败，请重试!");
			}
		},
		error: function(data,status, e) {
			alert("上传文件失败，请重试!");
		}
	});
}

//type 1:实物照,2:档案照,3:担保函,4:担保函
function ajaxFileUpload1(sId, _url,name,type) {
	$.ajaxFileUpload({
		url: _url,
		secureuri: false,
		fileElementId: sId,
		dataType: 'json',
		data:{typeCode:'aaa'},
		success: function(data) {
			if(data.state=="SUCCESS"){
				var sHtml = "";
				sHtml += '<li data-mum="' + data.tmpId + '">';
				sHtml += '<img src="' + data.url + '" alt="" style="width:100px;height:100px;float:left;">';
				sHtml += '<input name="'+name+'" value="'+data.url+'" type="hidden">';
				sHtml += '<a class="closePic" style="float:left;" href="javascript:;" title="删除图片">x</a>';
				sHtml += '</li>';
				$("#"+sId).parents("ul").append(sHtml);
				
			}else{
				alert("上传文件失败，请重试!");
			}
		},
		error: function(data,status, e) {
			alert("上传文件失败，请重试!");
		}
	});
}
//上传档案照
function ajaxFileUpload2(sId, _url,name) {
	$.ajaxFileUpload({
		url: _url,
		secureuri: false,
		fileElementId: sId,
		dataType: 'json',
		data:{typeCode:'aaa'},
		success: function(data) {
			if(data.state=="SUCCESS"){
				var sHtml = "";
				sHtml += '<li data-mum="' + data.tmpId + '">';
				sHtml += '<img src="' + data.url + '" alt="" style="width:100px;height:100px;float:left;">';
				sHtml += '<input name="'+name+'" value="'+data.url+'" type="hidden">';
				sHtml += '<a class="closePic" style="float:left;" href="javascript:;" title="删除图片">x</a>';
				sHtml += '</li>';
				$("#"+sId).parents("ul").append(sHtml);
				var physicalCount = $("#physicalCount").val();
				var archivesCount = $("#archivesCount").val();
				$("#physicalCount").val(physicalCount+1);
				$("#archivesCount").val(archivesCount+1);
			}else{
				alert("上传文件失败，请重试!");
			}
		},
		error: function(data,status, e) {
			alert("上传文件失败，请重试!");
		}
	});
}

