package com.fer.ppj.restly.faceDetection

import android.Manifest
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

class EyesExercise : AppCompatActivity() {

    private var exerciseProgress = 0
    private var eyeOpenPrev = 1
    private var noOfCalls = 0

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    private lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        textureView = findViewById(R.id.texture_view)

        tv_hint.text = "Zatvorite oči, zatim ih otvorite pa podignite obrve"

        // Request camera permissions
        if (isCameraPermissionGranted()) {
            textureView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
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
            .setTargetResolution(Size(200, 150))
            .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        val faceDetection =
            FaceEyesDetection { faces ->
                faces.forEach {
                    val rightEyeOpen = it.rightEyeOpenProbability
                    exercise(rightEyeOpen)
                }
            }

        imageAnalysis.analyzer = faceDetection

        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imageAnalysis)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
            ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun exercise(rightEyeOpen: Float) {
        if (eyeOpenPrev == 1 && rightEyeOpen < 0.3 && rightEyeOpen > 0) {
            exerciseProgress++
            progress_horizontal.progress = exerciseProgress
            eyeOpenPrev = 0
        }
        if (rightEyeOpen > 0.9){
            eyeOpenPrev = 1
        }
        if (noOfCalls == 0 && exerciseProgress == 1) {
            noOfCalls ++
            tv_hint.text = "Vježba uspješno završena."
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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