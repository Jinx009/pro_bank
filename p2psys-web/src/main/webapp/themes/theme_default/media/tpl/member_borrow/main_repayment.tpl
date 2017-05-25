<thead>
	<tr>
		<th>标的名称</th>
		<th>金额</th>
		<th>年利率</th>
		<th>期限</th>
		<th>发布时间</th>
		<th>还款进度</th>
		<th>状态</th>
	</tr>
</thead>
<tbody>
{{#each businessRepaymentList}}
	<tr>
		<td class="bidName mark"><em>{{{mainRepaymentNameFun name id}}}</em></td>
		<td>{{moneyFormat account}}元</td>
		<td>{{apr}}%</td>
		<td>{{{timeLimitFormat type timeLimit borrowTimeType}}}</td>
		<td>{{transFormatDate addTime}}</td>
		<td>{{mainRepayPeriodFun style borrowTimeType timeLimit currPeriod}}</td>
		<td class="mark">还款中</td>
	</tr>
{{/each}}
</tbody>
<tfoot>
	<tr>
		<td colspan="7"><a href="/member_borrow/borrow/repayment.html">查看所有还款信息&gt;&gt;</a></td>
	</tr>
</tfoot>
