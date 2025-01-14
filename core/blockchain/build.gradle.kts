plugins {
    alias(libs.plugins.fair.android.library.core)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair.core.blockchain"
}

dependencies {
    implementation(libs.metamask.android.sdk)
    implementation(platform(libs.ethers.bom))
    implementation(libs.ethers.abi) {
        exclude(group = "io.kriptal.ethers", module = "ethers-providers")
    }
    implementation(libs.ethers.core)
}
