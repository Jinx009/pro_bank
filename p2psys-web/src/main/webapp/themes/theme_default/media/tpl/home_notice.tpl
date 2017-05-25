{{#each data}}
	<li>
		<span>{{noticeDateFormat addTime}}</span>
		<h2><a href="/article/detail.html?nid={{nid}}&id={{id}}" title="{{title}}">{{hideNoticeTitle title}}</a></h2>
		<p>{{hideNoticIntroduction introduction}}</p>
	</li>	
{{/each}}