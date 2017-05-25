{{#each data.list}}
	<tr>
		<td class="time">{{addTimeStr}}<i></i></td>
		<td>提现银行<em>{{bank}}</em></td>
		<td>提现账号<em>{{hideAccount}}</em></td>
		<td>提现金额<b>{{moneyFormat1 money}}元</b></td>
		<td>到账金额<b>{{moneyFormat1 credited}}元</b></td>
		<td>手续费<b>{{cashFee fee cashFeeBear}}元</b></td>
		<td>状态<em>{{statusStr}}</em></td>
	</tr>	
{{/each}}