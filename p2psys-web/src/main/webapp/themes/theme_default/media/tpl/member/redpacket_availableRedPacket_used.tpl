<div class="disable_list" id="redPacketForm">
		<table>
				<tbody>
				<tr>
				<th>名称</th>
				<th>领取时间</th>
				<th>兑换时间</th>
				<th>面额</th>
				<th>状态</th>
				</tr>
				{{#each data.list}}
					<tr>
						<td>{{name}}</td>
						<td><span>{{dateFormat addTime}}</span></td>
						<td><span>{{dateFormat usedTime}}</span></td>
						<td><b>{{amount}}元</b></td>
						<td>已使用</td>
					</tr>
				{{/each}}
		</tbody>
	</table>
</div>