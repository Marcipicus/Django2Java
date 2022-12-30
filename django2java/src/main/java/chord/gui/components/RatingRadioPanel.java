package chord.gui.components;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import chord.ConsonanceRating;

public class RatingRadioPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JRadioButton veryGood,good,mediocre,bad,veryBad;

	public RatingRadioPanel() {
		super();
		veryGood = new JRadioButton(ConsonanceRating.VERY_GOOD.toString());
		good = new JRadioButton(ConsonanceRating.GOOD.toString());
		mediocre = new JRadioButton(ConsonanceRating.MEDIOCRE.toString());
		bad = new JRadioButton(ConsonanceRating.BAD.toString());
		veryBad = new JRadioButton(ConsonanceRating.VERY_BAD.toString());
		
		veryGood.setSelected(true);
		
		ButtonGroup ratingButtonGroup = new ButtonGroup();
		ratingButtonGroup.add(veryGood);
		ratingButtonGroup.add(good);
		ratingButtonGroup.add(mediocre);
		ratingButtonGroup.add(bad);
		ratingButtonGroup.add(veryBad);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		add(veryGood);
		add(good);
		add(mediocre);
		add(bad);
		add(veryBad);
	}
	
	public ConsonanceRating selectedRating() {
		if(veryGood.isSelected()) {
			return ConsonanceRating.VERY_GOOD;
		}if(good.isSelected()) {
			return ConsonanceRating.GOOD;
		}if(mediocre.isSelected()) {
			return ConsonanceRating.MEDIOCRE;
		}if(bad.isSelected()) {
			return ConsonanceRating.BAD;
		}if(veryBad.isSelected()) {
			return ConsonanceRating.VERY_BAD;
		}
		throw new IllegalStateException("There is no selected rating.");
	}
	
	public void setRating(ConsonanceRating rating) {
		if(rating == null) {
			throw new NullPointerException("rating may not be null");
		}
		switch(rating) {
		case BAD:
			bad.setSelected(true);
			break;
		case GOOD:
			good.setSelected(true);
			break;
		case MEDIOCRE:
			mediocre.setSelected(true);
			break;
		case VERY_BAD:
			veryBad.setSelected(true);
			break;
		case VERY_GOOD:
			veryGood.setSelected(true);
			break;
		default:
			throw new IllegalArgumentException("unhandled case statement...check ConsonanceRating enum");
		}
	}
}
