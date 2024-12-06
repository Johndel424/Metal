package com.example.metal.Borrower.UploadDocuments_Feature;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.mlkit.vision.face.Face;

import java.util.List;

public class FaceOverlayView extends View {

    private final Paint paint;
    private List<Face> faces;

    public FaceOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFFF0000); // Red color for bounding box
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    public void setFaces(List<Face> faces) {
        this.faces = faces;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (faces != null) {
            for (Face face : faces) {
                Rect bounds = face.getBoundingBox();
                canvas.drawRect(bounds, paint);
            }
        }
    }
}


