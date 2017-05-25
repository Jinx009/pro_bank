{{#with bond}}
<tr>
  <td><a href="/invest/detail.html?id={{borrowId}}" target="_blank">{{borrowName}}</a></td>
  <td>{{apr}}%</td>
  <td class="money"><i>￥</i>{{tenderMoney}}</td>
  <td>{{remainDays}}天</td>
  <td>{{timeMonthFormat reviewTime}}</td>
  <td>{{timeMonthFormat lastRepaymentTime}}</td>
  <td>{{transFormatStyle borrowStyle}}</td>
</tr>
{{/with}}
