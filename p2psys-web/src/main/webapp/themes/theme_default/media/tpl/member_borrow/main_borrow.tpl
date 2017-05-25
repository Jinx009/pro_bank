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
{{#each businessBidList}}
	<tr>
		<td class="bidName mark"><em>{{{logBorrowNameFun name id addTime}}}</em></td>
		<td>{{moneyFormat account}}元</td>
		<td>{{{apr}}}%</td>
		<td>{{{timeLimitFormat type timeLimit borrowTimeType}}}</td>
		<td>{{transFormatDate addTime}}</td>
		<td><em><span class="scalesBar"><span style="width:{{parseInt scales}}%;"></span></span>{{scales}}%</em></td>
		<td>{{logBorrowStatusFun status scales type flow}}</td>
	</tr>
{{/each}}
</tbody>
<tfoot>
	<tr>
		<td colspan="7"><a href="/member_borrow/borrow/mine.html">查看更多借款中的项目&gt;&gt;</a></td>
	</tr>
</tfoot>
