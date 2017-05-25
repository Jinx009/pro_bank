<div class="disable_list" id="redPacketForm">
		<table>
				<tbody>
				<tr>
				<th>名称</th>
				<th>领取时间</th>
				<th>过期时间</th>
				<th>面额</th>
				<th>状态</th>
				</tr>
				{{#each data.list}}
					<tr>
						<td>{{name}}</td>
						<td><span>{{dateFormat addTime}}</span></td>
						<td><span>{{dateFormat expiredTime}}</span></td>
						<td><b>{{amount}}</b></td>
						<td>已过期</td>
					</tr>
				{{/each}}
		</tbody>
	</table>
</div>