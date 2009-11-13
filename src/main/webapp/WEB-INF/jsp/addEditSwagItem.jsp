<%@ include file="/WEB-INF/jsp/header.jsp"%>

<table width="100%">
	<tr>
		<td style="vertical-align: bottom;"><c:if test="${!swagItem.new}">
			<div class="subTitleText">Edit Swag</div>
		</c:if> <c:if test="${swagItem.new}">
			<div class="subTitleText">Add Swag</div>
		</c:if></td>
		<td style="vertical-align: bottom; text-align: right;"><google-auth:loginLogoutTag
			requestURL="/springmvc/search" /></td>
	</tr>
</table>
<div class="subTitleBar"></div>
<br />
<br />


<form:form action="/springmvc/save" enctype="multipart/form-data"
	commandName="swagItem" method="post">
	<%--Any fields they're not filling in have to be carried
	    over in hidden fields because of request scoping --%>
	<form:hidden path="key" />
	<form:hidden path="imageKey" />
	<font color="red"><form:errors path="*" /></font>

	<table width="100%">
		<tr>
			<td width="60%">
			<table class="addEditTable" width="100%">
				<tr>
					<td>
					<div class="label">Name:</div>
					</td>
					<td><form:input path="name" /> <font color="red"><form:errors
						path="name" /></font></td>
				</tr>
				<tr>
					<td>
					<div class="label">Description:</div>
					</td>
					<td><form:input path="description" size="50" /></td>
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
					<td><form:input path="tags[0]" /> <form:input path="tags[1]" />
					<br />
					<form:input path="tags[2]" /> <form:input path="tags[3]" /></td>
				</tr>
				<tr>
					<td>
					<div class="label">Company/Vendor:</div>
					</td>
					<td><form:input path="company" /></td>
				</tr>
				<tr>
					<td>
					<div class="label">Current Image:</div>
					</td>
					<td><c:if test="${not empty swagItem.imageKey}">
						<img border="0"
							src="<c:url value='/springmvc/showImage/${swagItem.imageKey}'/>" />
					</c:if></td>
				</tr>
				<tr>
					<td>
					<div class="label">Upload a New Image:</div>
					</td>
					<td><input type="file" name="imageBytes" value="Change Image" />
					<font color="red"><form:errors path="imageBytes" /></font></td>
				</tr>
				<tr>
					<td>
					<div class="labelText">or</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td>
					<div class="label">Specify an image URL:</div>
					</td>
					<td><form:input path="imageURL" size="50" /> <font
						color="red"><form:errors path="imageURL" /></font></td>
				</tr>
				<tr>
					<td>
					<div class="labelText">or</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td>
					<div class="label">Image "I'm feeling lucky"</div>
					</td>
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
		<c:if test="${not empty swagItem.key}">
			<td width="40%">
			<table width="100%">
				<tr>
					<td><div class="label" style="text-align: center">Comments</div></td>
				</tr>
				<tr>
					<td id="comments"><c:forEach var="comment"
						items="${swagItem.comments}">
                    ${comment.commentText}<br />
    			 	[${comment.swagSwapUserNickname} <fmt:formatDate
							value="${comment.created}" pattern="dd/MM HH:mm" />]
    			 	<hr />
					</c:forEach></td>
				</tr>
			</table>
			</td>
		</c:if>
		</tr>
	</table>
	</br>
	<div style="width: 100%; text-align: center;"><input
		type="submit" value="save" /> <input type="submit" value="cancel"
		onclick="document.location.href='<c:url value='/springmvc/search'/>';return false;" />
	</div>

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