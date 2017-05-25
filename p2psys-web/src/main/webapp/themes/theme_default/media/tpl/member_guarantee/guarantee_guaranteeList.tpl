{{#each data.list}}
<tr>
	<td class="time">{{transFormatDate addTime}}<i></i></td>
	<td><em>{{{userName}}}</em>借款人</td>
	<td class="bidName mark"><em>{{{logBorrowNameFun name id addTime}}}</em>标种标题</td>
	<td><em>{{borrowTypeName type}}</em>类型</td>
	<td><em>{{moneyFormat account}}元</em>金额</td>
	<td><em>{{apr}}%</em>年利率</td>
	<td><em>{{{timeLimitFormat type timeLimit borrowTimeType}}}</em>期限</td>
	<td width="116"><em><span class="scalesBar"><span style="width:{{scales}}%;"></span></span>{{scales}}%</em>进度</td>
	<td><em>{{borrowMineStatusFun status scales type flow}}</em>状态</td>
	<td><em>{{guaranteeOperateFun id}}</em>操作</td>
	<td><em>{{{protocolBorrowerOperateFun status id}}}</em>协议</td>
	<td><em>{{moneyFormat guaranteeFee}}元</em>分润金额</td>
</tr>
{{/each}}