{{#each data.list}}
 	
	<tr>
		<td>{{addOne @index}}</td>
		<td>{{userName}}</td>
		{{#equal typeCode '体验标'}}
           <td>&yen;{{interestMoney}}</td>
	     {{else}}
	           <td>&yen;{{money}}</td>
	     {{/equal}}
		<td>{{transFormatDate addTime}}</td>
	</tr>

{{/each}}

