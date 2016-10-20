package printing;

import java.util.List;

/**
 * Class for printing in .csv files
 */
 class DatasetRow {
	private final String anger;
	private final String sadness;
	private final String fear;
	private final String joy;
	private final String love;
	private final String surprise;
	private final String label;
	private final String id;
	private final String comment;

	String getSurprise(){
		return surprise;
	}
	 String getId(){
		return id;
	}
	String getLabel(){
		return label;
	}
	String getAnger() {
		return anger;
	}

	 String getSadness() {
		return sadness;
	}

	 String getFear() {
		return fear;
	}

	 String getJoy() {
		return joy;
	}

	 String getLove() {
		return love;
	}
	 String getComment() {
		return comment;
	}
	private DatasetRow(DatasetRowBuilder builder) {
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

	 static class DatasetRowBuilder {
		private String anger;
		private String sadness;
		private String fear;
		private String joy;
		private String love;
		private String surprise;
		private String label;
		private String id;
		private String comment;

		   DatasetRowBuilder setAnger(String anger) {
			this.anger = anger;
			return this;
		}

		 DatasetRowBuilder  setSadness(String sadness) {
			this.sadness = sadness;
			return this;
		}

		  DatasetRowBuilder setFear(String fear) {
			this.fear = fear;
			return this;
		}

		  DatasetRowBuilder setJoy(String joy) {
			this.joy = joy;
			return this;
		}

		  DatasetRowBuilder  setLove(String love) {
			this.love = love;
			return this;
		}
		  DatasetRowBuilder  setSurprise(String surprise) {
			this.surprise=surprise;
			return this;
		}
		  DatasetRowBuilder  setLabel(String label) {
			this.label=label;
			return this;
		}
		  DatasetRowBuilder  setId(String id) {
			this.id=id;
			return this;
		}

		  DatasetRowBuilder  setComment(String comment) {
			 this.comment=comment;
			 return this;
		 }
		 DatasetRowBuilder  setIdCommentLabel(String id,String comment,String label) {
			 this.comment=comment;
			 this.id=id;
			 this.label=label;
			 return this;
		 }
		 DatasetRowBuilder  setCommentLabel(String comment,String label) {
			 this.comment=comment;
			 this.label=label;
			 return this;
		 }
		 DatasetRow build() {
			return new DatasetRow(this);
		}

	}
}
