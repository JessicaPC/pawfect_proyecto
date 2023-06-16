package com.example.pawfect.model

import java.io.Serializable

class Animal(): Serializable {
    var id: String? = null
    var userID: String? = null
    var name: String? = null
    var type : String? = null
    var age: String? = null
    var genre: String? = null
    var description: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var distance: String? = null
    var images: List<String>? = null


}
