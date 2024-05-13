plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.kotlinPluginSerialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.androidx.annotation)
    api(libs.okhttp)
}