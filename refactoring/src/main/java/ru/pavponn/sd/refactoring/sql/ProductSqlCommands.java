package ru.pavponn.sd.refactoring.sql;

public class ProductSqlCommands {

    public static String getAllProductsSql() {
        return "SELECT * FROM PRODUCT";
    }

    public static String getMinPriceProductSql() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    }

    public static String getMaxPriceProductSql() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    }

    public static String getSumPricesSql() {
        return "SELECT SUM(price) FROM PRODUCT";
    }

    public static String getCountSql() {
        return "SELECT COUNT(*) FROM PRODUCT";
    }

    public static String addProductSql(String name, long price) {
        return "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
    }

}
