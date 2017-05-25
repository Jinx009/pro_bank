<thead>
    <tr>
      <td class="bidName">产品名称</td>
      <td>年化收益</td>
      <td>投资期限</td>
      <td class="money">投资金额</td>
      <td>是否转出</td>
      <td>利息收益</td>
      <td>本息收益</td>
      <td>购买日期</td>
      <td>操作</td>
    </tr>
</thead>
<tbody id="member_table">
{{#each data.list}}
      <tr>
        <td class="bidName">{{ppfundName}}</td>
        <td>{{ppfundApr}}%</td>
        <td>{{#inequality isFixedTerm 1}}活期{{else}}{{ppfund.timeLimit}}天{{/inequality}}</td>
         {{#equal typeCode '体验标'}}
         <td class="money"><i>￥</i>{{moneyFormat1 interestMoney}}</td>
         {{else}}
         <td class="money"><i>￥</i>{{moneyFormat1 account}}</td>
         {{/equal}}
        
        <td>{{#inequality isOut 0}}是{{else}}否{{/inequality}}</td>
        <td>{{moneyFormat1 interest}}</td>
        <td>{{moneyFormat1 lastInterest}}</td>
        <td>{{timeMonthFormat addTime}}</td>
        <td>{{#inequality isFixedTerm 1}}{{#inequality isOut 1}}<a class="zc">转出</a><input type="hidden" value="/ppfund/doPpfundOut.html?id={{id}}" class="zcid"><span class="id" style="display:none">{{id}}</span>{{/inequality}}{{/inequality}} <a href="/member/invest/inProtocol.html?inId={{id}}">协议</a></td>
      </tr>
{{/each}}
</tbody>