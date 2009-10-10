<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2>View / Rate / Comment on ${swagItem.name}</h2>
<%--Trick because this page has swagItem as null which swagItemRating tag picks up and anchors, so anchor to that --%>
<form:form action="/swag/rate#null" commandName="newRating" name="rateForm" method="get">
	<form:hidden path="swagItemKey" />
	<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
	     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
     <input type="hidden" name="userRating" /> <!-- this is used for javascript trick below -->
<table>
	<tr>
	</tr>
	<google-auth:isLoggedIn>
		<tr>
			<td>Your Rating:</td>
			<td>
			<swagItemRating:showRatingStars 
				rateFormName="rateForm" 
				userRating="${userRating}" />
			</td>
			<td>
				<a href="/swag/comment">Comment</a>
			</td>
			<td>
				<a href="/swag/search">Back to list</a>
			</td>
			<td width="30%"></td>
		</tr>
	</google-auth:isLoggedIn>
   <google-auth:isNotLoggedIn>
	   <tr>
			<td width="30%"></td>
			<td><google-auth:loginLogoutTag 
					loginText="Login to rate or comment"
					requestURL="/swag/view/${swagItem.key}" 
					/>
			</td>
		</tr>
	</google-auth:isNotLoggedIn>
	
</table>
</form:form>
<table>
	<tr>
		<td>${swagItem.name}</td>
		<td nowrap="true">
			<%--TODO is this test needed anymore? --%>
			<c:if test="${not empty swagItem.imageKey}">
				<img border="0" src="<c:url value='/swag/showImage/${swagItem.imageKey}'/>"/>
			</c:if>
		</td>
	</tr>
	<tr>
		<td>Description:</td>
		<td style="white-space: pre-wrap"><c:out value="${swagItem.description}" /></td>
	</tr>
	<tr>
		<td>Created:</td>
		<td><fmt:formatDate value="${swagItem.created}" type="both"
			dateStyle="short" /></td>
	</tr>
	<tr>
		<td>Last Updated:</td>
		<td><fmt:formatDate value="${swagItem.lastUpdated}" type="both"
			dateStyle="short" /></td>
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
	<tr>
		<td>Company/Vendor:</td>
		<td><c:out value="${swagItem.company}" /></td>
	</tr>
	<tr>
		<td>Comments:</td>
		<td>
			 <c:forEach var="comment" items="${swagItem.comments}">
			 	${comment.swagSwapUserNickname} 
			 	(<fmt:formatDate value="${swagItem.created}" type="both" dateStyle="short" />):
			 	 ${comment.commentText}<br/><br/>
			 </c:forEach>
			 <form:form action="/swag/addComment" commandName="newComment" method="get">
			 	<form:hidden path="swagItemKey" />
			 	<form:input path="commentText" /><input type="submit" value="add comment" />
			 </form:form>
		</td>
	</tr>
</table>
<form:form  method="post">	
	<input type="submit" value="cancel" onclick="document.location.href='<c:url value='/swag/search'/>';return false;" />
</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>