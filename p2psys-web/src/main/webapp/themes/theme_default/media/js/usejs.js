
$("[rel='tooltip']").tooltip(); //冒泡
$("#nav").nav({isTab:false,childLi:".navul .nav_li",childContent:".submenu"})//导航调用
$(".asset_box").tabChange({isClick:false,childLi:".tab_ul li",childContent:".tab_content"})//选项卡切换
$(".success_case").slideFn({
	isTop:true,//是否
	slideTimer:"3000"
});
//无缝滚动

$(".lazyload").scrollLoading();//延时加载图片
$(".user_account_detail").nav({isTab:false,childLi:".user_account_detail_icon",childContent:".user_detail"})// 操作明细弹出框JS 
$(".J_topinfo").nav({isTab:false,childLi:".J_info_b",childContent:".J_info_ul"})// 操作明细弹出框JS 
$("#container").tabChange({childLi:"#tab li",childContent:"#myTabContent .list-tab-con",hoverClassName:"active",isClick:true});
$("#container").flowChange();
$(".borrow_tab_content").tabChange({isClick:true, childLi:".borrow_tab li",  hoverClassName:"active"});	//childContent:".borrow_tab_content_list",
$(".borrow_tab_content input[type='radio']").each(function(){$(this).minRadio();});
$(".borrow_tab_content input[type='checkbox']").each(function(){$(this).minCheck();});
//$(".borrow_tab_content select").minSelect();//根据select和ul的外层父级div来做事件触发	
$("#container").tabChange({childLi:".recharge_list li",childContent:".recharge_content",hoverClassName:"active",isClick:true,callBack:true});
//确定充值使用哪种接口
$(".user_left").memberNav({childLi:".user_menu",childContent:".user_menu_list",hoverClassName:"active"});

$("body").toggleTab({childTitle:".title_txt",childContent:".user_info_con",itemToggleBox:".user_info "});


//IE6显示该JS
$("#footer").showIE({txt:"您的浏览器版本过低，为了保证您的正常操作，请将您的浏览器版本升级到IE8"});

$("#customTest").mailAutoComplete({
	boxClass: "out_box",
	listClass: "list_box",
	focusClass: "focus_box",
	markCalss: "mark_box",
	autoClass: false,
	textHint: true,
	hintText: ""
});//实例化 注册页面的邮箱


    // bind form using 'ajaxForm' 

