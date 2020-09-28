/**
 * Created by Administrator on 2017/8/29.
 * zxr
 */
jQuery(document).ready(function () {
    var tools = window.tools,
        app = window.app;
    var mask;

    //点击收缩
    var $switch = $('#playback-switch'),
        $sidebar = $('#sidebar');
    $switch.click(function(){
        $(this).toggleClass('playback-switch-b');
        $sidebar.toggleClass('sidebar-hide');
    });
    //安装公司下拉优化
    jQuery.browser={};(function(){jQuery.browser.msie=false; jQuery.browser.version=0;if(navigator.userAgent.match(/MSIE ([0-9]+)./)){ jQuery.browser.msie=true;jQuery.browser.version=RegExp.$1;}})();
    $('.installselect').chosen({no_results_text: "未查询到数据!"});

    if( $(".chzn-results li").length > 1){
        $("input[name='Device']:eq(0)").attr("checked",'checked');
        $("#DeviceType").val($("input[name='Device']:eq(0)").val());
        $(":radio").click(function(){
            $("#DeviceType").val($(this).val());
        });
        //查询
        $("#playback-search").click(function () {
            var orgId = $("#install_id").val();
            var deviceType = $("#DeviceType").val();
            if (orgId != "" && deviceType != "") {
                mask = layer.load(1, {shade: [0.5, '#000000']});
                postUrl = '/mk/map/device/dis/listDis.html',//请求链接
                    postData = {
                        orgId: orgId,
                        deviceType: deviceType,
                        time: 3, stopTime: ''
                    };//请求参数
                app.ajax(postUrl, postData, function (reslut) {
                    layer.close(mask);
                    var json = JSON.parse(reslut);
                    var marker = null;
                    var markers = [],
                        pois = [],
                        onePoi = '';
                    if(json.aaData.length > 0){
                        var map = new BMap.Map("map",{enableMapClick:false});
                        map.centerAndZoom(new BMap.Point(116.413554,39.911013),11);
                        map.enableScrollWheelZoom();
                        var navigationControl = new BMap.NavigationControl({
                            anchor:BMAP_ANCHOR_TOP_RIGHT,//靠左上角位置
                            type:BMAP_NAVIGATION_CONTROL_LARGE, //LARGE类型
                            offset: new BMap.Size(5, 100)
                        });
                        map.addControl(navigationControl);
                        var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});// 左下角，添加比例尺
                        map.addControl(top_left_control);
                        var mapType = new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});//2D图,卫星图
                        map.addControl(mapType);
                        var distanceTool;
                        var distanceToolControl = function (){
                            this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
                            this.defaultOffset = new BMap.Size(100,10);
                        };
                        distanceToolControl.prototype = new BMap.Control();
                        distanceToolControl.prototype.initialize = function(map){
                            var div = document.createElement("div"),
                                $div = $(div);
                            $div.addClass('distance-tool').html('<i></i><span>测距</span>');
                            $div.click(function(){
                                distanceTool.open();
                            }).hover(function(){
                                $(this).addClass('distance-tool-hover');
                            },function(){
                                $(this).removeClass('distance-tool-hover');
                            });
                            map.getContainer().appendChild(div);
                            return div;
                        };
                        tools.plugin({
                            path:'/js/map/',
                            arr:['DistanceTool_min'],
                            success:function(){
                                distanceTool = new BMapLib.DistanceTool(map);
                                var mydistanceToolCtrl = new distanceToolControl();
                                map.addControl(mydistanceToolCtrl);
                            }
                        });
                        for (var i = 0; i < json.aaData.length; i++) {
                            var jsonI = json.aaData[i];
                            var myIcon = new BMap.Icon('/image/map/car' + jsonI.icon + '.png', new BMap.Size(50, 26));
                            var poi = new BMap.Point(jsonI.lng, jsonI.lat);
                            marker = new BMap.Marker(poi, {icon: myIcon});
                            var popHtml = '<p>车主姓名：' + jsonI.username + '</p>' + '<p>设备类型：' + jsonI.deviceType + '</p>' +
                                '<p>定位方式：' + jsonI.posType + '</p><p>上报时间：' + jsonI.endTime + '</p><p>状态：' + jsonI.state + '</p>' +
                                '<p>VIN号：' + jsonI.vin + '</p>' +
                                '<p>位置：<span class="device-address">' + jsonI.address + '</span></p>';
                            addClickHandler(marker,popHtml);
                            pois.push(poi);
                            markers.push(marker);
                        }
                        function addClickHandler(marker,popHtml){
                            marker.addEventListener("click",function(e){
                                /* 配置坐标点信息弹层 */
                                $('#poi-pop').find('.bd').html(popHtml);
                                $('#poi-pop').popShow({mask: false});
                            });
                        }
                        var viewport = map.getViewport(!onePoi ? pois : onePoi);
                        map.centerAndZoom(viewport.center, viewport.zoom);
                        var markerClusterer = new BMapLib.MarkerClusterer(map, {markers: markers});
                    }else{
                        var map = new BMap.Map("map",{enableMapClick:false});
                        map.centerAndZoom(new BMap.Point(116.413554,39.911013),11);
                        map.enableScrollWheelZoom();
                        //添加带有定位的导航控件
                        var navigationControl = new BMap.NavigationControl({
                            anchor:BMAP_ANCHOR_TOP_RIGHT,//靠左上角位置
                            type:BMAP_NAVIGATION_CONTROL_LARGE, //LARGE类型
                            offset: new BMap.Size(5, 100)
                        });
                        map.addControl(navigationControl);
                        // /** 添加比例尺***/
                        var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});// 左下角，添加比例尺
                        map.addControl(top_left_control);
                        //添加地图类型
                        var mapType = new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});//2D图,卫星图
                        map.addControl(mapType);
                        //定义测距控件
                        var distanceTool;
                        //定义测距控件按钮类
                        var distanceToolControl = function (){
                            // 默认停靠位置和偏移量
                            this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
                            this.defaultOffset = new BMap.Size(100,10);
                        };
                        //继承BMap.Control
                        distanceToolControl.prototype = new BMap.Control();
                        //创建div元素作为控件的容器,并将其添加到地图容器中
                        distanceToolControl.prototype.initialize = function(map){
                            // 创建一个DOM元素
                            var div = document.createElement("div"),
                                $div = $(div);
                            $div.addClass('distance-tool').html('<i></i><span>测距</span>');
                            // 绑定事件,点击开启测距
                            $div.click(function(){
                                distanceTool.open();
                            }).hover(function(){
                                $(this).addClass('distance-tool-hover');
                            },function(){
                                $(this).removeClass('distance-tool-hover');
                            });
                            // 添加DOM元素到地图中
                            map.getContainer().appendChild(div);
                            // 将DOM元素返回
                            return div;
                        };
                        //实例化测距相关
                        tools.plugin({
                            path:'/js/map/',
                            arr:['DistanceTool_min'],
                            success:function(){
                                //实例化测距工具和测距工具按钮
                                distanceTool = new BMapLib.DistanceTool(map);
                                // 创建测距控件按钮
                                var mydistanceToolCtrl = new distanceToolControl();
                                // 添加到地图当中
                                map.addControl(mydistanceToolCtrl);
                            }
                        });
                        layer.close(mask);
                        $.fn.popAlert('暂无数据！');
                    }
                });
            }else{
                layer.close(mask);
                $.fn.popAlert('请填写完整！');
            }
            $('#poi-pop').hide();
        });
    }else{
        $(".control-group").hide();
        $("input[name='Device']:eq(0)").attr("checked",'checked');
        $("#DeviceType").val($("input[name='Device']:eq(0)").val());
        $(":radio").click(function(){
            $("#DeviceType").val($(this).val());
        });
        //查询
        $("#playback-search").click(function () {
            var orgId = 0;
            var deviceType = $("#DeviceType").val();
            if ( deviceType != "") {
                mask = layer.load(1, {shade: [0.5, '#000000']});
                postUrl = '/mk/map/device/dis/listDis.html',//请求链接
                    postData = {
                        orgId: orgId,
                        deviceType: deviceType,
                        time: 3, stopTime: ''
                    };//请求参数
                app.ajax(postUrl, postData, function (reslut) {
                    layer.close(mask);
                    var json = JSON.parse(reslut);
                    var marker = null;
                    var markers = [],
                        pois = [],
                        onePoi = '';
                    if(json.aaData.length > 0){
                        var map = new BMap.Map("map",{enableMapClick:false});
                        map.centerAndZoom(new BMap.Point(116.413554,39.911013),11);
                        map.enableScrollWheelZoom();
                        var navigationControl = new BMap.NavigationControl({
                            anchor:BMAP_ANCHOR_TOP_RIGHT,//靠左上角位置
                            type:BMAP_NAVIGATION_CONTROL_LARGE, //LARGE类型
                            offset: new BMap.Size(5, 100)
                        });
                        map.addControl(navigationControl);
                        var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});// 左下角，添加比例尺
                        map.addControl(top_left_control);
                        var mapType = new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});//2D图,卫星图
                        map.addControl(mapType);
                        var distanceTool;
                        var distanceToolControl = function (){
                            this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
                            this.defaultOffset = new BMap.Size(100,10);
                        };
                        distanceToolControl.prototype = new BMap.Control();
                        distanceToolControl.prototype.initialize = function(map){
                            var div = document.createElement("div"),
                                $div = $(div);
                            $div.addClass('distance-tool').html('<i></i><span>测距</span>');
                            $div.click(function(){
                                distanceTool.open();
                            }).hover(function(){
                                $(this).addClass('distance-tool-hover');
                            },function(){
                                $(this).removeClass('distance-tool-hover');
                            });
                            map.getContainer().appendChild(div);
                            return div;
                        };
                        tools.plugin({
                            path:'/js/map/',
                            arr:['DistanceTool_min'],
                            success:function(){
                                distanceTool = new BMapLib.DistanceTool(map);
                                var mydistanceToolCtrl = new distanceToolControl();
                                map.addControl(mydistanceToolCtrl);
                            }
                        });
                        for (var i = 0; i < json.aaData.length; i++) {
                            var jsonI = json.aaData[i];
                            var myIcon = new BMap.Icon('/image/map/car' + jsonI.icon + '.png', new BMap.Size(50, 26));
                            var poi = new BMap.Point(jsonI.lng, jsonI.lat);
                            marker = new BMap.Marker(poi, {icon: myIcon});
                            /* 配置坐标点信息弹层 */
                            var popHtml = '<p>车主姓名：' + jsonI.username + '</p>' + '<p>设备类型：' + jsonI.deviceType + '</p>' +
                                '<p>定位方式：' + jsonI.posType + '</p><p>上报时间：' + jsonI.endTime + '</p><p>状态：' + jsonI.state + '</p>' +
                                '<p>VIN号：' + jsonI.vin + '</p>' +
                                '<p>位置：<span class="device-address">' + jsonI.address + '</span></p>';
                            addClickHandler(marker,popHtml);
                            pois.push(poi);
                            markers.push(marker);
                        }
                        function addClickHandler(marker,popHtml){
                            marker.addEventListener("click",function(e){
                                /* 配置坐标点信息弹层 */
                                $('#poi-pop').find('.bd').html(popHtml);
                                $('#poi-pop').popShow({mask: false});
                            });
                        }
                        var viewport = map.getViewport(!onePoi ? pois : onePoi);
                        map.centerAndZoom(viewport.center, viewport.zoom);
                        var markerClusterer = new BMapLib.MarkerClusterer(map, {markers: markers});
                    }else{
                        var map = new BMap.Map("map",{enableMapClick:false});
                        map.centerAndZoom(new BMap.Point(116.413554,39.911013),11);
                        map.enableScrollWheelZoom();
                        //添加带有定位的导航控件
                        var navigationControl = new BMap.NavigationControl({
                            anchor:BMAP_ANCHOR_TOP_RIGHT,//靠左上角位置
                            type:BMAP_NAVIGATION_CONTROL_LARGE, //LARGE类型
                            offset: new BMap.Size(5, 100)
                        });
                        map.addControl(navigationControl);
                        // /** 添加比例尺***/
                        var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});// 左下角，添加比例尺
                        map.addControl(top_left_control);
                        //添加地图类型
                        var mapType = new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});//2D图,卫星图
                        map.addControl(mapType);
                        //定义测距控件
                        var distanceTool;
                        //定义测距控件按钮类
                        var distanceToolControl = function (){
                            // 默认停靠位置和偏移量
                            this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
                            this.defaultOffset = new BMap.Size(100,10);
                        };
                        //继承BMap.Control
                        distanceToolControl.prototype = new BMap.Control();
                        //创建div元素作为控件的容器,并将其添加到地图容器中
                        distanceToolControl.prototype.initialize = function(map){
                            // 创建一个DOM元素
                            var div = document.createElement("div"),
                                $div = $(div);
                            $div.addClass('distance-tool').html('<i></i><span>测距</span>');
                            // 绑定事件,点击开启测距
                            $div.click(function(){
                                distanceTool.open();
                            }).hover(function(){
                                $(this).addClass('distance-tool-hover');
                            },function(){
                                $(this).removeClass('distance-tool-hover');
                            });
                            // 添加DOM元素到地图中
                            map.getContainer().appendChild(div);
                            // 将DOM元素返回
                            return div;
                        };
                        //实例化测距相关
                        tools.plugin({
                            path:'/js/map/',
                            arr:['DistanceTool_min'],
                            success:function(){
                                //实例化测距工具和测距工具按钮
                                distanceTool = new BMapLib.DistanceTool(map);
                                // 创建测距控件按钮
                                var mydistanceToolCtrl = new distanceToolControl();
                                // 添加到地图当中
                                map.addControl(mydistanceToolCtrl);
                            }
                        });
                        layer.close(mask);
                        $.fn.popAlert('暂无数据！');
                    }
                });
            }else{
                layer.close(mask);
                $.fn.popAlert('请填写完整！');
            }
            $('#poi-pop').hide();
        });
    }
});