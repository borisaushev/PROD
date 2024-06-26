package ru.prodcontest.countries;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@ResponseStatus(HttpStatus.OK)
public class CountriesController {

    @Autowired
    private CountriesService countriesService;

    @GetMapping
    @ResponseBody
    public String searchCountries(@RequestParam(name = "region", required = false) String[] regions) throws JSONException {

        //делаем запрос
        List<Country> selectedCountries = countriesService.searchCountries(regions);

        //формируем ответ
        var JSONArray = countriesService.getJsonArray(selectedCountries);

        return JSONArray.toString();
    }

    //контроллер для информации об определенной стране
    @GetMapping(path = "{alpha2}")
    @ResponseBody
    public String searchCountry(@PathVariable("alpha2") String code) throws JSONException {
        return countriesService.searchCountry(code);
    }

}
