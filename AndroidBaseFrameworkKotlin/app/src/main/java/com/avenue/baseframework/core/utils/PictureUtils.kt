package com.avenue.baseframework.core.utils

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLConnection
import java.util.*

object PictureUtils {

    fun getScaledBitMap(
        path: String?,
        destWidth: Int,
        destHeight: Int,
        context: Context?,
        uri: Uri?,
        compress: Boolean
    ): Bitmap? {
        val matrix = Matrix()
        matrix.setScale(0.196.toFloat(), 0.196.toFloat())
        matrix.postTranslate(0.5.toFloat(), 0.0.toFloat())
        var image = BitmapFactory.decodeFile(path)
        val image1 = Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        image = rotateImageIfRequired(image, context!!, uri!!)
        if (compress) {
            val bytes = ByteArrayOutputStream()
            image1.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
            try {
                val fo = FileOutputStream(path)
                fo.write(bytes.toByteArray())
                fo.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        return image
    }

    fun getScaledBitmap(path: String?, context: Context, uri: Uri?, compress: Boolean?): Bitmap? {
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            (context as Activity).windowManager.currentWindowMetrics
        } else {
            (context as Activity).windowManager.defaultDisplay.getSize(size)
        }
        return getScaledBitMap(path, size.x, size.y, context, uri, compress!!)
    }

    fun rotateImageIfRequired(img: Bitmap?, context: Context, selectedImage: Uri): Bitmap? {
        img?.let { bmp ->
            if (selectedImage.scheme == "content") {
                val projection = arrayOf(MediaStore.Images.ImageColumns.ORIENTATION)
                val c = context.contentResolver.query(selectedImage, projection, null, null, null)
                c?.let { cursor ->
                    if (cursor.moveToFirst()) {
                        val rotation = cursor.getInt(0)
                        cursor.close()
                        return rotateImage(bmp, rotation)
                    }
                }
            } else {
                try {
                    val ei = selectedImage.path?.let { p ->
                        ExifInterface(p)
                    }
                    val orientation: Int = ei!!.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )
                    return when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bmp, 90)
                        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bmp, 180)
                        else -> bmp
                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
        return null
    }

    fun encodedImage(image: Bitmap, quality: Int): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val data = baos.toByteArray()
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    fun decodedImage(imageCode: String?): Bitmap? {
        val decodedString = Base64.decode(imageCode, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    private fun combineImageIntoOne(bitmap: ArrayList<Bitmap>): Bitmap? {
        var w = 0
        var h = 0
        for (i in bitmap.indices) {
            if (i < bitmap.size - 1) {
                w = bitmap[i].width.coerceAtLeast(bitmap[i + 1].width)
            }
            h += bitmap[i].height
        }
        val temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(temp)
        var top = 0
        for (i in bitmap.indices) {
            Log.d("HTML", "Combine: " + i + "/" + bitmap.size + 1)
            top = if (i == 0) 0 else top + bitmap[i].height
            canvas.drawBitmap(bitmap[i], 0f, top.toFloat(), null)
        }
        return temp
    }

    fun isImageFile(path: String?): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.indexOf("image") == 0
    }
}