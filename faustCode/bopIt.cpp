//-----------------------------------------------------
//
// Code generated with Faust 0.9.58 (http://faust.grame.fr)
//-----------------------------------------------------
#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif  

typedef long double quad;
/* link with  */
#include <math.h>

#ifndef FAUSTCLASS 
#define FAUSTCLASS mydsp
#endif

class mydsp : public dsp {
  private:
	int 	iConst0;
	float 	fConst1;
	float 	fConst2;
	float 	fConst3;
	float 	fRec1[2];
	float 	fConst4;
	float 	fRec0[2];
	FAUSTFLOAT 	fbargraph0;
	FAUSTFLOAT 	fslider0;
  public:
	static void metadata(Meta* m) 	{ 
		m->declare("music.lib/name", "Music Library");
		m->declare("music.lib/author", "GRAME");
		m->declare("music.lib/copyright", "GRAME");
		m->declare("music.lib/version", "1.0");
		m->declare("music.lib/license", "LGPL with exception");
		m->declare("math.lib/name", "Math Library");
		m->declare("math.lib/author", "GRAME");
		m->declare("math.lib/copyright", "GRAME");
		m->declare("math.lib/version", "1.0");
		m->declare("math.lib/license", "LGPL with exception");
		m->declare("filter.lib/reference", "https://ccrma.stanford.edu/~jos/filters/");
		m->declare("filter.lib/name", "Faust Filter Library");
		m->declare("filter.lib/author", "Julius O. Smith (jos at ccrma.stanford.edu)");
		m->declare("filter.lib/copyright", "Julius O. Smith III");
		m->declare("filter.lib/version", "1.29");
		m->declare("filter.lib/license", "STK-4.3");
		m->declare("effect.lib/name", "Faust Audio Effect Library");
		m->declare("effect.lib/author", "Julius O. Smith (jos at ccrma.stanford.edu)");
		m->declare("effect.lib/copyright", "Julius O. Smith III");
		m->declare("effect.lib/version", "1.33");
		m->declare("effect.lib/license", "STK-4.3");
		m->declare("oscillator.lib/name", "Faust Oscillator Library");
		m->declare("oscillator.lib/author", "Julius O. Smith (jos at ccrma.stanford.edu)");
		m->declare("oscillator.lib/copyright", "Julius O. Smith III");
		m->declare("oscillator.lib/version", "1.11");
		m->declare("oscillator.lib/license", "STK-4.3");
	}

	virtual int getNumInputs() 	{ return 2; }
	virtual int getNumOutputs() 	{ return 1; }
	static void classInit(int samplingFreq) {
	}
	virtual void instanceInit(int samplingFreq) {
		fSamplingFreq = samplingFreq;
		iConst0 = min(192000, max(1, fSamplingFreq));
		fConst1 = expf((0 - (1e+03f / float(iConst0))));
		fConst2 = expf((0 - (16.666666666666668f / float(iConst0))));
		fConst3 = (1.0f - fConst2);
		for (int i=0; i<2; i++) fRec1[i] = 0;
		fConst4 = (1.0f - fConst1);
		for (int i=0; i<2; i++) fRec0[i] = 0;
		fslider0 = 0.0f;
	}
	virtual void init(int samplingFreq) {
		classInit(samplingFreq);
		instanceInit(samplingFreq);
	}
	virtual void buildUserInterface(UI* interface) {
		interface->openVerticalBox("bopIt");
		interface->addVerticalBargraph("amp", &fbargraph0, 0.0f, 1e+02f);
		interface->addHorizontalSlider("poopSlider", &fslider0, 0.0f, 0.0f, 1.0f, 0.1f);
		interface->closeBox();
	}
	virtual void compute (int count, FAUSTFLOAT** input, FAUSTFLOAT** output) {
		float 	fSlow0 = fslider0;
		FAUSTFLOAT* input0 = input[0];
		FAUSTFLOAT* input1 = input[1];
		FAUSTFLOAT* output0 = output[0];
		for (int i=0; i<count; i++) {
			float fTemp0 = fabsf((float)input0[i]);
			fRec1[0] = ((fConst3 * fTemp0) + (fConst2 * max(fTemp0, fRec1[1])));
			fRec0[0] = ((fConst4 * fRec1[0]) + (fConst1 * fRec0[1]));
			fbargraph0 = (fRec0[0] > 0.7f);
			output0[i] = (FAUSTFLOAT)((float)input1[i] + (fSlow0 * fbargraph0));
			// post processing
			fRec0[1] = fRec0[0];
			fRec1[1] = fRec1[0];
		}
	}
};


