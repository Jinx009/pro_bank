{{#each data.list}}
<tr>
	<td class="time">{{dateFormat addTime}}<i></i></td>
	<td>类型<em>{{typeName}}</em></td>
	
	<td>资金金额<em>{{moneyFormat1 money}}</em></td>
	<td class="mark">备注信息<em><u>
		<div class="mark_details_r"><i></i>
			{{{remark}}}
		</div>
	</u></em></td>
</tr>	
{{/each}}