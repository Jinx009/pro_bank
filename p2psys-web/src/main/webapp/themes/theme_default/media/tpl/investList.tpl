{{#each data.list}}
  <li class="invest-list">
      <div class="invest-list-top"><a href="/invest/detail.html?id={{id}}&startTime={{addTime}}" title="{{name}}"><img src="{{articleImage borrowImg url}}" width="258px" height="179px"></a></div>
      <div class="invest-list-down">
          <div class="invset-list-title"><a href="/invest/detail.html?id={{id}}&startTime={{addTime}}" title="{{name}}">{{name}}</a></div>
         
          <div class="invset-list-word">
              {{indexContent content}}
          </div>
          <div class="invest-jd">
              <div class="invest-jd-word clearfix"><span class="float_left">投资进度</span><span class="float_right"><font style="color:#333;font-weight:bold;">{{scales}}%</font></span></div>
              <div class="jindu float_left">
                  <div class="rate_tiao" style="width:{{scales}}%"></div>
              </div>
          </div>
          <ul class="clearfix">
              <li><font style="color:#333;font-weight:bold;">￥{{moneyFormat account}}</font><br/>投资规模</li>
              <li><font style="color:#333;font-weight:bold;">{{{apr}}}%</font><br/>预期年化收益率</li>
              <li><font style="color:#333;font-weight:bold;">{{{timeLimitFormat type timeLimit borrowTimeType}}}</font><br/>投资期限</li>
          </ul>
      </div>
  </li> 
{{/each}}