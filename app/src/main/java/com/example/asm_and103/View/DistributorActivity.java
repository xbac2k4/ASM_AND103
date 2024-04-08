package com.example.asm_and103.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_and103.Model.Fruit;
import com.example.asm_and103.databinding.DialogAddBinding;
import com.example.asm_and103.databinding.DialogAddupdateBinding;
import com.example.asm_and103.Model.Distributor;
import com.example.asm_and103.Model.Response;
import com.example.asm_and103.service.HttpRequest;
import com.example.asm_and103.Adapter.AdapterDistributor;
import com.example.asm_and103.Model.Distributor;
import com.example.asm_and103.databinding.ActivityDistributorBinding;
import com.example.asm_and103.service.HttpRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.example.asm_and103.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class DistributorActivity extends AppCompatActivity implements AdapterDistributor.DistributorClick {
    private ActivityDistributorBinding binding;
    private HttpRequest httpRequest;
    private ArrayList<Distributor> list = new ArrayList<>();
    private AdapterDistributor adapter;
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;
    private ArrayList<Distributor> listSeacrch = new ArrayList<>();

    FirebaseAuth mAuth;
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDistributorBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

//        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Distributor");
        getSupportActionBar().setSubtitle("Distributor Management");

        fetchAPI();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_them) {
            showDialogAdd();
        } else if (item.getItemId() == R.id.item_home) {
            startActivity(new Intent(DistributorActivity.this, MainActivity.class));
            Toast.makeText(DistributorActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.item_dangxuat) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finishAffinity();
            startActivity(new Intent(DistributorActivity.this, Login.class));
            Toast.makeText(DistributorActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void fetchAPI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        httpRequest = new HttpRequest();
        httpRequest.callAPI()
                .getDistributor()
                .enqueue(getDistributorAPI);

    }
    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();
        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding1.etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(DistributorActivity.this, "you must enter name", Toast.LENGTH_SHORT).show();
                }   else {
                    Distributor distributor = new Distributor();
                    distributor.setName(name);
                    httpRequest.callAPI().addDistributor(distributor).enqueue(new Callback<Response<Distributor>>() {
                        @Override
                        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    Toast.makeText(DistributorActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    fetchAPI();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

                        }
                    });
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void getData(ArrayList<Distributor> listS) {
        adapter = new AdapterDistributor(listS, this,this );
        binding.rcvDistributor.setAdapter(adapter);
        progressDialog.dismiss();
    }
    private void filterList(String text) {
        if (!text.equals("")) {
            listSeacrch.clear();
            httpRequest.callAPI().searchDistributor(text).enqueue(new Callback<Response<ArrayList<Distributor>>>() {
                @Override
                public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listSeacrch = response.body().getData();
                            getData(listSeacrch);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {

                }
            });
        } else {
            fetchAPI();
        }
    }

    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = response.body().getData();
                    getData(list);
                    Log.d(TAG, "onResponse: "+ list.size());
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.e(TAG, "onFailure: "+ t.getMessage() );
        }
    };

    private void showDialogEdit(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();

        binding1.etName.setText(distributor.getName());

        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = distributor.getName();

                if (name.isEmpty()) {
                    Toast.makeText(DistributorActivity.this, "you must enter name", Toast.LENGTH_SHORT).show();
                }   else {

                    Distributor distributor1 = new Distributor();
                    distributor1.setName(binding1.etName.getText().toString().trim());
                    httpRequest.callAPI().updateDistributor(distributor.get_id(),distributor1).enqueue(new Callback<Response<Distributor>>() {
                        @Override
                        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    Toast.makeText(DistributorActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    fetchAPI();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

                        }
                    });
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
    @Override
    public void delete(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("yes", (dialog, which) -> {
            httpRequest.callAPI().deleteDistributor(distributor.get_id()).enqueue(new Callback<Response<Distributor>>() {
                @Override
                public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Toast.makeText(DistributorActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            fetchAPI();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<Distributor>> call, Throwable t) {

                }
            });
        });
        builder.setNegativeButton("no", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();


    }

    @Override
    public void edit(Distributor distributor) {
        showDialogEdit(distributor);
    }
}

