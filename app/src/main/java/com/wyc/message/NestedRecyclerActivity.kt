package com.wyc.message

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyc.message.adapter.MainAdapter
import kotlinx.android.synthetic.main.activity_nested_recycler.*

class NestedRecyclerActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, NestedRecyclerActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_recycler)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)


        val adapter = MainAdapter(this)
        recyclerView.adapter = adapter
        adapter.setOnFunctionItemClickListener { view, text ->
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
        adapter.setOnServiceItemClickListener { view, position ->
            Toast.makeText(this, "服务 position $position", Toast.LENGTH_LONG).show()
        }
        adapter.setOnActivityItemClickListener { view, position ->
            Toast.makeText(this, "活动 position $position", Toast.LENGTH_LONG).show()
        }
    }
}
