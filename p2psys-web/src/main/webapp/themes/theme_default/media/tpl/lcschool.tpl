{{#each financeSiteModelList}}
 <div class="talk-people" id="talk-people">
  <div class="talk-people-con clearfix">
    <div class="talk-left"><img src="{{../url}}{{picPath}}" width="400" height="266" ></div>
    <div class="talk-right">
      <div class="talk-right-title">{{title}}</div>
      <p>{{noticIntroduction content}}</p>
      <div class="talk-con clearfix">
        <ul>
          {{#each list}}
          	<li><a href="/lcschool/detail.html?{{id}}">{{title}}</a></li>
          {{/each}}
        </ul>
        <a href="/lcschool/view.html?financeSiteId={{id}}" class="lm-btn">进入栏目</a>
      </div>
    </div>
  </div>
</div>
{{/each}}