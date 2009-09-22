<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>



<form:form action="/swag/swagItem/search" commandName="searchCriteria" name="searchForm" method="GET">

<c:if test="${not empty searchCriteria.searchString}">
	<h2>Swag Item Search: ${searchCriteria.searchString}</h2>
</c:if>
<c:if test="${empty searchCriteria.searchString}">
	<h2>All Swag Items</h2>
</c:if>
	<table>
		<tr>
			<td><form:input path="searchString" />
				<a href="#" onclick="document.searchForm.submit()">
				<img src="/images/icon_flashlight.gif" border="0"/>Search Swag</a>
				&nbsp; 
				<a href="/swag/swagItem/add"><img src="/images/newAdd.png" 
				   title="Add SwagItem" border="0"/>Add Swag</a>
			</td>
		</tr>
	</table>
</form:form>

<display:table name="swagItems" uid="swagItemsList" id="currentObject" 
               requestURI="/swag/listSwagItems" keepStatus="true">
	<display:column sortable="true" property="name" />

	<display:column title="Action" style="width: 74px;">
		<a href="<c:url value='/swag/swagItem/edit/${currentObject.key}'/>"> 
		<img border="0" alt="Edit" src="<%=request.getContextPath()%>/images/edit.gif"/></a>
              &nbsp;&nbsp;
              <a href="<c:url value='/swag/swagItem/delete/${currentObject.key}'/>"> 
              <img
			border="0" alt="Delete"
			src="<%=request.getContextPath()%>/images/delete.gif"/>
		</a>
	</display:column>

	<display:column property="tags" decorator="com.swagswap.web.springmvc.displaytag.TagsDecorator"/>

	<c:if test="${not empty currentObject.imageKey}">
		<display:column title="Image">
			<img border="0" alt="Image" src="<c:url value='/swag/showImage/${currentObject.imageKey}'/>"/>
		</display:column>
	</c:if>

</display:table>

<script language="JavaScript">
    document.forms[0].searchString.focus();
    document.forms[0].searchString.select();
</script>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>