define(function(require,exports,module){
    require('jquery');
    
    //总资产，账户余额,微投资，冻结金额(如需截取两位小数，用toFixed(2))
    $.ajax({
      type:"post",
      url:"/member/investHandleMoney.html?random="+Math.random(),
      dataType:"json",
      success:function(data){
         $("#count_AMT").html(data.total);/*总资产*/
         $("#count_mb").html(data.useMoney);/*账户余额*/
         if((data.total-data.noUseMoney-data.useMoney)==0){
            $("#span_mb").html(0);
         }else{
            $("#span_mb").html((data.total-data.noUseMoney-data.useMoney).toFixed(2));/*微投资*/
         }
         $("#span_dj").html(data.noUseMoney);/*冻结金额*/
         $("#collect_mb").html(data.collection);/*待收金额*/
         $("#amountMoney").html(data.sumInterest);/*累计净收益*/
      }
   });
 
    
});

