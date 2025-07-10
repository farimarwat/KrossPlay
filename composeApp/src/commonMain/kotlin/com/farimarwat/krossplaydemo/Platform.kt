package com.farimarwat.krossplaydemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform