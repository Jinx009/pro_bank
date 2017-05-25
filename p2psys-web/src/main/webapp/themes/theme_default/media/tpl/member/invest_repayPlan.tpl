{{#each data.list}}
<tr>
	<td class="bidName">{{investBorrowNameFun borrowName borrowId}}</td>
	<td>{{apr}}%</td>
	<td class="money"><i>￥</i>{{moneyFormat1 totalCapital}}</td>
	<td>{{investTimeFun startDate}}</td>

	<td>{{investTimeFun repaymentTime}}</td>
	<td class="money"><i>￥</i>{{transMoneySub capital bondCapital}}</td>
	<td class="money"><i>￥</i>{{transMoneySub interest bondInterest}}</td>

	<td>{{repayPeriodFun borrowStyle borrowTimeType timeLimit period}}</td>
</tr>
{{/each}}
