package com.example.ecocity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApplicantsListAdapter extends RecyclerView.Adapter <ApplicantsListAdapter.ActivityViewHolder> {

    private List<ApplicantsListHelper> applicants;
    private ClickListener clickListener;

    public ApplicantsListAdapter (ArrayList<ApplicantsListHelper> applicants, ApplicantsListAdapter.ClickListener clickListener){
        this.applicants = applicants;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ApplicantsListAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicants_card, parent, false);
        ActivityViewHolder viewHolder = new ActivityViewHolder(view);
        viewHolder.BTViewCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String) v.getTag();
                handleClick(url);
                downloadAndOpenPdf(url, v.getContext());
            }
        });
        viewHolder.statusSpinner = view.findViewById(R.id.statusSpinner);
        return viewHolder;
    }

    private void downloadAndOpenPdf(String url, Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading CV...");
        request.setDescription("Please wait...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_cv.pdf");
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadID = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "downloaded_cv.pdf");
                openPdf(file, context);
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void openPdf(File file, Context context) {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    private void handleClick(String url) {
        clickListener.onClick(url);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantsListAdapter.ActivityViewHolder holder, int position) {
        ApplicantsListHelper model = applicants.get(position);
        holder.username.setText(model.getUsername());
        holder.BTViewCV.setTag(model.getUrl());
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(holder.statusSpinner.getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.statusSpinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(model.getStatus());
        holder.statusSpinner.setSelection(spinnerPosition);

        holder.statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newStatus = parent.getItemAtPosition(position).toString();
                if (!newStatus.equals(model.getStatus())) {
                    DatabaseReference applicantRef = FirebaseDatabase.getInstance().getReference("Application")
                            .child(model.getUsername()).child(model.getKey()).child("status");
                    applicantRef.setValue(newStatus);
                    model.setStatus(newStatus);
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return applicants.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        AppCompatButton BTViewCV;
        Spinner statusSpinner;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.TVUsername1);
            BTViewCV = itemView.findViewById(R.id.BTViewCV);
            statusSpinner = itemView.findViewById(R.id.statusSpinner);
        }
    }

    public interface ClickListener {
        void onClick(String url);
    }

    public ApplicantsListHelper getApplicantAtPosition(int position) {
        return applicants.get(position);
    }
}
