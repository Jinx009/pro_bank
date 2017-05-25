//—S—定义全局变量
//datagrid全局变量
var pageSize = 20;
var pageList = [20, 30, 40, 50];
//—E—定义全局变量
//菜单树-表单增、删、查、改处理方法
$.fn.treeGridOptions = function() {}

//菜单树选中节点id返回
$.fn.treeGridOptions.doNode = function() {
	var c = "";
	var p = "";
	$(".tree-checkbox1").parent().children('.tree-title').each(function() {
		c += $(this).parent().attr('node-id') + ",";
	});
	$(".tree-checkbox2").parent().children('.tree-title').each(function() {
		p += $(this).parent().attr('node-id') + ",";
	});
	var str = (c + p);
	str = str.substring(0, str.length - 1);
	return (str);
}

//菜单树节点选中
$.fn.treeGridOptions.nodeChk = function(treeObj, id) {
	var node = treeObj.tree('find', id);
	if (node) {
		var isLeaf = treeObj.tree('isLeaf', node.target);
		if (isLeaf) treeObj.tree('check', node.target);
	}
}

//初始化左侧菜单
function InitLeftMenu(paramId) {
	var paramId = paramId || 0;
	$("#index_layout").layout('remove', 'west').layout('add', {
		region: 'west',
		width: 200,
		title: '',
		content: '<div id="nav"></div>'
	});
	var $nav = $("#nav");
	var _menus = (function() {
		var result;
		$.ajax({
			type: 'post',
			url: "/modules/system/menu/getMenuList.html?parentId=" + paramId,
			dataType: 'json',
			async: false,
			success: function(data) {
				result = data.rows;
			}
		});
		return result;
	})();
	$nav.accordion({
		fit: false,
		border: false
	});
	var selectedPanelname = '';
	$.each(_menus, function(i, n) {
		$nav.accordion('add', {
			title: n.name,
			content: '<ul class="navlist" data-id="' + n.id + '"></ul>',
			fit: false,
			border: false,
			iconCls: 'icon ' + n.iconCls
		});
		if (i != -1) selectedPanelname = n.name;
	});
	$nav.accordion('select', selectedPanelname);
	$(".navlist").each(function() {
		var _this = $(this);
		$.ajax({
			type: 'post',
			url: "/modules/system/menu/getMenuList.html?parentId=" + _this.attr("data-id"),
			dataType: 'json',
			async: false,
			success: function(data) {
				var menulist = '';
				$.each(data.rows, function(i, n) {
					menulist += '<li><div><a ref="' + n.id + '" href="#" rel="' + n.href + '" ><span class="nav">' + n.name + '</span></a></div></li>'
				})
				_this.html(menulist);
			}
		});
	})
	$("#nav .panel-title").eq(0).trigger("click");
}

//打开具体菜单链接
$('.navlist li a').live("click", function() {
	var tabTitle = $(this).children('.nav').text();
	var url = $(this).attr("rel");
	var icon = $(this).find('.icon').attr('class');
	if ($(this).parent().siblings().html()) {
		$('.third_ul').slideUp();
		var ul = $(this).parent().next();
		if (ul.is(":hidden")) {
			ul.slideDown();
		} else {
			ul.slideUp();
		}
	} else {
		addTab(tabTitle, url, icon);
		$('.navlist li div').removeClass("selected");
		$(this).parent().addClass("selected");
	}
})

//数据编辑
$.fn.treeGridOptions.editFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = treeGrid.treegrid('getSelections');
		id = rows[0].id;
	} else {
		treeGrid.treegrid('unselectAll').treegrid('uncheckAll');
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id
	} else {
		url = url + '&id=' + id
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
				parent.$.modalDialog.openner_treeGrid = treeGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}

