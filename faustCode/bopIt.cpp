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
#include <cmath>
template <int N> inline float faustpower(float x) 		{ return powf(x,N); } 
template <int N> inline double faustpower(double x) 	{ return pow(x,N); }
template <int N> inline int faustpower(int x) 			{ return faustpower<N/2>(x) * faustpower<N-N/2>(x); } 
template <> 	 inline int faustpower<0>(int x) 		{ return 1; }
template <> 	 inline int faustpower<1>(int x) 		{ return x; }

#ifndef FAUSTCLASS 
#define FAUSTCLASS mydsp
#endif

class mydsp : public dsp {
  private:
	class SIG0 {
	  private:
		int 	fSamplingFreq;
		int 	iRec11[2];
	  public:
		int getNumInputs() 	{ return 0; }
		int getNumOutputs() 	{ return 1; }
		void init(int samplingFreq) {
			fSamplingFreq = samplingFreq;
			for (int i=0; i<2; i++) iRec11[i] = 0;
		}
		void fill (int count, float output[]) {
			for (int i=0; i<count; i++) {
				iRec11[0] = (1 + iRec11[1]);
				output[i] = sinf((9.587379924285257e-05f * float((iRec11[0] - 1))));
				// post processing
				iRec11[1] = iRec11[0];
			}
		}
	};


	FAUSTFLOAT 	fbutton0;
	float 	fRec0[2];
	int 	IOTA;
	float 	fVec0[4096];
	float 	fRec1[2];
	int 	iConst0;
	float 	fConst1;
	float 	fConst2;
	float 	fConst3;
	float 	fRec4[2];
	float 	fConst4;
	float 	fRec3[2];
	FAUSTFLOAT 	fbargraph0;
	FAUSTFLOAT 	fslider0;
	float 	fConst5;
	float 	fConst6;
	float 	fRec7[2];
	float 	fRec6[2];
	float 	fConst7;
	float 	fConst8;
	float 	fRec8[2];
	float 	fRec9[2];
	float 	fRec5[3];
	FAUSTFLOAT 	fbutton1;
	float 	fRec10[2];
	static float 	ftbl0[65536];
	float 	fConst9;
	float 	fRec12[2];
	FAUSTFLOAT 	fbutton2;
	float 	fRec13[2];
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
		m->declare("filter.lib/name", "Faust Filter Library");
		m->declare("filter.lib/author", "Julius O. Smith (jos at ccrma.stanford.edu)");
		m->declare("filter.lib/copyright", "Julius O. Smith III");
		m->declare("filter.lib/version", "1.29");
		m->declare("filter.lib/license", "STK-4.3");
		m->declare("filter.lib/reference", "https://ccrma.stanford.edu/~jos/filters/");
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

