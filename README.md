# SQLHelper
[![](https://jitpack.io/v/Alviannn/SQLHelper.svg)](https://jitpack.io/#Alviannn/SQLHelper)

A java SQL tool

Example code:
```java
public class Example {

    public static void main(String[] args) throws SQLException {
        SQLHelper helper = SQLHelper.newBuilder(SQLHelper.Type.MYSQL)
                .setHost("localhost").setPort("3306")
                .setDatabase("uhc_db?useSSL=false&serverTimezone=UTC")
                .setUsername("root").setPassword("")
                .setHikari(true)
                .toSQL();

        helper.connect();
        println("SQL connected!\n");

        helper.executeQuery("CREATE TABLE IF NOT EXISTS stats (name TINYTEXT NOT NULL, kills INT DEFAULT '0');");
        helper.executeQuery("INSERT INTO stats (name, kills) VALUES ('Alviann', '10');");

        insertRandomStuff(helper);

        int count = 0;
        try (Results results = helper.results("SELECT * FROM stats;")) {
            ResultSet set = results.getResultSet();

            while (set.next()) {
                count++;

                String name = set.getString("name");
                int kills = set.getInt("kills");

                println("[" + count + "] " + name + " - " + kills);
            }
        }

        helper.disconnect();
        println("\nSQL disconnected!");
    }

    private static void insertRandomStuff(SQLHelper sql) throws SQLException {
        for (int i = 0; i < 25; i++) {
            String randomName = randomString(18);
            int randomKills = new Random().nextInt(100);

            sql.query("INSERT INTO stats (name, kills) VALUES (?, ?);")
                    .execute(randomName, randomKills);
        }
    }

    public static String randomString(int length) {
        Random random = new Random();

        char[] normal = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] capital = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] number = "0123456789".toCharArray();

        StringBuilder builder = new StringBuilder();
        while (builder.length() < length) {
            if (random.nextBoolean())
                builder.append(capital[random.nextInt(capital.length)]);
            else if (random.nextBoolean())
                builder.append(number[random.nextInt(number.length)]);
            else
                builder.append(normal[random.nextInt(normal.length)]);
        }

        return builder.toString();
    }

    private static void println(Object object) {
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
    <version>2.5.5</version>
</dependency>
```
