package com.example.samplepro20190829

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showGraphButton.setOnClickListener {
            onShowGraphButtonTapped(it)
        }

    }

    private fun onShowGraphButtonTapped(view : View?) {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
    }

}

class Globals : Application() {

    fun saveCapture(view: View, file: File) {
        // キャプチャを撮る
        val capture = getViewCapture(view)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file, false)
            // 画像のフォーマットと画質と出力先を指定して保存
            capture.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (ie: IOException) {
                    fos = null
                }

            }
        }
    }

    fun getViewCapture(view: View): Bitmap {
        view.isDrawingCacheEnabled = true

        // Viewのキャッシュを取得
        val cache = view.drawingCache
        val screenShot = Bitmap.createBitmap(cache)
        view.isDrawingCacheEnabled = false
        return screenShot
    }
}