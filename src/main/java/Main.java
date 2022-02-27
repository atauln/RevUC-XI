import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws LineUnavailableException {
        AudioManager am = new AudioManager();
        am.recordToWAVFile();
        am.playBackFromWAVFile(new File("record.wav"));
        System.out.println("Noise Gate Threshold: " + AudioManager.noiseGateThreshold);
        List<int[]> tab = MusicParser.createTab(am.parseFFT(new File("record.wav")));
        for (String line : DisplayAdapter.projectDisplay(tab)) {
            System.out.println(line);
        }
    }

}
