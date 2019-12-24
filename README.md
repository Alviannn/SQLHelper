# SQLHelper
[![](https://jitpack.io/v/Alviannn/SQLHelper.svg)](https://jitpack.io/#Alviannn/SQLHelper)

A java SQL tool (only MYSQL, SQLITE, and H2)

Example code:
```java
public class Example {

    public static void main(String[] args) throws SQLException {
        SQLHelper sql = SQLHelper.newBuilder(SQLHelper.Type.MYSQL)
                .setHost("localhost").setPort("3306")
                .setDatabase("uhc_db?useSSL=false&serverTimezone=UTC")
                .setUsername("root").setPassword("")
                .setHikari(true)
                .toSQL();

        sql.connect();
        print("SQL connected!\n");

        sql.executeQuery("CREATE TABLE IF NOT EXISTS stats (name TINYTEXT NOT NULL, uuid TINYTEXT NOT NULL);");

        int count = 0;
        try (Results results = sql.getResults("SELECT * FROM stats;")) {
            ResultSet set = results.getResultSet();

            while (set.next()) {
                count++;

                String name = set.getString("name");
                String uuid = set.getString("uuid");

                print("[" + count + "] " + name + " - " + uuid + " (uuid-length: " + uuid.length() + ")");
            }
        }

        sql.disconnect();
        print("\nSQL disconnected!");
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
    <version>2.3</version>
</dependency>
```