//数据添加
$.fn.treeGridOptions.addFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = treeGrid.treegrid('getSelections');
		id = rows[0].id;
	} else {
		treeGrid.treegrid('unselectAll').treegrid('uncheckAll');
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url + '?id=' + id,
		buttons: [{
			text: '添加',
			handler: function() {
				//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
				parent.$.modalDialog.openner_treeGrid = treeGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}

//单条数据删除
$.fn.treeGridOptions.deleteFun = function(id, url) {
	if (id == undefined) {
		//点击右键菜单才会触发这个
		var rows = treeGrid.treegrid('getSelections');
		id = rows[0].id;
	} else {
		//点击操作里面的删除图标会触发这个
		treeGrid.treegrid('unselectAll').treegrid('uncheckAll');
	}
	parent.$.messager.confirm('询问', '您确定当前操作？', function(b) {
		if (b) {
			var currentUserId = '0'; // 当前登录用户的ID
			if (currentUserId != id) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				$.post(url, {
					id: id
				}, function(result) {
					if (result.result) {
						parent.$.messager.alert('提示', result.msg, 'info');
						treeGrid.treegrid('reload');
					}
					parent.$.messager.progress('close');
				}, 'JSON');
			} else {
				parent.$.messager.show({
					title: '提示',
					msg: '不可以删除自己！'
				});
			}
		}
	});
}

//单条数据删除
$.fn.treeGridOptions.cfAcceptFun = function(id, url) {
	if (id == undefined) {
		//点击右键菜单才会触发这个
		var rows = treeGrid.treegrid('getSelections');
		id = rows[0].id;
	} else {
		//点击操作里面的删除图标会触发这个
		treeGrid.treegrid('unselectAll').treegrid('uncheckAll');
	}
	parent.$.messager.confirm('询问', '确定审核通过？', function(b) {
		if (b) {
			var currentUserId = '0'; // 当前登录用户的ID
			if (currentUserId != id) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				$.post(url, {
					id: id
				}, function(result) {
					if (result.result) {
						parent.$.messager.alert('提示', result.msg, 'info');
						treeGrid.treegrid('reload');
					}
					parent.$.messager.progress('close');
				}, 'JSON');
			} else {
				parent.$.messager.show({
					title: '提示',
					msg: '系统异常！'
				});
			}
		}
	});
}


//批量数据删除
$.fn.treeGridOptions.batchDeleteFun = function(url) {
	var rows = treeGrid.treegrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要删除当前选中的记录？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				var currentUserId = '0'; // 当前登录用户的ID
				var flag = false;
				for (var i = 0; i < rows.length; i++) {
					if (currentUserId != rows[i].id) {
						ids.push(rows[i].id);
					} else {
						flag = true;
					}
				}
				$.getJSON(url, {
					ids: ids.join(',')
				}, function(result) {
					if (result.success) {
						treeGrid.treegrid('load');
						treeGrid.treegrid('uncheckAll').treegrid('unselectAll').treegrid('clearSelections');
					}
					if (flag) {
						parent.$.messager.show({
							title: '提示',
							msg: '不可以删除自己！'
						});
					} else {
						parent.$.messager.alert('提示', result.msg, 'info');
					}
					parent.$.messager.progress('close');
				});
			}
		});
	} else {
		parent.$.messager.show({
			title: '提示',
			msg: '请勾选要删除的记录！'
		});
	}
}

//数据表单提交
$.fn.treeGridOptions.formSubFun = function(formId, url, successMsg, failMsg) {
	var successMsg = successMsg || "操作成功!";
	var failMsg = failMsg || "操作失败!";
	$(formId).form({
		url: url,
		onSubmit: function() {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			var isValid = $(this).form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success: function(result) {
			parent.$.messager.progress('close');
			result = typeof result == 'object' ? result : $.parseJSON(result);
			if (result.result) {
				parent.$.modalDialog.openner_treeGrid.treegrid('reload');
				parent.$.modalDialog.handler.dialog('close');
				$.messager.alert('提示', successMsg, 'info');
			} else {
				$.messager.alert('提示', result.msg, 'warning');
			}
		}
	});
}

//表格-表单增、删、查、改处理方法
$.fn.dataGridOptions = function() {}

//数据编辑
$.fn.dataGridOptions.editFun = function(id, title, width, height, url, text) {
	if (text == null || text == '') {
		text = '确定';
	}
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id;
	} else {
		url = url + '&id=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: text,
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}

//数据查看
$.fn.dataGridOptions.closeFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id;
	} else {
		url = url + '&id=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '关闭',
			handler: function() {
				parent.$.modalDialog.handler.dialog('close');
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
			}
		}]
	});
}

//数据添加
$.fn.dataGridOptions.addFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id;
	} else {
		url = url + '&id=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}

//后台发标
$.fn.dataGridOptions.addBorrowFun = function(title, width, height, url) {
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '添加',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}

$.fn.dataGridOptions.addGlodRechargeFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}

	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url + '?type=' + id,
		buttons: [{
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
			}
		}]
	});
}

//授权管理
$.fn.dataGridOptions.grantFun = function(id, title, width, height, url) {
	dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url + "?id=" + id,
		buttons: [{
			text: '授权',
			handler: function() {
				//因为授权成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}

//审核确认操作
$.fn.dataGridOptions.selectFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url + '?id=' + id,
	});
}

$.fn.dataGridOptions.confirmFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url + '?id=' + id,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				f = parent.$.modalDialog.handler.find('#form');
				isValid = f.form('validate');
				if (isValid) {
					top.$.messager.confirm(title, "真的决定了吗?", function(r) {
						if (r) {
							f.submit();
						}
					});
				}
			}
		}]
	});
}

