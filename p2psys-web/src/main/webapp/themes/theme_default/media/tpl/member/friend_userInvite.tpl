{{#each data.list}}
<tr>
	<td><em>{{inviteUserName}}</em>邀请人</td>
	<td><em>{{userName}}</em>被邀请人</td>
	<td><em>{{transFormatDate inviteTime}}</em>邀请日期</td>
</tr>	
{{/each}}