import java.util.List;

public class DisplayAdapter {

    public static String[] projectDisplay(List<int[]> tab) {
        String[] fretboard = {"[", "[", "[", "[", "[", "["};

        for (int[] pos : tab) {
            fretboard[pos[0]] += "-" + pos[1] + "-";
            for (int i = 0; i < fretboard.length; i++) {
                if (i!=pos[0]) {
                    fretboard[i] += "---";
                }
            }
        }

        for (int i = 0; i < fretboard.length; i++) {
            fretboard[i] += "]";
        }

        return fretboard;
    }

}
