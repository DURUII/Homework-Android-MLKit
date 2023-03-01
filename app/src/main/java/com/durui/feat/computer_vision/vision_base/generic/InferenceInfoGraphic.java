/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.durui.feat.computer_vision.vision_base.generic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.annotation.Nullable;

import com.durui.feat.computer_vision.vision_base.GraphicOverlay;

/**
 * Graphic instance for rendering inference info (latency, FPS, resolution) in an overlay view.
 */
public class InferenceInfoGraphic extends GraphicOverlay.Graphic {

    private static final int TEXT_COLOR = Color.rgb(238, 255, 255);//primaryLight
    private static final float TEXT_SIZE = 50.0f;
    private static final String TEXT_FACE = "font/jet_brains_mono.ttf";

    private final Paint textPaint;
    private final GraphicOverlay overlay;
    private final long frameLatency;
    private final long detectorLatency;

    // Only valid when a stream of input images is being processed. Null for single image mode.
    @Nullable
    private final Integer framesPerSecond;
    private boolean showLatencyInfo = true;

    public InferenceInfoGraphic(
            GraphicOverlay overlay,
            long frameLatency,
            long detectorLatency,
            @Nullable Integer framesPerSecond) {
        super(overlay);
        this.overlay = overlay;
        this.frameLatency = frameLatency;
        this.detectorLatency = detectorLatency;
        this.framesPerSecond = framesPerSecond;

        //TODO
        textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);
        Typeface typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(), TEXT_FACE);
        textPaint.setTypeface(typeFace);

        //invalidate 在主线程（UI线程）中调用，postInvalidate在工作线程中调用
        postInvalidate();
    }

    /**
     * Creates an {@link InferenceInfoGraphic} to only display image size.
     */
    public InferenceInfoGraphic(GraphicOverlay overlay) {
        this(overlay, 0, 0, null);
        showLatencyInfo = false;
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        float x = TEXT_SIZE * 0.5f;
        float y = TEXT_SIZE * 1.5f;

        canvas.drawText(
                "InputImage size: " + overlay.getImageHeight() + "x" + overlay.getImageWidth(),
                x,
                y,
                textPaint);

        if (!showLatencyInfo) {
            return;
        }
        // Draw FPS (if valid) and inference latency
        if (framesPerSecond != null) {
            canvas.drawText(
                    "FPS: " + framesPerSecond + ", Frame latency: " + frameLatency + " ms",
                    x,
                    y + TEXT_SIZE,
                    textPaint);
        } else {
            canvas.drawText("Frame latency: " + frameLatency + " ms", x, y + TEXT_SIZE, textPaint);
        }
        //canvas.drawText("Detector latency: " + detectorLatency + " ms", x, y + TEXT_SIZE * 2, textPaint);
        canvas.drawText("软件 2003 杜睿", x, y + TEXT_SIZE * 2, textPaint);
    }
}
