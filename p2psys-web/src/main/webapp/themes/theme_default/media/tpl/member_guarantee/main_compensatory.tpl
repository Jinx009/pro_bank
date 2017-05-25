<thead>
	<tr>
		<th>标的名称</th>
		<th>金额</th>
		<th>年利率</th>
		<th>期限</th>
		<th>发布时间</th>
		<th>进度</th>
		<th>状态</th>
	</tr>
</thead>
<tbody>
{{#each businessRepaymentList}}
	<tr>
		<td class="bidName mark"><em>{{{logBorrowNameFun name id addTime}}}</em></td>
		<td>{{account}}元</td>
		<td>{{apr}}%</td>
		<td>{{logBorrowTimeLimitFun type isDay timeLimitDay timeLimit}}</td>
		<td>{{transFormatDate addTime}}</td>
		<td><span class="scalesBar"><span style="width:{{slideBar accountYes account}}%;"></span></span>{{slideBar accountYes account}}%</td>
		<td class="mark"><em></em>备注</td>
	</tr>
{{/each}}
</tbody>
<!--<tfoot>
	<tr>
		<td colspan="7"><a href="/member_borrow/borrow/repayment.html">查看所有已代偿项目&gt;&gt;</a></td>
	</tr>
</tfoot>-->
