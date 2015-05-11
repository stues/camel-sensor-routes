package ch.trackdata.sbs1route.generator;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * This interface describes Classes which can be asked for a update interval
 * duration
 * 
 * @author stue
 * 
 */
public class VariableGeneratorInterval implements TrackGeneratorIntervalAware, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(VariableGeneratorInterval.class);

	private int startTrackAmount = -1;

	private int amountOfSteps = -1;

	private int stepSize = 1;

	private int stepDuration = 60000;

	private Timer updateTimer;

	private int currentStep;

	private AtomicInteger currentUpdateIntervall;

	private RandomTrackGenerator randomTrackGenerator;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUpdateInterval() {
		return currentUpdateIntervall.get();
	}

	@Override
	public boolean isConfigured() {
		return startTrackAmount >= 0 && amountOfSteps >= 0;
	}

	/**
	 * @return the startTrackAmount
	 */
	public int getStartTrackAmount() {
		return startTrackAmount;
	}

	/**
	 * @param startTrackAmount
	 *            the startTrackAmount to set
	 */
	public void setStartTrackAmount(int startTrackAmount) {
		this.startTrackAmount = startTrackAmount;
	}

	/**
	 * @return the amountOfSteps
	 */
	public int getAmountOfSteps() {
		return amountOfSteps;
	}

	/**
	 * @param amountOfSteps
	 *            the amountOfSteps to set
	 */
	public void setAmountOfSteps(int amountOfSteps) {
		this.amountOfSteps = amountOfSteps;
	}

	/**
	 * @return the updateTimer
	 */
	public Timer getUpdateTimer() {
		return updateTimer;
	}

	/**
	 * @param updateTimer
	 *            the updateTimer to set
	 */
	public void setUpdateTimer(Timer updateTimer) {
		this.updateTimer = updateTimer;
	}

	/**
	 * @return the stepSize
	 */
	public int getStepSize() {
		return stepSize;
	}

	/**
	 * @param stepSize
	 *            the stepSize to set
	 */
	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	/**
	 * @return the stepDuration
	 */
	public int getStepDuration() {
		return stepDuration;
	}

	/**
	 * @param stepDuration
	 *            the stepDuration to set
	 */
	public void setStepDuration(int stepDuration) {
		this.stepDuration = stepDuration;
	}

	/**
	 * @return the randomTrackGenerator
	 */
	public RandomTrackGenerator getRandomTrackGenerator() {
		return randomTrackGenerator;
	}

	/**
	 * @param randomTrackGenerator
	 *            the randomTrackGenerator to set
	 */
	public void setRandomTrackGenerator(RandomTrackGenerator randomTrackGenerator) {
		this.randomTrackGenerator = randomTrackGenerator;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		currentStep = 1;
		currentUpdateIntervall = new AtomicInteger((int) ((double) 1000 / startTrackAmount));
//		randomTrackGenerator.setEnabled(true);
		if (isConfigured()) {
			updateTimer = new Timer();
			updateTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					if (currentStep < amountOfSteps) {
						int currentStepSize = startTrackAmount + (++currentStep * stepSize);
						int newUpdateIntervall = (int) ((double) 1000 / currentStepSize);
						currentUpdateIntervall.set(newUpdateIntervall);
						LOGGER.warn("The new upate intervall is: " + newUpdateIntervall + "ms Next Step incease is in " + stepDuration + "ms");
					} else {
						updateTimer.cancel();
						randomTrackGenerator.setEnabled(false);
					}
				}
			}, stepDuration, stepDuration);
		}
	}
}
