{{#with borrow}}
<div class="product-left-img">
          <span>项目推荐</span>
          <img src="{{articleImage borrowImg ../url}}" style="width:300px; height:240px;">
        </div>
        <div class="product-right-word clearfix">
        {{#equal type 122}}
        	<a href="/invest/entrust.html" class="all-btn">查看全部</a>
        {{/equal}}
        {{#equal type 119}}
         	 <a href="/invest/estate.html" class="all-btn">查看全部</a>
       	{{/equal}}
       	 {{#equal type 103}}
        	<a href="/invest/index.html" class="all-btn">查看全部</a>
        {{/equal}}
          <div class="predouct-js">
             <div class="predouct-js-title">{{name}}</div>
             <span>融资方：<font>{{#if companyName}}{{companyName}}{{else}}800bank{{/if}}</font></span>
             <p>{{indexContent1 content}}</p>
          </div>
          <div class="predouct-main-con clearfix">
            <span>
              <font>￥{{moneyFormat account}}</font><br/>项目规模
            </span>
            <span>
              <font>{{apr}}%</font><br/>预期年化收益率
            </span>
            {{#equal type 122}}
        		<a href="/invest/entrustDetail.html?id={{id}}" class="product-btn">我要投资</a>
	        {{/equal}}
	        {{#equal type 119}}
	         	 <a href="/invest/entrustDetail.html?id={{id}}" class="product-btn">我要投资</a>
	       	{{/equal}}
	       	 {{#equal type 103}}
	        	<a href="/invest/detail.html?id={{id}}" class="product-btn">我要投资</a>
	        {{/equal}}
          </div>
</div>
{{/with}}