package apsteste;

import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class RonaldinhoGaucho extends AdvancedRobot {
    boolean movendoParaFrente = true;

    public void run() {
        setColors(Color.red, Color.blue, Color.green);
        setAdjustGunForRobotTurn(true);

        while (true) {
            radarMovimento();
            movimento();
            execute();
        }
    }

    void radarMovimento() {
        turnRadarRight(360);
    }

    void movimento() {
        if (movendoParaFrente) {
            setAhead(100);
        } else {
            setBack(100);
        }

        movendoParaFrente = !movendoParaFrente;
    }

    public void onScannedRobot(ScannedRobotEvent event) {
        double absoluteBearing = getHeadingRadians() + event.getBearingRadians();
        setTurnGunRightRadians(normalRelativeAngleDegrees(absoluteBearing - getGunHeadingRadians()));
        setFire(2);
    }

    public void onHitByBullet(HitByBulletEvent event) {
        setTurnLeft(45);
        setBack(50);
    }

    public void onHitWall(HitWallEvent event) {
        setBack(50);
        setTurnRight(90);
    }

    public void onHitRobot(HitRobotEvent event) {
        if (event.isMyFault()) {
            setBack(50);
        } else {
            setTurnRight(45);
            setAhead(50);
        }
    }
}
