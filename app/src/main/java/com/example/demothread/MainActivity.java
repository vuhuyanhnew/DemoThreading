package com.example.demothread;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.demothread.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView;
    private Button startButton;
    private ProgressBar progressBar;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);
        startButton = findViewById(R.id.startButton);
        progressBar = findViewById(R.id.progressBar);

        executorService = Executors.newFixedThreadPool(3);
        mainHandler = new Handler(Looper.getMainLooper());

        startButton.setOnClickListener(v -> startTasks());
    }

    private void startTasks() {
        startButton.setEnabled(false);
        progressBar.setProgress(0);
        statusTextView.setText("Đang bắt đầu các tác vụ...");

        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            executorService.execute(() -> performTask(taskId));
        }
    }

    private void performTask(int taskId) {
        for (int progress = 0; progress <= 100; progress += 10) {
            final int currentProgress = progress;
            try {
                Thread.sleep(500);
                mainHandler.post(() -> updateProgress(taskId, currentProgress));
            } catch (InterruptedException e) {
            }
        }

        mainHandler.post(() -> {
            statusTextView.append("\nTác vụ " + taskId + " hoàn thành!");
            if (taskId == 3) {
                startButton.setEnabled(true);
                statusTextView.append("\nTất cả tác vụ đã hoàn thành!");
            }
        });
    }

    private void updateProgress(int taskId, int progress) {
        statusTextView.setText("Tác vụ " + taskId + ": " + progress + "%");
        progressBar.setProgress(progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
    }
}