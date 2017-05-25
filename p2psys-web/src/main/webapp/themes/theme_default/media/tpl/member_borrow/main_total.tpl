<div class="left clearfix">
    <dl class="balance">
      <dt>可提现余额</dt>
      <dd><em>{{moneyFormat useMoney}}</em>元</dd>
     <dd class="balance_btn">
     	<a href="/member/recharge/newRecharge.html?borrow=1" class="withhold">充值</a>
        <a href="/member/cash/newCash.html?borrow=1" class="withdraw">提现</a>
     </dd>
     <dt>总信用额度：<em>{{userCreditTotal}}</em>元</dt>
      <dt>可用信用额度：<em>{{userCreditUseMoney}}</em>元</dt>
    </dl>
    <div class="borrow_money">
      <dl>
        <dt>正在借款项目</dt>
        <dd><em>{{count}}</em>个</dd>
      </dl>
      <dl class="money">
        <dt>正在借款金额</dt>
        <dd><em>{{moneyFormat total}}</em>元</dd>
      </dl>
      <p><a href="/member_borrow/borrow/mine.html{{mineLink count}}">查看更多借款项目</a></p>
    </div>
  </div>
  <dl class="right">
    <dt><a href="/member_borrow/borrow/repayment.html">待还详情&gt;&gt;</a>下一个待还日期</dt>
    {{#with borrowRepay}}
    {{#if nextRepayTime}}
    <dd class="calendar clearfix">
      <div class="time">
        <p class="month">{{repaymentMonth nextRepayTime}}</p>
        <p class="day">{{repaymentDay nextRepayTime}}</p>
      </div>
      <ul>
      <li>待还项目<em>{{nextRepayCount}}</em>个</li>
      <li>待还金额<em>{{moneyFormat nextRepayAccount}}</em>元</li>
      <li><a href="{{borrowHref nextRepayTime}}">还款</a></li>
      </ul>
      </dd>
    {{else}}
    <dd class="noCalendar">暂无待还信息</dd>
    {{/if}}
    {{/with}}
    <dd class="total">
        <span>待还总额<em>{{moneyFormat repaymentTotal}}</em>元</span>
        <span>待还总项目<em>{{repaymentCount}}</em>个</span>
    </dd>
  </dl>