//解绑银行卡
$.fn.dataGridOptions.unbindFun = function(id, url, msg) {
	var dfMsg = "您是否要解绑该银行卡？";
	msg = msg || dfMsg;
	if (id == undefined) {
		//点击右键菜单才会触发这个
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		//点击操作里面的删除图标会触发这个
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
			var currentUserId = '0'; //当前登录用户的ID
			if (currentUserId != id) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				$.post(url, {
					id: id
				}, function(result) {
					parent.$.messager.alert('提示', result.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}, 'JSON');
			} else {}
		}
	});
}

//单条数据删除
$.fn.dataGridOptions.deleteFun = function(id, url, msg) {
	var dfMsg = "您是否要执行当前操作？";
	msg = msg || dfMsg;
	if (id == undefined) {
		//点击右键菜单才会触发这个
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		//点击操作里面的删除图标会触发这个
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
			var currentUserId = '0'; //当前登录用户的ID
			if (currentUserId != id) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				$.post(url, {
					id: id
				}, function(result) {
					parent.$.messager.alert('提示', result.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}, 'JSON');
			} else {
				parent.$.messager.show({
					title: '提示',
					msg: '不可以删除自己！'
				});
			}
		}
	});
}

//单条数据删除
$.fn.dataGridOptions.deleteMFun = function(id, url, msg, flag) {
	var dfMsg = "您是否要删除当前记录？";
	msg = msg || dfMsg;
	if (id == undefined) {
		//点击右键菜单才会触发这个
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		//点击操作里面的删除图标会触发这个
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
			var currentUserId = '0'; //当前登录用户的ID
			if (currentUserId != id) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				$.post(url, {
					id: id,
					platform: flag
				}, function(result) {
					parent.$.messager.alert('提示', result.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}, 'JSON');
			} else {
				parent.$.messager.show({
					title: '提示',
					msg: '不可以删除自己！'
				});
			}
		}
	});
}

//确认函数
$.fn.dataGridOptions.sureFun = function(id, url, msg, params) {
	var dfMsg = "您确认了吗？";
	msg = msg || dfMsg;
	//转换字符串成对象
	if (typeof(params) != "object") {
		params = eval("(" + params + ")");
	}
	if (id == undefined) {
		//点击右键菜单才会触发这个
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		//点击操作里面的删除图标会触发这个
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
			if (id) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				$.post(url, {
					id: id
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						dataGrid.datagrid('reload');
					} else {
						parent.$.messager.confirm('提示', result.msg, function(b) {
							if (b) {
								var type = result.reType;
								$.fn.dataGridOptions.addFun(id, params.title[type], params.width, params.height, params.url[type]);
							}
						})
					}
					parent.$.messager.progress('close');
				}, 'JSON');
			}
		}
	});
}

//批量数据删除
$.fn.dataGridOptions.batchDeleteFun = function(url) {
	var rows = dataGrid.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要删除当前选中的记录？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title: '提示',
					text: '数据处理中，请稍后....'
				});
				var currentUserId = '0'; //当前登录用户的ID
				var flag = false;
				for (var i = 0; i < rows.length; i++) {
					if (currentUserId != rows[i].id) {
						ids.push(rows[i].id);
					} else {
						flag = true;
					}
				}
				$.getJSON(url, {
					ids: ids.join(',')
				}, function(result) {
					if (result.success) {
						dataGrid.datagrid('load');
						dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					}
					if (flag) {
						parent.$.messager.show({
							title: '提示',
							msg: '不可以删除自己！'
						});
					} else {
						parent.$.messager.alert('提示', result.msg, 'info');
					}
					parent.$.messager.progress('close');
				});
			}
		});
	} else {
		parent.$.messager.show({
			title: '提示',
			msg: '请勾选要删除的记录！'
		});
	}
}

//数据查询
$.fn.dataGridOptions.searchFun = function(formId) {
	dataGrid.datagrid('load', $.serializeObject($(formId)));
}

//查询文件清除
$.fn.dataGridOptions.cleanFun = function(formId) {
	$(".type").each(function() {
		$(this).children().eq(0).attr("selected", "selected");
	});
	$(".status").each(function() {
		$(this).children().eq(1).attr("selected", "selected");
	});
	$(formId + ' input').val('');
	dataGrid.datagrid('load', {});
	$("#showAdw")[0].reset();
}

