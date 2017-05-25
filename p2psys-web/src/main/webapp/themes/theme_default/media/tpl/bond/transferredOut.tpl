<thead>
    <tr>
      <td class="money">债权价值</td>
      <td>转让折让率</td>
      <td>转让价格</td>
      <td>应收收益<i class="iconfont"></i></td>
      <td>手续费</td>
      <td>赎回金额</td>
      <td>转让成功时间</td>
      <td>操作</td>
    </tr>
</thead>
<tbody id="member_table">
{{#each data.list}}
          <tr>
            <td class="money"><i>￥</i>{{moneyFormat bondMoney}}</td>
            <td>{{bondApr}}%</td>
            <td class="money"><i>￥</i>{{transferVal bondMoney bondApr}}</td>
            <td class="money"><i>￥</i>{{moneyFormat payInterest}}</td>
            <td class="money"><i>￥</i>{{moneyFormat manageFee}}</td>
            <td class="money"><i>￥</i>{{redemption bondMoney bondApr payInterest manageFee}}</td>
            <td>{{transFormatDate addTime}}</td>
            <td><a href="/bond/bondSellProtocol.html?id={{borrowId}}" target="_blank">下载协议</a> | <a href="javascript:;" class="transferingDetail" data-val="{{id}}">详情</a></td>
          </tr>
{{/each}}
</tbody>