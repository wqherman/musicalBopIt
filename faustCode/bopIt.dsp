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

//counters used to apply the effect and fade it out over time
shakeCounter = +(1)*(shakeGate)~_ : int;
tapCounter = +(1)*(tapGate)~_ : int;
ampCounter = +(1)*(ampGate)~_ : int;

//track the amplitude of the mic, use vbargraph so that we can send the value back to JAVA
amplitudeTracker = _:amp_follower_ud(0.001, 0.2);
amplitude = amplitudeTracker:vbargraph("amp",0,100)*hslider("poopSlider",0,0,1,0.1);

//create the wah effect on our input signal when the amplitude is above 0.6, this corresponds to 
//the yell it command in the game
wahEffect = autowah(1);

//modulation using a fast sine wave
modEffect = _*osc(40);

//comb filter effect to be applied at amplitude changes
combEffect = _:fb_comb(4096,2048,0.5,0.5);

/*process = _<:(_<:(_*(tapCounter >= 88200)*(shakeCounter >= 88200)*(ampCounter >= 88200)),(wahEffect*(tapCounter < 88200)),(modEffect*(shakeCounter < 88200)):>_),(combEffect*(ampCounter <= 88200)),amplitude:>_;*/

process = _:amplitude;
