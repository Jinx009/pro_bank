<thead>
	<tr>
		<th>产品名称</th>
		<th>借款人</th>
		<th>第几期/总期数</th>
		<th>收款总额</th>
		<th>应收本金</th>
		<th>实得利息</th>
		<th>备注</th>
	</tr>
</thead>
<tbody>
{{#each data}}
	<tr>
		<td class="bidName mark"><em>{{hideBorrowName borrowName}}</em></td>
		<td>{{userName}}</td>
		<td>{{investPeriodFun borrowStyle period timeLimit}}</td>
		<td>{{moneyFormat1 repaymentAccount}}元</td>
		<td>{{moneyFormat1 capital}}元</td>
		<td>{{moneyFormat1 actualInterest}}元</td>
		<td class="mark"><em><u>
		<div class="mark_details_r w176"><i></i>
			<p><label>应收利息：</label><span>{{moneyFormat1 interest}}元</span></p>
			<p><label>逾期利息：</label><span>{{moneyFormat1 lateInterest}}元</span></p>
			<p><label>逾期天数：</label><span>{{lateDays}}天</span></p>
		</div>
	</u></em>备注</td>
	</tr>
{{/each}}
</tbody>
<tfoot>
	<tr>
		<td colspan="7"><a href="/member/invest/collection.html" title="查看所有收款信息">查看所有收款信息</a></td>
	</tr>
</tfoot>
