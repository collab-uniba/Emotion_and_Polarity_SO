package OrtuValidation.model;

/**
 * Class for printing in .csv files
 */
public  class DatasetRowOrtu {
	private final String anger;
	private final String sadness;
	private final String fear;
	private final String joy;
	private final String love;
	private final String surprise;
	private final String label;
	private final String id;
	private final String comment;

	public String getSurprise(){
		return surprise;
	}
	public String getId(){
		return id;
	}
	public String getLabel(){
		return label;
	}
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
	public String getComment() {
		return comment;
	}
	private DatasetRowOrtu(DatasetRowBuilder builder) {
		this.anger=builder.anger;
		this.fear=builder.fear;
		this.sadness=builder.sadness;
		this.love=builder.love;
		this.joy=builder.joy;
		this.surprise=builder.surprise;
		this.label=builder.label;
		this.id=builder.id;
		this.comment=builder.comment;
	}

	public static class DatasetRowBuilder {
		private String anger;
		private String sadness;
		private String fear;
		private String joy;
		private String love;
		private String surprise;
		private String label;
		private String id;
		private String comment;

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
		public  DatasetRowBuilder  setLabel(String label) {
			this.label=label;
			return this;
		}
		 public  DatasetRowBuilder  setId(String id) {
			this.id=id;
			return this;
		}

		public DatasetRowBuilder  setComment(String comment) {
			 this.comment=comment;
			 return this;
		 }
		public DatasetRowBuilder  setIdCommentLabel(String id,String comment,String label) {
			 this.comment=comment;
			 this.id=id;
			 this.label=label;
			 return this;
		 }
		public DatasetRowBuilder  setCommentLabel(String comment,String label) {
			 this.comment=comment;
			 this.label=label;
			 return this;
		 }
		public DatasetRowOrtu build() {
			return new DatasetRowOrtu(this);
		}

	}
}
