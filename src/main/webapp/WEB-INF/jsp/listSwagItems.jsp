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
			<td><form:input path="searchString" /></td>
			<td>
				<INPUT TYPE="image" SRC="/images/icon_flashlight.gif"
				       BORDER="0" ALT="Search Swag"/>
				<a href="#" onclick="document.searchForm.submit()">Search Swag</a>
			</td>
		</tr>
	</table>
</form:form>

<a href="/swag/swagItem/add"><img src="/images/newAdd.png" title="Add SwagItem" border="0"/>Add Swag</a>
<br/>

	<display:table name="swagItems" id="currentObject" requestURI="/swag/listSwagItems">
		<display:column sortable="true" property="name" />

		<display:column title="Action">
			<a href="<c:url value='/swag/swagItem/edit/${currentObject.key}'/>"> 
			<img border="0" alt="Edit" src="<%=request.getContextPath()%>/images/edit.gif"/></a>
               &nbsp;&nbsp;
               <a href="<c:url value='/swag/swagItem/delete/${currentObject.key}'/>"> 
               <img
				border="0" alt="Delete"
				src="<%=request.getContextPath()%>/images/delete.gif"/>
			</a>
		</display:column>
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