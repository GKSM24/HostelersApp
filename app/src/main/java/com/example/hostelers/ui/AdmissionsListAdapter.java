package com.example.hostelers.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hostelers.R;
import com.example.hostelers.backend.AdmissionsResult;
import com.example.hostelers.backend.RetrofitInterface;


import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdmissionsListAdapter extends ArrayAdapter <AdmissionsResult>{
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";

    public AdmissionsListAdapter(Context context, ArrayList<AdmissionsResult> list){
        super(context, 0, list);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;
        if(resultView == null){
            resultView = LayoutInflater.from(getContext()).inflate(R.layout.admission_list_item_layout, parent, false);
        }

        AdmissionsResult item = getItem(position);

        TextView idTextView = resultView.findViewById(R.id.boarder_id_value), nameTextView = resultView.findViewById(R.id.boarder_name_value);
        final String id = item.getBoarderId(), name = item.getBoarderName(), job = item.getBoarderJob(), roomNumber = item.getRoomNumber(), fatherName = item.getFatherName();
        final String joiningDate = item.getJoiningDate(), mobileNum = item.getBoarderMobile(), email = item.getBoarderEmail();
        final Bitmap idProofImage = decodeString(item.getIdProof()), photoImage = decodeString(item.getPhoto());
        idTextView.setText(id);
        nameTextView.setText(name);

        Button details_btn = resultView.findViewById(R.id.show_details_btn);
        details_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                View detailsView = LayoutInflater.from(getContext()).inflate(R.layout.admission_details_layout, null);
                TextView b_id = detailsView.findViewById(R.id.admission_details_boarder_id_value), b_name = detailsView.findViewById(R.id.admission_details_boarder_name_value), b_job = detailsView.findViewById(R.id.admission_details_boarder_job_value), b_father = detailsView.findViewById(R.id.admission_details_boarder_father_name_value);
                TextView b_joining_date = detailsView.findViewById(R.id.admission_details_boarder_join_date_value), b_mobile_number = detailsView.findViewById(R.id.admission_details_boarder_mobile_number_value), b_email = detailsView.findViewById(R.id.admission_details_boarder_email_value);
                ImageView idProofView = detailsView.findViewById(R.id.admission_details_id_proof), photoView = detailsView.findViewById(R.id.admission_details_photo);
                final TextView b_room = detailsView.findViewById(R.id.tv_admission_details_boarder_room_value);
                final EditText et_b_room = detailsView.findViewById(R.id.et_admission_details_boarder_room_value);
                idProofView.setImageBitmap(idProofImage); photoView.setImageBitmap(photoImage);
                b_id.setText(id); b_name.setText(name); b_father.setText(fatherName); b_job.setText(job); b_joining_date.setText(joiningDate);
                b_mobile_number.setText(mobileNum); b_email.setText(email);
                if(roomNumber == null){
                    b_room.setVisibility(View.GONE);
                    b_room.setEnabled(false);
                    et_b_room.setVisibility(View.VISIBLE);
                    et_b_room.setEnabled(true);
                }
                else{
                    et_b_room.setVisibility(View.GONE);
                    et_b_room.setEnabled(false);
                    b_room.setText(roomNumber);
                }
                openDialogBox(detailsView);
            }
        });

        return resultView;
    }

    private Bitmap decodeString(String encodedString){
        byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void openDialogBox(View dialogView){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("WardenUser", Context.MODE_PRIVATE);
        String bId = ((TextView)dialogView.findViewById(R.id.admission_details_boarder_id_value)).getText().toString();
        final String bEmail = ((TextView)dialogView.findViewById(R.id.admission_details_boarder_email_value)).getText().toString();
        final String hName = preferences.getString("HostelName", null), hLocation = preferences.getString("HostelLocation", null), wId = preferences.getString("WardenId", null);
        final EditText roomNumber = dialogView.findViewById(R.id.et_admission_details_boarder_room_value);
        final HashMap<String,String> details = new HashMap<>();
        details.put("hostelName", hName);
        details.put("hostelLocation", hLocation);
        details.put("wardenId", wId);
        details.put("boarderId", bId);
        dialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //empty
            }
        });
        if(roomNumber.isEnabled()) {
            dialogBuilder.setPositiveButton("Allocate Room", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String roomNumberText = roomNumber.getText().toString();
                    details.put("roomNumber", roomNumberText);
                    Call<Void> call = retrofitInterface.updateRoomStatusForBoarder(details);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.code() == 200){
                                Toast.makeText(getContext(), "Room allocated", Toast.LENGTH_LONG).show();
                                sendMail(hName, roomNumberText, bEmail);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getContext(), "Connect failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        dialogBuilder.setView(dialogView).show();

    }

    private void sendMail(String hostelName, String roomNumber, String email){
        String text = getContext().getResources().getString(R.string.room_mail_text, hostelName, roomNumber, hostelName);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hostel Room Number");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        getContext().startActivity(intent);
    }


}
