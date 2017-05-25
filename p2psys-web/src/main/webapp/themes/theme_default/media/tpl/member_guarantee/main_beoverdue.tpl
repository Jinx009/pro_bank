<thead>
	<tr>
		<th>借款人</th>
		<th>标的名称</th>
		<th>期数</th>
		<th>预还金额</th>
		<th>已还金额</th>
		<th>本金</th>
		<th>利息</th>
		<th>滞纳金</th>
		<th>逾期天数</th>
		<th>操作</th>
	</tr>
</thead>
<tbody>
{{#each list.list}}
	<tr>
		<td>{{userName}}</td>
		<td class="bidName mark"><em>{{{logBorrowNameFun borrowName borrowId addTime}}}</em></td>
		<td>{{period}}</td>
		<td>{{moneyFormat repaymentAccount}}元</td>
		<td>{{moneyFormat repaymentYesAccount}}元</td>
		<td>{{moneyFormat capital}}元</td>
		<td>{{moneyFormat interest}}元</td>
		<td>{{moneyFormat forfeit}}元</td>
		<td>{{lateDays}}</td>
		<td><a href="javascript:;" title="代偿">代偿</a></td>
	</tr>
{{/each}}
</tbody>
<!--<tfoot>
	<tr>
		<td colspan="7"><a href="/member_borrow/borrow/mine.html">查看更多借款中的项目&gt;&gt;</a></td>
	</tr>
</tfoot>-->
