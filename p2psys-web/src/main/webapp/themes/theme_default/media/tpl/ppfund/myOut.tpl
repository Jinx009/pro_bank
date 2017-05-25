<thead>
    <tr>
      <td class="bidName">产品名称</td>
      <td>年化收益</td>
      <td class="money">转出金额</td>
      <td>利息收益</td>
      <td>转出时间</td>
    </tr>
</thead>
<tbody id="member_table">
{{#each data.list}}
              <tr>
                <td class="bidName">{{ppfundName}}</td>
                <td>{{ppfundApr}}%</td>
                <td class="money"><i>￥</i>{{moneyFormat1 money}}</td>
                <td>{{moneyFormat1 interestTotal}}元</td>
                <td>{{timeMonthFormat addTime}}</td>
              </tr>
{{/each}}
</tbody>
