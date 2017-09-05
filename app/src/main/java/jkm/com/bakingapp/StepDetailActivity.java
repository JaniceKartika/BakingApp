package jkm.com.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

import jkm.com.bakingapp.model.StepModel;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnPageSelected {

    private ArrayList<StepModel> stepModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        if (!getResources().getBoolean(R.bool.isTab)) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
            }
        }
        setContentView(R.layout.activity_step_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.steps_key))) {
            stepModels = intent.getParcelableArrayListExtra(getString(R.string.steps_key));
            int position = intent.getIntExtra(getString(R.string.step_position_key), 0);

            setTitle(stepModels.get(position).getShortDescription());

            if (savedInstanceState == null) {
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(this, stepModels, position);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }
        } else {
            Toast.makeText(this, getString(R.string.failed_show_step_detail), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setOnPageSelected(int position) {
        setTitle(stepModels.get(position).getShortDescription());
    }

    private void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
