package ch.stue.receiver.sbs1.generator;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface describes Classes which can be asked for a update interval
 * duration
 *
 * @author stue
 *
 */
public class VariableGeneratorInterval extends AbstractGeneratorInterval {

	private static final Logger LOGGER = LoggerFactory.getLogger(VariableGeneratorInterval.class);

	private int startTrackAmount = -1;

	private int amountOfSteps = -1;

	private int stepSize = 1;

	private int stepDuration = 60000;

	private int currentStep;

	private int delayBetweenSteps = 0;

	private AtomicInteger currentUpdateIntervall;

	private boolean enabled;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUpdateInterval() {
		return this.currentUpdateIntervall.get();
	}

	@Override
	public boolean isConfigured() {
		return this.startTrackAmount >= 0 && this.amountOfSteps >= 0;
	}

	/**
	 * Do configure and start variable interval
	 */
	@Override
	public void doStartInterval(final RandomTrackGenerator randomTrackGenerator) {
		if (isEnabled()) {
			this.currentStep = 1;
			this.currentUpdateIntervall = new AtomicInteger((int) ((double) 1000 / this.startTrackAmount));
			if (isConfigured()) {
				startInterval(randomTrackGenerator);
				randomTrackGenerator.generateTracks();
			}
		}
	}

	/**
	 * Starts the timer after which the next interval will be calculated an
	 * initiated
	 *
	 * @param randomTrackGenerator
	 *            the random track generator
	 */
	private void startInterval(final RandomTrackGenerator randomTrackGenerator) {
		this.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (VariableGeneratorInterval.this.currentStep < VariableGeneratorInterval.this.amountOfSteps) {
					int currentStepSize = VariableGeneratorInterval.this.startTrackAmount + (++VariableGeneratorInterval.this.currentStep * VariableGeneratorInterval.this.stepSize);
					int newUpdateIntervall = (int) ((double) 1000 / currentStepSize);
					startNextInterval(randomTrackGenerator, newUpdateIntervall);
				} else {
					VariableGeneratorInterval.this.timer.cancel();
					randomTrackGenerator.disconnectSource();
				}
			}
		}, this.stepDuration);
	}

	/**
	 * starts the next interval. If a delayBetweenSteps is set the thread will
	 * sleep for the defined time
	 *
	 * @param randomTrackGenerator
	 * @param newUpdateIntervall
	 */
	private void startNextInterval(final RandomTrackGenerator randomTrackGenerator, int newUpdateIntervall) {
		LOGGER.info("Disconnect Source and Sleep for {} ms", this.delayBetweenSteps);
		if (this.delayBetweenSteps > 0) {
			randomTrackGenerator.disconnectSource();
			try {
				Thread.sleep(this.delayBetweenSteps);
				this.currentUpdateIntervall.set(newUpdateIntervall);
			} catch (InterruptedException e) {
				LOGGER.debug("Thread interrupted", e);
			}
		}

		LOGGER.info("The new upate intervall is: {}ms Next Step incease is in {}ms", newUpdateIntervall, this.stepDuration);

		randomTrackGenerator.generateTracks();
		startInterval(randomTrackGenerator);
	}

	/**
	 * @return the startTrackAmount
	 */
	public int getStartTrackAmount() {
		return this.startTrackAmount;
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
		return this.amountOfSteps;
	}

	/**
	 * @param amountOfSteps
	 *            the amountOfSteps to set
	 */
	public void setAmountOfSteps(int amountOfSteps) {
		this.amountOfSteps = amountOfSteps;
	}

	/**
	 * @return the stepSize
	 */
	public int getStepSize() {
		return this.stepSize;
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
		return this.stepDuration;
	}

	/**
	 * @param stepDuration
	 *            the stepDuration to set
	 */
	public void setStepDuration(int stepDuration) {
		this.stepDuration = stepDuration;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the delayBetweenSteps
	 */
	public int getDelayBetweenSteps() {
		return this.delayBetweenSteps;
	}

	/**
	 * @param delayBetweenSteps
	 *            the delayBetweenSteps to set
	 */
	public void setDelayBetweenSteps(int delayBetweenSteps) {
		this.delayBetweenSteps = delayBetweenSteps;
	}
}
