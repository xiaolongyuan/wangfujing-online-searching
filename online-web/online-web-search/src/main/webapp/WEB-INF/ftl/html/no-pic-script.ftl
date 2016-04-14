<#-- @ftlvariable name="cssHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="cssLocationTemplate" type="java.lang.String" -->
<#include "../macro/randomLocation.ftl">
<script type="text/javascript">
    var tags = document.getElementsByTagName("img");
    for (var i = 0; i < tags.length; i++) {
        tags.item(i).onerror = function () {
            this.src = '<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/img/public/no-pic.png';
            <#--noinspection JSUnusedGlobalSymbols-->
            this.onerror = null;
        }
    }
</script>