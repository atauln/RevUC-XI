import jm.music.data.Note;
import jm.music.data.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jm.constants.Durations.*;

public class MusicParser {

    public static Phrase timeNotesToSheetMusic(Note[] notes) {
        Object[][] condensedNotes = new Object[notes.length][2];
        int lastModified = -1;
        for (Note n : notes) {
            if (lastModified == -1) {
                lastModified++;
                condensedNotes[lastModified][0] = n;
                condensedNotes[lastModified][1] = 1;
            } else if (Objects.equals(n.getNote(), ((Note) condensedNotes[lastModified][0]).getNote())) {
                condensedNotes[lastModified][1] = (int) condensedNotes[lastModified][1] + 1;
            } else {
                lastModified++;
                condensedNotes[lastModified][0] = n;
                condensedNotes[lastModified][1] = 1;
            }
        }
        Phrase phrase = new Phrase(0.0);
        for (Object[] note : condensedNotes) {
            int noteDuration = (int) note[1];
            Note aNote = (Note) note[0];
            if (noteDuration > 20) {
                aNote.setRhythmValue(WHOLE_NOTE);
            } else if (noteDuration > 10) {
                aNote.setRhythmValue(HALF_NOTE);
            } else if (noteDuration > 4) {
                aNote.setRhythmValue(QUARTER_NOTE);
            } else if (noteDuration > 1) {
                aNote.setRhythmValue(EIGHTH_NOTE);
            }
            phrase.add(aNote);
        }
        return phrase;
    }

    public static List<int[]> createTab(Note[] notes) {
        List<int[]> tab = new ArrayList<>();
        String[][] tabDictionary = {
                {"E4", "F4", "F#4", "G4", "Ab4", "A4", "Bb4", "B4", "C5", "C#5", "D5", "Eb5", "E5", "F5", "F#5", "G5", "Ab5", "A5", "Bb5", "B5", "C6", "C#6", "D6"},
                {"B3", "C4", "C#4", "D4", "Eb4", "E4", "F4", "F#4", "G4", "Ab4", "A4", "Bb4", "B4", "C5", "C#5", "D5", "Eb5", "E5", "F5", "F#5", "G5", "Ab5", "A5"},
                {"G3", "Ab3", "A3", "Bb3", "B3", "C4", "C#4", "D4", "Eb4", "E4", "F4", "F#4", "G4", "Ab4", "A4", "Bb4", "B4", "C5", "C#5", "D5", "Eb5", "E5", "F5"},
                {"D3", "Eb3", "E3", "F3", "F#3", "G3", "Ab3", "A3", "Bb3", "B3", "C4", "C#4", "D4", "Eb4", "E4", "F4", "F#4", "G4", "Ab4", "A4", "Bb4", "B4", "C5"},
                {"A2", "Bb2", "B2", "C3", "C#3", "D3", "Eb3", "E3", "F3", "F#3", "G3", "Ab3", "A3", "Bb3", "B3", "C4", "C#4", "D4", "Eb4", "E4", "F4", "F#4", "G4"},
                {"E2", "F2", "F#2", "G2", "Ab2", "A2", "Bb2", "B2", "C3", "C#3", "D3", "Eb3", "E3", "F3", "F#3", "G3", "Ab3", "A3", "Bb3", "B3", "C4", "C#4", "D4"}
        };
        Object[][] condensedNotes = new Object[notes.length][2];
        int lastModified = -1;
        for (Note n : notes) {
            if (lastModified == -1) {
                lastModified++;
                condensedNotes[lastModified][0] = n;
                condensedNotes[lastModified][1] = 1;
            } else if (Objects.equals(n.getNote(), ((Note) condensedNotes[lastModified][0]).getNote())) {
                condensedNotes[lastModified][1] = (int) condensedNotes[lastModified][1] + 1;
            } else {
                lastModified++;
                condensedNotes[lastModified][0] = n;
                condensedNotes[lastModified][1] = 1;
            }
        }

        for (int i=0; i < condensedNotes.length - 1; i++) {
            Note n = (Note) condensedNotes[i][0];
            if (n == null || condensedNotes[i+1][0] == null) {
                break;
            }
            int duration = (int) condensedNotes[i][1];

            if (n.getNote().length() > 2 && duration <=2) {
                if (i + 1 < condensedNotes.length && i > 0) {
                    if ((((Note) condensedNotes[i - 1][0]).getNote() + PrivMath.convertToNote(((Note) condensedNotes[i - 1][0]).getFrequency())).equals(((Note) condensedNotes[i + 1][0]).getNote() + PrivMath.convertToNote(((Note) condensedNotes[i + 1][0]).getFrequency()))) {
                        condensedNotes[i+1][0] = (Note) condensedNotes[i][0];
                    }
                }
            }
        }

        for (int h=0; h < condensedNotes.length; h++) {
            Note n = ((Note) condensedNotes[h][0]);
            List<int[]> possibleChoices = new ArrayList<>();
            if (n == null) {
                break;
            }
            if ((int) condensedNotes[h][1] < 3) {
                continue;
            }
            if (!n.getNote().equals("N/A")) {
                for (int i = 0; i < tabDictionary.length; i++) {
                    for (int j = 0; j < tabDictionary[i].length; j++) {
                        if (tabDictionary[i][j].equals(n.getNote() + PrivMath.getOctave(n))) {
                            possibleChoices.add(new int[]{i, j});
                        }
                    }
                }
            }
            //System.out.println("Done with note \"" + n.getNote() + "\" with " + possibleChoices.size() + " possible choices!");
            if (!possibleChoices.isEmpty()) {
                if (tab.isEmpty()) {
                    tab.add(possibleChoices.get(0));
                } else {
                    double[] lowestDistance = {1000, 0};
                    for (int i = 0; i < possibleChoices.size(); i++) {
                        int[] choice = possibleChoices.get(i);
                        if (choice[1] == 0) {
                            lowestDistance[0] = 0;
                            lowestDistance[1] = i;
                        }
                        if (Math.sqrt(Math.pow(tab.get(tab.size() - 1)[0] - choice[0], 2) + Math.pow(tab.get(tab.size() - 1)[1] - choice[1], 2)) < lowestDistance[0]) {
                            lowestDistance[0] = Math.sqrt(Math.pow(tab.get(tab.size() - 1)[0] - choice[0], 2) + Math.pow(tab.get(tab.size() - 1)[1] - choice[1], 2));
                            lowestDistance[1] = i;
                        }
                    }
                    tab.add(possibleChoices.get((int) lowestDistance[1]));
                }
            }
        }
        return tab;
    }

}
