package domain;

import java.time.Duration;
import java.time.LocalDateTime;

public class timeslot {

	
	 private  LocalDateTime startTime;
	 private  LocalDateTime endTime;


	 public timeslot(LocalDateTime startTime, LocalDateTime endTime) {
	        if (endTime.isBefore(startTime)) {
	            throw new IllegalArgumentException("End time cannot be before start time.");
	        }
	        this.startTime = startTime;
	        this.endTime = endTime;
	    }

	    public long getDurationMinutes() {
	        return Duration.between(startTime, endTime).toMinutes();
	    }

	    public boolean overlapsWith(timeslot other) {
	        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
	    }



	    @Override
	    public String toString() {
	        return startTime + " → " + endTime;
	    }

		public LocalDateTime getStartTime() {
			return startTime;
		}

		public LocalDateTime getEndTime() {
			return endTime;
		}
}
