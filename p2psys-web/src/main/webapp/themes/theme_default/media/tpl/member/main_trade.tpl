<thead>
	<tr>
		<th>交易时间</th>
		<th>类型</th>
		<th>资金金额</th>
		<th>备注信息</th>
	</tr>
</thead>
<tbody>
{{#each accountLogList}}
<tr>
	<td>{{transFormatDate addTime}}</td>
	<td>{{typeName}}</td>
	<td>{{moneyFormat1 money}}</td>
	<td style="display:none;">{{{mainremarkFunction remark}}}</td>
	<td >{{{maindoremarkFunction doremark}}}</td>
</tr>	
{{/each}}
</tbody>
<tfoot>
	<tr>
		<td colspan="5"><a href="/member/account/log.html" title="查看所有交易记录">查看所有交易记录</a></td>
	</tr>
</tfoot>