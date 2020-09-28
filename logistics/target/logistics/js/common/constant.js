
//table常量
var dataTableConstant = {

    get : function () {
        var json = {
            "sZeroRecords" : "没有检索到数据",
            "sInfo": "显示 _START_ 至 _END_ 条 &nbsp;&nbsp;共 _TOTAL_ 条",
            "sInfoFiltered": "(筛选自 _MAX_ 条数据)",
            "sInfoEmtpy": "没有数据",
            "infoEmpty": "没有数据",
            "search":"查找:",
            "oPaginate":
            {
                "sFirst": "首页",
                "sPrevious": "前一页",
                "sNext": "后一页",
                "sLast": "末页"
            },
            "pagingType": "full_numbers"//翻页空间样式
        }

        return json;
    }
}