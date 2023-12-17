package com.panca_nugraha.percoobaan9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ListView listUsers;
    private ProgressBar progressBar;
    private UserAdapter adapter;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("List of USERS");
        }

        progressBar = findViewById(R.id.progressBar);
        listUsers = findViewById(R.id.lv_list);
        adapter = new UserAdapter(this);
        userList = new ArrayList<>();

        getListUsers();

        listUsers.setOnItemClickListener((adapterView, view, i, l) -> {
            Toast.makeText(MainActivity.this, userList.get(i).getName(), Toast.LENGTH_SHORT).show();
            Log.d("Lihat", userList.get(i).getName());
        });
    }

    private void getListUsers() {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users";
        // Headers should be properly spelled (Authorization) and filled with actual values
        client.addHeader("Authorization", "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");
        client.addHeader("User-Agent", "Percobaan9");

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                ArrayList<User> listUser = new ArrayList<>();
                String result = new String(responseBody);

                try {
                    JSONArray dataArray = new JSONArray(result);
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataJson = dataArray.getJSONObject(i);
                        String name = dataJson.getString("login");
                        String type = dataJson.getString("type");
                        String photo = dataJson.getString("avatar_url");
                        User user = new User();
                        user.setPhoto(photo);
                        user.setName(name);
                        user.setType(type);
                        listUser.add(user);
                    }

                    userList = listUser;
                    adapter.setUsers(listUser);
                    listUsers.setAdapter(adapter);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Unauthorized";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                }
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
