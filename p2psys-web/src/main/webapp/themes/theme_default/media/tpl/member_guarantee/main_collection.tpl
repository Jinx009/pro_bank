<thead>
	<tr>
		<th>借款人</th>
		<th>联系方式</th>
		<th>标的名称</th>
		<th>第几期/总期数</th>
		<th>应还本金</th>
		<th>应还利息</th>
		<th>应还总额</th>
		<th>还款日期</th>
		<th>剩余还款天数</th>
	</tr>
</thead>
<tbody>
{{#each data.list}}
	<tr>
		<td>{{userName}}</td>
		<td>{{mobilePhone}}</td>
		<td class="bidName mark"><em>{{{logBorrowNameFun borrow.name borrow.id addTime}}}</em></td>
		<td>{{investPeriodFun borrowStyle period timeLimit}}</td>
		<td>{{capital}}元</td>
		<td>{{interest}}元</td>
		<td>{{repaymentAccount}}元</td>
		<td>{{transGuaranteeDateFormat repaymentTime}}</td>
		<td>{{lastDays}}天</td>
	</tr>
{{/each}}
</tbody>
<!--<tfoot>
	<tr>
		<td colspan="7"><a href="/member_borrow/borrow/mine.html">查看更多借款中的项目&gt;&gt;</a></td>
	</tr>
</tfoot>-->
