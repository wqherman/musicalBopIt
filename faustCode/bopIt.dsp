//
import("music.lib");
import("filter.lib");
import("effect.lib");
import("oscillator.lib");

//buttons used to get values from the interface, these are triggers that will be set
//when th eappropriate bop it action is performed (tapping the screen, shaking, etc)
shakeGate = button("shakeButton");
tapGate = button("tapButton");
ampGate = button("ampButton");

//track the amplitude of the mic, use vbargraph so that we can send the value back to JAVA
amplitudeTracker = _:amp_follower_ud(0.001, 0.06);
amplitude = (amplitudeTracker > (0.7)):vbargraph("amp",0,100)*hslider("poopSlider",0,0,1,0.1);

//create the wah effect on our input signal when the amplitude is above 0.6, this corresponds to 
//the yell it command in the game
wahEffect = autowah(amplitudeTracker);

//create a phasing effect that occurs when the shake command is performed in the game

process = amplitude + _;
