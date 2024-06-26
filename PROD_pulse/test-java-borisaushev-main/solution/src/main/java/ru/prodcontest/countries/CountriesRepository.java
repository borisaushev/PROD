package ru.prodcontest.countries;

import org.springframework.stereotype.Repository;

@Repository
public class CountriesRepository {

    public String getCountriesByRegionsSQLQuery(String[] regions) {
        if (regions == null || regions.length == 0)
            return "SELECT * FROM countries ORDER BY alpha2";

        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM countries WHERE region in (");

        for (String region : regions)
            sqlQuery.append("'" + region + "',");
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(") ORDER BY alpha2");

        return sqlQuery.toString();
    }

    public String getCountryByCode() {
        String sqlQuery = "SELECT * FROM countries WHERE alpha2 = :code";
        return sqlQuery;
    }

}
