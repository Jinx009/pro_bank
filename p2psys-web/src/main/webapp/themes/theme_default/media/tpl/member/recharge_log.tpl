{{#each data.list}}
	<tr>
		<td class="time">{{addTimeStr}}<i></i></td>
		<td>支付方式<em>{{typeStr}}</em></td>
		<td>充值金额<b>{{moneyFormat1 money}}元</b></td>
		<td>实际到账金额<b>{{moneyFormat1 amountIn}}元</b></td>
		<td>状态<em>{{statusStr}}</em></td>
		<td class="mark">充值备注<em><u>
			<div class="mark_details_r"><i></i>
				<ul>
					<li class="clearfix"><em>{{remark}}</em></li>
				</ul>
			</div>
		</u></em></td>
	</tr>	
{{/each}}