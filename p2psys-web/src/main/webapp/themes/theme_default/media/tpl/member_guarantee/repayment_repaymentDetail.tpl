{{#each data.list}}
<tr>
	<td class="time">{{transFormatDate addTime}}<i></i></td>
	<td class="bidName mark"><em>{{{logBorrowNameFun borrowName borrowId addTime}}}</em>标种标题</td>
	<td><em>{{investPeriodFun borrowStyle period timeLimit}}</em>第几期/总期数</td>
	<td><em>{{repaymentAccount}}</em>本期应还本息</td>
	<td><em>{{interest}}元</em>利息</td>
	<td><em>{{guaranteeRepaymentStatusFun status}}</em>还款状态</td>
	<td><em>{{guaranteeRepaymentLateDaysFun lateDays}}天</em>剩余天数</td>
	<td><em>{{guaranteeCompensatoryOperateFun id status}}</em>操作</td>
</tr>
{{/each}}