package apsteste;

import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.util.*;

public class CamperDaRodinha extends RateControlRobot {
    public void run() {
        while (true) {
            moveAndScan();
        }
    }

    private void moveAndScan() {
        setVelocityRate(5);
        setTurnRateRadians(0);
        execute();
        turnRadarRight(360);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double bulletPower = Math.min(2.0, getEnergy());
        double heading = getHeadingRadians() + e.getBearingRadians();
        double[] enemyPosition = calculateEnemyPosition(e);
        double absoluteAngle = calculateAbsoluteAngle(enemyPosition);
        
        adjustRobotDirection(heading);
        adjustRadarDirection(heading);
        adjustGunDirection(absoluteAngle);
        fire(bulletPower);
        adjustVelocityAndTurn(heading);
    }

    private double[] calculateEnemyPosition(ScannedRobotEvent e) {
        double enemyX = getX() + e.getDistance() * Math.sin(getHeadingRadians() + e.getBearingRadians());
        double enemyY = getY() + e.getDistance() * Math.cos(getHeadingRadians() + e.getBearingRadians());
        double enemyHeading = e.getHeadingRadians();
        double enemyVelocity = e.getVelocity();
        double predictedX = enemyX + Math.sin(enemyHeading) * enemyVelocity;
        double predictedY = enemyY + Math.cos(enemyHeading) * enemyVelocity;
        
        return new double[]{predictedX, predictedY};
    }

    private double calculateAbsoluteAngle(double[] enemyPosition) {
        return Utils.normalAbsoluteAngle(Math.atan2(enemyPosition[0] - getX(), enemyPosition[1] - getY()));
    }

    private void adjustRobotDirection(double heading) {
        setTurnRightRadians(heading / 2 * -1 - getRadarHeadingRadians());
    }

    private void adjustRadarDirection(double heading) {
        setTurnRadarRightRadians(Utils.normalRelativeAngle(heading - getRadarHeadingRadians()));
    }

    private void adjustGunDirection(double absoluteAngle) {
        setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteAngle - getGunHeadingRadians()));
    }

    private void adjustVelocityAndTurn(double heading) {
        if (getVelocityRate() > 0) {
            setVelocityRate(getVelocityRate() + 1);
        } else {
            setVelocityRate(-1);
        }

        if (getVelocityRate() > 0 && ((getTurnRate() < 0 && heading > 0) || (getTurnRate() > 0 && heading < 0))) {
            setTurnRate(getTurnRate() * -1);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        double radarTurn = normalRelativeAngleDegrees(e.getBearing() + getHeading() - getRadarHeading());
        setTurnRadarRight(radarTurn);
        setTurnLeft(-3);
        setTurnRate(3);
        setVelocityRate(-1 * getVelocityRate());
    }

    public void onHitWall(HitWallEvent e) {
        setVelocityRate(-1 * getVelocityRate());
        setTurnRate(getTurnRate() + 2);
    }

    public void onHitRobot(HitRobotEvent e) {
        double gunTurn = normalRelativeAngleDegrees(e.getBearing() + getHeading() - getGunHeading());
        turnGunRight(gunTurn);
        setFire(3);
        setVelocityRate(getVelocity() + 3);
    }
}
