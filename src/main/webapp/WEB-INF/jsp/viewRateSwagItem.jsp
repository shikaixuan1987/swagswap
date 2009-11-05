<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>View / Rate / Comment on ${swagItem.name}</h2>
<%--Trick because this page has swagItem as null which swagItemRating tag picks up and anchors, so anchor to that --%>
<form:form action="/springmvc/rate#null" commandName="newRating"
  name="rateForm" method="get">
  <form:hidden path="swagItemKey" />
  <!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
	     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
  <input type="hidden" name="userRating" />
  <!-- this is used for javascript trick below -->
  <table>
    <tr>
    </tr>
    <google-auth:isLoggedIn>
      <tr>
        <td><a href="/springmvc/search">Back</a></td>
        <td>My Rating:</td>
        <td><swagItemRating:showRatingStars rateFormName="rateForm"
          userRating="${userRating}" /></td>
        <td width="30%"></td>
      </tr>
    </google-auth:isLoggedIn>
    <google-auth:isNotLoggedIn>
      <tr>
        <td width="30%"></td>
        <td><google-auth:loginLogoutTag
          loginText="Login to rate items"
          requestURL="/springmvc/view/${swagItem.key}" /></td>
      </tr>
    </google-auth:isNotLoggedIn>

  </table>
</form:form>
<table>
  <tr>
    <td>
    <table>
      <tr>
        <td>${swagItem.name}</td>
        <td nowrap="true"><%--TODO is this test needed anymore? --%>
        <c:if test="${not empty swagItem.imageKey}">
          <img border="0"
            src="<c:url value='/springmvc/showImage/${swagItem.imageKey}'/>" />
        </c:if></td>
      </tr>
      <tr>
        <td>Description:</td>
        <td style="white-space: pre-wrap"><c:out
          value="${swagItem.description}" /></td>
      </tr>
      <tr>
        <td>Created:</td>
        <td><fmt:formatDate value="${swagItem.created}" pattern="dd/MM HH:mm" /></td>
      </tr>
      <tr>
        <td>Last Updated:</td>
        <td><fmt:formatDate value="${swagItem.lastUpdated}" pattern="dd/MM HH:mm" /></td>
      </tr>
      <tr>
        <td>Tags:</td>
        <td><c:out value="${swagItem.tags[0]}" /> <c:out
          value="${swagItem.tags[1]}" /> <c:out
          value="${swagItem.tags[2]}" /> <c:out
          value="${swagItem.tags[3]}" /></td>
      </tr>
      <tr>
        <td>Company/Vendor:</td>
        <td><c:out value="${swagItem.company}" /></td>
      </tr>
    </table>
    </td>
    <td width="30%">
    <table>
      <tr>
        <td>Comments:
        <form:form action="/springmvc/addComment" commandName="newComment" method="get">
          <form:hidden path="swagItemKey" />
          <form:input path="commentText" />
          <input type="submit" value="add comment" />
        </td>
       </tr>
       <tr>
        </form:form>
        <td id="comments">
          <c:forEach var="comment" items="${swagItem.comments}">
            ${comment.commentText}<br/>[${comment.swagSwapUserNickname} <fmt:formatDate value="${comment.created}" pattern="dd/MM HH:mm" />]
            <hr/>
          </c:forEach>
        </td>
      </tr>
    </table>
    </td>
  </tr>
</table>
<form:form method="post">
  <input type="submit" value="cancel"
    onclick="document.location.href='<c:url value='/springmvc/search'/>';return false;" />
</form:form>

<script>
window.onload=formatStr(document.getElementById('comments'));
</script>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>