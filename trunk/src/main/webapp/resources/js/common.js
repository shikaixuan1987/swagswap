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








function changeto(table, event, highlightcolor) { 
	var target=event.target;
	if (target) {
		var tagName=target.tagName;
		if (tagName=="TR" || tagName=="TABLE"){
			return;
		}
		
		var row = findRow(table, target);
	
		highlightRow(row, highlightcolor, true);
	}
}

function findRow(table, target) {
	//  Not working in IE.  ParentNode not an object
	while(target.parentNode && target.parentNode != table) {
		target=target.parentNode; 
		
		if (target.tagName=="TR") {
			return target;
		}

	}
}

function highlightRow(row, color, saveOriginal) {
	
	if (!row) {
		return;
	}
	
	if (row.parentNode.tagName=="THEAD") {
		return;
	}
	
	for (i=0; i < row.children.length; i++) {
		var child = row.children[i];
		// window.alert(child);
		if (child.style.backgroundColor!=color) {
			if (saveOriginal) {
				child.style.originalColor=child.style.backgroundColor;
				child.style.backgroundColor=color;
			} else {
				child.style.backgroundColor=child.style.originalColor;
			}	
		}
	}	
}

function changeback(table, event) {
	var target=event.target;
	var row = findRow(table, target);
	highlightRow(row, '', false);
}


