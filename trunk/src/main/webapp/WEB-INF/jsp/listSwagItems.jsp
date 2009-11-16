<%@ include file="/WEB-INF/jsp/header.jsp"%>
<%--Shows a search box and a list of SwagItems using the old but good displaytag library --%>

<br />
<form:form action="/springmvc/search" commandName="searchCriteria"
	name="searchForm" method="GET">
	<table width="100%">
		<tr>
			<td style="vertical-align: bottom;"><c:if test="${not empty searchCriteria.searchString}">
				<div class="subTitleText">Swag Item Search: ${searchCriteria.searchString}</div>
			</c:if> <c:if test="${empty searchCriteria.searchString}">
				<div class="subTitleText">All Swag Items</div>
			</c:if></td>
			<td style="vertical-align: bottom; text-align:right;"><google-auth:loginLogoutTag requestURL="/springmvc/search" />
			</td>
		</tr>
	</table>
	<div class="subTitleBar" ></div><br />
	<br />
	<table width="100%">
		<tr>
			<td><form:input
				path="searchString" /> <a href="#"
				onclick="document.searchForm.submit()"> <img
				src="/images/icon_flashlight.gif" border="0" />Search Swag</a> </td>
				<td style="text-align: right;"><a
				href="/springmvc/add"><img src="/images/newAdd.png"
				title="Add SwagItem" border="0" />Add Swag</a> &nbsp;</td>
		</tr>
	</table>
</form:form>
<br/>
<display:table name="swagItems" uid="swagItemsList" id="currentObject"
	requestURI="/springmvc/search" keepStatus="true" pagesize="10" 
	decorator="com.swagswap.web.springmvc.displaytag.NameDecorator">
	<display:column property="swagItemKeyLink" title="Name" sortable="true"/>
	<display:column title="Action">
		<a href="<c:url value='/springmvc/view/${currentObject.key}'/>"> <img
			border="0" alt="View/Comment/Rate"
			src="<%=request.getContextPath()%>/images/view.gif" /> </a>
		<%-- Users can only edit their own items --%>
		<google-auth:isAllowed
			swagItemOwnerGoogleID="${currentObject.ownerGoogleID}">

			<a href="<c:url value='/springmvc/edit/${currentObject.key}'/>">
			<img border="0" alt="Edit"
				src="<%=request.getContextPath()%>/images/edit.gif" /> </a>

			<a href="<c:url value='/springmvc/delete/${currentObject.key}'/>"
				onclick="return confirmSubmit()"> <img border="0" alt="Delete"
				src="<%=request.getContextPath()%>/images/delete.gif" /> </a>
		</google-auth:isAllowed>
	</display:column>

	<%-- Ratings --%>
	<display:column title="My Rating" style="width: 1%">
		<google-auth:isLoggedIn>
			<%-- HTML anchor to compensate for web 1.0 implementation --%>
			<a name="${currentObject.key}"></a>
			<form:form action="/springmvc/rate#${currentObject.key}"
				commandName="newRating-${currentObject.key}"
				name="rateForm${currentObject.key}" method="get">
				<form:hidden path="swagItemKey" />
				<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
				<input type="hidden" name="userRating" />
				<!-- this is used for javascript trick below -->
				<center><swagItemRating:showRatingStars
					rateFormName="rateForm${currentObject.key}"
					swagSwapUser="${swagSwapUser}" swagItemKey="${currentObject.key}" />
				</center>
			</form:form>
		</google-auth:isLoggedIn>
		<google-auth:isNotLoggedIn>
			<%--show them a teaser with a link to the login page --%>
			<a href="${loginUrl}" border="0"> <img border="0"
				src="/images/starOff.gif" /><img border="0"
				src="/images/starOff.gif" /><img border="0"
				src="/images/starOff.gif" /><img border="0"
				src="/images/starOff.gif" /><img border="0"
				src="/images/starOff.gif" /> </a>
		</google-auth:isNotLoggedIn>
	</display:column>

	<%-- 
	<display:column property="tags" decorator="com.swagswap.web.springmvc.displaytag.TagsDecorator"/>
	--%>
	<c:if test="${not empty currentObject.imageKey}">
		<display:column title="Image" style="width: 1%">
			<a href="<c:url value='/springmvc/view/${currentObject.key}'/>">
			<img border="0" alt="Image" height="50" width="66"
				src="<c:url value='/springmvc/showThumbnail/${currentObject.imageKey}'/>" />
			</a>
		</display:column>
	</c:if>
	<display:column sortable="true" title="Owner" property="ownerNickName" />
	<display:column sortable="true" property="company" />
	<display:column property="numberOfRatings" title="No. Ratings"
		sortable="true" />
	<display:column property="lastUpdated" title="Last Updated" format="{0,date,dd-MM HH:mm}" sortable="true"/>	
	<display:column property="averageRating" title="Avg Rating"
		sortable="true" />

</display:table>

<script language="JavaScript">
    document.forms[0].searchString.focus();
    document.forms[0].searchString.select();

	function confirmSubmit(name)
	{
	var agree=confirm("Are you sure you want to delete this item?");
	if (agree)
		return true ;
	else
		return false ;
	}

</script>

