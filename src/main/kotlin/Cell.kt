package com.company.Cell

abstract class Cell(var coordinate: Int,val name:String) {

    override fun toString(): String {
        return "|$coordinate $name|"
    }

    abstract fun Display()
}