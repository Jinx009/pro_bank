//日期范围控件
var start = {
            elem: '#startTime',
            min: '1990-04-10 23:59:59', //设定最小日期为当前日期
            max: '2099-06-16 23:59:59', //最大日期
            istoday: false, //是否显示今天
            choose: function(datas){
                 end.min = datas; //开始日选好后，重置结束日的最小日期
                 end.start = datas //将结束日的初始值设定为开始日
            }
        };
var end = {
            elem: '#endTime',
            min: '1990-04-10 23:59:59',
            max: '2099-06-16 23:59:59',
            istoday: false,
            choose: function(datas){
                start.max = datas; //结束日选好后，充值开始日的最大日期
            }
};
laydate(start);
laydate(end);
//设置默认值
// var time = new Date();
// var year = time.getFullYear();
// var startmonth = time.getMonth();
// var endmonth = parseInt(time.getMonth())+1;
// var day = time.getDate();
// if(parseInt(startmonth) < 10)
// {
//     startmonth = '0' + startmonth;
// }
// if(endmonth < 10)
// {
//     endmonth = '0' + endmonth;
// }
// if(parseInt(day) < 10)
// {
//     day = '0' + day;
// }
// var startTimeVal = year+'-'+startmonth+'-'+day;
// var endTimeVal = year+'-'+endmonth+'-'+day;
// document.getElementById("startTime").value = startTimeVal;
// document.getElementById("endTime").value = endTimeVal;
