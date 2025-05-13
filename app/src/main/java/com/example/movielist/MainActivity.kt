/*
Sage Johnson
Mobile App Dev - CS 3013
Dr. Elliott Evans
12 May 2025
 */

package com.example.movielist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Scanner
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    var myPlace: String? = null
    val movieList: MutableList<Movie?> = ArrayList<Movie?>()
    val movieAdapter = MovieAdapter(movieList as MutableList<Movie>)

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            activityResult ->
            val data = activityResult.data
            val title = data?.getStringExtra("title") ?: ""
            val year = data?.getStringExtra("year") ?: ""
            val genre = data?.getStringExtra("genre") ?: ""
            val rating = data?.getStringExtra("rating") ?: ""
            // Log.d("nullmovie?", "$title,$year,$genre,$rating")
            movieList.add(Movie(title, year, genre, rating))
            movieAdapter.notifyDataSetChanged()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val myDir = this.getFilesDir()
        val myDirName = myDir.getAbsolutePath()
        myPlace = myDirName

        // test movie below
        // movieList.add(Movie("John Wick", "2016", "Action", "10"))

        readFile()


        val recyclerView = findViewById<RecyclerView?>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        recyclerView.setAdapter(movieAdapter)

        // allowing swipe-right delete
        val itemTouchHelper = ItemTouchHelper(movieAdapter.swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun writeFile(v: View) {
        try {
            val f = File(myPlace + "/MOVIELIST.csv")
            if (f.exists()) {
                Log.d("MOVIELIST.csv", "EXISTS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            } else {
                Log.d("MOVIELIST.csv", "DOES NOT EXIST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            }
            val fw = FileWriter(f, false)
            val count = movieList.size
            Log.d("MOVIELIST", "Count = $count")
            // print the list
            for (i in 0..<count) {
                val s = movieList[i]?.toString()
                fw.write(s + "\n")
            }
            fw.flush()
            fw.close()
        } catch (iox: IOException) {
            Log.d("MOVIELIST", "exception!")
            Log.d("MOVIELIST", "exception msg: $iox")
        }
    }
    // FUNCTION readFile - reads the file MOVIELIST.csv - populates full movie list
    fun readFile() {
        Log.d("MOVIELIST", "readFile() entered")
        try {
            val f = File(myPlace + "/MOVIELIST.csv")
            f.createNewFile()
            val myReader = Scanner(f)
            while (myReader.hasNextLine()) {
                val data = myReader.nextLine()
                Log.d("MOVIELIST", "Line of input: " + data)
                val parts = data.split(",")
                movieList.add(Movie(parts[0], parts[1], parts[2], parts[3]))
            }
            myReader.close()
        } catch (e: IOException) {
            Log.d("READ", "exception occurred: $e")
            println("An error occurred.")
            e.printStackTrace()
        }
    }

    fun startSecond(v : View) {
        startForResult.launch(Intent(this, AddMovieActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ratingSort -> {
                movieList?.sortByDescending{ it?.rating }
                movieAdapter.notifyDataSetChanged()
            }
            R.id.yearSort -> {
                movieList?.sortByDescending{ it?.year }
                movieAdapter.notifyDataSetChanged()
            }
            R.id.genreSort -> {
                movieList?.sortBy{ it?.genre }
                movieAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}