//数据表单提交
$.fn.dataGridOptions.formSubFun = function(formId, url, successMsg, failMsg) {
	var successMsg = successMsg || "操作成功!";
	var failMsg = failMsg || "操作失败!";
	$(formId).form({
		url: url,
		onSubmit: function() {
			if ($("#uploadPiclist3") && $("#uploadPiclist3").children().length == 1) {
				$.messager.alert('提示', "至少上传一张公司照", 'warning');
				return false;
			}
			if ($('#isDS') && $('#isDS').attr("checked") && $("#fixedTime").val() == '') {
				$.messager.alert('提示', '请输入定时时间');
				return false;
			}
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			var isValid = $(this).form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success: function(result) {
			parent.$.messager.progress('close');
			result = typeof result == 'object' ? result : $.parseJSON(result);
			if (result.result) {
				$.messager.alert('提示', successMsg, 'info');
				parent.$.modalDialog.handler.dialog('close');
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
				parent.$.modalDialog.openner_dataGrid.datagrid('clearChecked');
			} else {
				$.messager.alert('提示', result.msg, 'warning');
			}
		}
	});
}

$.fn.dataGridOptions.picFormSubFun = function(formId, url) {
	$(formId).form({
		url: url,
		onSubmit: function() {
			if ($("#uploadPiclist") && $("#uploadPiclist").children().length == 1) {
				$.messager.alert('提示', "至少上传一张实物照", 'warning');
				return false;
			}
			if ($("#uploadPiclist1") && $("#uploadPiclist1").children().length == 1) {
				$.messager.alert('提示', "至少上传一张档案照", 'warning');
				return false;
			}
		},
		success: function(result) {
			parent.$.messager.progress('close');
			result = typeof result == 'object' ? result : $.parseJSON(result);
			if (result.result) {
				$.messager.alert('提示', result.msg, 'info');
				parent.$.modalDialog.handler.dialog('close');
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
			} else {
				$.messager.alert('提示', result.msg, 'warning');
			}
		}
	});
}

$.fn.dataGridOptions.addBorrowFormSubFun = function(formId, url) {
	$(formId).form({
		url: url,
		onSubmit: function() {
			var selectType = $("#protocolType").val();
			if (selectType == 0) {
				$.messager.alert('提示', '把协议类型选一下好吗？', 'warning');
				return false;
			}
			var isValid = $(this).form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}
			return isValid;

			var guaranteeRateVal = $("#guaranteeRate").val();
			var dayManageFeeVal = $("#dayManageFee").val();
			var manageFeeVal = $("#manageFee").val();
			if ($("#vouchFirms").css('display') != "none") {
				if (guaranteeRateVal == '') {
					$.messager.alert('提示', '请输入担保年化费', 'warning');
					return false;
				} else {
					if ($("#changetoDay").hasClass("dayhover")) { //天标
						if (guaranteeRateVal < 0 || guaranteeRateVal > dayManageFeeVal) {
							$.messager.alert('提示', '输入担保年化费必须在0至' + dayManageFeeVal + '%之间', 'warning');
						}
					} else {
						if (guaranteeRateVal < 0 || guaranteeRateVal > manageFeeVal) {
							$.messager.alert('提示', '输入担保年化费必须在0至' + manageFeeVal + '%之间', 'warning');
						}
					}
					return false;
				}
			}
			if ($("#borrowPagePic") && $("#borrowPagePic").children().length == 1) {
				$.messager.alert('提示', "至少上传一张图片", 'warning');
				return false;
			}
		},
		success: function(result) {
			parent.$.messager.progress('close');
			result = typeof result == 'object' ? result : $.parseJSON(result);
			if (result.result) {
				$.messager.alert('提示', result.msg, 'info');
				parent.$.modalDialog.handler.dialog('close');
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
			} else {
				$.messager.alert('提示', result.msg, 'warning');
			}
		}
	});
}

//数据表单提交
$.fn.formOptions = function() {};

$.fn.formOptions.viewFun = function(id, title, width, height, url) {
	if (id == undefined) {
		//点击右键菜单才会触发这个
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id;
	} else {
		url = url + '&id=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url
	});
}

