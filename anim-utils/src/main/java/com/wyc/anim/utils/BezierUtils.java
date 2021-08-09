package com.wyc.anim.utils;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Arrays;

public class BezierUtils {

    private static final int FRAME = 1000; // 1000帧

    /**
     * deCasteljau算法
     *
     * @param i 阶数
     * @param j 点
     * @param t 时间
     * @return
     */
    private static float deCasteljauX(int i, int j, float t, ArrayList<PointF> controlPoints) {
        if (i == 1) {
            return (1 - t) * controlPoints.get(j).x + t * controlPoints.get(j + 1).x;
        }
        return (1 - t) * deCasteljauX(i - 1, j, t, controlPoints) + t * deCasteljauX(i - 1, j + 1, t, controlPoints);
    }

    /**
     * deCasteljau算法
     *
     * @param i 阶数
     * @param j 点
     * @param t 时间
     * @return
     */
    private static float deCasteljauY(int i, int j, float t, ArrayList<PointF> controlPoints) {
        if (i == 1) {
            return (1 - t) * controlPoints.get(j).y + t * controlPoints.get(j + 1).y;
        }
        return (1 - t) * deCasteljauY(i - 1, j, t, controlPoints) + t * deCasteljauY(i - 1, j + 1, t, controlPoints);
    }

    /**
     * 创建Bezier点集
     *
     * @return
     */
    private static ArrayList<PointF> buildBezierPoints(ArrayList<PointF> controlPoints) {
        ArrayList<PointF> points = new ArrayList<PointF>();
        int order = controlPoints.size() - 1;
        float delta = 1.0f / FRAME;
        for (float t = 0; t <= 1; t += delta) {
            points.add(new PointF(deCasteljauX(order, 0, t, controlPoints), deCasteljauY(order, 0, t, controlPoints)));
        }
        return points;
    }

    /**
     * 生成(0,0)到(1,1)的贝塞尔曲线
     *
     * @param points Bezier曲线控制点坐标集
     * @return Bezier曲线
     */
    public static Path buildPath(PointF... points) {
        ArrayList<PointF> controlPoints = new ArrayList<PointF>();
        ArrayList<PointF> bezierPoints;
        Path bezierPath = new Path();
        controlPoints.add(new PointF(0f, 0f));
        controlPoints.addAll(Arrays.asList(points));
        controlPoints.add(new PointF(1f, 1f));
        bezierPoints = buildBezierPoints(controlPoints);
        bezierPath.reset();
        bezierPath.moveTo(0f, 0f);
        for (PointF point : bezierPoints) {
            bezierPath.lineTo(point.x, point.y);
        }
        bezierPath.lineTo(1f, 1f);
        return bezierPath;
    }
}
