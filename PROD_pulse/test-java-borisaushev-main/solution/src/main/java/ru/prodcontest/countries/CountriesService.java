package ru.prodcontest.countries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prodcontest.countries.Exceptions.NoSuchCountryException;

import java.util.InputMismatchException;
import java.util.List;

@Service
public class CountriesService {
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public List<Country> searchCountries(String[] regions) {

        //проверяем на наличие неверно указанных регионов
        if (regions != null)
            for (String region : regions)
                if (!Country.validateRegion(region))
                    throw new InputMismatchException("some of the regions are invalid");

        String query = countriesRepository.getCountriesByRegionsSQLQuery(regions);

        List<Country> selectedCountries = jdbcTemplate.query(query, (rs, rowNum) ->
                new Country(rs.getString("name"), rs.getString("alpha2"),
                        rs.getString("alpha3"), rs.getString("region")));
        return selectedCountries;

    }

    @Transactional
    public String searchCountry(String code) throws JSONException {
        if(code.length() != 2 || !code.matches("[A-z]{2}")) {
            System.out.println("invalid code: " + code);
            throw new InputMismatchException("country code invalid");
        }
        code = code.toUpperCase();

        String sqlQuery = countriesRepository.getCountryByCode();

        List<Country> selectedCountry = jdbcTemplate.query(sqlQuery, new MapSqlParameterSource("code", code),
                (rs, rowNum) -> new Country(rs.getString("name"), rs.getString("alpha2"),
                        rs.getString("alpha3"), rs.getString("region")));

        if(selectedCountry.isEmpty())
            throw new NoSuchCountryException("no country with such code exists");

        Country country = selectedCountry.get(0);

        JSONObject currentCountryJSON = new JSONObject();
        currentCountryJSON.put("name", country.name());
        currentCountryJSON.put("alpha2", country.alpha2());
        currentCountryJSON.put("alpha3", country.alpha3());
        currentCountryJSON.put("region", country.region());

        return currentCountryJSON.toString();

    }

    public JSONArray getJsonArray(List<Country> selectedCountries) throws JSONException {
        JSONArray responseArray = new JSONArray();

        for (Country country : selectedCountries) {
            JSONObject currentCountryJSON = new JSONObject();

            currentCountryJSON.put("name", country.name());
            currentCountryJSON.put("alpha2", country.alpha2());
            currentCountryJSON.put("alpha3", country.alpha3());
            currentCountryJSON.put("region", country.region());

            responseArray.put(currentCountryJSON);
        }
        return responseArray;
    }

}