$.fn.formOptions.btnSubFun = function(formId, url, successMsg, failMsg) {
	var successMsg = successMsg || "操作成功!";
	var failMsg = failMsg || "操作失败!";
	$(formId).form('submit', {
		url: url,
		onSubmit: function() {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			var isValid = $(this).form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success: function(result) {
			parent.$.messager.progress('close');
			result = typeof result == 'object' ? result : $.parseJSON(result);
			if (result.success) {
				$.messager.alert('提示', successMsg, 'info');
			} else {
				$.messager.alert('提示', failMsg, 'warning');
			}
		}
	});
}

//options控制区块显示、隐藏
$.fn.formOptions.optionsCtl = function(obj) {
	var childLab = obj.parent().next(".child-options");
	var type = obj.val();
	if (type == 1) {
		childLab.show();
		childLab.find("input").addClass("easyui-validatebox validatebox-text");
	} else {
		childLab.hide();
		childLab.find("input").removeClass("easyui-validatebox validatebox-text validatebox-invalid");
	}
}

//平滑数据格式转换
$.fn.treeConvert = function() {}

//菜单平滑数据格式转换
$.fn.treeConvert.menu = function(rows) {
	function exists(rows, parentId) {
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].id == parentId) return true;
		}
		return false;
	}
	var nodes = [];
	// 获取顶级菜单
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		if (!exists(rows, row.parentId)) {
			nodes.push({
				id: row.id,
				text: row.name,
				iconCls: row.iconCls,
				attributes: {
					href: row.href
				}
			});
		}
	}
	var toDo = [];
	for (var i = 0; i < nodes.length; i++) {
		toDo.push(nodes[i]);
	}
	while (toDo.length) {
		// 父级节点
		var node = toDo.shift();
		// 获取子级节点
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (row.parentId == node.id) {
				var child = {
					id: row.id,
					text: row.name,
					iconCls: row.iconCls,
					attributes: {
						href: row.href
					}
				};
				if (node.children) {
					node.children.push(child);
				} else {
					node.children = [child];
				}
				toDo.push(child);
			}
		}
	}
	return nodes;
}

//iframe 页面最小宽度设置
$.fn.iframeWidthResize = function(width) {
	$("body", parent.document).find('iframe').each(function(i) {
		var thisUrl = window.location.href;
		var frameUrl = $(this).attr('src');
		if (thisUrl.indexOf(frameUrl) > 0 || thisUrl == frameUrl) {
			$(this).css({
				"min-width": width
			});
		}
	});
}

//文件下载
$.fn.downloadFun = function(id, url) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	}
	url = url + "id=" + id
	$.ajax({
		"url": url,
		"type": "GET",
		"success": function(data) {
			window.location.href = url;
		},
		"error": function() {
			$.messager.alert("消息提醒：", "文件下载失败，请稍候再试！", "warning");
		}
	});
}

//数据类型转字符串
var typeToStr = function(data, type, status) {
	var result = '';
	$.each(data, function(index, item) {
		if (type == parseInt(item.value) && item.type == status) result = item.name;
	});
	return result;
}

//浏览小图标
var showIcons = function() {
	var dialog = parent.sy.modalDialog({
		title: '浏览小图标',
		width: 520,
		height: 398,
		url: '/icons.html',
		buttons: [{
			text: '确定',
			handler: function() {
				dialog.find('iframe').get(0).contentWindow.selectIcon(dialog, $('#iconCls'));
			}
		}]
	});
};

function goAnother() {
	var url = $("#new_url").val();
	var subtitle = $("#new_title").val();
	var icon = "undefiend";
	addTab(subtitle, url, icon);
}

function closeTab(subtitle, new_title, new_url) {
	var subtitle = $("#close_title").val();
	$('#index_tabs').tabs('close', subtitle);
}

function closeTab(subtitle) {
	$('#index_tabs').tabs('close', subtitle);
}

function updateTab() {
	var subtitle = $("#close_title").val();
	$('#index_tabs').tabs('close', subtitle);
	var url = $("#new_url").val();
	var subtitle = $("#new_title").val();
	var icon = "undefiend";
	addTab(subtitle, url, icon);
	subtitle = $("#temp_title").val();
	$('#index_tabs').tabs('close', subtitle);
}

//添加tab页   
function addTab(subtitle, url, icon) {
	if (!$('#index_tabs').tabs('exists', subtitle)) {
		$('#index_tabs').tabs('add', {
			title: subtitle,
			content: createFrame(url),
			closable: true
				//closable:true,
				//icon:icon
		});
	} else {
		$('#index_tabs').tabs('select', subtitle);
	}
}

function addCollateralPage(id, title, url) {
	if (!parent.$('#index_tabs').tabs('exists', title)) {
		var url = url + '?id=' + id;
		parent.$('#index_tabs').tabs('add', {
			title: title,
			content: createFrame(url),
			closable: true
		});
	} else {
		parent.$('#index_tabs').tabs('select', title);
		var url = url + '?id=' + id;
		var tab = parent.$('#index_tabs').tabs('getSelected');
		parent.$('#index_tabs').tabs('update', {
			tab: tab,
			options: {
				title: title,
				content: createFrame(url),
				closable: true
			}
		});
		tab.panel('refresh');
	}

}

