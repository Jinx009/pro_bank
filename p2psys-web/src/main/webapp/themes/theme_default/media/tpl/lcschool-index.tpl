<div class="talk-people-con clearfix">
		<div class="talk-left"><img width="400" height="266" src="{{url}}{{data.picPath}}"></div>
		<div class="talk-right">
			<div class="talk-right-title">{{data.title}}</div>
			<p>{{noticIntroduction data.content}}</p>
			<div class="talk-con clearfix">
				<ul>
					{{#each data.list}}
			          <li><a href="/lcschool/detail.html?{{id}}">{{title}}</a></li>
			        {{/each}}
				</ul>
				<a href="/lcschool/view.html?financeSiteId={{data.id}}" class="lm-btn">进入栏目</a>
			</div>
		</div>
</div>