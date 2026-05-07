import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

public class Application {

    static String[][] meals = {
            {"breakfast","Omelette","10","Easy","🍳","High protein breakfast","true","250"},
            {"breakfast","Cereal with milk","3","Very Easy","🥣","Fast and simple","true","180"},
            {"breakfast","Cheese toast","5","Very Easy","🧀","Warm and quick","true","220"},
            {"breakfast","Peanut butter toast","4","Very Easy","🥜","Sweet and filling","true","300"},
            {"breakfast","Pancakes","15","Medium","🥞","Soft breakfast meal","true","350"},
            {"breakfast","French toast","12","Medium","🍞","Sweet morning meal","true","320"},
            {"breakfast","Scrambled eggs","8","Easy","🍳","Classic breakfast","true","260"},
            {"breakfast","Avocado toast","6","Easy","🥑","Healthy choice","true","280"},
            {"breakfast","Yogurt with honey","4","Very Easy","🍯","Light and fresh","true","150"},
            {"breakfast","Banana smoothie","5","Very Easy","🥤","Fresh drink meal","true","200"},
            {"breakfast","Boiled eggs","9","Easy","🥚","Simple protein meal","true","210"},
            {"breakfast","Labneh sandwich","5","Very Easy","🥪","Lebanese style breakfast","true","240"},
            {"breakfast","Manoushe zaatar","10","Easy","🫓","Classic Lebanese breakfast","true","390"},
            {"breakfast","Croissant","2","Very Easy","🥐","Quick bakery option","true","310"},
            {"breakfast","Bagel with cream cheese","6","Easy","🥯","Soft and filling","true","330"},
            {"breakfast","Granola bowl","5","Very Easy","🥣","Crunchy and light","true","290"},
            {"breakfast","Egg wrap","10","Easy","🌯","Easy morning wrap","true","340"},
            {"breakfast","Jam toast","3","Very Easy","🍓","Sweet toast","true","210"},
            {"breakfast","Apple oatmeal","8","Easy","🍎","Warm healthy bowl","true","270"},
            {"breakfast","Mini breakfast plate","12","Medium","🍽️","Balanced breakfast","true","400"},

            {"lunch","Tuna sandwich","7","Easy","🐟","Fast lunch","false","400"},
            {"lunch","Instant noodles","6","Very Easy","🍜","Lazy classic meal","true","350"},
            {"lunch","Chicken wrap","12","Medium","🌯","Simple lunch wrap","false","450"},
            {"lunch","Rice bowl","8","Easy","🍚","Filling meal","true","380"},
            {"lunch","Grilled chicken","15","Medium","🍗","Protein lunch","false","500"},
            {"lunch","Pasta","14","Medium","🍝","Classic lunch","true","420"},
            {"lunch","Burger","10","Easy","🍔","Fast food style","false","550"},
            {"lunch","Pizza slice","8","Easy","🍕","Popular choice","true","480"},
            {"lunch","Salad bowl","6","Easy","🥗","Healthy lunch","true","250"},
            {"lunch","Falafel wrap","9","Easy","🥙","Middle Eastern meal","true","430"},
            {"lunch","Chicken sandwich","10","Easy","🥪","Quick lunch","false","460"},
            {"lunch","Beef shawarma","12","Medium","🌯","Street food style","false","620"},
            {"lunch","Veggie wrap","8","Easy","🥬","Light vegetarian meal","true","340"},
            {"lunch","Mac and cheese","13","Medium","🧀","Comfort food","true","520"},
            {"lunch","Chicken salad","10","Easy","🥗","Healthy protein lunch","false","360"},
            {"lunch","Fried rice","12","Medium","🍚","Fast cooked meal","true","470"},
            {"lunch","Turkey sandwich","7","Easy","🥪","Simple sandwich","false","390"},
            {"lunch","Potato plate","14","Medium","🥔","Filling lunch","true","440"},
            {"lunch","Cheese quesadilla","9","Easy","🫓","Cheesy fast meal","true","410"},
            {"lunch","Lentil soup","15","Medium","🍲","Warm healthy lunch","true","300"},

            {"snack","Chips","2","Very Easy","🥔","Salty snack","true","200"},
            {"snack","Chocolate","1","Very Easy","🍫","Sweet snack","true","230"},
            {"snack","Fruit bowl","5","Very Easy","🍎","Fresh snack","true","120"},
            {"snack","Cookies","3","Very Easy","🍪","Sweet and quick","true","300"},
            {"snack","Popcorn","4","Very Easy","🍿","Movie snack","true","180"},
            {"snack","Ice cream","2","Very Easy","🍨","Cold snack","true","260"},
            {"snack","Granola bar","2","Very Easy","🍫","Quick energy","true","150"},
            {"snack","Nuts mix","3","Very Easy","🥜","Protein snack","true","220"},
            {"snack","Cheese cubes","2","Very Easy","🧀","Simple snack","true","210"},
            {"snack","Banana","1","Very Easy","🍌","Natural snack","true","100"},
            {"snack","Apple slices","3","Very Easy","🍎","Light snack","true","90"},
            {"snack","Peanut butter crackers","4","Very Easy","🥜","Crunchy snack","true","280"},
            {"snack","Mini sandwich","5","Easy","🥪","Small bite","true","260"},
            {"snack","Yogurt cup","2","Very Easy","🥛","Cold and light","true","140"},
            {"snack","Pretzels","2","Very Easy","🥨","Salty snack","true","190"},
            {"snack","Protein bar","2","Very Easy","🍫","Energy snack","true","250"},
            {"snack","Carrot sticks","5","Very Easy","🥕","Healthy snack","true","80"},
            {"snack","Nachos","6","Easy","🧀","Cheesy snack","true","370"},
            {"snack","Toast with jam","4","Very Easy","🍞","Sweet quick snack","true","210"},
            {"snack","Dates with nuts","3","Very Easy","🌰","Sweet energy snack","true","240"}
    };

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/meal", (HttpExchange exchange) -> {
            String query = exchange.getRequestURI().getQuery();

