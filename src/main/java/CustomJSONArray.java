import org.json.JSONArray;
import org.json.JSONObject;

public class CustomJSONArray extends JSONArray {

    public void addCustomArrayPoint(String key, Point value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        this.put(jsonObject);
    }
}
