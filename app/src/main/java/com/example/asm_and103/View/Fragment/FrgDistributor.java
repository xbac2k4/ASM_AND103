package com.example.asm_and103.View.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asm_and103.Adapter.AdapterDistributor;
import com.example.asm_and103.Model.Distributor;
import com.example.asm_and103.Model.Response;
import com.example.asm_and103.R;
import com.example.asm_and103.View.DistributorActivity;
import com.example.asm_and103.databinding.ActivityDistributorBinding;
import com.example.asm_and103.databinding.DialogAddBinding;
import com.example.asm_and103.databinding.FragmentFrgDistributorBinding;
import com.example.asm_and103.service.HttpRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FrgDistributor extends Fragment implements AdapterDistributor.DistributorClick {

//    private ActivityDistributorBinding binding;
    FragmentFrgDistributorBinding binding;
    private HttpRequest httpRequest;
    private ArrayList<Distributor> list = new ArrayList<>();
    private AdapterDistributor adapter;
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;

    public FrgDistributor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frg_distributor, container, false);
        //        toolbar = findViewById(R.id.tool_bar);
//        binding = ActivityDistributorBinding.inflate(getLayoutInflater());
        binding = FragmentFrgDistributorBinding.inflate(getLayoutInflater());
        fetchAPI();
        userListener();
        return view;
    }
    private void fetchAPI() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        httpRequest = new HttpRequest();
        httpRequest.callAPI()
                .getDistributor()
                .enqueue(getDistributorAPI);

    }

    private void userListener() {
        binding.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String key = binding.edSearch.getText().toString().trim();
                    httpRequest.callAPI()
                            .searchDistributor(key)
                            .enqueue(getDistributorAPI);
                    Log.d(TAG, "onEditorAction: " + key);
                    return true;
                }
                return false;
            }
        });

    }
    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(getContext()));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();
        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding1.etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "you must enter name", Toast.LENGTH_SHORT).show();
                }   else {
                    Distributor distributor = new Distributor();
                    distributor.setName(name);
                    httpRequest.callAPI()
                            .addDistributor(distributor)
                            .enqueue(responseDistributorAPI);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void getData() {
        adapter = new AdapterDistributor(list, getContext(), (AdapterDistributor.DistributorClick) getContext());
        binding.rcvDistributor.setAdapter(adapter);
        progressDialog.dismiss();
    }


    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = response.body().getData();
                    getData();
                    Log.d(TAG, "onResponse: "+ list.size());
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.e(TAG, "onFailure: "+ t.getMessage() );
        }
    };


    Callback<Response<Distributor>> responseDistributorAPI  = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    httpRequest.callAPI()
                            .getDistributor()
                            .enqueue(getDistributorAPI);
                    Toast.makeText(getActivity(), response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {
            Log.e(TAG, "onFailure: "+t.getMessage() );
        }
    };

    @Override
    public void delete(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("yes", (dialog, which) -> {
            httpRequest.callAPI().deleteDistributor(distributor.get_id()).enqueue(new Callback<Response<Distributor>>() {
                @Override
                public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
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

    }
}