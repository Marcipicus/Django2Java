package testing;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

public class MidiSequenceProgramming {

	public static void retune(final Track track, final double concertAFreq) {
		if (track == null) {
			throw new NullPointerException();
		} else if (concertAFreq <= 0) {
			throw new IllegalArgumentException("concertAFreq " + concertAFreq
					+ " <= 0");
		}

		final int bank = 0;
		final int preset = 0;
		final int channel = 0;
		addTuningChange(track, channel, preset);

		// New frequencies in Hz for the 128 MIDI keys
		final double[] frequencies = new double[128];
		for (int key = 0; key < 128; key++) {
			frequencies[key] = getFrequency(key, concertAFreq);
		}

		final MidiMessage message = createSingleNoteTuningChange(bank, preset,
				frequencies);
		track.add(new MidiEvent(message, 0));
	}

	private static void addTuningChange(final Track track, final int channel,
			final int preset) {
		try {
			// Data Entry
			final ShortMessage dataEntry = new ShortMessage(
					ShortMessage.CONTROL_CHANGE, channel, 0x64, 03);
			final ShortMessage dataEntry2 = new ShortMessage(
					ShortMessage.CONTROL_CHANGE, channel, 0x65, 00);
			track.add(new MidiEvent(dataEntry, 0));
			track.add(new MidiEvent(dataEntry2, 0));
			// Tuning program
			final ShortMessage tuningProgram = new ShortMessage(
					ShortMessage.CONTROL_CHANGE, channel, 0x06, preset);
			track.add(new MidiEvent(tuningProgram, 0));
			// Data Increment
			final ShortMessage dataIncrement = new ShortMessage(
					ShortMessage.CONTROL_CHANGE, channel, 0x60, 0x7F);
			track.add(new MidiEvent(dataIncrement, 0));
			// Data Decrement
			final ShortMessage dataDecrement = new ShortMessage(
					ShortMessage.CONTROL_CHANGE, channel, 0x61, 0x7F);
			track.add(new MidiEvent(dataDecrement, 0));
		} catch (final InvalidMidiDataException e) {
			throw new AssertionError("Unexpected InvalidMidiDataException", e);
		}
	}

	private static MidiMessage createSingleNoteTuningChange(final int bank,
			final int preset, final double[] frequencies) {
		// Compute the integer representation of the frequencies
		final int[] baseKeys = new int[128];
		final int[] tunings = new int[128];
		// MIDI Standard tuning frequency
		final double STANDARD_A4_FREQ = 440d;
		for (int key = 0; key < 128; key++) {
			final int baseKey = computeBaseKey(frequencies[key]);
			if (baseKey >= 0 && baseKey <= 127) {
				final double baseFreq = getFrequency(baseKey, STANDARD_A4_FREQ);
				assert baseFreq <= frequencies[key];
				final double centInterval = getCentInterval(baseFreq,
						frequencies[key]);
				baseKeys[key] = baseKey;
				tunings[key] = (int) (centInterval * 16384d / 100d);
			} else {
				// Frequency is out of range. Using default MIDI tuning for it
				// TODO: Use LOGGER.warn to warn about
				baseKeys[key] = key;
				tunings[key] = 0;
			}
		}

		// Data to send
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write((byte) 0xf0); // SysEx Header
		stream.write((byte) 0x7e); // Non-Realtime. For Realtime use 0x7f
		stream.write((byte) 0x7f); // Target Device: All Devices
		stream.write((byte) 0x08); // MIDI Tuning Standard
		stream.write((byte) 0x07); // Single Note Tuning Change Bank
		stream.write((byte) bank);
		stream.write((byte) preset);
		stream.write(128); // Number of keys to retune
		for (int key = 0; key < 128; key++) {
			stream.write(key); // Key to retune
			stream.write(baseKeys[key]);
			stream.write((tunings[key] >> 7) & 0x7f); // Higher 7 Bit
			stream.write(tunings[key] & 0x7f); // Lower 7 Bit
		}
		stream.write((byte) 0xf7); // EOX
		final byte[] data = stream.toByteArray();

		final MidiMessage message;
		try {
			message = new SysexMessage(data, data.length);
		} catch (final InvalidMidiDataException e) {
			throw new AssertionError("Unexpected InvalidMidiDataException", e);
		}
		return message;
	}

	private static int computeBaseKey(final double freq) {
		// Concert A Pitch is A4 and has the key number 69
		final int A4_KEY = 69;
		final double A4_FREQ = 440d;

		// Returns the highest key number with a lower or equal frequency than
		// freq in standard MIDI frequency mapping (equal temparement, concert
		// pitch A4 = 440 Hz).
		int baseKey = (int) Math.round((12 * log2(freq / A4_FREQ) + A4_KEY));
		double baseFreq = getFrequency(baseKey, A4_FREQ);
		if (baseFreq > freq) {
			baseKey--;
		}
		return baseKey;
	}

	private static double getCentInterval(final double f1, final double f2) {
		// Returns the interval between f1 and f2 in cent
		// (100 Cent complies to one semitone)
		return 1200d * log2(f2 / f1);
	}

	private static double log2(final double x) {
		// Returns the logarithm dualis (log with base 2)
		return Math.log(x) / Math.log(2);
	}

	private static float getFrequency(final int keyNumber,
			final double concertAFreq) {
		// Concert A Pitch is A4 and has the key number 69
		final int KEY_A4 = 69;
		// Returns the frequency of the given key (equal temperament)
		return (float) (concertAFreq * Math.pow(2, (keyNumber - KEY_A4) / 12d));
	}

	public static void main(String[] args) throws Exception {
		final int PPQN = 16; // Pulses/Ticks per quarter note
		Sequence sequence = new Sequence(Sequence.PPQ, PPQN);
		final Track track = sequence.createTrack();

		final double a4Freq = 500; // Hz
		retune(track, a4Freq);

		// Play chromatic Scale from C4 to B4
		final int C4_KEY = 60;
		final int B4_KEY = 71;
		final long quarterTicks = PPQN;
		long tick = 0;
		for (int key = C4_KEY; key <= B4_KEY; key++) {
			final int channel = 0;
			final int velocity = 96;
			final ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_ON,
					channel, key, velocity);
			track.add(new MidiEvent(noteOn, tick));
			tick += quarterTicks;
			final ShortMessage noteOff = new ShortMessage(
					ShortMessage.NOTE_OFF, channel, key, 0);
			track.add(new MidiEvent(noteOff, tick));
		}

		final Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.setSequence(sequence);
		final CountDownLatch waitForEnd = new CountDownLatch(1);
		sequencer.addMetaEventListener(e -> {
			if (e.getType() == 47) {
				waitForEnd.countDown();
			}
		});
		sequencer.open();
		sequencer.start();
		System.out.println("started");
		waitForEnd.await();
		sequencer.stop();
		sequencer.close();
		System.out.println("ready");
	}

}
