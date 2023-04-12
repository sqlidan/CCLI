<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>

    <form id="mainform" action="${ctx}/wms/plan/form/${action}" method="post">

        <input type="hidden" id="planId" name="planId" value="${planId}">
        <table id="selectRadio"></table>

    </form>

</div>

<script type="text/javascript">

    $(function () {
        initSelect();
    });

    function initSelect() {

        var url = "${ctx}/wms/plan/time/list";

        $.get(url, function (data) {

            var selectRadio = $("#selectRadio");

            for (var i = 0; i < data.length; i++) {

                var row = data[i];

                var planTime = row.PLANDATE;
                var large = row.LARGENO;
                var total = row.TOTAL;

                var date = planTime.substring(0, 2) + ":" + planTime.substring(2, 4);

                var itemText = "<td>" + date + "</td><td>最大可约" + large + "车次</td><td>已约" + total + "车次</td>";

                var item = "<tr><td><input type='radio' name='selectTimeType' ";

                if (large <= total) {
                    item += " disabled ";
                }

                item += " value='" + row.ID + "'></td>" + itemText + "</tr>";

                selectRadio.append(item);
            }

            if (data.length == 0) {
                selectRadio.append("没有可预约时间");
            }

        });

    }


</script>
</body>
</html>