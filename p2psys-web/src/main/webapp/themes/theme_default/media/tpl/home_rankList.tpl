{{#each data}}
	<li>
		<span class="Investmentlist-mc">{{addOne @index}}</span>
	 	<span class="Investmentlist-name">{{username}}</span>
	 	<span class="Investmentlist-money">￥{{tenderMoney}}</span>
	</li>	
{{/each}}