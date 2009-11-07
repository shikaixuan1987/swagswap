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
		//  keep looking for TBODY to avoid header and footer
		if (target.tagName=="TBODY" || target.tagName=="tbody") {
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


