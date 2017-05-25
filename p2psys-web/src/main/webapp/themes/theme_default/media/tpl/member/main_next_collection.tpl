<span class="title">下个待收日期</span>
{{#with borrowCollection}}
{{#if nextCollectTime}}
  <div class="date-receipt-con clearfix">
      <div class="date-receipt-left">
         <span class="date-receipt-top">
           {{repaymentMonth nextCollectTime}}
         </span>
         <span class="date-receipt-down">
           {{repaymentDay nextCollectTime}}
         </span>
      </div>
       <div class="date-receipt-right">
          <span>待收项目：<br/><b>{{nextCollextCount}}</b>个</span>
          <span>待收金额：<br/><b>{{nextCollextAccount}}</b>元</span>
       </div>
  </div>
  {{else}}
    <dd class="noCalendar">暂无待收信息</dd>
  {{/if}}
  {{/with}}
