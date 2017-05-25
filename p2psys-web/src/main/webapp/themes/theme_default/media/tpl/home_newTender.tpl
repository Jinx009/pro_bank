{{#each data}}
	<li>{{countdownDate tenderTime nowTime}}：{{userName}} 投资 {{newTender borrowName}} <br/><span title="{{borrowName}}">金额 {{tenderMoney}}元</span></li>
{{/each}}