	virtual int getNumInputs() 	{ return 1; }
	virtual int getNumOutputs() 	{ return 1; }
	static void classInit(int samplingFreq) {
		SIG0 sig0;
		sig0.init(samplingFreq);
		sig0.fill(65536,ftbl0);
	}
	virtual void instanceInit(int samplingFreq) {
		fSamplingFreq = samplingFreq;
		fbutton0 = 0.0;
		for (int i=0; i<2; i++) fRec0[i] = 0;
		IOTA = 0;
		for (int i=0; i<4096; i++) fVec0[i] = 0;
		for (int i=0; i<2; i++) fRec1[i] = 0;
		iConst0 = min(192000, max(1, fSamplingFreq));
		fConst1 = expf((0 - (1e+03f / float(iConst0))));
		fConst2 = expf((0 - (16.666666666666668f / float(iConst0))));
		fConst3 = (1.0f - fConst2);
		for (int i=0; i<2; i++) fRec4[i] = 0;
		fConst4 = (1.0f - fConst1);
		for (int i=0; i<2; i++) fRec3[i] = 0;
		fslider0 = 0.0f;
		fConst5 = expf((0 - (1e+01f / float(iConst0))));
		fConst6 = (1.0f - fConst5);
		for (int i=0; i<2; i++) fRec7[i] = 0;
		for (int i=0; i<2; i++) fRec6[i] = 0;
		fConst7 = (2827.4333882308138f / float(iConst0));
		fConst8 = (1413.7166941154069f / float(iConst0));
		for (int i=0; i<2; i++) fRec8[i] = 0;
		for (int i=0; i<2; i++) fRec9[i] = 0;
		for (int i=0; i<3; i++) fRec5[i] = 0;
		fbutton1 = 0.0;
		for (int i=0; i<2; i++) fRec10[i] = 0;
		fConst9 = (float(40) / float(iConst0));
		for (int i=0; i<2; i++) fRec12[i] = 0;
		fbutton2 = 0.0;
		for (int i=0; i<2; i++) fRec13[i] = 0;
	}
	virtual void init(int samplingFreq) {
		classInit(samplingFreq);
		instanceInit(samplingFreq);
	}
	virtual void buildUserInterface(UI* interface) {
		interface->openVerticalBox("bopIt");
		interface->addVerticalBargraph("amp", &fbargraph0, 0.0f, 1e+02f);
		interface->addButton("ampButton", &fbutton0);
		interface->addHorizontalSlider("poopSlider", &fslider0, 0.0f, 0.0f, 1.0f, 0.1f);
		interface->addButton("shakeButton", &fbutton2);
		interface->addButton("tapButton", &fbutton1);
		interface->closeBox();
	}
	virtual void compute (int count, FAUSTFLOAT** input, FAUSTFLOAT** output) {
		float 	fSlow0 = fbutton0;
		float 	fSlow1 = fslider0;
		float 	fSlow2 = fbutton1;
		float 	fSlow3 = fbutton2;
		FAUSTFLOAT* input0 = input[0];
		FAUSTFLOAT* output0 = output[0];
		for (int i=0; i<count; i++) {
			fRec0[0] = (fSlow0 * (1 + fRec0[1]));
			int iTemp0 = int(fRec0[0]);
			float fTemp1 = (float)input0[i];
			float fTemp2 = (fTemp1 - (0.5f * fRec1[1]));
			fVec0[IOTA&4095] = fTemp2;
			fRec1[0] = fVec0[(IOTA-2048)&4095];
			float 	fRec2 = fVec0[IOTA&4095];
			float fTemp3 = fabsf(fTemp1);
			fRec4[0] = ((fConst3 * fTemp3) + (fConst2 * max(fTemp3, fRec4[1])));
			fRec3[0] = ((fConst4 * fRec4[0]) + (fConst1 * fRec3[1]));
			fbargraph0 = fRec3[0];
			fRec7[0] = ((fConst6 * fTemp3) + (fConst5 * max(fTemp3, fRec7[1])));
			fRec6[0] = ((0.0001000000000000001f * powf(4.0f,fRec7[0])) + (0.999f * fRec6[1]));
			float fTemp4 = powf(2.0f,(2.3f * fRec7[0]));
			float fTemp5 = (1 - (fConst8 * (fTemp4 / powf(2.0f,(1.0f + (2.0f * (1.0f - fRec7[0])))))));
			fRec8[0] = ((0.0010000000000000009f * (0 - (2.0f * (fTemp5 * cosf((fConst7 * fTemp4)))))) + (0.999f * fRec8[1]));
			fRec9[0] = ((0.0010000000000000009f * faustpower<2>(fTemp5)) + (0.999f * fRec9[1]));
			fRec5[0] = (0 - (((fRec9[0] * fRec5[2]) + (fRec8[0] * fRec5[1])) - (fTemp1 * fRec6[0])));
			fRec10[0] = (fSlow2 * (1 + fRec10[1]));
			int iTemp6 = int(fRec10[0]);
			float fTemp7 = (fConst9 + fRec12[1]);
			fRec12[0] = (fTemp7 - floorf(fTemp7));
			fRec13[0] = (fSlow3 * (1 + fRec13[1]));
			int iTemp8 = int(fRec13[0]);
			output0[i] = (FAUSTFLOAT)((fTemp1 * ((((iTemp6 >= 88200) * (iTemp8 >= 88200)) * (iTemp0 >= 88200)) + ((iTemp8 < 88200) * ftbl0[int((65536.0f * fRec12[0]))]))) + (((iTemp6 < 88200) * (fRec5[0] - fRec5[1])) + ((fSlow1 * fbargraph0) + (0.5f * (fRec2 * (iTemp0 <= 88200))))));
			// post processing
			fRec13[1] = fRec13[0];
			fRec12[1] = fRec12[0];
			fRec10[1] = fRec10[0];
			fRec5[2] = fRec5[1]; fRec5[1] = fRec5[0];
			fRec9[1] = fRec9[0];
			fRec8[1] = fRec8[0];
			fRec6[1] = fRec6[0];
			fRec7[1] = fRec7[0];
			fRec3[1] = fRec3[0];
			fRec4[1] = fRec4[0];
			fRec1[1] = fRec1[0];
			IOTA = IOTA+1;
			fRec0[1] = fRec0[0];
		}
	}
};


float 	mydsp::ftbl0[65536];
