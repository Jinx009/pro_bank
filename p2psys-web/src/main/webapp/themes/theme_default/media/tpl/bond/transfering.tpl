<thead>
    <tr>
      <td>债权价值</td>
      <td>转让折让率</td>
      <td>转让价格</td>
      <td>转让申请时间</td>
      <td>转让进度</td>
      <td>年化收益</td>
      <td>本期回款日</td>
      <td>操作</td>
    </tr>
</thead>
<tbody id="member_table">
{{#each data.list}}
          <tr>
            <td class="money"><i>￥</i>{{moneyFormat bondMoney}}</td>
            <td>{{bondApr}}%</td>
            <td class="money"><i>￥</i>{{transferVal bondMoney bondApr}}</td>
            <td>{{timeMonthFormat addTime}}</td>
            <td><span class="progress w80"><em style="width:{{progress soldCapital bondMoney}}%"></em></span>{{progress soldCapital bondMoney}}%</td>
            <td>{{apr}}%</td>
            <td>{{timeMonthFormat nextRepaymentTime}}</td>
            <td><a href="javascript:;" data-val="{{id}}" class="revocation">撤回</a> | <a href="javascript:;" class="transferingDetail" data-val="{{id}}">详情</a></td>
          </tr>
{{/each}}
</tbody>