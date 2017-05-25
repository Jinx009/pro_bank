{{#each data.list}}
<tr>
	<td class="time">{{timeFormat goods.addTime}}<i></i></td>
	<td class="shopName"><em><a href="/scoreMall/detail_{{goodsId}}.html">{{name}}</a></em>商品名称</td>
	<td><b>{{score}}</b>所需积分</td>
	<td><em>{{deliverStatus status}}</em>状态</td>
	<td><em>{{timeFormat deliverTime}}</em>发货时间</td>
	<td><em>{{courierName}}</em>快递名</td>
	<td><em>{{courierNum}}</em>快递单号</td>
</tr>
{{/each}}