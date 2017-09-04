package jkm.com.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import jkm.com.bakingapp.model.StepModel;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.steps_key))) {
            ArrayList<StepModel> stepModels = intent.getParcelableArrayListExtra(getString(R.string.steps_key));
            int position = intent.getIntExtra(getString(R.string.step_position_key), 0);

            if (savedInstanceState == null) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStepModels(stepModels);
                stepDetailFragment.setInitialPosition(position);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }
        } else {
            Toast.makeText(this, getString(R.string.failed_show_step_detail), Toast.LENGTH_LONG).show();
        }
    }
}
