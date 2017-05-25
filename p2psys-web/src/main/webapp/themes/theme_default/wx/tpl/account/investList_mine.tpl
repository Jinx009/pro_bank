{{#each data.list}}
{{#equal type 2}}
							{{#equal flag 1}}
								<div class="fix_details_s1">
							 {{else}}
							 	<div class="fix_details_s">
							{{/equal}}
						{{else}}
						    {{#equal isFixedTerm 1}}
								{{#equal flag 1}}
									<div class="fix_details_s1">
								 {{else}}
							 		<div class="fix_details_s">
								{{/equal}}
							{{else}}
								{{#equal isOut 0}}
						    			<div class="fix_details_s">
						   		{{else}}
						     		<div class="fix_details_s1">
						  	 	{{/equal}}
							{{/equal}}
					   	{{/equal}}
 		<dl>
					<dt>
					{{nameFormat1 productName}}
					</dt>
					<dd>
					{{#equal ppfundInType '体验标'}}
						{{moneyFormat1 interestAmount}}元
					{{else}}
					    {{moneyFormat1 money}}元
					{{/equal}}
					</dd>
				</dl>
				<dl>
				   {{#equal rateStatus 1}}
				      {{#equal addRate  0}}
				      	<dt>{{apr}}%</dt>
				      {{else}}
				      	<dt>{{apr}}%+{{addRate}}%</dt>
				      {{/equal}}
				   {{else}}
				   <dt>{{apr}}%</dt>
				   {{/equal}}
					<dd>{{transFormatDate addTime}}</dd>
				</dl>
				<dl>
					<dt>
					{{#equal expectProfit 0}}
					
					{{else}}
						  {{#equal isFixedTerm 1}}
						      {{moneyFormat1 expectProfit}}元
						  {{else}}
						    {{#equal isOut 0}}
						      {{moneyFormat1 expectProfit}}元
						   {{else}}
						    
						   {{/equal}}
						  {{/equal}}
					{{/equal}}
					</dt>
					<dd class="redeemCash">
					{{#equal type 2}}
						{{expirationDate}}
					{{else}}
						{{#equal isFixedTerm 1}}
						   {{expirationDate}}
						{{else}}
						   {{#equal isOut 0}}
						    <a class="redeem" id="{{id}}" vtext=" {{money}}" >赎回</a><input type="hidden"  value="/nb/wechat/account/doPpfundOut.html?id={{id}}" class="redeemId">
						   {{else}}
						     已赎回
						   {{/equal}}
						{{/equal}}
					{{/equal}}
					</dd>
				</dl>
				<dl>
					<dt>
						{{#equal type 2}}
							{{#equal flag 1}}
								已还款
							{{/equal}}
						{{else}}
						    {{#equal isFixedTerm 1}}
								{{#equal flag 1}}
									已还款
								{{/equal}}
							{{/equal}}
					   	{{/equal}}
					</dt>
					<dd class="invest_color">
					  {{#equal typeId 199}}
					  	<a href='/nb/wechat/account/protocol.html?type=borrow&id={{id}}' >查看</a>
					  {{else}}
						  {{#equal type 2}}
								<a href='/nb/wechat/account/protocol.html?type=borrow&id={{id}}' >查看</a>
							{{else}}
								<a href='/nb/wechat/account/protocol.html?type=ppfund&id={{id}}' >查看</a>
						   {{/equal}}
					  {{/equal}}
						
					</dd>
				</dl>
	</div>

{{/each}}