{{#each data.list}}
<tr>
	<td class="time">{{transFormatDate addTime}}<i></i></td>
	<td class="bidName mark" style="display:none;">产品名称<em>{{{logBorrowTypeFun borrowName borrowId addTime borrowType}}}</em></td>
	<td class="bidName mark">产品名称<em>{{{logBorrow1Fun borrowName borrowId addTime borrowType flagId}}}</em></td>
	<td>投资规模<em>{{moneyFormat1 account}}元</em></td>
	<td>投标进度<em><span class="scalesBar"><span style="width:{{scales}}%;"></span></span>{{scales}}%</em></td>
	<td>状态<em>{{transFormatTenderStatus status scales}}</em></td>
	<td class="downBtn">{{{AddTenderProtocol status scales id}}}</td>
</tr>
{{/each}}