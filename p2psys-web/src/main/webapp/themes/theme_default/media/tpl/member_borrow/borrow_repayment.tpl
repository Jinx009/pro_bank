{{#each data.list}}
<tr class="{{#less repaymentTime}}expect{{/less}}">
	<td  class="bidName">{{{logBorrowNameFun borrowName borrowId addTime}}}</td>
	<td>{{repayPeriodFun borrowStyle borrowTimeType timeLimit period}}</td>
	<td>{{dateFormat repaymentTime}}</td>
	<td class="money"><i>￥</i>{{repaymentAccount}}</td>
	<td class="money"><i>￥</i>{{interest}}</td>
	<td>{{repayStatusFun status}}</td>
	<td>{{{repayOpearteFun status id ../api_code ../is_open_deposit repaymentAccount lateInterest ../account/useMoney}}}</td>
</tr>
{{/each}}