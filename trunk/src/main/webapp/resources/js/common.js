/*  Nasty piece of JavaScript to find a named element within a form in JSF land  */
function findElementInForm(form, id) {

	for (i = 0; i < form.elements.length; i++) {
		var name = form.elements[i].name;
		if (name != form.name) {
			var ix = name.lastIndexOf(":");
			var partialId = name.substr(ix + 1);
			if (partialId == id) {
				return form.elements[i];
			}
		}
	}
}


function selectRow(table, event, btnID) {
	var target=event.target;

	// IE support.
	event = event || window.event;            
	target = event.target || event.srcElement; 
	
	if (target) {	
		if (target.tagName=="INPUT" || target.tagName=="input") {
			return;
		}
		var tagName=target.tagName;
		if (tagName=="TR" || tagName=="TABLE"){
			return;
		}
		
		var row = findRow(table, target);
		var button = findButton(row, btnID);
		if (button) {
			button.click();
		}
	}
}
	

function findButton(row, btnID) {

	if (!row) {
		return;
	}

	var elements;
	if (row.all) {
		// IE
		elements = row.all;
	} else {
		elements = row.getElementsByTagName("input");
	}

	for (var i = 0; i < elements.length; i++) {
		var element = elements[i];
		if (element.id && (element.id.indexOf(btnID) > -1)) {
			return element;
		}
	}
}




function changeto(table, event, highlightClass) { 
	var target=event.target;
	
	// IE support.
	event = event || window.event;            
	target = event.target || event.srcElement; 
	
	if (target) {	
		var tagName=target.tagName;
		if (tagName=="TR" || tagName=="TABLE"){
			return;
		}
		
		var row = findRow(table, target);
		highlightRow(row, highlightClass, true);
	}
}

function findRow(table, target) {
	var element;
	while(target.parentNode && target.parentNode != table) {
		target=target.parentNode; 
		
		if (target.tagName=="TR" || target.tagName=="tr") {
			element = target;
		}
	}
	//  Make sure row is in BODY of table
	if (element.parentNode) {
		if (element.parentNode.tagName == "TBODY" || element.parentNode.tagName == "tbody") {
			return element;
		}
		
	}
}

function highlightRow(row, highlightClass, saveOriginal) {
	
	if (!row) {
		return;
	}
		
	if (saveOriginal) {
		row.originalClassName = row.className;
		row.className = highlightClass;
	} else {
		row.className=row.originalClassName;
	}
}

function changeback(table, event) {
	// IE support.
	event = event || window.event;            
	var target = event.target || event.srcElement; 
	var row = findRow(table, target);
	highlightRow(row, '', false);
}


