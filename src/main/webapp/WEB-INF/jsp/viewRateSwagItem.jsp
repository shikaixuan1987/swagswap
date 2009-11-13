<%@ include file="/WEB-INF/jsp/header.jsp"%>

<table width="100%">
	<tr>
		<td style="vertical-align: bottom;">
		<div class="subTitleText">View / Rate / Comment on
		${swagItem.name}</div>
		</td>
		<td style="vertical-align: bottom; text-align: right;"><google-auth:loginLogoutTag
			requestURL="/springmvc/search" /></td>
	</tr>
</table>
<div class="subTitleBar"></div>
<br />
<br />
<%--Trick because this page has swagItem as null which swagItemRating tag picks up and anchors, so anchor to that --%>
<form:form action="/springmvc/rate#null" commandName="newRating"
	name="rateForm" method="get">
	<form:hidden path="swagItemKey" />
	<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
	     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
	<input type="hidden" name="userRating" />
	<!-- this is used for javascript trick below -->
	<table class="addEditTable">
		<tr>
			<google-auth:isLoggedIn>
				<td>
				<div class="label" style="width: 130px">My Rating:</div>
				</td>
				<td><swagItemRating:showRatingStars rateFormName="rateForm"
					userRating="${userRating}" /></td>
				<td width="30%"></td>
			</google-auth:isLoggedIn>
			<google-auth:isNotLoggedIn>
				<td><google-auth:loginLogoutTag loginText="Login to rate items"
					requestURL="/springmvc/view/${swagItem.key}" /></td>
			</google-auth:isNotLoggedIn>
		</tr>

	</table>
</form:form>
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="60%">
		<table class="addEditTable" width="100%">
			<tr>
				<td>
				<div class="label">${swagItem.name}</div>
				</td>
				<td nowrap="true"><%--TODO is this test needed anymore? --%> <c:if
					test="${not empty swagItem.imageKey}">
					<img border="0"
						src="<c:url value='/springmvc/showImage/${swagItem.imageKey}'/>" />
				</c:if></td>
			</tr>
			<tr>
				<td>
				<div class="label">Description:</div>
				</td>
				<td style="white-space: pre-wrap"><c:out
					value="${swagItem.description}" /></td>
			</tr>
			<tr>
				<td>
				<div class="label">Created:</div>
				</td>
				<td><fmt:formatDate value="${swagItem.created}"
					pattern="dd/MM HH:mm" /></td>
			</tr>
			<tr>
				<td>
				<div class="label">Last Updated:</div>
				</td>
				<td><fmt:formatDate value="${swagItem.lastUpdated}"
					pattern="dd/MM HH:mm" /></td>
			</tr>
			<tr>
				<td>
				<div class="label">Tags:</div>
				</td>
				<td><c:out value="${swagItem.tags[0]}" /> <c:out
					value="${swagItem.tags[1]}" /> <c:out value="${swagItem.tags[2]}" />
				<c:out value="${swagItem.tags[3]}" /></td>
			</tr>
			<tr>
				<td>
				<div class="label">Company/Vendor:</div>
				</td>
				<td><c:out value="${swagItem.company}" /></td>
			</tr>
		</table>
		</td>
		<td width="40%">
		<table width="100%">
			<tr>
				<google-auth:isLoggedIn>
					<td><form:form action="/springmvc/addComment"
						commandName="newComment" method="get">
						<form:hidden path="swagItemKey" />
						<form:input path="commentText" size="40"/>
						<input type="submit" value="add comment"/>
					</form:form></td>
				</google-auth:isLoggedIn>
				<google-auth:isNotLoggedIn>
					<td><google-auth:loginLogoutTag
						loginText="Login to comment on items"
						requestURL="/springmvc/view/${swagItem.key}" /></td>
				</google-auth:isNotLoggedIn>
			</tr>
			<tr>
				<td id="comments">
				<div class="label" style="text-align: center;">Comments</div>
				<br />
				<c:forEach var="comment" items="${swagItem.comments}">
              ${comment.commentText}<br />[${comment.swagSwapUserNickname} <fmt:formatDate
						value="${comment.created}" pattern="dd/MM HH:mm" />]
              <hr />
				</c:forEach> <%--
            <c:if test="${empty swagItem.comments}">
             <div style="white-space: nowrap">No comments yet</div>
            </c:if>
             --%></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<script>
window.onload=formatStr(document.getElementById('comments'));
</script>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>