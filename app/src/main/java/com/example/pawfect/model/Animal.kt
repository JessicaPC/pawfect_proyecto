package com.example.pawfect.model

class Animal(
    //var id: String,
    val name: String,
    //val type : AnimalType,
    //var age: Int,
    var image: Int
    // var gallery: List<String>

) {

    enum class AnimalType{
        DOG,
        CAT,
        OTHER
    }

}
