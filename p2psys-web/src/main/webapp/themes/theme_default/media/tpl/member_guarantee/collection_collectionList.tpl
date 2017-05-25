{{#each data.list}}
<tr>
	<td class="time">{{transFormatDate addTime}}<i></i></td>
	<td><em>{{{userName}}}</em>借款人</td>
	<td><em>{{{mobilePhone}}}</em>联系方式</td>
	<td class="bidName mark"><em>{{{logBorrowNameFun borrow.name borrow.id addTime}}}</em>标种标题</td>
	<td><em>{{borrowTypeName borrow.type}}</em>类型</td>
	<td><em>{{moneyFormat borrow.account}}元</em>金额</td>
	<td><em>{{borrow.apr}}%</em>年利率</td>
	<td><em>{{{timeLimitFormat borrow.type borrow.timeLimit borrow.borrowTimeType}}}</em>期限</td>
	<td><em>{{investPeriodFun borrowStyle period timeLimit}}</em>第几期/总期数</td>
	<td><em>{{transGuaranteeDateFormat repaymentTime}}</em>还款日期</td>
	<td><em>{{lastDays}}天</em>剩余天数</td>
	<td><em>{{guaranteeOperateFun borrow.id}}</em>操作</td>
</tr>
{{/each}}