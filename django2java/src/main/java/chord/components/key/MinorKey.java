package chord.components.key;

import chord.NoteName;

/**
 * I'm not sure if this class is really necessary since the keys can
 * be dealt with using musicxml or MIDI numbers.
 * @author DAD
 *
 */
public enum MinorKey {
	A(MajorKey.C),
	E(MajorKey.G),
	B(MajorKey.D),
	F_S(MajorKey.A),
	C_S(MajorKey.E),
	G_S(MajorKey.B),
	D_S(MajorKey.F_S),
	A_S(MajorKey.C_S),
	Ab(MajorKey.Cb),
	Eb(MajorKey.Gb),
	Bb(MajorKey.Db),
	F(MajorKey.Ab),
	C(MajorKey.Eb),
	G(MajorKey.Bb),
	D(MajorKey.F);
	
	private final MajorKey relativeMajor;
	
	private MinorKey(MajorKey relativeMajor) {
		if(relativeMajor==null) {
			String MSG_RELATIVE_MAJOR_CANNOT_BE_NULL = "Relative Major may not be null";
			throw new IllegalArgumentException(MSG_RELATIVE_MAJOR_CANNOT_BE_NULL);
		}
		
		this.relativeMajor = relativeMajor;
	}
	
	public MajorKey getRelativeMajor() {
		return this.relativeMajor;
	}
	
	public String getNoteStringForKey(NoteName noteName) {
		return getRelativeMajor().getNoteStringForKey(noteName);
	}
	
}
