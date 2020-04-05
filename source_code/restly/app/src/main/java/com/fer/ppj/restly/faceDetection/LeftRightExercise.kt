package com.fer.ppj.restly.faceDetection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.fer.ppj.restly.R
import kotlinx.android.synthetic.main.activity_exercise.*

class LeftRightExercise : AppCompatActivity() {

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
            textureView.surfaceTexture = previewOutput.surfaceTexture
        }

        // Image for analysis configuration
        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
            .setLensFacing(CameraX.LensFacing.FRONT)
            .setTargetResolution(Size(400,300))
            .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        val faceDetection =
            FaceDetection { faces ->
                faces.forEach {
                    exercise(it.headEulerAngleY)
                }
            }

        imageAnalysis.analyzer = faceDetection

        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imageAnalysis)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun exercise(angleY: Float){
        if((angleY > 45 && prevAngle < 45)|| (angleY <-45 && prevAngle > -45)){
            exerciseProgress ++
            progress_horizontal.progress = exerciseProgress
            prevAngle = angleY
        }
        if(noOfCalls == 0 && exerciseProgress == 1){
            noOfCalls ++
            text.text = "Vježba uspješno završena."
            startActivity(Intent(this, FrontBackExercise::class.java))
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