{{#each data.list}}
<tr>
	<td class="time">{{dateFormat repaymentTime}}<i></i></td>
	<td class="bidName mark" style="display:none;">产品名称<em>{{{logBorrowTypeFun borrowName borrowId addTime borrow.type flagId}}}</em></td>
	<td class="bidName mark" >产品名称<em>{{{logBorrow1Fun borrowName borrowId addTime borrow.type flagId}}}</em></td>
	<td>第几期/总期数<em>{{investPeriodFun borrowStyle period timeLimit}}</em></td>
	<td>收款总额<em>{{moneyFormat1 repaymentAccount}}元</em></td>
	<td>应收本金<em>{{moneyFormat1 capital}}元</em></td>
	<td>实得利息<em>{{moneyFormat1 actualInterest}}元</em></td>
	<td>交易状态<em>{{transStatusFun status}}</em></td>
	<td class="mark"><em><u>
		<div class="mark_details_r w176"><i></i>
			<ul>
				<li class="clearfix"><label for="">应收利息：</label><span>{{moneyFormat1 interest}}元</span></li>
				<li class="clearfix"><label for="">加息券利息：</label><span>{{moneyFormat1 interestRate}}元</span></li>
				<li class="clearfix"><label for="">逾期利息：</label><span>{{moneyFormat1 lateInterest}}元</span></li>
				<li class="clearfix"><label for="">逾期天数：</label><span>{{lateDays}}天</span></li>
			</ul>
		</div>
	</u></em>备注</td>
	
</tr>
{{/each}}