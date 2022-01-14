package start.openApi.openapi.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController("/api")
public class WeatherApiController {

    @GetMapping("/weather")
    public String restApiGetWeather() throws Exception {
        StringBuffer result = new StringBuffer();
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst\n" +
                "?serviceKey=C3zta1OR7DASN%2FlSZ64mg%2BnqBdohqxKLzjM%2BgGD0n2GQrp0AE14vku%2FYXdc%2BFO9enwGHQU7jFf329FMNBu5LHQ%3D%3D" +
                "&dataType=JSON" +
                "&numOfRows=10" +
                "&pageNo=1" +
                "&base_date=202200112" +
                "&base_time=0600" +
                "&nx=55" +
                "&ny=127";

        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
        System.out.println("# RESUTL : " + resultMap);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("result", resultMap);
        return jsonObj.toString();

    }

    public HashMap<String, Object> getDataFromJson(String url,String encoding, String type, String jsonStr) throws Exception {

        boolean isPost = false;

        if ("post".equals(type)) {
            isPost = true;
        } else {
            url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
        }
        return getStringFormURL(url, encoding, isPost, jsonStr, "application/json");
    }

    public HashMap<String,Object> getStringFormURL(String url, String encoding, boolean isPost, String parameter, String contentType) throws Exception {

        URL apiurl = new URL(url);
        HttpURLConnection conn = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            conn = (HttpURLConnection) apiurl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            if (isPost) {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Trpe",contentType);
                conn.setRequestProperty("Accept","*/*");
            } else {
                conn.setRequestMethod("GET");
            }

            conn.connect();

            if (isPost) {
                bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
                bw.write(parameter);
                bw.flush();
                bw = null;
            }

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));

            String line = null;

            StringBuffer result = new StringBuffer();

            while ((line=br.readLine()) != null) result.append(line);

            ObjectMapper mapper = new ObjectMapper();

            resultMap = mapper.readValue(result.toString(),HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(url + " interface failed" + e.toString());
        } finally {
            if (conn != null ) conn.disconnect();
            if (br != null) br.close();
            if (bw != null) bw.close();
        }
        return resultMap;


            /*urlConnection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String returnLine;
            result.append("<xmp>");

            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine + "\n");
            }

            // 시간 자동
            LocalTime now = LocalTime.now();        //현재시간
            System.out.println(now);
            // 포맷 정리
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
            // 포맷 적용
            String formatedNow = now.format(formatter);
            System.out.println(formatedNow);    // 06시 20분 57초
            urlConnection.disconnect();*/
    }
}
