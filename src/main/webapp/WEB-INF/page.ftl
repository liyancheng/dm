<script type="text/javascript">
    function pageIndex(i) {

        window.self.location.href = "${ctx}/courseStudy/listCorp.do?"+$('#searchForm').serialize()+"&page.index="+i;
    }

    function toHome() {
        window.self.location.href = "${ctx}";
    }
</script>

<form>
    <input type="hidden" id="action" value="list"/>
</form>
<#--分页-->
<#macro pagination page >
    <#if (page ?? && page.total > 0)>
        <#if page.isFirst()>
        <span>首页</span>
        <span>上一页</span>
        <#else>
        <a href="javascript:pageIndex(1)">首页</a>
        <a href="javascript:pageIndex(${page.index-1})">上一页</a>
        </#if>

        <#if page.isLast()>
        <span>下一页</span>
        <span>尾页</span>
        <#else>
        <a href="javascript:pageIndex(${page.index+1})">下一页</a>
        <a href="javascript:pageIndex(${page.count})">尾页</a>
        </#if>

    <span>共${page.total}条</span>
    <span>${page.index}/${page.count}页</span>

    <#else>
    <span>首页</span>
    <span>上一页</span>
    <span>下一页</span>
    <span>尾页</span>
    <span>共${page.total}条</span>
    <span>${page.index}/${page.count}页</span>
    </#if>
</#macro>