            String category = getValue(query, "type");
            String vegetarian = getValue(query, "vegetarian");
            String maxTimeText = getValue(query, "maxTime");
            String maxCaloriesText = getValue(query, "maxCalories");

            int maxTime = parseNumber(maxTimeText, 999);
            int maxCalories = parseNumber(maxCaloriesText, 9999);

            String[][] filtered = new String[meals.length][];
            int count = 0;

            for (String[] meal : meals) {
                boolean categoryOk = category == null || category.equals("all") || meal[0].equals(category);
                boolean vegetarianOk = vegetarian == null || vegetarian.equals("false") || meal[6].equals("true");
                boolean timeOk = Integer.parseInt(meal[2]) <= maxTime;
                boolean caloriesOk = Integer.parseInt(meal[7]) <= maxCalories;

                if (categoryOk && vegetarianOk && timeOk && caloriesOk) {
                    filtered[count] = meal;
                    count++;
                }
            }

            String response;

            if (count == 0) {
                response = "{"
                        + "\"category\":\"none\","
                        + "\"name\":\"No meal found\","
                        + "\"time\":\"0\","
                        + "\"difficulty\":\"None\","
                        + "\"emoji\":\"😅\","
                        + "\"note\":\"Try changing the filters\","
                        + "\"vegetarian\":\"true\","
                        + "\"calories\":\"0\""
                        + "}";
            } else {
                Random random = new Random();
                String[] meal = filtered[random.nextInt(count)];

                response = "{"
                        + "\"category\":\"" + meal[0] + "\","
                        + "\"name\":\"" + meal[1] + "\","
                        + "\"time\":\"" + meal[2] + "\","
                        + "\"difficulty\":\"" + meal[3] + "\","
                        + "\"emoji\":\"" + meal[4] + "\","
                        + "\"note\":\"" + meal[5] + "\","
                        + "\"vegetarian\":\"" + meal[6] + "\","
                        + "\"calories\":\"" + meal[7] + "\""
                        + "}";
            }

            send(exchange, response);
        });

        server.start();
        System.out.println("Server running on http://localhost:8080/meal");
    }

    static int parseNumber(String text, int defaultValue) {
        try {
            if (text == null || text.isEmpty()) return defaultValue;
            return Integer.parseInt(text);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    static String getValue(String query, String key) {
        if (query == null) return null;

        String[] parts = query.split("&");
        for (String part : parts) {
            String[] pair = part.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return null;
    }

    static void send(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}