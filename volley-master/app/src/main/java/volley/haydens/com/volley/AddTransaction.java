package com.agri;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.agri.Config.ApiEndpoint;
import com.agri.Models.APIResponse;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;


import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddTransaction extends AppCompatActivity {



    EditText title,qty,rate,description;
    Spinner type;
    String tType="PURCHASE";


    Button btnAddCover;

    ImageView imageView;
    String imagePath,imageFileName;
    //Untuk mengambil gambar dari device kita tidak akan menulis ulang.
    //Pakai library yang sudah ada
    //Lihat: https://github.com/coomar2841/android-multipicker-library
    ImagePicker imagePicker; //instance image picker
    CameraImagePicker cameraImagePicker; //instance camera image picker

    String TAG = getClass().getName().toString(); //untuk keperluan log, ambil nama class
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg"); //mime type gambar jpeg



    //Define okhttp dengan network interceptor agar mudah debug dengan Chrome
    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    Gson gson = new Gson(); //gson untuk handling json

    //Image picker callback, untuk menghandle pengambilan gambar dari gallery/camera
    ImagePickerCallback callback = new ImagePickerCallback(){
        @Override
        public void onImagesChosen(List<ChosenImage> images) {
            // get image path
            if(images.size() > 0){
                imagePath = images.get(0).getOriginalPath();
                imageFileName = images.get(0).getDisplayName();
                Picasso.get().load(new File(imagePath)).into(imageView);
            }
        }

        @Override
        public void onError(String message) {
            // Do error handling
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_transaction);
        title=(EditText)findViewById(R.id.title);
        qty=(EditText)findViewById(R.id.quantity);
        rate=(EditText)findViewById(R.id.rate);
        description=(EditText)findViewById(R.id.description);
        btnAddCover=(Button)findViewById(R.id.billsnap);
        imageView=(ImageView)findViewById(R.id.snapImage);

        addListenerOnSpinnerItemSelection();

        //inisiaisasi ImagePicker dan CameraImagePicker
        imagePicker = new ImagePicker(this);
        cameraImagePicker = new CameraImagePicker(this);

        //set callback handler
        imagePicker.setImagePickerCallback(callback);
        cameraImagePicker.setImagePickerCallback(callback);

        //beri action pada tombol thumbnail
        btnAddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageChoice();
            }
        });


        try{
            post("","");

        }catch (Exception e){}


    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    //OkHttpClient client = new OkHttpClient();

    void post(String url, String json) throws IOException {

        System.out.println("------"+url);
        System.out.println("------"+json);
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, json))
                .build();






        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                AddTransaction.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(AddTransaction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                System.out.println("------"+response.toString());
                if (response.isSuccessful()) {
                    try {
                        //Finish activity
                        AddTransaction.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                APIResponse res =  gson.fromJson(response.body().charStream(), APIResponse.class);
                                //Jika response success, finish activity
//                                if(StringUtils.equals(res.getStatus(), "success")){
//                                    Toast.makeText(AddBook.this, "Book saved!", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }else{
//                                    //Tampilkan error jika ada
//                                    Toast.makeText(AddBook.this, "Error: "+res.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
                            }
                        });
                    } catch (JsonSyntaxException e) {
                        Log.e("MainActivity", "JSON Errors:"+e.getMessage());
                    } finally {
                        response.body().close();
                    }

                } else {
                    AddTransaction.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddTransaction.this, "Server error"+response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
        type = (Spinner) findViewById(R.id.type);
        type.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void addTrans(View view){

        System.out.println("--"+title.getText()+"--"+qty.getText()+"--"+rate.getText()+"--"+description.getText()+"--"+tType+"---"+imageFileName);


       // saveBook();
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("title", title.getText().toString());
            jsonParam.put("rate", 5);
            jsonParam.put("qty", 2);
            jsonParam.put("categoryId", 1);
            jsonParam.put("description", description.getText().toString());
            jsonParam.put("tType", "PURCHASE");
            jsonParam.put("author", 1);
            jsonParam.put("attachement", "lerktgmldfgj");
            post(ApiEndpoint.BASE,jsonParam.toString());
        }catch (Exception e){
            e.printStackTrace();
        }


    }



    private void saveBook(){


        //Sesuaikan parameter input. Jika edit mode, gambar tidak harus diisi
        //Sesuaikan juga URL dari API yang digunakan
        RequestBody requestBody = null;
        String URL = ApiEndpoint.BASE;

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("title", title.getText().toString());
            jsonParam.put("rate", 5);
            jsonParam.put("qty", 2);
            jsonParam.put("categoryId", 1);
            jsonParam.put("description", description.getText().toString());
            jsonParam.put("tType", "PURCHASE");
            jsonParam.put("author", 1);
            jsonParam.put("attachement", "lerktgmldfgj");
        }catch (Exception e){
            e.printStackTrace();
        }

            //Buat parameter input form