function createFrame(url) {
	var s = '<iframe allowtransparency="true" scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
	return s;
}

var addGoodsTab = function(params) {
	var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	var t = parent.$('#index_tabs');
	var opts = {
		title: params.title,
		closable: true,
		content: iframe,
		border: false,
		fit: true
	};
	if (t.tabs('exists', opts.title)) {
		t.tabs('select', opts.title);
		parent.$.messager.progress('close');
	} else {
		t.tabs('add', opts);
	}
}

//从grid列表页添加tab页   
var gridAddTab = function(params, id, borrowNo) {
	if (typeof(params) != "object") params = eval("(" + params + ")");
	var iframe = '<iframe allowtransparency="true" src="' + params.url + id + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
	var t = parent.$('#index_tabs');
	var opts = {
		title: params.title.replace("{$borrowNo}", borrowNo),
		closable: true,
		iconCls: params.iconCls,
		content: iframe,
		border: false,
		fit: true
	};
	if (t.tabs('exists', opts.title)) {
		t.tabs('select', opts.title);
		parent.$.messager.progress('close');
	} else {
		t.tabs('add', opts);
	}
}

//时间戳转换
var getLocalTime = function(value, type) {
	if (value == null || value == '') {
		return '';
	}
	var dt;
	if (value instanceof Date) {
		dt = value;
	} else {
		dt = new Date(value);
		if (isNaN(dt)) {
			//将那个长字符串的日期值转换成正常的JS日期格式
			value = value.replace(/\/Date\((-?\d+)\)\//, '$1');
			dt = new Date();
			dt.setTime(value);
		}
	}
	switch (type) {
		//这里用到一个javascript的Date类型的拓展方法
		case 1:
			return dt.format("yyyy年MM月dd日");
			break;
		case 2:
			return dt.format("yyyy年MM月dd日 hh:mm:ss");
			break;
		case 3:
			return dt.format("yyyy-MM-dd");
			break;
		case 4:
			return dt.format("yyyy-MM-dd hh:mm:ss");
			break;
	}
}

// 日期显示的格式化	
Date.prototype.format = function(format) {
	var o = {
		"M+": this.getMonth() + 1, //month 
		"d+": this.getDate(), //day 
		"h+": this.getHours(), //hour 
		"m+": this.getMinutes(), //minute 
		"s+": this.getSeconds(), //second 
		"q+": Math.floor((this.getMonth() + 3) / 3), //quarter 
		"S": this.getMilliseconds() //millisecond 
	}
	if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1,
				RegExp.$1.length == 1 ? o[k] :
				("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}

/*
 * MAP对象，实现MAP功能
 *
 * 接口：
 * size()     获取MAP元素个数
 * isEmpty()    判断MAP是否为空
 * clear()     删除MAP所有元素
 * put(key, value)   向MAP中增加元素（key, value) 
 * remove(key)    删除指定KEY的元素，成功返回True，失败返回False
 * get(key)    获取指定KEY的元素值VALUE，失败返回NULL
 * element(index)   获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
 * containsKey(key)  判断MAP中是否含有指定KEY的元素
 * containsValue(value) 判断MAP中是否含有指定VALUE的元素
 * values()    获取MAP中所有VALUE的数组（ARRAY）
 * keys()     获取MAP中所有KEY的数组（ARRAY）
 *
 * 例子：
 * var map = new Map();
 *
 * map.put("key", "value");
 * var val = map.get("key")
 * ……
 *
 */
function Map() {
	this.elements = new Array();

	//获取MAP元素个数
	this.size = function() {
		return this.elements.length;
	};

	//判断MAP是否为空
	this.isEmpty = function() {
		return (this.elements.length < 1);
	};

	//删除MAP所有元素
	this.clear = function() {
		this.elements = new Array();
	};

	//向MAP中增加元素（key, value) 
	this.put = function(_key, _value) {
		this.elements.push({
			key: _key,
			value: _value
		});
	};

	//删除指定KEY的元素，成功返回True，失败返回False
	this.remove = function(_key) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	};

	//获取指定KEY的元素值VALUE，失败返回NULL
	this.get = function(_key) {
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return this.elements[i].value;
				}
			}
		} catch (e) {
			return null;
		}
	};

	//获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
	this.element = function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return null;
		}
		return this.elements[_index];
	};

	//判断MAP中是否含有指定KEY的元素
	this.containsKey = function(_key) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	};

	//判断MAP中是否含有指定VALUE的元素
	this.containsValue = function(_value) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].value == _value) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	};

	//获取MAP中所有VALUE的数组（ARRAY）
	this.values = function() {
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	};

	//获取MAP中所有KEY的数组（ARRAY）
	this.keys = function() {
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	};
}

