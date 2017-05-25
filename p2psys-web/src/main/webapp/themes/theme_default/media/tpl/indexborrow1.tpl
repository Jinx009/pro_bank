{{#with ppfund}}
<div class="product-left-img">
          <span>项目推荐</span>
          <img src="{{articleImage ppfundImg ../url}}" style="width:300px;height:240px;">
        </div>
        <div class="product-right-word clearfix">
          <a href="/ppfund/index.html" class="all-btn">查看全部</a>
          <div class="predouct-js">
             <div class="predouct-js-title">{{name}}</div>
             <span>融资方：<font>800bank</font></span>
             <p>{{indexContent1 content}}</p>
          </div>
          <div class="predouct-main-con clearfix">
            <span>
              <font>{{#equal account 0}}无限额{{else}}￥{{moneyFormat account}}{{/equal}}</font><br/>项目规模
            </span>
            <span>
              <font>{{apr}}%</font><br/>预期年化收益率
            </span>
            <a href="/ppfund/detail.html?id={{id}}" class="product-btn">我要投资</a>
          </div>
</div>
{{/with}}