//            requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("title", title.getText().toString())
//                    .addFormDataPart("rate", rate.getText().toString())
//                    .addFormDataPart("qty", qty.getText().toString())
//                    .addFormDataPart("categoryId", "1")
//                    .addFormDataPart("description", description.getText().toString())
//                    .addFormDataPart("tType", "PURCHASE")
//                    .addFormDataPart("attachement", imageFileName,
//                            RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath)))
//                    .build();
            // URL = ApiEndpoint.ADD_BOOK;



        Request request = new Request.Builder()
                .url(URL) //Ingat sesuaikan dengan URL
                .post(requestBody)
                .build();

        //Handle response dari request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                AddTransaction.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(AddTransaction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        //Finish activity
                        AddTransaction.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                APIResponse res =  gson.fromJson(response.body().charStream(), APIResponse.class);
                                //Jika response success, finish activity
                                if(StringUtils.equals(res.getTitle(), "success---")){
                                    Toast.makeText(AddTransaction.this, "Trans saved--!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    //Tampilkan error jika ada
                                    Toast.makeText(AddTransaction.this, "Error: "+res.getTitle(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JsonSyntaxException e) {
                        Log.e("MainActivity", "JSON Errors:"+e.getMessage());
                    } finally {
                        response.body().close();
                    }

                } else {
                    AddTransaction.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddTransaction.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
             tType=parent.getItemAtPosition(pos).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    /**
     * Method untuk mengambil gambar ketika tombol Thumbnail di click
     */
    private void pickImageChoice(){
        //Pertama, minta permission untuk mengakses camera dan storage (untuk Android M ke atas)
        //Biar gampang, kita pakai library namanya Dexter.
        //https://github.com/Karumi/Dexter

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //Jika permission diijinkan user, buat dialog pilihan
                        //untuk memilih gambar diambil dari gallery atau camera

                        // setup the alert builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTransaction.this);
                        builder.setTitle("Pick image from?");

                        // add a list
                        String[] menus = {"Gallery", "Camera"};
                        builder.setItems(menus, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: // dari gallery
                                        imagePicker.pickImage();
                                        break;
                                    case 1: // dari camera
                                        imagePath = cameraImagePicker.pickImage();
                                        break;
                                }
                            }
                        });
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    /**
     * onActivityResult untuk menghandle data yang diambil dari camera atau gallery
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(AddTransaction.this);
                    imagePicker.setImagePickerCallback(callback);
                }
                imagePicker.submit(data);
            }
            if(requestCode == Picker.PICK_IMAGE_CAMERA) {
                if(cameraImagePicker == null) {
                    cameraImagePicker = new CameraImagePicker(AddTransaction.this);
                    cameraImagePicker.reinitialize(imagePath);
                    // OR in one statement
                    // imagePicker = new CameraImagePicker(Activity.this, outputPath);
                    cameraImagePicker.setImagePickerCallback(callback);
                }
                cameraImagePicker.submit(data);
            }
        }
    }

}
