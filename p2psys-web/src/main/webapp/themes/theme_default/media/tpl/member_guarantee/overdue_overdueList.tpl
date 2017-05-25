{{#each data.list}}
<tr>
	<td class="time">{{transFormatDate addTime}}<i></i></td>
	<td><em>{{{userName}}}</em>借款人</td>
	<td><em>{{{mobilePhone}}}</em>联系方式</td>
	<td class="bidName mark"><em>{{{logBorrowNameFun borrowName borrowId addTime}}}</em>标种标题</td>
	<td><em>{{borrowTypeName type}}</em>类型</td>
	<td><em>{{moneyFormat account}}元</em>金额</td>
	<td><em>{{apr}}%</em>年利率</td>
	<td><em>{{{timeLimitFormat type timeLimit borrowTimeType}}}</em>期限</td>
	<td><em>{{investPeriodFun borrowStyle period timeLimit}}</em>第几期/总期数</td>
	<td><em>{{guaranteeOperateFun borrowId}}</em>操作</td>
	<td class="mark"><em><u>
		<div class="mark_details_r w176"><i></i>
			<p><label>应还日期：</label><span>{{transGuaranteeDateFormat repaymentTime}}</span></p>
			<p><label>应还本金：</label><span>{{capital}}元</span></p>
			<p><label>应还利息：</label><span>{{interest}}元</span></p>
			<p><label>逾期天数：</label><span>{{lateDays}}天</span></p>
			<p><label>逾期罚息：</label><span>{{lateInterest}}元</span></p>
			<p><label>应还总额：</label><span>{{repaymentAccount}}元</span></p>
		</div>
	</u></em>备注</td>
</tr>
{{/each}}