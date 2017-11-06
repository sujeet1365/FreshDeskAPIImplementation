package com.example.sujeet.freshdeskintegration;

import java.io.BufferedReader;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static ListView conversationList;

    EditText edt_email;
    EditText edt_name;
    EditText edt_subject;
    EditText edt_description;
    Button btn_generate_ticket;
    TextView txt_chat;
    Button btn_show_conversation;
    public static String name,email,subject,description;
    public static ProgressDialog progressDialog;
    Spinner faq_spinner;
    public static SharedPreferences preferences;
    public static String id;
    public static ArrayList<String> conversation;
    public static  String[] conversation_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_show_conversation = (Button) findViewById(R.id.btn_show_conversation);
        faq_spinner = (Spinner)findViewById(R.id.faq_spinner);
        conversationList = (ListView) findViewById(R.id.conversationList);
        txt_chat = (TextView)findViewById(R.id.txt_chat);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Working");
        progressDialog.setMessage("Please wait...");

        conversation = new ArrayList<String>();
        preferences = getSharedPreferences("Ticket Id",MODE_PRIVATE);
        id =preferences.getString("id","");


        if(!id.equals("")) {
            progressDialog.show();
            new MyTask2().execute();
        }
            btn_show_conversation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    conversation_array = new String[conversation.size()];
                    conversation_array = conversation.toArray(conversation_array);
                    if(!conversation.isEmpty())
                        txt_chat.setText(conversation.get(0));
                    else
                        Toast.makeText(MainActivity.this, "No Conversation Availbale", Toast.LENGTH_SHORT).show();
                    Log.e("List######",conversation+"");
//                    ArrayAdapter_Custom<String> adapter = new ArrayAdapter_Custom<String>(MainActivity.this,R.layout.conversation,conversation_array);
                    ArrayAdapter_Custom adapter = new ArrayAdapter_Custom(MainActivity.this,R.layout.conversation,conversation_array);
                    conversationList.setAdapter(adapter);
//                    progressDialog.dismiss();
                }
            });






        name = "Sujeet";
        email = "speedoo7g@hotmail.com";
        subject = "Money Problem";


        final String[] faqs = {" Select Your Querry","1. Maximum ammount I can get as Loan?","2. How much I'll get after investment?","3. For how long I can get the money"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,faqs);
        faq_spinner.setAdapter(adapter);
        faq_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 1:
                        progressDialog.show();
                        description = faqs[1];
                        new Mytask1().execute();
                        Toast.makeText(MainActivity.this, "Ticket Created Successfully",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        progressDialog.show();
                        description = faqs[2];
                        new Mytask1().execute();
                        Toast.makeText(MainActivity.this, "Ticket Created Successfully", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        progressDialog.show();
                        description = faqs[3];
                        new Mytask1().execute();
                        Toast.makeText(MainActivity.this, "Ticket Created Successfully", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
class Mytask1 extends AsyncTask<Integer, Integer, String> {

    @Override
    protected String doInBackground(Integer... integers) {

        String str="";
        try {
            URL url = null;
            url = new URL(ApiName.endPoint + ApiName.createTicket);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            String basicAuth = "" + new String(new Base64().encode(ApiName.token.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            CreateApi ct = new CreateApi();
            ct.setSubject(MainActivity.subject);
            ct.setEmail(MainActivity.email);
            ct.setDescription(MainActivity.description);
            ct.setName(MainActivity.name);
            wr.write(ct.toString());
            wr.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            str = br.readLine();
            MainActivity.progressDialog.dismiss();

            JSONObject object = new JSONObject(str);
            String id = object.get("id").toString();

            SharedPreferences.Editor editor = MainActivity.preferences.edit();
            editor.putString("id",id);
            editor.commit();

            Log.e("id",id);
        }catch (Exception e) {
            Log.e("IoException",""+e);
        }
        return str;
    }
}
class MyTask2 extends AsyncTask<Integer, Integer, ArrayList<String>>
{

    @Override
    protected ArrayList<String> doInBackground(Integer... integers) {
//        public ArrayList<String> ticketConversation(String ticketId) throws IOException, JSONException{
//        ArrayList<String> conversation = new ArrayList<String>();
            MainActivity.conversation.clear();
        try {
            URL url = new URL(ApiName.endPoint + ApiName.ticketConversation(MainActivity.id));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            String basicAuth = "" + new String(new Base64().encode(ApiName.token.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            String str = br.readLine();
            JSONArray jsonarray = new JSONArray(str);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject object = jsonarray.getJSONObject(i);
                MainActivity.conversation.add(object.get("body_text").toString());
            }
            MainActivity.progressDialog.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return MainActivity.conversation;
    }
}

