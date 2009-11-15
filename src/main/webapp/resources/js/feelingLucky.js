google.load('search', '1');

var imageSearch;
var lastImageIndex = 0;
var URLElement;
var buttonElement;

function searchComplete() {
	// Check that we got results
	if (imageSearch.results && imageSearch.results.length > 0) {
		// Grab our content div, clear it.
		var contentDiv = document.getElementById('luckyImage');
		contentDiv.innerHTML = '';

		var results = imageSearch.results;

		// only have results 0..7
		var randomResult = results[lastImageIndex++ % 8]; 
		
		var imgContainer = document.getElementById('luckyImage');
		var newImg = document.createElement('img');
		newImg.height = 80;
		newImg.width = 120;
		newImg.src = randomResult.tbUrl;
		imgContainer.appendChild(newImg);

		// change text of I'm feeling lucky button
		buttonElement.value = "I'm feeling lucky again!";

		// put url of image in imageURL
		URLElement.value = randomResult.url;
	}
}

function OnImFeelingLucky(button, searchStringElement, url) {

	URLElement = url;
	buttonElement = button;
	// Our ImageSearch instance.
	imageSearch = new google.search.ImageSearch();
	// Restrict to extra large images only
	imageSearch.setRestriction(google.search.ImageSearch.RESTRICT_IMAGESIZE,
			google.search.ImageSearch.IMAGESIZE_MEDIUM);
	// get 8 results (default is 4)
	imageSearch.setResultSetSize(google.search.Search.LARGE_RESULTSET);
	// Here we set a callback so that anytime a search is executed, it will call
	// the searchComplete function and pass it our ImageSearch searcher.
	// When a search completes, our ImageSearch object is automatically
	// populated with the results.
	imageSearch.setSearchCompleteCallback(this, searchComplete, null);
	// Find based value from searchString component
	var searchValue = searchStringElement.value;
	imageSearch.execute(searchValue);

}