//标类型
var borrowType = function(value) {
	switch (value) {
		case 101:
			return '秒还标';
			break;
		case 102:
			return '信用标';
			break;
		case 103:
			return '固定收益类产品';
			break;
		case 104:
			return '净值标';
			break;
		case 110:
			return '流转标';
			break;
		case 112:
			return '担保标';
			break;
		case 119:
			return "海外投资产品";
			break;
		case 122:
			return "浮动收益类产品";
			break;
		default:
			return "";
			break;
	}
}

//审核状态
var verifyStatus = function(value) {
	switch (value) {
		case 0:
			return '待审核';
			break;
		case 1:
			return '审核通过';
			break;
		case 2:
			return '审核未通过';
			break;
		default:
			return "";
			break;
	}
}

//登记标
$.fn.dataGridOptions.registerBorrowFun = function(id, url, openUrl) {
	//跳转外部链接处理
	window.open(openUrl + "?id=" + id);
	parent.$.messager.confirm('提示', "是否登记成功？", function(b) {
		if (b) {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			$.post(url, {
				id: id
			}, function(data) {
				if (data.result) {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				} else {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}
			}, 'JSON');
		}
	});
}

//登记标--撤回
$.fn.dataGridOptions.registerCancelBorrowFun = function(id, openUrl) {
	window.open(openUrl + "?id=" + id);
	parent.$.messager.confirm('提示', "是否撤回成功？", function(b) {
		if (b) {
			dataGrid.datagrid('reload');
		}
	});
}

//登记担保方
$.fn.dataGridOptions.registerGuaranteeFun = function(id, url, openUrl) {
	//跳转外部链接处理
	window.open(openUrl + "?id=" + id);
	parent.$.messager.confirm('提示', "担保方登记是否成功？", function(b) {
		if (b) {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			$.post(url, {
				id: id
			}, function(data) {
				if (data.result) {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				} else {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}
			}, 'JSON');
		}
	});
}

//确认发标管理-取消标操作
$.fn.dataGridOptions.cancelBorrowFun = function(id, url) {
	parent.$.messager.confirm('提示', "是否取消标操作？", function(b) {
		if (b) {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			$.post(url, {
				id: id
			}, function(data) {
				if (data.result) {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				} else {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}
			}, 'JSON');
		}
	});
}

//查看抵押物
$.fn.dataGridOptions.CollateralPageFun = function(id, title, width, height, url) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id;
	} else {
		url = url + '&id=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '关闭',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.handler.dialog('close');
				parent.$.modalDialog.openner_dataGrid = dataGrid;

			}
		}]
	});
}

//更新资产包
$.fn.dataGridOptions.editPicFun = function(id, title, width, height, url) {
	if (url.indexOf("?") == -1) {
		url = url + '?ids=' + id;
	} else {
		url = url + '&ids=' + id;
	}
	parent.$('#getMortgagePicPage').dialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '确定',
			handler: function() {
				$('#form').form({
					url: '/modules/loan/borrow/updateBorrowCollateral.html',
					success: function(data) {
						data = $.parseJSON(data);
						if (data.result) {
							parent.$.messager.alert('提示1', data.msg, 'info');
							$.modalDialog.handler.dialog('close');
							dataGrid.datagrid('reload');
						} else {
							parent.$.messager.alert('提示2', data.msg, 'info');
							dataGrid.datagrid('reload');
						}
					}
				});
				$('#form').submit();
			}
		}]
	})
}

//发标相关
$.fn.dataGridOptions.borroweditFun = function(id, title, width, height, url, text) {
	if (text == null || text == '') {
		text = '确定';
	}
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id;
	} else {
		url = url + '&id=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: text,
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}

//浏览借款人邮箱
var showMail = function() {
	dialog = parent.sy.modalDialog({
		title: '选择借款人',
		width: 520,
		height: 450,
		url: '/modules/loan/borrow/getEmailListPage.html',
		buttons: [{
			text: '确定',
			handler: function() {
				dialog.find('iframe').get(0).contentWindow.getSelected(dialog, $('#email'));
				$('#email').focus();
			}
		}]
	});
};

var insertData = function(email) {
	alert(email);
}

