define(function(require,exports,module){
  require('jquery');
$(function(){
//--------------------------------------------------【最新投资】
  var oHotBid = $("#newTender"),   //热门标列表
    bTime = "",           //自动滚动开关
    iIndex = 0,           //当前滚动编码
    iBidLength = oHotBid.children("li").length,     //标的数量
    sHtml=oHotBid.html();
  /*滚动动画*/
  oHotBid.append(sHtml + sHtml)
    .hover(function() {
      clearInterval(bTime);
    }, function() {
      bTime = setInterval(function() {
        oHotBid.children("li").eq(iIndex).animate({
          "left": "+=246",
          "opacity": "0"
        }, 500, function() {
          oHotBid.animate({
            "margin-top": "-=61"
          }, function() {
            if (iIndex < iBidLength) {
              iIndex += 1
            } else {
              oHotBid.css("margin-top", "0")
                .children("li").css({
                  "left": "0",
                  "opacity": "1"
                });
              iIndex = 0;
            }
          });
        });
      }, 3000);
    }).mouseleave();
  /*日期倒计时*/

  
});

});