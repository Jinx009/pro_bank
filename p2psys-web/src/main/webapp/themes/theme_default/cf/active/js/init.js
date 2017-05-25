// Edit the date here

$(document).ready(function() {
	$("#countdown").css("height",$(window).height());					   
	$("#countdown").countdown({
		date: "22 March 2016 16:00:00",
		format: "on"
	},
	function() {
		
	});
});	
