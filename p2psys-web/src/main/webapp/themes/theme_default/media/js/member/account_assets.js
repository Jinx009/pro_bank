define(function(require,exports,module){
	require('jquery');
	require('/plugins/echarts/echarts-plain-original');
	//提示效果
    require.async(['/plugins/poshytip/tip-yellowsimple/tip-yellowsimple.css','/plugins/poshytip/jquery.poshytip.min'],function(){
    	$('.textTips').poshytip({
    		className: 'tip-yellowsimple',
    		showTimeout: 1,
    		alignTo: 'target',
				alignX: 'center',
				alignY: 'top',
				offsetY: 10,
    		allowTipHover:false
    	});
	});
	
	//初始化加载表
	ajaxDate($("#collectInterestSelect").val());
	ajaxDate1($("#collectPrincipalSelect").val());
	
	
	$("#collectInterestSelect").change(function(){
        var nowData1 =$(this).val();
        ajaxDate(nowData1);
    });
	
	$("#collectPrincipalSelect").change(function(){
        var nowData2 =$(this).val();
        ajaxDate1(nowData2);
    });
	
	function ajaxDate(param){
		//待收利息表
		$.ajax({
			url:"/member/account/interestJson.html?date="+param,
			type:"get",
			dataType:"json",
			success:function(data){
				// 基于准备好的dom，初始化echarts图表
		        var myChart = echarts.init(document.getElementById('collectInterest')); 
		        var option = {
		        	color  : ['#1874CD'],
		            tooltip: {
		                show: true
		            },
		            xAxis : [
		                {
		                	type : 'category',
		                    data : data.day
		                }
		            ],
		            yAxis : [
		                {
		                    type : 'value'
		                }
		            ],
		            series : [
		                {
		                    "name":"待收利息表",
		                    "type":"bar",
		                    "data":data.value
		                }
		            ]
		        };
		        // 为echarts对象加载数据 
		        myChart.setOption(option);
			}
		});
		}
		
	function ajaxDate1(param){ 
        //待收本金表
        // 基于准备好的dom，初始化echarts图表
		$.ajax({
			url:"/member/account/capitalJson.html?date=" + param,
			type:"get",
			dataType:"json",
			success:function(data){
				
				var myChart = echarts.init(document.getElementById('collectPrincipal')); 
		        var option = {
		        	color  : ['#1874CD'],
		            tooltip: {
		                show: true
		            },
		           
		            xAxis : [
		                {
		                    type : 'category',
		                    data : data.day
		                }
		            ],
		            yAxis : [
		                {
		                    type : 'value'
		                }
		            ],
		            series : [
		                {
		                    "name":"待收本金表",
		                    "type":"bar",
		                    "data":data.value
		                }
		            ]
		        };
		        // 为echarts对象加载数据 
		        myChart.setOption(option); 
			}
		});
	}  
});