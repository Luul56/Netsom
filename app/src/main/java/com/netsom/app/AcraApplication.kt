package com.netsom.app

/**
 * Deprecated alias for CloudStreamApp for backwards compatibility with plugins.
 * Use CloudStreamApp instead.
 */
@Deprecated(
    message = "AcraApplication is deprecated, use CloudStreamApp instead",
    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp"),
    level = DeprecationLevel.WARNING
)
class AcraApplication {
	companion object {

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.context"),
		    level = DeprecationLevel.WARNING
		)
		val context get() = CloudStreamApp.context

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.removeKeys(folder)"),
		    level = DeprecationLevel.WARNING
		)
		fun removeKeys(folder: String): Int? =
		    CloudStreamApp.removeKeys(folder)

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.setKey(path, value)"),
		    level = DeprecationLevel.WARNING
		)
		fun <T> setKey(path: String, value: T) =
			CloudStreamApp.setKey(path, value)

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.setKey(folder, path, value)"),
		    level = DeprecationLevel.WARNING
		)
		fun <T> setKey(folder: String, path: String, value: T) =
			CloudStreamApp.setKey(folder, path, value)

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.getKey(path, defVal)"),
		    level = DeprecationLevel.WARNING
		)
		inline fun <reified T : Any> getKey(path: String, defVal: T?): T? =
			CloudStreamApp.getKey(path, defVal)

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.getKey(path)"),
		    level = DeprecationLevel.WARNING
		)
		inline fun <reified T : Any> getKey(path: String): T? =
			CloudStreamApp.getKey(path)

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.getKey(folder, path)"),
		    level = DeprecationLevel.WARNING
		)
		inline fun <reified T : Any> getKey(folder: String, path: String): T? =
		    CloudStreamApp.getKey(folder, path)

		@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.netsom.app.CloudStreamApp.getKey(folder, path, defVal)"),
		    level = DeprecationLevel.WARNING
		)
		inline fun <reified T : Any> getKey(folder: String, path: String, defVal: T?): T? =
			CloudStreamApp.getKey(folder, path, defVal)
	}
}
