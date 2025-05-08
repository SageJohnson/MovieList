package com.example.movielist

class Movie(val title: String?, val year: String?, val genre: String?, val rating: String?) {

    override fun toString(): String {
        return "$title,$year,$genre,$rating"
    }
}