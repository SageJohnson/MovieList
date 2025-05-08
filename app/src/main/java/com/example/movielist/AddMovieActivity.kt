package com.example.movielist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddMovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_movie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun backToFirst(v : View) {
        var textTitle = findViewById<TextInputEditText>(R.id.title)
        var textYear = findViewById<TextInputEditText>(R.id.year)
        var textGenre = findViewById<TextInputEditText>(R.id.genre)
        var textRating = findViewById<TextInputEditText>(R.id.rating)

        // get the string versions of movie characteristics
        var title = textTitle.getText().toString()
        var year = textYear.getText().toString()
        var genre = textGenre.getText().toString()
        var rating = textRating.getText().toString()

        setMovieInfo(title, year, genre, rating)
    }
    private fun setMovieInfo(title: String, year: String, genre: String, rating: String) {
        var movieInfoIntent = Intent()
        movieInfoIntent.putExtra("title", title)
        movieInfoIntent.putExtra("year", year)
        movieInfoIntent.putExtra("genre", genre)
        movieInfoIntent.putExtra("rating", rating)
        setResult(Activity.RESULT_OK, movieInfoIntent)
        finish()
    }
}