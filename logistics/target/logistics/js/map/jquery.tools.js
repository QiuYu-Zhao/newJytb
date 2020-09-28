/**
 * @author  Lyca,qmzmxfy@vip.qq.com
**/
var tools = {
  plugin:function(group) {//插件加载
    group = $.extend({
      path:'',
      arr:[],
      len:0
    },group);
    group.len = group.arr.length;
    $.each(group.arr,function(i,v){
      $.ajax({
        url:group.path+v+'.js',
        dataType:'script',
        cache:true,
        success:function() {
          group.len--;
          if(!group.len){
            group.success();
          }
        }
      });
    });
  },
  cookie:function(n,v,group){//cookie操作:键,值,其他参数
    if(typeof v!=='undefined'){
      group = $.extend({
        e:0,  //0:会话,-1:删除,1:1天
        p:'/',  //路径
        d:location.hostname,  //域名
        date:new Date()
      },group);
      group.date.setTime(group.date.getTime()+group.e*24*60*60*1e3);
      group.date = group.e!==0?group.date.toGMTString():'';
      document.cookie = n+'='+window.escape(v)+';expires='+group.date+';path='+group.p+';domain='+group.d;
    }else{
      v = document.cookie.match(new RegExp('(^| )' + n + '=([^;]*)(;|$)'));
      return v!==null?window.unescape(v[2]):v;
    }
  },
  creatFlash:function(needs,group){//添加flash:DOM,必需参数[swf地址,宽,高,id],附加参数
    var param = [],
        others = $.browser.msie?'classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"':'type="application/x-shockwave-flash" data="'+needs.src+'"';
    group = $.extend({
      wmode:'transparent'
    },group);
    $.each(group,function(n,v){
      param.push('<param name="'+n+'" value="'+v+'">');
    });
    return '<object id="'+(needs.id||"")+'" width="'+needs.w+'" height="'+needs.h+'" '+others+'><param name="src" value="'+needs.src+'">'+param.join('')+'</object>';
  },
  linkPar:function(key,v){//链接参数取值
    return ((v||location.href).match(new RegExp('(?:\\?|&)'+key+'=(.*?)(?=&|$)'))||['',null])[1];
  },
  cutStr:function(s,l,nodot){//字符串截取
    var temp = [],
        sc = '',
        m = nodot?l:l-3,
        n = 0,
        sl = s.replace(/[^\x00-\xff]/g,'**').length;
    if(!l){return sl;}
    if(l>=sl){return s;}
    for(var i=0;i<s.length;i++){
      sc = s.charAt(i);
      n = window.escape(sc).length>4?n+2:n+1;
      if(n>m){
        break;
      }
      temp.push(sc);
    }
    if(!nodot){
      temp.push('...');
    }
    return temp.join('');
  },
  rNum:function(x,y){//生成包含x-y之间的随机数
    return Math.floor(Math.random()*(y-x+1)+x);
  },
  thousandFormat:function(s){//千位分隔符
    var decimal,
        reg = /(\d)(\d{3},)/;
    if(/[^0-9\.]/.test(s)){
      return s;
    }else{
      s = Number(s).toString().split('.');
      decimal = s[1];
      s = s[0]+',';
    }
    while(reg.test(s)){
      s = s.replace(reg,'$1,$2');
      s = s.replace(/,(\d\d)$/,'.$1');
    }
    return s.replace(/\,$/,'')+(decimal?'.'+decimal:'');
  },
  date:function(format,stamp) {//时间格式:类型,时间戳
    format = format||'Y-M-D H:I:S';
    var D = stamp||new Date(),
        dd,
        week = [['Sun','Mon','Tues','Wed','Thur','Fri','Sat'],['\u65e5','\u4e00','\u4e8c','\u4e09','\u56db','\u4e94','\u516d']],
        double = function(v){
          v = v>9?''+v:'0'+v;
          return v;
        };
    if(/^\d+$/.test(D)){
      D = new Date(D);
    }
    dd = {
      year:D.getYear(),
      month:D.getMonth()+1,
      date:D.getDate(),
      day:week[0][D.getDay()],
      Day:week[1][D.getDay()],
      hours:D.getHours(),
      minutes:D.getMinutes(),
      seconds:D.getSeconds(),
      millisecond:D.getMilliseconds()
    };
    dd.G = dd.hours>12?'PM'+double(dd.hours-12):'AM'+double(dd.hours);
    dd.g = dd.hours>12?'PM'+(dd.hours-12):'AM'+dd.hours;
    var oType = {
      Y:D.getFullYear(),//2015年
      y:dd.year,//115年(-1900)
      M:double(dd.month),//08月
      m:dd.month,//8月
      D:double(dd.date),//04日
      d:dd.date,//4日
      W:dd.Day,//日
      w:dd.day,//Sun
      H:double(dd.hours),//7点
      h:dd.hours,//07点
      G:dd.G,//AM07点
      g:dd.g,//AM3点
      I:double(dd.minutes),//08分
      i:dd.minutes,//8分
      S:double(dd.seconds),//09秒
      s:dd.seconds,//9秒
      L:double(parseInt(dd.millisecond*0.1, 10)),//08毫秒
      l:dd.millisecond
    };
    for(var i in oType){
      format = format.replace(i,oType[i]);
    }
    return format;
  },
  countdown:function(group) {//倒计时:秒数,毫秒过程,秒过程,完成
    group = $.extend({
      process:function(){},
      complete:function(){}
    },group);
    var ctInterval,
        timeArr = group.t.toString().split('.'),
        rs = timeArr[0],
        ms = Math.floor((timeArr[1]||0)/10);
    var format = function(t,t2){
      var d = parseInt(t/24/3600,10),
          h = parseInt((t-d*24*3600)/3600,10),
          H = h+d*24,
          i = parseInt((t-d*24*3600-h*3600)/60,10),
          s = t-d*24*3600-h*3600-i*60,
          date = {
            d:d,
            h:h,
            H:H,
            i:i,
            s:s,
            ms:t2||0
          };
      $.each(date,function(n,v){
        date[n] = v>9?''+v:'0'+v;
      });
      return date;
    },
    takeCount = function(){
      if(ms<0){
        rs--;
        ms = 99;
      }
      if(rs>=0){
        group.process(format(rs,ms));
      }
      if(rs<0){
        group.complete();
        clearInterval(ctInterval);
      }
      ms--;
    };
    takeCount();
    ctInterval = setInterval(takeCount,10);
  },
  clipboard:function(v,handler){//复制到剪切板 仅IE
    if(window.clipboardData){
      window.clipboardData.setData('Text',v);
      if(handler){handler(v);}
    }else{
      alert('\u5f53\u524d\u6d4f\u89c8\u5668\u4e0d\u652f\u6301\u6b64\u590d\u5236\u529f\u80fd\uff0c\u8bf7\u4f7f\u7528ctrl+c\u6216\u9f20\u6807\u53f3\u952e\u3002');
    }
  },
  ZeroClipboard:function(path){//复制到剪切板 flash
      var p = (path||'/swf/map/')+'ZeroClipboard.swf'
      console.log('p=' + p);
    window.ZeroClipboard.config({

      swfPath:p
    }); 
  },
  addBookmark:function($o,group){//加入到收藏夹
    var ua = navigator.userAgent.toLowerCase(),
        ctrl = ua.indexOf('mac')!=-1?'Command':'Ctrl',
        failure = '\u60a8\u7684\u6d4f\u89c8\u5668\u4e0d\u652f\u6301\uff0c\u8bf7\u5c1d\u8bd5 '+ctrl+'+D \u624b\u52a8\u6536\u85cf\uff01';
    group = $.extend({
      title:document.title,
      href:location.href,
      way:'click'
    },group);
   if(window.sidebar){
      $o.attr({
        rel:'sidebar',
        href:group.href,
        title:group.title
      });
    }else{
      $o.on(group.way,function(){
        try{
          window.external.addFavorite(group.href,group.title);
        }catch(e){
          alert(failure);
        }
        return false;
      });
    }
  },
  lock:function(obj,handler,group){//事件锁定
    var $obj = $(obj),
        f = function(){
          var $this = $(this);
          if(!$this.data('lock')){
            $this.data('lock',1);
            handler(this,obj);
          }
          return false;
        };
    group = $.extend({
      way:'click',
      parent:''
    },group);
    if(!handler){
      $obj.data('lock',0);
    }else if(handler===true){
      $obj.data('lock',1);
    }else if(group.parent){
      $(group.parent).on(group.way,obj,f);   
    }else{
      $obj.on(group.way,f);
    }
  },
  seamless:function($o,group) {//无缝滚动
    group = $.extend({
      t:40
    },group);
    if(group.height){
      $o.height(group.height);
    }
    $o.css('overflow','hidden').children().clone().appendTo($o);
    var $a = $o.children().eq(0),
        $b = $o.children().eq(1),
        aH = $a.outerHeight(true),
        marquee = function(){
          var aT = $a.offset().top,
              bT = $b.offset().top,
              oS = $o.scrollTop();
          if(bT-aT<=oS){
            $o.scrollTop(oS-aH);
          }else{
            $o.scrollTop(oS+1);
          }
        },
        marInterval = setInterval(marquee,group.t);
    $o.hover(function(){
      clearInterval(marInterval);
    }, function() {
      marInterval = setInterval(marquee,group.t);
    });
  },
  focusMode:function($o,v,flag){//input提示:DOM,值,placeholder使用标识
    if(!flag&&'placeholder' in document.createElement('input')){
      $o.attr('placeholder',v);
    }else{
      $o.val(v).focus(function(){
        var $this = $(this);
        $this.addClass('input-focus');
        if($this.val()==v){
          $this.val('');
        }
      }).blur(function(){
        var $this = $(this);
        $this.removeClass('input-focus');
        if($this.val()===''){
          $this.val(v);
        }
      });
    }
  },
  hoverSwitch:function(way, hoverThis, hoverClass, switchObject, n, speed, interval, handler) {
      var canSwitch = true;
      function switchNow($this, canSwitch) {
          var switchObjectLen = switchObject.length, NowIndex = $this.index(), css_z = 'z-index';
          $this.addClass(hoverClass).siblings().removeClass(hoverClass);
          for (var i = 0; i < switchObjectLen; i++) {
              var $nowLi = $(switchObject[i] + ':eq(' + NowIndex + ')'), $nowLi_s = $nowLi.siblings(), $visible = $(switchObject[i] + ':visible'), visible_z = parseInt($visible.css(css_z));
              visible_z = visible_z ? visible_z :1;
              if (speed) {
                  if (interval) {
                      canSwitch = false;
                  }
                  $nowLi_s.css(css_z, visible_z - 1).stop();
                  $nowLi.css(css_z, visible_z).fadeTo(speed, 1, function() {
                      $nowLi_s.hide();
                      if (interval) {
                          canSwitch = true;
                      }
                  });
              } else {
                  $nowLi.show();
                  $nowLi_s.hide();
              }
              if (handler) {
                  handler($this);
              }
          }
      }
      function autoSwitch() {
          var autoLen = $(hoverThis).length;
          if (canSwitch) {
              var i = $(hoverThis).filter(function() {
                  if ($(this).hasClass(hoverClass)) {
                      return $(this);
                  }
              }).index();
              i++;
              if (i >= autoLen) {
                  i = 0;
              }
              switchNow($(hoverThis + ':eq(' + i + ')'), canSwitch);
          }
      }
      if (way) {
          $(hoverThis).on(way, function() {
              var $this = $(this);
              switchNow($this);
              return false;
          });
          $(hoverThis + ':eq(' + n + ')').trigger(way);
      } else {
          $(hoverThis).mouseenter(function() {
              switchNow($(this));
              return false;
          });
          $(hoverThis + ':eq(' + n + ')').mouseenter();
      }
      if (interval) {
          setInterval(autoSwitch, interval);
          $(hoverThis + ',' + switchObject[0]).hover(function() {
              canSwitch = false;
          }, function() {
              canSwitch = true;
          });
      }
  },
  page:function(m,n) {//页码生成:当前页码,总页码;
    var pageShow = 10,
        prevHtml = '<span class="page-btn page-prev' + (m == 1 ? " page-prev-disabled" :"") + '" data-page="' + (m == 1 ? 0 :m - 1) + '">上一页</span>',
        nextHtml = '<span class="page-btn page-next' + (m == n ? " page-next-disabled" :"") + '" data-page="' + (m == n ? 0 :m + 1) + '">下一页</span>',
        moreHtml = '<span class="page-more">...</span>',
        currClass = '',
        pageHtml = '',
        middleHtml = '',
        firstPageHtml = '<span data-page="1">1</span>',
        lastPageHtml = '<span data-page="' + n + '">' + n + "</span>",
        loopPage = function(x, y) {
          for(var i=x;i<=y;i++) {
            currClass = m == i ? 'page-curr':'';
            pageHtml += '<span class="' + currClass + '" data-page="' + (m == i ? 0 :i) + '">' + i + "</span>";
          }
          return pageHtml;
        };
    if(n>pageShow){
      if(m<pageShow-1){
        middleHtml = loopPage(1,pageShow-2) + moreHtml + lastPageHtml;
      }else if(m>n-3){
        middleHtml = firstPageHtml + moreHtml + loopPage(n-7,n);
      }else{
        middleHtml = loopPage(m-7,m) + moreHtml + lastPageHtml;
      }
    }else{
      middleHtml = loopPage(1,n);
    }
    return prevHtml + middleHtml + nextHtml;
  },
  baiduShare:function(share){
    window._bd_share_config = {
      share:share
    };
    $.getScript('http://bdimg.share.baidu.com/static/api/js/share.js');
  },
  directionText:function(direction){
    var ret = '无';
    if(direction<=23||direction>338){
      ret = "北";
    }else if(direction>23&&direction<=68){
      ret = "东北";
    }else if(direction>68&&direction<=113){
      ret = "东";
    }else if(direction>113&&direction<=158){
      ret = "东南";
    }else if(direction>158&&direction<=203){
      ret = "南";
    }else if(direction>203&&direction<=248){
      ret = "西南";
    }else if(direction>248&&direction<=293){
      ret = "西";
    }else if(direction>293&&direction<=338){
      ret = "西北";
    }
    return ret;
  }
};