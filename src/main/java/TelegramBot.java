import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class TelegramBot {

    private final String tokenID = "1883741918:AAHLNZd_LxVkYkj8_ePUPjhSsLcpMAKv3LI";
    private String user_id = null;
    private boolean id_found = false;


    public boolean add_id(String user) {
        RestTemplate restTemplate = new RestTemplate();
        String response = (String)restTemplate.getForObject("https://api.telegram.org/bot" + this.tokenID + "/getUpdates", String.class, new Object[0]);
        String id = StringUtils.substringBetween(response, "\"username\":\"" + user + "\",\"language_code\":\"es\"},\"chat\":{\"id\":", ",\"first_name\":");
        if(id == null) {
            return false;
        } else {
            this.id_found = true;
            this.user_id = id;
            return true;
        }
    }

    public boolean get_idfound() {
        return this.id_found;
    }

    public boolean notification(String texto) {
        if(this.user_id == null) {
            return false;
        } else {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            JSONObject json = new JSONObject();
            json.put("chat_id", "796710443");
            json.put("text", texto);

            try {
                HttpPost request = new HttpPost("https://api.telegram.org/bot1883741918:AAHLNZd_LxVkYkj8_ePUPjhSsLcpMAKv3LI/sendMessage");
                StringEntity params = new StringEntity(json.toString());
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                httpclient.execute(request);
            } catch (Exception var6) {
                ;
            }

            return true;
        }
    }
}
