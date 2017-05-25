$(function(){
	var userName = $('#userName').val();
	if(null!=userName&&''!=userName){
		$('#login_btn').css('display','none');
		$('#all_btn').css('display','block');
	}
	showLi('index_li');
	getData();
	getLeaderFactory();
	var data = 'mobiePhone=18217700275';
/*	$.ajax({
		url:'http://test.e800bank.com/nb/pc/userExists.action',
		type:'POST',
		data:data,
		dataType:'json',
		success:function(res){
			console.log(res);
		}
	})*/
})

/**
 * 跳转
 * @param url
 */
function openUrl(url){
	location.href = url;
}

/**
 * 热销榜数据
 */
function getData(){
	$.ajax({
		url:'/cf/popular/pc.action',
		type:'POST',
		dataType:'json',
		ansyc:false,
		success:function(res){
			if(null!=res.errorMsg&&''!=res.errorMsg){
				var htmlStr = '';
				for(var i = 0;i<res.errorMsg.length;i++){
					var projectImg = '/themes/theme_default/cf/img/popular_img.png';
					if(null!=res.errorMsg[i].materialsList){
						for(var j = 0;j<res.errorMsg[i].materialsList.length;j++){
							if('project_img'==res.errorMsg[i].materialsList[j].materialCode){
								projectImg = adminUrl+res.errorMsg[i].materialsList[j].materialContent;
							}
						}
					}
					htmlStr += '<div class="item" onclick="goDetail('+res.errorMsg[i].id+')" >';
					htmlStr += '<img src="'+projectImg+'" alt="800众服">';
					htmlStr += '<div class="shd"></div>';
					htmlStr += '<div class="product-status-content" >';
					htmlStr += getStatus(res.errorMsg[i].timeStatus);
					htmlStr += '</div>';
					htmlStr += '<div class="txt">';
					htmlStr += '<h3><a>'+res.errorMsg[i].projectName+'</a></h3>';
					htmlStr += '<p  class="this-p" >'+res.errorMsg[i].info+'</p>';
					htmlStr += getProgressStatus(res.errorMsg[i].timeStatus);
					htmlStr += '<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
					htmlStr += 'style="width:'+getStepStyle(res.errorMsg[i].account,res.errorMsg[i].wannaAccount)+'%;"></div></div>';
					htmlStr += '</div></div>';
				}
				$('#scroll').html(htmlStr);
			     $('#scroll').owlCarousel({
			 		items:4,
			 		autoPlay: true,
			 		navigation: true,
			 		navigationText: ['',''],
			 		scrollPerPage: true
			 	});
			}
		}
	})
}

/**
 * 跳转至产品详情
 * @param id
 */
function goDetail(id){
	location.href = '/pro/detail.html?id='+id;
}

/**
 * 领投人列表页面
 */
function goLeaderFactory(){
	location.href = '/leaderFactory.html';
}

/**
 * 领头人列表数据
 */
function getLeaderFactory(){
	$.ajax({
		url:'/cf/leaderFactory/list.action',
		type:'POST',
		dataType:'json',
		success:function(res){
			var htmlStr = '';
			if('success'==res.result){
				if(null!=res.errorMsg){
					var data = {};
					data = res.errorMsg;
					var j = 0;
					for(var i = 0;i<res.errorMsg.length;i++){
						//htmlStr += '<div class="ltr-box" onclick="openUrl("/leaderFactory.html")" >';
						htmlStr += '<div class="ltr-box" >';
						if(res.errorMsg[i].name!='蔡军毅'&&res.errorMsg[i].name!='戈晓文'){
							if(null==res.errorMsg[i].picPath||''==res.errorMsg[i].picPath){
								data[j].img = '/themes/theme_default/cf/img/login_icon.png';
							}else{
								if('0'==data.picUrl){
									data[j].img = res.errorMsg[i].picPath;
								}else{
									data[j].img = adminUrl+res.errorMsg[i].picPath;
								}
							}
							data[j].name = res.errorMsg[i].name;
							data[j].history = res.errorMsg[i].history;
							data[j].info = res.errorMsg[i].info;
							j++;
						}
					}
				}
				//创建模型
	            var model = new Vue({
	                el: 'body',
	                data:{
	                    item: data,
	                }
	            });
	            $.scrollIt();
	        	$.fooTree();
			}
		}
	})
}

/**
 * 进度文字
 * @param index
 * @returns {String}
 */
function getStatus(object){
	var step = '';
	if('1'==object){
		step += '<div class="product-status-wrap-ing">';
		step += '<span>预热中</span><img src="/themes/theme_default/cf/img/little_right.png" />';
		step += '</div>';
	}else if('2'==object){
		step += '<div class="product-status-wrap-ing">';
		step += '<span>众筹中</span><img src="/themes/theme_default/cf/img/little_right.png" />';
		step += '</div>';
	}else if('3'==object){
		step += '<div class="product-status-wrap-ing">';
		step += '<span>已失败</span><img src="/themes/theme_default/cf/img/little_right.png" />';
		step += '</div>';
	}else{
		step += '<div class="product-status-wrap-ing">';
		step += '<span>已完成</span><img src="/themes/theme_default/cf/img/little_right.png" />';
		step += '</div>';
	}
	return step;
}

/**
 * 进度条颜色
 * @param object
 */
function getProgressStatus(object){
	var step = '';
	if('1'==object){
		 step = '<div class="progress progress-no">';
	}else if('2'==object){
		 step = '<div class="progress progress-ing">';
	}else{
		 step = '<div class="progress progress-had">';
	}
	return step;
}

/**
 * 金钱格式化
 */
function getMoney(money){
	money = parseFloat(money);
	if(money>=10000){
		return parseFloat(money/10000).toFixed(0)+"万";
	}else{
		return money+"元";
	}
}

function goFactory(){
	location.href = '/leaderFactory.html';
}

/**
 * 进度条
 * @param min
 * @param max
 * @returns
 */
function getStepStyle(account,money){
	var step = parseFloat(account)/parseFloat(money)*100;
	if(step>=100){
		return 100;
	}
	return step;
}