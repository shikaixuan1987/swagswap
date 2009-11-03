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