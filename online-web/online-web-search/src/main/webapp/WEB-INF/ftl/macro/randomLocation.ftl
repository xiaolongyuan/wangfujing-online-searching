<#-- @ftlvariable name="random" type="java.util.Random" -->
<#macro randomLocation hosts template>${template?replace('{host}', hosts[random.nextInt(hosts?size)])}</#macro>