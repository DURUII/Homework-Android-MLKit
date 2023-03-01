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

package com.durui.feat.computer_vision.pose_detector;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.durui.feat.computer_vision.classification_counter.PoseClassifierProcessor;
import com.durui.feat.computer_vision.vision_base.GraphicOverlay;
import com.durui.feat.computer_vision.vision_base.VisionProcessorBase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.odml.image.MlImage;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A processor to run pose detector.
 */
public class PoseDetectorProcessor
        extends VisionProcessorBase<PoseDetectorProcessor.PoseWithClassification> {//durui 分析成什么视觉信息：Pose + Class
    // 字符常量
    private static final String TAG = "PoseDetectorProcessor";

    //图像分析
    private final PoseDetector poseDetector;

    //成员变量
    private final boolean showInFrameLikelihood;
    private final boolean visualizeZ;
    private final boolean rescaleZForVisualization;
    private final boolean runClassification;
    private final boolean isStreamMode;
    private final Context context;
    private final Executor classificationExecutor;
    private PoseClassifierProcessor poseClassifierProcessor;

    //durui 构造函数
    public PoseDetectorProcessor(
            Context context,
            PoseDetectorOptionsBase options,//durui 传递指定检测方式，例如，STREAM_MODE/SINGLE_IMAGE_MODE
            boolean showInFrameLikelihood,
            boolean visualizeZ,
            boolean rescaleZForVisualization,
            boolean runClassification,
            boolean isStreamMode) {
        super(context);
        this.showInFrameLikelihood = showInFrameLikelihood;
        this.visualizeZ = visualizeZ;
        this.rescaleZForVisualization = rescaleZForVisualization;
        poseDetector = PoseDetection.getClient(options);
        this.runClassification = runClassification;
        this.isStreamMode = isStreamMode;
        this.context = context;
        classificationExecutor = Executors.newSingleThreadExecutor();//durui 多线程与并发
    }

    @Override
    protected Task<PoseWithClassification> detectInImage(InputImage image) {
        //durui 将图像输入模型，获取预测结果（33个关键点）
        return poseDetector.process(image)
                .continueWith(classificationExecutor, new Continuation<Pose, PoseWithClassification>() {
                    @Override
                    public PoseWithClassification then(@NonNull Task<Pose> task) throws Exception {
                        Pose pose = task.getResult();
                        List<String> classificationResult = new ArrayList<>();
                        //durui 利用上述预测结果，继续获取分类结果（姿态）
                        if (runClassification) {
                            if (poseClassifierProcessor == null) {
                                poseClassifierProcessor = new PoseClassifierProcessor(context, isStreamMode);
                            }
                            classificationResult = poseClassifierProcessor.getPoseResult(pose);
                        }
                        return new PoseWithClassification(pose, classificationResult);
                    }
                });
    }

    @Override
    protected Task<PoseWithClassification> detectInImage(MlImage image) {
        //durui 怎么样处理视觉信息：poseDetector + poseClassifierProcessor
        return poseDetector.process(image)
                .continueWith(classificationExecutor, new Continuation<Pose, PoseWithClassification>() {
                    @Override
                    public PoseWithClassification then(@NonNull Task<Pose> task) throws Exception {
                        Pose pose = task.getResult();//durui 33个关键点
                        List<String> classificationResult = new ArrayList<>();
                        if (runClassification) {
                            if (poseClassifierProcessor == null) {
                                poseClassifierProcessor = new PoseClassifierProcessor(context, isStreamMode);
                            }
                            classificationResult = poseClassifierProcessor.getPoseResult(pose);//durui k-NN
                        }
                        return new PoseWithClassification(pose, classificationResult);//durui Pose + Class
                    }
                });
    }

    @Override
    protected void onSuccess(
            @NonNull PoseWithClassification poseWithClassification,
            @NonNull GraphicOverlay graphicOverlay) {
        //DURUI 关键点可视化
        graphicOverlay.add(
                new PoseGraphic(
                        graphicOverlay,
                        poseWithClassification.pose,
                        showInFrameLikelihood,
                        visualizeZ,
                        rescaleZForVisualization,
                        poseWithClassification.classificationResult));
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Pose detection failed!", e);
    }

    @Override
    protected boolean isMlImageEnabled(Context context) {
        // Use MlImage in Pose Detection by default, change it to OFF to switch to InputImage.
        return true;
    }

    /**
     * Internal class to hold Pose and classification results.
     * durui 处理成什么视觉信息
     */
    protected static class PoseWithClassification {
        private final Pose pose;
        private final List<String> classificationResult;

        public PoseWithClassification(Pose pose, List<String> classificationResult) {
            this.pose = pose;
            this.classificationResult = classificationResult;
        }

        public Pose getPose() {
            return pose;
        }

        public List<String> getClassificationResult() {
            return classificationResult;
        }

    }

    @Override
    public void stop() {
        super.stop();
        poseDetector.close();
    }
}
