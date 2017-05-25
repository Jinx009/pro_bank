{{#each data.list}}
<li>
	<div class="goods_bg">
	    <div class="goods_img"><a href="/scoreMall/detail_{{id}}.html"><img src="{{imgSrcAdd imgSrc}}" alt=""></a></div>
	    <h3><a href="/scoreMall/detail_{{id}}.html">{{name}}</a></h3>
	    <p>所需积分<em>{{score}}</em></p>
	    <p>剩余数量<em>{{remainCount store sellAcount freezeStore}}件</em></p>
	    <a href="/scoreMall/detail_{{id}}.html" class="convert_btn">立即兑换</a>
	</div>
</li>
{{/each}}