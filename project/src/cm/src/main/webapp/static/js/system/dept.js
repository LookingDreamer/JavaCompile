require(["jquery", "jqform",  "jqcookie", "jqtreeview", "jqtreeviewedit", "jqtreeviewasync"], function ($) {

	$(function() {
		
		$("#black").treeview({
	        url: "../dept/depttreelist",
	        showCheckbox: true,
	        ajax: {
	            type: "POST",
	            data: {
	                "v": function() {
	                    return "yeah: " + new Date();
	                }
	            }
	        }
	    });
    });
})

