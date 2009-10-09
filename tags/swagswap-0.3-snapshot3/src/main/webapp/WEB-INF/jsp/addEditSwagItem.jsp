<%@ include file="/WEB-INF/jsp/header.jsp" %>

<c:if test="${!swagItem.new}">
	<h2>Edit ${swagItem.name}</h2>
</c:if>
<c:if test="${swagItem.new}">
	<h2>Add</h2>
</c:if>

<form:form action="/swag/save" enctype="multipart/form-data" commandName="swagItem" method="post">
	<%--Any fields they're not filling in have to be carried
	    over in hidden fields because of request scoping --%>
	<form:hidden path="key" />
	<form:hidden path="imageKey" />
	<font color="red"><form:errors path="*" /></font>
	<table>
		<tr>
			<td>Name:</td>
			<td>
			<form:input path="name" /> 
			<font color="red"><form:errors path="name" /></font>
			</td>
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
		<tr>
			<td>Company/Vendor:</td>
			<td><form:input path="company" /></td>
		</tr>
		<%-- 
		<tr>
			<td>Comments:</td>
			<td>
			<form:checkboxes path="comments" /></td>
		</tr>
		 --%>
		<tr>
			<td>Current Image:</td>
			<td>
				<c:if test="${not empty swagItem.imageKey}">
					<img border="0" src="<c:url value='/swag/showImage/${swagItem.imageKey}'/>"/>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Upload a New Image:</td>
			<td>
			<input type="file" name="imageBytes" value="Change Image"/>
			<font color="red"><form:errors path="imageBytes" /></font>
			</td>
		</tr>
		<tr>
			<td>or</td>
			<td></td>
		</tr>
		<tr>
			<td>Specify an image URL:</td>
			<td>
			<form:input path="imageURL" size="70" />
			<font color="red"><form:errors path="imageURL" /></font>
			</td>
		</tr>

	</table>
	<input type="submit" value="save" />
	<input type="submit" value="cancel" onclick="document.location.href='<c:url value='/swag/search'/>';return false;" />

</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>