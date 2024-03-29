<%@ include file="/WEB-INF/jsp/header.jsp"%>

<table width="100%">
	<tr>
		<td style="vertical-align: bottom;">
		<div class="subTitleText">Swag Stats</div>
		</td>
		<td style="vertical-align: bottom; text-align: right;"><google-auth:loginLogoutTag
			requestURL="/springmvc/search" /></td>
	</tr>
</table>
<div class="subTitleBar"></div>
<br />
<br />
<table>
    <tr>
      <td><H3 style="border-style: hidden">Swag Rankings</H3></td>
    </tr>
	<tr>
		<td>Top Rated: <display:table style="width:1px;"
			name="swagStats.topRated" uid="swagItemsList_topRated" id="topRated_current"
			requestURI="/springmvc/swagStats" decorator="com.swagswap.web.springmvc.displaytag.NameDecorator"
            keepStatus="true"
            defaultsort="3" defaultorder="descending">
			<display:column property="swagItemKeyLink" title="Name"/>
			<%-- Ratings --%>
			<display:column title="My Rating">
				<google-auth:isLoggedIn>
					<a name="${topRated_current.key}"></a>
					<form:form action="/springmvc/rate#${topRated_current.key}"
						commandName="newRating-${topRated_current.key}"
						name="rateFormTopRated${topRated_current.key}" method="get">
						<form:hidden path="swagItemKey" />
						<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
        		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
						<input type="hidden" name="userRating" />
						<!-- this is used for javascript trick below -->
						<center><swagItemRating:showRatingStars
							rateFormName="rateFormTopRated${topRated_current.key}"
							swagSwapUser="${swagSwapUser}"
							swagItemKey="${topRated_current.key}" /></center>
					</form:form>
				</google-auth:isLoggedIn>
				<google-auth:isNotLoggedIn>
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
			<display:column property="averageRating" title="Avg Rating" />
		</display:table></td>
		<td>Most Rated: <display:table style="width:1px;"
			name="swagStats.topRated" uid="swagItemsList_mostRated" 
            id="mostRated_current" decorator="com.swagswap.web.springmvc.displaytag.NameDecorator"
			requestURI="/springmvc/swagStats" keepStatus="true" defaultsort="3" defaultorder="descending">
			<display:column property="swagItemKeyLink" title="Name"/>
			<%-- Ratings --%>
			<display:column title="My Rating">
				<google-auth:isLoggedIn>
					<a name="${mostRated_current.key}"></a>
					<form:form action="/springmvc/rate#${mostRated_current.key}"
						commandName="newRating-${mostRated_current.key}"
						name="rateFormMostRated${mostRated_current.key}" method="get">
						<form:hidden path="swagItemKey" />
						<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
        		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
						<input type="hidden" name="userRating" />
						<!-- this is used for javascript trick below -->
						<center><swagItemRating:showRatingStars
							rateFormName="rateFormMostRated${mostRated_current.key}"
							swagSwapUser="${swagSwapUser}"
							swagItemKey="${mostRated_current.key}" /></center>
					</form:form>
				</google-auth:isLoggedIn>
				<google-auth:isNotLoggedIn>
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
			<display:column property="numberOfRatings" title="Ratings"/>
		</display:table></td>
		<td>Most Commented: <display:table style="width:1px;"
			name="swagStats.topRated" uid="swagItemsList_mostCommented"
			id="mostCommented_current" requestURI="/springmvc/swagStats"
            decorator="com.swagswap.web.springmvc.displaytag.NameDecorator"
			keepStatus="true" defaultsort="3" defaultorder="descending">
			<display:column property="swagItemKeyLink" title="Name"/>
			<%-- Ratings --%>
			<display:column title="My Rating">
				<google-auth:isLoggedIn>
					<a name="${mostCommented_current.key}"></a>
					<form:form action="/springmvc/rate#${mostCommented_current.key}"
						commandName="newRating-${mostCommented_current.key}"
						name="rateFormMostCommented${mostCommented_current.key}"
						method="get">
						<form:hidden path="swagItemKey" />
						<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
        		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
						<input type="hidden" name="userRating" />
						<!-- this is used for javascript trick below -->
						<center><swagItemRating:showRatingStars
							rateFormName="rateFormMostCommented${mostCommented_current.key}"
							swagSwapUser="${swagSwapUser}"
							swagItemKey="${mostCommented_current.key}" /></center>
					</form:form>
				</google-auth:isLoggedIn>
				<google-auth:isNotLoggedIn>
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
			<display:column property="numberOfComments" title="Comments" />
		</display:table></td>
	</tr>
    
	<tr>
		<td><H3 style="border-style: hidden">User Rankings</H3></td>
	</tr>
	<tr>
		<td style="border-style: ridge">Most Items Created<br/><img
			src="http://chart.apis.google.com/chart?chs=320x75&${swagStats.googleChartsUrlItemsCreated}" />
		</td>
		<td style="border-style: ridge">Most Items Rated<br/><img
			src="http://chart.apis.google.com/chart?chs=320x75&${swagStats.googleChartsUrlItemsRated}" />
		</td>
		<td style="border-style: ridge">Most Items Commented<br/><img
			src="http://chart.apis.google.com/chart?chs=320x75&${swagStats.googleChartsUrlItemsCommented}" />
		</td>
	</tr>
	<tr>
		<td><a href="/jsf/showSwagStats.jsf">Another view on Swag Stats</a></td>
	</tr>
</table>


<%@ include file="/WEB-INF/jsp/footer.jsp"%>