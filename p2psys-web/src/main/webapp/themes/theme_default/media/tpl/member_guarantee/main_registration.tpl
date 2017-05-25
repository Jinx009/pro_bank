<thead>
	<tr>
		<th>标的名称</th>
		<th>借款人</th>
		<th>金额</th>
		<th>年利率</th>
		<th>期限</th>
		<th>发布时间</th>
		<th>操作</th>
	</tr>
</thead>
<tbody>
{{#each data}}
<tr>
	<td>{{name}}</td>
	<td>{{userName}}</td>
	<td>{{moneyFormat account}}元</td>
	<td>{{{apr}}}%</td>
	<td>{{{timeLimitFormat type timeLimit borrowTimeType}}}</td>
	<td>{{transFormatDate addTime}}</td>
	<td>{{guaranteeRegFun id}}</td>
</tr>	
{{/each}}
</tbody>