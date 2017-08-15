<#macro deliveryType type>
    <#if "${type}"=="0">
        自取
    <#elseif "${type}"=="1">
        快递
    <#elseif "${type}"=="2">
        电子保单
    <#else>
        未知
    </#if>
</#macro>