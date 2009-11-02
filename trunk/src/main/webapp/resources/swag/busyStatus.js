
if (!window["busystatusdemo"]) {
	var busystatusdemo;
}
busystatusdemo.onStatusChange = function onStatusChange(data) {
	var status = data.status;
	var componentID = busystatusdemo;

	var element = document.getElementById(componentID);
	if (status === "begin") { // turn on busy indicator
		element.style.display = "inline";
	} else { 
		element.style.display = "none";
	}
};

jsf.ajax.addOnEvent(busystatusdemo.onStatusChange);

busystatusdemo.init = function init(componentID) {
	window.alert('componentID');
	busystatusdemo = componentID;
};
