package com.tashambra.mobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static com.tashambra.mobileapp.MainActivity.mDrinkSessionDrinks;


public class GraphActivity extends Fragment{
    private final Handler mHandler = new Handler();
    private Runnable mTimer2;
    private TextView mGraphTextView;
    private LineGraphSeries<DataPoint> mSeries2;
    private LineGraphSeries<DataPoint> mSeries3;
    private double graph2LastXValue = 5d;

    private double mBACvalue;
    public static String Gender;
    public static double Weight;
    private double AlcoholPercent;
    private double AlcoholVolume;
    private double TimePassed;
    private long initialTime;
    private FloatingActionButton mBeerButton;
    private FloatingActionButton mWineButton;
    private FloatingActionButton mShotsButton;
    private FloatingActionButton mMyDrinksButton;
    private FloatingActionButton mAddNewDrinkButton;
    List<Drink> myDrinksList = new ArrayList<>();
    private GestureDetectorCompat detector;
    private EditText mDrinkname;
    public String mdrinkname;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private TextView mText;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_graph, container, false);

        mText = (TextView) v.findViewById(R.id.list_drinks);

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (getBAC()>0.08){
                            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(1000);
                            Toast.makeText(getActivity(),"BAC too high! Swipe Up quickly",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }, 20, 20, TimeUnit.SECONDS);
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {

                        if(e1.getY() - e2.getY() > 50){
                            Intent intent = new Intent(getActivity(),features.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });


        mGraphTextView = (TextView) v.findViewById(R.id.graph_text_view);
        //Buttons FABs to be
        mBeerButton = (FloatingActionButton) v.findViewById(R.id.beer_button);
        mBeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinksDialogBox(new Beer());
            }
        });

        mWineButton = (FloatingActionButton) v.findViewById(R.id.wine_button);
        mWineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDrinksDialogBox(new Wine());
            }
        });

        mShotsButton = (FloatingActionButton) v.findViewById(R.id.shots_button);
        mShotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinksDialogBox(new Shot());
            }
        });

        mAddNewDrinkButton = (FloatingActionButton) v.findViewById(R.id.add_new_drink_button);
        mDrinkname = (EditText) v.findViewById(R.id.drink_name_edit_text);

        mAddNewDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNewDrinkDialogBox();


            }
        });


        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MainAdapter((ArrayList<Drink>) mDrinkSessionDrinks);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mMyDrinksButton = (FloatingActionButton) v.findViewById(R.id.my_drinks_button);
        mMyDrinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseMyDrinksDialogBox();
            }
        });

        populateDrinks(v);

        //Getting the values stored
        SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        String weightString = defaultsSharedPref.getString("WeightInPounds", "150");
        Weight = Double.valueOf(weightString);
        Gender = defaultsSharedPref.getString("Gender", "female");
        getBAC();
        mGraphTextView.setText(String.valueOf((new DecimalFormat(".####").format(getmBACvalue()))));
        mGraphTextView.setShadowLayer(3,3,3,Color.BLACK);
        final GraphView graph = (GraphView) v.findViewById(R.id.graph);
        mSeries2 = new LineGraphSeries<>();
        mSeries3 = new LineGraphSeries<>();
        mSeries2.setColor(Color.parseColor("#AB4A4A"));
        graph.getBackground();
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(s)");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitle("BAC");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setTextSize(25);
        mSeries2.setDrawBackground(true);
        mSeries2.setBackgroundColor(Color.parseColor("#F77878"));
        graph.setBackgroundColor(Color.TRANSPARENT);
//        graph.setBackgroundColor(Color.parseColor("#ffffff"));

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);// It will remove the background grids

//        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);// remove horizontal x labels and line
//        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);

        mSeries2.setThickness(10);
        graph.addSeries(mSeries2);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.addSeries(mSeries3);
