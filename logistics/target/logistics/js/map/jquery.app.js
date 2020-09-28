/**
 * @author  Lyca,qmzmxfy@vip.qq.com
**/
var BMap = window.BMap;
var map = window.map;
var tools = window.tools;
var lushu = window.lushu;
var geofenceOverlays = window.geofenceOverlays;
var ComplexCustomOverlay = window.ComplexCustomOverlay;
var ZeroClipboard = window.ZeroClipboard;
//var localeOverlays,localePath;
//var localeOverlays,localePath,localenum = 0;

var localeOverlays,localePath,localenum = tools.linkPar('localenum')||0;
var markerCache;
var app = {
  ajax:function(url,data,handler){
	  $.ajax({
	        url: url,
	        timeout:(arguments[3] || 60000),
	        data: data,
	        type: "post",
	        success: handler,
	        error: function (error) {
              alert("登录超时，请关闭地图窗口,重新登陆!");
	          console.log(error);
	        }
      });
  },
  getList:function(data,type){
    var list = [],dataLen = data.length;
    if(data&&dataLen>0){
      $.each(data,function(i,v){
        switch(type){
          case 'trunk'://树干
          case 'branch'://树枝
          case 'search'://查询
            var style0 = v.icon>0?'tree-leaf':'',
                style1 = type=='trunk'?'tree-trunk':'',
                cutName = (v.icon>0&&type!='search')?tools.cutStr(v.name,22):v.name;
            list.push('<li class="tree-branch '+style0+' '+style1+'"><a class="tree-branch-name clearfix" href="javascript:;" title="'+v.name+'" data-id="'+v.id+'" data-name="'+v.name+'" data-icon="'+v.icon+'"><i class="tree-branch-ocl tree-icon fl"></i><i class="tree-branch-icon tree-branch-icon'+v.icon+' tree-icon fl"></i><span class="tree-branch-txt fl">'+cutName+'</span></a><ul class="tree-children"></ul></li>');
            break;
          case 'carDevice'://车辆及设备HTMl结构
          case 'device'://仅设备HTML
            var collectState = v.collection?['car-info-collected','已关注',true]:['','重点关注',false],
                localeState = v.locale&&v.locale.length>2?['car-info-localecancel','取消原地设防设置',true]:['','原地设防设置',false],
                style3 = i%2===0?'':'tr-line',
                firstHtml = (i===0&&type=='carDevice')?'<td class="td-rowspan th-big" rowspan="'+dataLen+'"><a class="car-info-track car-info-btn icon" href="" target="_blank" title="实时定位"></a><a class="car-info-playback car-info-btn icon" href="" target="_blank" title="轨迹回放"></a><a class="car-info-pop car-info-btn icon" href="javascript:;" title="车辆信息"></a><a class="car-info-locale '+localeState[0]+' car-info-btn icon" href="javascript:;" title="'+localeState[1]+'" data-state="'+localeState[2]+'"></a><a class="car-info-collect '+collectState[0]+' car-info-btn icon" href="javascript:;" title="'+collectState[1]+'" data-state="'+collectState[2]+'"></a></td><td class="td-rowspan" rowspan="'+dataLen+'">'+v.carnum+'</td><td class="td-rowspan" rowspan="'+dataLen+'">'+v.username+'</td>':'',
                lastHtml = type=='device'?'<td>'+v.lng+'</td><td>'+v.lat+'</td>':'';
            list.push('<tr class="'+style3+'">'+firstHtml+'<td>'+v.deviceType+'</td><td>'+v.devicenum+'</td><td><a class="view-location" href="javascript:;" data-num="'+i+'" data-devicenum="'+v.devicenum+'">查看</a></td><td>'+v.state+'</td><td>'+v.speed+'</td><td>'+v.mileage+'</td>'+lastHtml+'</tr>');
            break;

          case 'playback':
            var style2 = i%2===0?'':'tr-line';
            var addressTd = v.adres;
              if('' == v.adres) {
                addressTd = '<td class="th-big"><a class="view-address" href="javascript:;" data-lng="'+v.lng+'" data-lat="'+v.lat+'">查看</a></td>'
              } else {
                //addressTd = '<td class="th-big" >'+v.adres+'</td>'
                var addressFull = v.adres;
                if (''!= v.sdesc) {
                  addressFull += v.sdesc;
                }
                var addressLengh = 20;
                if(addressFull.length > addressLengh) {
                  addressFull = addressFull.substring(0,addressLengh);
                  addressFull +='...';
                }
                addressTd = '<td class="th-big ">' + addressFull + '</td>';
              }
            var oneRow = '<tr class="'+style2+'"><td>'+(i+1)+'</td><td>'+v.cn+'</td><td>'+v.ptm+'</td><td>'+v.sp+'</td><td>'+tools.directionText(v.di)+'</td><td>'+v.mil+'</td><td>'+v.pot + '</td>' + addressTd+'</tr>'
            list.push(oneRow);
            break;
          default:
            break;
        }
      });
    }else{
      //list.push('无');
    	switch(type){
	        case 'trunk':
	        case 'branch':
	          var style = type=='trunk'?'tree-trunk':'';
	          list.push('<li class="tree-branch '+style+'"><a class="tree-branch-none clearfix" href="javascript:;"><i class="tree-branch-ocl tree-icon fl"></i><span class="tree-branch-txt fl">当前分类下没有数据！</span></a></li>');
	          break;
	        case 'search':
	          list.push('<li class="tree-trunk"><a class="tree-branch-none clearfix" href="javascript:;"><i class="tree-branch-ocl tree-icon fl"></i><span class="tree-branch-txt fl">没有查到数据信息！</span></a></li>');
	          break;
	        default:
	          list.push('');
	          break;
    	}
    }
    return list.join('');
  },
  position:function(){//根据IP获取定位信息
	var myCity = new BMap.LocalCity();
    myCity.get(function(result){
      var cityName = result.name;
      if(cityName!='全国'&&cityName!='北京'){
        map.centerAndZoom(cityName);
      }
    });
  },
  getCityName:function(json,handler){
    var myGeo = new BMap.Geocoder();
    myGeo.getLocation(new BMap.Point(json.lng,json.lat),function(result){
      handler(result?result.address:'暂无位置信息！');
    });
  },
  customerName:function(){
    var id = tools.linkPar('orgId');
    app.ajax('list/find.html',{id:id,pageName:$("#pageName").val()},function(json){
      /* 测试数据 */
      //json = {'customerName':'百度开发总部','carCount':'58475'};
      json = JSON.parse(json);
      /* 测试数据 */
      $('h1').html(json.customerName);
      $('#car-count').html(json.carCount);
    });
  },
  bottomBar:function(){
    //底部设备列表添加滚动条
    var $wrap = $('#car-list-wrap');
    $wrap.mCustomScrollbar({'scrollInertia':0,theme:"dark-2"});
  },
  tree:function($cartree,url){
    /* cartree滚动条 */
    $cartree.mCustomScrollbar({'mouseWheelPixels':'200','theme':'dark-thin'});
    /* 定义树枝伸展 */
    var showHide = function($this,$brother){
      if($this.hasClass('tree-opened')){
        $this.addClass('tree-closed').removeClass('tree-opened');
        $brother.hide();
      }else{
        $this.addClass('tree-opened').removeClass('tree-closed');
        $brother.show();
      }
      $cartree.mCustomScrollbar('update');
    };
    /* 点击树 */
    var leafFlag = true;
    tools.lock('.tree-branch-name',function(obj){
      var $this = $(obj),
          $brother = $this.siblings('ul'),
          hasdata = $this.data('has'),
          id = $this.data('id'),
          icon = $this.data('icon');
      /* 判断树枝树叶 */
      if(icon>0){//树叶被点击
        tools.lock(obj);//解锁按钮,使用leafFlag标识判断多次点击
        if(!leafFlag){
          return false;
        }
        leafFlag = false;
        $cartree.find('.tree-leaf').removeClass('tree-clicked');
        $this.parent().addClass('tree-clicked');
        localenum = 0;
        //请求车辆信息
        app.carData({'id':id},function(){
          leafFlag = true;//解锁标识
        });
      }else{//树枝被点击
        if(hasdata){//调用缓存,直接显示
          showHide($this,$brother);
          tools.lock(obj);//解锁按钮
        }else{//没有缓存,请求数据
          $this.addClass('tree-throbber');//loading
          app.treeData(url,{'id':id},function(json){
            $this.data('has',true).removeClass('tree-throbber');//写入缓存,取消loading
            $brother.html(app.getList(json,'branch'));//写入数据
            showHide($this,$brother);//显示数据
            tools.lock(obj);//解锁按钮
          });
        }
      }
    },{'parent':'#cartree'});
    $cartree.on('mouseenter','.tree-leaf',function(){
      $(this).addClass('tree-hovered');
    }).on('mouseleave','.tree-leaf',function(){
      $(this).removeClass('tree-hovered');
    });
    //tree 高度自适应
    var treeHeight = function(){
      var wH = $(window).height();
      $cartree.height(wH-180);
    };
    treeHeight();
    $(window).resize(function(){
      var tHtimeout;
      clearTimeout(tHtimeout);
      tHtimeout = setTimeout(function(){
        treeHeight();
      },100);
    });
  },
  treeData:function(url,data,handler){
      app.ajax(url,data,function(json){//'list/page.html'
      /* 测试数据 */
      //icon 0.节点 1.离线 2.行驶中 3.静止
      //json = [{'id':'1','name':'北京市朝阳区','icon':0},{'id':'22','name':'京A054517 沈惠霞','icon':1},{'id':'33','name':'京A054517 沈惠霞','icon':2},{'id':'44','name':'京A054517 沈惠霞','icon':3}];
      /* 测试数据 */
      json = JSON.parse(json);
      handler(json);
    });
  },
    carData:function(data,handler){
    var $wrap = $('#car-list-wrap'),
        $carInfo = $('#car-info'),
        loadingHtml = '<tr><td class="loading"></td></tr>';
    //map.clearOverlays();
      isDevice = !data.id&&!!data.devicenum,
      url = 'list/device.html';
    $carInfo.html(loadingHtml);
    $wrap.mCustomScrollbar('update');
      if(isDevice){//单设备接口
        url = 'list/device/one.html';
      }
      //请求车辆信息
    app.ajax(url,data,function(json){
      json = JSON.parse(json);
      if(!json||json.length===0){
          $carInfo.html('<tr><td class="none">没有设备数据信息！</td></tr>');
          map.clearOverlays();
          if(handler){
            handler();
          }
          return false;
      }
      /* 车辆信息默认获取第一个设备的信息 */
      var carJson = json[0];
      markerCache = carJson.markers;//配置全局的标注数据缓存
      localePath = carJson.locale;//配置初始原地设防设置
      //配置底部信息栏
      $carInfo.html(app.getList(json,isDevice?'device':'carDevice'));
      $wrap.mCustomScrollbar('update');
      /* 定义重点关注按钮 */
      tools.lock('.car-info-collect',function(obj){
        var $this = $(obj),
            state = $this.data('state'),
            carId = carJson.id,//$this.data('id'),
            stateArr = state?[0,'移除',false]:[1,'添加到',true];
        //进入时是重点关注1，则是取消
        if(state) {//取消重点关注
          app.ajax('focus/car.html',{id:carId,state:stateArr[0]},function(json2){
            //2取消关注
            json2 = true;
            if(json2){
              $.fn.popAlert('取消重点关注成功','提示');
              $this.data('state',stateArr[2]);
              $this.removeClass('car-info-collected');
            }else{
              $.fn.popAlert('取消重点关注失败','提示');
            }
            tools.lock(obj);
          });
        } else { //设置成重点关注
          $.fn.popAlert('<ul class="car-setImportant clearfix" id="car-setImportant"><li class="clearfix"><label class="fl">类型：</label><select class="fl" name="iType"><option value="-1">请选择</option><option value="1">逾期未还款</option><option value="2">车价高</option><option value="3">评价高风险</option><option value="0">其他</option></select><span class="tips fl"></span></li><li class="clearfix"><label class="fl">原因：</label><input class="fl" type="text" name="iTxt"><span class="tips fl"></span></li></ul>','设置重点关注',[{
            classname:'pop-alert-btn-b pop-close',
            txt:'取消'
          },{
            classname:'pop-alert-btn-a',
            txt:'设置',
            callback:function(){
              var $setI = $('#car-setImportant'),
                  $iType = $setI.find('[name="iType"]'),
                  $iTxt = $setI.find('[name="iTxt"]'),
                  iType = $iType.val(),
                  iTxt = $iTxt.val();
              $setI.find('.error').removeClass('error');
              if(iType=='-1'){
                $iType.siblings('.tips').html('请选择类型！').parent().addClass('error');
              }else if(iTxt==''){
                $iTxt.siblings('.tips').html('请选择原因！').parent().addClass('error');
              }else{
                console.log(iType+','+iTxt);//获取设置的内容
                app.ajax('focus/car.html',{id:carId,state:stateArr[0],remark:iTxt,type:iType},function(json2){
                  /* 测试数据 */
                  json2 = true;
                  /* 测试数据 */
                  if(json2){
                    $.fn.popAlert('设置重点关注成功！','提示');
                    $this.data('state',stateArr[2]);
                    $this.addClass('car-info-collected');
                  }else{
                    $.fn.popAlert('设置重点关注失败！','提示');
                  }
                });
              }

            }
          }]);
          tools.lock(obj);

        }
      });
      /* 定义原地设防设置按钮 */
      tools.lock('.car-info-locale',function(obj){
        var $this = $(obj),
            state = $this.data('state');
        if(state){
          $.fn.popAlert('确定取消原地设防设置？','提示',[{
            classname:'pop-alert-btn-b pop-close',
            txt:'取消'
          },{
            classname:'pop-alert-btn-a',
            txt:'确定',
            callback:function(){
              app.ajax('localeSetting.html',{id:carJson.id},function(json){//取消原地设防设置
                /* 测试数据 */
                var result = JSON.parse(json);
                if(result.code == 1){
                  $.fn.popAlert('取消原地设防设置成功！');
                  $this.removeClass('car-info-localecancel').data('state',false).attr('title','原地设防设置');
                  map.removeOverlay(localeOverlays);
                  localePath = [];
                }else{
                  $.fn.popAlert('取消原地设防设置失败，请重试！');
                }
              });
            }
          }]);
        }else{
          $.fn.popAlert('<div class="car-setlocale clearfix"><label class="fl">区域半径（米）：</label><input class="car-setlocale-txt fl" type="text"><p class="fl">请输入正整数</p></div>','原地设防设置',[{
            classname:'pop-alert-btn-b pop-close',
            txt:'取消'
          },{
            classname:'pop-alert-btn-a',
            txt:'设置',
            callback:function(){
              var $localeTxt = $('.car-setlocale-txt'),
                  localeRadii = $localeTxt.val(),
                  localePoint = new BMap.Point(carJson.lng,carJson.lat);
              if(/^[1-9]\d*$/.test(localeRadii)){
                $localeTxt.parent().removeClass('car-setlocale-error');
                localeOverlays = new BMap.Circle(localePoint,localeRadii,{strokeColor:"#567efd", strokeWeight:2, strokeOpacity:0.8,fillColor:"#567efd",fillOpacity:0.5});
                map.addOverlay(localeOverlays);
                app.ajax('localeSetting.html',{id:carJson.id,posArr:JSON.stringify(localeOverlays.getPath())},function(json){//上传原地设防设置
                  /* 测试数据 */
                  var result = JSON.parse(json);
                  if(result.code == 1){
                    $.fn.popAlert('原地设防设置成功！');
                    $this.addClass('car-info-localecancel').data({'state':true,'locale':localePath}).attr('title','取消原地设防设置');
                    localePath = localeOverlays.getPath();
                  }else{
                    $.fn.popAlert('原地设防设置失败，请重试！');
                    map.removeOverlay(localeOverlays);
                  }
                });
              }else{
                $localeTxt.parent().addClass('car-setlocale-error');
              }
            }
          }]);
        }
        tools.lock(obj);
      });
      /* 定义查看设备位置按钮 */
      var viewLocationFlag = true;
      $('.view-location').click(function(){
        var $this = $(this),
            dataNum = $this.data('num'),
            className = 'view-location-curr';
        if(!viewLocationFlag||$this.hasClass(className)){
          return false;
        }
        $carInfo.find('.'+className).removeClass(className).html('查看');
        $this.addClass(className).html('当前');
        viewLocationFlag = false;
        var deviceJson = json[dataNum];
        localenum = dataNum;
        $carInfo.find('.car-info-track').attr('href','track.html?id='+deviceJson.id+'&devicenum='+deviceJson.devicenum);
        $carInfo.find('.car-info-playback').attr('href','playback.html?id='+deviceJson.id+'&devicenum='+deviceJson.devicenum);
        app.carIcon(deviceJson,function(){
          viewLocationFlag = true;
        });
        /* 配置车辆标注 */
        if(markOverlayFlag){//开启标注标识的页面执行标注相关
          app.getmark(carJson);
        }
      });
      /* 定义车辆信息按钮 */
      $('.car-info-pop').click(function(){
        $('#car-pop').popShow({mask:false});
      });
      /* 配置车辆信息弹层 */
      var popHtml = '<p>创建时间：'+carJson.creatTime+'</p><p>车主姓名：'+carJson.username+'</p><p>车主电话：'+carJson.phone+'</p>' +
          '<p>车辆品牌：'+carJson.brand+'</p><p>车辆型号：'+carJson.models+'</p><p>车牌号：'+carJson.carnum+'</p>' +
          '<p>vin车架号：'+carJson.vin+'</p>';
      $('#car-pop .bd').html(popHtml);
      if(handler){
        handler();
      }
      /* 默认显示第一个设备的位置 */

      //$('.view-location:first').click();
      //$('.view-location:eq('+localenum+')').click();
      var $viewLocation = $('.view-location:eq('+localenum+')');
      if(data.devicenum){
        $viewLocation = $('.view-location[data-devicenum="'+data.devicenum+'"]');
      }
      //等等再出发点击，以免出现定位不准的情况
      setTimeout(function(){
        $viewLocation.click();
      }, 500);
    });
  },

  showArea:function(dataPath){ // 显示高危区域
    map.clearOverlays();

    var localePoint = [];
    $.each(dataPath, function(i,v){
      localePoint.push(new BMap.Point(v.lon, v.lat));
    });
    geofenceOverlays = new BMap.Polygon(localePoint,{strokeColor:"#567efd", strokeWeight:2, strokeOpacity:0.8,fillColor:"#567efd",fillOpacity:0.5});
      map.addOverlay(geofenceOverlays);
      // map.disableDragging();
  },
  carIcon:function(json,handler){//配置地图设备信息
    map.clearOverlays();
    app.getCityName(json,function(address){
      /* 添加原地设防设置覆盖物 */
      var localePoint = [];
      $.each(localePath,function(i,v){
        localePoint.push(new BMap.Point(v.lng,v.lat));
      });
      localeOverlays = new BMap.Polygon(localePoint,{strokeColor:"#567efd", strokeWeight:2, strokeOpacity:0.8,fillColor:"#567efd",fillOpacity:0.5});
	    map.addOverlay(localeOverlays);
      /* 添加汽车Icon */
      //json.address = address;
      json.address = '' != json.address?json.address:address;//服务端没得到的，客户端调用查询
      var mapHtml = '<div class="car-info-list">'+(!json.id?'':'<p>车主姓名：'+json.username+'</p>')+'<p>设备类型：'+json.deviceType+'</p>' +
          '<p>定位方式：'+json.posType+'</p><p>上报时间：'+json.posTime+'</p><p>状态：'+json.state+'</p>' +
          '<p>电量：'+json.battery+'</p><p>位置：<span class="device-address">'+json.address+'</span>' +
          '<p>家庭住址：'+json.homeAdd+'</p><p>工作地址：<span class="device-address">'+json.workAdd+'</span>' +
          '</p></div><div class="car-info-tips"></div><div class="car-info-closed icon"></div>';
      var carPoint = new BMap.Point(json.lng,json.lat),
          myCompOverlay = new ComplexCustomOverlay('/image/map/car'+json.icon+'.png',mapHtml,carPoint,json.direction);
      map.addOverlay(myCompOverlay);
      map.centerAndZoom(carPoint,15);
      /* 配置复制按钮 */
      var $copy = $('.car-info-copy');
      if(!$.browser.msie){
        tools.plugin({
          path:'/js/map/',
          arr:['ZeroClipboard.min'],
          success:function(){
            tools.ZeroClipboard();
            new ZeroClipboard($copy);
            ZeroClipboard.on('copy',function(e){
              $.fn.popAlert('复制成功！');
            });
          }
        });
      }else{
        $copy.click(function(){
          var txt = $(this).data('clipboard-text');
          tools.clipboard(txt,function(){
            $.fn.popAlert('复制成功！');
          });
        });
      }
      if(handler){
        handler();
      }
    });
  },
  carSearch:function($cartree,url){
    var $tree = $('#tree'),
        $reset = $('#search-reset'),
        $txt = $('#search-txt'),
        $result = $('#search-result'),
        txtTips = '输入车牌号';
    //输入框焦点
    tools.focusMode($txt,txtTips);
    //查询数据
    tools.lock('#search-btn',function(obj){
      var txt = $txt.val();
      if(txt&&txt!==txtTips){
        if(!url){//本地查询
          $reset.show();
          $result.children().hide();
          $result.find('.tree-branch-name[data-name*="'+txt+'"]').parent().show();
          $cartree.mCustomScrollbar('update');
          tools.lock(obj);
        }else{
          app.ajax(url,{'keyword':txt},function(json){//接口查询
        	json = JSON.parse(json);
            /* 测试数据 */
            $reset.show();
            $tree.hide();
            $result.html(app.getList(json,'search')).show();
            $cartree.mCustomScrollbar('update');
            tools.lock(obj);
          });
        }
      }else{
        tools.lock(obj);
      }
    });
    //重置数据
    $reset.click(function(){
      $(this).hide();
      $txt.val('').focus();
      if(!url){//本地查询
        $result.children().show();
      }else{
        $result.hide();
        $tree.show();
      }
      $cartree.mCustomScrollbar('update');
    });
  },
    /*输入城市定位，点击搜索实现的逻辑*/
  citySearch:function(){
    var $reset = $('#search-reset'),
        $txt = $('#search-txt'),
        $searchbtn = $('#search-btn'),
        txtTips = '输入城市定位';
    //输入框焦点
    tools.focusMode($txt,txtTips);
    //查询数据
    tools.lock('#search-btn',function(obj){
      var txt = $txt.val();
      if(txt&&txt!==txtTips){
        map.centerAndZoom(txt,12);
        $reset.show();
        tools.lock(obj);
      }else{
        tools.lock(obj);
      }
    });
    //重置数据
    $reset.click(function(){
      $(this).hide();
      $txt.val('').focus();
    });
    //点击
    $searchbtn.click(function(){
        map.centerAndZoom($txt.val(), 11);
        var local = new BMap.LocalSearch(map, {
            renderOptions:{map: map}
        });
        local.search($txt.val());
    });
    $txt.change(function(){
        map.centerAndZoom($txt.val(), 11);
        var local = new BMap.LocalSearch(map, {
            renderOptions:{map: map}
        });
        local.search($txt.val());
    });
  },
  addmark:function(carJson,marker1,handler){
    var point = marker1.getPosition(),
        style = '',
        markname = carJson.markname,
        markinfo = carJson.markinfo;
    if(!carJson){
      style = 'mark-content-cancel';
      markname = '';
      markinfo = '';
    }
    var markHtml = '';

    //只有其他类型的标注可已删除，家和公司的不能删
    if(carJson.type == '1' || carJson.type == '2') {
      markHtml = '<div class="mark-content-list"><div class="mark-content-one clearfix"><label class="fl">名称：</label><input class="fl" type="text" name="markname" value="'+markname+'"></div><div class="mark-content-one clearfix"><label class="fl">备注：</label><textarea class="fl" name="markinfo">'+markinfo+'</textarea></div><div class="mark-content-btn clearfix"><input class="mark-content-btn1 fl" type="button" value="保存"><input class="mark-content-btn2 fr '+style+'" type="button" value="删除" disabled="disabled"></div></div><div class="mark-content-tips"></div><div class="mark-content-closed icon '+style+'"></div>';
    } else {
      markHtml = '<div class="mark-content-list"><div class="mark-content-one clearfix"><label class="fl">名称：</label><input class="fl" type="text" name="markname" value="'+markname+'"></div><div class="mark-content-one clearfix"><label class="fl">备注：</label><textarea class="fl" name="markinfo">'+markinfo+'</textarea></div><div class="mark-content-btn clearfix"><input class="mark-content-btn1 fl" type="button" value="保存"><input class="mark-content-btn2 fr '+style+'" type="button" value="删除"></div></div><div class="mark-content-tips"></div><div class="mark-content-closed icon '+style+'"></div>';
    }

    var marker2 = new markOverlay(markHtml,point);
    map.addOverlay(marker2);
    var $marker2 = $(marker2._wrap),
        $content = $(marker2._content);
    if(!carJson){
      $content.show();
    }
    //绑定按钮事件
    $marker2.find('[name]').click(function(){//输入框获取焦点
      $(this).focus();
    });
    marker1.addEventListener("click",function(){//修改模式：显示隐藏标注弹层
      if(carJson){
        $content.toggle();
      }
    });
    $marker2.find('.mark-content-closed').click(function(){//修改模式：隐藏标注弹层
      if(carJson){
        $content.hide();
      }
    });
    $marker2.find('.mark-content-btn2').click(function(){//修改模式：删除标注
      var $this = $(this);
      var delUrl = 'interest/del.html';
      if(carJson){
        $this.attr('disabled',true);
        app.ajax(delUrl,{'id':carJson.id,'markid':carJson.markid},function(json){//删除标注请求
          /* 测试数据 */
          json = JSON.parse(json);
          console.log({'id':carJson.id,'markid':carJson.markid});//提交的数据
          /* 测试数据 */
          $this.attr('disabled',false);
          if(json.code == 1){
            map.removeOverlay(marker1);
            map.removeOverlay(marker2);
            //写入数据缓存
            carJson.flag = true;
            $.fn.popAlert('删除标注成功！');
          };
        });
      }
    });
    $marker2.find('.mark-content-cancel').click(function(){//添加模式：取消添加标注
      map.removeOverlay(marker1);
      map.removeOverlay(marker2);
    });
    $marker2.find('.mark-content-btn1').click(function(){//提交标注
      var $this = $(this),
          postUrl = '',
          postData = {'lng':point.lng,'lat':point.lat},
          isError = false;
      //配置接口及参数
      if(!carJson){
        postUrl = 'interest/add.html';//添加标注接口
        postData.id = tools.linkPar('id')||$('#cartree .tree-clicked a:first').data('id');
      } else {
        postUrl = 'interest/update.html';//修改标注接口
        postData.id = carJson.id;
        postData.markid = carJson.markid;
      }
      $marker2.find('[name]').each(function(i,v){
        var name = $(v).attr('name'),
            val = $(v).val();
        postData[name] = val;
        if(val==''){
          isError = true;
        }
      });
      if(isError){
        $.fn.popAlert('名称和备注不能为空！');
      }else if(!postData.id){
        $.fn.popAlert('请先选择添加标注的车辆！');
      }else{
        $this.attr('disabled',true);
        app.ajax(postUrl,postData,function(json){//提交标注请求，接口在上面配置
          /* 测试数据 */
          console.log(postData);//提交的数据
          json = JSON.parse(json);
          //json = {"code":1,"markid":556};//返回数据，添加标注接口的返回值需要返回markid，修改标注接口可以只返回code状态

          /* 测试数据 */
          $this.attr('disabled',false);
          if(!carJson){//添加标注执行
            if (json.code==1) {
              map.removeOverlay(marker1);
              map.removeOverlay(marker2);
              postData.markid = json.markid;
              var newPostData = {'id':postData.id,markers:[postData]};
              app.getmark(newPostData);
              //写入数据缓存
              markerCache.push(postData);
              $.fn.popAlert('添加标注成功！');
            }
          } else {//修改标注执行
            if(json.code==1){
              $content.hide();
              marker1.getLabel().setContent(postData.markname);
              //写入数据缓存
              carJson.markname = postData.markname;
              carJson.markinfo = postData.markinfo;
              $.fn.popAlert('修改标注成功！');
            }
          }
        });
      }
    });
  },
  getmark:function(carJson){
    $.each(carJson.markers,function(i,v) {
      if(!v.flag){
        var point = new BMap.Point(v.lng, v.lat),
            //家庭 单位 其他 三种图标
        markerIcon = new BMap.Icon(v.type == 1 ?
            "/image/map/homepho1.png" : v.type == 2 ?
            "/image/map/workpho1.png" : "/image/map/i/marker.png", new BMap.Size(30, 30)),
        marker1 = new BMap.Marker(point, {icon: markerIcon, offset: new BMap.Size(8, -16)});
        marker1.setLabel(new BMap.Label(v.markname, {offset: new BMap.Size(33, 4)}));
        map.addOverlay(marker1);
        v.id = carJson.id;
        app.addmark(v, marker1, function (marker2) {
        });
     }
    });
  },
  setmark:function(){
    //标注相关开启
    markOverlayFlag = true;
    //添加标注
    var drawingManager = new BMapLib.DrawingManager(map,{//实例化鼠标绘制工具
      isOpen:false,//是否开启绘制模式
      enableDrawingTool:true,//是否显示工具栏
      drawingToolOptions:{
        anchor:BMAP_ANCHOR_TOP_RIGHT,//位置
        offset:new BMap.Size(-3,42),//偏离值
        drawingModes:[BMAP_DRAWING_MARKER],
        scale:0.8
      }
    });
    drawingManager.addEventListener('overlaycomplete',function(e){//添加鼠标绘制工具监听事件，用于获取绘制结果
      var marker1 = e.overlay;
      marker1.setOffset(new BMap.Size(8,-16));
      marker1.setIcon(new BMap.Icon("/image/map/i/marker.png",new BMap.Size(32,32)));

      drawingManager.close();
      app.addmark('',marker1,function(marker2){
        //$(marker2._content).show();g
      });
    });
  },
  carall:function(){
    var $cartree = $('#cartree'),
        $tree = $('#tree'),
        id = tools.linkPar('orgId');
    app.customerName();
    app.tree($cartree,"list/page.html");
    app.carSearch($cartree,"search/car.html");
    app.bottomBar();
    app.treeData('list/page.html',{'id':id},function(json){//获取树干
      $tree.html(app.getList(json,'trunk'));
      $cartree.mCustomScrollbar('update');
    });
    app.setmark();
  },
  track:function(){
	var carId = tools.linkPar('id'),
    devicenum = tools.linkPar('devicenum');

	var getCar = function(data){
		app.carData({'id':carId,'devicenum':data});
    };
    if(carId){
      getCar(devicenum);
      setInterval(function(){
        getCar();
      },60000);
    }else{
      $.fn.popAlert('车辆信息错误！','提示',[{
        callback:function(){
          //可配置其他操作
        }
      }]);
    }
    app.bottomBar();
    app.setmark();
  },
    /*  726-759   2017-6-5上午ZXR修改*/
    gather_the_alarm:function(){
        $.ajax({//调取车辆信息并且渲染到页面上
            type : "post",
            dataType : "JSON",
            url : "/mk/gpsdata/assemble/list/assembleElement.html",
            data : {
                "parentId": function() {
                    return $("#pidData").val();
                }
            },
            success: function(result) {
                //成功开始绘制车辆
                for(var i = 0 ; i < result.aaData.length; i++){
                    var car_id = result.aaData[i].car_id;
                    //下面是绘制弹框
                    var mapHtml = '<div class="car-info-list">'+(!result.aaData[i]?'':'<p>车主姓名：'+result.aaData[i].name+'</p>')+'<p>设备类型：'+result.aaData[i].deviceType+'</p>' +
                        '<p>定位方式：'+result.aaData[i].posType+'</p><p>状态：'+result.aaData[i].state+'</p>' +
                        '<p>上报时间：'+result.aaData[i].posTime+'</p><p>位置：<span class="device-address">'+result.aaData[i].address+'</span></p></div><div class="car-info-tips"></div><div class="car-info-closed icon"></div>';
                    var carPoint = new BMap.Point(result.aaData[i].lon,result.aaData[i].lat),//经纬度位置
                        myCompOverlay = new ComplexCustomOverlay('/image/map/car2.png',mapHtml,carPoint,result.aaData[i].direction);//汽车图标放置位置
                    map.addOverlay(myCompOverlay);
                    map.centerAndZoom(carPoint,15);
                }
            },
            error : function(result) {
                alert("聚集点错误")
            }
        });
        app.bottomBar();
        app.setmark();
    },
    deviceTrack:function(){//设备轨迹
    var devicenum = tools.linkPar('devicenum');
    var getDevice = function(data){
      app.carData({'devicenum':data});
    };
    if(devicenum){
      getDevice(devicenum);
      setInterval(function(){
        getDevice(devicenum);
      },60000);
    }else{
      $.fn.popAlert('设备信息错误！','提示',[{
        callback:function(){
          //可配置其他操作
        }
      }]);
    }
    app.bottomBar();
  },
  collect:function(){
    var $cartree = $('#cartree'),
        $tree = $('#tree'),
        id = tools.linkPar('orgId');
    app.customerName();
    app.tree($cartree,"getImportantCarByType.html");
    app.carSearch($cartree,"search/important/car.html");
    app.treeData('list/focus/car.html',{'id':id},function(json){//重点关注列表接口
      $tree.html(app.getList(json,'trunk'));
      $cartree.mCustomScrollbar('update');
    });

    /*app.ajax('list/focus/car.html',{id:id},function(json){//重点关注列表接口
      *//* 测试数据 *//*
      //json = [{'id':'22','name':'京A054515 沈惠霞','icon':1},{'id':'33','name':'京A054516 沈惠霞','icon':2},{'id':'44','name':'京A054517 沈惠霞','icon':3}];
      json = JSON.parse(json);
      *//* 测试数据 *//*
      $tree.html(app.getList(json,'trunk'));
      $cartree.mCustomScrollbar('update');
    });*/
    app.bottomBar();

  },
  geofence:function(){
    window.fence();
    app.citySearch();
    var $clear = $('#geofence-clear'),
        $name = $('#geofence-name'),
        $areaType = $('#areaType'),
        $remark = $('#remark');
    //清除高危区域
    $clear.click(function(){
      map.removeOverlay(geofenceOverlays);
    });
    //添加高危区域
    tools.lock('#geofence-save',function(obj){
      var name = $name.val();
      if(!name){
        $.fn.popAlert('请输入高危区域名称！');
        tools.lock(obj);
      }else if(!geofenceOverlays){
        $.fn.popAlert('请在地图上画出高危区域！');
        tools.lock(obj);
      }else{
        app.ajax('save/danger/area.html',{id:$('#geofence-id').val(), name:name,areaType:$areaType.val(),remark:$remark.val(),posArr:JSON.stringify(geofenceOverlays.getPath())},function(json){
          /* 测试数据 */
          json = true;
          /* 测试数据 */
          if(json){
        	alert('高危区域设置成功！');
        	//   $.fn.popAlert('高危区域设置成功！');
            map.removeOverlay(geofenceOverlays);
            window.location.href = "/mk/admin/danger/danger.html";
          }else{
            $.fn.popAlert('高危区域设置失败，请重试！');
          }
          tools.lock(obj);
        });
      }
    });
  },
  playback:function(){
    //日期插件
    var laydate = window.laydate,
    format = 'YYYY-MM-DD hh:mm:ss';
    laydate.skin('danlan');
    var start = {
      elem:'#date-start',
      max:laydate.now(),
      format:format,
      event: 'click',
      istime:true
    };
    var end = {
      elem:'#date-end',
      max:laydate.now(),
      format:format,
      istime:true

    };
    laydate(start);
    laydate(end);

    //快捷选择时间
    var $dateStart = $('#date-start'),
        $dateEnd = $('#date-end');
    var startFormat = 'YYYY-MM-DD 00:00:00';
    var endFormat = 'YYYY-MM-DD 23:59:59';
    //默认今天
    $dateStart.val(laydate.now(0,startFormat));
    $dateEnd.val(laydate.now(0,endFormat));
    $('.quick-date-btn').click(function(){
      var $this = $(this),
          step = $this.data('step');
      $dateStart.val(laydate.now(-1*(step-1),startFormat));
      $dateEnd.val(laydate.now(0,endFormat));
    });
    var $switch = $('#playback-switch'),
        $sidebar = $('#sidebar');
    $switch.click(function(){
      $(this).toggleClass('playback-switch-b');
      $sidebar.toggleClass('sidebar-hide');
    });
  //键盘选择下一条记录
    var checkItem = function(flag){
      var $tr = $list.find('.tr-curr'),
          i = $tr.index();
      if($tr.size()!==0){
        if(flag){
          $tr.next().click();
        }else{
          i = i<2?0:i-2;
          $tr.prev().click();
        }
        $wrap.mCustomScrollbar('scrollTo',i*40);
      }
    };
    $('body').on('keydown',function(e){
      e = e||window.event;
      if(e.keyCode===38){//prev
        checkItem();
      }else if(e.keyCode===40){//next
        checkItem(1);
      }
    });

    //倍率操作
    var $rate = $('#control-rate'),
        defaultSpeed = 2000;
    var writeSpeed = function(v){
      var realSpeed = Math.pow(2,v)/16;
      $rate.html(realSpeed);
      lushu.speed(defaultSpeed*realSpeed);
    };
    var speedSlider = $("#speed-slider").slider({
      value:4,
      min:0,
      max:8,
      step:1,
      slide:function(event,ui){
        writeSpeed(ui.value);
      }
    });
    //路书控制台
    var $control = $('#playback-control'),
        $play = $('#control-play'),
        $stop = $('#control-stop'),
        $slow = $('#control-slow'),
        $quick = $('#control-quick');
    $play.click(function(){
      var $this = $(this);
      if($this.hasClass('control-pause')){
        lushu.pause();
        $this.removeClass('control-pause');
      }else{
        lushu.start();
        $this.addClass('control-pause');
      }
    });
    $stop.click(function(){
      lushu.stop();
      $play.removeClass('control-pause');
    });
    $slow.click(function(){
      var speed = speedSlider.slider("value")-1;
      speedSlider.slider("value",speed);
      writeSpeed(speedSlider.slider("value"));
    });
    $quick.click(function(){
      var speed = speedSlider.slider("value")+1;
      speedSlider.slider("value",speed);
      writeSpeed(speedSlider.slider("value"));
    });
    //轨迹查询
    var $list = $('#playback-list'),
        $wrap = $('#playback-list-wrap'),
        $poiPop = $('#poi-pop'),
        loadingHtml = '<tr><td class="loading"></td></tr>';
    $wrap.mCustomScrollbar({'scrollInertia':0,theme:"dark-2",scrollButtons:{
      enable:false,
      scrollAmount:40,
      scrollType:'pixels'
    },keyboard:{
      enable:false,
      scrollAmount:40,
      scrollType:"stepped"
    }});
    tools.lock('#playback-search',function(obj) {
      var id = tools.linkPar('id'),
          devicenum = tools.linkPar('devicenum'),
          dateStart = $dateStart.val(),
          dateEnd = $dateEnd.val();

      var trackPoint= $('#trackPoint').is(':checked') ?  1 : 0;
      var lbs = $('#lbs').is(':checked') ?  1 : 0;
      var stopPoint= $('#stopPoint').is(':checked') ?  1 : 0;
      if(dateStart&&dateEnd){
        var dateStartD = new Date(dateStart);
        var dateEndD = new Date(dateEnd);
        var sub = dateEndD - dateStartD;
        if (sub > 1000 * 60 * 60 * 24 * 14) {
          layer.tips('时间范围不能超过2周', '#date-end', {
            tips: [2, '#ff0000'],
            time: 2000,
            anim: 6
          });
          tools.lock(obj);
          return;
        }
        //已查询过数据,先重置
        if(lushu){
          $stop.click();
          $control.hide();
          $poiPop.data('poi',-1);
          //清除路线、坐标点、车辆icon等覆盖物
          map.clearOverlays();

        }
        $list.html(loadingHtml);
        $wrap.mCustomScrollbar('update');
        console.log(obj);
        app.ajax('track/device/num.html',{'id':id,'devicenum':devicenum,'start':dateStart,'end':dateEnd,'lbs':lbs,'stopPoint':stopPoint},function(json){
          json = JSON.parse(json);
          if(json&&json.length>0){
            //配置表格数据
            $list.html(app.getList(json,'playback'));
            $wrap.mCustomScrollbar('update');
            //配置路书
            lushuGo(json,function(i){
              $list.find('.tr-curr').removeClass('tr-curr');
              $list.find('tr:eq('+i+')').addClass('tr-curr');
              i = i<4?i:(i-1);
              $wrap.mCustomScrollbar('scrollTo',i*40);
            },function(arrPois){
              var i = arrPois.length-1;
              $list.find('.tr-curr').removeClass('tr-curr');
              $list.find('tr:eq('+i+')').addClass('tr-curr');
              $wrap.mCustomScrollbar('scrollTo',i*40);
              $play.removeClass('control-pause');
            },trackPoint);
            //显示控制台
            $control.show();
            //配置路书点的点击事件
            $.each(lushuPoi,function(i,v){
              v.addEventListener('click',function(e){
                var oldNum = $poiPop.data('poi');
                if(oldNum>=0){
                  lushuPoi[oldNum].setAnimation(null);
                }
                $poiPop.data('poi',i);
                e.target.setAnimation(BMAP_ANIMATION_BOUNCE);
                /* 配置坐标点信息弹层 */
                var poiJson = json[i];
                app.getCityName(poiJson,function(addressCallBack){
                  var addres = poiJson.adres + poiJson.sdesc;
                  if('' == addres) {
                    addres = addressCallBack;
                  }
                  var popHtml = '<p>序号：'+(i+1)+'</p ><p>定位方式：'+ poiJson.pot+'</p><p>上报时间：'+poiJson.ptm+'</p ><p>速度(km/h)：'+poiJson.sp+'</p ><p>方向：'+tools.directionText(poiJson.di)+'</p ><p>总里程(km)：'+poiJson.mil+'</p ><p>位置：'+ addres +'</p >';
                  $poiPop.find('.bd').html(popHtml);
                  $poiPop.popShow({mask:false});
                });
              });
            });
            //配置列表内容的点击事件
            $list.find('tr').click(function(){
              var $this = $(this),
                  index = $this.index(),
                  oldNum = $poiPop.data('poi');
              //坐标点动画
              if(oldNum >= 0) {
                var oldLushuPoi = lushuPoi[oldNum];//上次的坐标点
                if (oldLushuPoi) {
                  oldLushuPoi.setAnimation(null);
                }
              }
              $poiPop.data('poi',index);
              var lushuPoiPoint = lushuPoi[index];//本次坐标点
              if(lushuPoiPoint) {
                lushuPoiPoint.setAnimation(BMAP_ANIMATION_BOUNCE);
              }
              //列表高亮
              $list.find('.tr-curr').removeClass('tr-curr');
              $this.addClass('tr-curr');
            });
            //按钮选择下一条记录
            var $next = $('#playback-next');
            if($next.size()==0){
              $wrap.after('<a class="playback-next" id="playback-next" href="javascript:;"></a>');
              $next = $('#playback-next');
              $next.click(function(){
                checkItem(1);
              });
            }

            //写入车辆标注
            var markerUrl = 'interest/get.html';
            app.ajax(markerUrl,{'id':id},function(json){//标注接口
              /* 测试数据 */
              //json = {'id':111,'markers':[{'markid':1,'markname':'测试名称1','markinfo':'测试备注2','lng':116.387112,'lat':39.950977},{'markid':2,'markname':'测试名称3','markinfo':'测试备注4','lng':116.375243,'lat':39.923063}]};
              /* 测试数据 */
              json = JSON.parse(json);
              markerCache = json.markers;//配置全局的标注数据缓存
              /* 配置车辆标注 */
              app.getmark(json);
            });

          }else{
            $list.html('<tr><td class="none">没有查到数据信息！</td></tr>');
            $wrap.mCustomScrollbar('update');
          }
        },5*60*1000);
      }else{
        if(dateStart) {
          layer.tips('请选择结束时间', '#date-end', {
            tips: [2, '#ff0000'],
            time: 2000,
            anim: 6
          });
        } else if(dateEnd) {
          layer.tips('请选择开始时间', '#date-start', {
            tips: [2, '#ff0000'],
            time: 2000,
            anim: 6
          });
        }

      }
      tools.lock(obj);
    });

    tools.lock('#playback-export',function(obj){
          var id = tools.linkPar('id'),
          devicenum = tools.linkPar('devicenum'),
          dateStart = $dateStart.val(),
          dateEnd = $dateEnd.val();
      var lbs = $('#lbs').is(':checked') ?  1 : 0;
      var stopPoint= $('#stopPoint').is(':checked') ?  1 : 0;
          //var lbs = $('input:radio:checked').val();
      if(dateStart&&dateEnd){
            //显示控制台
          $control.show();
        var param = "devicenum=" + devicenum +
            "&id=" + id + "&lbs="+lbs + "&stopPoint="+stopPoint
            + "&start="+ dateStart + "&end="+ dateEnd;
        window.open("track/toExport.html?" + param,"_blank");
      }else{
        if(dateStart) {
          layer.tips('请选择结束时间', '#date-end', {
            tips: [2, '#ff0000'],
            time: 2000,
            anim: 6
          });
        } else if(dateEnd) {
          layer.tips('请选择开始时间', '#date-start', {
            tips: [2, '#ff0000'],
            time: 2000,
            anim: 6
          });
        }
      }
      tools.lock(obj);
    });
    //查看坐标具体地址
    $list.on('click','.view-address',function(){
      var $this = $(this),
          json = {
            lng:$this.data('lng'),
            lat:$this.data('lat')
          };
      app.getCityName(json,function(address){
        $this.after(address).hide();
      });
    });
    app.setmark();
  },
  init:function(){
    //app.position();
  }
};
app.init();