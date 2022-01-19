package start.openApi.openapi.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.var;
//import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController     // 기본으로 하위에 있는 메소드들은 모두 @ResponseBody를 가지게 된다.
                    // @RequestBody : 클라이언트가 요청한 XML/JSON을 자바 객체로 변환해서 전달 받을 수 있다.
                    // @ReqonseBody : 자바 객체를 XML/JSON으로 변환해서 응답 객체의 Body에 실어 전송할 수 있다.
    // 클라이언트에게 JSON 객체를 받아야 할 경우에는 @RequestBody, 자바 객체를 클라이언트에게 JSON으로 전달해야할 경우에는
    // @ResponseBody 어노테이션을 붙여주면 된다.
    // @ResponseBody를 사용한 경우 View가 아닌 자바 객체를 리턴해주면 된다.
@RequestMapping("/api")     // 클라이언트는 URL로 요청을 전송하고, 요청 URL을 어떤 메서드가 처리할지 여부를 결정하는 것
@Transactional
@RequiredArgsConstructor
public class WeatherApiController {

    @Autowired DbRepository dbRepository;

    @GetMapping("/weather")
    public String restApiGetWeather() throws Exception {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2= new SimpleDateFormat("HHmm");
        String nowTime1 = sdf1.format(System.currentTimeMillis());
        String nowTime2 = sdf2.format(System.currentTimeMillis());


        StringBuffer result = new StringBuffer();
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" +
                "?serviceKey=C3zta1OR7DASN%2FlSZ64mg%2BnqBdohqxKLzjM%2BgGD0n2GQrp0AE14vku%2FYXdc%2BFO9enwGHQU7jFf329FMNBu5LHQ%3D%3D" +
                "&dataType=JSON" +
                "&numOfRows=10" +
                "&pageNo=1" +
                "&base_date=20220118"+
                "&base_time=0600"+
                "&nx=55" +
                "&ny=127";


        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
        System.out.println("# RESUTL : " + resultMap);
        JSONObject jsonObj = new JSONObject();      // Json 형태로 바꿔주는 메서드
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
        BufferedReader br = null;       // 데이터를 한번에 받아서 읽는
        BufferedWriter bw = null;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            conn = (HttpURLConnection) apiurl.openConnection();
            conn.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            conn.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
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

            while ((line=br.readLine()) != null) result.append(line);       // null이 아니면 문자 붙이는거

            ObjectMapper mapper = new ObjectMapper();

            resultMap = mapper.readValue(result.toString(),HashMap.class);  // readValue -> json 문자를 자바 맵으로 변환

            String result1 = result.toString();

            JSONObject result2 = new JSONObject(result1);    // result값 가져오기

            JSONObject response = result2.getJSONObject("response");    // response값 가져오기

            JSONObject body = response.getJSONObject("body");           // body값 가져오기

            JSONObject items = body.getJSONObject("items");             // items값 가져오기

            JSONArray item = items.getJSONArray("item");            // item이 배열로 들어있어서 Array로 가져오기

            List<DB> dbItems = new ArrayList<>();

            for (int i = 0; i < item.length(); i++) {
                JSONObject obj = item.getJSONObject(i);
                int id = i+1;
                String baseDate = obj.getString("baseDate");
                String baseTime = obj.getString("baseTime");
                String category = obj.getString("category");
                int nx = obj.getInt("nx");
                int ny = obj.getInt("ny");
                String obsrValue = obj.getString("obsrValue");

                if(category.equals("T1H") || category.equals("RN1") || category.equals("REH") || category.equals("PTY")) {

                    DB db = new DB(id,baseDate,baseTime,category,nx,ny,obsrValue);

                    dbRepository.save(db);

                    System.out.println("id("+i+"): " + id);
                    System.out.println("baseDate("+i+"): " + baseDate);
                    System.out.println("baseTime("+i+"): " + baseTime);
                    System.out.println("category("+i+"): " + category);
                    System.out.println("nx("+i+"): " + nx);
                    System.out.println("ny("+i+"): " + ny);
                    System.out.println("obsrValue("+i+"): " +obsrValue);
                    System.out.println();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(url + " interface failed" + e.toString());
        } finally {
            if (conn != null ) conn.disconnect();
            if (br != null) br.close();
            if (bw != null) bw.close();
        }
        return resultMap;

    }
}
