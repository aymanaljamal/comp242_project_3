package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MartyrDate implements Comparable<MartyrDate> {
	private Calendar date;
	private AVLTree<Martyr> martyrsAVL;

	public MartyrDate(Calendar date) {
		this.date = date;
		this.martyrsAVL = new AVLTree<>();
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public AVLTree<Martyr> getMartyrsAVL() {
		return martyrsAVL;
	}

	public void setMartyrsAVL(AVLTree<Martyr> martyrsAVL) {
		this.martyrsAVL = martyrsAVL;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MartyrDate that = (MartyrDate) obj;
		return date.equals(that.date);
	}

	@Override
	public String toString() {

		if (this.date == null) {
			return "Date not set";
		}

		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
		return formatter.format(date.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(date);
	}

	@Override
	public int compareTo(MartyrDate o) {
		return date.compareTo(o.getDate());
	}
}
