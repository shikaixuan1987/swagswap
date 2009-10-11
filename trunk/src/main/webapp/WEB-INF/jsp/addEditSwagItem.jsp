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
            <td><input id="ImfeelingLuckyButton" type="submit"
              alt="search Google images based on item name"
              value="I'm feeling lucky"
              onClick="OnImFeeelingLucky();return false;" /></td>

          </tr>
          <tr>
            <td></td>
            <td align="center">
            <div id="luckyImage" />
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
<script language="Javascript" type="text/javascript">

google.load('search', '1');

var imageSearch;
var lastImageIndex=0;

function searchComplete() {
  // Check that we got results
  if (imageSearch.results && imageSearch.results.length > 0) {
    // Grab our content div, clear it.
    var contentDiv = document.getElementById('luckyImage');
    contentDiv.innerHTML = '';

    var results = imageSearch.results;

    var randomResult = results[lastImageIndex++ % 8]; //only have results 0..3
    
    var imgContainer = document.getElementById('luckyImage');
    var newImg = document.createElement('img');
    newImg.src = randomResult.tbUrl;
    imgContainer.appendChild(newImg);

    //change text of I'm feeling lucky button
    document.getElementById('ImfeelingLuckyButton').value="I'm feeling lucky again";

    //put url of image in imageURL
    document.getElementById('imageURL').value=randomResult.tbUrl;
  }
}

function OnImFeeelingLucky() {
  // Our ImageSearch instance.
  imageSearch = new google.search.ImageSearch();

  // Restrict to extra large images only
  imageSearch.setRestriction(google.search.ImageSearch.RESTRICT_IMAGESIZE,
                             google.search.ImageSearch.IMAGESIZE_MEDIUM);
  //get 8 results (default is 4)
  imageSearch.setResultSetSize(google.search.Search.LARGE_RESULTSET);

  // Here we set a callback so that anytime a search is executed, it will call
  // the searchComplete function and pass it our ImageSearch searcher.
  // When a search completes, our ImageSearch object is automatically
  // populated with the results.
  imageSearch.setSearchCompleteCallback(this, searchComplete, null);

  // Find based on swag item name
  var name = document.getElementById('name').value;
  imageSearch.execute(name);
}

window.onload=formatStr(document.getElementById('comments'));
</script>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>