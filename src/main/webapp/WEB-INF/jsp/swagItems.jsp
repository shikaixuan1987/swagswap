<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<c:if test="${!swagItem.new}">
	<h2>Edit ${swagItem.name}</h2>
</c:if>
<c:if test="${swagItem.new}">
	<h2>Add</h2>
</c:if>

<form:form action="/swag/swagItem/save" enctype="multipart/form-data" commandName="swagItem" method="post">
	<form:hidden path="key" />

	<table>
		<tr>
			<td>Name:</td>
			<td><form:input path="name" /></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><form:textarea path="description" rows="3" cols="40" /></td>
		<tr>
			<td>Created:</td>
			<td><fmt:formatDate value="${swagItem.created}" type="both"
				dateStyle="full" /></td>
		</tr>
		<tr>
			<td>Last Updated:</td>
			<td><fmt:formatDate value="${swagItem.lastUpdated}" type="both"
				dateStyle="full" /></td>
		</tr>
		<tr>
			<td>Tags:</td>
			<td><%--@elvariable id="availableDataFormats" type="java.lang.Array"--%>
			<form:checkboxes path="tags" items="${availableDataFormats}" />
			</td>
		</tr>

		<tr>
			<td>Protocols:</td>
			<td><%--@elvariable id="availableProtocols" type="java.lang.Array"--%>
			<form:checkboxes path="comments" items="${availableProtocols}" /></td>
		</tr>
		<tr>
			<td>Image:</td>
			<td><input type="file" name="imageBytes"/></td>
		</tr>

	</table>
	<input type="submit" value="save" />
	<input type="submit" value="cancel" onclick="document.location.href='<c:url value='/swag/swagItems'/>';return false;" />

	<c:if test="${not empty swagItems}">

		<display:table name="swagItems" id="currentObject" requestURI="/swag/swagItems">
			<display:column href="<c:url value='/swag/swagItem/edit/${currentObject.key}'/>" sortable="true" property="name" />

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

		</display:table>
	</c:if>
</form:form>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>