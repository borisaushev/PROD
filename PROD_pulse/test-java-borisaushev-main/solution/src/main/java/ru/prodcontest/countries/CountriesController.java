package ru.prodcontest.countries;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;
import org.json.JSONObject;
import ru.prodcontest.Json.JsonUtil;


//с какого то момента 
//я понял что делал все коммиты
//не с того аккаунта
//и теперь мой профиль
//не такой зеленый
//:(
//в следующий раз буду повнимательнее

@RestController
public class CountriesController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping(path = "/api/countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchCountries(@RequestParam(name = "region", required = false) String[] regions,
                                  HttpServletResponse httpResponse) throws JSONException {
        String sqlQuery = getSqlQuery(regions);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //проверяем на паличие неверно указанных регионов
        if(regions != null)
            for(String region : regions)
                if(!Country.validateRegion(region))
                    return JsonUtil.getJsonErrorResponse(400, "неправильный формат региона", httpResponse);

        //делаем запрос
        List<Country> selectedCountries = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new Country(rs.getString("name"), rs.getString("alpha2"),
                            rs.getString("alpha3"), rs.getString("region")));

        //формируем ответ
        JSONArray responseArray = new JSONArray();
        
        for(Country country : selectedCountries) {
            JSONObject currentCountryJSON = new JSONObject();
            
            currentCountryJSON.put("name", country.name());
            currentCountryJSON.put("alpha2", country.alpha2());
            currentCountryJSON.put("alpha3", country.alpha3());
            currentCountryJSON.put("region", country.region());

            responseArray.put(currentCountryJSON);
        }

        return responseArray.toString();
    }

    private String getSqlQuery(String[] regions) {
        //SELECT * FROM countries WHERE region in ('Asia', 'Africa', 'Asia') ORDER BY alpha2

        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM countries ");
        if(regions != null) {
            //needed: SELECT * FROM countries WHERE region in ('Asia', 'Africa', 'Asia')

            //step1: SELECT * FROM countries WHERE region in (
            sqlQuery.append("WHERE region in (");

            //step2: SELECT * FROM countries WHERE region in ('Asia', 'Africa', 'Asia',
            for(String region : regions)
                sqlQuery.append("'" + region + "',");

            //step3: SELECT * FROM countries WHERE region in ('Asia', 'Africa', 'Asia'
            sqlQuery.deleteCharAt(sqlQuery.length() - 1);

            //step4: SELECT * FROM countries WHERE region in ('Asia', 'Africa', 'Asia')
            sqlQuery.append(") ");
        }

        //final step:
        //SELECT * FROM countries ORDER BY alpha2 ORDER BY alpha2
        //SELECT * FROM countries WHERE region in ('Asia', 'Africa', 'Asia') ORDER BY alpha2
        sqlQuery.append("ORDER BY alpha2");

        return sqlQuery.toString();
    }


    //контроллер для информации о определенной стране
    @RequestMapping(path = "/api/countries/{alpha2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchCountry(@PathVariable("alpha2") String code, HttpServletResponse httpResponse) throws JSONException {

        if(code.length() != 2 || !code.matches("[A-z]{2}"))
            return JsonUtil.getJsonErrorResponse(400, "Неправильный формат параметра alpha2", httpResponse);

        code = code.toUpperCase();
        String sqlQuery = "SELECT * FROM countries WHERE alpha2='" + code + "'";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
        try {
            
            Country selectedCountry = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) ->
                    new Country(rs.getString("name"), rs.getString("alpha2"),
                    rs.getString("alpha3"), rs.getString("region")));

            JSONObject currentCountryJSON = new JSONObject();
            currentCountryJSON.put("name", selectedCountry.name());
            currentCountryJSON.put("alpha2", selectedCountry.alpha2());
            currentCountryJSON.put("alpha3", selectedCountry.alpha3());
            currentCountryJSON.put("region", selectedCountry.region());

            return currentCountryJSON.toString();

        } catch(EmptyResultDataAccessException exception) {
            return JsonUtil.getJsonErrorResponse(404, "Страна с указанным кодом не найдена", httpResponse);
        }
    }

}
