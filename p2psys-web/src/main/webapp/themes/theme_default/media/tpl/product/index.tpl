{{#each data.list}}

		{{#if ppfundModel}}
			  <li class="invest-list" style="height:398px;">
			      <div class="invest-list-top"><a href="/ppfund/detail.html?id={{ppfundModel.id}}&startTime={{ppfundModel.addTime}}&flagId={{flagId}}" title="{{productName}}"><img src="{{articleImage ppfundModel.ppfundImg ppfundModel.url}}" width="258px" height="179px"></a></div>
			      <div class="invest-list-down">
			          <div class="invset-list-title"><a href="/ppfund/detail.html?id={{ppfundModel.id}}&flagId={{flagId}}" title="{{productName}}">{{productName}}</a></div>
			          
			          <p style="padding-bottom:15px;">投资规模 ￥{{ppfundModel.mostAccountTotal}}</p>		         
			          <p style="padding-bottom:15px;">计息方式 <font>T+{{ppfundModel.interestWay}}</font></p>
			           <div class="invest-jd">
			              <div class="invest-jd-word clearfix"><span class="float_left">投资进度</span><span class="float_right"><font style="color:#333;font-weight:bold;">{{ppfundModel.scales}}%</font></span></div>
			              <div class="jindu float_left">
			                  <div class="rate_tiao" style="width:{{ppfundModel.scales}}%"></div>
			              </div>
			          </div>
			          <ul class="clearfix">
			          
			          	  {{#inequality highestRefundRate lowestRefundRate}}
				              {{#equal highestRefundRate 0}}
					              <li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%+浮动</font><br/>预期年化收益率</li>
					              	{{else}}
					              <li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%~{{highestRefundRate}}%</font><br/>预期年化收益率</li>
				              {{/equal}}
			              {{else}}
				              {{#equal highestRefundRate 0}}
					              	<li><font style="color:#333;font-weight:bold;">浮动</font><br/>预期年化收益率</li>
					            {{else}}
					              	<li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%</font><br/>预期年化收益率</li>
				              {{/equal}}
				           {{/inequality}}  
				               
				              <li><font style="color:#333;font-weight:bold;">{{ppfundRepaymentWay ppfundModel.isFixedTerm}}</font><br/>还款方式</li>
				              {{#inequality ppfundModel.timeLimit 0}}
					         	<li><font style="color:#333;font-weight:bold;">{{ppfundModel.timeLimit}}天</font><br/>投资期限</li>   
							  {{else}}	
					         	 <li><font style="color:#333;font-weight:bold;">活期</font><br/>投资期限</li>  
					            
				          {{/inequality}}
				       
			          </ul>
			          
			      </div>
			  </li>
  		{{/if}}
  		
  		{{#if borrowModel}}
  		
			  {{#inequality borrowModel.type 122}}
			  		
						  <li class="invest-list" style="height:398px;">
					      <div class="invest-list-top"><a href="/invest/detail.html?id={{borrowModel.id}}&startTime={{borrowModel.addTime}}&flagId={{flagId}}" title="{{productName}}"><img src="{{articleImage borrowModel.borrowImg borrowModel.url}}" width="258px" height="179px"></a></div>
					      <div class="invest-list-down">
					          <div class="invset-list-title"><a href="/invest/detail.html?id={{borrowModel.id}}&flagId={{flagId}}" title="{{productName}}">{{productName}}</a></div>
					         
					          <div class="invset-list-word" style="padding-top:0px;height:63px;">
					             {{indexContent  borrowModel.content}}
					          </div>
					          <div class="invest-jd">
					              <div class="invest-jd-word clearfix"><span class="float_left">投资进度</span><span class="float_right"><font style="color:#333;font-weight:bold;">{{borrowModel.scales}}%</font></span></div>
					              <div class="jindu float_left">
					                  <div class="rate_tiao" style="width:{{borrowModel.scales}}%"></div>
					              </div>
					          </div>
					          <ul class="clearfix">
					              <li><font style="color:#333;font-weight:bold;">￥{{borrowModel.mostAccount}}</font><br/>投资规模</li>
					              {{#inequality highestRefundRate lowestRefundRate}}
						              {{#equal highestRefundRate 0}}
							              <li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%+浮动</font><br/>预期年化收益率</li>
							              	{{else}}
							              <li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%~{{highestRefundRate}}%</font><br/>预期年化收益率</li>
						              {{/equal}}
					              {{else}}
						              {{#equal highestRefundRate 0}}
							              	<li><font style="color:#333;font-weight:bold;">浮动</font><br/>预期年化收益率</li>
							            {{else}}
							              	<li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%</font><br/>预期年化收益率</li>
						              {{/equal}}
						           {{/inequality}}  
					              <li><font style="color:#333;font-weight:bold;">{{borrowModel.timeLimitStr}}</font><br/>投资期限</li>
					          </ul>
					      </div>
					  </li>
			  		{{else}}
			  		<li class="invest-list" style="height:398px;">
				      <div class="invest-list-top"><a href="/invest/entrustDetail.html?id={{borrowModel.id}}&startTime={{borrowModel.addTime}}&flagId={{flagId}}" title="{{productName}}"><img src="{{articleImage borrowModel.borrowImg borrowModel.url}}" width="258px" height="179px"></a></div>
				      <div class="invest-list-down">
				          <div class="invset-list-title"><a href="/invest/entrustDetail.html?id={{borrowModel.id}}&flagId={{flagId}}" title="{{productName}}">{{productName}}</a></div>
				         
				          <div class="invset-list-word" style="padding-top:0px;height:63px;">
				             {{indexContent  borrowModel.content}}
				          </div>
				          <div class="invest-jd">
				              <div class="invest-jd-word clearfix"><span class="float_left">投资进度</span><span class="float_right"><font style="color:#333;font-weight:bold;">{{borrowModel.scales}}%</font></span></div>
				              <div class="jindu float_left">
				                  <div class="rate_tiao" style="width:{{borrowModel.scales}}%"></div>
				              </div>
				          </div>
				          <ul class="clearfix">
				              <li><font style="color:#333;font-weight:bold;">￥{{borrowModel.mostAccount}}</font><br/>投资规模</li>
				              {{#inequality highestRefundRate lowestRefundRate}}
						              {{#equal highestRefundRate 0}}
							              <li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%+浮动</font><br/>预期年化收益率</li>
							              	{{else}}
							              <li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%~{{highestRefundRate}}%</font><br/>预期年化收益率</li>
						              {{/equal}}
					              {{else}}
						              {{#equal highestRefundRate 0}}
							              	<li><font style="color:#333;font-weight:bold;">浮动</font><br/>预期年化收益率</li>
							            {{else}}
							              	<li><font style="color:#333;font-weight:bold;">{{lowestRefundRate}}%</font><br/>预期年化收益率</li>
						              {{/equal}}
						           {{/inequality}}
				              <li><font style="color:#333;font-weight:bold;">{{borrowModel.timeLimitStr}}</font><br/>投资期限</li>
				          </ul>
				      </div>
				  </li>
			  {{/inequality}}	
			  		  
  		{{/if}}
   
{{/each}}