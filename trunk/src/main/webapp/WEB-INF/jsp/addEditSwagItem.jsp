<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:if test="${!swagItem.new}">
  <h2>Edit ${swagItem.name}</h2>
</c:if>
<c:if test="${swagItem.new}">
  <h2>Add</h2>
</c:if>

<form:form action="/swag/save" enctype="multipart/form-data"
  commandName="swagItem" method="post">
  <%--Any fields they're not filling in have to be carried
	    over in hidden fields because of request scoping --%>
  <form:hidden path="key" />
  <form:hidden path="imageKey" />
  <font color="red"><form:errors path="*" /></font>
  <table>
    <tr>
      <td>
      <table>
        <tr>
          <td>Name:</td>
          <td><form:input path="name" /> <font color="red"><form:errors
            path="name" /></font></td>
        </tr>
        <tr>
          <td>Description:</td>
          <td><form:input path="description" size="70" /></td>
          <tr>
            <td>Created:</td>
            <td><fmt:formatDate value="${swagItem.created}" pattern="dd/MM HH:mm" /></td>
          </tr>
          <tr>
            <td>Last Updated:</td>
            <td><fmt:formatDate value="${swagItem.lastUpdated}"pattern="dd/MM HH:mm" /></td>
          </tr>
          <tr>
            <td>Tags:</td>
            <td><form:input path="tags[0]" /> <form:input
              path="tags[1]" /> <br />
            <form:input path="tags[2]" /> <form:input path="tags[3]" />
            </td>
          </tr>
          <tr>
            <td>Company/Vendor:</td>
            <td><form:input path="company" /></td>
          </tr>
          <tr>
            <td>Current Image:</td>
            <td><c:if test="${not empty swagItem.imageKey}">
              <img border="0"
                src="<c:url value='/swag/showImage/${swagItem.imageKey}'/>" />
            </c:if></td>
          </tr>
          <tr>
            <td>Upload a New Image:</td>
            <td><input type="file" name="imageBytes"
              value="Change Image" /> <font color="red"><form:errors
              path="imageBytes" /></font></td>
          </tr>
          <tr>
            <td>or</td>
            <td></td>
          </tr>
          <tr>
            <td>Specify an image URL:</td>
            <td><form:input path="imageURL" size="70" /> <font
              color="red"><form:errors path="imageURL" /></font></td>
          </tr>
          <tr>
            <td>or</td>
            <td></td>
          </tr>
          <tr>
            <td>Image "I'm feeling lucky"</td>
            <td><input id="ImfeelingLuckyButton" type="button"
              alt="search Google images based on item name"
              value="I'm feeling lucky"
              onClick="OnImFeelingLuckyWrapper();return false;" /></td>

          </tr>
          <tr>
            <td></td>
            <td align="center">
				<div id="luckyImage" style="margin-left: 5px; margin-top: 3px;" />
            </td>

          </tr>
      </table>
      </td>
      <td width="30%">
      <table>
        <tr>
          <td>Comments:</td>
        </tr>
        <tr>
          <td id="comments">
          <c:forEach var="comment" items="${swagItem.comments}">
                ${comment.commentText}<br/>
			 	[${comment.swagSwapUserNickname} <fmt:formatDate value="${comment.created}" pattern="dd/MM HH:mm" />]
			 	<hr/>
          </c:forEach>
          </td>
        </tr>
      </table>
      </td>
    </tr>
  </table>
  <input type="submit" value="save" />
  <input type="submit" value="cancel"
    onclick="document.location.href='<c:url value='/swag/search'/>';return false;" />

</form:form>

<%--
I'm feeling lucky functionality.  
Inspired by http://code.google.com/apis/ajax/playground/#raw_search
--%>

<script src="http://www.google.com/jsapi" type="text/javascript"></script>
<script type="text/javascript" src="/resources/js/feelingLucky.js"></script>
<script language="Javascript" type="text/javascript">

function OnImFeelingLuckyWrapper() {
  	searchElement = document.getElementById('name');
  	buttonElement = document.getElementById('ImfeelingLuckyButton');
  	urlElement = document.getElementById('imageURL');
 	OnImFeelingLucky(buttonElement, searchElement, urlElement);
}

window.onload=formatStr(document.getElementById('comments'));
</script>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>