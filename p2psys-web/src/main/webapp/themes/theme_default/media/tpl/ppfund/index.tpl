{{#each data.list}}
  <li class="invest-list" style="height:398px;">
      <div class="invest-list-top"><a href="/ppfund/detail.html?id={{id}}&startTime={{addTime}}" title="{{name}}"><img src="{{articleImage ppfundImg ../url}}" width="258px" height="179px"></a></div>
      <div class="invest-list-down">
          <div class="invset-list-title"><a href="/ppfund/detail.html?id={{id}}" title="{{name}}">{{name}}</a></div>
          <p style="padding-bottom:15px;">投资规模 ￥{{moneyFormat account}}</p>
          <p style="padding-bottom:15px;">计息方式 <font>T+{{interestWay}}</font></p>
           <div class="invest-jd">
              <div class="invest-jd-word clearfix"><span class="float_left">投资进度</span><span class="float_right"><font style="color:#333;font-weight:bold;">{{scales}}%</font></span></div>
              <div class="jindu float_left">
                  <div class="rate_tiao" style="width:{{scales}}%"></div>
              </div>
          </div>
          <ul class="clearfix">
              <li><font style="color:#333;font-weight:bold;">{{apr}}%</font><br/>预期年化收益率</li>
              <li><font style="color:#333;font-weight:bold;">{{ppfundRepaymentWay isFixedTerm}}</font><br/>还款方式</li>
              <li><font style="color:#333;font-weight:bold;">{{ppfundTimeLimit timeLimit}}</font><br/>投资期限</li>
          </ul>
      </div>
  </li> 
{{/each}}