var script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.6.4.min.js'; // Check https://jquery.com/ for the current version
document.getElementsByTagName('head')[0].appendChild(script);

function getAvailableCurrencies() {
	$.get("http://127.0.0.1:8080/availableCurrencies/", (data, status) => {
		console.log(data);
		$.each(data, function(index, value) {
			$('#currency1').append($('<option>').text(value).val(index));
		});
		$.each(data, function(index, value) {
			$('#currency2').append($('<option>').text(value).val(index));
		});
	});
}

const currencyValue1 = document.getElementById("currencyValue1");
const currencyValue2 = document.getElementById("currencyValue2");

const currency1 = document.getElementById("currency1");
const currency2 = document.getElementById("currency2");

currencyValue1.addEventListener("currencyValue1", updateValue2);
currencyValue2.addEventListener("currencyValue2", updateValue1);

function updateValue2(e) {
  currencyValue2.textContent = recalculateCurrency2();
}

function updateValue1(e) {
  currencyValue1.textContent = recalculateCurrency1();
}

function recalculateCurrency1() {
	$.get("http://127.0.0.1:8080/currencies/", (data, status) => {
		var selectedCurrency2 = currency1.value;
		var selectedValue = data.get(selectedCurrency2);
		return currencyValue1 * selectedValue;
	});
	
}

function recalculateCurrency2() {
	$.get("http://127.0.0.1:8080/currencies/", (data, status) => {
		var selectedCurrency1 = currency1.value;
		var selectedValue = data.get(selectedCurrency1);
		return currencyValue2 * selectedValue;
	});
	
}