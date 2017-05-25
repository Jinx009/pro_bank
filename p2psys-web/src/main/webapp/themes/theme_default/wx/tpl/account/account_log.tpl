{{#each data.list}}	
<div class="money_details_list">
<div class="test">

				<p>{{noticeDateFormat addTime}}</p>
				<p>{{typeName}}</p>
				<p>{{moneyFormat1 money}}</p>
</div>
<div class="mark_details">
{{{remark}}}
</div>
</div>
{{/each}}