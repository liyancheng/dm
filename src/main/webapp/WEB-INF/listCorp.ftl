<!DOCTYPE html>
<html>
<head>
<#include '/WEB-INF/vars.ftl'/>
<#include '/WEB-INF/page.ftl'/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>用户学习课程数据迁移</title>
    <!--jquery插件-->
    <script type="text/javascript" language=Javascript src="${ctx}/js/jquery-1.8.0.min.js"></script>
</head>

<body>
<h2 style="text-align: center">用户学习课程数据迁移页面</h2>

<div style="text-align: center">
    <form id="searchForm">
        <div style="text-align: center">
            <select name="status">
            <#if (status=="notExecuting")>
                <option value="notExecuting" selected="selected">未迁移</option>
                <option value="all">全部</option>
                <option value="success">迁移成功</option>
            <#elseif (status=="success")>
                <option value="all">全部</option>
                <option value="notExecuting">未迁移</option>
                <option value="success" selected="selected">迁移成功</option>
            <#else>
                <option value="all" selected="selected">全部</option>
                <option value="notExecuting">未迁移</option>
                <option value="success">迁移成功</option>
            </#if>
            </select>
    <span style="text-align: right;margin-left:10px; margin-right:10px">
        <input type="text" name="corpCode" placeholder="公司编码/公司名称" value="${corpCode!}"/>
        <a href="javascript:void(0)" onclick="doSearch()">搜 索</a>
    </span>

        </div>
    </form>

    <div>
        <div style="text-align: center">
            <table style="margin: 0 auto; width: 600px;">
                <tr>
                    <td style="text-align: center;margin-left:50px; margin-right:50px">公司编号</td>
                    <td style="text-align: center;margin-left:50px; margin-right:50px">公司名称</td>
                    <td style="text-align: center;margin-left:50px; margin-right:50px">操作</td>
                </tr>
            <#if page??&&page.data??>
                <#list page.data as userCourseData>
                    <tr>
                        <td style="text-align: center;margin-left:50px; margin-right:50px;height: 30px;">${userCourseData.corpCode!}</td>
                        <td style="text-align: center;margin-left:50px; margin-right:50px;height: 30px;">${userCourseData.corpName!}</td>
                        <td style="text-align: center;margin-left:50px; margin-right:50px;height: 30px;">
                            <#assign status="${userCourseData.status!}" />
                            <#if (status=="executing")>
                                执行中
                            <#elseif (status=="notExecuting" || status=="failed")>
                                <a href="javascript:void(0)" class="dataMove" data-corp="${userCourseData.corpCode!}">数据迁移</a>
                            <#elseif (status=="success")>
                                迁移成功
                            <#else >
                                --
                            </#if>
                        </td>
                    </tr>
                </#list>
            </#if>
            </table>
        </div>
    <@pagination page/>
        <form id="pagination" method="post" action="">
            <input type="hidden" id="index" value="${page.index!}"/>
            <input type="hidden" id="total" value="${page.total!}"/>
        </form>
    </div>
</div>

</body>

<script type="text/javascript">
    function doSearch() {
        var param = $('#searchForm').serialize();
        window.self.location.href = "${ctx}/courseStudy/listCorp.do?" + param;
    }
    $(function () {
        $('.dataMove').on('click', function () {
            var corpCode = $(this).data('corp');
            var $tbParent = $(this).parent('td');
            $tbParent.html('迁移中');
            $.post(
                    "${ctx}/courseStudy/userCourseDataMove.do",
                    {'corpCode': corpCode},
                    function (data) {
                        if (data.result == 'success') {
                            $tbParent.html('迁移成功');
                        } else {
                            alert(data.msg);
                        }
                    }, 'json'
            );
        });
    });
</script>
</html>