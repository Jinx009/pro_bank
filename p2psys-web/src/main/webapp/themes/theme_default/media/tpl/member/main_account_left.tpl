 <div class="account-left-con1">
           <div class="account-left-con1top">
            <span>账户总额<em>￥{{moneyFormat1 account.total}}</em></span>
            <p><a href="/member/invest/mine.html" target="_blank">查看投资详情</a><!--<a href="/member/auto/detail.html">设置自动投标</a>--></p>
           </div>
           <div class="account-left-con1down">
            <a href="/member/recharge/newRecharge.html" class="cz-btn">充值</a>
            <a href="/member/cash/newCash.html" class="tx-btn">提现</a>
           </div>
        </div>
        <div class="account-left-con2" id="moneychart">
        </div>
        <div class="account-left-con3">
          <div class="account-left-con3top">
            <span>可用金额<em>￥{{moneyFormat1 useMoney}}</em></span>
            <p><a href="/member/account/log.html">查看资金记录</a></p>
           </div>
           <div class="account-left-con3down">
            <span>冻结金额<em>￥{{moneyFormat1 account.noUseMoney}}</em></span>
            <p><a href="/member/invest/myInvest.html">查看投标项目</a></p>
           </div>
            <div class="account-left-con3top">
            <span>待收金额<em>￥{{moneyFormat1 account.collection}}</em></span>
            <p><a href="/member/invest/collection.html">查看待收项目</a></p>
           </div>
</div>
