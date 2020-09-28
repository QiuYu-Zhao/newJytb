var tools = window.tools,
    app = window.app;
app.electricfence = function(){
  window.fence();
  app.citySearch();
  var efid = tools.linkPar('efid'),//当前电子围栏ID
      type = tools.linkPar('type'),
      $name = $('#electricfence-name'),
      $type = $('#electricfence-type'),
      $group = $('#electricfence-group'),
      $cars = $('#electricfence-cars'),
      $carsPop = $('#electricfence-cars-pop'),
      $list = $('#electricfence-cars-list'),
      $page = $('#electricfence-cars-page'),
      $clear = $('#electricfence-clear'),
      $save = $('#electricfence-save'),
      perpage = 15;
  //获取当前电子围栏信息
  var getEF = function(){
    var postUrl = '/mk/map/electricFence/getElectricFenceById.html',//电子围栏数据接口
        postData = {efid:efid};
    app.ajax(postUrl,postData,function(result){
      /* 测试数据 */

      var json = JSON.parse(result);
      /*json = {
        name:'我是电子围栏名称',//围栏名称
        type:'1',//围栏类型
        groupid:1,//分组ID
        cars:[1,11,111,6,113,2,3,4,12,13,14,15,16,17],
        posArr:[{
          lat:39.936052,
          lng:116.386246
        },{
          lat:39.936052,
          lng:116.388401
        },{
          lat:39.934281,
          lng:116.388401
        },{
          lat:39.934281,
          lng:116.386246
        }]
      };*/
      /* 测试数据 */
      if(json&&json.name){
        $name.val(json.name);
        $type.val(json.type);
        //获取分组信息
        getGroup(function(){
          $group.val(json.groupid);
          //获取绑定车辆信息
          getCars(json.groupid,json.cars,function(){
            //配置页面车辆相关信息
            getList(getCarData());
          });
        });
        //添加电子围栏覆盖物
        var localePoint = [];
        $.each(json.posArr,function(i,v){
          var poi = new BMap.Point(v.lng,v.lat);
          localePoint.push(poi);
        });
        geofenceOverlays = new BMap.Polygon(localePoint,{strokeColor:"#567efd", strokeWeight:2, strokeOpacity:0.8,fillColor:"#567efd",fillOpacity:0.5,enableEditing:true});
        map.addOverlay(geofenceOverlays);
        //最佳视角
        var viewport = map.getViewport(localePoint);
        map.centerAndZoom(viewport.center,viewport.zoom);
        //开启只读模式
        if(type=='readonly'){
          $name.attr('disabled',true);
          $type.attr('disabled',true);
          $group.attr('disabled',true);
          $cars.off('focus');
          $clear.off('click');
          $save.off('click');
          geofenceOverlays.disableEditing();
        }
      }else{
        efid = '';//无效id
      }
    });
  };
  //获取分组信息
  var getGroup = function(callback){
    var postUrl = '/mk/map/electricFence/getOrgGroup.html',//所属分组数据接口
        postData = {};//跟电子围栏关联可传{efid:efid}
    app.ajax(postUrl,postData,function(result){
      /* 测试数据 */
     /* json = [{
        groupid:1,
        name:'分组1'
      },{
        groupid:2,
        name:'分组2'
      },{
        groupid:3,
        name:'分组3'
      }];*/

      var json = JSON.parse(result);

      /* 测试数据 */
      var list = ['<option value="0">选择分组</option>'];
      $.each(json,function(i,v){
        list.push('<option value="'+v.groupid+'">'+v.name+'</option>');
      });
      $group.html(list.join(''));
      if(callback){
        callback();
      }
    });
  };
  //选择分组信息
  $group.change(function(){
    var $this = $(this),
        groupid = $this.val();
    if(groupid){
      if(groupid != "0"){
        $this.attr('disabled',true);
      }
      getCars(groupid,'',function(){
        $this.attr('disabled',false);
        getList(getCarData());
      });
    }
  });
  //获取绑定车辆信息
  var getCars = function(groupid,data,callback){
    var postUrl = '/mk/map/electricFence/getOrgCars.html',//绑定车辆数据接口
        postData = {groupid:groupid};
    app.ajax(postUrl,postData,function(result){
      /* 测试数据 */

      var json = JSON.parse(result);
      /*json = [{
        pname:'没名字租赁公司',
        cars:[{
          id:1,
          carnum:'京A00001'
        },{
          id:2,
          carnum:'京A00002'
        },{
          id:3,
          carnum:'京A00003'
        },{
          id:4,
          carnum:'京A00004'
        },{
          id:5,
          carnum:'北京分公司-京A00005'
        },{
          id:6,
          carnum:'北京分公司-京A00006'
        }]
      },{
        pname:'有名字租赁公司',
        cars:[{
          id:11,
          carnum:'北京分公司-京A00011'
        },{
          id:12,
          carnum:'北京分公司-京A00012'
        },{
          id:13,
          carnum:'北京分公司-京A00013'
        },{
          id:14,
          carnum:'北京分公司-京A00014'
        },{
          id:15,
          carnum:'北京分公司-京A00015'
        },{
          id:16,
          carnum:'北京分公司-京A00016'
        }]
      },{
        pname:'好名字租赁公司',
        cars:[{
          id:111,
          carnum:'京A00111'
        },{
          id:112,
          carnum:'京A00112'
        },{
          id:113,
          carnum:'京A00113'
        },{
          id:114,
          carnum:'京A00114'
        },{
          id:115,
          carnum:'京A00115'
        },{
          id:116,
          carnum:'京A00116'
        }]
      }];*/
      /* 测试数据 */
      var list = [];
      $.each(json,function(i,v){
        var list2 = [],
            style1 = 'curr';
        $.each(v.cars,function(ii,vv){
          var style2 = data&&data.indexOf(vv.id)>-1?'curr':'';
          if(style2===''){
            style1 = '';
          }
          list2.push('<span class="'+style2+'" data-id="'+vv.id+'">'+vv.carnum+'</span>');
        });
        list.push('<p class="'+style1+'">'+v.pname+'</p><div>'+list2.join('')+'</div>');
      });
      $carsPop.html(list.join(''));
      if(callback){
        callback();
      }
    });
  };
  //选择绑定车辆
  $carsPop.on('click','p',function(){
    var $this = $(this),
        $child = $this.next('div');
    $this.toggleClass('curr');
    if($this.hasClass('curr')){
      $child.find('span').addClass('curr');
    }else{
      $child.find('span').removeClass('curr');
    }
    getList(getCarData());
  });
  $carsPop.on('click','span',function(){
    var $this = $(this),
        $parent = $this.parent('div'),
        $p = $parent.prev('p');
    $this.toggleClass('curr');
    var count = $parent.children().length,
        now = $parent.children('.curr').length;
    if(count==now){
      $p.addClass('curr');
    }else{
      $p.removeClass('curr');
    }
    getList(getCarData());
  });
  //选择绑定车辆弹层
  var carsPopTimeout = '';
  $cars.focus(function(){
    var groupid = $group.val();
    if(groupid==0){
      $.fn.popAlert('请先选择所属分组！');
      $carsPop.html('').hide();
    }else{
      $carsPop.show();
    }
  }).blur(function(){
    carsPopTimeout = setTimeout(function(){
      $carsPop.hide();
    },100);
  });
  $carsPop.click(function(){
    clearTimeout(carsPopTimeout);
    $cars.focus();
  });
  //获取已选车辆数据
  var getCarData = function(){
    var $curr = $carsPop.find('span.curr'),
        carData = [];
    $cars.val('已选择'+$curr.length+'辆');
    $curr.each(function(i,v){
      var $v = $(v),
          id = $v.data('id'),
          carnum = $v.html().split("-")[0];
      carData.push({id:id,carnum:carnum});
    });
    return carData;
  };
  //配置已选车辆列表
  var getList = function(data){
    var list = [];
    $.each(data,function(i,v){
      var style = i>perpage-1?'hide ':'';
      list.push('<li class="'+style+'fl">'+v.carnum+'</li>');
    });
    $list.children('ul').html(list.join(''));
    if(data.length>perpage){
      $page.show().data('page',0).data('pageall',Math.ceil(data.length/perpage));
    }else{
      $page.hide();
    }
  };
  //配置车辆列表翻页
  $page.on('click','a',function(){
    var $this = $(this),
        $prev = $page.children('a:first'),
        $next = $page.children('a:last'),
        type = $this.data('type'),
        page = $page.data('page'),
        pageall = $page.data('pageall');
    if(!$this.hasClass('disabled')){
      $list.find('li').addClass('hide');
      if(type){//下一页
        page++;
        $list.find('li:gt('+(page*perpage-1)+'):lt('+perpage+')').removeClass('hide');
        $page.data('page',page);
      }else{//上一页
        page--;
        var gt = page===0?'':':gt('+(page*perpage-1)+')';
        $list.find('li'+gt+':lt('+perpage+')').removeClass('hide');
        $page.data('page',page);
      }
      if(page==pageall-1){
        $next.addClass('disabled');
      }else{
        $next.removeClass('disabled');
      }
      if(page===0){
        $prev.addClass('disabled');
      }else{
        $prev.removeClass('disabled');
      }
    }
  });
  //取消添加
  $clear.click(function(){
    map.removeOverlay(geofenceOverlays);
    geofenceOverlays = '';
    //执行什么
  });
  //添加电子围栏
  tools.lock('#electricfence-save',function(obj){
    var name = $name.val(),
        type = $type.val(),
        groupid = $group.val(),
        cars = (function(){
          var temp = [];
          $.each(getCarData(),function(i,v){
            temp.push(v.id);
          });
          return temp;
        })();
    if(!name){
      $.fn.popAlert('请输入围栏名称！');
      tools.lock(obj);
    }else if(type=='0'){
      $.fn.popAlert('请选择围栏类型！');
      tools.lock(obj);
    }else if(groupid==0){
      $.fn.popAlert('请选择所属分组！');
      tools.lock(obj);
    }else if(cars.length===0){
      $.fn.popAlert('请选择绑定车辆！');
      tools.lock(obj);
    }else if(!geofenceOverlays){
      $.fn.popAlert('请在地图上画出围栏区域！');
      tools.lock(obj);
    }else{
      var postUrl,
          postData = {name:name,type:type,groupid:groupid,cars:JSON.stringify(cars),posArr:JSON.stringify(geofenceOverlays.getPath())};
      if(efid){
        postUrl = 'update/electricFence.html';//修改电子围栏接口
        postData.efid = efid;
      }else{
        postUrl = 'save/electricFence.html';//新建电子围栏接口
      }
      app.ajax(postUrl,postData,function(json){
        /* 测试数据 */
        json = true;
        console.log(postData);
        /* 测试数据 */
        if(json){
          if(efid){
            $.fn.popAlert('修改电子围栏成功！');
          }else{
            $.fn.popAlert('新建电子围栏成功！','提示',[{
              callback:function(){
                location.reload();
              }
            }]);
          }
        }else{
          $.fn.popAlert('电子围栏设置失败，请重试！');
        }
        tools.lock(obj);
      });
    }
  });
  if(efid){
    getEF();
  }else{
    $name.val('');
    $type.val(0);
    getGroup();
    getCarData();//配置当前选择车辆信息
  }
};


