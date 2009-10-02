<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2>View / Rate / Comment on ${swagItem.name}</h2>
<form:form action="/swag/rate" commandName="newRating" name="rateForm" method="post">
	<form:hidden path="swagItemKey" />
	<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
	     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
     <input type="hidden" name="userRating" /> <!-- this is used for javascript trick below -->
<table>
	<tr>
		<td>
			<c:if test="${not empty ratingSuccessMessage}">
				<c:out value="${swagItem.name}" /> Rated successfully
			</c:if>
		</td>
		<td></td>
	</tr>
	<google-auth:isLoggedIn>
		<tr>
			<td><br/><br/>Your Rating:</td>
			<td>
				<a href="#" onclick="document.rateForm.userRating.value='1';document.rateForm.submit()">
				<swagItemRating:showRatingStars numberOfStarsToShow="1" userRating="${userRating}"/></a>
				<br/> 
				<a href="#" onclick="document.rateForm.userRating.value='2';document.rateForm.submit()">
				<swagItemRating:showRatingStars numberOfStarsToShow="2" userRating="${userRating}"/>
				</a>
				<br/> 
				<a href="#" onclick="document.rateForm.userRating.value='3';document.rateForm.submit()">
				<swagItemRating:showRatingStars numberOfStarsToShow="3" userRating="${userRating}"/>
				</a>
				<br/> 
				<a href="#" onclick="document.rateForm.userRating.value='4';document.rateForm.submit()">
				<swagItemRating:showRatingStars numberOfStarsToShow="4" userRating="${userRating}"/>
				</a>
				<br/> 
				<a href="#" onclick="document.rateForm.userRating.value='5';document.rateForm.submit()">
				<swagItemRating:showRatingStars numberOfStarsToShow="5" userRating="${userRating}"/>
				</a>
				<br/> 
			</td>
			<td>
				<br/>
				<br/>
				<a href="/swag/comment">Comment</a>
			</td>
			<td>
				<br/>
				<br/>
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
		<td>Image:</td>
		<td nowrap="true">
			<%--TODO is this test needed anymore? --%>
			<c:if test="${not empty swagItem.imageKey}">
				<img border="0" src="<c:url value='/swag/showImage/${swagItem.imageKey}'/>"/>
			</c:if>
		</td>
	</tr>

</table>
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
			<%--TODO is this test needed anymore? --%>
			<c:if test="${not empty swagItem.imageKey}">
				<img border="0" src="<c:url value='/swag/showImage/${swagItem.imageKey}'/>"/>
			</c:if>
		</td>
	</tr>

</table>
<form:form  method="post">	
	<input type="submit" value="cancel" onclick="document.location.href='<c:url value='/swag/search'/>';return false;" />
</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>