$(document).ready(function() {
	// put the focus on the first non-hidden field in a form
	var $field = $("div[id!='heading'] form input[type!='hidden']:first");
	if ($field != undefined) {
		$field.focus();
	}
});
