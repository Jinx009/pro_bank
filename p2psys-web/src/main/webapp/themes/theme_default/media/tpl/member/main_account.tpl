<dl class="account_income_info float_left">
	<dt>
		<div class="float_left today_income">
			<label><a href="javascript:;" title="您今日可获得的收益" class="textTips"><i class="iconfont">&#xe61c;</i></a>今日收益</label>
			<p>{{moneyFormatNews sumTodayInterest}}元</p>
		</div>
		<em class="float_left"></em>
		<div class="float_left">
			<label><a href="javascript:;" title="您在平台已获得的所有收益" class="textTips"><i class="iconfont">&#xe61c;</i></a>累计净收益</label>
			<p>{{moneyFormat sumInterest}}元</p>
		</div>
	</dt>
	<dd>
		<span><a href="javascript:;" title="您投资的正在还款中项目的金额" class="textTips"><i class="iconfont">&#xe61c;</i></a>在投金额<em>{{moneyFormat sumInMoney}}元</em></span>
		<a href="javascript:;" title="您在平台已投资完成的所有金额" class="textTips"><i class="iconfont">&#xe61c;</i></a>累计投资<em>{{moneyFormat investTotal}}元</em>
	</dd>
</dl>
<dl class="account_data float_right">
	<dt>
		<div class="float_left">
			<label><a href="javascript:;" title="您当前账户的总金额" class="textTips"><i class="iconfont">&#xe61c;</i></a>账户总额</label>
			<p>{{accountBalanceFun account.useMoney account.noUseMoney}}元</p>
		</div>
		<div class="float_right">
			<a href="/member/recharge/newRecharge.html" class="recharge">充值</a>
  			<a href="/member/cash/newCash.html" class="withdraw">提现</a>
		</div>    			
  	</dt>
	<dd>
		<a href="javascript:;" title="您可以用于投标或者提现的金额" class="textTips"><i class="iconfont">&#xe61c;</i></a>可用金额<em>{{moneyFormat account.useMoney}}元</em>
		<span><a href="javascript:;" title="您投资的正在满标项目的金额" class="textTips"><i class="iconfont">&#xe61c;</i></a>冻结金额<em>{{moneyFormat account.noUseMoney}}元</em></span>
	</dd>
</dl>