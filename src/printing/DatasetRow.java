package printing;

import java.util.List;

/**
 * Class for printing in .csv files
 */
public class DatasetRow {
	private final String anger;
	private final String sadness;
	private final String fear;
	private final String joy;
	private final String love;
	private final String surprise;

	public String getAnger() {
		return anger;
	}

	public String getSadness() {
		return sadness;
	}

	public String getFear() {
		return fear;
	}

	public String getJoy() {
		return joy;
	}

	public String getLove() {
		return love;
	}

	private DatasetRow(DatasetRowBuilder builder) {
		this.anger=builder.anger;
		this.fear=builder.fear;
		this.sadness=builder.sadness;
		this.love=builder.love;
		this.joy=builder.joy;
		this.surprise=builder.surprise;
	}

	public static class DatasetRowBuilder {
		private String anger;
		private String sadness;
		private String fear;
		private String joy;
		private String love;
		private String surprise;

		public   DatasetRowBuilder setAnger(String anger) {
			this.anger = anger;
			return this;
		}

		public DatasetRowBuilder  setSadness(String sadness) {
			this.sadness = sadness;
			return this;
		}

		public  DatasetRowBuilder setFear(String fear) {
			this.fear = fear;
			return this;
		}

		public  DatasetRowBuilder setJoy(String joy) {
			this.joy = joy;
			return this;
		}

		public  DatasetRowBuilder  setLove(String love) {
			this.love = love;
			return this;
		}
		public  DatasetRowBuilder  setSurprise(String surprise) {
			this.surprise=surprise;
			return this;
		}
		public DatasetRow build() {
			return new DatasetRow(this);
		}
	}
}
