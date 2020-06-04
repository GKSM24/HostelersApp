package com.example.hostelers.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelers.R;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IdProofsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IdProofsFragment extends Fragment {

    private int IMG_PROOF_REQ_CODE = 1, IMG_PHOTO_REQ_CODE = 2;
    private PhotoViewModel viewModelPhoto;
    private ProofViewModel viewModelProof;


    public IdProofsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IdProofsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IdProofsFragment newInstance() {
        return new IdProofsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModelPhoto = new ViewModelProvider(this).get(PhotoViewModel.class);
        viewModelProof = new ViewModelProvider(this).get(ProofViewModel.class);
        return inflater.inflate(R.layout.fragment_id_proofs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final FragmentActivity activity = requireActivity();
        final ImageView photo_img = activity.findViewById(R.id.photo_image), proof_img = activity.findViewById(R.id.id_image);
        final TextView photo_img_text = activity.findViewById(R.id.photo_image_text), proof_img_text = activity.findViewById(R.id.id_image_text);
        final Spinner id_category_spinner = activity.findViewById(R.id.id_proofs_spinner);
        Button attachIdProof = view.findViewById(R.id.id_proof_attachment), attachPhoto = view.findViewById(R.id.id_proof_photo_attachment);
        LiveData<Bitmap> photo_data = viewModelPhoto.getImage(), proof_data = viewModelProof.getImage();
        photo_data.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                photo_img.setImageBitmap(bitmap);
                photo_img_text.setText("Photo.jpeg");
                photo_img.setVisibility(View.VISIBLE);
                photo_img_text.setVisibility(View.VISIBLE);
            }
        });


        proof_data.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                proof_img.setImageBitmap(bitmap);
                if (id_category_spinner.getSelectedItemPosition() == 1) {
                    proof_img_text.setText("ID_" + "Aadhar" + ".jpeg");
                } else {
                    proof_img_text.setText("ID_" + "Pan" + ".jpeg");
                }
                proof_img.setVisibility(View.VISIBLE);
                proof_img_text.setVisibility(View.VISIBLE);
            }
        });
        attachIdProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile(v.getId());
            }
        });

        attachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile(v.getId());
            }
        });
    }

    private void chooseFile(int viewId) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            if (viewId == R.id.id_proof_photo_attachment)
                startActivityForResult(intent, IMG_PHOTO_REQ_CODE);
            else if (viewId == R.id.id_proof_attachment)
                startActivityForResult(intent, IMG_PROOF_REQ_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri path = data.getData();
            Activity activity = requireActivity();
            try {
                if (requestCode == IMG_PHOTO_REQ_CODE) {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), path);
                    int size = (photo.getHeight()*photo.getWidth()*3)/1024;
                    if (size <= 700) {
                        viewModelPhoto.setImage(photo);
                        Toast.makeText(activity, "File " + path.getLastPathSegment() + " is attached", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(activity, "Please attach the file of size less than 40kb", Toast.LENGTH_LONG).show();
                } else if (requestCode == IMG_PROOF_REQ_CODE) {
                    Bitmap proof = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), path);
                    int size = (proof.getWidth()*proof.getHeight()*3)/1024;
                    if (size <= 5000) {
                        viewModelProof.setImage(proof);
                        Toast.makeText(activity, "File " + path.getLastPathSegment() + " is attached", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(activity, "Please attach the file of size less than 150kb", Toast.LENGTH_LONG).show();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
