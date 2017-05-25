{{#each data.list}}	
<div class="show_detail" style="height:35px;line-height:35px;padding-top:5px;">
	<div class="col w30">
		<div class="list-row overflow">
			{{dateFormat addTime}}
		</div>
	</div>
	<div class="col w40">
		<div class="list-row overflow">{{typeName}}</div>
	</div>
	<div class="col w30">
		<div class="list-row list-ltr">
			{{moneyFormat1 money}}
		</div>
	</div>
</div>
{{/each}}