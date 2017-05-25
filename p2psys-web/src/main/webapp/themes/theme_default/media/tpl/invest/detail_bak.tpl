
 <div class="invest-main-left">
    <div class="bid_info mb20 bdr1-e6e6e6 pt40 pl40 pr40 pb30 bg-white">
      <img src="/themes/theme_default/images/wytz_face.png" class="face mr30 mb20">
      {{#with borrow}}
      <dl class="clearfix float_left">
        <dt class="c315A8B">{{name}} <div class="investSchedule"><span>{{scales}}%</span><input type="text" class="knob" value="{{scales}}" /></div></dt>
        <dd class="bid_type">{{borrowDetailsTypeName type}} 借款编号：{{bidNo}}&nbsp;&nbsp;&nbsp;{{{protocolPreview status}}}</dd>
        <dd class="bid_intro"> 
          <div class="first"><span><i class="icon money"></i>借款金额</span><br><b class="c315A8B f24">{{moneyFormat account}} <em>元</em></b></div>
          <div class="second"><span><i class="icon pecent"></i>年化利率</span><br><b class="c315A8B f24">{{apr}}<em>%</em></b></div>
          <div class="last"><span><i class="icon clock-gray"></i>借款时间</span><br>{{borrowLimitTime borrowTimeType timeLimit}}</div>
        </dd>
      </dl>
      <ul class="bdr1-e6e6e6 pt10 pb10 pl20 pr20">
        <li>还款方式　<span>{{transFormatStyle style}}</span></li>
        <li>发布时间　<span>{{fixedTimeTranformation fixedTime  startTime}}</span></li>
        <li>单笔最小投标　<span>{{bidLimit lowestAccount}}元</span></li>
        <li>
        投资奖励　<span>{{#equal partAccount 0}}无{{else}}{{partAccount}}%{{/equal}}</span>
        </li>
        <li>结束时间　<span>{{endTimeTransformation fixedTime validTime endTime}}</span></li>
        <li>{{bidMostLimit mostAccount account}}</span></li>
      </ul>
      {{/with}}
    </div>

    <div class="mb20 bdr1-e6e6e6 bg-white">
      <div class="detail-menus clearfix">
        <div class="detail_menus_center">
          {{#if userBaseInfo}}<a href="javascript:void(0)" class="a1">借款方详情</a>{{/if}}
          <a href="javascript:void(0)" class="a2">借款描述</a>
          {{#inequality borrow.type 101}}<a href="javascript:void(0)" class="a4">借款资料</a>{{/inequality}}
          <a href="javascript:void(0)" class="a5">投资记录</a>
          <p id="invest_btn" class="js_hide" href="javascript:;">立即投资</p>
        </div>
      </div>
      <div class="invest_detail_content">
      {{#with userBaseInfo}}
        <div class="borrower-info mb30">
          <h3 class="invest-detail-h3"><i class="icon clip"></i>借款方详情</h3>
          <h4>借款人信息</h4>
          <ul class="clearfix">
            <li><label for="">用户名：</label>{{../borrow/userName}}</li>
            <li><label for="">婚姻状况：</label>{{transMaritalStatus maritalStatus}}</li>
            <li><label for="">收入范围：</label>{{transMonthIncomeRange monthIncomeRange}}</li>
            <li><label for="">年　龄：</label>{{transBirthday birthday}}</li>
            <li><label for="">户籍城市：</label>{{province}} {{city}}</li>
            <li><label for="">车　　产：</label>{{transValue carStatus}}</li>
            <li><label for="">学　历：</label>{{transEducation education}}</li>
            <li><label for="">工作时间：</label>{{transWorkExperience workExperience}}</li>
            <li><label for="">房　　产：</label>{{transValue houseStatus}}</li>
          </ul>
          <h4 class="mt10">资料审核状态</h4> 
          <table>
          <thead>
            <tr>
              <th>审核项目</th>
              <th>状态</th>
              <th>通过时间</th>
            </tr>
          </thead>
          <tbody>
          {{#each ../certificationApply}}
            <tr>
              <td>{{typeName}}</td>
              <td><img src="/themes/theme_default/media/bg/ok.png" alt=""></td>
              <td>{{timeMonthFormat addTime}}</td>
            </tr>
            {{/each}}
          </tbody>
          </table>
        </div>
        {{/with}}
        {{#with borrow}}
        <div class="borrow-desc mb30">
          <h3 class="invest-detail-h3"><i class="icon clip"></i>借款描述</h3>
          <div class="pl30 pt20">
            <p style="text-indent:2em">{{borrowDetailContent content}}</p>
          </div>
        </div>
        {{/with}}
        {{!-- 秒标不显示借款资料--}}
        {{#inequality borrow.type 101}}
        <div class="borrow-detail mb30">
          <h3 class="invest-detail-h3"><i class="icon clip"></i>借款资料</h3>
          <div class="tab-content">
            <div class="album">
              <ul class="img-box bdr1-e6e6e6">
              {{#each borrowUploads}}
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
              {{#each borrowUploads}}
                <li></li>
              {{/each}}
              </ul>
            </div>
          </div>
        </div>
        {{/inequality}}
        <div class="invest-record">
          <h3 class="invest-detail-h3"><i class="icon clip"></i>投资记录</h3>
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
                <td>{{transFormatTenderType tenderType}}</td>
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
  </div>

  <div class="invest-main-right">
    <div class="mb20 invest-right-top clearfix">
      <b class="invest-rightside-h3-left"></b>
      <h3 class="invest-rightside-h3 float_right">我要投资</h3>
      {{!判断是否为预览页面  status等于0为预览页面}}
      {{#inequality borrow.status 0}}
      {{#if borrowPreview}}
      {{#inequality userType 2}}
                      {{#inequality borrow/scales 100}}
                    		<div class="amount-form-box pt10 pl30 pr30 bdr1-e6e6e6 bg-white float_right">
                    		  	{{#with account}}
                                		        <p class="amount-money">可用余额：<i class="pageUseMoney">{{moneyFormat useMoney}}</i>元</p>
                                		        <p class="amount-money">可投金额：<i class="pageInvestMoney">{{moneyFormat ../accountWait}}</i>元</p>
                                		        <form action="/invest/tender.html" name="form1" id="invest_detail_form1" method="post" class="amount-form" onkeydown="if(event.keyCode==13) return false; " autocomplete="off" target="_blank">
                                		          <div class="form-group">
                                		             <em class="iconfont">&#xe63b;</em><input type="text" id="money"  name="money" size="11" onKeyUp="replaceAndSetPos(this,/[^0-9]/g,'')" placeholder="投标金额" autocomplete="off" maxlength="7" />
                                                     <div class="form_group_tip form_mony_tip hide"><b class="iconfont">&#xe60f;</b>请输入投标金额进行投标</div><i>元</i>
                                		          </div>
                                                      {{#equal ../isOpenAip false}}
                                                                  {{#if ../../packets}}
                                                                  <div class="form-red">
                                                                  <a href="javascript:;" class="choiceRedPacket" title="我的红包">我的红包&nbsp;&gt;&gt;</a>
                                                                   </div>
                                                                   {{/if}}
                                                       {{else}}
                                                                  {{#equal ../../apiCode 3}}
                                                                      {{#if ../../../packets}}
                                                                      <div class="form-red">
                                                                      <a href="javascript:;" class="choiceRedPacket" title="我的红包">我的红包&nbsp;&gt;&gt;</a>
                                                                       </div>
                                                                       {{/if}}
                                                                  {{/equal}}
                                                      {{/equal}}
                                                     
                                                      {{#if ../isDirectional}}
                                                      <div class="form-group">
                                                          <em class="iconfont">&#xe617;</em><input type="text" class="isDirectionalText"  size="11" autocomplete="off"/><input type="password" class="isDirectional"  name="pwd" size="11" placeholder="定向密码" autocomplete="off" style="display:none;"/>
                                                          <div class="form_group_tip form_directional_tip hide"></div>
                                                      </div>
                                                      {{/if}}
                                                      {{#equal ../isOpenAip false}}
                                                                   {{#equal ../../payPwd false}}
                                                                  <div class="form-group">
                                                                      <em class="iconfont">&#xe617;</em><input type="text" class="payPwdText"  size="11" autocomplete="off"/><input type="password" class="payPwd"  name="payPwd" size="11" placeholder="交易密码" autocomplete="off" style="display:none;"/>
                                                                      <div class="form_group_tip form_trade_tip hide"></div>
                                                                  </div>
                                                                  {{else}}
                                                                  <div class="form-group">
                                                                      	交易密码：<a href="/member/security/setting.html" target="_blank" style="color:#c83e41">请设置交易密码</a>
                                                                  </div>
                                                                  {{/equal}}
                                                      
                                                      {{/equal}}
                                		          <div class="form-action">
                                		            <input type="hidden" id="bid_id" name="id" value="{{../borrow/id}}"/>
                                		            <input type="hidden" name="tenderToken" value="{{../tenderToken}}">
                                		            <input type="button" value="立即投资" class="green-btn sub-invest-btn">
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
                                                            <p class="expirationTimeP">{{lastDays}}天后过期</p>
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
                                    {{#with borrow}}
                                    <div class="amount-form-box borrow_full pt10 pl30 pr30 bdr1-e6e6e6 bg-white float_right">
                                      <p>投资笔数：<em>{{../data.page.total}}笔</em></p>
                                      <p>当前状态：<em>{{logBorrowStatusFun status scales type flow}}</em></p>
                                      <p class="disabled_btn">投资已结束</p>
                                      <p>欢迎进入<a href="/invest/index.html">投资列表</a>，投资其他借款。</p>
                                    </div>
                                    {{/with}}
                                    <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
                      {{/inequality}}
      {{else}}
                  <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
                    <dl>
                      <dt>对不起！</dt>
                      <dd>当前账号无投资权限，不能进行投资。</dd>
                    </dl>
                    <a class="login_account" href="/member/main.html">进入账户中心</a>
                  </div>
                  <input type="hidden" id="bid_id" value="{{../borrow/id}}"/>
      {{/inequality}}
      {{else}}
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>此页面为预览页面，不能进行投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{/if}}
      {{else}}
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>等待管理员初审，不能进行投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{/inequality}}
    </div>
    
    <div class="guarantee mb20 clearfix">
      <b class="invest-rightside-h3-left"></b>
      <h3 class="invest-rightside-h3 float_right">安全保障</h3>
      <div class="guaranteebd pb15 pl30 bdr1-e6e6e6 bg-white  float_right">
      	<dl class="guaranteeIco1">
      		<dt>业务模式</dt>
      		<dd>一对多的交易机 制，充分、有效分散风险</dd>
      	</dl>
      	<dl class="guaranteeIco2">
      		<dt>机构合作</dt>
      		<dd>全面控制源头风险</dd>
      	</dl>
      	<dl class="guaranteeIco3">
      		<dt>项目审核</dt>
      		<dd>多重审核，保证每笔借款都是优质的</dd>
      	</dl>
      	<dl class="guaranteeIco4">
      		<dt>资金保护</dt>
      		<dd>本息保障制度</dd>
      	</dl>
      	<dl class="guaranteeIco5">
      		<dt>网站建设</dt>
      		<dd>银行级别的投资安全保障</dd>
      	</dl>
        <div class="pt20">     
          <span><i class="icon shield"></i><br>本息担保</span>
          <span><i class="icon clock-blue"></i><br>稳定经营</span>
          <span><i class="icon crown"></i><br>资质优良</span>
        </div>
      </div>
    </div>
    <div class="windControl clearfix">
          <b class="invest-rightside-h3-left"></b>
      	  <h3 class="invest-rightside-h3 float_right">风控模型</h3>
            <div class="tab-content bdr1-e6e6e6 bg-white  float_right"  style="border-top:0;">
              <h4><i>1</i>资信评级及尽职调查</h4>
              <p>对借款人进行资信评级，包括借款人的资信状况，收入来源，房产情况调查；对企业尽职调查，企业基本信息，贷款卡记录，及企业资产负债，企业当前状态下的库销比；结合借款人资信情况及企业的营收能力，进行综合授信。实地调查企业经营情况，形成专业的尽职调查报告，结合前期资信评级完成对车商风险调查</p>
              <h4><i>2</i>风险分类 </h4>
              <p>根据借款后检查在借项目的动态性风险程度，由低至高，进行五级分类（包括正常类、关注类、次级类、可疑类、损失类）。次级类、可疑类、损失类资产为不良资产</p>
              <h4><i>3</i>不定期巡查</h4>
              <p>定期检查与不定期检查；线下公司自查与风控部指定专人检查；可疑情况，联合调查或独立调查、补充检查或复查</p>
              <h4><i>4</i>风险预警</h4>
              <p style="border-bottom:0;">结合第三方监管机构信息反馈，最早发现借款人风险情况，最快排除资金风险</p>
            </div>
        </div>
  </div>
