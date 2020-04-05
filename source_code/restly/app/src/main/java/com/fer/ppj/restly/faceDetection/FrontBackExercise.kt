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

        text.text = "Pomičite glavu naprijed natrag"

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
            .setTargetResolution(Size(200,150))
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

    private fun exercise(angleY: Float){
        if((angleY > 110 && prevAngle < 110)|| (angleY < 60 && prevAngle > 60)){
            exerciseProgress ++
            progress_horizontal.progress = exerciseProgress
            prevAngle = angleY
        }
        if(noOfCalls == 0 && exerciseProgress == 1){
            noOfCalls ++
            text.text = "Vježba uspješno završena."
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