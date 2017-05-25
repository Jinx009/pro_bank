	
	// HOME
	var index_tabs;
	var index_tabsMenu;
	var index_layout;
	$(function() {
		index_layout = $('#index_layout').layout({
			fit : true
		});
		index_tabs = $('#index_tabs').tabs({
			fit : true,
			border : false,
			onContextMenu : function(e, title) {
				e.preventDefault();
				index_tabsMenu.menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data('tabTitle', title);
			}
		});

		index_tabsMenu = $('#index_tabsMenu').menu({
			onClick : function(item) {
				var curTabTitle = $(this).data('tabTitle');
				var type = $(item.target).attr('title');

				if (type === 'refresh') {
					index_tabs.tabs('getTab', curTabTitle).panel('refresh');
					return;
				}

				if (type === 'close') {
					var t = index_tabs.tabs('getTab', curTabTitle);
					if (t.panel('options').closable) {
						index_tabs.tabs('close', curTabTitle);
					}
					return;
				}

				var allTabs = index_tabs.tabs('tabs');
				var closeTabsTitle = [];

				$.each(allTabs, function() {
					var opt = $(this).panel('options');
					if (opt.closable && opt.title != curTabTitle && type === 'closeOther') {
						closeTabsTitle.push(opt.title);
					} else if (opt.closable && type === 'closeAll') {
						closeTabsTitle.push(opt.title);
					}
				});

				for ( var i = 0; i < closeTabsTitle.length; i++) {
					index_tabs.tabs('close', closeTabsTitle[i]);
				}
			}
		});
	});
	//NORTH
	function logoutFun(b) {	    
		$.ajax({
		     url:'/modules/logout.html', 
			 data: {t:new Date()}, 
			 type:"GET",
			 dataType:"json",
			 success:function(data) {	 
			    if(data.result){
					location.href=('/index.html');
				}else{
				    $.messager.alert("提醒",data.msg,"warning");				  
				}
		     }
		});
	}
	// var sessionInfo_userId = '${(session_user.id)!""}';
	function editCurrentUserPwd() {
		parent.$.modalDialog({
			title : '修改密码',
			width : 400,
			height : 320,
			href : './manager/editPwdPage.html',
			buttons : [ {
				text : '修改',
				handler : function() {
					var f = parent.$.modalDialog.handler.find('#editCurrentUserPwdForm');
					f.submit();
				}
			} ]
		});
	}
	function currentUserRole() {
		parent.$.modalDialog({
			title : '我的角色',
			width : 400,
			height : 500,
			href : './manager/role.html'
		});
	}
	function currentUserResource() {
		parent.$.modalDialog({
			title : '我可以访问的资源',
			width : 400,
			height : 500,
			href : './manager/resource.html'
		});
	}
	//main
	var portalLayout;
	var portal;
	$(function() {
		portalLayout = $('#portal').layout({
			fit : true
		});
		$(window).resize(function() {
			portalLayout.layout('panel', 'center').panel('resize', {
				width : 1,
				height : 1
			});
		});

		panels = [ {
			id : 'p1',
			title : '运营信息',
			iconCls:'tj_ico',
			height : 230,
			collapsible : true,
			href : '/modules/home/notices.html'
		},{
			id : 'p2',
			title : '待办事项',
			iconCls:'tj_ico',
			height : 230,
			collapsible : true,
			href : '/modules/home/todoManager.html'

		},{
			id : 'p3',
			title : '投资人数最近7天统计',
			iconCls:'tj_ico',
			height : 230,
			collapsible : true,
			href : '/modules/home/investmentStatisticsPage.html'
		},{
			id : 'p4',
			title : '借款金额最近7天统计',
			iconCls:'tj_ico',
			height : 230,
			collapsible : true,
			href : '/modules/home/borrowingStatisticsPage.html'
		}];

		portal = $('#portal').portal({
			border : false,
			fit : true,
			onStateChange : function() {
				$.cookie('portal-state', getPortalState(), {
					expires : 7
				});
			}
		});
		
		var state = $.cookie('portal-state');
		if (!state) {
			state = 'p1,p3:p2,p4';/*冒号代表列，逗号代表行*/
		}
		addPortalPanels(state);
		portal.portal('resize');

	});

	function getPanelOptions(id) {
		for ( var i = 0; i < panels.length; i++) {
			if (panels[i].id == id) {
				return panels[i];
			}
		}
		return undefined;
	}
	function getPortalState() {
		var aa = [];
		for ( var columnIndex = 0; columnIndex < 2; columnIndex++) {
			var cc = [];
			var panels = portal.portal('getPanels', columnIndex);
			for ( var i = 0; i < panels.length; i++) {
				cc.push(panels[i].attr('id'));
			}
			aa.push(cc.join(','));
		}
		return aa.join(':');
	}
	function addPortalPanels(portalState) {
		var columns = portalState.split(':');
		for ( var columnIndex = 0; columnIndex < columns.length; columnIndex++) {
			var cc = columns[columnIndex].split(',');
			for ( var j = 0; j < cc.length; j++) {
				var options = getPanelOptions(cc[j]);
				if (options) {
					var p = $('<div/>').attr('id', options.id).appendTo('body');
					p.panel(options);
					portal.portal('add', {
						panel : p,
						columnIndex : columnIndex
					});
				}
			}
		}
	}

	  