package dev.jkcarino.adobo.patches.all.misc.packagename

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import dev.jkcarino.adobo.patches.shared.resource.ANDROID_NAME_ATTR
import dev.jkcarino.adobo.patches.shared.resource.MANIFEST_NODE
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.util.asElementSequence
import dev.jkcarino.adobo.util.get
import dev.jkcarino.adobo.util.set
import org.w3c.dom.NodeList

@Suppress("unused")
val changePackageNamePatch = resourcePatch(
    name = "Change package name",
    description = "Appends \".adobo\" to the package name by default. " +
        "Changing the package name of the app can lead to unexpected issues.",
    use = false,
) {
    val packageNameOption =
        stringOption(
            key = "packageName",
            default = "Default",
            values = mapOf("Default" to "Default"),
            title = "Package name",
            description = "The name of the package to rename the app to.",
            required = true,
        ) { packageName ->
            val packageNamePattern = """^[a-z]\w*(\.[a-z]\w*)+$""".toRegex()
            packageName == "Default" || packageName!!.matches(packageNamePattern)
        }

    val updatePermissions by booleanOption(
        key = "updatePermissions",
        default = true,
        title = "Update permissions",
        description = "Update compatibility receiver permissions. " +
            "Enabling this can fix installation errors, " +
            "but this can also break features in certain apps.",
    )

    val updateOtherPermissions by booleanOption(
        key = "updateOtherPermissions",
        default = true,
        title = "Update other permissions",
        description = "Update other permissions declared by the app. " +
            "Enabling this can fix installation errors, " +
            "but this can also break features in certain apps.",
    )

    val updateProviders by booleanOption(
        key = "updateProviders",
        default = true,
        title = "Update content providers",
        description = "Update content provider URI authorities. " +
            "Enabling this can fix installation errors, " +
            "but this can also break features in certain apps.",
    )

    finalize {
        androidManifest {
            val manifest = this[MANIFEST_NODE]
            val packageName = manifest["package"]
            val replacementPackageName = packageNameOption.value!!
            val newPackageName =
                if (replacementPackageName == packageNameOption.default) {
                    "${packageName}.adobo"
                } else {
                    replacementPackageName
                }

            manifest["package"] = newPackageName

            if (updatePermissions == true || updateOtherPermissions == true) {
                val receiverNotExported = "DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"

                setOf("permission", "uses-permission")
                    .asSequence()
                    .map(::getElementsByTagName)
                    .flatMap(NodeList::asElementSequence)
                    .filter { element ->
                        val nameAttrValue = element[ANDROID_NAME_ATTR]
                        val isReceiverPermission = updatePermissions == true &&
                            nameAttrValue == "$packageName.$receiverNotExported"
                        val isCustomPermission = updateOtherPermissions == true &&
                            nameAttrValue.startsWith(packageName)

                        isReceiverPermission || isCustomPermission
                    }
                    .forEach { element ->
                        element[ANDROID_NAME_ATTR] = element[ANDROID_NAME_ATTR].replace(
                            oldValue = packageName,
                            newValue = newPackageName
                        )
                    }
            }

            if (updateProviders == true) {
                val authoritiesAttr = "android:authorities"

                manifest
                    .getElementsByTagName("provider")
                    .asElementSequence()
                    .filter { it[authoritiesAttr].startsWith(packageName) }
                    .forEach { provider ->
                        provider[authoritiesAttr] = provider[authoritiesAttr].replace(
                            oldValue = packageName,
                            newValue = newPackageName
                        )
                    }
            }
        }
    }
}
