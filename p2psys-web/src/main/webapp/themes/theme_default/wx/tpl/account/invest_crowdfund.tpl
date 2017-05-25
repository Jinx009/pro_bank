{{#each data.list}}
<div class="fix_details">
<dl>
		<dt class="test">	{{projectName}}</dt>
					<dd>{{moneyFormat1 money}}元</dd>
				</dl>
				<dl>
					<dt>{{moneyFormat apr}}%</dt>
					<dd>{{transFormatDate addTime}}</dd>
				</dl>
				<dl>
					<dt></dt>
					<dd>
					  {{expirationDate}}
					</dd>
				</dl>
</div>


{{/each}}