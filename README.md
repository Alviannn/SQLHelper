# SQLHelper
[![](https://jitpack.io/v/Alviannn/SQLHelper.svg)](https://jitpack.io/#Alviannn/SQLHelper)

A java SQL tool (only MYSQL, SQLITE, and H2)

Example code:
```java
public class Example {

    public static void main(String[] args) throws SQLException {
        SQLTool sql = SQLTool.newBuilder(SQLTool.Type.MYSQL)
                .setHost("localhost").setPort("3306")
                .setDatabase("database")
                .setUsername("root").setPassword("password")
                .setHikari(true)
                .toSQL();

        Properties config = new Properties();

        config.setProperty("serverTimezone", "UTC");
        config.setPropety("useSSL", "false");

        sql.connect();

        int count = 0;
        try (Results results = sql.getResults("SELECT * FROM table;")) {
            ResultSet set = results.getResultSet();

            while (set.next()) {
                count++;
                print("[" + count + "] " + set.getString("column"));
            }
        }

        sql.disconnect();
    }

    private static void print(Object object) {
        System.out.println(object);
    }

}
```

### How to setup

#### Maven
1. Insert this to your repository section (on the pom.xml)
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

2. Insert this to your dependency section (on the pom.xml)
```xml
<dependency>
    <groupId>com.github.Alviannn</groupId>
    <artifactId>SQLHelper</artifactId>
    <version>2.0</version>
</dependency>
```
