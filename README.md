## AI 健身系统（2020 级《移动平台软件设计》课程设计）

课程设计文档: https://github.com/DURUII/Homework-Android-MLKit/blob/master/2020级《移动平台软件设计》课程设计.pdf

### 项目概述：
本项目旨在开发一款 AI 健身系统，利用计算机视觉、人工智能等前沿技术，通过手机摄像头识别并计数用户的健身动作，为用户提供更加精准的健身指导和数据记录。项目基于 Android 平台，采用 JetPack 等一系列新技术进行开发。

### 技术栈：
- **JetPack 组件**：Fragment、Navigation、Lifecycle、DataBinding、ViewModel、LiveData、Room Database、RecyclerView、CameraX
- **计算机视觉与图像处理**：Google ML Kit & Mediapipe
- **其他技术**：Canvas、EazeGraph、DatePicker、RulerView、SharedPreferences、TTS

### 任务难点：
1. **计算机视觉与动作识别**：利用 CameraX 获取图像数据，结合 Google ML Kit 的 Pose Detection API 实时检测人体关键点，实现动作计数与姿态分类。
2. **JetPack 组件的综合应用**：合理运用 JetPack 各组件，实现界面与数据的分离，提升应用的可维护性和用户体验。
3. **数据持久化与管理**：使用 Room Database 设计合理的数据库结构，实现用户数据、运动记录等信息的存储与查询。
4. **UI 设计与 Material Design**：采用 Material Design 设计原则，打造美观、一致的用户界面。

### 技术亮点：
1. **实时动作识别与计数**：通过 CameraX 获取图像流，利用 ML Kit 的 Pose Detection API 实时检测人体关键点，结合 K-最近邻分类算法（K-NN）实现动作计数与姿态分类，为用户提供精准的健身指导。
2. **JetPack 组件的高效集成**：采用 MVVM 架构，利用 ViewModel 与 LiveData 实现数据与界面的双向绑定，结合 Fragment 与 Navigation 实现灵活的界面切换与导航，提升应用的可维护性和用户体验。
3. **数据持久化与查询优化**：使用 Room Database 设计合理的数据库结构，实现用户数据、运动记录等信息的高效存储与查询，支持复杂的数据分析与统计功能。
4. **UI 与交互设计**：遵循 Material Design 设计原则，采用最新的 Material 3 组件，结合 Canvas、EazeGraph 等技术实现丰富的数据可视化效果，提升应用的美观度与用户交互体验。

> 注：本README文件由Kimi阅读课程设计文档后总结。
