package jkm.com.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.R;

public class RecipeWidgetConfigurationActivity extends AppCompatActivity {

    @BindView(R.id.tv_recipe_widget_configuration)
    TextView recipeWidgetTextView;

    private int mAppWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_recipe_widget_configuration);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        recipeWidgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWidget(RecipeWidgetConfigurationActivity.this);
            }
        });
    }

    private void createWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
