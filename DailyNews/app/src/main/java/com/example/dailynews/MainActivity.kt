package com.example.dailynews

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestTask
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.dailynews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), onitemcclick {

    lateinit var binding: ActivityMainBinding
    lateinit var madapter:newsadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager=LinearLayoutManager(this)
        getdata()

        madapter = newsadapter(this)
        binding.recyclerview.adapter=madapter
    }
    private fun getdata() {
        var url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=37baee769e8843f99c6d9629a283dae0"
        var jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                {
                    var jsonarraynews = it.getJSONArray("articles")
                    var newsarray = ArrayList<News>()
                    for (i in 0 until jsonarraynews.length()) {
                        var jsonnewsobject = jsonarraynews.getJSONObject(i)
                        var news = News(
                                jsonnewsobject.getString("title"),
                                jsonnewsobject.getString("author"),
                                jsonnewsobject.getString("url"),
                                jsonnewsobject.getString("urlToImage")
                        )
                        newsarray.add(news)
                    }
                    madapter.updatenews(newsarray)
                },
                {
                    Log.i("nooooooooooooo", "rereee")
                }
        )

        {override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["User-Agent"] = "Mozilla/5.0"
            return headers
        }
        }



        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }



        override fun onitemclicked(item: News) {
            var builder=CustomTabsIntent.Builder()
            var intent=builder.build()
            intent.launchUrl(this,Uri.parse(item.url))
    }
}