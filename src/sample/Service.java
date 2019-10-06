package sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Currency;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class Service {
    private static String APIKey =  "440d79d63ccb3ed0cd83e283fd9bad63";
    private static String WeatherURL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String country;
    private String city;
    private String base;
    private String result_cur, zloty;


    public Service (String country){
        this.country = country;
    }


    public String getWeather(String city){
        setCity(city);
        HttpURLConnection connection = null;
        InputStream input = null;

        try {

            //set connection
            connection = (HttpURLConnection) (new URL(WeatherURL + city + "," + country + "&appid="+APIKey)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuffer buffer = new StringBuffer();
            input = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while((line = br.readLine())!= null){
                 buffer.append(line +"\r\n");
            }

            input.close();
            connection.disconnect();
            return parsetoJSON(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static String parsetoJSON(String input){
        StringBuffer sb = new StringBuffer();
        //Object obj = (Object) input;
        JSONObject jo = null;
        try {
            jo = new JSONObject(input);

            //get coord
            JSONObject coorObj = JSONGET.getObject("coord", jo);
            sb.append("lat: " + JSONGET.getFloat("lat", coorObj) + "\n");
            sb.append("lon: " + JSONGET.getFloat("lon", coorObj) +"\n");


            //get sys
            JSONObject sysObj = JSONGET.getObject("sys", jo);
            sb.append("country : " + JSONGET.getString("country", sysObj) + "\n");
            sb.append("sunrise : " + JSONGET.getInt("sunrise", sysObj) + "\n");
            sb.append("sunset : " + JSONGET.getInt("sunset", sysObj) + "\n");

            //get weather arr
            JSONArray weatherArr = jo.getJSONArray("weather");
            JSONObject jsonweather = weatherArr.getJSONObject(0);
            sb.append("id :" + JSONGET.getInt("id", jsonweather) +  "\n");
            sb.append("main :" + JSONGET.getString("main", jsonweather) + "\n");
            sb.append("description :" + JSONGET.getString("description", jsonweather) + "\n");

            //get main
            JSONObject mainObj = JSONGET.getObject("main", jo);
            sb.append("humidity : " + JSONGET.getInt("humidity", mainObj) + "\n");
            sb.append("pressure : " + JSONGET.getInt("pressure", mainObj) + "\n");
            sb.append("Max temp : " + JSONGET.getFloat("temp_max", mainObj) + "\n");
            sb.append("Min temp : " + JSONGET.getFloat("temp_min", mainObj) + "\n");

            //get wind
            JSONObject windObj = JSONGET.getObject("wind", jo);
            sb.append("speed : " + JSONGET.getFloat("speed", windObj) + "\n");
            sb.append("deg : " + JSONGET.getFloat("deg", windObj) + "\n");

            JSONObject cloudObj = JSONGET.getObject("clouds", jo);
            sb.append("all : " + JSONGET.getInt("all", cloudObj) + "\n");

            return sb.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public double getRateFor(String currencyCode){
        String URL = "https://api.exchangeratesapi.io/latest?base=";

        Locale local = null;
        for (Locale loc : Locale.getAvailableLocales()) {
            if(loc.getDisplayCountry().equals(country)) {
                local = loc;
            }
        }

        base = Currency.getInstance(local).getCurrencyCode();
        HttpURLConnection connection = null;
        InputStream input = null;

        try {

            //set connection
            connection = (HttpURLConnection) (new URL(URL + base).openConnection());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuffer buffer = new StringBuffer();
            input = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while((line = br.readLine())!= null){
                buffer.append(line +"\r\n");
            }

            input.close();
            connection.disconnect();

            JSONObject jo = new JSONObject(buffer.toString());
            JSONObject rate = JSONGET.getObject("rates", jo);
            Double result = rate.getDouble(currencyCode);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    public double getNBPRate(){
        try {
            Document pageA = Jsoup.connect("http://www.nbp.pl/kursy/kursya.html").get();
            Document pageB = Jsoup.connect("http://www.nbp.pl/kursy/kursyb.html").get();
            String query = "td:matches(" + getBase() + ")";

            Elements tableRows = pageA.select(query);
            if(tableRows.isEmpty()){
                tableRows = pageB.select(query);
                getRate(pageB);
            }

            getRate(pageA );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void getRate(Document doc){
        Elements elements = doc.select("td");

        for(int i = 1; i <elements.size() ; i++) {

            if (elements.get(i).toString().contains(base)) {

                result_cur =  elements.get(i).text();
                zloty = elements.get(i+1).text();
            }
        }



    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getResult_cur() {
        return result_cur;
    }

    public void setResult_cur(String result_cur) {
        this.result_cur = result_cur;
    }

    public String getZloty() {
        return zloty;
    }

    public void setZloty(String zloty) {
        this.zloty = zloty;
    }
}
