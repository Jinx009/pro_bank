<tr>
	<th>序号</th>
	<th>投标人</th>
	<th>投标金额</th>
	<th>有效金额</th>
	<th>投标类型</th>
	<th>投标时间</th>
</tr>
{{#each data.list}}
<tr class="firsttr">
	<td>{{addNum @index}}</td>
	<td>{{userName}}</td>
	<td>{{money}}</td>
	<td>{{account}}</td>
	<td>{{isAutoTender}}</td>
	<td>{{addTime}}</td>
</tr>
{{/each}}
