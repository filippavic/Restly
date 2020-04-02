package com.fer.ppj.restly.kotlin.facedetection

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.fer.ppj.restly.common.GraphicOverlay
import com.fer.ppj.restly.common.GraphicOverlay.Graphic

/** Graphic instance for rendering face contours graphic overlay view.  */
class FaceContourGraphic(overlay: GraphicOverlay, private val firebaseVisionFace: FirebaseVisionFace?) :
        Graphic(overlay) {

    private val idPaint: Paint

    init {
        val selectedColor = Color.BLACK

        idPaint = Paint()
        idPaint.color = selectedColor
        idPaint.textSize = ID_TEXT_SIZE

    }

    /** Draws the face annotations for position on the supplied canvas.  */
    override fun draw(canvas: Canvas) {
        val face = firebaseVisionFace ?: return

//        // Draws a circle at the position of the detected face, with the face's track id below.
//        val x = translateX(face.boundingBox.centerX().toFloat())
//        val y = translateY(face.boundingBox.centerY().toFloat())
//        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, facePositionPaint)
//        canvas.drawText("id: ${face.trackingId}", x + ID_X_OFFSET, y + ID_Y_OFFSET, idPaint)

//        // Draws a bounding box around the face.
//        val xOffset = scaleX(face.boundingBox.width() / 2.0f)
//        val yOffset = scaleY(face.boundingBox.height() / 2.0f)
//        val left = x - xOffset
//        val top = y - yOffset
//        val right = x + xOffset
//        val bottom = y + yOffset
//        canvas.drawRect(left, top, right, bottom, boxPaint)

//        val contour = face.getContour(FirebaseVisionFaceContour.ALL_POINTS)
//        for (point in contour.points) {
//            val px = translateX(point.x)
//            val py = translateY(point.y)
//            canvas.drawCircle(px, py, FACE_POSITION_RADIUS, facePositionPaint)
//        }

//        if (face.smilingProbability >= 0) {
//            canvas.drawText(
//                    "happiness: ${String.format("%.2f", face.smilingProbability)}",
//                    x + ID_X_OFFSET * 3,
//                    y - ID_Y_OFFSET,
//                    idPaint)
//        }
        canvas.drawText(
                "angle y: ${String.format("%.2f", face.headEulerAngleY)}",
                0.toFloat(),
                30.toFloat(),
                idPaint)
        canvas.drawText(
                "angle z: ${String.format("%.2f", face.headEulerAngleZ)}",
                0.toFloat(),
                60.toFloat(),
                idPaint)
        canvas.drawText(
                "right eye: ${String.format("%.2f", face.rightEyeOpenProbability)}",
                0.toFloat(),
                90.toFloat(),
                idPaint)
        canvas.drawText(
                "left eye: ${String.format("%.2f", face.leftEyeOpenProbability)}",
                0.toFloat(),
                120.toFloat(),
                idPaint)
        val right_eyebrow = face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).points[2].y
        val right_eye = face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).points[4].y
        val right_eyebrow_relative = right_eye - right_eyebrow
        canvas.drawText(
                "right eyebrow: ${String.format("%.2f", right_eyebrow_relative)}",
                0.toFloat(),
                150.toFloat(),
                idPaint)
        val left_eyebrow = face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP).points[2].y
        val left_eye = face.getContour(FirebaseVisionFaceContour.LEFT_EYE).points[4].y
        val left_eyebrow_relative = left_eye - left_eyebrow
        canvas.drawText(
                "left eyebrow: ${String.format("%.2f", left_eyebrow_relative)}",
                0.toFloat(),
                180.toFloat(),
                idPaint)
    }

    companion object {
        private const val ID_TEXT_SIZE = 30.0f
    }
}
