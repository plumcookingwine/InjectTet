package com.kevin.java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.injecttest.R;
import com.kevin.java.annotation.InjectLayout;
import com.kevin.java.annotation.InjectView;
import com.kevin.java.annotation.event.OnClick;
import com.kevin.java.annotation.event.OnLongClick;

@InjectLayout(R.layout.activity_java)
public class JavaActivity extends AppCompatActivity {

    @InjectView(R.id.tv_test)
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.injectLayout(this);
        InjectUtils.injectEvent(this);
    }


    @OnClick({R.id.tv_test})
    private void clickText(View view) {
        Toast.makeText(this, "点击了view", Toast.LENGTH_SHORT).show();
    }

    @OnLongClick({R.id.tv_test})
    private boolean longClickText(View view) {
        Toast.makeText(this, "长按点击了view", Toast.LENGTH_SHORT).show();
        return true;
    }
}
