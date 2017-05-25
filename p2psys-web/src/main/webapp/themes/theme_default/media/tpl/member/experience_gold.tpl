{{#each data.list}}
{{#less3 expiredTime}}
	
{{else}}

	{{#equal status 0}}
	<li class="dh">
	   <form action="" id="form1">
		<label for="choiceRedPacket0">
		<p class="redPacketName">{{name}}</p>
	    <p class="redPacketMoneyP">￥<i class="redPacketMoney">{{money}}</i></p>
	    <p class="expirationTimeP">投资有效天：{{days}}天</p>
	    <div class="button-bg hide"></div>
	    <input type="botto" name="ids" value="投资" id="choiceRedPacket-btn" class="hide">
	    <input type="hidden" value="{{id}}" class="hb-id" name="redPacket_id">
	   </form>
	</li>
	{{else}}
	<li>
		<label for="choiceRedPacket0">
		<p class="redPacketName">{{name}}</p>
	    <p class="redPacketMoneyP">￥<i class="redPacketMoney">{{money}}</i></p>
	    <p class="expirationTimeP">体验金已被使用</p>
	    </label>
	</li>
	{{/equal}}

{{/less3}}
{{/each}}