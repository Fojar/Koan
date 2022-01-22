package koan

import java.io.ByteArrayOutputStream
import java.io.File


class DrawingClassLoader(parent: ClassLoader) : ClassLoader(parent) {

	override fun loadClass(name: String): Class<*> {

		if (!name.startsWith("drawings.")) return super.loadClass(name)

		val b = getClassBytes(name)
		return defineClass(name, b, 0, b.size)
	}

	private fun getClassBytes(className: String): ByteArray {

		return javaClass.classLoader.getResourceAsStream(
			className.replace('.', File.separatorChar) + ".class"
		)?.use {
			val byteStream = ByteArrayOutputStream()
			it.copyTo(byteStream)
			byteStream.toByteArray()
		} ?: throw Exception()

	}

}