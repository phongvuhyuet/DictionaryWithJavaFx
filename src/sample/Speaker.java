package sample;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Speaker {
    private Voice voice;

    public Speaker() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();// Allocating Voice
        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }

    public void say(String something) {
        this.voice.speak(something);
    }

    public void sayMultiple(String[] sayMePls) {
        for (String str : sayMePls) {
            this.say(str);
        }
    }
}
