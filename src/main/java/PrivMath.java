import jm.music.data.Note;

public class PrivMath {

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static String convertToNote(double frequency) {
        String note = "";
        double[][] noteDictionary = {
                {16.35,	17.32,	18.35,	19.45,	20.60,	21.83,	23.12,	24.50,	25.96,	27.50,	29.14,	30.87},
                {32.70,	34.65,	36.71,	38.89,	41.20,	43.65,	46.25,	49.00,	51.91,	55.00,	58.27,	61.74},
                {65.41,	69.30,	73.42,	77.78,	82.41,	87.31,	92.50,	98.00,	103.8,	110.0,	116.5,	123.5},
                {130.8,	138.6,	146.8,	155.6,	164.8,	174.6,	185.0,	196.0,	207.7,	220.0,	233.1,	246.9},
                {261.6,	277.2,	293.7,	311.1,	329.6,	349.2,	370.0,	392.0,	415.3,	440.0,	466.2,	493.9},
                {523.3,	554.4,	587.3,	622.3,	659.3,	698.5,	740.0,	784.0,	830.6,	880.0,	932.3,	987.8},
                {1047,	1109,	1175,	1245,	1319,	1397,	1480,	1568,	1661,	1760,	1865,	1976},
                {2093,	2217,	2349,	2489,	2637,	2794,	2960,	3136,	3322,	3520,	3729,	3951},
                {4186,	4435,	4699,	4978,	5274,	5588,	5920,	6272,	6645,	7040,	7459,	7902}
        };
        String[] baseNoteConversionDictionary = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B"};
        for (int i = 0; i < noteDictionary.length; i++) {
            double[] row = noteDictionary[i];
            if (!(row[row.length - 1] < frequency)) {
                int highEnd = 0;
                for (int j=0; j < row.length; j++) {
                    double element = row[j];
                    if (frequency < element) {
                        highEnd = j;
                        break;
                    }
                }
                if (highEnd != 0) {
                    return baseNoteConversionDictionary[(Math.abs(row[highEnd] - frequency) > Math.abs(row[highEnd - 1] - frequency)) ? highEnd - 1 : highEnd] + i + "";
                } else {
                    if (Math.abs(row[0] - frequency) > Math.abs(noteDictionary[i][row.length - 1] - frequency)) {
                        return "B" + (i - 1) + "";
                    } else {
                        return "C" + i + "";
                    }
                }
            }
        }
        return "ERROR";
    }

    public static int getOctave(Note n) {
        double freq = n.getFrequency();
        if (!n.getNote().equals("B")) {
            if (freq > 4000) {
                return 8;
            } else if (freq > 2000) {
                return 7;
            } else if (freq > 1000) {
                return 6;
            } else if (freq > 500) {
                return 5;
            } else if (freq > 250) {
                return 4;
            } else if (freq > 125) {
                return 3;
            } else if (freq > 63) {
                return 2;
            } else if (freq > 30) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (freq > 4500) {
                return 8;
            } else if (freq > 2500) {
                return 7;
            } else if (freq > 1200) {
                return 6;
            } else if (freq > 600) {
                return 5;
            } else if (freq > 350) {
                return 4;
            } else if (freq > 145) {
                return 3;
            } else if (freq > 83) {
                return 2;
            } else if (freq > 40) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
