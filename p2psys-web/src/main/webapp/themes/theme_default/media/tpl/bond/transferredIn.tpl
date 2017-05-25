<thead>
    <tr>
      <td>债权价值</td>
      <td>转让折让率</td>
      <td>转入价格</td>
      <td>应付收益<i class="iconfont"></i></td>
      <td>转入时间</td>
      <td>操作</td>
    </tr>
</thead>
<tbody id="member_table">
{{#each data.list}}
          <tr>
            <td class="money"><i>￥</i>{{moneyFormat tenderMoney}}</td>
            <td>{{bondApr}}%</td>
            <td class="money"><i>￥</i>{{transferVal tenderMoney bondApr}}</td>
            <td class="money"><i>￥</i>{{moneyFormat payInterest}}</td>
            <td>{{timeMonthFormat addTime}}</td>
            <td><a href="/bond/bondBuyProtocol.html?id={{borrowId}}" target="_blank">转让协议</a> | <a href="javascript:;" class="transfeInDetail" data-val="{{id}}">详情</a></td>
          </tr>
{{/each}}
</tbody>