if (!window["ajaxStatus"]) {
	var busystatus = {};
}
busystatus.onStatusChange = function onStatusChange(data) {
	var status = data.status;
	var componentID = busystatus["@all"];
	var element = document.getElementById(componentID);
	
	if (status === "begin") { // turn on busy indicator
		element.style.display = "inline";
	} else { 
		element.style.display = "none";
	}
};

jsf.ajax.addOnEvent(busystatus.onStatusChange);

busystatus.init = function init(componentID) {
	busystatus["@all"] = componentID;
};
