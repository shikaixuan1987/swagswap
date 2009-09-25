<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%--Shows a search box and a list of SwagItems using the old but good displaytag library --%>

<form:form action="/swag/search" commandName="searchCriteria" name="searchForm" method="GET">

	<c:if test="${not empty searchCriteria.searchString}">
		<h2>Swag Item Search: ${searchCriteria.searchString}</h2>
	</c:if>
	<c:if test="${empty searchCriteria.searchString}">
		<h2>All Swag Items</h2>
	</c:if>

	Note: Search is case sensitive, full word, and only on Name and Tags in this implementation
	<table>
		<tr>
			<td><form:input path="searchString" />
				<a href="#" onclick="document.searchForm.submit()">
				<img src="/images/icon_flashlight.gif" border="0"/>Search Swag</a>
				&nbsp; 
				<a href="/swag/add"><img src="/images/newAdd.png" 
				   title="Add SwagItem" border="0"/>Add Swag</a>
			</td>
		</tr>
	</table>
</form:form>

<display:table name="swagItems" uid="swagItemsList" id="currentObject" 
               requestURI="/swag/search" keepStatus="true">
	<display:column sortable="true" property="name" />

	<display:column title="Action" style="width: 74px;">
		<a href="<c:url value='/swag/edit/${currentObject.key}'/>"> 
			<img border="0" alt="Edit" src="<%=request.getContextPath()%>/images/edit.gif"/></a>
	        &nbsp;&nbsp;
            <a href="<c:url value='/swag/delete/${currentObject.key}'/>" onclick="return confirmSubmit()"> 
            <img border="0" alt="Delete" src="<%=request.getContextPath()%>/images/delete.gif"/>
		</a>
	</display:column>

	<display:column property="tags" decorator="com.swagswap.web.springmvc.displaytag.TagsDecorator"/>

	<c:if test="${not empty currentObject.imageKey}">
		<display:column title="Image">
			<img border="0" alt="Image" src="<c:url value='/swag/showImage/${currentObject.imageKey}'/>"/>
		</display:column>
	</c:if>

	<display:column property="lastUpdated" title="Last Updated" format="{0,date,dd-MM-yyyy HH:mm:ss}" sortable="true"/>
	
</display:table>

<script language="JavaScript">
    document.forms[0].searchString.focus();
    document.forms[0].searchString.select();

	function confirmSubmit(name)
	{
	var agree=confirm("Are you sure you want to delete this item?");
	if (agree)
		return true ;
	else
		return false ;
	}

</script>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>