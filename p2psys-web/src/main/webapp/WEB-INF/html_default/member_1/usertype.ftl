<#macro getUserType name>
	<#assign length=name?length>
	<#if length&gt;15>
		${name[0..15]}
	<#else>
		${name}
	</#if>
</#macro>