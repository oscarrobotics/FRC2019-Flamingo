package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Sensors.Vision.JevoisTracker;
import frc.team832.GrouchLib.Sensors.Vision.VisionConstants;

public class Vision extends Subsystem {

	private JevoisTracker tracker;

	public Vision() {

	}

	public static class Constants extends VisionConstants {
		private final static double kHorizontalFOV = 55;
		private final static double kVerticalFOV = 42.65386; // calculated from focalLength
		private final static double kHorizontalPixels = 320;
		private final static double kVerticalPixels = 240;
		private final static double kXFocalLength = 341.3307738; // focalLength = px_width / (2 * tan(FOV / 2))
		private final static double kYFocalLength = 332.3115843;

		@Override
		public double getCameraHorizontalMountAngle() {
			return 0; // TODO: Find mounting spot for JeVois!
		}

		@Override
		public double getCameraHorizontalOffset() {
			return 0; // TODO: Find mounting spot for JeVois!
		}

		@Override
		public double getHorizontalFOV() {
			return kHorizontalFOV;
		}

		@Override
		public double getVerticalFOV() {
			return kVerticalFOV;
		}

		@Override
		public double getHorizontalPixels() {
			return kHorizontalPixels;
		}

		@Override
		public double getVerticalPixels() {
			return kVerticalPixels;
		}

		@Override
		public double getXFocalLength() {
			return kXFocalLength;
		}

		@Override
		public double getYFocalLength() {
			return kYFocalLength;
		}
	}

	@Override
	protected void initDefaultCommand() {

	}
}
