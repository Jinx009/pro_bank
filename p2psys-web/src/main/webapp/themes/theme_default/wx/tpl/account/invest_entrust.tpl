{{#each data.list}}
{{#equal flag 0}}
		<div class="fix_details_s">
	{{else}}
		<div class="fix_details_s1">
{{/equal}}

         <dl>
		    <dt>{{borrowName}}</dt>
			<dd>{{moneyFormat1 account}}元</dd>
		</dl>
		<dl>
			<dt>{{expectedLow}}%~{{expectedUp}}%</dt>
			<dd>{{transFormatDate addTime}}</dd>
		</dl>
		<dl>
			<dt>
			{{#equal  flag 0}}
			/
			{{else}}
			{{moneyFormat1 expectProfit}}元
			{{/equal}}
			</dt>
			<dd>
				 {{expirationDate}}
			</dd>
		</dl>
		<dl>
			<dt>
			{{#equal  flag 0}}
				
			{{else}}
				已还款
			{{/equal}}
			</dt>
			<dd>
				<a href='/nb/wechat/account/protocol.html?type=borrow&id={{id}}'>查看</a>
			</dd>
		</dl>
</div>


{{/each}}