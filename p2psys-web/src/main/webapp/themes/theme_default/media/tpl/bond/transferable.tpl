<thead>
    <tr>
      <td class="bidName">项目名称</td>
      <td>年化收益</td>
      <td class="money">投资金额</td>
      <td>持有期限</td>
      <td>计息起始日</td>
      <td>到期日</td>
      <td>本期回款日</td>
      <td>操作</td>
    </tr>
</thead>
<tbody id="member_table">
{{#each data.list}}
              <tr>
                <td class="bidName">{{borrowName}}</td>
                <td>{{apr}}%</td>
                <td class="money"><i>￥</i>{{moneyFormat tenderMoney}}</td>
                <td>{{holdDays}}天</td>
                <td>{{timeMonthFormat reviewTime}}</td>
                <td>{{timeMonthFormat lastRepaymentTime}}</td>
                <td>{{timeMonthFormat nextRepaymentTime}}</td>
                <td><a href="javascript:;" class="transferAble" data-discountMin="{{../bondAprL}}" data-discountMax="{{../bondAprH}}" data-value="[{{remainMoney}},{{borrowId}},{{borrowTenderId}},{{kfId}},{{type}}]">转让</a></td>
              </tr>
{{/each}}
</tbody>