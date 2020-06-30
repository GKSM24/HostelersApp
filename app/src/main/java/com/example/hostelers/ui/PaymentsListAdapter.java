package com.example.hostelers.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hostelers.R;
import com.example.hostelers.backend.PaymentListItemResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentsListAdapter extends ArrayAdapter<PaymentListItemResult> {

    public PaymentsListAdapter(Context context, ArrayList<PaymentListItemResult> list){
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;
        if(resultView == null){
            resultView = LayoutInflater.from(getContext()).inflate(R.layout.previous_payment_list_item, parent, false);
        }
        PaymentListItemResult item = getItem(position);
        TextView payment_date = resultView.findViewById(R.id.payment_date), payment_amt = resultView.findViewById(R.id.payment_amount_tv), payment_stat = resultView.findViewById(R.id.payment_status), payment_msg = resultView.findViewById(R.id.pay_msg);
        ImageButton notifyBtn = resultView.findViewById(R.id.payment_notify);
        final String status = item.getTr_status(), date_str = item.getTr_date(), amt_str = item.getTr_amount(), msg = item.getTr_message();
        payment_date.setText(date_str);
        payment_amt.setText(amt_str);
        payment_msg.setText(msg);

        if(status.equalsIgnoreCase("success")){
            payment_stat.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
            notifyBtn.setVisibility(View.VISIBLE);
        }
        else{
            payment_stat.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));
            notifyBtn.setVisibility(View.GONE);
        }
        payment_stat.setText(status);
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences("BoarderUser", Context.MODE_PRIVATE);
                String bId = preferences.getString("BoarderId", null), hName = preferences.getString("HostelName", null), hLoc = preferences.getString("HostelLocation", null);
                String text = "On date "+date_str+" I have paid amount "+amt_str+" to the hostel "+(msg.isEmpty() ? "to pay pending hostel dues.":"for the purpose of "+msg+".")+" Please check your account.";
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000").addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> details = new HashMap<>();
                details.put("hostelName", hName);
                details.put("hostelLocation", hLoc);
                details.put("boarderId", bId);
                details.put("issueType", "Personal");
                details.put("typeCategory", "Payment");
                details.put("issueDescription", text);
                Call<Void> call = retrofitInterface.createBoarderIssue(details);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        int response_code = response.code();
                        if(response_code == 200){
                            Toast.makeText(getContext(), "Message Sent!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("Connection error!");
                    }
                });
            }
        });
        return resultView;
    }
}
