<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<c:if test="${!swagItem.new}">
	<h2>Edit ${swagItem.name}</h2>
</c:if>
<c:if test="${swagItem.new}">
	<h2>Add</h2>
</c:if>

<form:form action="/swag/save" enctype="multipart/form-data" commandName="swagItem" method="post">
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
			<td>
			<form:input path="tags[0]" />
			<form:input path="tags[1]" />
			<form:input path="tags[2]" />
			<form:input path="tags[3]" />
			</td>
		</tr>
		<%-- 
		<tr>
			<td>Comments:</td>
			<td>
			<form:checkboxes path="comments" /></td>
		</tr>
		 --%>
		<tr>
			<td>Image:</td>
			<td nowrap="true">
			<c:if test="${not empty swagItem.imageKey}">
			<img border="0" src="<c:url value='/swag/showImage/${swagItem.imageKey}'/>"/>
			</c:if>
			<input type="file" name="imageBytes" value="Change Image"/> (Upload New Image)</td>
		</tr>

	</table>
	<input type="submit" value="save" />
	<input type="submit" value="cancel" onclick="document.location.href='<c:url value='/swag/search'/>';return false;" />

</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>