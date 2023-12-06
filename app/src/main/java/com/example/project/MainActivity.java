package com.example.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText foodSearchEditText;
    private Button searchButton;
    private TextView dietRecordTextView;

    private static final String DIETAGRAM_API_ENDPOINT = "https://dietagram.p.rapidapi.com/";
    private static final String RAPID_API_KEY = "c58ea99313msh7a24d28ca9bf167p1d1e62jsna5976660ee8e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodSearchEditText = findViewById(R.id.foodSearchEditText);
        searchButton = findViewById(R.id.searchButton);
        dietRecordTextView = findViewById(R.id.dietRecordTextView);

        searchButton.setOnClickListener(v -> {
            String foodName = foodSearchEditText.getText().toString().trim();
            if (!foodName.isEmpty()) {
                fetchFoodCalories(foodName);
            } else {
                Toast.makeText(this, "음식을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFoodCalories(String foodName) {
        String url = DIETAGRAM_API_ENDPOINT + "food/search?query=" + foodName;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject firstResult = response.getJSONArray("result").optJSONObject(0);
                            String calories = firstResult.optString("calories");

                            dietRecordTextView.setText("칼로리: " + calories);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "칼로리 정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "API 요청 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key", RAPID_API_KEY);
                headers.put("X-RapidAPI-Host", "dietagram.p.rapidapi.com");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}