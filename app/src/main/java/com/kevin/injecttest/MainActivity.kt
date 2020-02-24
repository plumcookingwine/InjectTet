package com.kevin.injecttest

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kevin.injecttest.annotation.InjectLayout
import com.kevin.injecttest.annotation.InjectView
import com.kevin.injecttest.annotation.event.OnClick
import com.kevin.injecttest.annotation.event.OnLongClick
import com.kevin.java.Test

@InjectLayout(R.layout.activity_main)
class MainActivity : BaseActivity() {

    @InjectView(R.id.mTvView)
    var mTvView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        mTvView?.text = "inject 哈哈哈"
    }


    @OnClick(R.id.mTvView)
    fun clickBtn(view: View) {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
    }


    @OnLongClick(R.id.mTvView)
    fun longClickBtn(view: View): Boolean {
        Toast.makeText(this, "long click", Toast.LENGTH_SHORT).show()

        return true
    }
}
