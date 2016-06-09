
package com.example.andrea.littewhale;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andrea.utils.Weather;
import com.example.andrea.utils.WeatherStorage;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class FiveDayWeatherListAdapter extends RecyclerView.Adapter<FiveDayWeatherListAdapter.ViewHolder> {
    private static Typeface weatherFont;
    private WeatherStorage weatherStorage;
    private static DecimalFormat decimalFormat;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout weatherItemLayout;
        public Map<Integer, TextView> textViews = new HashMap<Integer, TextView>();

        static final int[] textViewsFontChangeIDs = {R.id.editTextWindDirFiveDay, R.id.editTextTemperatureFiveDay,
                R.id.editTextWindSpeedFiveDay, R.id.editTextHumidityFiveDay, R.id.editTextPressureFiveDay
                , R.id.editTextCurWeatherIconFiveDay, R.id.editTextCloudsFiveDay};
        static final int[] textViewsValueIDs = {R.id.editTextWindDirValueFiveDay, R.id.editTextTemperatureValueFiveDay,
                R.id.editTextWindSpeedValueFiveDay, R.id.editTextHumidityValueFiveDay, R.id.editTextPressureValueFiveDay
                , R.id.editTextCurWeatherIconFiveDay, R.id.editTextCloudsValueFiveDay, R.id.editTextDateFiveDay};

        public ViewHolder(LinearLayout weatherItemLayout) {
            super(weatherItemLayout);
            this.weatherItemLayout = weatherItemLayout;

            for (int id : textViewsFontChangeIDs) {
                TextView curTextView = (TextView) weatherItemLayout.findViewById(id);
                if (curTextView != null) {
                    textViews.put(id, curTextView);
                    curTextView.setTypeface(weatherFont);
                }
            }

            for (int valueId : textViewsValueIDs) {
                textViews.put(valueId, (TextView) weatherItemLayout.findViewById(valueId));
            }
        }

        public void updateWeather(Weather weather) {
            textViews.get(R.id.editTextWindDirValueFiveDay).setText(decimalFormat.format(weather.getWindDirection()) + "°");
            textViews.get(R.id.editTextTemperatureValueFiveDay).setText(decimalFormat.format(weather.getTemperature()) + "°C");
            textViews.get(R.id.editTextWindSpeedValueFiveDay).setText(decimalFormat.format(weather.getWindSpeed()) + "m/s");
            textViews.get(R.id.editTextHumidityValueFiveDay).setText(weather.getHumidity() + "%");
            textViews.get(R.id.editTextPressureValueFiveDay).setText(decimalFormat.format(weather.getPressure()) + "hPa");
            textViews.get(R.id.editTextCurWeatherIconFiveDay).setText(weather.getWeatherIcon());
            textViews.get(R.id.editTextCloudsValueFiveDay).setText(weather.getClouds() + "%");
            textViews.get(R.id.editTextDateFiveDay).setText(weather.getFormattedDate() + " " + weather.getFormattedTime());
        }

        private void setFont(int id) {
            for (Map.Entry<Integer, TextView> textView : textViews.entrySet()) {
                textView.getValue().setTextColor(weatherItemLayout.getResources().getColor(id));
            }
        }
    }


    public FiveDayWeatherListAdapter(Typeface weatherFont, DecimalFormat decimalFormat) {
        this.weatherFont = weatherFont;
        this.decimalFormat = decimalFormat;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FiveDayWeatherListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.five_day_weather_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        int storagePosition = position + 1; // 0 is current weather
        if (storagePosition < weatherStorage.size()) {
            holder.updateWeather(weatherStorage.get(storagePosition));
            if (storagePosition % 2 == 0) {
                holder.weatherItemLayout.setBackgroundColor(holder.weatherItemLayout.getResources().getColor(R.color.colorAccent));
                holder.setFont(R.color.colorWhite);
            } else {
                holder.weatherItemLayout.setBackgroundColor(0x00000000); //Transparent
                holder.setFont(R.color.colorBlack);
            }
        } else {
            Log.e("donneerstag", "else error!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    public void replaceWeatherStorage(WeatherStorage weatherStorage) {
        this.weatherStorage = weatherStorage;
        notifyItemRangeInserted(0, weatherStorage.size());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return weatherStorage.size();
    }
}