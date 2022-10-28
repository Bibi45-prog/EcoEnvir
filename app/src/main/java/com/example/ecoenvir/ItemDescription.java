package com.example.ecoenvir;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;


public class ItemDescription extends Fragment {
    String recycleName = "";
    private String currentPhotoPath;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    public static void setOnClickListener(View.OnClickListener onClickListener) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recycleName = getArguments().getString("recycleName");

        return inflater.inflate(R.layout.fragment_item_description, container, false);

    }


    //variables for image selection
    public static final int CAMERA_ACTION_CODE = 1;
    Uri uri = null;
    ImageView imageView;
    RelativeLayout imageViewCont;
    ActivityResultLauncher<Intent> activityResultLauncher;

    //variables for datepicker dialog
    TextView tvDate;
    EditText etDate;
    DatePickerDialog.OnDateSetListener setListener;

    //variables fot timepicker
    TextView tvTimer;
    int t1Hour, t1Minute;

    //variables for itemDescriptionForm
    EditText txtQuantity, txtItemDescription, txtDate, txtTime, txtPhone, txtAddress;
    Button next;
    ProgressDialog progressDialog;
    FirebaseUser currentUser;
    StorageReference storageReference;
    String TAG = "theS";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ImageView btnBack = view.findViewById(R.id.btnBack3);

        txtItemDescription = view.findViewById(R.id.et_ItemDescription);
        txtQuantity = view.findViewById(R.id.et_Quantity);
        txtDate = view.findViewById(R.id.et_date);
        txtTime = view.findViewById(R.id.et_time);
        txtPhone = view.findViewById(R.id.et_Phone);
        txtAddress = view.findViewById(R.id.et_Address);
        next = view.findViewById(R.id.btnNextThree);

        //back button function
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Direct to Recycle page
                Fragment next_fragment = new Recycle();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
            }
        });

        //next button function
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_description = txtItemDescription.getText().toString();
                String txt_quantity = txtQuantity.getText().toString();
                String txt_date = txtDate.getText().toString();
                String txt_time = txtTime.getText().toString();
                String txt_Phone = txtPhone.getText().toString();
                String txt_address = txtAddress.getText().toString();



                if (txt_description.equals("") || txt_quantity.equals("") || txt_date.equals("")
                        || txt_time.equals("") || txt_Phone.equals("") || txt_address.equals("")) {
                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri == null){
                    Toast.makeText(getActivity(), "Please capture a image.", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(getContext(),"","Please wait",false,true);
                storageReference =  FirebaseStorage.getInstance().getReference();
                StorageReference fileRef = storageReference.child(uri.getLastPathSegment());
                fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                Log.d(TAG, "onSuccess: "+uri);
                                SelectDeliveryMethod1 next_fragment = new SelectDeliveryMethod1();
                                next_fragment.recycleName = recycleName;
                                next_fragment.currentRequest = new RequestHelperClass(
                                        txt_description, txt_quantity, txt_date, txt_time, txt_address, txt_Phone,
                                        uri.toString(), null
                                );
                                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        SelectDeliveryMethod1 next_fragment = new SelectDeliveryMethod1();
                        next_fragment.recycleName = recycleName;
                        next_fragment.currentRequest = new RequestHelperClass(
                                txt_description, txt_quantity, txt_date, txt_time, txt_Phone, txt_address,
                                "", null
                        );
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
                    }
                });
            }
        });

        //Calendar
        etDate = view.findViewById(R.id.et_date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                tvDate.setText(date);
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        etDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        //Time picker dialog
        tvTimer = view.findViewById(R.id.et_time);
        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Initialize hour and minute
                        t1Hour = hourOfDay;
                        t1Minute = minute;
                        //initialize calendar
                        Calendar calendar = Calendar.getInstance();
                        //store hour and minute
                        String time = t1Hour + ":" + t1Minute;
                        //Initialize 24 hours time format
                        calendar.set(0, 0, 0, t1Hour, t1Minute);
                        //set selected time on text view
                        tvTimer.setText(DateFormat.format("hh:mm aa", calendar));
                    }


                }, 12, 0, false
                );

                //Displayed previous selected time
                timePickerDialog.updateTime(t1Hour, t1Minute);
                //Show dialog
                timePickerDialog.show();
            }
        });

        imageViewCont = view.findViewById(R.id.iv_uploadImageBox_cont);
        imageView = view.findViewById(R.id.iv_uploadImageBox);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    uri = data.getData();
                    Log.d(TAG, "onActivityResult: "+uri);

                    imageView.setImageURI(uri);
                }

            }

        });

        imageView.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
            galleryIntent.setType("image/*");

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
            chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");

            Intent [] intentArray = {cameraIntent};
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            activityResultLauncher.launch(galleryIntent);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                activityResultLauncher.launch(intent);
            } else{
                Toast.makeText(getActivity(),"There is no app to support this action", Toast.LENGTH_SHORT).show();
            }
        });

    }
}