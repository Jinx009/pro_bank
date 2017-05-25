<thead>
    <tr height="30px"></tr>
    <tr>
       <th  class="bidName">被推荐人手机</th>
      <th>投资项目名称</th>
      <th>推荐收益</th>
      <th>收益日期</th>
    </tr>
</thead>
<tbody id="member_table">
{{#each data.list}}
      <tr>
         <td style="text-align: center" class="bidName">{{mobilePhone}}</td>
        <td style="text-align: center" class="bidName">{{projectName}}</td>
        <td style="text-align: center">{{money}}</td>
        <td style="text-align: center">{{transFormatDate addTime}}</td>
      </tr>
{{/each}}
</tbody>