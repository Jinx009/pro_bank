{{#each data.list}}
<tr>
	<td class="time">{{transFormatDate addTime}}<i></i></td>
	<td><em>借款信用额度</em>申请类型</td>
	<td><em>{{account}}元</em>申请额度</td>
	<td><em>{{accountNewFun accountNew}}元</em>新额度</td>
	<td><em>{{amountStatusFun status}}</em>状态</td>
	<td><em>{{verifyTimeFun verifyTime status}}</em>审核时间</td>
	<td class="mark"><em><u>
		<div class="mark_details_r">
			<ul>
				<li><label for="">申请备注：</label><span>{{content}}</span></li>
				<li><label for="">审核备注：</label><span>{{verifyRemark}}</span></li>
			</ul>
		</div>
	</u></em>备注</td>
</tr>
{{/each}}