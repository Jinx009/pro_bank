$(function() {
        //vue开发调试，到线上之前下面一行删掉
        Vue.config.debug = true;
        //banner图
        $.ajax({
            url: '/cf/wechat/banner.action',
            type: 'GET',
            dataType: 'JSON'
        }).done(function(res) {
        	if(null!=res.data){
        		for(var i in res.data){
        			res.data[i].bannerPicUrl = adminUrl+res.data[i].bannerPicUrl;
        		}
        	}
            // 实例化vue
            var _banner=new Vue({
                el: '.swiper-container',
                data: {
                    banners: res.data
                }
            });

            // banner轮播图
            var mySwiper = new Swiper('.swiper-container', {
                loop: true, //循环播放
                autoplay: 5000,
                lazyLoading: true, //延迟加载图片
                lazyLoadingInPrevNext: true, //延迟加载当前图片之前和之后一张图片
                lazyLoadingOnTransitionStart: true, //过渡一开始就加载
                pagination: '.swiper-pagination', //导航分页
                paginationClickable: true //导航点击切换
            });

        }); //$ajax-done
        var id = $('#id').val();
        //产品列表
        $.ajax({
            url: '/wechat/p/list.action?id='+id,
            type: 'GET',
            dataType: 'JSON'
        }).done(function(res) {
        	for(var j in  res.errorMsg){
        		res.errorMsg[j].step = res.errorMsg[j].account/res.errorMsg[j].wannaAccount*100;
        		if(null!=res.errorMsg[j].materialsList){
    				for(var i in res.errorMsg[j].materialsList){
    					//项目头图
    					if('detail_img'==res.errorMsg[j].materialsList[i].materialCode){
    						res.errorMsg[j].detailImg = adminUrl+res.errorMsg[j].materialsList[i].materialContent;
    					}
    				}
    			}
        		res.errorMsg[j].step = getDivisionResult(res.errorMsg[j].account,res.errorMsg[j].wannaAccount)*100+'%';
        	}
            // 实例化vue
            var _prolist=new Vue({
                el: '.productList',
                data: {
                    products: res.errorMsg
                }
            });
        }); //$ajax-done

        $('.select > p').on('click', function(event) {
            $('.select').toggleClass('open');
        });
        $('.select ul li').on('click', function(event) {
            var _this = $(this);
            $('.select > p').text(_this.attr('data-value'));
            _this.addClass('selected').siblings().removeClass('selected');
            $('.select').removeClass('open');
        });

    }) //$function