//        graph.setDrawBackground(true);
        mSeries3.appendData(new DataPoint(0,0.08), true, 10000);

        final long initialTimeFinal = System.currentTimeMillis();

        mTimer2 = new Runnable() {
            @Override
            public void run() {
                mGraphTextView.setShadowLayer(3,3,3,Color.BLACK);
                graph2LastXValue = (System.currentTimeMillis()-initialTimeFinal)/1000;
                if (getBAC()>=0.08){
                    mSeries2.setBackgroundColor(Color.parseColor("#FA0808"));
                    mSeries2.setColor(Color.parseColor("#CB0000"));
                } else {
                    mSeries2.setBackgroundColor(Color.parseColor("#F77878"));
                    mSeries2.setColor(Color.parseColor("#AB4A4A"));

                }
                mSeries2.appendData(new DataPoint(graph2LastXValue, getBAC()), true, 10000);
                mSeries3.appendData(new DataPoint(graph2LastXValue,0.08), true, 10000);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(graph2LastXValue);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(Math.max(0.001, mSeries2.getHighestValueY()*1.1));
                mGraphTextView.setText(String.valueOf((new DecimalFormat(".####").format(getmBACvalue()))));
                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(mTimer2, 0);
        return v;
    }

    public void setvisible(){
        mText.setText("List of Drinks");
        mText.setVisibility(View.VISIBLE);
    }
    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public double getAlcoholPercent() {
        return AlcoholPercent;
    }

    public void setAlcoholPercent(double alcoholPercent) {
        AlcoholPercent = alcoholPercent;
    }

    public double getAlcoholVolume() {
        return AlcoholVolume;
    }

    public void setAlcoholVolume(double alcholVolume) {
        AlcoholVolume = alcholVolume;
    }

    public double getTimePassed() {
        return TimePassed;
    }

    public void setTimePassed(double timePassed) {
        TimePassed = Math.min(timePassed, TimePassed);
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    private void setmBACvalue(double resBAC){
        mBACvalue = resBAC;
    }

    private double getmBACvalue(){
        return  mBACvalue;
    }


    private double getBAC(){
        double totalBAC = 0.0;
        for (int i =0; i < mDrinkSessionDrinks.size(); i++) {
            totalBAC += mDrinkSessionDrinks.get(i).GetAlcoholContent();
            Log.i("CURRBAC", String.valueOf(totalBAC));
        }
        setmBACvalue(totalBAC);
        return getmBACvalue();
    }

    //ALERT DIALOG AND ADDING DRINKS
    private void showDrinksDialogBox(Drink drink){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_drink, null);
        builder.setView(dialogView);
        final TextView mDrinkName = (TextView) dialogView.findViewById(R.id.drink_name_text_view);
        final EditText mVolumeEditText = (EditText) dialogView.findViewById(R.id.volume_edit_text);
        final EditText mAlcoholPercentEdit = (EditText) dialogView.findViewById(R.id.alcoholp_edit_text);
        final EditText mTimeSince = (EditText) dialogView.findViewById(R.id.time_edit_text) ;
        Log.i("getName", drink.getName());
        mDrinkName.setText(drink.getName());
        mVolumeEditText.setText(String.valueOf(drink.getVolume()));
        mAlcoholPercentEdit.setText(String.valueOf(drink.getAlcoholPercent()));
        mTimeSince.setText("0");
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Drink drink = new Drink(mDrinkName.getText().toString(), Double.valueOf(mAlcoholPercentEdit.getText().toString()), Double.valueOf(mVolumeEditText.getText().toString()));
                drink.setTimePassed(Double.valueOf(mTimeSince.getText().toString()));
                mDrinkSessionDrinks.add(drink);
                setvisible();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addNewDrinkDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_drink, null);
        builder.setView(dialogView);
        TextView mDrinkName = (TextView) dialogView.findViewById(R.id.drink_name_text_view);
        final EditText mDrinkNameEdit = (EditText) dialogView.findViewById(R.id.drink_name_edit_text);
        mDrinkName.setVisibility(View.INVISIBLE);
        mDrinkNameEdit.setVisibility(View.VISIBLE);
        final EditText mVolumeEditText = (EditText) dialogView.findViewById(R.id.volume_edit_text);
        final EditText mAlcoholPercentEdit = (EditText) dialogView.findViewById(R.id.alcoholp_edit_text);
//        Button mAddDrink = (Button) getView().findViewById(R.id.ok_button);
        final EditText mTimeSince = (EditText) dialogView.findViewById(R.id.time_edit_text) ;
        mTimeSince.setText("0");
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(dialogView.getContext());
                String totalCustomDrinks = defaultsSharedPref.getString("TotalCustomDrinks", "0");
                String jsonObjectKey = "customDrink" + (Integer.valueOf(totalCustomDrinks) + 1);
                Drink drink = new Drink(mDrinkNameEdit.getText().toString(), Double.valueOf(mAlcoholPercentEdit.getText().toString()), Double.valueOf(mVolumeEditText.getText().toString()));
                drink.setTimePassed(Double.parseDouble(mTimeSince.getText().toString()));
                mDrinkSessionDrinks.add(drink);
                SharedPreferences.Editor edit = defaultsSharedPref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(drink);
                edit.putString(jsonObjectKey, json);
                Log.i("StringObj", drink.toString());
                edit.putString("TotalCustomDrinks", String.valueOf(Integer.valueOf(totalCustomDrinks) + 1));        //Updating the value
                edit.apply();
                populateDrinks(dialogView);
                setvisible();

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void populateDrinks(View v){
        SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        Integer totalDrinks = Integer.valueOf(defaultsSharedPref.getString("TotalCustomDrinks", "0"));
        String jsonObjectKey;
        for (int i = 1; i <= totalDrinks; i++){
            jsonObjectKey = "customDrink" + i;
            String strjson = defaultsSharedPref.getString(jsonObjectKey, "0");
            Log.i("strjson", strjson);
            if (strjson != null){
                try {
                    JSONObject jsondata = new JSONObject(strjson);
                    Drink adrink = new Drink(jsondata.getString("name"), Double.valueOf(jsondata.getString("AlcoholPercent")), Double.valueOf(jsondata.getString("volume")) );
                    myDrinksList.add(adrink);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private void showChooseMyDrinksDialogBox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.list_fragment, null);
        builder.setView(dialogView);

        ListView mListView = (ListView) dialogView.findViewById(R.id.list_view) ;
        final EditText mTimeSince = (EditText) dialogView.findViewById(R.id.time_since_edit_text);
        DrinkAdapter adapter = new DrinkAdapter(getContext());
        adapter.setItems(myDrinksList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Drink drink = (Drink) parent.getAdapter().getItem(position);
                drink.setTimePassed(Double.parseDouble(mTimeSince.getText().toString()));
                mDrinkSessionDrinks.add(drink);
                setvisible();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private class DrinkAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private List<Drink> mDataSource;
        private TextView mDrinkName;
        private TextView mDrinkVolume;
        private TextView mDrinkAlcoholPercent;

        public DrinkAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<Drink> drinkList) {
            mDataSource.clear();
            mDataSource.addAll(drinkList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() { return mDataSource.size(); }

        @Override
        public Object getItem(int position) { return mDataSource.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Drink drink = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.list_item_drink, parent, false);
            mDrinkName = (TextView) rowView.findViewById(R.id.user_drinkname);
            mDrinkVolume = (TextView) rowView.findViewById(R.id.user_volume);
            mDrinkAlcoholPercent = (TextView) rowView.findViewById(R.id.user_alcoholpercent);
            mDrinkName.setText(drink.getName().toUpperCase());
            mDrinkVolume.append(String.valueOf(drink.getVolume()));
            mDrinkAlcoholPercent.append(String.valueOf(drink.getAlcoholPercent()));
            return rowView;
        }
    }
    }

