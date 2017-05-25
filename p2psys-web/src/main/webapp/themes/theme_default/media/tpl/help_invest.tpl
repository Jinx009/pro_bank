{{#each data.list}}
 <li class="clearfix index-list-xm">
 		<ul class="index-list-left clearfix">
 			<li class="index-project-title">{{{investTypeName type}}}<a href="/invest/detail.html?id={{id}}&startTime={{addTime}}" title="{{name}}">{{hideIndexBorrowName name}}</a><span class="recommend">{{{transrecommend recommend}}}</span></li>
 			<li class="index-list-rate">年利率<span>{{{apr}}}</span>%</li>
 			<li class="index-list-money">金额<br/><span>{{moneyFormat account}}</span></li>
 			<li class="index-list-time">期限<br/><font color="#333">{{{timeLimitFormat type timeLimit borrowTimeType}}}</font></li>
 			<li class="index-list-status">状态<br/>{{{transFormatScales scales}}}</li>
 			<li style="width:170px;">借款进度<br/>
 				<div class="jindu float_left">
			        <div class="rate_tiao" style="width:{{scales}}%"></div>
			    </div>
			    <div class="float_left"><font color="#333">{{scales}}%</font></div>
			</li>
 		</ul>
    <div class="full-biao">{{{transFormatfull scales}}}</div>
  	 	<div class="index-list-right">
  	 		<div class="clearfix">
  	 			<span class="float_left">已投金额：</span>
  	 			<span class="float_right">{{moneyFormat accountYes}}元</span>
  	 		</div>
  	 		<div class="clearfix">
  	 			<span class="float_left">投资人数：</span>
  	 			<span class="float_right">{{tenderCount}}人</span>
  	 		</div>
  	 		{{inverstListOptFun scales id addTime fixedTime}}
  	 	</div>
 </li>
{{/each}}