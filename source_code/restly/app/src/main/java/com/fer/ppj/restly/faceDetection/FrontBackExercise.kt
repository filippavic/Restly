package com.fer.ppj.restly.faceDetection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Size
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.fer.ppj.restly.R
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import kotlinx.android.synthetic.main.activity_exercise.*

class FrontBackExercise : AppCompatActivity() {

    private var exerciseProgress = 0
    private var prevAngle = 0.toFloat()
    private var noOfCalls = 0

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    private lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        textureView = findViewById(R.id.texture_view)

        animation_view.setAnimation(R.raw.nod)
        Handler().postDelayed({
            exercise_animation.startAnimation(AnimationUtils.loadAnimation(this@FrontBackExercise, R.anim.fade_out_anim))
            exercise_animation.visibility= View.GONE
        }, 6000)

        tv_hint.text = getString(R.string.frontback_exc)

        // Request camera permissions
        if (isCameraPermissionGranted()) {
            textureView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun startCamera() {
        val previewConfig = PreviewConfig.Builder()
            // Preview front camera, 16:9 aspect ratio
            .setLensFacing(CameraX.LensFacing.FRONT)
            .build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener { previewOutput ->
            val parent = texture_view.parent as ViewGroup
            parent.removeView(texture_view)
            textureView.surfaceTexture = previewOutput.surfaceTexture
            parent.addView(texture_view, 0)
        }

        // Image for analysis configuration
        // 200x150 for phones with AI core
        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
            .setLensFacing(CameraX.LensFacing.FRONT)
            .setTargetResolution(Size(100,100))
            .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        val faceDetection =
            FaceAngleXDetection { faces ->
                faces.forEach {
                    val faceCenter =
                        it.getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).points[1].y
                    exercise(faceCenter)
                }
            }

        imageAnalysis.analyzer = faceDetection

        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imageAnalysis)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun exercise(angleX: Float){
        Log.d("angleX", angleX.toString())
        if((angleX > 110 && prevAngle < 110)|| (angleX < 65 && prevAngle > 65)){
            exerciseProgress ++
            progress_horizontal.progress = exerciseProgress
            prevAngle = angleX
        }
        if(noOfCalls == 0 && exerciseProgress == 10){
            noOfCalls ++
            tv_hint.text = "Vježba uspješno završena."
            startActivity(Intent(this, TiltExercise::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                textureView.post { startCamera() }
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}