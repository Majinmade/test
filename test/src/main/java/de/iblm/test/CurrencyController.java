package de.iblm.test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CurrencyController {

	@Autowired
	CSVReader reader;

	@GetMapping("/currencies/")
	public Map<String, String> getCurrencies() {
		loadCurrencies();
		return reader.getMappedCols();
	}

	@GetMapping("/currencies/{SYMBOL}")
	public String getCurrenciesPerSymbol(@PathVariable("SYMBOL") String symbol) {
		loadCurrencies();
		if (reader.getMappedCols().containsKey(symbol)) {
			return reader.getMappedCols().get(symbol);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency not found");
		}
	}
	
	@GetMapping("/availableCurrencies")
	public Set<String> getAvailableCurrencies() {
		loadCurrencies();
		return reader.getMappedCols().keySet();
	}

	private void loadCurrencies() {
		reader.readCsv("https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip");
	}
}
