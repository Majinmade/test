var script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.6.4.min.js'; // Check https://jquery.com/ for the current version
document.getElementsByTagName('head')[0].appendChild(script);

function getAvailableCurrencies() {
	$.get("http://127.0.0.1:8080/availablecurrencies/", (data, status) => {
		console.log(data);
		$.each(data, function(index, value) {
			$('#currency2').append($('<option>').text(value).val(index));
		});
		$.each(data, function(index, value) {
			$('#currency2').append($('<option>').text(value).val(index));
		});
	});
}