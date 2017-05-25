 {{#each data}}
 <li class="clearfix">
    <a href="/scoreMall/detail_{{goodsId}}.html" class="a_img"><img src="{{imgDemicSrc imgSrc}}" alt=""></a>
    <dl>
      <dt><em>{{userName}}</em>兑换了</dt>
      <dd><a href="/scoreMall/detail_{{goodsId}}.html">{{name}}</a></dd>
    </dl>
</li>
{{/each}}