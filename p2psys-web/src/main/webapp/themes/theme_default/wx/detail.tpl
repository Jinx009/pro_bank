 <div class="clearfix invest-border">
 <div class="invest-detail-box clearfix">
 <div class="invest-main-left">
    <div class="bid_info mb20 bdr1-e6e6e6 pt40 pb30 bg-white">
      <img src="{{articleImage ppfundImag.picPath url}}" class="face mr30 mb20">
      <div class="invest-mid">
      {{#with ppfund}}
      <dl class="clearfix float_left">
        <dt class="c315A8B">{{name}}</dt>
        <dd class="bid_intro"> 
          <div class="first">
                 <span class="detail-money">
                    借款金额<br/>
                    <p><em>{{ppfundAccount account}}</em></p>
                 </span>
              </div>
              <div class="second">
                 <span class="detail-rate">
                    年化利率<br/>
                    <p><em>{{apr}}</em>%</p>
                 </spn>
              </div>
              <div class="last">
                 <span class="detal-time">
                    借款时间<br/>
                    <p><em>{{ppfundTimeLimit timeLimit}}</em></p>
                 </span>
              </div>
              {{#inequality isFixedTerm 1}}
             <div class="last">
                 <span class="detal-time">
                    结息周期<br/>
                    <p><em>{{cycle}}天</em></p>
                 </span>
              </div>
              {{/inequality}}
        </dd>
      </dl>
      {{/with}}
      {{#with ppfund}}
      <ul class="bdr1-e6e6e6 pt10 pb10 pl20 pr20">
        <li>还款方式：<span>{{ppfundRepaymentWay isFixedTerm}}</span></li>
        <li>计息方式：<span>T+{{interestWay}}</span></li>
        <li>单笔最小投标：<span>{{bidLimit lowestAccount}}元</span></li>
        <li>单笔最大投标：<span>{{bidLimit mostAccount}}</span></li>
        <li>投标开始时间：<span>{{startTime}}元</span></li>
       	<li>投标结束时间：<span>{{endTime}}</span></li>
       	<input type="hidden" id="pid_id" value="{{id}}" />
        {{#inequality account 0}}
         <li class="jd">
          <span class="detail-jd">
              <div class="float_left" style="color:#666;">投资进度：</div>
              <div class="jindu float_left">
                  <div class="rate_tiao" style="width:{{ppfundScales account accountYes}}%"></div>
              </div>
              <div class="float_left" style="margin-left:10px;">{{ppfundScales account accountYes}}%</div>
          </span>
        </li>
      </ul>
      {{/inequality}}
      {{/with}}
    </div>
    </div>

  </div>

  <div class="invest-main-right">
    <div class="mb20 bdr1-e6e6e6 bg-white">
      {{#if account}}
      {{!判断是否为预览页面  status等于0为预览页面}}
      {{#userTypeval login_user_cache.userType}}
       <div class="amount-form-box borrow_user_tip pt10 pl30 pr30" style="padding-left:30px;">
        <dl style="height:100px;">
          <dt>对不起！</dt>
          <dd>借款账户不能投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="pid_id" value="{{../ppfund/id}}" />
      {{else}}

      {{#inequality ppfund.status 0}}
      {{#inequality scales 100}}
      
      <h3 class="invest-rightside-h3">投资金额</h3>
		<div class="amount-form-box pt10 pl20 pr20" style="padding-left:30px;">
		  	{{#with account}}
		        <p class="amount-money">账户余额：<b class="c315A8B"><em class="pageUseMoney">{{moneyFormat useMoney}}</em>元</b><br/>
		        {{#inequality ../ppfund.account 0}}
		        	可投金额：<b class="c315A8B"><span class="ok-money">{{ppfundAccountWait ../../ppfund.account ../../ppfund.accountYes}}</span>元</b></p>
		        {{else}}
		        	<span class="ok-money"></span>
		        {{/inequality}}
		        <form action="/ppfund/in.html" name="form1" id="invest_detail_form1" method="post" class="amount-form" onkeydown="if(event.keyCode==13) return false; " autocomplete="off" target="_blank">
		          <div class="form-group">
		             投标金额：<input type="text" id="money" autocomplete="off" name="money" size="11" onKeyUp="value=value.replace(/[^0-9]/g,'')" placeholder="输入投标金额" style="width:170px;border:1px solid #f63"/>
                        <div class="form_group_tip form_mony_tip hide">请输入投标金额进行投标</div><i>元</i>
		          </div>
		          <input type="hidden" id="isFixedTerm" name="isFixedTerm" value="{{../ppfund.isFixedTerm}}"/>
		          {{#equal ../ppfund.isFixedTerm 0}}
		          <div class="form-group">
		             转出时间：<input type="text" id="outTime" autocomplete="off" name="outTime" size="11"  placeholder="选择转出时间" onclick="laydate({istime: false, format: 'YYYY-MM-DD'})" style="width:170px;border:1px solid #f63"/>
		             <div class="form_group_tip form_out_time_tip hide">请选择转出时间</div><i>元</i>   
		          </div>
		          {{/equal}}
              <div class="form-group dy-input hide">
                <input type="text" id="borrowVoucherMoney" autocomplete="off" name="borrowVoucherMoney" size="11" onKeyUp="value=value.replace(/[^\d]/g,'')" />剩余抵用券<em class="c315A8B" id="VoucherMoney">{{transborrowVoucherUserMoney ../borrowVoucherUserMoney}}</em>元
              </div>
              
              <div class="form-group">
                      {{#if ../packets}}
                      <a href="javascript:;" class="choiceRedPacket" title="点击选择红包"></a>
                      {{/if}}
                      </div>
                      <div class="form-group">
                          交易密码：<input type="password" class="payPwd" autocomplete="off" name="payPwd" size="11" placeholder="输入交易密码" />
                          <div class="form_group_tip form_trade_tip hide"></div>
                          <i class="lock"></i>
                      </div>
		          <div class="form-action" style="text-align:left;">
		            <input type="hidden" id="pid_id" name="id" value="{{../ppfund/id}}"/>
		            <input type="hidden" name="ppfundTenderToken" value="{{../ppfundTenderToken}}"/>
		            <input type="button" value="立即投资" class="green-btn sub-invest-btn">
                <span class="all-tb">全投</span>
		          </div>
                    {{!S红包dom结构}}
                      <div  id="choiceRedPacket" class="hide choiceRedPacketBox">
                      <label class="checkAllRedPacket" for="checkAllRedPacket"><input type="checkbox" id="checkAllRedPacket"/>全选</label>
                        <ul class="clearfix">
                        {{#each ../packets}}
                          <li>
                          <label for="choiceRedPacket{{@index}}">
                          <p  class="redPacketName">{{name}}</p>
                            <p class="redPacketMoneyP">￥<i class="redPacketMoney">{{amount}}</i><input type="checkbox" name="ids" value="{{id}}" id="choiceRedPacket{{@index}}"/></p>
                            <p class="expirationTimeP">20天后过期</p>
                            </label>
                          </li>
                          {{/each}}
                        </ul>
                        <p>选中红包总额：<span class="choiceRedPacketMoney">0.00</span>元</p>
                        <p >实际支付金额：<span class="investMoney">0.00</span>元</p>
                        <div class="redPacketConfirm"><a href="javascript:;" id="redPacketConfirm" class="green-btn">确定</a></div>
                      </div>
                      {{!E红包dom结构}}
		        </form>
		      {{/with}}
            </div>
      {{else}}
      {{#with ppfund}}
      <div class="amount-form-box borrow_full pt10 pl30 pr30">
        <p>投资笔数：<em>{{../data.page.total}}笔</em></p>
        <p class="disabled_btn">投资已结束</p>
        <p>欢迎进入<a href="/ppfund/index.html">投资列表</a>，投资其他借款。</p>
      </div>
      {{/with}}
      {{/inequality}}
      {{else}}
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>等待管理员初审，不能进行投资。</dd>
        </dl>
      </div>
      {{/inequality}}
      {{/userTypeval}}
       {{else}}
     <a href="/user/login.html" style="margin:50px 30px;width: 235px;display:block;line-height: 40px;height: 40px;color: #fff;text-align: center;background-color: #2370b6;border: none;border-bottom: 2px solid #2370b6;cursor: pointer;-moz-border-radius: 5px;-webkit-border-radius: 5px;border-radius: 5px;font-size: 16px;">立刻登录</a>
     {{/if}}
    </div>
  
  </div>
  </div>
  </div>

 <div class="mb20 bdr1-e6e6e6 bg-white" style="width:1190px; margin:0 auto;">
      <div class="detail-menus clearfix">
        <div class="detail_menus_center">
          <a href="javascript:void(0)" class="a2">借款描述</a>
          {{#if ppfundUploads}}<a href="javascript:void(0)" class="a4">借款资料</a>{{/if}}
          <a href="javascript:void(0)" class="a5">投资记录</a>
          <p id="invest_btn" class="js_hide" href="javascript:;">立即投资</p>
        </div>
      </div>
      <div class="invest_detail_content">
        {{#with ppfund}}
        <div class="borrow-desc mb30">
          <h3 class="invest-detail-h3">借款描述</h3>
          <div class="pl30 pt20">
            <p style="text-indent:2em">{{borrowDetailContent content}}</p>
          </div>
        </div>
        {{/with}}
    
    {{#if ppfundUploads}}
        <div class="borrow-detail mb30">
          <h3 class="invest-detail-h3">借款资料</h3>
          <div class="tab-content" style="width:800px; margin:50px auto 0;">
            <div class="album">
              <ul class="img-box bdr1-e6e6e6">
              {{#each ppfundUploads}}
                {{#equal type 4}}
                  <li><a href="{{picPath}}" class="fancybox" ><img src="{{picPath}}" alt="借款资料"></a></li>
                {{else}}
                  <li><a href="{{../../url}}{{picPath}}" class="fancybox" ><img src="{{../../url}}{{picPath}}" alt="借款资料"></a></li>
                {{/equal}}
              {{/each}}
              </ul>
              <a href="javascript:void(0)" class="prev"></a>
              <a href="javascript:void(0)" class="next"></a>
              <ul class="hd">
              {{#each ppfundUploads}}
                <li></li>
              {{/each}}
              </ul>
            </div>
          </div>
        </div>
        {{/if}}
        <div class="invest-record">
          <h3 class="invest-detail-h3">投资记录</h3>
          <table id="invest-txjl-tbl">
            <thead>
              <tr>
                <th>序号</th>
                <th>投资人</th>
                <th>投资金额</th>
                <th>投资时间</th>
                <th>投资方式</th>
                <th>状态</th>
              </tr>
            </thead>
            <tbody>
            {{#if data.list}}
            {{#each data.list}}
              <tr>
                <td>{{addOne @index}}</td>
                <td>{{userName}}</td>
                <td>￥{{money}}</td>
                <td>{{transFormatDate addTime}}</td>
                <td>手动投标</td>
                <td><i class="icon success"></i></td>
              </tr>
            {{/each}}
            {{else}}
            <tr><td colspan="6">暂无投资记录</td></tr>
            {{/if}}
            </tbody>
          </table>
          <div id="kkpager" class="ml20 mr20"></div>
        </div>
      </div>
    </div>
