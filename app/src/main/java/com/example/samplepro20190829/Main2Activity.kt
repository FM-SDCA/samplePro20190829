package com.example.samplepro20190829

import android.Manifest
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import kotlinx.android.synthetic.main.activity_main2.*
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.widget.Toast
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment

class Main2Activity : AppCompatActivity() {
    var myView :CanvasBasicView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        myView = CanvasBasicView(this)
        canvas_FrameLayout.addView(myView)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var pCount : Int = event.getPointerCount()
        var returnVal : Boolean = true
        var eventResult = event.getAction() and ACTION_MASK

        when (pCount) {
            3 -> {
                when (eventResult) {
                    ACTION_POINTER_DOWN -> {
                        checkPermission()
                    }
                }
            }
        }

        return returnVal
    }

    // permissionの確認
    fun checkPermission() {

        // 既に許可している
        if (ActivityCompat.checkSelfPermission(
                this,
//                Manifest.permission.WRITE_INTERNAL_STORAGE
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        ) {
            println("is accessable")
            var globals = Globals()
            globals.saveCapture(findViewById(android.R.id.content), Environment.getExternalStorageDirectory())
        } else {
            println("is not accessable")
            requestLocationPermission()
        } // 拒否していた場合
    }


    // 許可を求める
    fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ActivityCompat.requestPermissions(
                this@Main2Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1234
            )
        } else {
            val toast = Toast.makeText(this, "許可してください", Toast.LENGTH_SHORT)
            toast.show()

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1234
            )
        }
    }
}

class CanvasBasicView(context: Context) : View(context) {

    private val mPaint = Paint()
    val globals = Globals()

    object tapped {
        var x : Float = -1F
        var y : Float = -1F
    }

    object move {
        var x : Float = 0F
        var y : Float = 0F
    }

    object moved {
        var x : Float = 0F
        var y : Float = 0F
    }

    object tapped1 {
        var x : Float = -1F
        var y : Float = -1F
    }
    object tapped2 {
        var x : Float = -1F
        var y : Float = -1F
    }
    object move1 {
        var x : Float = -1F
        var y : Float = -1F
    }
    object move2 {
        var x : Float = -1F
        var y : Float = -1F
    }
    object moved1 {
        var x : Float = 0F
        var y : Float = 0F
    }
    object moved2 {
        var x : Float = 0F
        var y : Float = 0F
    }

    object zoom {
        var x : Float = 1F
        var y : Float = 1F
    }

    object zoomed {
        var x : Float = 1F
        var y : Float = 1F
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPaint.style = Paint.Style.STROKE
        canvas.drawRect(
            (width * 25 * zoom.x / 100F) - move.x,
            (height * 25 * zoom.y / 100F) - move.y,
            (width * 65 * zoom.x / 100F) - move.x,
            (height * 55 * zoom.y / 100F) - move.y,
            mPaint)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var pCount : Int? = event?.getPointerCount()
        var returnVal = true
        var eventResult = event!!.action and ACTION_MASK

        when(pCount) {

            1 -> {
                when (eventResult) {
                    ACTION_DOWN -> {
                        tapped.x = event?.getX()
                        tapped.y = event?.getY()
                        println("TAPPED :x = ${tapped.x}, y = ${tapped.y}  MOVED : x = ${move.x}, y = ${move.y}")
                    }
                    ACTION_MOVE -> {
                        move.x = tapped.x - event?.getX() + moved.x
                        move.y = tapped.y - event?.getY() + moved.y
                        println("TAP :x = ${tapped.x}, y = ${tapped.y}  MOVE : x = ${move.x}, y = ${move.y} ZOOM : x = ${zoom.x}, y = ${zoom.y} ")
                    }
                    ACTION_UP -> {
                        moved.x = move.x
                        moved.y = move.y
                    }
                }
            }

            2 -> {
                when (eventResult) {
                    ACTION_POINTER_DOWN -> {
                        moved.x = move.x
                        moved.y = move.y
                        if(event?.getX(0) < event?.getX(1)){
                            tapped1.x = event?.getX(0)
                            tapped2.x = event?.getX(1)
                        }
                        else {
                            tapped1.x = event?.getX(1)
                            tapped2.x = event?.getX(0)
                        }
                        if(event?.getY(0) < event?.getY(1)){
                            tapped1.y = event?.getY(0)
                            tapped2.y = event?.getY(1)
                        }
                        else {
                            tapped1.y = event?.getY(1)
                            tapped2.y = event?.getY(0)
                        }
                    }
                    ACTION_MOVE -> {

                        if(event?.getX(0) < event?.getX(1)){
                            move1.x = event?.getX(0)
                            move2.x = event?.getX(1)
                        }
                        else {
                            move1.x = event?.getX(1)
                            move2.x = event?.getX(0)
                        }
                        if(event?.getY(0) < event?.getY(1)){
                            move1.y = event?.getY(0)
                            move2.y = event?.getY(1)
                        }
                        else {
                            move1.y = event?.getY(1)
                            move2.y = event?.getY(0)
                        }

                        zoom.x = zoomed.x + ((move2.x - move1.x) / 1000)
                        zoom.y = zoomed.y + ((move2.y - move1.y) / 1000)
                        if(zoom.x < 0.1F) zoom.x = 0.1F
                        if(zoom.y < 0.1F) zoom.y = 0.1F
                        println("TAP :x = ${tapped.x}, y = ${tapped.y}  MOVE : x = ${move.x}, y = ${move.y} ZOOM : x = ${zoom.x}, y = ${zoom.y}  ZOOMED : x = ${zoomed.x}, y = ${zoomed.y} ")
                    }
                    ACTION_POINTER_UP -> {
                        zoomed.x = zoom.x
                        zoomed.y = zoom.y
                    }
                }
            }

            3 -> {
                zoomed.x = 1F
                zoomed.y = 1F
                moved.x = 1F
                moved.y = 1F
                returnVal = false
//                val filePath = getExternalStorageDirectory().toString() + "/capture.jpeg"
//                val file = File(filePath)
//                globals.saveCapture(this, file)

            }
        }

        this.invalidate()

        return returnVal
    }


}