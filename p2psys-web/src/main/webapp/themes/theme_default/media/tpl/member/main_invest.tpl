<thead>
	<tr>
		<th>产品名称</th>
		<th>投资规模</th>
		<th>预期收益</th>
		<th>投资期限</th>
		<th>发布时间</th>
		<th>进度</th>
		<th>操作</th>
	</tr>
</thead>
<tbody>
{{#each list}}
	<tr>		
		<td class="bidName mark" style="display:none;"><em>{{{logBorrowNameFun name id addTime typeStr}}}</em></td>
		<td class="bidName"><em>{{{logBorrowFun name id addTime typeStr flagId}}}</em></td>
		<td>{{moneyFormat1 account}}元</td>
		<td>{{{apr}}}%</td>
		<td>{{{timeLimitFormat type timeLimit borrowTimeType}}}</td>
		<td>{{startTime}}</td>
		<td><em><span class="scalesBar"><span style="width:{{scales}}%;"></span></span>{{scales}}%</em></td>
		<td>
			{{#if fixedTime}}
				<div class="yomibox" data="{{transFormatDate fixedTime}}"/>
			{{/if}}

			<a {{#if fixedTime}}style="display:none"{{/if}} href="/invest/detail.html?id={{id}}&startTime={{addTime}}" title="{{memberInvestBtn scales}}">{{memberInvestBtn scales}}</a>

		</td>
	</tr>
{{/each}}
</tbody>
<tfoot>
	<tr>
		<td colspan="7"><a href="/invest/index.html" title="查看更多招标中的项目" id="flag">查看更多招标中的项目</a></td>
	</tr>
</tfoot>
