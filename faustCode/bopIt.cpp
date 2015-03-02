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
	float 	fRec11[2];
	int 	IOTA;
	float 	fVec0[8192];
	int 	iConst5;
	float 	fRec10[2];
	float 	fRec13[2];
	float 	fVec1[8192];
	int 	iConst6;
	float 	fRec12[2];
	float 	fRec15[2];
	float 	fVec2[8192];
	int 	iConst7;
	float 	fRec14[2];
	float 	fRec17[2];
	float 	fVec3[8192];
	int 	iConst8;
	float 	fRec16[2];
	float 	fRec19[2];
	float 	fVec4[8192];
	int 	iConst9;
	float 	fRec18[2];
	float 	fRec21[2];
	float 	fVec5[8192];
	int 	iConst10;
	float 	fRec20[2];
	float 	fRec23[2];
	float 	fVec6[8192];
	int 	iConst11;
	float 	fRec22[2];
	float 	fRec25[2];
	float 	fVec7[8192];
	int 	iConst12;
	float 	fRec24[2];
	float 	fVec8[1024];
	int 	iConst13;
	float 	fRec8[2];
	float 	fVec9[1024];
	int 	iConst14;
	float 	fRec6[2];
	float 	fVec10[1024];
	int 	iConst15;
	float 	fRec4[2];
	float 	fVec11[1024];
	int 	iConst16;
	float 	fRec2[2];
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
		m->declare("oscillator.lib/name", "Faust Oscillator Library");
		m->declare("oscillator.lib/author", "Julius O. Smith (jos at ccrma.stanford.edu)");
		m->declare("oscillator.lib/copyright", "Julius O. Smith III");
		m->declare("oscillator.lib/version", "1.11");
		m->declare("oscillator.lib/license", "STK-4.3");
		m->declare("effectNew.lib/name", "Faust Audio Effect Library");
		m->declare("effectNew.lib/author", "Julius O. Smith (jos at ccrma.stanford.edu)");
		m->declare("effectNew.lib/copyright", "Julius O. Smith III");
		m->declare("effectNew.lib/version", "1.33");
		m->declare("effectNew.lib/license", "STK-4.3");
		m->declare("effectNew.lib/exciter_name", "Harmonic Exciter");
		m->declare("effectNew.lib/exciter_author", "Priyanka Shekar (pshekar@ccrma.stanford.edu)");
		m->declare("effectNew.lib/exciter_copyright", "Copyright (c) 2013 Priyanka Shekar");
		m->declare("effectNew.lib/exciter_version", "1.0");
		m->declare("effectNew.lib/exciter_license", "MIT License (MIT)");
	}

	virtual int getNumInputs() 	{ return 1; }
	virtual int getNumOutputs() 	{ return 1; }
	static void classInit(int samplingFreq) {
	}
	virtual void instanceInit(int samplingFreq) {
		fSamplingFreq = samplingFreq;
		iConst0 = min(192000, max(1, fSamplingFreq));
		fConst1 = expf((0 - (1e+03f / float(iConst0))));
		fConst2 = expf((0 - (5.0f / float(iConst0))));
		fConst3 = (1.0f - fConst2);
		for (int i=0; i<2; i++) fRec1[i] = 0;
		fConst4 = (1.0f - fConst1);
		for (int i=0; i<2; i++) fRec0[i] = 0;
		fslider0 = 0.0f;
		for (int i=0; i<2; i++) fRec11[i] = 0;
		IOTA = 0;
		for (int i=0; i<8192; i++) fVec0[i] = 0;
		iConst5 = int((0.03666666666666667f * iConst0));
		for (int i=0; i<2; i++) fRec10[i] = 0;
		for (int i=0; i<2; i++) fRec13[i] = 0;
		for (int i=0; i<8192; i++) fVec1[i] = 0;
		iConst6 = int((0.03530612244897959f * iConst0));
		for (int i=0; i<2; i++) fRec12[i] = 0;
		for (int i=0; i<2; i++) fRec15[i] = 0;
		for (int i=0; i<8192; i++) fVec2[i] = 0;
		iConst7 = int((0.03380952380952381f * iConst0));
		for (int i=0; i<2; i++) fRec14[i] = 0;
		for (int i=0; i<2; i++) fRec17[i] = 0;
		for (int i=0; i<8192; i++) fVec3[i] = 0;
		iConst8 = int((0.03224489795918367f * iConst0));
		for (int i=0; i<2; i++) fRec16[i] = 0;
		for (int i=0; i<2; i++) fRec19[i] = 0;
		for (int i=0; i<8192; i++) fVec4[i] = 0;
		iConst9 = int((0.03074829931972789f * iConst0));
		for (int i=0; i<2; i++) fRec18[i] = 0;
		for (int i=0; i<2; i++) fRec21[i] = 0;
		for (int i=0; i<8192; i++) fVec5[i] = 0;
		iConst10 = int((0.02895691609977324f * iConst0));
		for (int i=0; i<2; i++) fRec20[i] = 0;
		for (int i=0; i<2; i++) fRec23[i] = 0;
		for (int i=0; i<8192; i++) fVec6[i] = 0;
		iConst11 = int((0.026938775510204082f * iConst0));
		for (int i=0; i<2; i++) fRec22[i] = 0;
		for (int i=0; i<2; i++) fRec25[i] = 0;
		for (int i=0; i<8192; i++) fVec7[i] = 0;
		iConst12 = int((0.025306122448979593f * iConst0));
		for (int i=0; i<2; i++) fRec24[i] = 0;
		for (int i=0; i<1024; i++) fVec8[i] = 0;
		iConst13 = int((int((int((0.012607709750566893f * iConst0)) - 1)) & 1023));
		for (int i=0; i<2; i++) fRec8[i] = 0;
		for (int i=0; i<1024; i++) fVec9[i] = 0;
		iConst14 = int((int((int((0.01f * iConst0)) - 1)) & 1023));
		for (int i=0; i<2; i++) fRec6[i] = 0;
		for (int i=0; i<1024; i++) fVec10[i] = 0;
		iConst15 = int((int((int((0.007732426303854875f * iConst0)) - 1)) & 1023));
		for (int i=0; i<2; i++) fRec4[i] = 0;
		for (int i=0; i<1024; i++) fVec11[i] = 0;
		iConst16 = int((int((int((0.00510204081632653f * iConst0)) - 1)) & 1023));
		for (int i=0; i<2; i++) fRec2[i] = 0;
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
		FAUSTFLOAT* output0 = output[0];
		for (int i=0; i<count; i++) {
			float fTemp0 = (float)input0[i];
			float fTemp1 = fabsf(fTemp0);
			fRec1[0] = max(fTemp1, ((fConst3 * fTemp1) + (fConst2 * fRec1[1])));
			fRec0[0] = ((fConst4 * fRec1[0]) + (fConst1 * fRec0[1]));
			fbargraph0 = fRec0[0];
			fRec11[0] = ((0.08999999999999997f * fRec10[1]) + (0.91f * fRec11[1]));
			fVec0[IOTA&8191] = (fTemp0 + (0.3f * fRec11[0]));
			fRec10[0] = fVec0[(IOTA-iConst5)&8191];
			fRec13[0] = ((0.08999999999999997f * fRec12[1]) + (0.91f * fRec13[1]));
			fVec1[IOTA&8191] = (fTemp0 + (0.3f * fRec13[0]));
			fRec12[0] = fVec1[(IOTA-iConst6)&8191];
			fRec15[0] = ((0.08999999999999997f * fRec14[1]) + (0.91f * fRec15[1]));
			fVec2[IOTA&8191] = (fTemp0 + (0.3f * fRec15[0]));
			fRec14[0] = fVec2[(IOTA-iConst7)&8191];
			fRec17[0] = ((0.08999999999999997f * fRec16[1]) + (0.91f * fRec17[1]));
			fVec3[IOTA&8191] = (fTemp0 + (0.3f * fRec17[0]));
			fRec16[0] = fVec3[(IOTA-iConst8)&8191];
			fRec19[0] = ((0.08999999999999997f * fRec18[1]) + (0.91f * fRec19[1]));
			fVec4[IOTA&8191] = (fTemp0 + (0.3f * fRec19[0]));
			fRec18[0] = fVec4[(IOTA-iConst9)&8191];
			fRec21[0] = ((0.08999999999999997f * fRec20[1]) + (0.91f * fRec21[1]));
			fVec5[IOTA&8191] = (fTemp0 + (0.3f * fRec21[0]));
			fRec20[0] = fVec5[(IOTA-iConst10)&8191];
			fRec23[0] = ((0.08999999999999997f * fRec22[1]) + (0.91f * fRec23[1]));
			fVec6[IOTA&8191] = (fTemp0 + (0.3f * fRec23[0]));
			fRec22[0] = fVec6[(IOTA-iConst11)&8191];
			fRec25[0] = ((0.08999999999999997f * fRec24[1]) + (0.91f * fRec25[1]));
			fVec7[IOTA&8191] = (fTemp0 + (0.3f * fRec25[0]));
			fRec24[0] = fVec7[(IOTA-iConst12)&8191];
			float fTemp2 = ((((((((fRec24[0] + fRec22[0]) + fRec20[0]) + fRec18[0]) + fRec16[0]) + fRec14[0]) + fRec12[0]) + fRec10[0]) + (0.25f * fRec8[1]));
			fVec8[IOTA&1023] = fTemp2;
			fRec8[0] = fVec8[(IOTA-iConst13)&1023];
			float 	fRec9 = (0 - (0.25f * fVec8[IOTA&1023]));
			float fTemp3 = ((fRec9 + fRec8[1]) + (0.25f * fRec6[1]));
			fVec9[IOTA&1023] = fTemp3;
			fRec6[0] = fVec9[(IOTA-iConst14)&1023];
			float 	fRec7 = (0 - (0.25f * fVec9[IOTA&1023]));
			float fTemp4 = ((fRec7 + fRec6[1]) + (0.25f * fRec4[1]));
			fVec10[IOTA&1023] = fTemp4;
			fRec4[0] = fVec10[(IOTA-iConst15)&1023];
			float 	fRec5 = (0 - (0.25f * fVec10[IOTA&1023]));
			float fTemp5 = ((fRec5 + fRec4[1]) + (0.25f * fRec2[1]));
			fVec11[IOTA&1023] = fTemp5;
			fRec2[0] = fVec11[(IOTA-iConst16)&1023];
			float 	fRec3 = (0 - (0.25f * fVec11[IOTA&1023]));
			output0[i] = (FAUSTFLOAT)((fRec3 + fRec2[1]) + (fSlow0 * fbargraph0));
			// post processing
			fRec2[1] = fRec2[0];
			fRec4[1] = fRec4[0];
			fRec6[1] = fRec6[0];
			fRec8[1] = fRec8[0];
			fRec24[1] = fRec24[0];
			fRec25[1] = fRec25[0];
			fRec22[1] = fRec22[0];
			fRec23[1] = fRec23[0];
			fRec20[1] = fRec20[0];
			fRec21[1] = fRec21[0];
			fRec18[1] = fRec18[0];
			fRec19[1] = fRec19[0];
			fRec16[1] = fRec16[0];
			fRec17[1] = fRec17[0];
			fRec14[1] = fRec14[0];
			fRec15[1] = fRec15[0];
			fRec12[1] = fRec12[0];
			fRec13[1] = fRec13[0];
			fRec10[1] = fRec10[0];
			IOTA = IOTA+1;
			fRec11[1] = fRec11[0];
			fRec0[1] = fRec0[0];
			fRec1[1] = fRec1[0];
		}
	}
};


