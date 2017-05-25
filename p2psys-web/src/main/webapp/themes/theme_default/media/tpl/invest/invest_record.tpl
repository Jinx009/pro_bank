{{#each data.list}}
  <tr>
    <td class="order">{{addOne @index}}</td>
    <td>{{userName}}</td>
     {{#equal typeCode '体验标'}}
           <td>￥{{interestMoney}}</td>
     {{else}}
           <td>￥{{money}}</td>
     {{/equal}}
    <td>{{transFormatDate addTime}}</td>
    <td>{{transFormatTenderType 0}}</td>
    <td><i class="icon success"></i></td>
  </tr>
{{/each}}