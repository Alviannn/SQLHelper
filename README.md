# SQLHelper
[![](https://jitpack.io/v/Alviannn/SQLHelper.svg)](https://jitpack.io/#Alviannn/SQLHelper)

A java SQL tool

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

        sql.connect();

        int count = 0;
        try (Results results = sql.getResults("SELECT * FROM table;")) {
            ResultSet set = results.getResultSet();

            while (set.next()) {
                count++;
                print("[" + count + "] " + set.getString("name"));
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
    <artifactId>SQLTool</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
