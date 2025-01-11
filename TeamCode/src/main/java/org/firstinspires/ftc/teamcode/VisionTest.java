package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

public class VisionTest extends LinearOpMode {

    OpenCvWebcam webcam1 = null;

    @Override
    public void runOpMode() {

        WebcamName webcamName = hardwareMap.get(WebcamName.class,("webcam1"));
        int cameraMoniterViewId = hardwareMap.appContext.getResources().getIdentifier("CameraMoniterViewId", "id", hardwareMap.appContext.getPackageName());
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMoniterViewId);

        webcam1.setPipeline(new ExamplePipeline());

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {

                webcam1.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);

            }

            @Override
            public void onError(int errorCode) {

            }


        });

        waitForStart();

        class examplePipeline extends OpenCvPipeline{

            Mat YCbCr = new Mat();
            Mat leftCrop;
            Mat rightCrop;
            double leftavgfin;
            double rightavgfin;
            Mat outPut = new Mat();
            Scalar rectColor = new Scalar(255.0, 0.0, 0.0);

            @Override
            public Mat processFrame(Mat input) {

                Imgproc.cvtColor(input,YCbCr,Imgproc.COLOR_RGB2YCrCb);
                telemetry.addLine("pipeline running");

                Rect leftRect = new Rect(1,1,319,359);
                Rect rightRect = new Rect(320,1,319,359);
                input.copyTo(outPut);


                Core.extractChannel(leftCrop, leftCrop, 2);
                Core.extractChannel(rightCrop, rightCrop, 2);

                Scalar leftavg = Core.mean(leftCrop);
                Scalar rightavg = Core.mean(rightCrop);

                leftavgfin = leftavg.val[0];
                rightavgfin = rightavg.val[0];

                if (leftavgfin > rightavgfin) {
                    telemetry.addLine("Left");
                }

                else {
                    telemetry.addLine("Right");
                }

                else {
                    telemetry.addLine("Right");
                }

                return(outPut);
            }
        }


    }
}
