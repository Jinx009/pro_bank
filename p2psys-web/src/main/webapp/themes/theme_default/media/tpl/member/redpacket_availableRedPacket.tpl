{{#each data.list}}
{{#less3 expiredTime}}
	
{{else}}

	{{#equal redPacketType 1}}
	<li class="dh">
	   <form action="" id="form1">
		<label for="choiceRedPacket0">
		<p class="redPacketName">{{name}}</p>
	    <p class="redPacketMoneyP">￥<i class="redPacketMoney">{{amount}}</i></p>
	    <p class="expirationTimeP">{{dateFormat expiredTime}}过期</p>
	    <div class="button-bg hide"></div>
	    <input type="botto" name="ids" value="兑现" id="choiceRedPacket-btn" class="hide">
	    <input type="hidden" value="{{id}}" class="hb-id" name="redPacket_id">
	   </form>
	</li>
	{{else}}
	<li class="hh" >
		<label for="choiceRedPacket0">
		<p class="redPacketName">{{name}}</p>
	    <p class="redPacketMoneyP">￥<i class="redPacketMoney">{{amount}}</i></p>
	    {{#equal ../used false}}
	    <p class="expirationTimeP">{{dateFormat expiredTime}}过期</p>
		<div class="button-bg hide"></div>
	    <input type="botto"  value="去投资" id="choiceRedPacket-btn1" class="hide"  >
	    {{else}}
	    <p class="expirationTimeP">红包已被使用</p>
	    {{/equal}}
	    </label>
	</li>
	{{/equal}}

{{/less3}}
{{/each}}