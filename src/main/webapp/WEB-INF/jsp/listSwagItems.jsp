<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2>All Swag Items</h2>

<a href="/swag/swagItem/add"><img src="/images/newAdd.png" title="Add SwagItem" border="0"/></a>
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
<%@ include file="/WEB-INF/jsp/footer.jsp" %>