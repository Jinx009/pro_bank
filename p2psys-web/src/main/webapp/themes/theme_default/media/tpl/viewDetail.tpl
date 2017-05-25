<div class="view-banner">
		 <div class="view-banner-word">
		 	<div class="view-banner-left">800社区</div>
		 	<div class="view-banner-right clearfix">
		 		<h3>栏目介绍</h3>
		 		<p>独家观点，资讯分享，以互联网+方式展现宏观金融及商业真相。</p>
		 	</div>
		 </div>
	</div>
	<div class="view-tab1">
		<span class="active">观点</span><span>简介</span>
	</div>
	<div class="view-detail clearfix">
		<div class="view-detail-left">
			<h3>{{financeArticle.title}}</h3>
			<div class="wirter-share clearfix">
				<span style="margin-right:30px;">作者：{{financeArticle.financeArticleExpert.autorName}}</span>
				<span class="clearfix">
					<div class="share-title">分享至：</div>
					<!-- JiaThis Button BEGIN -->
					<div class="jiathis_style">
						<a class="jiathis_button_qzone"></a>
						<a class="jiathis_button_tsina"></a>
						<a class="jiathis_button_tqq"></a>
						<a class="jiathis_button_weixin"></a>
						<a class="jiathis_button_renren"></a>
						<a class="jiathis_button_xiaoyou"></a>
						<a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jtico jtico_jiathis" target="_blank"></a>
						<a class="jiathis_counter_style"></a>
					</div>
					<script type="text/javascript" src="http://v3.jiathis.com/code/jia.js" charset="utf-8"></script>
				<!-- JiaThis Button END -->
				</span>
			</div>
			
			<div class="view-detail-con">
				<div class="view-detail-img"><img src="{{url}}{{financeArticle.picPath}}" width="496" height="340"></div>
				<div class="view-detail-word">
					{{noticIntroduction financeArticle.content}}
				</div>
			</div>

		</div>
		<div class="view-detail-right">
			<div class="expert-dt">
				<h3>专家介绍</h3>
				<div class="expert-dt-con clearfix">
					<div class="expert-dt-left"><img src="{{url}}{{financeArticle.financeArticleExpert.picPath}}"></div>
					<div class="expert-dt-right">
						<p>{{financeArticle.financeArticleExpert.autorName}}<font>（{{financeArticle.financeArticleExpert.position}}）</font></p>
						<div class="expert-look">
		                 <span>关注他：</span><a href="{{financeArticle.financeArticleExpert.blogUrl}}" class="expert-wb" target="_blank"></a><a href="javascript;" class="expert-wx"></a> 
		               	<div class="export-look-wx hide"> <img src="{{url}}{{financeArticle.financeArticleExpert.wechatPath}}" width="100" height="100"></div>
		               </div>
					</div>
				</div>
			</div>
			 <div class="expert-dt-list">
	       	  	<div class="title clearfix"><span>最新专栏文章</span><a href="">阅读全部文章</a></div>
	       	  	<ul id="detailList">
	       	  	{{#each data.financeArticleList}}
	       	  		<li class="clearfix"><span >1</span><a href="">价格改革须防范阵痛下的犹豫</a></li>
	       	  	{{/each}}
	       	  	</ul>
	       	 </div>
		</div>
		</div>
	<div class="view-detail clearfix hide">
		<ul class="expert-box">
   
	<li>
	  <div class="expert-con clearfix">
	    <div class="expert-left">
	       <img src="{{url}}{{financeArticle.financeArticleExpert.picPath}}" width="180" height="180">
	       <div class="expert-look">
	         <span>关注他：</span><a href="{{financeArticle.financeArticleExpert.blogUrl}}" target="_blank"class="expert-wb"></a><a href="" class="expert-wx"></a>
	         <div class="export-look-wx hide"> <img src="{{url}}{{financeArticle.financeArticleExpert.wechatPath}}" width="100" height="100"></div>
	       </div>
	    </div>
	    <div class="expert-right">
	      <h3>{{financeArticle.financeArticleExpert.autorName}}</h3>
	      <p>{{financeArticle.financeArticleExpert.position}}</p>
	      <p>{{expertDetailContent financeArticle.financeArticleExpert.content}}</p>
	    </div>
	  </div>
	</li>
	

</ul>
	</div>

	</div>