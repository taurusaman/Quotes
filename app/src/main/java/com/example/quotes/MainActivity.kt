package com.example.quotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quotes.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getQuote()

        binding.nextBtn.setOnClickListener {
            getQuote()
        }
    }


    private fun getQuote(){
       setInProgress(true)

        GlobalScope.launch {
            try {
               val response = RetrofitInstance.quoteApi.getRandomQuote()

                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let {
                        setUi(it)
                    }
                }

            }catch (e: Exception){
              runOnUiThread {
                  Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_SHORT).show()
              }
            }
        }
    }

    private fun setUi(quote : QuoteModel){
        binding.quoteTextview.text = quote.q
        binding.authorTextviewId.text = quote.a
    }

    private fun setInProgress(inProgress: Boolean){
        if (inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.nextBtn.visibility = View.VISIBLE
        }
    }
}