{{#each data.list}}
<tr>
	<td><em>{{transGuaranteeDateFormat repaymentYesTime}}</em>代偿日期</td>
	<td><em>{{{userName}}}</em>借款人</td>
	<td class="time">{{transFormatDate addTime}}<i></i></td>
	<td class="bidName mark"><em>{{{logBorrowNameFun borrowName borrowId addTime}}}</em>标种标题</td>
	<td><em>{{borrowTypeName type}}</em>类型</td>
	<td><em>{{moneyFormat account}}元</em>金额</td>
	<td><em>{{apr}}%</em>年利率</td>
	<td><em>{{{timeLimitFormat type timeLimit borrowTimeType}}}</em>期限</td>
	<td><em>{{investPeriodFun borrowStyle period timeLimit}}</em>第几期/总期数</td>
	<td><em>{{transGuaranteeDateFormat repaymentTime}}</em>应还日期</td>
	<td><em>{{capital}}天</em>代偿本金</td>
	<td><em>{{interest}}天</em>代偿利息</td>
	<td><em>{{lateDays}}天</em>逾期天数</td>
	<td><em>{{lateInterest}}</em>代偿罚息</td>
	<td><em>{{repaymentYesAccount}}</em>代偿总额</td>
</tr>
{{/each}}