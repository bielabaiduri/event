package com.splash.user.event;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtVwEveName,txtVwEveDate,txtVwEveTime,txtVwEvePlace,txtVwEveDesc,txtVwEveHandle,txtVwEveTarget;
    ListView listEvents;
    List<Event> events = new ArrayList<Event>();
    StableArrayAdapter stableArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       listEvents = findViewById(R.id.listevents);


        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
       /* final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listEvents.setAdapter(adapter);*/


        stableArrayAdapter = new StableArrayAdapter(this,events);
        listEvents.setAdapter(stableArrayAdapter);

       listEvents.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }

       });

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        fnGetData(this );

        //requstQueue.start();

    }



    public void fnGetData(final MainActivity mainActivity)
    {
        String strUrl = getString(R.string.url_webapi)+"?selectFn=event"  ;
        HashMap data = new HashMap();
        data.put("selectFn","event");
        RequestQueue requstQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonObject = new JsonArrayRequest(Request.Method.GET, strUrl,  null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {

                    for(int i = 0 ; i < response.length();i++)
                    {
                        JSONObject object = response.getJSONObject(i);
                        Event tempevent = new Event(object.getString("Eventname").toString(),object.getString("Eventdate").toString(),
                                object.getString("Eventtime").toString(),object.getString("Eventplace").toString(),object.getString("Description").toString(),object.getString("Handleby").toString(),object.getString("Targetcus").toString());
                        events.add(tempevent);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stableArrayAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });
        requstQueue.add(jsonObject);


    }

    private class StableArrayAdapter extends BaseAdapter {

        private final Activity context;
        private final List<Event> values;

        public StableArrayAdapter(Activity context, List<Event> objects) {
            //super(context,R.layout.listviewevents,  objects);
            this.context  = context;
            this.values = objects;
        }


        @Override
        public View getView(int position, View convertView,   ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = inflater.inflate(R.layout.listviewevents,parent,false);

            txtVwEveName =  (TextView) convertView.findViewById(R.id.txtEveName);
            txtVwEveDate = (TextView) convertView.findViewById(R.id.txtVwEveDate);
            txtVwEveTime = (TextView) convertView.findViewById(R.id.txtVwEveTime);
            txtVwEvePlace = (TextView) convertView.findViewById(R.id.txtVwEvePlace);
            txtVwEveDesc = (TextView) convertView.findViewById(R.id.txtVwEveDesc);
            txtVwEveHandle = (TextView) convertView.findViewById(R.id.txtVwEveHandle);
            txtVwEveTarget = (TextView) convertView.findViewById(R.id.txtVwEveTarget);

            //for(int i = 0; i < values.length;i++)
            {
                txtVwEveName.setText("Event Name: " + values.get(position).getStrEventName());
                txtVwEveDate.setText("Event Date: " + values.get(position).getStrEventDate());
                txtVwEveTime.setText("Event Time: " +values.get(position).getStrEventTime());
                txtVwEvePlace.setText("Event Place: " + values.get(position).getStrEventPlace());
                txtVwEveDesc.setText("Description: " + values.get(position).getStrEventDate());
                txtVwEveHandle.setText("Handle By: " + values.get(position).getStrHandleBy());
                txtVwEveTarget.setText("Target Cust: " + values.get(position).getStrTarget());
            }


            return convertView;

        }


        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
