package com.tbz.practice.tbzweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tbz.practice.tbzweather.R;
import com.tbz.practice.tbzweather.activity.MainActivity;
import com.tbz.practice.tbzweather.response.WeatherResponce;
import com.tbz.practice.tbzweather.serviceAPI.WeatherServiceApi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by USER on 15-Dec-16.
 */
public class Fragment1 extends Fragment {

    ImageView showIconIv;
    TextView temperatureTv;
    TextView weatherDesTv;
    TextView maxTempTv;
    TextView minTempTv;
    TextView windSpeedTv;
    TextView cloudlynessTv;
    TextView pressureTv;
    TextView humidityTv;
    TextView sunriseTv;
    TextView sunsetTv;
    TextView cityTv;


    WeatherResponce weatherResponce;
    Bundle b;
    String test;
    WeatherServiceApi weatherServiceApi;

    public static  final String BASE_URL= "http://api.openweathermap.org/";
    public static final String BASE_URL_OF_ICON= "http://openweathermap.org/img/w/";
    public static  final String _PNG= ".png";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.frag1,container,false);

        showIconIv= (ImageView) rootView.findViewById(R.id.weatherIcon);
        temperatureTv= (TextView) rootView.findViewById(R.id.tempTv);
        weatherDesTv= (TextView) rootView.findViewById(R.id.weatherDescriptionTV);
        maxTempTv= (TextView) rootView.findViewById(R.id.tempMaxTv);
        minTempTv= (TextView) rootView.findViewById(R.id.tempMinTv);
        windSpeedTv= (TextView) rootView.findViewById(R.id.windSpeedValueTv);
        //cloudlynessTv= (TextView) rootView.findViewById(R.id.cloudDescriptionTv);
        pressureTv= (TextView) rootView.findViewById(R.id.pressurTv);
        humidityTv= (TextView) rootView.findViewById(R.id.humidityTv);
        sunriseTv= (TextView) rootView.findViewById(R.id.sunriseTv);
        sunsetTv= (TextView) rootView.findViewById(R.id.sunsetTv);
        cityTv= (TextView) rootView.findViewById(R.id.cityTv);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherServiceApi = retrofit.create(WeatherServiceApi.class);
        calldata();

//        Toast.makeText(getContext().getApplicationContext(), weatherResponce.getMain().getTemp().toString(), Toast.LENGTH_SHORT).show();

        return rootView;

    }

    private void calldata() {
         Call<WeatherResponce> weatherResponceCall =weatherServiceApi.getWeatherInformation();

        weatherResponceCall.enqueue(new Callback<WeatherResponce>() {
            @Override
            public void onResponse(Call<WeatherResponce> call, Response<WeatherResponce> response) {

               WeatherResponce weatherResponce = response.body();
                Toast.makeText(getContext().getApplicationContext(), weatherResponce.getName()+" Frag 01", Toast.LENGTH_SHORT).show();

                /*Double tempTemp = weatherResponce.getMain().getTemp();
                String currentTemp=convertTOCelcius(tempTemp);
                Double maxTemp= weatherResponce.getMain().getTempMax();
                String maxtemp= convertTOCelcius(maxTemp);
                Double minTemp =weatherResponce.getMain().getTempMin();
                String mintemp= convertTOCelcius(minTemp);*/

                long srTime= (weatherResponce.getSys().getSunrise())*1000;
                Date sunRise=new java.util.Date(srTime);
                String sunRiseString = new SimpleDateFormat("hh:mma").format(sunRise);
                long ssTime= (weatherResponce.getSys().getSunset())*1000;
                Date sunSet=new java.util.Date(ssTime);
                String sunSetString = new SimpleDateFormat("hh:mma").format(sunSet);


                if(weatherResponce != null){
                    Picasso.with(getContext().getApplicationContext()).load(MainActivity.BASE_URL_OF_ICON+weatherResponce.getWeather().get(0).getIcon()+_PNG).resize(70,70).into(showIconIv);
                    weatherDesTv.setText(weatherResponce.getWeather().get(0).getDescription());
                    temperatureTv.setText(weatherResponce.getMain().getTemp().toString());
                    maxTempTv.setText(weatherResponce.getMain().getTempMax().toString());
                    minTempTv.setText(weatherResponce.getMain().getTempMin().toString());
                    windSpeedTv.setText(weatherResponce.getWind().getSpeed().toString()+"m/s");
//                    cloudlynessTv.setText(weatherResponce.getClouds().getAll().toString());
                    pressureTv.setText(weatherResponce.getMain().getPressure().toString() +" hpa");
                    humidityTv.setText(weatherResponce.getMain().getHumidity().toString()+" %");
                    sunriseTv.setText(sunRiseString);
                    sunsetTv.setText(sunSetString);
                    cityTv.setText(weatherResponce.getName().toString());
                }
                else
                    Toast.makeText(getContext().getApplicationContext(), "no data assigned", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WeatherResponce> call, Throwable t) {
                    temperatureTv.setText("");
                cityTv.setText("No Connection");
            }
        });
    }

    /*private String convertTOCelcius(double temp) {
        DecimalFormat df= new DecimalFormat("0.##");
        double tempCelcius=temp- 273.15;
        String tempString=String.valueOf(df.format(tempCelcius));
        return tempString;
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //weatherResponce = (WeatherResponce) getArguments().getSerializable("response");


        /*if(weatherResponce!=null)
            Toast.makeText(getContext().getApplicationContext(), "got data", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext().getApplicationContext(), "no data", Toast.LENGTH_SHORT).show();*/
    }
    public void getWeatherResponse(WeatherResponce weatherResponce){
        this.weatherResponce = weatherResponce;
    }
}