//浏览借款人邮箱
var showUserInvite = function(id) {
	dialog = parent.sy.modalDialog({
		title: '选择用户名',
		width: 520,
		height: 450,
		url: '/modules/user/user/user/userPromotDetailPage.html?id=' + id,
		buttons: [{
			text: '确定',
			handler: function() {
				dialog.find('iframe').get(0).contentWindow.getSelected(dialog, $('#email'));
				$('#email').focus();
			}
		}]
	});
};

//浏览用户
var showUser = function() {
	dialog = parent.sy.modalDialog({
		title: '选择充值用户',
		width: 520,
		height: 450,
		url: '/modules/user/user/user/getUserListPage.html',
		buttons: [{
			text: '确定',
			handler: function() {
				dialog.find('iframe').get(0).contentWindow.getSelected(dialog, $('#userName'), $('#userType'));
				$('#userName').focus();
			}
		}]
	});
};

//提现管理 设置为提现不通过
$.fn.dataGridOptions.cancelCashFun = function(id, url) {
	parent.$.messager.confirm('提示', "是否设置为提现失败？", function(b) {
		if (b) {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			$.post(url, {
				id: id
			}, function(data) {
				if (data.result) {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				} else {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}
			}, 'JSON');
		}
	});
}

//提现管理 设置为提现通过
$.fn.dataGridOptions.verifyCashFun = function(id, url) {
	parent.$.messager.confirm('提示', "是否设置为提现成功？", function(b) {
		if (b) {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			$.post(url, {
				id: id
			}, function(data) {
				if (data.result) {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				} else {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}
			}, 'JSON');
		}
	});
}

//充值管理 设置为充值不通过
$.fn.dataGridOptions.cancelRechargeFun = function(id, url) {
	parent.$.messager.confirm('提示', "是否设置为充值失败？", function(b) {
		if (b) {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			$.post(url, {
				id: id
			}, function(data) {
				if (data.result) {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				} else {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}
			}, 'JSON');
		}
	});
}

//充值管理 设置为充值通过
$.fn.dataGridOptions.verifyRechargeFun = function(id, url) {
	parent.$.messager.confirm('提示', "是否设置为充值成功？", function(b) {
		if (b) {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			$.post(url, {
				id: id
			}, function(data) {
				if (data.result) {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				} else {
					parent.$.messager.alert('提示', data.msg, 'info');
					dataGrid.datagrid('reload');
					parent.$.messager.progress('close');
				}
			}, 'JSON');
		}
	});
}

//更新资产包
$.fn.dataGridOptions.upDataMortgageFun = function(id, title, width, height, url) {
	if (url.indexOf("?") == -1) {
		url = url + '?ids=' + id;
	} else {
		url = url + '&ids=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	})
}

//更新资产包图片
$.fn.dataGridOptions.upDataMortgagePicFun = function(id, title, width, height, url) {
	if (url.indexOf("?") == -1) {
		url = url + '?ids=' + id;
	} else {
		url = url + '&ids=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	})
}

//添加资产包图片
$.fn.dataGridOptions.addMortgagePicFun = function(id, title, width, height, url) {
	url = url + '?id=' + id;
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	})
}

//批量更新资产包
function outBound() {
	var id = $("#borrowId").val();
	var ids = [];
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length != 0) {
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		var param = "?id=" + id;
		for (var i = 0; i < ids.length; i++) {
			param += "&ids=" + ids[i];
		}
		update(param);
	} else {
		parent.$.messager.alert('提示', '请先选择资产包', 'info');
	}
}

function update(id) {
	parent.$.modalDialog({
		title: '更新资产包',
		width: 800,
		height: 400,
		href: '/modules/loan/borrow/getUpdateMortgageListPage.html' + id,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	})
}

//预览标
$.fn.dataGridOptions.viewBorrowFun = function(id, openUrl) {
	//跳转外部链接处理
	window.open(openUrl + "&id=" + id);
}

//查看红包明细
$.fn.dataGridOptions.viewRedPacketFun = function(id, title, width, height, url) {
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id;
	} else {
		url = url + '&ids=' + id;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '关闭',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				parent.$.modalDialog.handler.dialog('close');
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
			}
		}]
	})
}

//数据编辑
$.fn.dataGridOptions.certificationEditFun = function(id, title, width, height, url, userId, typeId) {
	if (id == undefined) {
		var rows = dataGrid.datagrid('getSelections');
		id = rows[0].id;
	} else {
		dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
	}
	if (url.indexOf("?") == -1) {
		url = url + '?id=' + id + '&userId=' + userId + '&typeId=' + typeId;
	} else {
		url = url + '&id=' + id + '&userId=' + userId + '&typeId=' + typeId;
	}
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: url,
		buttons: [{
			text: '确定',
			handler: function() {
				//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		}]
	});
}
