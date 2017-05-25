<thead>
	<tr>
		<th>记录时间</th>
		<th>类型</th>
		<th>交易对方</th>
		<th>操作金额</th>
		<th>备注信息</th>
	</tr>
</thead>
<tbody>
{{#each accountLogList}}
<tr>
	<td>{{transFormatDate addTime}}</td>
	<td>{{typeName}}</td>
	<td>{{hideToUserName}}</td>
	<td>{{moneyFormat money}}</td>
	<td>{{{mainremarkFun remark}}}</td>
</tr>	
{{/each}}
</tbody>
<tfoot>
	<tr>
		<td colspan="5"><a href="/member_borrow/account/log.html">查看所有交易记录&gt;&gt;</a></td>
	</tr>
</tfoot>