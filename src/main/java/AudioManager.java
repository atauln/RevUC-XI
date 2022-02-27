import jm.music.data.Note;
import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTResult;
import org.quifft.output.FrequencyBin;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static jm.constants.Durations.CROTCHET;

public class AudioManager {

    public static double noiseGateThreshold = -40;

    public void recordToWAVFile() {
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(dataInfo)) {
                System.out.println("Not Supported!");
            }

            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();
            JOptionPane.showMessageDialog(null, "Hit OK to start recording!");
            targetLine.start();

            Thread audioRecorderThread = new Thread() {
                @Override
                public void run() {
                    AudioInputStream recordingStream = new AudioInputStream(targetLine);
                    File outputFile = new File("record.wav");
                    try {
                        AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Stopped recording");
                }
            };

            audioRecorderThread.start();
            JOptionPane.showMessageDialog(null, "Hit OK to stop recording!");
            targetLine.stop();
            targetLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBackFromWAVFile(File file) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Note[] parseFFT(File file) {
        FFTResult fft  = null;
        try {
            QuiFFT quiFFT = new QuiFFT(file).windowOverlap(0).numPoints((int) Math.pow(2, 17));
            fft = quiFFT.fullFFT();
        } catch (IOException e) {
            System.out.println("An I/O exception occurred while QuiFFT was opening an input stream to the audio file");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("QuiFFT was given an invalid audio file");
        }

        //System.out.println(fft);

        assert fft != null;
        FFTFrame[] fftFrames = fft.fftFrames;
        //System.out.println("There are " + fftFrames.length + " frames in this FFT, each of which was computed " +
        //        "from a sampling window that was about " + Math.round(fft.windowDurationMs) + " milliseconds long.");
        Note[] results = new Note[fftFrames.length];
        for (int i=0; i < fftFrames.length; i++) {
            FFTFrame fftFrame = fftFrames[i];
            double highestFreq = 30;
            double highestAmp = -100;
            for (FrequencyBin fBin : fftFrame.bins) {
                if (fBin.amplitude > highestAmp && fBin.frequency > 30) {
                    highestAmp = fBin.amplitude;
                    highestFreq = fBin.frequency;
                }
            }
            //System.out.println("Highest Frequency in this frame ( @" + PrivMath.round(fftFrame.frameStartMs, 1) + "ms) is: "
            //        + PrivMath.round(highestFreq, 1) + "hz ( @" + PrivMath.round(highestAmp, 1) + "dB)" +
            //        " Note: " + PrivMath.convertToNote(highestFreq));
            if (highestAmp > noiseGateThreshold) {
                results[i] = new Note(Note.freqToMidiPitch(highestFreq), CROTCHET);
            } else {
                results[i] = new Note(Note.REST, CROTCHET);
            }
        }
        return results;
    }
}
