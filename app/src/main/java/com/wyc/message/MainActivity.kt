package com.wyc.message

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wyc.message.msg.MessageActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerAnim.setOnClickListener {
            RecyclerScrollActivity.start(this)
        }

        nestedRecycler.setOnClickListener {
            NestedRecyclerActivity.start(this)
        }

        message.setOnClickListener {
            MessageActivity.start(this)
        }

    }
}
