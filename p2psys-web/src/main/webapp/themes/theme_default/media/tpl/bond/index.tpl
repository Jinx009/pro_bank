{{#each data.list}}
<dl>
    <dt><h2>{{{investTypeName borrowType}}}<a href="/invest/detail.html?id={{borrowId}}" class="title">{{borrowName}}</a></h2><br/> <span class="number">债权转让编号：{{name}}</span><span class="worth">债权总价：<em>{{moneyFormat bondMoney}}</em><i>元</i></span></dt>
    <dd class="content">
        <ul class="clearfix">
                <li class="w148">年化收益：{{apr}}%</li>
                <li class="w216">还款方式：{{transFormatStyle borrowStyle}}</li>
                <li class="w222">剩余债权：{{transRemainMoney bondMoney soldCapital}}元</li>
                <li class="w148">剩余期限：{{remainDays}}天</li>
                <li class="w216">折让率：{{bondApr}}% <i class="iconfont" title="出让人在转让该债权时，折让的本金占本金的比例。">&#xe641;</i></li>
                <li class="w222">进度：<span class="progress"><em style="width:{{progress soldCapital bondMoney}}%"></em></span>{{progress soldCapital bondMoney}}%</li>
        </ul>
        {{{transBondListBtn id bondMoney soldCapital}}}
    </dd>
    {{{transBondStatus bondMoney soldCapital}}}
</dl>
{{/each}}