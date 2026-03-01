group = "dev.jkcarino"

patches {
    about {
        name = "Adobo"
        description = "Patches for Morphe to disable ads, trackers and analytics, " +
            "remove Reddit ads everywhere, always open Gboard in incognito mode, and much more!"
        source = "git@github.com:jkennethcarino/adobo.git"
        author = "Ken"
        contact = "6307355+jkennethcarino@users.noreply.github.com"
        website = "https://github.com/jkennethcarino/adobo"
        license = "GNU General Public License v3.0"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

dependencies {
    // Used by JsonGenerator.
    implementation(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("dev.jkcarino.adobo.util.PatchListGeneratorKt")
    }
    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
