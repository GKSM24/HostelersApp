package com.example.hostelers.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoarderPaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoarderPaymentsFragment extends Fragment {
    private final String BASE_URL = "http://10.0.2.2:3000";

    public BoarderPaymentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BoarderPaymentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoarderPaymentsFragment newInstance() {
        return new BoarderPaymentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boarder_payments, container, false);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        final RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        final EditText amount_to_pay = view.findViewById(R.id.amount_pay_et), amount_msg = view.findViewById(R.id.payment_msg_et);
        final ListView previousPaymentsList = view.findViewById(R.id.previous_payments_list);
        final SharedPreferences preferences = getActivity().getSharedPreferences("BoarderUser", Context.MODE_PRIVATE);
        final String bId = preferences.getString("BoarderId", null), hName = preferences.getString("HostelName", null), hLoc = preferences.getString("HostelLocation", null);
        Button button = view.findViewById(R.id.payment_btn);
        BoarderPreviousPaymentsViewModel paymentsViewModel = new BoarderPreviousPaymentsViewModel();
        paymentsViewModel.setData(hName, hLoc, bId);
        LiveData<ArrayList<PaymentListItemResult>> paymentHistory = paymentsViewModel.getData();
        paymentHistory.observe(getViewLifecycleOwner(), new Observer<ArrayList<PaymentListItemResult>>() {
            @Override
            public void onChanged(ArrayList<PaymentListItemResult> paymentListItemResults) {
                PaymentsListAdapter adapter = new PaymentsListAdapter(getContext(), paymentListItemResults);
                previousPaymentsList.setAdapter(adapter);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amount_to_pay.getText().toString();
                if(Integer.parseInt(amount) > 10000)
                    amount_to_pay.setError("Amount can't be more than 10,000");
                else{
                    HashMap<String, String> payment_data = new HashMap<>();
                    payment_data.put("boarderId", bId);
                    payment_data.put("hostelName", hName);
                    payment_data.put("hostelLocation", hLoc);
                    payment_data.put("amount", amount);
                    payment_data.put("amt_message", amount_msg.getText().toString());
                    Call<Void> call = retrofitInterface.executeMakePayment(payment_data);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int response_code = response.code();
                            if(response_code == 12)
                                Toast.makeText(getContext(), "Transaction Failure!", Toast.LENGTH_LONG).show();
                            else if(response_code == 200)
                                Toast.makeText(getContext(), "Transaction Successful", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.println("Server error! Can't connect to Server");
                        }
                    });
                }
            }
        });
    }
}
