<dl class="member_guarantee_account float_left">
    <dt>
      <div class="float_left today_income">
        <label>正在担保项目</label>
        <p>{{guaranteeingCount}}  个</p>
      </div>
      <em class="float_left"></em>
      <div class="float_left">
        <label>待登记项目</label>
        <p>{{needGuaranteeRegisteCount}}  个</p>
      </div>
    </dt>
    <dd>
      <span>正在担保项目金额<em>{{moneyFormat guaranteeingAccount}}元</em></span>
      	待登记项目金额<em>{{moneyFormat needGuaranteeRegisteAccount}}元</em>
    </dd>
  </dl>
<dl class="member_guarantee_item float_right">
	<dt>催收项目<em>{{urgeCount}}</em>个<span><a href="/member_guarantee/collection/collectionList.html">查看详情</a></span></dt>
	<dd>逾期项目<em class="red">{{overdueCount}}</em>个<span><a href="/member_guarantee/overdue/overdueList.html">查看详情</a></span></dd>
</dl>