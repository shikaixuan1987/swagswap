<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2>View / Rate / Comment on ${swagItem.name}</h2>


<form:form action="/swag/save" enctype="multipart/form-data" commandName="swagItem" method="post">
	<form:hidden path="key" />

	<table>
		<tr>
			<td>Name:</td>
			<td><c:out value="${swagItem.name}" /></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><textarea rows="1" cols="40" disabled="true"
			style="background-color: #FFFFFF;border: #3532ff 1px solid;color: #000000;ursor: default;">
			<c:out value="${swagItem.description}" />
			</textarea>
			</td>
		</tr>
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
			<c:out value="${swagItem.tags[0]}" />
			<c:out value="${swagItem.tags[1]}" />
			<c:out value="${swagItem.tags[2]}" />
			<c:out value="${swagItem.tags[3]}" />
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
			</td>
		</tr>

	</table>
	<input type="submit" value="save" />
	<input type="submit" value="cancel" onclick="document.location.href='<c:url value='/swag/search'/>';return false;" />

</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>